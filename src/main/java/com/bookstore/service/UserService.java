package com.bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> searchAndSort(String keyword, boolean ascending) {
        if (ascending) {
            return userRepository.findByUserNameContainingIgnoreCaseOrderByUserNameAsc(keyword);
        } else {
            return userRepository.findByUserNameContainingIgnoreCaseOrderByUserNameDesc(keyword);
        }
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
