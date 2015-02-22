package ungenutzt;

import java.awt.Checkbox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import data.Ingredient;
import data.Recipe;
import sun.awt.IconInfo;

public class HeaderToolBar extends JToolBar {
  
  private static final String IconLocation = "WebContent/images/icons/";

  private MainUndoManager _undoManager;
  private MainWindow      mainWindow;
  private ResourceBundle  languageBundle;

  private JButton         addRecipe;
  // private JButton deleteRecipe;
  private JButton         deleteSelectedRecipe;
  private JButton         addIngredient;
  // private JButton deleteIngredient;
  private JButton         deleteSelectedIngredient;
  private JButton         undo;
  private JButton         redo;
  private JButton         print;
  private JButton         save;
  private JCheckBox       activateHue;

  public HeaderToolBar(MainWindow mainWindow, MainUndoManager undoManager, ResourceBundle languageBundle) {
    super();
    setEnabled(false);

    _undoManager = undoManager;
    this.mainWindow = mainWindow;
    this.languageBundle = languageBundle;

    // addRecipe = new JButton(languageBundle.getString("AddRecipe"), new
    // ImageIcon("ressources/icons/add_bg.png"));
    addRecipe = new JButton(new ImageIcon(((new ImageIcon(IconLocation + "add_recipe.png")).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    add(addRecipe);
    // deleteRecipe = new JButton(languageBundle.getString("DeleteRecipe"));
    // add(deleteRecipe);
    // deleteSelectedRecipe = new
    // JButton(languageBundle.getString("DeleteSelectedRecipe"));
    deleteSelectedRecipe = new JButton(new ImageIcon(((new ImageIcon(IconLocation + "remove_recipe.png")).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    add(deleteSelectedRecipe);
    addSeparator();

    // addIngredient = new JButton(languageBundle.getString("AddIngredient"));
    addIngredient = new JButton(new ImageIcon(((new ImageIcon(IconLocation + "add_pot.png")).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    add(addIngredient);
    // deleteIngredient = new
    // JButton(languageBundle.getString("DeleteIngredient"));
    // add(deleteIngredient);
    // deleteSelectedIngredient = new
    // JButton(languageBundle.getString("DeleteSelectedIngredient"));
    deleteSelectedIngredient = new JButton(new ImageIcon(((new ImageIcon(IconLocation + "remove_pot.png")).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    add(deleteSelectedIngredient);
    addSeparator();

    // undo = new JButton(languageBundle.getString("Undo"));
    undo = new JButton(new ImageIcon(((new ImageIcon(IconLocation + "undone_bg.png")).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    add(undo);
    _undoManager.watchUndoComponent(undo);
    // redo = new JButton(languageBundle.getString("Redo"));
    redo = new JButton(new ImageIcon(((new ImageIcon(IconLocation + "redone_bg.png")).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    add(redo);
    _undoManager.watchRedoComponent(redo);
    addSeparator();

    // print = new JButton(languageBundle.getString("Print"));
    print = new JButton(new ImageIcon(((new ImageIcon(IconLocation + "print_bg.png")).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    add(print);
    addSeparator();

//    save = new JButton(languageBundle.getString("Save"));
    save = new JButton(new ImageIcon(((new ImageIcon(IconLocation + "save_bg.png")).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    add(save);

    activateHue = new JCheckBox(languageBundle.getString("SetHued"), true);
    add(activateHue);

    addRecipe.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        DialogGetRecipe dialog = new DialogGetRecipe(getMainWindow(), getLanguageBundle());
        if (dialog.isConfirmed()) {
          RecipeDetailsModel model = (RecipeDetailsModel) getMainWindow().getRecipeDetailsTable().getModel();
          model.addRow(dialog.getRecipe(), true);
          getMainWindow().getRecipeListPanel().getAllRecipes().add(dialog.getRecipe());
          getMainWindow().updateTables();
          int index = getMainWindow().getRecipeDetailsTable().convertRowIndexToView(model.getRowByRecipe(dialog.getRecipe()));
          getMainWindow().getRecipeDetailsTable().getSelectionModel().setSelectionInterval(index, index);
        }
      }
    });

    // deleteRecipe.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent arg0) {
    // if (getMainWindow().getRecipeDetailsTable().getModel().getRowCount() > 0)
    // {
    // int[] lastRow = {
    // getMainWindow().getRecipeDetailsTable().convertRowIndexToModel(getMainWindow().getRecipeDetailsTable().getModel().getRowCount()
    // - 1) };
    // getMainWindow().getRecipeListPanel().getAllRecipes().remove(((RecipeDetailsModel)
    // getMainWindow().getRecipeDetailsTable().getModel()).getRecipes().get(lastRow[0]));
    // ((RecipeDetailsModel)
    // getMainWindow().getRecipeDetailsTable().getModel()).deleteSelectedRows(lastRow,
    // true);
    // getMainWindow().updateTables();
    // }
    // }
    // });

    deleteSelectedRecipe.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        int[] rows = getMainWindow().getRecipeDetailsTable().getConvertedRows(getMainWindow().getRecipeDetailsTable().getSelectedRows());
        for (int i = 0; i < rows.length; i++) {
          Recipe toDeleteRecipe = ((RecipeDetailsModel) getMainWindow().getRecipeDetailsTable().getModel()).getRecipes().get(rows[i]);
          for (PlanerDayPanel element : getMainWindow().getRecipePlanerPanel().getDayPanelList()) {
            if (element.getDayPlan().getRecipes().contains(toDeleteRecipe)) {
              element.removeRecipe(toDeleteRecipe);
            }
          }
          getMainWindow().getRecipeListPanel().getAllRecipes().remove(toDeleteRecipe);
        }
        ((RecipeDetailsModel) getMainWindow().getRecipeDetailsTable().getModel()).deleteSelectedRows(rows, true);
        getMainWindow().updateTables();
      }
    });

    addIngredient.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        int[] selectedRecipeRows = getMainWindow().getRecipeDetailsTable().getConvertedRows(getMainWindow().getRecipeDetailsTable().getSelectedRows());
        for (int i = 0; i < selectedRecipeRows.length; i++) {
          Recipe recipe = ((RecipeDetailsModel) getMainWindow().getRecipeDetailsTable().getModel()).getRecipes().get(selectedRecipeRows[i]);
          Ingredient ingred = new Ingredient("", "", recipe, -1);
          recipe.addIngredient(ingred);
          ((IngredientDetailsModel) getMainWindow().getIngredientDetailsTable().getModel()).addRow(ingred, true);
        }
        getMainWindow().restoreTableSelection(getMainWindow().getIngredientDetailsTable());
      }
    });

    // deleteIngredient.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent arg0) {
    // if (getMainWindow().getIngredientDetailsTable().getModel().getRowCount()
    // > 0) {
    // int[] lastRow = {
    // getMainWindow().getIngredientDetailsTable().convertRowIndexToModel(getMainWindow().getIngredientDetailsTable().getModel().getRowCount()
    // - 1) };
    // ((IngredientDetailsModel)
    // getMainWindow().getIngredientDetailsTable().getModel()).deleteSelectedRows(lastRow,
    // false, true);
    // getMainWindow().restoreTableSelection(getMainWindow().getIngredientDetailsTable());
    // }
    // }
    // });

    deleteSelectedIngredient.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        int[] rows = getMainWindow().getIngredientDetailsTable().getConvertedRows(getMainWindow().getIngredientDetailsTable().getSelectedRows());
        ((IngredientDetailsModel) getMainWindow().getIngredientDetailsTable().getModel()).deleteSelectedRows(rows, false, true);
        getMainWindow().getIngredientDetailsTable().clearSelection();
        getMainWindow().restoreTableSelection(getMainWindow().getIngredientDetailsTable());
      }
    });

    undo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        _undoManager.undo();
        getMainWindow().updateTables();
      }
    });

    redo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        _undoManager.redo();
        getMainWindow().updateTables();
      }
    });

    print.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        DialogPrintPreview printPreview = new DialogPrintPreview(getMainWindow(), getLanguageBundle());
        printPreview.setVisible(true);
      }
    });

    save.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        getMainWindow().storeRecipes();
        getMainWindow().reloadFinderModel();
        getMainWindow().updateTables();
      }
    });

    activateHue.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        getMainWindow().getRecipeFinderCellRenderer().setHued(((JCheckBox) arg0.getSource()).isSelected());
        getMainWindow().getRecipeFinderTable().updateUI();
      }
    });

    activateDetailsView();
  }

  public ResourceBundle getLanguageBundle() {
    return languageBundle;
  }

  public void setLanguageBundle(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
  }

  public MainWindow getMainWindow() {
    return mainWindow;
  }

  public void activateDetailsView() {
    addRecipe.setEnabled(true);
    // deleteRecipe.setEnabled(true);
    deleteSelectedRecipe.setEnabled(true);
    addIngredient.setEnabled(true);
    // deleteIngredient.setEnabled(true);
    deleteSelectedIngredient.setEnabled(true);
    // undo.setEnabled(true);
    // redo.setEnabled(true);
    print.setEnabled(true);
    save.setEnabled(true);
    activateHue.setEnabled(false);
  }

  public void activateFinderView() {
    addRecipe.setEnabled(false);
    // deleteRecipe.setEnabled(false);
    deleteSelectedRecipe.setEnabled(false);
    addIngredient.setEnabled(false);
    // deleteIngredient.setEnabled(false);
    deleteSelectedIngredient.setEnabled(false);
    // undo.setEnabled(true);
    // redo.setEnabled(true);
    print.setEnabled(true);
    save.setEnabled(true);
    activateHue.setEnabled(true);
  }
}
