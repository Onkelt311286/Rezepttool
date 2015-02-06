package mainwindow;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import mainwindow.data.Ingredient;
import mainwindow.data.Recipe;
import mainwindow.daypanel.DayPanelKeyListener;
import mainwindow.header.HeaderMenuBar;
import mainwindow.header.HeaderToolBar;
import mainwindow.planer.PlanerPanel;
import recipedetails.ingredient.IngredientDetailsCellEditedAction;
import recipedetails.ingredient.IngredientDetailsModel;
import recipedetails.recipe.RecipeDetailsModel;
import recipedetails.recipe.RecipeFormulaDocumentListener;
import recipedetails.recipe.RecipeFormulaPanel;
import recipedetails.recipe.RecipeListPanel;
import recipedetails.recipe.RecipeTable;
import recipefinder.ingredient.IngredientFinderCellEditedAction;
import recipefinder.ingredient.IngredientFinderModel;
import recipefinder.ingredient.IngredientFinderTableCellRenderer;
import recipefinder.recipe.RecipeFinderModel;
import recipefinder.recipe.RecipeFinderTableCellRenderer;
import ungenutzt.IngredientTableModelListener;
import util.AbstractTable;
import util.DataBaseControl;
import util.TableCellListener;
import util.XMLConfigurationLoader;

public class MainWindow extends JFrame implements WindowListener {

  private IngredientDetailsModel        ingredientDetailsModel;
  private AbstractTable                 ingredientDetailsTable;
  private IngredientFinderModel         ingredientFinderModel;
  private AbstractTable                 ingredientFinderTable;

  private PlanerPanel                   recipePlanerPanel;
  private RecipeListPanel               recipeListPanel;
  private RecipeFormulaPanel            recipeFormulaPanel;
  private RecipeDetailsModel            recipeDetailsModel;
  private RecipeTable                   recipeDetailsTable;
  private RecipeFinderModel             recipeFinderModel;
  private AbstractTable                 recipeFinderTable;
  private RecipeFinderTableCellRenderer recipeRenderer;

  private MainUndoManager               undoManager;
  private DataBaseControl               dataBaseControl;
  private XMLConfigurationLoader        configLoader;
  private HeaderMenuBar                 mainMenuBar;
  private HeaderToolBar                 toolBar;
  private JTabbedPane                   mainTabbedPane;

