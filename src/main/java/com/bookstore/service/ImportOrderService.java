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

    public void createImportOrder(User user, Long supplierId, List<Long> bookIds,
            List<Integer> quantities, List<Double> prices) {

        ImportOrder order = new ImportOrder();
        order.setCreatedAt(LocalDateTime.now());
        order.setCreatedBy(user);
        order.setBookstore(user.getBookstore());

        // Chỉ set 1 lần supplier cho đơn hàng
        order.setSupplier(supplierRepository.findById(supplierId).orElse(null));

        importOrderRepository.save(order);

        for (int i = 0; i < bookIds.size(); i++) {
            ImportOrderItem item = new ImportOrderItem();
            Book book = bookRepository.findById(bookIds.get(i)).orElse(null);
            item.setBook(book);
            item.setImportOrder(order);
            item.setQuantity(quantities.get(i));
            item.setUnitPrice(prices.get(i));

            // Cập nhật tồn kho
            book.setQuantity(book.getQuantity() + quantities.get(i));
            bookRepository.save(book);

            importItemRepository.save(item);
        }
    }

}
