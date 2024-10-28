package ozdemir0ozdemir.recipeshare.request;

public record CreateRecipeRequest(String title,
                                  String imageUrl,
                                  String description,
                                  boolean vegetarian) {
}
