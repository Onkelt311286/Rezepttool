package ungenutzt;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.TableModel;

import recipedetails.ingredient.IngredientDetailsModel;


public class RecipeModelListener implements TableModelListener {

  IngredientDetailsModel _ingredientModel;
  boolean _wasUndone;

  public RecipeModelListener(IngredientDetailsModel ingredientModel) {
    _ingredientModel = ingredientModel;
    _wasUndone = false;
  }

  @Override
  public void tableChanged(TableModelEvent event) {
//    System.out.println("table Changed was invoked");
//    
//    RecipeModel model = (RecipeModel) event.getSource();
//    
//    Recipe recipe = model.getLastUpdatedRecipe();
////    if(recipe == null){
////      recipe = model.getRecipes().get(event.getFirstRow());
////    }
//    
//    
//    
//    if (recipe.isUsed() == true) {
//      for (Ingredient ingred : recipe.getIngredients()) {
//        _ingredientModel.addRow(ingred, false);
//      }
//    }
//    else {
//      _ingredientModel.deleteSelectedRows(model.getIngredientRows(recipe), true, false);
//    }
  }
}
