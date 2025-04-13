package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.*;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;

import java.io.ByteArrayOutputStream;

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

    // public void createImportOrder(User user, Long supplierId, List<Long> bookIds,
    // List<Integer> quantities, List<Double> prices) {

    // ImportOrder order = new ImportOrder();
    // order.setCreatedAt(LocalDateTime.now());
    // order.setCreatedBy(user);
    // order.setBookstore(user.getBookstore());

    // // Chỉ set 1 lần supplier cho đơn hàng
    // order.setSupplier(supplierRepository.findById(supplierId).orElse(null));

    // importOrderRepository.save(order);

    // for (int i = 0; i < bookIds.size(); i++) {
    // ImportOrderItem item = new ImportOrderItem();
    // Book book = bookRepository.findById(bookIds.get(i)).orElse(null);
    // item.setBook(book);
    // item.setImportOrder(order);
    // item.setQuantity(quantities.get(i));
    // item.setUnitPrice(prices.get(i));

    // // Cập nhật tồn kho
    // book.setQuantity(book.getQuantity() + quantities.get(i));
    // bookRepository.save(book);

    // importItemRepository.save(item);
    // }
    // }
    // Sửa return type thành ImportOrder
    public ImportOrder createImportOrder(User user, Long supplierId, List<Long> bookIds,
            List<Integer> quantities, List<Double> prices) {

        ImportOrder order = new ImportOrder();
        order.setCreatedAt(LocalDateTime.now());
        order.setCreatedBy(user);
        order.setBookstore(user.getBookstore());

        order.setSupplier(supplierRepository.findById(supplierId).orElse(null));
        importOrderRepository.save(order);

        for (int i = 0; i < bookIds.size(); i++) {
            ImportOrderItem item = new ImportOrderItem();
            Book book = bookRepository.findById(bookIds.get(i)).orElse(null);
            item.setBook(book);
            item.setImportOrder(order);
            item.setQuantity(quantities.get(i));
            item.setUnitPrice(prices.get(i));

            book.setQuantity(book.getQuantity() + quantities.get(i));
            bookRepository.save(book);

            importItemRepository.save(item);
        }

        return order; // 🔥 Quan trọng!
    }

    // Hàm xuất PDF
    // public byte[] exportImportOrderToPDF(ImportOrder order, List<ImportOrderItem>
    // items) {
    // try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
    // PdfWriter writer = new PdfWriter(baos);
    // PdfDocument pdf = new PdfDocument(writer);
    // Document doc = new Document(pdf);

    // doc.add(new Paragraph("PHIẾU NHẬP HÀNG")
    // .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

    // doc.add(new Paragraph("Mã đơn hàng: " + order.getId()));
    // doc.add(new Paragraph("Thời gian: " + order.getCreatedAt()));
    // doc.add(new Paragraph("Nhân viên: " + order.getCreatedBy().getUsername()));
    // doc.add(new Paragraph("Nhà cung cấp: " + order.getSupplier().getName()));
    // doc.add(new Paragraph(" "));

    // Table table = new Table(5);
    // table.addHeaderCell("STT");
    // table.addHeaderCell("Tên sách");
    // table.addHeaderCell("Tác giả");
    // table.addHeaderCell("Số lượng");
    // table.addHeaderCell("Đơn giá");

    // int index = 1;
    // double total = 0;
    // for (ImportOrderItem item : items) {
    // table.addCell(String.valueOf(index++));
    // table.addCell(item.getBook().getTitle());
    // table.addCell(item.getBook().getAuthor());
    // table.addCell(String.valueOf(item.getQuantity()));
    // table.addCell(String.valueOf(item.getUnitPrice()));
    // total += item.getQuantity() * item.getUnitPrice();
    // }

    // doc.add(table);
    // doc.add(new Paragraph("\nTổng tiền: " + total + " VND").setBold());
    // doc.close();

    // return baos.toByteArray();
    // } catch (Exception e) {
    // e.printStackTrace();
    // return null;
    // }
    // }
    public byte[] exportImportOrderToPDF(ImportOrder order, List<ImportOrderItem> items, double vatRate) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            // Font hỗ trợ tiếng Việt
            PdfFont font = PdfFontFactory.createFont(
                    "src/main/resources/static/fonts/ARIAL.ttf", PdfEncodings.IDENTITY_H);
            doc.setFont(font);

            Bookstore bookstore = order.getBookstore();
            doc.add(new Paragraph("Nhà sách: " + bookstore.getName())
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("Địa chỉ: " + bookstore.getAddress())
                    .setItalic().setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("\nPHIẾU NHẬP HÀNG")
                    .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("\nMã đơn: " + order.getId()));
            doc.add(new Paragraph("Nhà cung cấp hàng: " + order.getSupplier().getName()));

            doc.add(new Paragraph("\nCHI TIẾT ĐƠN HÀNG")
                    .setBold().setTextAlignment(TextAlignment.CENTER));

            // Thiết lập độ rộng cột theo tỷ lệ %
            float[] columnWidths = { 5f, 30f, 15f, 20f, 15f, 15f };
            Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

            table.addHeaderCell(new Cell().add(new Paragraph("STT").setBold().setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(
                    new Cell().add(new Paragraph("Tên sách").setBold().setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(
                    new Cell().add(new Paragraph("Tác giả").setBold().setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(
                    new Cell().add(new Paragraph("Danh mục").setBold().setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(
                    new Cell().add(new Paragraph("Đơn giá").setBold().setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(
                    new Cell().add(new Paragraph("Số lượng").setBold().setTextAlignment(TextAlignment.CENTER)));

            int index = 1;
            double total = 0;
            for (ImportOrderItem item : items) {
                table.addCell(String.valueOf(index++)).setTextAlignment(TextAlignment.CENTER);
                table.addCell(item.getBook().getTitle());
                table.addCell(item.getBook().getAuthor());
                table.addCell(item.getBook().getCategory().getName());
                table.addCell(String.format("%,.0f", item.getUnitPrice()));
                table.addCell(String.valueOf(item.getQuantity()));
                total += item.getQuantity() * item.getUnitPrice();
            }
            doc.add(table);

            double vatAmount = total * vatRate / 100;
            double grandTotal = total + vatAmount;

            doc.add(new Paragraph("\nTổng cộng: " + String.format("%,.0f", total) + " VND").setBold());
            doc.add(new Paragraph("Thuế VAT: " + vatRate + "%").setBold());
            doc.add(new Paragraph("Thành tiền: " + String.format("%,.0f", grandTotal) + " VND").setBold());

            LocalDateTime created = order.getCreatedAt();
            doc.add(new Paragraph("\n\nNgày " + created.getDayOfMonth()
                    + " Tháng " + created.getMonthValue()
                    + " Năm " + created.getYear()));
            doc.add(new Paragraph("Người lập: " + order.getCreatedBy().getId()));

            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
