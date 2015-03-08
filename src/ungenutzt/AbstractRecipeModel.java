package ungenutzt;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.event.UndoableEditListener;
import javax.swing.table.AbstractTableModel;

import data.Recipe;

public abstract class AbstractRecipeModel extends AbstractTableModel {

  private ArrayList<Recipe>      recipes;
  private String[]               columnNames;
  private MainUndoManager        undoManager;
  
  public AbstractRecipeModel(String[] columnNames){
    recipes = new ArrayList<Recipe>();
    this.columnNames = columnNames;
  }
  
  public void clear() {
    getRecipes().clear();
  }
  
  @Override
  public int getRowCount() {
    return recipes.size();
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }
  
  @Override
  public String getColumnName(int column) {
    return columnNames[column];
  }

  @Override
  public abstract Object getValueAt(int rowIndex, int columnIndex);

  public void addRow(Recipe recipe, boolean undoable) {
    getRecipes().add(recipe);
    if (undoable) {
      undoManager.registerUndoableEdit(new RecipeActionAdd(this, recipe), this);
    }
  }
  
  public void deleteSelectedRows(int[] rows, boolean undoable) {
    ArrayList<Recipe> recipes = getRecipesByRows(rows);
    for (Recipe recipe : recipes) {
      recipes.remove(recipe);
    }
    if (undoable) {
      undoManager.registerUndoableEdit(new RecipeActionDelete(this, recipes.toArray(new Recipe[recipes.size()])), this);
    }
  }
  
  public ArrayList<Recipe> getRecipesByRows(int[] rows) {
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    for (int i = 0; i < rows.length; i++) {
      recipes.add(this.recipes.get(rows[i]));
    }
    return recipes;
  }
  
  public int getRowByRecipe(Recipe recipe) {
    int row = -1;
    for (int i = 0; i < recipes.size(); i++) {
      Recipe currentRecipe = recipes.get(i);
      if (currentRecipe.equals(recipe)) {
        row = i;
        break;
      }
    }
    return row;
  }
  
  public Recipe getRecipeByID(int id) {
    Recipe result = null;
    for (Recipe recipe : recipes) {
      if (recipe.getId() == id) {
        result = recipe;
        break;
      }
    }
    return result;
  }
  
  public void addUndoableEditListener(MainUndoManager listener) {
    undoManager = listener;
    listenerList.add(UndoableEditListener.class, listener);
  }

  public ArrayList<Recipe> getRecipes() {
    return recipes;
  }

  public MainUndoManager getUndoManager() {
    return undoManager;
  }

  public String[] getColumnNames() {
    return columnNames;
  }
}
