package ozdemir0ozdemir.recipeshare.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.recipeshare.dto.RecipeDto;
import ozdemir0ozdemir.recipeshare.model.Recipe;
import ozdemir0ozdemir.recipeshare.model.User;
import ozdemir0ozdemir.recipeshare.repository.RecipeRepository;
import ozdemir0ozdemir.recipeshare.request.CreateRecipeRequest;
import ozdemir0ozdemir.recipeshare.request.UpdateRecipeRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private static final Logger log = LoggerFactory.getLogger(RecipeService.class);
    private final RecipeRepository repository;

    public Page<RecipeDto> findAllRecipes(int page, int pageSize) {

        log.info("find all recipes invoked for page: {}, and page size: {}", page, pageSize);
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.repository.findAll(pageable)
                .map(RecipeDto::from);
    }

    public Optional<Recipe> createRecipe(CreateRecipeRequest request) {

        User authenticatedUser = this.getAuthenticatedUser();

        log.info("Create recipe invoked for user id: {}, and request: {}", authenticatedUser.getId(), request);

        Recipe createdRecipe = Recipe.builder()
                .title(request.title())
                .imageUrl(request.imageUrl())
                .description(request.description())
                .vegetarian(request.vegetarian())
                .user(authenticatedUser)
                .build();

        return Optional.of(this.repository.save(createdRecipe));
    }

    public Optional<Recipe> findRecipeById(Long recipeId) {

        log.info("find recipe by id invoked for recipe id: {}", recipeId);
        return this.repository.findById(recipeId);
    }

    public Optional<RecipeDto> updateRecipe(UpdateRecipeRequest request, Long recipeId) {

        log.info("Recipe update invoked for recipe id: {}", recipeId);

        Optional<Recipe> optionalRecipe = this.findByIdForAdminOrAuthenticatedUser(recipeId);

        if (optionalRecipe.isEmpty()) {
            log.info("Recipe not found while updating recipe with recipe id: {}", recipeId);
            return Optional.empty();
        }

        Recipe oldRecipe = optionalRecipe.get();
        oldRecipe.setTitle(request.title());
        oldRecipe.setDescription(request.description());
        oldRecipe.setImageUrl(request.imageUrl());
        oldRecipe.setVegetarian(request.vegetarian());

        log.info("Recipe updating succeeded for recipe id: {}", recipeId);
        return Optional.of(RecipeDto.from(this.repository.save(oldRecipe)));
    }

    public void deleteRecipeById(Long recipeId) {

        log.info("Delete recipe invoked for recipe id: {}", recipeId);

        this.findByIdForAdminOrAuthenticatedUser(recipeId)
                .ifPresent(recipe -> {
                    log.info("Recipe deleted with id: {}", recipe.getId());
                    this.repository.deleteById(recipeId);
                });
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Optional<Recipe> findByIdForAdminOrAuthenticatedUser(Long recipeId) {
        boolean admin = false;
        User authenticatedUser = getAuthenticatedUser();
        // FIXME: Based on authenticatedUser authorities
        if (admin) {
            return this.repository.findById(recipeId);
        }
        return this.repository.findByIdAndUserId(recipeId, authenticatedUser.getId());
    }

}
