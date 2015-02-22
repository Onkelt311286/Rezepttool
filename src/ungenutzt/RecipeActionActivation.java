package ungenutzt;

import javax.swing.undo.AbstractUndoableEdit;

import data.Recipe;



public class RecipeActionActivation extends AbstractUndoableEdit {
  
  RecipeDetailsModel _model;
  Recipe      _recipe;

  public RecipeActionActivation(Recipe recipe, RecipeDetailsModel model) {
    super();
    _recipe = recipe;
    _model = model;
  }
  
  @Override
  public void undo() {
    super.undo();
    changeUse();
  }
  
  @Override
  public void redo() {
    super.redo();
    changeUse();
  }
  
  public void changeUse(){
    _model.changeRecipeActivation(_recipe);
    _recipe.setUsed(!_recipe.isUsed());
  }
}
