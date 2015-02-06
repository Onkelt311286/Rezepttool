package mainwindow.data;

import java.util.ArrayList;
import java.util.Date;



public class DayPlan {
  
  private Date day;
  private ArrayList<Recipe> recipes;
  
  public DayPlan(Date day){
    this.day = day;
    recipes = new ArrayList<Recipe>();
  }
  
  public void addRecipe(Recipe recipe){
    recipes.add(recipe);
  }

  public void removeRecipe(Recipe recipe) {
    recipes.remove(recipe);
  }

  public Date getDay() {
    return day;
  }

  public void setDay(Date day) {
    this.day = day;
  }

  public ArrayList<Recipe> getRecipes() {
    return recipes;
  }

  public void setRecipes(ArrayList<Recipe> recipes) {
    this.recipes = recipes;
  }
}
