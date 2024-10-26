package ozdemir0ozdemir.recipeshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ozdemir0ozdemir.recipeshare.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
