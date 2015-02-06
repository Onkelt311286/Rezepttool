package recipedetails.ingredient;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;

import recipefinder.ingredient.IngredientFinderModel;
import mainwindow.MainWindow;
import mainwindow.data.Ingredient;
import mainwindow.data.Recipe;
import util.TableCellListener;

public class IngredientDetailsCellEditedAction extends AbstractAction {

  private MainWindow     mainWindow;
  private ResourceBundle languageBundle;

  public IngredientDetailsCellEditedAction(MainWindow mainWindow, ResourceBundle languageBundle) {
    this.mainWindow = mainWindow;
    this.languageBundle = languageBundle;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    TableCellListener tcl = (TableCellListener) event.getSource();
    IngredientDetailsModel ingredientDetailsModel = (IngredientDetailsModel) mainWindow.getIngredientDetailsTable().getModel();
    Ingredient newIngred = ingredientDetailsModel.getIngredients().get(tcl.getRow());
    if (ingredientDetailsModel.getColumnName(tcl.getColumn()).equals(languageBundle.getString("Available"))) {
      Ingredient oldIngred = new Ingredient(newIngred.getName(), newIngred.getStorePlace());
      mainWindow.updateAllIngredients(oldIngred, newIngred);
      mainWindow.getIngredientDetailsTable().updateUI();
    }
    else if (ingredientDetailsModel.getColumnName(tcl.getColumn()).equals(languageBundle.getString("Ingredient")) || ingredientDetailsModel.getColumnName(tcl.getColumn()).equals(languageBundle.getString("StorePlace"))) {
      for (Recipe recipe : mainWindow.getRecipeListPanel().getAllRecipes()) {
        for (Ingredient detailsIngred : recipe.getIngredients()) {          
          if (detailsIngred.getName().equals(newIngred.getName()) && detailsIngred.getStorePlace().equals(newIngred.getStorePlace())) {
            newIngred.setAvailable(detailsIngred.isAvailable());
            mainWindow.getIngredientDetailsTable().updateUI();
          }
        }
      }
    }
  }
}
