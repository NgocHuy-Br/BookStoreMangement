package com.bookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findByRole(String role);

    // Tìm kiếm theo username (không phân biệt hoa thường) và có role nhất định
    List<User> findByUsernameContainingIgnoreCaseAndRole(String username, String role);

    List<User> findByUsernameContainingIgnoreCaseAndRoleAndBookstore(String username, String role, Bookstore bookstore);

    List<User> findByRoleAndBookstore_Id(String role, int bookstoreId);

    List<User> findByRoleAndBookstore(String role, Bookstore bookstore);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.username LIKE %:keyword%")
    List<User> searchByRoleAndUsername(String role, String keyword);

}
