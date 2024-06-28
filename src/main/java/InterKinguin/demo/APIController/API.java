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
    public ResponseEntity<?> addBook(@RequestBody Book book){
        try{
        //add book if it doesn't already exist
            return ResponseEntity.ok(this.bookService.addBook(book));
        }catch (BookAlreadyExistsException e){
        //throw exception if book is on database
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping("/books/available")
    public ResponseEntity<List<Book>> getAvailableBooks(){
    //display all books with available status
        return ResponseEntity.ok((this.bookService.getAvailableBooks()));
    }
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(){
    //display all books 
        return ResponseEntity.ok((this.bookService.getAllBooks()));
    }

    @GetMapping("/books/byTitle")
    public ResponseEntity<?> getBookByTitle(@RequestParam("bookTitle") String bookTitle){
    //find book by it's title
        try{
            return ResponseEntity.ok(bookService.getBookByTitle(bookTitle));
        }catch (BookNotFoundException e){
        //throw exception if not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/books/checkAvailability")
    public ResponseEntity<String> checkBookAvailability(@RequestParam("bookTitle") String bookTitle){
    //check if specific book is avaliable
        try{
            return ResponseEntity.ok(bookService.checkBookAvailability(bookTitle));
        }catch (BookNotFoundException e){
        //throw exception if not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //--------------------------------------------User-Methods----------------------------
    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody User user){
    //add user with specific email 
        try{
            return ResponseEntity.ok(userService.addUser(user));
        }catch (EmailAlreadyExistsException e){
        //if it already is on database, throw exception
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PostMapping("/users/rentBook")//this method registers when the book was rented and by who and saved it on database
    public ResponseEntity<String> rentBook(@RequestParam("userEmail") String userEmail,
                                      @RequestParam("bookTitle") String bookTitle){
                                      //allow users to rent book
        try{
            return ResponseEntity.ok(userService.rentBook(userEmail, bookTitle));
        }catch (BookNotAvaliableException e){
        //if book is rented by someone throw exception
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (BookNotFoundException | AccountNotFoundException e){
        //if book or user account were not found throw exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/users/returnBook")//this method registers when the book was returned and by who and saved it on database
    public ResponseEntity<String> returnBook(@RequestParam("userEmail") String userEmail,
                                        @RequestParam("bookTitle") String bookTitle){
                                        //allow users to return book
        try{
            return ResponseEntity.ok(userService.returnBook(userEmail, bookTitle));
        }catch (BookNotFoundException | AccountNotFoundException e){
        //if book or user account were not found throw exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteAccount(@RequestParam("userEmail") String userEmail){
    //allow users to delete their account
        try{
            return ResponseEntity.ok(userService.deleteAccount(userEmail));
        }catch (ReturnAllBooksException e){
        //if they are renting some books, throw exception
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping("/users/ByEmail")
    public ResponseEntity<User> findUserByEmail(@RequestParam("userEmail") String userEmail){
    //find user by their email
        return ResponseEntity.ok(userService.findUserByEmail(userEmail));
    }
}
