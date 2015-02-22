package ungenutzt;


import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;


import javax.swing.JComboBox;




import data.Recipe;


import util.ToolConstants;

public class RecipeListPanel extends JPanel {

  private MainWindow        _parent;

  private JPanel            _filterPanel;
  private JTextField        _filterfield;
  private JCheckBox         _filterCheckBox;
  private JComboBox         _filterComboBox;
  private JComboBox<String> _filterSelectionComboBox;

  private int               _filterProperty;
  private RecipeTable       _recipeTable;
  private RecipeDetailsModel       _recipeModel;

  private ArrayList<Recipe> _allRecipes;
  
  private ResourceBundle languageBundle;

  /**
   * Create the panel.
   * 
   * @param mainWindow
   * @param languageBundle 
   * 
   * @param _recipeTable
   */
  public RecipeListPanel(MainWindow mainWindow, RecipeTable recipeTable, ResourceBundle languageBundle) {
    setLayout(new BorderLayout(0, 0));

    _parent = mainWindow;
    _recipeTable = recipeTable;
    _recipeModel = (RecipeDetailsModel) _recipeTable.getModel();
    _allRecipes = new ArrayList<Recipe>(_recipeModel.getRecipes());
    this.languageBundle = languageBundle;

    _filterPanel = new JPanel();
    add(_filterPanel, BorderLayout.NORTH);
    _filterPanel.setLayout(new BoxLayout(_filterPanel, BoxLayout.X_AXIS));

    _filterfield = new JTextField();
    _filterPanel.add(_filterfield);
    _filterCheckBox = new JCheckBox();
    _filterComboBox = new JComboBox(_recipeTable.kindOfMealValuesToString(ToolConstants.KINDOFMEAL.values()));

    JButton filterButton = new JButton(languageBundle.getString("Filter"));
    _filterPanel.add(filterButton);
    filterButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        Object[] filterValues = { _filterfield.getText(), _filterComboBox.getSelectedIndex(), _filterCheckBox.isSelected() };
        filter(filterValues);
      }
    });

    JButton resetButton = new JButton(languageBundle.getString("Reset"));
    _filterPanel.add(resetButton);

    _filterSelectionComboBox = new JComboBox<String>(_recipeModel.getColumnNames());
    _filterPanel.add(_filterSelectionComboBox);
    _filterSelectionComboBox.addItem(languageBundle.getString("All"));
    _filterSelectionComboBox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
          Object item = event.getItem();
          setFilterProperties(item);
        }
      }
    });

    resetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        reset();
      }
    });

    JScrollPane scrollPane = new JScrollPane(_recipeTable);
    float[] detailsRecipecolumnWidths = new float[] {0.40f, 0.10f, 0.40f, 0.40f, 0.10f, 0.10f};
    _recipeTable.setColumnWidths(scrollPane.getPreferredSize(), detailsRecipecolumnWidths);
    add(scrollPane, BorderLayout.CENTER);
  }

  private void setFilterProperties(Object item) {
    int filterField = -1;
    for (int i = 0; i < _recipeModel.getColumnCount(); i++) {
      String columnName = _recipeModel.getColumnName(i);
      if (item.toString().equals(columnName)) {
        Dimension firstComponentDimension = new Dimension(300, 10);
        if (_recipeModel.getColumnClass(i) == String.class) {
          _filterfield.setPreferredSize(firstComponentDimension);
          _filterPanel.remove(0);
          _filterPanel.add(_filterfield, 0);
        }
        else if (_recipeModel.getColumnClass(i) == Enum.class) {
          _filterComboBox.setPreferredSize(firstComponentDimension);
          _filterPanel.remove(0);
          _filterPanel.add(_filterComboBox, 0);
        }
        else if (_recipeModel.getColumnClass(i) == Boolean.class) {
          _filterCheckBox.setPreferredSize(firstComponentDimension);
          _filterPanel.remove(0);
          _filterPanel.add(_filterCheckBox, 0);
        }
        else if (_recipeModel.getColumnClass(i) == Long.class) {
          _filterCheckBox.setPreferredSize(firstComponentDimension);
          _filterPanel.remove(0);
          _filterPanel.add(new JLabel("TODO"), 0);
        }
        filterField = i;
      }
    }
    if (filterField == -1) {
      _filterfield.setPreferredSize(new Dimension(100, 10));
      _filterComboBox.setPreferredSize(new Dimension(100, 10));
      _filterCheckBox.setPreferredSize(new Dimension(25, 10));
      _filterPanel.remove(0);
      JPanel allFiltersPanel = new JPanel();
      BoxLayout allFiltersLayout = new BoxLayout(allFiltersPanel, BoxLayout.X_AXIS);
      allFiltersPanel.setLayout(allFiltersLayout);
      allFiltersPanel.add(_filterfield);
      allFiltersPanel.add(_filterComboBox);
      allFiltersPanel.add(_filterCheckBox);
      _filterPanel.add(allFiltersPanel, 0);
      filterField = _recipeModel.getColumnCount();
    }
    _filterProperty = filterField;
    _filterPanel.updateUI();
  }

  public void filter(Object[] filterValue) {
    for (Recipe recipe : _allRecipes) {
      boolean isNotInFilterButInList = false;
      boolean isInFilterButNotInList = false;
      if (_filterSelectionComboBox.getItemAt(_filterProperty).equals(languageBundle.getString("All"))) {
        isNotInFilterButInList = checkAllForRemoval(recipe, filterValue);
        isInFilterButNotInList = checkAllForAddition(recipe, filterValue);
      }
      else if (_recipeModel.getColumnName(_filterProperty).equals(languageBundle.getString("Recipe"))) {
        isNotInFilterButInList = !checkString(recipe.getName(), filterValue) && checkContaied(recipe);
        isInFilterButNotInList = checkString(recipe.getName(), filterValue) && !checkContaied(recipe);
      }
      else if (_recipeModel.getColumnName(_filterProperty).equals(languageBundle.getString("Duration"))) {
        isNotInFilterButInList = !checkString(recipe.getDuration(), filterValue) && checkContaied(recipe);
        isInFilterButNotInList = checkString(recipe.getDuration(), filterValue) && !checkContaied(recipe);
      }
      else if (_recipeModel.getColumnName(_filterProperty).equals(languageBundle.getString("Type"))) {
        isNotInFilterButInList = !checkString(recipe.getType(), filterValue) && checkContaied(recipe);
        isInFilterButNotInList = checkString(recipe.getType(), filterValue) && !checkContaied(recipe);
      }
      else if (_recipeModel.getColumnName(_filterProperty).equals(languageBundle.getString("KindOfMeal"))) {
        isNotInFilterButInList = !checkEnum(recipe.getKindOfMeal(), filterValue) && checkContaied(recipe);
        isInFilterButNotInList = checkEnum(recipe.getKindOfMeal(), filterValue) && !checkContaied(recipe);
      }
      else if (_recipeModel.getColumnName(_filterProperty).equals(languageBundle.getString("Used"))) {
        isNotInFilterButInList = !checkBoolean(recipe.isUsed(), filterValue) && checkContaied(recipe);
        isInFilterButNotInList = checkBoolean(recipe.isUsed(), filterValue) && !checkContaied(recipe);
      }
      filterRecipe(recipe, isNotInFilterButInList, isInFilterButNotInList);
    }
    _parent.updateTables();
  }

  private void filterRecipe(Recipe recipe, boolean isNotInFilterButInList, boolean isInFilterButNotInList) {
    if (isNotInFilterButInList) {
      int[] recipeRows = { _recipeModel.getRowByRecipe(recipe)};
      _recipeModel.deleteSelectedRows(recipeRows, false);
    }
    else if (isInFilterButNotInList) {
      _recipeModel.addRow(recipe, false);
    }
  }

  public void reset() {
    _filterSelectionComboBox.setSelectedIndex(0);
    _filterfield.setText("");
    Object[] empty = { "", 0, false };
    filter(empty);
  }

  public boolean checkString(String recipeValue, Object[] compareValue) {
    return recipeValue.toLowerCase().contains(((String) compareValue[0]).toLowerCase());
  }

  public boolean checkBoolean(boolean recipeValue, Object compareValue[]) {
    return recipeValue == (boolean) compareValue[2];
  }

  public boolean checkEnum(ToolConstants.KINDOFMEAL recipeValue, Object compareValue[]) {
    return recipeValue == ToolConstants.KINDOFMEAL.intToMeal((Integer) compareValue[1]);
  }

  public boolean checkContaied(Recipe recipe) {
    return _recipeModel.getRecipes().contains(recipe);
  }

  public boolean checkAllForRemoval(Recipe recipe, Object compareValue[]) {
    boolean isNotRecipe = !checkString(recipe.getName(), compareValue);
    boolean isNotDuration = !checkString(recipe.getDuration(), compareValue);
    boolean isNotType = !checkString(recipe.getType(), compareValue);
    boolean isNotKind = !checkEnum(recipe.getKindOfMeal(), compareValue);
    boolean isNotUsed = !checkBoolean(recipe.isUsed(), compareValue);
    boolean isContained = checkContaied(recipe);
    boolean all = ((isNotRecipe && isNotDuration && isNotType) || isNotKind || isNotUsed) && isContained;
    return all;
  }

  public boolean checkAllForAddition(Recipe recipe, Object compareValue[]) {
    boolean isRecipe = checkString(recipe.getName(), compareValue);
    boolean isDuration = checkString(recipe.getDuration(), compareValue);
    boolean isType = checkString(recipe.getType(), compareValue);
    boolean isKind = checkEnum(recipe.getKindOfMeal(), compareValue);
    boolean isUsed = checkBoolean(recipe.isUsed(), compareValue);
    boolean isNotContained = !checkContaied(recipe);
    boolean all = (isRecipe || isDuration || isType) && isKind && isUsed && isNotContained;
    return all;
  }

  public ArrayList<Recipe> getAllRecipes() {
    return _allRecipes;
  }

  public RecipeTable getRecipeTable() {
    return _recipeTable;
  }
}
