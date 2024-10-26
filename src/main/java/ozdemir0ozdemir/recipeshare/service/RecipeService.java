package ozdemir0ozdemir.recipeshare.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.recipeshare.model.Recipe;
import ozdemir0ozdemir.recipeshare.model.User;
import ozdemir0ozdemir.recipeshare.repository.RecipeRepository;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository repository;

    public Page<Recipe> findAllRecipes(int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(Math.max(0, pageNumber - 1), Math.max(5, pageSize));

        return this.repository.findAll(pageable);
    }

    public Recipe createRecipe(Recipe request, User user) {

        Recipe createdRecipe = Recipe.builder()
                .title(request.getTitle())
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .vegetarian(request.isVegetarian())
                .user(user)
                .build();

        return this.repository.save(createdRecipe);
    }

    public Recipe findRecipeById(Long recipeId) {

        return this.repository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
    }

    public Recipe updateRecipe(Recipe request, Long recipeId) {

        Recipe oldRecipe = findRecipeById(recipeId);
        oldRecipe.setTitle(request.getTitle());
        oldRecipe.setDescription(request.getDescription());
        oldRecipe.setImageUrl(request.getImageUrl());
        oldRecipe.setVegetarian(request.isVegetarian());

        return this.repository.save(oldRecipe);
    }

    public void deleteRecipeById(Long recipeId) {

        this.repository.deleteById(recipeId);
    }

    public void likeRecipe(Long recipeId, Long userId) {
        // TODO: RecipeService: Recipe's likes
    }


}
