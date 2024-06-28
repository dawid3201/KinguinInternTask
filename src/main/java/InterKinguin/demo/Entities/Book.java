package InterKinguin.demo.Entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Book {
    @Id
    private String title;
    private String author;
    private String description;
    private String dateOfRelease;
    private boolean available;
    private String bookBorrowedOnAndBy;

    public final boolean getAvailable(){
        return available;
    }
}
