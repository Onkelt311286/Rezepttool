package ungenutzt;

import javax.swing.table.AbstractTableModel;
import javax.swing.undo.AbstractUndoableEdit;

import data.Recipe;



public class RecipeActionDelete extends AbstractUndoableEdit {

  Recipe[] _recipes;
  AbstractRecipeModel _model;
  
  public RecipeActionDelete(AbstractRecipeModel recipeModel, Recipe[] recipes) {
    super();
    _recipes = recipes;
    _model = recipeModel;
  }

  @Override
  public void undo() {
    super.undo();
    for (Recipe recipe : _recipes) {
      _model.addRow(recipe, false);
    }
  }
  
  @Override
  public void redo() {
    super.redo();    
    int[] recipePositions = new int[_recipes.length];
    for (int i = 0; i < _recipes.length; i++) {
      recipePositions[i] = _model.getRowByRecipe(_recipes[i]);
    }
    _model.deleteSelectedRows(recipePositions, false);
  }

}