  public MainWindow(ResourceBundle languageBundle, XMLConfigurationLoader configLoader) {
    super(languageBundle.getString("MainTitle"));
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    addWindowListener(this);

    this.configLoader = configLoader;
    mainMenuBar = new HeaderMenuBar(configLoader.getDBSourceFileName(), configLoader.getMailConfig(), languageBundle);

    undoManager = new MainUndoManager();
    undoManager.setLimit(10000);

    setJMenuBar(mainMenuBar);

    String[] detailsIngredientcolumnNames = new String[] { languageBundle.getString("Amount"), languageBundle.getString("Ingredient"), languageBundle.getString("Order"), languageBundle.getString("StorePlace"), languageBundle.getString("Recipe"), languageBundle.getString("Available") };
    float[] detailsIngredientcolumnWidths = new float[] { 0.40f, 0.40f, 0.10f, 0.40f, 0.40f, 0.10f };
    ingredientDetailsModel = new IngredientDetailsModel(detailsIngredientcolumnNames, languageBundle);
    ingredientDetailsTable = new AbstractTable();
    ingredientDetailsTable.setModel(ingredientDetailsModel);
    ingredientDetailsModel.addUndoableEditListener(undoManager);
    JScrollPane ingredientTableScrollPane = new JScrollPane(ingredientDetailsTable);
    ingredientDetailsTable.setColumnWidths(ingredientTableScrollPane.getPreferredSize(), detailsIngredientcolumnWidths);

    recipeDetailsTable = new RecipeTable(this, languageBundle);
    // ingredientDetailsTable.setOppositeTable(recipeDetailsTable);
    RecipeFormulaDocumentListener documentListener = new RecipeFormulaDocumentListener(recipeDetailsTable, languageBundle);
    recipeFormulaPanel = new RecipeFormulaPanel(documentListener);
    String[] detailsRecipeColumnNames = new String[] { languageBundle.getString("Recipe"), languageBundle.getString("Duration"), languageBundle.getString("Type"), languageBundle.getString("KindOfMeal"), languageBundle.getString("Frequency"), languageBundle.getString("Used") };
    recipeDetailsModel = new RecipeDetailsModel(this, detailsRecipeColumnNames, languageBundle);
    recipeDetailsModel.addUndoableEditListener(undoManager);
    recipeDetailsTable.setModel(recipeDetailsModel);
    // recipeDetailsTable.setOppositeTable(ingredientDetailsTable);

    String[] finderIngredientcolumnNames = new String[] { languageBundle.getString("Ingredient"), languageBundle.getString("StorePlace"), languageBundle.getString("Available") };
    float[] finderIngredientcolumnWidths = new float[] { 0.40f, 0.40f, 0.10f };
    ingredientFinderModel = new IngredientFinderModel(finderIngredientcolumnNames, languageBundle);
    ingredientFinderTable = new AbstractTable();
    ingredientFinderTable.setModel(ingredientFinderModel);
    JScrollPane finderIngredientTableScrollPane = new JScrollPane(ingredientFinderTable);
    ingredientFinderTable.setColumnWidths(finderIngredientTableScrollPane.getPreferredSize(), finderIngredientcolumnWidths);
    ingredientFinderTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

    String[] finderRecipecolumnNames = new String[] { languageBundle.getString("Recipe"), languageBundle.getString("Type"), languageBundle.getString("KindOfMeal"), languageBundle.getString("Frequency"), languageBundle.getString("Percentage"), languageBundle.getString("Used") };
    float[] finderRecipecolumnWidths = new float[] { 0.40f, 0.40f, 0.40f, 0.10f, 0.10f, 0.10f };
    recipeFinderModel = new RecipeFinderModel(finderRecipecolumnNames, ingredientFinderModel, languageBundle);
    recipeFinderModel.addUndoableEditListener(undoManager);
    recipeFinderTable = new AbstractTable();
    // recipeFinderTable.setOppositeTable(ingredientFinderTable);
    // ingredientFinderTable.setOppositeTable(recipeFinderTable);
    recipeFinderTable.setModel(recipeFinderModel);
    JScrollPane finderRecipeTableScrollPane = new JScrollPane(recipeFinderTable);
    recipeFinderTable.setColumnWidths(finderRecipeTableScrollPane.getPreferredSize(), finderRecipecolumnWidths);

    IngredientFinderTableCellRenderer ingredRenderer = new IngredientFinderTableCellRenderer(recipeFinderTable);
    ingredientFinderTable.setDefaultRenderer(String.class, ingredRenderer);
    ingredientFinderTable.setDefaultRenderer(Boolean.class, ingredRenderer);

    recipeRenderer = new RecipeFinderTableCellRenderer(ingredientFinderTable);
    recipeFinderTable.setDefaultRenderer(String.class, recipeRenderer);
    recipeFinderTable.setDefaultRenderer(Long.class, recipeRenderer);
    recipeFinderTable.setDefaultRenderer(Double.class, recipeRenderer);
    recipeFinderTable.setDefaultRenderer(Boolean.class, recipeRenderer);

    ingredientFinderTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        recipeFinderTable.updateUI();
      }
    });
    recipeFinderTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        // TODO: wirft exceptions in internen JAVA Bibliotheken, diese haben
        // aber bisher keine sichtbaren auswirkungen. Ohne das Update, wird der
        // IngredFinder View nicht aktualisiert.
        restoreTableSelection(ingredientFinderTable);
      }
    });

    dataBaseControl = new DataBaseControl(configLoader.getDBSourceFileName(), languageBundle);
    dataBaseControl.loadRecipeData(recipeDetailsModel, recipeFinderModel);
    dataBaseControl.loadIngredientData(ingredientFinderModel);
    recipeListPanel = new RecipeListPanel(this, recipeDetailsTable, languageBundle);
    // IngredientTableModelListener ingredientModelListener = new
    // IngredientTableModelListener(this, languageBundle);

    TableCellListener ingredientFinderCellListener = new TableCellListener(ingredientDetailsTable, new IngredientDetailsCellEditedAction(this, languageBundle));
    TableCellListener ingredientDetailsCellListener = new TableCellListener(ingredientFinderTable, new IngredientFinderCellEditedAction(this, languageBundle));
    // ingredientFinderModel.addTableModelListener(ingredientModelListener);
    // ingredientDetailsModel.addTableModelListener(ingredientModelListener);

    recipePlanerPanel = new PlanerPanel(dataBaseControl, recipeDetailsTable, languageBundle);

    DayPanelKeyListener keyListener = new DayPanelKeyListener();
    MainMouseListener mouseListener = new MainMouseListener(this, keyListener, languageBundle);
    ingredientFinderTable.addMouseListener(mouseListener);
    recipeFinderTable.addMouseListener(mouseListener);
    recipeDetailsTable.addMouseListener(mouseListener);
    ingredientDetailsTable.addMouseListener(mouseListener);
    recipePlanerPanel.addMouseListener(mouseListener);
    recipePlanerPanel.addKeyListener(keyListener);
    recipePlanerPanel.initDayPanelList();

    toolBar = new HeaderToolBar(this, undoManager, languageBundle);

    JSplitPane ingredientFormulaSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ingredientTableScrollPane, recipeFormulaPanel);
    JSplitPane recipeIngredientSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, recipeListPanel, ingredientFormulaSplitPane);
    JSplitPane recipeFinderSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, finderRecipeTableScrollPane, finderIngredientTableScrollPane);
    mainTabbedPane = new JTabbedPane();
    mainTabbedPane.addTab(languageBundle.getString("RecipeDeails"), recipeIngredientSplitPane);
    mainTabbedPane.addTab(languageBundle.getString("RecipeFinder"), recipeFinderSplitPane);
    mainTabbedPane.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        manageToolBarControls();
      }
    });
    JSplitPane toolBarRecipeSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolBar, mainTabbedPane);
    JSplitPane RecipePlanerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolBarRecipeSplitPane, recipePlanerPanel);
    add(RecipePlanerSplitPane);

    setMinimumSize(new Dimension(1024, 768));
    setVisible(true);
    ingredientFormulaSplitPane.setDividerLocation(500);
    ingredientFormulaSplitPane.setResizeWeight(0.5);
    recipeIngredientSplitPane.setDividerLocation(1000);
    recipeIngredientSplitPane.setResizeWeight(0.5);
    recipeFinderSplitPane.setDividerLocation(1000);
    recipeFinderSplitPane.setResizeWeight(0.5);
    toolBarRecipeSplitPane.setDividerLocation(30);
    RecipePlanerSplitPane.setDividerLocation(1000);
    RecipePlanerSplitPane.setResizeWeight(0.9);
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  }

  private void manageToolBarControls() {
    if (mainTabbedPane.getSelectedIndex() == 0) {
      toolBar.activateDetailsView();
    }
    else if (mainTabbedPane.getSelectedIndex() == 1) {
      toolBar.activateFinderView();
    }
  }

  public void updateTables() {
    restoreTableSelection(ingredientDetailsTable);
    restoreTableSelection(recipeDetailsTable);
    restoreTableSelection(ingredientFinderTable);
    restoreTableSelection(recipeFinderTable);
  }

  public void restoreTableSelection(JTable table) {
    List currentSortKeys = table.getRowSorter().getSortKeys();
    int[] rows = table.getSelectedRows();
    int[] convertedRows = new int[rows.length];

    for (int i = 0; i < rows.length; i++) {
      if (table.getModel().getRowCount() <= rows[i]) {
        convertedRows[i] = -1;
      }
      else {
        convertedRows[i] = table.convertRowIndexToModel(rows[i]);
      }
    }

    TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<AbstractTableModel>();
    sorter.setModel((AbstractTableModel) table.getModel());
    table.setRowSorter(sorter);

    for (int i = 0; i < convertedRows.length; i++) {
      table.getSelectionModel().addSelectionInterval(convertedRows[i], convertedRows[i]);
    }

    table.getRowSorter().setSortKeys(currentSortKeys);
    // table.updateUI();
  }

  public void storeRecipes() {
    recipeListPanel.reset();
    dataBaseControl.storeRecipes(recipeDetailsModel.getRecipes());
    dataBaseControl.storeDayPlan(recipePlanerPanel, recipeDetailsModel);
    restoreTableSelection(ingredientDetailsTable);
  }

  public void reloadFinderModel() {
    ingredientFinderTable.clearSelection();
    ingredientFinderModel.clear();
    ingredientFinderTable.setAutoCreateRowSorter(true);
    dataBaseControl.loadIngredientData(ingredientFinderModel);
    recipeFinderModel.clear();
    for (Recipe recipe : recipeDetailsModel.getRecipes()) {
      recipeFinderModel.addRow(recipe, false);
    }
  }

  // public void completeIngredientUpdate(Ingredient ingred, Object lastValue) {
  // for (Ingredient finderIngred : ingredientFinderModel.getIngredients()) {
  // if (isSameIngredient(finderIngred, ingred, lastValue)) {
  // updateIngredient(ingred, finderIngred);
  // }
  // }
  // updateRecipes(lastValue, ingred);
  // restoreTableSelection(ingredientDetailsTable);
  // }

  // public void updateRecipes(Object lastValue, Ingredient ingred) {
  // for (Recipe recipe : recipeListPanel.getAllRecipes()) {
  // for (Ingredient detailsIngred : recipe.getIngredients()) {
  // if (isSameIngredient(detailsIngred, ingred, lastValue)) {
  // updateIngredient(ingred, detailsIngred);
  // }
  // }
  // }
  // }

  // public boolean isSameIngredient(Ingredient firstIngred, Ingredient
  // secondIngredient, Object lastValue) {
  // boolean result = false;
  // if ((firstIngred.getName().equals(secondIngredient.getName()) ||
  // firstIngred.getName().equals(lastValue)) &&
  // (firstIngred.getStorePlace().equals(secondIngredient.getStorePlace()) ||
  // firstIngred.getStorePlace().equals(lastValue))) {
  // result = true;
  // }
  // return result;
  // }

  public void updateAllIngredients(Ingredient oldIngred, Ingredient newIngred) {
    for (Recipe recipe : recipeListPanel.getAllRecipes()) {
      for (Ingredient detailsIngred : recipe.getIngredients()) {
        if (detailsIngred.getName().equals(oldIngred.getName()) && detailsIngred.getStorePlace().equals(oldIngred.getStorePlace())) {
          updateIngredient(newIngred, detailsIngred);
        }
      }
    }
    for (Ingredient finderIngred : ingredientFinderModel.getIngredients()) {
      if (finderIngred.getName().equals(oldIngred.getName()) && finderIngred.getStorePlace().equals(oldIngred.getStorePlace())) {
        updateIngredient(newIngred, finderIngred);
      }
    }
  }

  public void updateIngredient(Ingredient sourceIngred, Ingredient updatedIngred) {
    updatedIngred.setName(sourceIngred.getName());
    updatedIngred.setStorePlace(sourceIngred.getStorePlace());
    updatedIngred.setAvailable(sourceIngred.isAvailable());
  }

  @Override
  public void windowActivated(WindowEvent arg0) {
  }

  @Override
  public void windowClosed(WindowEvent arg0) {
  }

  @Override
  public void windowClosing(WindowEvent arg0) {
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    storeRecipes();
    configLoader.storeProperty("Locale", mainMenuBar.getSelectedLanguage());
    configLoader.storeProperty("DataBaseFile", mainMenuBar.getDbSource());
    configLoader.storeProperty("Mail.To", mainMenuBar.getMailConfig().getTo());
    configLoader.storeProperty("Mail.From", mainMenuBar.getMailConfig().getFrom());
    configLoader.storeProperty("Mail.Host", mainMenuBar.getMailConfig().getHost());
    configLoader.storeProperty("Mail.Port", mainMenuBar.getMailConfig().getPort());
    configLoader.storeProperty("Mail.Username", mainMenuBar.getMailConfig().getUsername());
    configLoader.storeProperty("Mail.Password", configLoader.encrypt(mainMenuBar.getMailConfig().getPassword()));
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    System.exit(DISPOSE_ON_CLOSE);
  }

  @Override
  public void windowDeactivated(WindowEvent arg0) {
  }

  @Override
  public void windowDeiconified(WindowEvent arg0) {
  }

  @Override
  public void windowIconified(WindowEvent arg0) {
  }

  @Override
  public void windowOpened(WindowEvent arg0) {
    recipeDetailsTable.updateUI();
    ingredientDetailsTable.updateUI();
  }

  public RecipeListPanel getRecipeListPanel() {
    return recipeListPanel;
  }

  public PlanerPanel getRecipePlanerPanel() {
    return recipePlanerPanel;
  }

  public XMLConfigurationLoader getConfigLoader() {
    return configLoader;
  }

  public AbstractTable getIngredientDetailsTable() {
    return ingredientDetailsTable;
  }

  public RecipeFormulaPanel getRecipeFormulaPanel() {
    return recipeFormulaPanel;
  }

  public RecipeTable getRecipeDetailsTable() {
    return recipeDetailsTable;
  }

  public AbstractTable getIngredientFinderTable() {
    return ingredientFinderTable;
  }

  public AbstractTable getRecipeFinderTable() {
    return recipeFinderTable;
  }

  public HeaderToolBar getToolBar() {
    return toolBar;
  }

  public JTabbedPane getMainTabbedPane() {
    return mainTabbedPane;
  }

  public RecipeFinderTableCellRenderer getRecipeFinderCellRenderer() {
    return recipeRenderer;
  }
}
