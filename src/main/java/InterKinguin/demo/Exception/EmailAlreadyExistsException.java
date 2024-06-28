package InterKinguin.demo.Exception;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException(String message){
        super(message);
    }
    public EmailAlreadyExistsException(){
        super("An account with this email already exists.");
    }
}
