package ozdemir0ozdemir.recipeshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ozdemir0ozdemir.recipeshare.model.Recipe;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findByIdAndUserId(Long id, Long userId);
}
