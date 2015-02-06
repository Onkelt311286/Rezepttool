package recipedetails.actions;

import javax.swing.undo.AbstractUndoableEdit;

import mainwindow.data.Ingredient;

import util.AbstractIngredientModel;



public class IngredientActionDelete extends AbstractUndoableEdit {

  Ingredient[] _ingreds;
  AbstractIngredientModel _model;

  public IngredientActionDelete(AbstractIngredientModel ingredientModel, Ingredient[] ingreds) {
    super();
    _ingreds = ingreds;
    _model = ingredientModel;
  }

  @Override
  public void undo() {
    super.undo();
    for (Ingredient ingredient : _ingreds) {
      _model.addRow(ingredient, false);
    }
  }
  
  @Override
  public void redo() {
    super.redo();    
    int[] ingredPositions = new int[_ingreds.length];
    for (int i = 0; i < _ingreds.length; i++) {
      ingredPositions[i] = _model.getRowByIngredient(_ingreds[i]);
    }
    _model.deleteSelectedRows(ingredPositions, true, false);
  }
}
