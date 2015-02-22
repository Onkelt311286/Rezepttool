package ungenutzt;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;

import data.Ingredient;

public class IngredientFinderCellEditedAction extends AbstractAction {

  private MainWindow     mainWindow;
  private ResourceBundle languageBundle;

  public IngredientFinderCellEditedAction(MainWindow mainWindow, ResourceBundle languageBundle) {
    this.mainWindow = mainWindow;
    this.languageBundle = languageBundle;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    TableCellListener tcl = (TableCellListener) event.getSource();
    IngredientFinderModel ingredientFinderModel = (IngredientFinderModel) mainWindow.getIngredientFinderTable().getModel();
    Ingredient newIngred = ingredientFinderModel.getIngredients().get(tcl.getRow());
    Ingredient oldIngred;
    if (ingredientFinderModel.getColumnName(tcl.getColumn()).equals(languageBundle.getString("Ingredient"))) {
      oldIngred = new Ingredient((String) tcl.getOldValue(), newIngred.getStorePlace());
    }
    else if (ingredientFinderModel.getColumnName(tcl.getColumn()).equals(languageBundle.getString("StorePlace"))) {
      oldIngred = new Ingredient(newIngred.getName(), (String) tcl.getOldValue());
    }
    else {
      oldIngred = new Ingredient(newIngred.getName(), newIngred.getStorePlace());
    }
    mainWindow.updateAllIngredients(oldIngred, newIngred);
    mainWindow.getRecipeFinderTable().getRowSorter().toggleSortOrder(4);
    mainWindow.getRecipeFinderTable().getRowSorter().toggleSortOrder(4);
  }
}
