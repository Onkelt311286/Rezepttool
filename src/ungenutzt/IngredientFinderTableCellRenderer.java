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

public class IngredientFinderTableCellRenderer implements TableCellRenderer {

  private JTable recipeTable;

  public IngredientFinderTableCellRenderer(JTable recipeTable) {
    super();

    this.recipeTable = recipeTable;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    Component result = null;
    DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
    JLabel label = (JLabel) defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    JCheckBox box = new JCheckBox();
    AbstractIngredientModel model = (AbstractIngredientModel) table.getModel();
    Ingredient ingred = model.getIngredients().get(table.convertRowIndexToModel(row));

    int[] convertedRows = new int[recipeTable.getSelectedRows().length];
    for (int i = 0; i < recipeTable.getSelectedRows().length; i++) {
      convertedRows[i] = recipeTable.convertRowIndexToModel(recipeTable.getSelectedRows()[i]);
    }

    boolean contains = false;

    if (convertedRows.length <= ((AbstractRecipeModel) recipeTable.getModel()).getRecipes().size()) {
      for (int i : convertedRows) {
        Recipe recipe = ((AbstractRecipeModel) recipeTable.getModel()).getRecipes().get(i);
        for (Ingredient ingredFromRecipe : recipe.getIngredients()) {
          if (ingred.getName().equals(ingredFromRecipe.getName()) && ingred.getStorePlace().equals(ingredFromRecipe.getStorePlace())) {
            contains = true;
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

    if (contains && !table.isPaintingForPrint()) {
      result.setBackground(new Color(177, 255, 166));
      if (isSelected && !table.isPaintingForPrint()) {
        result.setBackground(new Color(30, 240, 0));
      }
    }

    return result;
  }
}
