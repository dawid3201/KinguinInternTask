package InterKinguin.demo.Exception;

public class BookNotAvaliableException extends Exception {
    public BookNotAvaliableException(String message){
        super(message);
    }
    public BookNotAvaliableException(){
        super("Book is rented by someone else.");
    }
}
