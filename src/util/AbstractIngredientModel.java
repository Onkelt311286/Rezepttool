package util;

import java.util.ArrayList;

import javax.swing.event.UndoableEditListener;
import javax.swing.table.AbstractTableModel;

import mainwindow.MainUndoManager;
import mainwindow.data.Ingredient;

import recipedetails.actions.IngredientActionAdd;

public abstract class AbstractIngredientModel extends AbstractTableModel {

  private ArrayList<Ingredient> ingredients;
  private String[]              columnNames;
  private MainUndoManager       undoManager;

  public AbstractIngredientModel(String[] columnNames) {
    ingredients = new ArrayList<Ingredient>();
    this.columnNames = columnNames;
  }

  public void clear() {
    getIngredients().clear();
  }

  @Override
  public int getRowCount() {
    return ingredients.size();
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

  public void addRow(Ingredient ingred, boolean undoable) {
    getIngredients().add(ingred);
    if (!ingred.getRecipe().getIngredients().contains(ingred)) {
      ingred.getRecipe().getIngredients().add(ingred);
    }

    if (undoable) {
      undoManager.registerUndoableEdit(new IngredientActionAdd(this, ingred), this);
    }
    fireTableRowsInserted(ingredients.size() - 1, ingredients.size() - 1);
  }

  public abstract void deleteSelectedRows(int[] rows, boolean keepInRecipe, boolean undoable);

  public int getRowByIngredient(Ingredient ingredient) {
    int row = -1;
    for (int i = 0; i < getIngredients().size(); i++) {
      Ingredient currentIngredient = getIngredients().get(i);
      if (currentIngredient.equals(ingredient)) {
        row = i;
        break;
      }
    }
    return row;
  }

  public void addUndoableEditListener(MainUndoManager listener) {
    undoManager = listener;
    listenerList.add(UndoableEditListener.class, listener);
  }

  public ArrayList<Ingredient> getIngredients() {
    return ingredients;
  }

  public String[] getColumnNames() {
    return columnNames;
  }

  public MainUndoManager getUndoManager() {
    return undoManager;
  }
}
