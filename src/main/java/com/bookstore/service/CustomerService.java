package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private CustomerSettingRepository settingRepo;

    // @Autowired
    // private InvoiceRepository invoiceRepo;

    public List<Customer> getCustomersByBookstore(Bookstore bookstore) {
        return customerRepo.findByBookstore(bookstore);
    }

    public CustomerSetting getOrCreateSetting(Bookstore bookstore) {
        return settingRepo.findByBookstore(bookstore).orElseGet(() -> {
            CustomerSetting setting = new CustomerSetting();
            setting.setBookstore(bookstore);
            setting.setDiscountRate(0.0);
            setting.setRequiredPointsForMembership(0);
            return settingRepo.save(setting);
        });
    }

    public void updateSetting(CustomerSetting setting) {
        settingRepo.save(setting);
    }

    // public boolean canDeleteCustomer(Customer customer) {
    // return invoiceRepo.findByCustomer(customer).isEmpty();
    // }

    public boolean canDeleteCustomer(Customer customer) {
        // Nếu Customer chưa có hóa đơn nào => được xóa
        return customer.getInvoices() == null || customer.getInvoices().isEmpty();
    }

    public void deleteCustomer(Customer customer) {
        customerRepo.delete(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepo.findById(id).orElse(null);
    }

    public List<Customer> searchByName(Bookstore bookstore, String keyword) {
        return customerRepo.findByBookstoreAndNameContainingIgnoreCase(bookstore, keyword);
    }

    public void save(Customer customer) {
        customerRepo.save(customer);
    }

}
