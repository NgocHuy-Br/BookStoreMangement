package com.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.User;
import com.bookstore.repository.ImportOrderRepository;
import com.bookstore.repository.InvoiceRepository;
import com.bookstore.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private InvoiceRepository invoiceRepo;

    @Autowired
    private ImportOrderRepository importOrderRepo;

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

    public List<User> getAllEmployees() {
        return userRepo.findByRole("EMPLOYEE");
    }


    public List<User> searchEmployeesByUsernameAndBookstore(String keyword, Bookstore bookstore) {
        return userRepository.findByUsernameContainingIgnoreCaseAndRoleAndBookstore(keyword, "EMPLOYEE", bookstore);
    }

    public User getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public void updateUserInfo(User employee) {
        userRepo.save(employee);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);

    }

    public List<User> getEmployeesByBookstore(Bookstore bookstore) {
        return userRepository.findByRoleAndBookstore("EMPLOYEE", bookstore);
    }

    public void saveEmployee(User employee) {
        userRepo.save(employee);
    }

    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean hasRelatedData(User user) {
        return invoiceRepo.existsByUser(user) || importOrderRepo.existsByCreatedBy(user);
    }

    public boolean isUsernameTakenByAnotherUser(String username, Long currentUserId) {
        return userRepository.findByUsername(username)
                .filter(user -> !user.getId().equals(currentUserId))
                .isPresent();
    }

}
