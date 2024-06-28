package InterKinguin.demo.Exception;

public class AccountNotFoundException extends Exception{
    public AccountNotFoundException(String message){
        super(message);
    }
    public AccountNotFoundException(){
        super("Account was not found.");
    }
}
