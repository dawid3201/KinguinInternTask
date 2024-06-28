package InterKinguin.demo.Exception;

public class BookNotFoundException extends Exception{
    public BookNotFoundException(String message){
        super(message);
    }
    public BookNotFoundException(){
        super("Book was not found");
    }
}
