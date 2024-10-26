package ozdemir0ozdemir.recipeshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ozdemir0ozdemir.recipeshare.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
