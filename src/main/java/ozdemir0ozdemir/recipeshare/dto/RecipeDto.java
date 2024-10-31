package ozdemir0ozdemir.recipeshare.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;
import ozdemir0ozdemir.recipeshare.model.Recipe;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecipeDto {

    private String title;
    private String imageUrl;
    private String description;
    private boolean vegetarian;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RecipeDto from(@NonNull Recipe recipe) {
        return new RecipeDto(
                recipe.getTitle(),
                recipe.getImageUrl(),
                recipe.getDescription(),
                recipe.isVegetarian(),
                recipe.getCreatedAt(),
                recipe.getUpdatedAt()
        );
    }
}
