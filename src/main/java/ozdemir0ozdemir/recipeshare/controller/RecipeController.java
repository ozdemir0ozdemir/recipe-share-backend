package ozdemir0ozdemir.recipeshare.controller;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.recipeshare.dto.RecipeDto;
import ozdemir0ozdemir.recipeshare.model.Recipe;
import ozdemir0ozdemir.recipeshare.request.CreateRecipeRequest;
import ozdemir0ozdemir.recipeshare.request.UpdateRecipeRequest;
import ozdemir0ozdemir.recipeshare.response.PagedResponse;
import ozdemir0ozdemir.recipeshare.response.Response;
import ozdemir0ozdemir.recipeshare.service.RecipeService;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
class RecipeController {

    private static final Logger log = LoggerFactory.getLogger(RecipeController.class);
    private final RecipeService service;

    @RateLimiter(name = "basic", fallbackMethod = "rateLimited")
    @GetMapping
    ResponseEntity<PagedResponse<Set<RecipeDto>>> getAllRecipes(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "5") int pageSize) {

        int securedPage = Math.max(0, page - 1);
        int securedPageSize = Math.max(5, pageSize);
        log.info("All recipes requested with page: {}, and page size: {}", securedPage, securedPageSize);

        Page<RecipeDto> pages = this.service.findAllRecipes(securedPage, securedPageSize);

        if (pages.isEmpty()) {
            log.info("No recipes found in database");
            return ResponseEntity.ok(PagedResponse.of(null, pages, "No recipes found"));
        }

        return ResponseEntity.ok(PagedResponse.of(pages.toSet(), pages, "Recipes found"));
    }

    ResponseEntity<PagedResponse<Set<RecipeDto>>> rateLimited(int page, int pageSize, RequestNotPermitted requestNotPermitted) {
        log.error("Rate Limited: ",requestNotPermitted);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{recipeId}")
    ResponseEntity<Response<RecipeDto>> getRecipeById(@PathVariable Long recipeId) {

        log.info("Recipe requested for recipe id: {}", recipeId);

        return this.service.findRecipeById(recipeId)
                .map(RecipeDto::from)
                .map(dto -> Response.ofSucceeded(dto, "Recipe retrieved"))
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.info("Recipe not found for recipe id: {}", recipeId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Response.ofFailed(null, "Recipe not found"));
                });
    }

    @PostMapping
    ResponseEntity<Response<RecipeDto>> createNewRecipe(@RequestBody CreateRecipeRequest request) {

        log.info("Create recipe requested with request: {}", request);
        Optional<Recipe> optionalRecipe = this.service.createRecipe(request);

        if(optionalRecipe.isEmpty()){
            log.info("Create recipe request failed");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(Response.ofFailed(null,"Recipe creation failed"));
        }

        log.info("Recipe saved with recipe id: {}", optionalRecipe.get().getId());

        URI createdUri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{recipeId}")
                .build(optionalRecipe.get().getId());

        Response<RecipeDto> response = Response.ofSucceeded(
                RecipeDto.from(optionalRecipe.get()),
                "Recipe saved successfully");

        return ResponseEntity
                .created(createdUri)
                .body(response);
    }


    @PatchMapping("/{recipeId}")
    ResponseEntity<Response<RecipeDto>> updateExistingRecipeById(@RequestBody UpdateRecipeRequest request,
                                                                 @PathVariable Long recipeId) {

        log.info("Recipe update requested for recipe id: {}, request: {}", recipeId, request);

        return this.service.updateRecipe(request, recipeId)
                .map(dto -> Response.ofSucceeded(dto, "Recipe updated"))
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.info("Recipe not found while updating recipe with recipe id: {}", recipeId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.ofFailed(null, "Recipe not found"));
                });
    }

    @DeleteMapping("/{recipeId}")
    ResponseEntity<Void> deleteExistingRecipeById(@PathVariable Long recipeId) {
        log.info("Delete recipe requested for recipe id: {}", recipeId);
        this.service.deleteRecipeById(recipeId);
        return ResponseEntity.noContent().build();
    }


}
