package com.example.zwiesel.recipe_app;

import java.util.ArrayList;


public class Recipe {

    static final int CATEGORY_APPETIZER = 0;
    static final int CATEGORY_ENTREE = 1;
    static final int CATEGORY_DESSERT = 2;

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

    public String toString(){
        return name;
    }
}