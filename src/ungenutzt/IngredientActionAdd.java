package ungenutzt;

import javax.swing.undo.AbstractUndoableEdit;

import data.Ingredient;



public class IngredientActionAdd extends AbstractUndoableEdit {

  Ingredient _ingred;
  AbstractIngredientModel model;

  public IngredientActionAdd(AbstractIngredientModel ingredientModel, Ingredient ingred) {
    super();
    _ingred = ingred;
    model = ingredientModel;
  }

  @Override
  public void undo() {
    super.undo();
    int[] ingredPositions = {model.getRowByIngredient(_ingred)};
    model.deleteSelectedRows(ingredPositions, false, false);
  }
  
  @Override
  public void redo() {
    super.redo();
    model.addRow(_ingred, false);
  }
}
