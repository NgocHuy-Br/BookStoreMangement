package com.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    @Autowired
    private UserRepository userRepo;

    public List<User> getAllEmployees() {
        return userRepo.findByRole("EMPLOYEE");
    }

    public List<User> searchEmployeesByUsername(String keyword) {
        return userRepo.findByUsernameContainingIgnoreCaseAndRole(keyword, "EMPLOYEE");
    }

    public User getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public void updateUserInfo(User employee) {
        // Bạn có thể cập nhật các field cần thiết (không bao gồm password nếu không cập
        // nhật)
        userRepo.save(employee);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);

    }

    public List<User> getEmployeesByBookstore(int bookstoreId) {
        return userRepo.findByRoleAndBookstore_Id("EMPLOYEE", bookstoreId);
    }

    public void saveEmployee(User employee) {
        userRepo.save(employee);
    }

}
