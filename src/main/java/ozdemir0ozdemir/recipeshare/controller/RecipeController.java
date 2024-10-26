package ozdemir0ozdemir.recipeshare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.recipeshare.model.Recipe;
import ozdemir0ozdemir.recipeshare.model.User;
import ozdemir0ozdemir.recipeshare.service.RecipeService;
import ozdemir0ozdemir.recipeshare.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
class RecipeController {

    private final RecipeService service;
    private final UserService userService;

    @GetMapping
    Page<Recipe> getAllRecipes(@RequestParam(defaultValue = "1") int pageNumber,
                               @RequestParam(defaultValue = "5") int pageSize) {

        return this.service.findAllRecipes(pageNumber, pageSize);
    }

    @GetMapping("/{recipeId}")
    ResponseEntity<Recipe> getRecipeById(@PathVariable Long recipeId) {

        return ResponseEntity.ok(this.service.findRecipeById(recipeId));
    }

    @PostMapping
    ResponseEntity<Recipe> createNewRecipe(@RequestBody Recipe request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Recipe savedRecipe = this.service.createRecipe(request, user);

        URI createdUri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{recipeId}")
                .build(savedRecipe.getId());

        return ResponseEntity
                .created(createdUri)
                .body(savedRecipe);
    }

    @PutMapping("/{recipeId}")
    ResponseEntity<Recipe> updateExistingRecipeById(@RequestBody Recipe request,
                                                    @PathVariable Long recipeId) {

        return ResponseEntity.ok(this.service.updateRecipe(request, recipeId));
    }

    @DeleteMapping("/{recipeId}")
    ResponseEntity<?> deleteExistingRecipeById(@PathVariable Long recipeId) {

        this.service.deleteRecipeById(recipeId);

        return ResponseEntity.noContent().build();
    }


}
