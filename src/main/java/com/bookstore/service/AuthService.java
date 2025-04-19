package com.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.User;
import com.bookstore.repository.BookstoreRepository;
import com.bookstore.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookstoreRepository bookstoreRepository;

    public String register(User user, String bookstoreName, String bookstoreAddress) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Tên đăng nhập đã tồn tại!";
        }

        Bookstore bookstore = new Bookstore();
        bookstore.setName(bookstoreName);
        bookstore.setAddress(bookstoreAddress);
        bookstore = bookstoreRepository.save(bookstore);

        user.setBookstore(bookstore);
        user.setRole("ADMIN");
        userRepository.save(user);

        return "Đăng ký tài khoản thành công!";
    }
}
