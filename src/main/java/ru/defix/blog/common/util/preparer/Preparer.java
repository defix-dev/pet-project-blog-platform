package ru.defix.blog.common.util.preparer;

import org.jetbrains.annotations.NotNull;
import ru.defix.blog.common.util.preparer.exception.PreparerInnerException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.*;
import java.util.stream.Collectors;

public final class Preparer {
    private record SearchPath(String fieldName, Class<?> clazz) {
    }

    private final static Set<Class<?>> primitiveTypes = Set.of(
            String.class,
            Integer.class,
            Long.class,
            Double.class,
            Float.class,
            Boolean.class,
            Byte.class,
            Short.class,
            Character.class
    );

    public static <ST, TT> List<TT> prepareCollection(List<ST> data, Class<TT> targetClazz) {
        return data.stream().map(value -> prepare(value, targetClazz)).toList();
    }

    public static <ST, TT> TT prepare(ST data, Class<TT> targetClazz) {
        if (!targetClazz.isRecord()) throw new PreparerInnerException("Preparer support only a record");

        Constructor<?> constructor;
        RecordComponent[] targetFields = targetClazz.getRecordComponents();

        try {
            constructor = getDeclaredConstructor(targetClazz, targetFields);
        } catch (NoSuchMethodException e) {
            throw new PreparerInnerException("Failed to get constructor");
        }

        Class<?> sourceClazz = data.getClass();
        Map<String, Field> sourceFieldsMap = getProcessedFields(sourceClazz);

        Object[] args = new Object[targetFields.length];

        for (int i = 0; i < targetFields.length; i++) {
            RecordComponent field = targetFields[i];
            PreparerSearchPath searchPath = field.getAnnotation(PreparerSearchPath.class);
            boolean isPrimitive = field.getType().isPrimitive() || primitiveTypes.contains(field.getType());

            try {
                Field curField;
                if (sourceFieldsMap.containsKey(field.getName()) && (curField = sourceFieldsMap.get(field.getName())).getType().isAssignableFrom(field.getType())
                        && searchPath == null && isPrimitive) {
                    args[i] = curField.get(data);
                    continue;
                } else if (searchPath == null && !isPrimitive) {
                    args[i] = prepare(data, field.getType());
                    continue;
                }
            } catch (IllegalAccessException e) {
                throw new PreparerInnerException("Failed to access field " + field.getName());
            }

            if (searchPath == null || searchPath.value().isEmpty())
                throw new PreparerInnerException("Field not found: " + field.getName());

            SearchPath[] paths = parseSearchPathLabel(searchPath.value());
            if (paths.length == 0)
                throw new PreparerInnerException("Field not found: " + field.getName());

            Field rootField = sourceFieldsMap.get(paths[0].fieldName());

            args[i] = prepare(searchFieldValue(rootField, data, paths), field.getType());
        }

        try {
            Object output = constructor.newInstance(args);
            if (targetClazz.isAssignableFrom(output.getClass())) {
                @SuppressWarnings("unchecked")
                TT result = (TT) output;
                return result;
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new PreparerInnerException("Failed to create instance of " + targetClazz.getName());
        }

        throw new PreparerInnerException("Unresolved error");
    }

    private static @NotNull Object searchFieldValue(Field rootField, Object rootObject, SearchPath[] paths) {
        for (int j = 0; j < paths.length - 1; j++) {
            if (rootField == null || !rootField.getType().isAssignableFrom(paths[j].clazz()))
                throw new PreparerInnerException("Field not found in path: " + paths[j].fieldName());

            try {
                rootField.setAccessible(true);
                rootObject = rootField.get(rootObject);
                if (rootObject == null)
                    throw new PreparerInnerException("Null value in path at: " + paths[j].fieldName());

                rootField = rootObject.getClass().getDeclaredField(paths[j + 1].fieldName());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new PreparerInnerException("Field not found or inaccessible in path: " + paths[j].fieldName());
            }
        }

        try {
            rootField.setAccessible(true);
            return rootField.get(rootObject);
        } catch (IllegalAccessException e) {
            throw new PreparerInnerException("Failed to access field " + paths[paths.length - 1].fieldName());
        }
    }

    private static <TT> @NotNull Constructor<TT> getDeclaredConstructor(Class<TT> targetClazz, RecordComponent[] targetFields) throws NoSuchMethodException {
        return targetClazz.getDeclaredConstructor(
                Arrays.stream(targetFields).map(RecordComponent::getType)
                        .toArray(Class[]::new)
        );
    }

    private static @NotNull Map<String, Field> getProcessedFields(Class<?> sourceClazz) {
        return Arrays.stream(sourceClazz.getDeclaredFields()).map(field -> {
                    field.setAccessible(true);
                    return Map.entry(field.getName(), field);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static SearchPath[] parseSearchPathLabel(String label) {
        return Arrays.stream(label.split(";")).map(fullSP -> {
            String[] details = fullSP.split(":");
            if (details.length != 2)
                throw new PreparerInnerException("Invalid search path format: " + fullSP);
            try {
                return new SearchPath(details[0], Class.forName(details[1]));
            } catch (ClassNotFoundException e) {
                throw new PreparerInnerException("Failed to find class " + details[1]);
            }
        }).toArray(SearchPath[]::new);
    }
}