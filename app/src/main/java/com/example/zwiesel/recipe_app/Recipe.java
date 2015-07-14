package com.example.zwiesel.recipe_app;

import java.util.ArrayList;


/**
 * The recipe class provides a data type for the recipes you want to manage.
 * The name and the recipe-description are implemented with strings,
 * the ingredients with String-Arrays and for better managing the categories
 * on the main activity, the assigned categories are implemented with the int data type.
 */
public class Recipe {

    static final int CATEGORY_APPETIZER = 0;
    static final int CATEGORY_MAIN_DISH = 1;
    static final int CATEGORY_DESSERT = 2;
    static final int CATEGORY_SNACKS = 3;
    static final int CATEGORY_SALAD = 4;
    static final int CATEGORY_SOUP = 5;


    private int category;
    private String name, description;
    private ArrayList<String[]> aList = new ArrayList<>();

    public Recipe(String n){
        name = n;
    }

    /**
     * Adding an ingredient to the recipe, which is implemented as a string-array
     * @param title
     * @param amount
     * @param unit
     */
    public void addIngredient(String title, String amount, String unit){
        String[] ingredient = new String[]{title, amount, unit};
        aList.add(ingredient);
    }

    public void setName(String n){
        name = n;
    }

    public void setDescription(String d){
        description = d;
    }

    public void setCategory(int c){
        category = c;
    }

    public void setCategory(String c){
        switch (c){
            case "Appetizer":
                category = CATEGORY_APPETIZER;
                break;
            case "Dessert":
                category = CATEGORY_DESSERT;
                break;
            case "Soup":
                category = CATEGORY_SOUP;
                break;
            case "Snacks":
                category = CATEGORY_SNACKS;
                break;
            case "Salad":
                category = CATEGORY_SALAD;
                break;
            default:
                category = CATEGORY_MAIN_DISH;
                break;
        }
    }

    public void setIngredient(int index, String title, String amount, String unit){
        String[] ingredient = new String[]{title, amount, unit};
        aList.set(index, ingredient);
    }

    public void setIngredient(int index, String[] array){
        aList.set(index, array);
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getCategory(){
        return category;
    }

    public String getCategoryAsString() {
        switch (category){
            case CATEGORY_APPETIZER:
                return "Appetizer";
            case CATEGORY_DESSERT:
                return "Dessert";
            case CATEGORY_SOUP:
                return "Soup";
            case CATEGORY_SNACKS:
                return "Snacks";
            case CATEGORY_SALAD:
                return "Salad";
            default:
                return "Main Dish";
        }
    }

    public String[] getIngredient(int index){
        return aList.get(index);
    }

    public int getIngredientCount(){
        return aList.size();
    }
}