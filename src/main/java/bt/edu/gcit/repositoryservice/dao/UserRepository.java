package bt.edu.gcit.repositoryservice.dao;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import bt.edu.gcit.repositoryservice.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByIndexNumber(String indexNumber);
    Optional<User> findByEmail(String email);
    Optional<User> findByPasswordToken(String passwordToken);
}
