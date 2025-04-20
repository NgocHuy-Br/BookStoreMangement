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

        // Tính số lượng bán ra
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
        return "book/book-list"; // nếu view dùng chung
    }

    @GetMapping("/create")
    public String showCreateBookForm(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Book book = new Book();
        book.setInventory(0); // số lượng mặc định

        // Lấy danh sách danh mục theo bookstore của admin đang đăng nhập
        List<Category> categories = categoryService.getCategoriesByBookstore(currentUser.getBookstore());

        model.addAttribute("book", book);
        model.addAttribute("categories", categories);
        return "book/book-create";
    }

    @PostMapping("create")
    public String createBook(@ModelAttribute("book") Book book, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        book.setBookstore(user.getBookstore());
        bookService.save(book);
        return "redirect:/book";
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
        return "redirect:/book";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Book book = bookService.getById(id);
        if (book != null && book.getBookstore().getId().equals(currentUser.getBookstore().getId())) {
            if (bookService.isBookUsedInImportOrder(id)) {
                // Nếu đã tồn tại trong đơn hàng thì không xóa, gắn thông báo lỗi
                session.setAttribute("bookDeleteError", "Sách đã nằm trong đơn hàng. Xóa không thành công!");
            } else {
                bookService.deleteById(id);
            }
        }
        return "redirect:/book";
    }

}
