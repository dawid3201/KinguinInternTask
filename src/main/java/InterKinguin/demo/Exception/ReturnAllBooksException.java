package InterKinguin.demo.Exception;

public class ReturnAllBooksException extends Exception{

    public ReturnAllBooksException(String message){
        super(message);
    }
    public ReturnAllBooksException(){
        super("Please return all the books before deleting the account.");
    }
}
