package ungenutzt;

import java.awt.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import mainwindow.MainWindow;
import mainwindow.data.Ingredient;
import mainwindow.data.Recipe;
import recipedetails.ingredient.IngredientDetailsModel;
import recipefinder.ingredient.IngredientFinderModel;

public class IngredientTableModelListener implements TableModelListener {

//  private MainWindow     mainWindow;
//  private ResourceBundle languageBundle;
//
//  public IngredientTableModelListener(MainWindow mainWindow, ResourceBundle languageBundle) {
//    this.mainWindow = mainWindow;
//    this.languageBundle = languageBundle;
//  }
//
  @Override
  public void tableChanged(TableModelEvent event) {
//    if (event.getType() == TableModelEvent.UPDATE) {
//      if (event.getSource() instanceof IngredientFinderModel) {
//        // mainWindow.getRecipeFinderTable().clearSelection();
//
////        System.out.println("updating");
//        
//        IngredientFinderModel ingredientFinderModel = (IngredientFinderModel) mainWindow.getIngredientFinderTable().getModel();
//        Ingredient ingred = ingredientFinderModel.getIngredients().get(event.getFirstRow());
//        mainWindow.updateRecipes(ingredientFinderModel.getLastUpdatedValue(), ingred);
//        // 2 mal f�r absteigend, alternativ Setzen der SortKeys
//        // TODO: store selection -> clear Selection -> sort -> restore selection
//        // mainWindow.restoreTableSelection(mainWindow.getRecipeFinderTable());
//
//        mainWindow.getRecipeFinderTable().getRowSorter().toggleSortOrder(4);
//        mainWindow.getRecipeFinderTable().getRowSorter().toggleSortOrder(4);
////        int[] storage = mainWindow.getRecipeFinderTable().getSelectedRowsStorage();
////        System.out.println("size = " + storage.length);
////        for (int i = 0; i < storage.length; i++) {
////          System.out.println(i + " = " + storage[i]);
////          mainWindow.getRecipeFinderTable().getSelectionModel().addSelectionInterval(storage[i], storage[i]);
////        }
//
//      }
//      else if (event.getSource() instanceof IngredientDetailsModel) {
//        IngredientDetailsModel model = (IngredientDetailsModel) event.getSource();
//        Ingredient ingred = model.getIngredients().get(event.getFirstRow());
//        if (model.getColumnName(event.getColumn()).equals(languageBundle.getString("Available"))) {
//          mainWindow.completeIngredientUpdate(ingred, model.getLastUpdatedValue());
//        }
//        else if (model.getColumnName(event.getColumn()).equals(languageBundle.getString("Ingredient")) || model.getColumnName(event.getColumn()).equals(languageBundle.getString("StorePlace"))) {
//          if (isUpdateNecessary(model, ingred)) {
//            int option = JOptionPane.showConfirmDialog(null, languageBundle.getString("UpdateIngredientsMessage"), languageBundle.getString("UpdateIngredientsTitle"), JOptionPane.YES_NO_OPTION);
//            if (option == JOptionPane.YES_OPTION) {
//              mainWindow.completeIngredientUpdate(ingred, model.getLastUpdatedValue());
//            }
//            else {
//              for (Ingredient storedIngred : getSameIngredients(ingred, model.getLastUpdatedValue())) {
//                if (!storedIngred.equals(ingred)) {
//                  ingred.setAvailable(storedIngred.isAvailable());
//                  mainWindow.restoreTableSelection(mainWindow.getIngredientDetailsTable());
//                  break;
//                }
//              }
//            }
//          }
//        }
//      }
//    }
  }
//
//  private boolean isUpdateNecessary(IngredientDetailsModel model, Ingredient ingred) {
//    boolean result = true;
//    if (model.getLastUpdatedValue().equals(ingred.getName()) || model.getLastUpdatedValue().equals(ingred.getStorePlace())) {
//      result = false;
//    }
//    else {
//      ArrayList<Ingredient> otherIngreds = getSameIngredients(ingred, model.getLastUpdatedValue());
//      if (otherIngreds.size() <= 1) {
//        result = false;
//      }
//    }
//    return result;
//  }
//
//  private ArrayList<Ingredient> getSameIngredients(Ingredient sourceIngred, Object lastValue) {
//    ArrayList<Ingredient> result = new ArrayList<Ingredient>();
//    for (Recipe recipe : mainWindow.getRecipeListPanel().getAllRecipes()) {
//      for (Ingredient detailsIngred : recipe.getIngredients()) {
//        if (mainWindow.isSameIngredient(detailsIngred, sourceIngred, lastValue)) {
//          result.add(detailsIngred);
//        }
//      }
//    }
//    return result;
//  }
}
