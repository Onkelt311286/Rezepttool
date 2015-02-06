package recipedetails.actions;

import javax.swing.undo.AbstractUndoableEdit;

import mainwindow.data.Recipe;

import util.AbstractRecipeModel;



public class RecipeActionAdd extends AbstractUndoableEdit {

  AbstractRecipeModel _model;
  Recipe      _recipe;

  public RecipeActionAdd(AbstractRecipeModel recipeModel, Recipe recipe) {
    super();
    _recipe = recipe;
    _model = recipeModel;
  }

  @Override
  public void undo() {
    super.undo();
    int[] recipePositions = {_model.getRowByRecipe(_recipe)};
    _model.deleteSelectedRows(recipePositions, false);
  }
  
  @Override
  public void redo() {
    super.redo();
    _model.addRow(_recipe, false);
  }
}
