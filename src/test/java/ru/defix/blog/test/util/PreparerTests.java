package ru.defix.blog.test.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.defix.blog.common.util.preparer.Preparer;
import ru.defix.blog.common.util.preparer.PreparerSearchPath;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Util")
public class PreparerTests {
    public record Employee(String firstName, String lastName, int age) {}
    public record EmployeeDto(String firstName) {}

    @Test
    public void testPrepareEmployeeToDto_Success() {
        EmployeeDto dto = Preparer.prepare(new Employee("Kosta", "Malik", 123), EmployeeDto.class);

        assertNotNull(dto);
        assertEquals("Kosta", dto.firstName());
    }

    public record UserMeta(String firstName, String lastName, int age) {}
    public record User(int id, String email, String firstName, String lastName, int age) {}
    public record UserDto(int id, String email, UserMeta meta) {}

    @Test
    public void testPrepareUserToDto_Success() {
        UserDto dto = Preparer.prepare(new User(1, "defix@email.com", "Kosta", "Malik", 123),
                UserDto.class);

        assertNotNull(dto);
        assertEquals(1, dto.id());
        assertEquals("defix@email.com", dto.email());
        assertNotNull(dto.meta());
        assertEquals("Kosta", dto.meta().firstName());
        assertEquals("Malik", dto.meta().lastName());
        assertEquals(123, dto.meta().age());
    }

    public record Role(int id, String name) {}
    public record RoleInfo(String name) {}
    public record User_2(int id, String email, String firstName, String lastName, int age, Role role) {}
    public record UserDto_2(int id, String email, @PreparerSearchPath("role:ru.defix.blog.test.util.PreparerTests$Role") RoleInfo role) {}

    @Test
    public void testPrepareUserToDto_Success_2() {
        UserDto_2 dto = Preparer.prepare(new User_2(1, "defix@email.com", "Kosta", "Malik", 123, new Role(1, "ADMIN")),
                UserDto_2.class);

        assertNotNull(dto);
        assertEquals(1, dto.id());
        assertEquals("defix@email.com", dto.email());
        assertNotNull(dto.role());
        assertEquals("ADMIN", dto.role().name());
    }
}
