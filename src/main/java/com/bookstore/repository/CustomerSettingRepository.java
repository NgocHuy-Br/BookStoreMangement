package com.bookstore.repository;

import com.bookstore.entity.CustomerSetting;
import com.bookstore.entity.Bookstore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerSettingRepository extends JpaRepository<CustomerSetting, Long> {
    CustomerSetting findByBookstore(Bookstore bookstore);
}
