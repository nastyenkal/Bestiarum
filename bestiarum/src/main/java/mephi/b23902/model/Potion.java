package mephi.b23902.model;

public class Potion {
    private String ingredients;
    private String preparation_time;
    private String strength;

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(String preparation_time) {
        this.preparation_time = preparation_time;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    @Override
    public String toString() {
        return String.format("Ингредиенты: %s, Время: %s, Сила: %s",
                ingredients, preparation_time, strength);
    }
}