package thisis.vegetarian.question.mark.model;

public interface Product {
    long getId();
    String getBarcode();
    String getName();
    int getCategory();
    String getOrigin();
    int getVegetarian();
    String getRemark();
    int getStatus();
}
