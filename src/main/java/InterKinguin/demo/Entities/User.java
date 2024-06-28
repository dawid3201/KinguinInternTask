package InterKinguin.demo.Entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class User {
    @Id
    private String email;
    private String fullName;
    private String password;
    List<Book> rentedBooks = new ArrayList<>();
    List<String> borrowingHistory = new ArrayList<>();
}
