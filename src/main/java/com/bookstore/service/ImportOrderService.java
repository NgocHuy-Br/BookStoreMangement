package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ImportOrderService {

    @Autowired
    private ImportOrderRepository importOrderRepository;
    @Autowired
    private ImportOrderItemRepository importItemRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    public void createMultipleSupplierOrder(User user, List<Long> bookIds, List<Long> supplierIds,
            List<Integer> quantities, List<Double> prices) {

        for (int i = 0; i < bookIds.size(); i++) {
            ImportOrder order = new ImportOrder();
            order.setCreatedAt(LocalDateTime.now());
            order.setCreatedBy(user);
            order.setBookstore(user.getBookstore());

            Supplier supplier = supplierRepository.findById(supplierIds.get(i)).orElse(null);
            order.setSupplier(supplier);

            importOrderRepository.save(order);

            Book book = bookRepository.findById(bookIds.get(i)).orElse(null);

            ImportOrderItem item = new ImportOrderItem();
            item.setImportOrder(order);
            item.setBook(book);
            item.setQuantity(quantities.get(i));
            item.setUnitPrice(prices.get(i));

            book.setQuantity(book.getQuantity() + quantities.get(i));
            bookRepository.save(book);

            importItemRepository.save(item);
        }
    }
}
