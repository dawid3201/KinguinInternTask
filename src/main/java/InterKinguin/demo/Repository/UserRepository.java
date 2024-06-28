package InterKinguin.demo.Repository;

import InterKinguin.demo.Entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {
    User findByEmail(String userEmail);
}
