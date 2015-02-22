package ungenutzt;

import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import data.Ingredient;
import data.Recipe;

public class IngredientFinderModel extends AbstractIngredientModel {

  private ResourceBundle  languageBundle;
//  private Object lastUpdatedValue;
  
  public IngredientFinderModel(String[] columnNames, ResourceBundle languageBundle) {
    super(columnNames);
    this.languageBundle = languageBundle;
  }
  
  @Override
  public void addRow(Ingredient ingred, boolean undoable) {
    getIngredients().add(ingred);
    fireTableRowsInserted(getIngredients().size() -1, getIngredients().size() -1);

    if (undoable) {
      getUndoManager().registerUndoableEdit(new IngredientActionAdd(this, ingred), this);
    }
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
    if (getColumnName(column).equals(languageBundle.getString("Ingredient"))) {
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
    if (getColumnName(column).equals(languageBundle.getString("Ingredient"))) {
//      lastUpdatedValue = ingred.getName();
      ingred.setName((String) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("StorePlace"))) {
//      lastUpdatedValue = ingred.getStorePlace();
      ingred.setStorePlace((String) value);
    }
    else if (getColumnName(column).equals(languageBundle.getString("Available"))) {
//      lastUpdatedValue = ingred.isAvailable();
      ingred.setAvailable((Boolean) value);
    }
    fireTableCellUpdated(row, column);
  }
  
  @Override
  public Object getValueAt(int row, int column) {
    Ingredient ingred = getIngredients().get(row);
    if (getColumnName(column).equals(languageBundle.getString("Ingredient"))) {
      return ingred.getName();
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
//    if (getColumnName(column).equals(languageBundle.getString("Ingredient"))) return false;
//    if (getColumnName(column).equals(languageBundle.getString("StorePlace"))) return false;
    return true;
  }
  
  @Override
  public void clear(){
    super.clear();
    getIngredients().clear();
  }

//  public Object getLastUpdatedValue() {
//    return lastUpdatedValue;
//  }
}
