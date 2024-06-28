package InterKinguin.demo.Exception;

public class BookAlreadyExistsException extends Exception{
    public BookAlreadyExistsException(String message){
        super(message);
    }
    public BookAlreadyExistsException(){
        super("Book with this title already exists on database.");
    }
}
