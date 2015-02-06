package recipedetails.recipe;

import java.util.ArrayList;
import java.util.ResourceBundle;

import mainwindow.MainWindow;
import mainwindow.data.Ingredient;
import mainwindow.data.Recipe;
import recipedetails.actions.RecipeActionDelete;
import recipedetails.ingredient.IngredientDetailsModel;
import util.AbstractRecipeModel;
import util.AbstractTable;
import util.ToolConstants.KINDOFMEAL;

public class RecipeDetailsModel extends AbstractRecipeModel {

  private MainWindow mainWindow;
  private RecipeTable            _parent;
  private AbstractTable          ingredientTable;
  private IngredientDetailsModel _ingredientModel;
  private RecipeFormulaPanel     _recipeDetailPanel;
  private ResourceBundle         languageBundle;

  public RecipeDetailsModel(MainWindow mainWindow, String[] columnNames, ResourceBundle languageBundle) {
    super(columnNames);
    this.ingredientTable = mainWindow.getIngredientDetailsTable();
    _ingredientModel = (IngredientDetailsModel) ingredientTable.getModel();
    _parent = mainWindow.getRecipeDetailsTable();
    _recipeDetailPanel = mainWindow.getRecipeFormulaPanel();
    this.languageBundle = languageBundle;
  }

  @Override
  public void deleteSelectedRows(int[] rows, boolean undoable) {
    ArrayList<Recipe> recipes = getRecipesByRows(rows);
    for (Recipe recipe : recipes) {
      int[] intRows = getIngredientRows(recipe);
      _ingredientModel.deleteSelectedRows(intRows, true, false);
      if ((getRowCount() - 1) == getRowByRecipe(recipe)) {
        _parent.clearSelection();
        _recipeDetailPanel.getTextField().setText("");
      }
      getRecipes().remove(recipe);
    }
    if (undoable) {
      getUndoManager().registerUndoableEdit(new RecipeActionDelete(this, recipes.toArray(new Recipe[recipes.size()])), this);
    }
  }

  @Override
  public Class getColumnClass(int column) {
    if (getColumnName(column).equals(languageBundle.getString("KindOfMeal"))) {
      return Enum.class;
    }
    else if (getColumnName(column).equals(languageBundle.getString("Used"))) {
      return Boolean.class;
    }
    else if (getColumnName(column).equals(languageBundle.getString("Frequency"))) {
      return Long.class;
    }
    else {
      return String.class;
    }
  }

  @Override
  public void setValueAt(Object value, int row, int column) {
    Recipe recipe = getRecipes().get(row);

    if (getColumnName(column).equals(languageBundle.getString("Recipe"))) {
      recipe.setName((String) value);
      ingredientTable.updateUI();
    }
    else if (getColumnName(column).equals(languageBundle.getString("Duration"))) {
      recipe.setDuration((String) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Type"))) {
      recipe.setType((String) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("KindOfMeal"))) {
      recipe.setKindOfMeal((KINDOFMEAL) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Used"))) {
      recipe.setUsed((Boolean) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Frequency"))) {
      recipe.setFrequency((Integer) value);
    }
  }

  @Override
  public Object getValueAt(int row, int column) {
    Recipe recipe = getRecipes().get(row);
    if (getColumnName(column).equals(languageBundle.getString("Recipe"))) {
      return recipe.getName();
    }
    else if (getColumnName(column).equals(languageBundle.getString("Duration"))) {
      return recipe.getDuration();
    }
    else if (getColumnName(column).equals(languageBundle.getString("Type"))) {
      return recipe.getType();
    }
    else if (getColumnName(column).equals(languageBundle.getString("Frequency"))) {
      return recipe.getFrequency();
    }
    else if (getColumnName(column).equals(languageBundle.getString("KindOfMeal"))) {
      return languageBundle.getString(recipe.getKindOfMeal().name());
    }
    else {
      return recipe.isUsed();
    }
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    if (getColumnName(column).equals(languageBundle.getString("Frequency"))) return false;
    return true;
  }

  public int[] getIngredientRows(Recipe recipe) {
    ArrayList<Integer> ingredRows = new ArrayList<Integer>();
    for (Ingredient ingred : recipe.getIngredients()) {
      int row = _ingredientModel.getRowByIngredient(ingred);
      if (row != -1) {
        ingredRows.add(row);
      }
    }

    int[] intRows = new int[ingredRows.size()];
    int i = 0;
    for (Integer integer : ingredRows) {
      intRows[i] = integer;
      i++;
    }

    return intRows;
  }

  public Recipe getRoot() {
    return getRecipes().get(0);
  }

  public void changeRecipeActivation(Recipe recipe) {
    if (recipe.isUsed() == true) {
      _ingredientModel.deleteSelectedRows(getIngredientRows(recipe), true, false);
    }
    else {
      for (Ingredient ingred : recipe.getIngredients()) {
        _ingredientModel.addRow(ingred, false);
      }
    }
  }
}
