package recipedetails.recipe;

import java.awt.Dimension;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import recipedetails.ingredient.IngredientDetailsModel;

import mainwindow.MainWindow;
import mainwindow.data.Ingredient;
import mainwindow.data.Recipe;

import util.AbstractRecipeModel;
import util.AbstractTable;
import util.ToolConstants;

public class RecipeTable extends AbstractTable {

  private AbstractTableModel model;
  private ResourceBundle     languageBundle;
  private MainWindow         mainWindow;

  public RecipeTable(MainWindow parent, ResourceBundle languageBundle) {
    super();
    this.mainWindow = parent;
    this.languageBundle = languageBundle;

    getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if(getSelectedRow() != -1){
          int row = convertRowIndexToModel(getSelectedRow());
          AbstractRecipeModel recipeModel = (AbstractRecipeModel) getModel();
          Recipe selectedRecipe = recipeModel.getRecipes().get(row);
          mainWindow.getRecipeFormulaPanel().getTextField().setText(selectedRecipe.getFormula());
          IngredientDetailsModel ingredientModel = (IngredientDetailsModel) mainWindow.getIngredientDetailsTable().getModel();
          ingredientModel.clear();
          mainWindow.getIngredientDetailsTable().setAutoCreateRowSorter(true);
          int rows[] = getConvertedRows(getSelectedRows());
          for (int i : rows) {
            Recipe recipe = recipeModel.getRecipes().get(i);
            for (Ingredient ingred : recipe.getIngredients()) {
              ingredientModel.addRow(ingred, false);
            }
          }
        }
      }
    });
  }

  public void setModel(RecipeDetailsModel model) {
    super.setModel(model);
    this.model = model;
    getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JComboBox(ToolConstants.KINDOFMEAL.values())));
  }

  public Vector<String> kindOfMealValuesToString(ToolConstants.KINDOFMEAL[] values) {
    Vector<String> stringValues = new Vector<String>();
    for (ToolConstants.KINDOFMEAL kindofmeal : values) {
      stringValues.add(languageBundle.getString(kindofmeal.name()));
    }
    return stringValues;
  }
}
