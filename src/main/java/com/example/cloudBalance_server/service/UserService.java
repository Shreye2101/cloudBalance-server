package com.example.cloudBalance_server.service;

import com.example.cloudBalance_server.dto.UserRequestDTO;
import com.example.cloudBalance_server.dto.UserResponseDTO;
import com.example.cloudBalance_server.entity.User;
import com.example.cloudBalance_server.exception.ResourceNotFoundException;
import com.example.cloudBalance_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String addUser(UserRequestDTO dto) {

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {

            throw new IllegalArgumentException("Password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        repo.save(user);

        return "Added user successfully";
    }

    public List<User> getAllUsers() {

        return repo.findAll();
    }

    public User updateUser(Long id, UserRequestDTO userDto) {

        User existingUser = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setRole(userDto.getRole());

        return repo.save(existingUser);
    }
}


