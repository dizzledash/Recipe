package com.example.zwiesel.recipe_app;

import java.util.ArrayList;


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
        aList.remove(index);
        aList.add(index, ingredient);
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

    public String[] getIngredient(int index){
        return aList.get(index);
    }

    public int getIngredientCount(){
        return aList.size();
    }

    public String toString(){
        return name;
    }
}