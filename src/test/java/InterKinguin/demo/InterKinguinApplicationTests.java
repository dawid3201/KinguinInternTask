package InterKinguin.demo;

import InterKinguin.demo.APIController.API;
import InterKinguin.demo.Entities.Book;
import InterKinguin.demo.Entities.User;
import InterKinguin.demo.Exception.*;
import InterKinguin.demo.Repository.BookRepository;
import InterKinguin.demo.Repository.UserRepository;
import InterKinguin.demo.Service.BookService;
import InterKinguin.demo.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.when;

@SpringBootTest
class InterKinguinApplicationTests {
	@Mock
	private UserRepository userRepository;
	@Mock
	private BookRepository bookRepository;
	@Mock
	private BookService bookService;
	@Mock private UserService userService;
	private Book book;
	private User user;
	private API api;
	@BeforeEach
	public void setUp(){
		MockitoAnnotations.openMocks(this);
		api = new API(bookService, userService);

		user = new User();
		user.setEmail("test@email.com");

		book = new Book();
		book.setTitle("New book");
		book.setAvailable(true);
	}
	//------------------------------------------------TESTS-FOR-BOOKS-METHODS-------------------------------------------
	@Test
	public void addBookSuccessful(){
		when(bookRepository.findByTitle(book.getTitle())).thenReturn(null);
		ResponseEntity<?> responseEntity = api.addBook(book);
		assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
	}
	@Test
	public void addBookAlreadyExistsException() throws BookAlreadyExistsException {
		when(bookService.addBook(book)).thenThrow(new BookAlreadyExistsException("Book already exists"));

		// Calling the controller method
		ResponseEntity<?> responseEntity = api.addBook(book);

		// Verifying the response status and body
		assertEquals(HttpStatus.CONFLICT.value(), responseEntity.getStatusCode().value());
		assertEquals("Book already exists", responseEntity.getBody());
	}
	@Test
	public void getAvailableBooksSuccessful(){
		List<Book> listOfBooks = new ArrayList<>();
		listOfBooks.add(book);

		when(bookService.getAvailableBooks()).thenReturn(listOfBooks);
		ResponseEntity<List<Book>> responseEntity = api.getAvailableBooks();
		assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
	}
	@Test
	public void getBookByTitleSuccessful() throws BookNotFoundException {
		when(bookService.getBookByTitle("New book")).thenReturn(book);
		ResponseEntity<?> responseEntity = api.getBookByTitle("New book");
		assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
	}
	@Test
	public void getBookByTitleExceptionThrow() throws BookNotFoundException {
		when(bookService.getBookByTitle("Just book")).thenThrow(new BookNotFoundException());
		ResponseEntity<?> responseEntity = api.getBookByTitle("Just book");
		assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCode().value());
		assertEquals("Book was not found", responseEntity.getBody());
	}
	@Test
	public void checkBookAvailabilityTrue() throws BookNotFoundException {
		book.setAvailable(true);

		when(bookService.checkBookAvailability("New book")).thenReturn("Book available.");
		String result = bookService.checkBookAvailability("New book");
		assertEquals("Book available.", result);
	}
	@Test
	public void checkBookAvailabilityFalse() throws BookNotFoundException {
		book.setAvailable(false);
		String expectedOutput = "Book not available. Borrowed By: d@r.com, on: 2024-06-28";

		when(bookService.checkBookAvailability("New book")).thenReturn(expectedOutput);
		String result = bookService.checkBookAvailability("New book");
		assertEquals(expectedOutput, result);
	}
	@Test
	public void checkBookAvailabilitySuccessfulExceptionThrow() throws BookNotFoundException {
		when(bookService.checkBookAvailability("Wrong book title")).thenThrow(new BookNotFoundException("Book was not found"));
		ResponseEntity<String> responseEntity = api.checkBookAvailability("Wrong book title");
		assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCode().value());
		assertEquals("Book was not found", responseEntity.getBody());
	}
	//------------------------------------------------TESTS-FOR-USER-METHODS--------------------------------------------
	@Test
	public void addUserSuccess(){
		when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
		ResponseEntity<?> responseEntity = api.addUser(user);
		assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
	}
	@Test
	public void addUserFail() throws EmailAlreadyExistsException{
		when(userService.addUser(user)).thenThrow(new EmailAlreadyExistsException());

		ResponseEntity<?> responseEntity = api.addUser(user);

		assertEquals(HttpStatus.CONFLICT.value(), responseEntity.getStatusCode().value());
		assertEquals("An account with this email already exists.", responseEntity.getBody());
	}
	@Test
	public void deleteAccountSuccess(){
		when(userRepository.findByEmail("test@email.com")).thenReturn(user);
		ResponseEntity<?> response = api.deleteAccount("test@email.com");
		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
	}
	@Test
	public void deleteAccountFail() throws ReturnAllBooksException {
		String userEmail= "test@email.com";
		//if users has some books rented, account cannot be deleted
		List<Book> bookList = new ArrayList<>();
		bookList.add(book);
		user.setRentedBooks(bookList);

		when(userService.deleteAccount(userEmail)).thenThrow(new ReturnAllBooksException());
		ResponseEntity<?> responseEntity = api.deleteAccount(userEmail);
		assertEquals("Please return all the books before deleting the account.", responseEntity.getBody());
	}
	@Test
	public void testRentBookSuccessful() throws Exception {
		// Mock data
		String userEmail = "test@email.com";
		String bookTitle = "Available Book";

		// Mock repository behavior
		when(userRepository.findByEmail(userEmail)).thenReturn(user);
		ResponseEntity<?> response = api.rentBook(userEmail, bookTitle);

		// Verify the response message
		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
	}
	@Test
	public void rentBookBookNotAvaliable() throws BookNotAvaliableException, BookNotFoundException, AccountNotFoundException {
		String userEmail= "test@email.com";
		String bookTitle = "Book title";

		when(userService.rentBook(userEmail, bookTitle)).thenThrow(new BookNotAvaliableException());
		ResponseEntity<?> responseEntity = api.rentBook(userEmail, bookTitle);
		assertEquals("Book is rented by someone else.", responseEntity.getBody());
	}
	@Test
	public void rentBookBookNotFound() throws BookNotAvaliableException, BookNotFoundException, AccountNotFoundException {
		String userEmail= "test@email.com";
		String bookTitle = "Book title";

		when(userService.rentBook(userEmail, bookTitle)).thenThrow(new BookNotFoundException());
		ResponseEntity<?> responseEntity = api.rentBook(userEmail, bookTitle);
		assertEquals("Book was not found", responseEntity.getBody());
	}
	@Test
	public void rentBookAccountNotFound() throws BookNotAvaliableException, BookNotFoundException, AccountNotFoundException {
		String userEmail= "test@email.com";
		String bookTitle = "Book title";

		when(userService.rentBook(userEmail, bookTitle)).thenThrow(new AccountNotFoundException());
		ResponseEntity<?> responseEntity = api.rentBook(userEmail, bookTitle);
		assertEquals("Account was not found.", responseEntity.getBody());
	}
	@Test
	public void returnBookSuccess(){
		String userEmail = "test@email.com";
		String bookTitle = "Available Book";

		// Mock repository behavior
		when(userRepository.findByEmail(userEmail)).thenReturn(user);
		ResponseEntity<?> response = api.returnBook(userEmail, bookTitle);

		// Verify the response message
		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
	}
	@Test
	public void returnBookBookNotFound() throws BookNotFoundException, AccountNotFoundException {
		String userEmail = "test@email.com";
		String bookTitle = "Available Book";

		// Mock repository behavior
		when(userService.returnBook(userEmail,bookTitle)).thenThrow(new BookNotFoundException());
		ResponseEntity<?> response = api.returnBook(userEmail, bookTitle);

		// Verify the response message
		assertEquals("Book was not found", response.getBody());
	}
	@Test
	public void returnBookAccountNotFound() throws BookNotFoundException, AccountNotFoundException {
		String userEmail = "test@email.com";
		String bookTitle = "Available Book";

		// Mock repository behavior
		when(userService.returnBook(userEmail,bookTitle)).thenThrow(new AccountNotFoundException());
		ResponseEntity<?> response = api.returnBook(userEmail, bookTitle);

		// Verify the response message
		assertEquals("Account was not found.", response.getBody());
	}
}
