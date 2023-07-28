package com.example.library.service;

import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user) {
        checkIfUserIsNull(user);
        checkIfRoleIsNull(user);
        return userRepository.save(user);
    }

    private static void checkIfUserIsNull(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
    }

    private static void checkIfRoleIsNull(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
    }

    public User findByID(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User update(User user) {
        checkIfUserIsNull(user);
        findByID(user.getId());
        checkIfRoleIsNull(user);
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.delete(findByID(id));
    }
}
