package ru.defix.blog.user.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.defix.blog.db.entity.Role;
import ru.defix.blog.db.entity.User;
import ru.defix.blog.db.repository.UserRepository;
import ru.defix.blog.user.service.dto.UserSaveParams;

import java.util.Collections;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public void save(UserSaveParams params) {
        User user = new User();
        user.setUsername(params.username());
        user.setEmail(params.email());
        user.setPassword(encoder.encode(params.password()));
        user.setRoles(Collections.singleton(User.defaultRole));

        userRepository.save(user);
    }
}
