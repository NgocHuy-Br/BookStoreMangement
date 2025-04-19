package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import com.bookstore.util.ImportOrderPdfUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class ImportOrderService {

    @Autowired
    private ImportOrderRepository orderRepo;
    @Autowired
    private ImportOrderItemRepository itemRepo;
    @Autowired
    private SupplierRepository supplierRepo;
    @Autowired
    private BookRepository bookRepo;

    public List<ImportOrder> filterOrders(Bookstore bookstore, String supplierName, LocalDate from, LocalDate to) {
        List<ImportOrder> allOrders = orderRepo.findAll()
                .stream()
                .filter(order -> order.getBookstore().getId().equals(bookstore.getId()))
                .filter(order -> supplierName == null || supplierName.isBlank()
                        || order.getSupplier().getName().toLowerCase().contains(supplierName.toLowerCase()))
                .filter(order -> {
                    LocalDate date = order.getCreatedAt().toLocalDate();
                    return (from == null || !date.isBefore(from)) && (to == null || !date.isAfter(to));
                })
                .collect(Collectors.toList());

        // return allOrders;
        return allOrders.stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .collect(Collectors.toList());

    }

    public double calculateTotalWithVAT(List<ImportOrder> orders) {
        double total = 0;
        for (ImportOrder order : orders) {
            List<ImportOrderItem> items = itemRepo.findByImportOrder(order);
            double sum = items.stream()
                    .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                    .sum();

            // Nếu muốn lấy VAT từ order sau này: có thể lưu vào order.getVatRate()
            total += sum * 1.0; // tạm chưa cộng VAT vì chưa lưu theo đơn
        }
        return total;
    }

    public ImportOrder createImportOrder(User user, Long supplierId, List<Long> bookIds, List<Integer> qtys,
            List<Double> prices) {
        ImportOrder order = new ImportOrder();
        order.setBookstore(user.getBookstore());
        order.setCreatedBy(user);
        order.setCreatedAt(java.time.LocalDateTime.now());
        order.setSupplier(supplierRepo.findById(supplierId).orElse(null));
        orderRepo.save(order);

        for (int i = 0; i < bookIds.size(); i++) {
            Book book = bookRepo.findById(bookIds.get(i)).orElse(null);
            ImportOrderItem item = new ImportOrderItem();
            item.setBook(book);
            item.setImportOrder(order);
            item.setQuantity(qtys.get(i));
            item.setUnitPrice(prices.get(i));
            itemRepo.save(item);

            // update tồn kho
            book.setInventory(book.getInventory() + qtys.get(i));
            bookRepo.save(book);
        }

        return order;
    }

    public double sumTotalOfOrders(List<ImportOrder> orders) {
        return orders.stream().mapToDouble(order -> itemRepo.findByImportOrder(order).stream()
                .mapToDouble(i -> i.getQuantity() * i.getUnitPrice()).sum()).sum();
    }

    public byte[] exportImportOrderToPDF(ImportOrder order, List<ImportOrderItem> items, double vat) {
        return ImportOrderPdfUtil.exportImportOrderPDF(order, items, vat);
    }
}
