package com.bookstore.repository;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.CustomerSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerSettingRepository extends JpaRepository<CustomerSetting, Long> {
    Optional<CustomerSetting> findByBookstore(Bookstore bookstore);
}
