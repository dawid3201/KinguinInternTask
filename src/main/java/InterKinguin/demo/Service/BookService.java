package InterKinguin.demo.Service;

import InterKinguin.demo.Entities.User;
import InterKinguin.demo.Exception.BookAlreadyExistsException;
import InterKinguin.demo.Exception.ReturnAllBooksException;
import InterKinguin.demo.Repository.BookRepository;
import InterKinguin.demo.Entities.Book;
import InterKinguin.demo.Exception.BookNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public final Book addBook(Book book) throws BookAlreadyExistsException {
        Book book1 = bookRepository.findByTitle(book.getTitle());
        if(book1 == null){
            book.setAvailable(true);
            return bookRepository.save(book);
        }else{
            throw new BookAlreadyExistsException();
        }
    }
    public final List<Book> getAvailableBooks(){
        List<Book> bookList = new ArrayList<>();
        for(Book book : bookRepository.findAll()){
            if(book.getAvailable()){
                bookList.add(book);
            }
        }
        return bookList;
    }
    public final List<Book> getAllBooks(){
        return new ArrayList<>(bookRepository.findAll());
    }
    public final Book getBookByTitle(String bookTitle) throws BookNotFoundException{
        for(Book book : bookRepository.findAll()){
            if(book.getTitle().equals(bookTitle)){
                return book;
            }
        }
        throw new BookNotFoundException();
    }
    public final String checkBookAvailability(String bookTitle) throws BookNotFoundException {
        Book book = bookRepository.findByTitle(bookTitle);
        if(book != null){
            if(book.getAvailable()){
                return "Book available.";
            }else{
                return "Book not available." + " " + book.getBookBorrowedOnAndBy();
            }
        }else{
            throw new BookNotFoundException();
        }
    }
}
