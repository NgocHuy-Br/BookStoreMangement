package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.entity.Bookstore;
import com.bookstore.entity.User;
import com.bookstore.service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("")
    public String listBooks(@RequestParam(required = false) String keyword, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Bookstore bookstore = user.getBookstore();

        List<Book> books = (keyword != null && !keyword.isEmpty()) ? bookService.searchBooks(bookstore, keyword)
                : bookService.getBooksByBookstore(bookstore);

        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        return "admin/book-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin/book-create";
    }

    @PostMapping("/create")
    public String createBook(@ModelAttribute("book") Book book, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        book.setBookstore(user.getBookstore());
        bookService.save(book);
        return "redirect:/book";
    }
    // @GetMapping("/create")
    // public String showCreateForm(Model model, HttpSession session) {
    // User currentUser = (User) session.getAttribute("loggedInUser");
    // if (currentUser == null)
    // return "redirect:/auth/login";

    // Book book = new Book();
    // model.addAttribute("book", book);
    // model.addAttribute("categories", categoryService.getAllCategories());
    // return "admin/book-create";
    // }

    // @PostMapping("/create")
    // public String createBook(@ModelAttribute("book") Book book, HttpSession
    // session) {
    // User currentUser = (User) session.getAttribute("loggedInUser");
    // if (currentUser == null)
    // return "redirect:/auth/login";

    // book.setBookstore(currentUser.getBookstore()); // gắn nhà sách của người đang
    // đăng nhập
    // bookService.save(book);
    // return "redirect:/admin/book";
    // }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getById(id));
        return "admin/book-edit";
    }

    @PostMapping("/edit")
    public String updateBook(@ModelAttribute("book") Book book, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        book.setBookstore(user.getBookstore());
        bookService.save(book);
        return "redirect:/book";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return "redirect:/book";
    }
}
