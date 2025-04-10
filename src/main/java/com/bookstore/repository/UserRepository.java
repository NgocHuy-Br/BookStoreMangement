package com.bookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findByRole(String role);

    // Tìm kiếm theo username (không phân biệt hoa thường) và có role nhất định
    List<User> findByUsernameContainingIgnoreCaseAndRole(String username, String role);

    List<User> findByRoleAndBookstore_Id(String role, int bookstoreId);

}
