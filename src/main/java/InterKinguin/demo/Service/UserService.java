package InterKinguin.demo.Service;

import InterKinguin.demo.Repository.BookRepository;
import InterKinguin.demo.Repository.UserRepository;
import InterKinguin.demo.Entities.Book;
import InterKinguin.demo.Entities.User;
import InterKinguin.demo.Exception.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public final User addUser(User user) throws EmailAlreadyExistsException {
        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new EmailAlreadyExistsException();
        }
        return userRepository.save(user);
    }
    public final String deleteAccount(String userEmail) throws ReturnAllBooksException {
        User user = userRepository.findByEmail(userEmail);
        //check if all books has been returned by this user
        if(!user.getRentedBooks().isEmpty()){
            throw new ReturnAllBooksException();
        }
        userRepository.delete(user);
        return "An account assigned to email address: " + userEmail + " was deleted.";
    }
    public final User findUserByEmail(String userEmail){
        return userRepository.findByEmail(userEmail);
    }
    public final String rentBook(String userEmail, String bookTitle) throws BookNotAvaliableException,
                                                                            BookNotFoundException,
                                                                            AccountNotFoundException {
        User user = userRepository.findByEmail(userEmail);
        if (user != null) {                                  // Check if user exists
            for (Book book : bookRepository.findAll()) {    // Loop through books
                if (book.getTitle().equals(bookTitle)) {   // If book with title was found
                    if (book.getAvailable()) {            // Check availability
                        user.getRentedBooks().add(book); // Add to user rent list
                        book.setAvailable(false);       // Change availability

                        //Set current time to track when book was borrowed
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String formattedNow = now.format(formatter);
                        book.setBookBorrowedOnAndBy("Borrowed By: " + userEmail + ", on: " + formattedNow);
                        user.getBorrowingHistory().add("Borrowed book: " + bookTitle + " on: " + formattedNow);

                        userRepository.save(user);
                        bookRepository.save(book);
                        return "User with email: " + user.getEmail() + " has rented the book: " + bookTitle;
                    } else{
                        throw new BookNotAvaliableException();
                    }
                }
            }
            throw new BookNotFoundException("Book with title: " + bookTitle + " was not found.");  // After looping through all books
        } else {
            throw new AccountNotFoundException("Account with email: " + userEmail + " was not found.");
        }
    }

    public final String returnBook(String userEmail, String bookTitle) throws BookNotFoundException,
                                                                              AccountNotFoundException {
        User user = userRepository.findByEmail(userEmail);
        if(user != null){
            for(Book book : user.getRentedBooks()){
                if(book.getTitle().equals(bookTitle)){
                    user.getRentedBooks().remove(book);
                    book.setAvailable(true);
                    //set current time to track when book was returned
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedNow = now.format(formatter);
                    book.setBookBorrowedOnAndBy("Book was returned by: " + userEmail + ", on: " + formattedNow);
                    user.getBorrowingHistory().add("Returned book: " + bookTitle + " on: " + formattedNow);

                    userRepository.save(user);
                    bookRepository.save(book);
                    return "User with email: " + user.getEmail() + " has returned the book: " + bookTitle;
                }
            }
            throw new BookNotFoundException("Book with title: " + bookTitle + " was not found on user list.");
        }else{
            throw new AccountNotFoundException("Account with email: " + userEmail + " was not found.");
        }
    }
}
