package ungenutzt;

import java.util.ResourceBundle;

import data.Ingredient;
import data.Recipe;

public class IngredientPrintModel extends AbstractIngredientModel {

  private ResourceBundle languageBundle;

  public IngredientPrintModel(String[] columnNames, ResourceBundle languageBundle) {
    super(columnNames);
    this.languageBundle = languageBundle;
  }

  @Override
  public void setValueAt(Object value, int row, int column) {
    Ingredient ingred = getIngredients().get(row);
    if (getColumnName(column).equals(languageBundle.getString("Amount"))) {
      ingred.setAmount((String) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Ingredient"))) {
      ingred.setName((String) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Order"))) {
      ingred.setOrder((Integer) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Recipe"))) {
      ingred.setRecipe((Recipe) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("StorePlace"))) {
      ingred.setStorePlace((String) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Available"))) {
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
  public void deleteSelectedRows(int[] rows, boolean keepInRecipe, boolean undoable) {
    // wird nicht benötigt
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
  public boolean isCellEditable(int row, int column) {
    if (getColumnName(column).equals(languageBundle.getString("Available"))) return true;
    return false;
  }
}
