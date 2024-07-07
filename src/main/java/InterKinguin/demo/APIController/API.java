package InterKinguin.demo.APIController;

import InterKinguin.demo.Entities.Book;
import InterKinguin.demo.Entities.User;
import InterKinguin.demo.Exception.*;
import InterKinguin.demo.Service.BookService;
import InterKinguin.demo.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookAPI")
@AllArgsConstructor
public class API {
    private BookService bookService;
    private UserService userService;
    //---------------------------------------Book-Methods-------------------------------

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) throws BookAlreadyExistsException {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @GetMapping("/books/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/books/byTitle")
    public ResponseEntity<Book> getBookByTitle(@RequestParam("bookTitle") String bookTitle) throws BookNotFoundException {
        return ResponseEntity.ok(bookService.getBookByTitle(bookTitle));
    }

    @GetMapping("/books/checkAvailability")
    public ResponseEntity<String> checkBookAvailability(@RequestParam("bookTitle") String bookTitle) throws BookNotFoundException {
        return ResponseEntity.ok(bookService.checkBookAvailability(bookTitle));
    }
    //--------------------------------------------User-Methods----------------------------

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) throws EmailAlreadyExistsException {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @PostMapping("/users/rentBook")
    public ResponseEntity<String> rentBook(@RequestParam("userEmail") String userEmail,
                                           @RequestParam("bookTitle") String bookTitle) throws BookNotAvaliableException, BookNotFoundException, AccountNotFoundException {
        return ResponseEntity.ok(userService.rentBook(userEmail, bookTitle));
    }

    @PostMapping("/users/returnBook")
    public ResponseEntity<String> returnBook(@RequestParam("userEmail") String userEmail,
                                             @RequestParam("bookTitle") String bookTitle) throws BookNotFoundException, AccountNotFoundException {
        return ResponseEntity.ok(userService.returnBook(userEmail, bookTitle));
    }

    @DeleteMapping("/users")
    public ResponseEntity<String> deleteAccount(@RequestParam("userEmail") String userEmail) throws ReturnAllBooksException {
        return ResponseEntity.ok(userService.deleteAccount(userEmail));
    }

    @GetMapping("/users/byEmail")
    public ResponseEntity<User> findUserByEmail(@RequestParam("userEmail") String userEmail) {
        return ResponseEntity.ok(userService.findUserByEmail(userEmail));
    }
}
