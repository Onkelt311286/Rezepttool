package recipedetails.ingredient;

import java.util.ResourceBundle;

import javax.swing.event.UndoableEditListener;

import recipedetails.actions.IngredientActionDelete;
import util.AbstractIngredientModel;

import mainwindow.MainUndoManager;
import mainwindow.data.Ingredient;
import mainwindow.data.Recipe;

public class IngredientDetailsModel extends AbstractIngredientModel {

  private ResourceBundle  languageBundle;
//  private Object lastUpdatedValue;

  public IngredientDetailsModel(String[] columnNames, ResourceBundle languageBundle) {
    super(columnNames);
    this.languageBundle = languageBundle;
  }

  @Override
  public void deleteSelectedRows(int[] rows, boolean keepInRecipe, boolean undoable) {
    if (getRowCount() > 0) {
      Ingredient[] ingreds = new Ingredient[rows.length];
      for (int i = 0; i < rows.length; i++) {
        ingreds[i] = getIngredients().get(rows[i]);
      }
      for (int i = 0; i < ingreds.length; i++) {
        if (!keepInRecipe) {
          ingreds[i].getRecipe().getIngredients().remove(ingreds[i]);
        }
        getIngredients().remove(ingreds[i]);
      }

      if (undoable) {
        getUndoManager().registerUndoableEdit(new IngredientActionDelete(this, ingreds), this);
      }
    }
  }


  @Override
  public Class getColumnClass(int column) {
    if (getColumnName(column).equals(languageBundle.getString("Amount"))) {
      return String.class;
    }
    else if (getColumnName(column).equals(languageBundle.getString("Ingredient"))) {
      return String.class;
    }
    else if (getColumnName(column).equals(languageBundle.getString("Order"))) {
      return Integer.class;
    }
    else if (getColumnName(column).equals(languageBundle.getString("Recipe"))) {
      return String.class;
    }
    else if (getColumnName(column).equals(languageBundle.getString("StorePlace"))) {
      return String.class;
    }
    else {
      return Boolean.class;
    }
  }

  @Override
  public void setValueAt(Object value, int row, int column) {
    Ingredient ingred = getIngredients().get(row);
    if (getColumnName(column).equals(languageBundle.getString("Amount"))) {
      ingred.setAmount((String) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Ingredient"))) {
//      lastUpdatedValue = ingred.getName();
      ingred.setName((String) value);
      fireTableCellUpdated(row, column);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Order"))) {
      ingred.setOrder((Integer) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Recipe"))) {
      ingred.setRecipe((Recipe) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("StorePlace"))) {
//      lastUpdatedValue = ingred.getStorePlace();
      ingred.setStorePlace((String) value);
      fireTableCellUpdated(row, column);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Available"))) {
//      lastUpdatedValue = ingred.isAvailable();
      ingred.setAvailable((Boolean) value);
      fireTableCellUpdated(row, column);
    }
  }

  @Override
  public Object getValueAt(int row, int column) {
    Ingredient ingred = getIngredients().get(row);
    if (getColumnName(column).equals(languageBundle.getString("Amount"))) {
      return ingred.getAmount();
    }
    else if (getColumnName(column).equals(languageBundle.getString("Ingredient"))) {
      return ingred.getName();
    }
    else if (getColumnName(column).equals(languageBundle.getString("Order"))) {
      return ingred.getOrder();
    }
    else if (getColumnName(column).equals(languageBundle.getString("Recipe"))) {
      return ingred.getRecipe().getName();
    }
    else if (getColumnName(column).equals(languageBundle.getString("StorePlace"))) {
      return ingred.getStorePlace();
    }
    else {
      return ingred.isAvailable();
    }
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    if (getColumnName(column).equals(languageBundle.getString("Recipe"))) return false;
    return true;
  }

//  public Object getLastUpdatedValue() {
//    return lastUpdatedValue;
//  }
}
