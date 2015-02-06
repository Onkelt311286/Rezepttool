package recipefinder.recipe;

import java.util.ResourceBundle;

import recipefinder.ingredient.IngredientFinderModel;

import mainwindow.data.Ingredient;
import mainwindow.data.Recipe;

import util.AbstractRecipeModel;

public class RecipeFinderModel extends AbstractRecipeModel {

  private ResourceBundle        languageBundle;
  private IngredientFinderModel ingredientModel;

  public RecipeFinderModel(String[] finderRecipecolumnNames, IngredientFinderModel ingredientModel, ResourceBundle languageBundle) {
    super(finderRecipecolumnNames);
    this.ingredientModel = ingredientModel;
    this.languageBundle = languageBundle;
  }

  @Override
  public Class getColumnClass(int column) {
    if (getColumnName(column).equals(languageBundle.getString("Recipe"))) {
      return String.class;
    }
    else if (getColumnName(column).equals(languageBundle.getString("Frequency"))) {
      return Long.class;
    }
    else if (getColumnName(column).equals(languageBundle.getString("Percentage"))) {
      return Double.class;
    }
    else if (getColumnName(column).equals(languageBundle.getString("Used"))) {
      return Boolean.class;
    }
    else {
      return String.class;
    }
  }

  @Override
  public void setValueAt(Object value, int row, int column) {
    Recipe recipe = getRecipes().get(row);
    if (getColumnName(column).equals(languageBundle.getString("Used"))) {
      recipe.setUsed((Boolean) value);
    }
  }

  @Override
  public Object getValueAt(int row, int column) {
    Recipe recipe = getRecipes().get(row);
    if (getColumnName(column).equals(languageBundle.getString("Recipe"))) {
      return recipe.getName();
    }
    else if (getColumnName(column).equals(languageBundle.getString("Type"))) {
      return recipe.getType();
    }
    else if (getColumnName(column).equals(languageBundle.getString("KindOfMeal"))) {
      return languageBundle.getString(recipe.getKindOfMeal().name());
    }
    else if (getColumnName(column).equals(languageBundle.getString("Frequency"))) {
      return recipe.getFrequency();
    }
    else if (getColumnName(column).equals(languageBundle.getString("Percentage"))) {
      return calculateRecipePercentage(recipe);
    }
    else {
      return recipe.isUsed();
    }
  }

  private Double calculateRecipePercentage(Recipe recipe) {
    double matchingIngreds = 0.0;
    double recipeIngredSize = recipe.getIngredients().size();
    if (recipeIngredSize == 0.0) {
      return 100.0;
    }
    else {
      for (Ingredient modelIngred : ingredientModel.getIngredients()) {
        for (Ingredient recipeIngred : recipe.getIngredients()) {
          if (recipeIngred.getName().equals(modelIngred.getName()) && recipeIngred.getStorePlace().equals(modelIngred.getStorePlace()) && modelIngred.isAvailable()) {
            matchingIngreds++;
            recipeIngred.setAvailable(true);
          }
        }
      }

      double percentage = (matchingIngreds * 100.0) / recipeIngredSize;
      return percentage;
    }
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    if (getColumnName(column).equals(languageBundle.getString("Used"))) return true;
    return false;
  }
}
