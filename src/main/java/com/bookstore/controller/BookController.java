package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.entity.Bookstore;
import com.bookstore.entity.Category;
import com.bookstore.entity.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;
import com.bookstore.service.CategoryService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String listBooks(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer top,
            Model model,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        Bookstore bookstore = currentUser.getBookstore();

        List<Book> books;

        if (top != null && top > 0) {
            books = bookService.findTopSellingBooks(bookstore, top);
            model.addAttribute("top", top);
        } else if (keyword != null && !keyword.isBlank()) {
            books = bookService.searchBooks(bookstore, keyword);
            model.addAttribute("keyword", keyword);
        } else {
            books = bookService.getBooksByBookstore(bookstore);
        }

        // Tính số lượng đã bán ra
        for (Book book : books) {
            int soldQty = bookService.getSoldQuantity(book);
            book.setSoldQuantity(soldQty);
        }

        model.addAttribute("books", books);
        return "book/book-list";
    }

    @GetMapping("/inventory-asc")
    public String viewBooksSortedByInventory(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        List<Book> books = bookRepo.findByBookstoreOrderByInventoryAsc(user.getBookstore());
        model.addAttribute("books", books);
        return "book/book-list";
    }

    @GetMapping("/create")
    public String showCreateBookForm(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Book book = new Book();
        book.setInventory(0); // số lượng mặc định 0

        // Lấy danh sách danh mục theo bookstore của admin đang đăng nhập
        List<Category> categories = categoryService.getCategoriesByBookstore(currentUser.getBookstore());

        model.addAttribute("book", book);
        model.addAttribute("categories", categories);
        return "book/book-create";
    }

    @PostMapping("create")
    public String createBook(@ModelAttribute("book") Book book, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        book.setBookstore(user.getBookstore());
        bookService.save(book);

        // Gán lại model để ở lại trang
        List<Category> categories = categoryService.getCategoriesByBookstore(user.getBookstore());
        model.addAttribute("book", new Book()); // reset form
        model.addAttribute("categories", categories);
        model.addAttribute("bookMessage", "Thêm sách mới thành công !");
        return "book/book-create";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        Book book = bookService.getById(id);

        model.addAttribute("book", book);

        // Thêm danh sách category hiện có trong nhà sách
        List<Category> categories = categoryService.getCategoriesByBookstore(currentUser.getBookstore());
        model.addAttribute("categories", categories);

        return "book/book-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@ModelAttribute("book") Book book, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        book.setBookstore(user.getBookstore());
        bookService.save(book);
        session.setAttribute("bookMessage", "Cập nhật sách thành công !");
        return "redirect:/book";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("loggedInUser");

        Book book = bookService.getById(id);
        if (book != null && book.getBookstore().getId().equals(currentUser.getBookstore().getId())) {
            if (bookService.isBookUsedInImportOrder(id)) {
                session.setAttribute("bookMessage", "Sách đã nằm trong đơn hàng. Xóa không thành công !");
            } else {
                bookService.deleteById(id);
                session.setAttribute("bookMessage", "Xóa sách thành công!");
            }
        }
        return "redirect:/book";
    }

}
