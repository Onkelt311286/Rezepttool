package ungenutzt;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import data.Ingredient;
import data.Recipe;

public class RecipeFinderTableCellRenderer implements TableCellRenderer {

  private JTable ingredientTable;
  private boolean isHued;

  public RecipeFinderTableCellRenderer(JTable ingredientTable) {
    super();

    this.ingredientTable = ingredientTable;
    isHued = true;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    Component result = null;
    DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
    JLabel label = (JLabel) defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    JCheckBox box = new JCheckBox();
    AbstractRecipeModel model = (AbstractRecipeModel) table.getModel();
    Recipe recipe = model.getRecipes().get(table.convertRowIndexToModel(row));

    int[] convertedRows = new int[ingredientTable.getSelectedRows().length];
    for (int i = 0; i < ingredientTable.getSelectedRows().length; i++) {
      convertedRows[i] = ingredientTable.convertRowIndexToModel(ingredientTable.getSelectedRows()[i]);
    }

    boolean contains = false;
    float hits = 0;
    if (convertedRows.length <= ((AbstractIngredientModel) ingredientTable.getModel()).getIngredients().size()) {
      for (int i : convertedRows) {
        Ingredient ingred = ((AbstractIngredientModel) ingredientTable.getModel()).getIngredients().get(i);
        for (Ingredient ingredFromRecipe : recipe.getIngredients()) {
          if (ingred.getName().equals(ingredFromRecipe.getName()) && ingred.getStorePlace().equals(ingredFromRecipe.getStorePlace())) {
            contains = true;
            hits++;
          }
        }
      }
    }

    if (model.getColumnClass(table.convertColumnIndexToModel(column)) == Boolean.class) {
      boolean enabled = (Boolean) value;
      result = box;
      box.setHorizontalAlignment(SwingConstants.CENTER);
      box.setSelected(enabled);
      box.setBackground(Color.WHITE);
      if (isSelected && !table.isPaintingForPrint()) {
        box.setBackground(new Color(184, 207, 229));
      }
    }
    else {
      result = label;
    }


    if(isHued){
      float size = recipe.getIngredients().size();
      float hue = hits / size;
      if (contains && !table.isPaintingForPrint()) {
        result.setBackground(Color.getHSBColor(0.3f, hue, 1f));
        if (isSelected && !table.isPaintingForPrint()) {
          if (hue + 0.1f > 1) {
            hue = 1;
          }
          else {
            hue = hue + 0.1f;
          }
          result.setBackground(Color.getHSBColor(0.3f, hue, 0.85f));
        }
      }
    }
    else {
      if (contains && !table.isPaintingForPrint()) {
        result.setBackground(new Color(177, 255, 166));
        if (isSelected && !table.isPaintingForPrint()) {
          result.setBackground(new Color(30, 240, 0));
        }
      }
    }

    return result;
  }
  
  public boolean isHued() {
    return isHued;
  }

  public void setHued(boolean isHued) {
    this.isHued = isHued;
  }
}
