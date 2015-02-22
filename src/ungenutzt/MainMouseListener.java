package ungenutzt;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;

import javax.swing.event.UndoableEditListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.undo.AbstractUndoableEdit;

import data.Recipe;

public class MainMouseListener implements MouseListener {

  private MainWindow          mainWindow;
  private DayPanelKeyListener keyListener;
  private ResourceBundle      languageBundle;

  public MainMouseListener(MainWindow mainWindow, DayPanelKeyListener keyListener, ResourceBundle languageBundle) {
    this.mainWindow = mainWindow;
    this.keyListener = keyListener;
    this.languageBundle = languageBundle;
  }

  @Override
  public void mouseClicked(MouseEvent event) {
  }

  @Override
  public void mouseEntered(MouseEvent event) {
    changeDayPanelBackground(event, PlanerDayPanel.MOUSESTATUS.HOVERED);
  }

  @Override
  public void mouseExited(MouseEvent event) {
    changeDayPanelBackground(event, PlanerDayPanel.MOUSESTATUS.NONE);
  }

  @Override
  public void mousePressed(MouseEvent event) {
  }

  @Override
  public void mouseReleased(MouseEvent event) {
    if (event.getComponent().equals(mainWindow.getRecipeDetailsTable())) {
      int row = mainWindow.getRecipeDetailsTable().convertRowIndexToModel(mainWindow.getRecipeDetailsTable().getSelectedRow());
      AbstractRecipeModel recipeModel = (AbstractRecipeModel) mainWindow.getRecipeDetailsTable().getModel();
      Recipe selectedRecipe = recipeModel.getRecipes().get(row);
      planRecipe(mainWindow.getRecipeDetailsTable().getEditingColumn(), recipeModel, selectedRecipe);
//      mainWindow.restoreTableSelection(mainWindow.getIngredientDetailsTable());
    }
    else if (event.getComponent().equals(mainWindow.getIngredientDetailsTable())) {
      // mainWindow.restoreTableSelection(mainWindow.getIngredientDetailsTable());
    }
    else if (event.getComponent().equals(mainWindow.getRecipeFinderTable())) {
      int row = mainWindow.getRecipeFinderTable().convertRowIndexToModel(mainWindow.getRecipeFinderTable().getSelectedRow());
      AbstractRecipeModel recipeModel = (AbstractRecipeModel) mainWindow.getRecipeFinderTable().getModel();
      Recipe selectedRecipe = recipeModel.getRecipes().get(row);
      planRecipe(mainWindow.getRecipeFinderTable().getEditingColumn(), recipeModel, selectedRecipe);
      
//      mainWindow.getIngredientFinderTable().clearSelection();
//      mainWindow.restoreTableSelection(mainWindow.getIngredientFinderTable());
    }
    else if (event.getComponent().equals(mainWindow.getIngredientFinderTable())) {
//      int[] storage = mainWindow.getRecipeFinderTable().getSelectedRowsStorage();
//      System.out.println("size = " + storage.length);
//      mainWindow.getRecipeFinderTable().storeSelectedRows();
//      System.out.println("size = " + storage.length);
//      mainWindow.getRecipeFinderTable().clearSelection();
//      System.out.println("size = " + storage.length);
//      mainWindow.restoreTableSelection(mainWindow.getRecipeFinderTable());
//      mainWindow.getRecipeFinderTable();
//       mainWindow.restoreTableSelection(mainWindow.getRecipeFinderTable());
      // mainWindow.restoreTableSelection(mainWindow.getIngredientDetailsTable());
    }
    else if (mainWindow.getRecipePlanerPanel().contains(event.getPoint())) {
      mainWindow.getRecipePlanerPanel().requestFocus();
      changeDayPanelBackground(event, PlanerDayPanel.MOUSESTATUS.CLICKED);
      
      // Sortiere nach Aktivierten Rezepten für den Tag und dann nach Namen
      mainWindow.getRecipeDetailsTable().getRowSorter().toggleSortOrder(0);
      mainWindow.getRecipeDetailsTable().getRowSorter().toggleSortOrder(5);
      mainWindow.getRecipeDetailsTable().getRowSorter().toggleSortOrder(5);
    }
  }

  private void planRecipe(int column, AbstractRecipeModel recipeModel, Recipe selectedRecipe) {
    if (column != -1 && recipeModel.getColumnName(column).equals(languageBundle.getString("Used"))) {
      if (selectedRecipe.isUsed()) {
        removeRecipeFromPlan(recipeModel, selectedRecipe);
      }
      else {
        addRecipeToPlan(recipeModel, selectedRecipe);
      }
    }
  }

  private void addRecipeToPlan(AbstractRecipeModel recipeModel, Recipe selectedRecipe) {
    for (PlanerDayPanel selectedPanel : mainWindow.getRecipePlanerPanel().getSelectedPanels()) {
      selectedPanel.addRecipe(selectedRecipe);
      addUndoableEdit(new DayPanelActionAddRecipe(selectedRecipe, selectedPanel), recipeModel);
    }
  }

  private void removeRecipeFromPlan(AbstractRecipeModel recipeModel, Recipe selectedRecipe) {
    for (PlanerDayPanel selectedPanel : mainWindow.getRecipePlanerPanel().getSelectedPanels()) {
      if (selectedPanel.getDayPlan().getRecipes().contains(selectedRecipe)) {
        selectedPanel.removeRecipe(selectedRecipe);
        addUndoableEdit(new DayPanelActionRemoveRecipe(selectedRecipe, selectedPanel), recipeModel);
      }
    }
  }

  public void changeDayPanelBackground(MouseEvent event, PlanerDayPanel.MOUSESTATUS status) {
    for (PlanerDayPanel dayPanel : mainWindow.getRecipePlanerPanel().getDayPanelList()) {
      if (status == PlanerDayPanel.MOUSESTATUS.CLICKED && !keyListener.isSTRGPressed()) {
        for (Recipe recipe : dayPanel.getDayPlan().getRecipes()) {
          recipe.setUsed(false);
        }
        dayPanel.setClicked(false);
        dayPanel.setBackground(javax.swing.UIManager.getDefaults().getColor("SystemColor.window"));
        // Hier werden die Tabellen einzeln upgedated, damit die Sortierung
        // nicht ver�ndert wird.
        mainWindow.getRecipeDetailsTable().updateUI();
        mainWindow.getRecipeFinderTable().updateUI();
      }
    }
    if (event.getComponent() instanceof PlanerDayPanel) {
      PlanerDayPanel dayPanel = (PlanerDayPanel) event.getComponent();
      dayPanel.changePlanStatus(status);
      // Hier werden die Tabellen einzeln upgedated, damit die Sortierung nicht
      // ver�ndert wird.
      mainWindow.getRecipeDetailsTable().updateUI();
      mainWindow.getRecipeFinderTable().updateUI();
    }
  }

  public void addUndoableEdit(AbstractUndoableEdit edit, AbstractTableModel model) {
    UndoableEditListener listeners[] = ((AbstractTableModel) mainWindow.getIngredientDetailsTable().getModel()).getListeners(UndoableEditListener.class);
    for (UndoableEditListener listener : listeners) {
      MainUndoManager aum = (MainUndoManager) listener;
      aum.registerUndoableEdit(edit, model);
    }
  }
}
