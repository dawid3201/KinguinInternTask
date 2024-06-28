package InterKinguin.demo.Repository;

import InterKinguin.demo.Entities.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, Long> {
    Book findByTitle(String bookTitle);
}
