package ungenutzt;


import javax.swing.undo.AbstractUndoableEdit;

import data.Recipe;




public class DayPanelActionAddRecipe extends AbstractUndoableEdit {

  Recipe   recipe;
  PlanerDayPanel dayPanel;

  public DayPanelActionAddRecipe(Recipe recipe, PlanerDayPanel dayPanel) {
    super();
    this.recipe = recipe;
    this.dayPanel = dayPanel;
  }

  @Override
  public void undo() {
    super.undo();
    if (dayPanel.isClicked) {
      recipe.setUsed(false);
    }
    dayPanel.removeRecipe(recipe);
  }

  @Override
  public void redo() {
    super.redo();
    if (dayPanel.isClicked) {
      recipe.setUsed(true);
    }
    dayPanel.addRecipe(recipe);
  }
}
