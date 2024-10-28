package ozdemir0ozdemir.recipeshare.request;

public record UpdateRecipeRequest(String title,
                                  String imageUrl,
                                  String description,
                                  boolean vegetarian) {
}
