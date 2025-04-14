package com.bookstore.service;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.CustomerSetting;
import com.bookstore.repository.CustomerSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerSettingService {

    @Autowired
    private CustomerSettingRepository customerSettingRepository;

    public CustomerSetting getByBookstore(Bookstore bookstore) {
        return customerSettingRepository.findByBookstore(bookstore);
    }

    public void save(CustomerSetting setting) {
        customerSettingRepository.save(setting);
    }
}
