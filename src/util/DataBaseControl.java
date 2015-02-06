package util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import recipedetails.recipe.RecipeDetailsModel;
import recipefinder.ingredient.IngredientFinderModel;
import recipefinder.recipe.RecipeFinderModel;

import mainwindow.data.DayPlan;
import mainwindow.data.Ingredient;
import mainwindow.data.Recipe;
import mainwindow.planer.PlanerDayPanel;
import mainwindow.planer.PlanerPanel;

public class DataBaseControl {
  // private static final String dataBaseName =
  // "jdbc:h2:.\\db\\RecipeDB;MV_STORE=FALSE;MVCC=FALSE";
  private static final String userName      = "";
  private static final String userPassword  = "";

  private String              _dataBaseName = "";

  private Connection          _connection;
  private Statement           _statement;

  private ResourceBundle      languageBundle;

  public DataBaseControl(String dbSource, ResourceBundle languageBundle) {
    _connection = null;
    _statement = null;
    this.languageBundle = languageBundle;

    _dataBaseName = dbSource;
    if (_dataBaseName.endsWith(".h2.db")) {
      _dataBaseName = _dataBaseName.replace(".h2.db", "");
    }
    _dataBaseName = "jdbc:h2:" + _dataBaseName + "\\RecipeDB" + ";MV_STORE=FALSE;MVCC=FALSE";

    InitializeDataBase();
  }

  public boolean connect() {
    boolean success = false;
    try {
      _connection = DriverManager.getConnection(_dataBaseName, userName, userPassword);
      _statement = _connection.createStatement();
      success = true;
    }
    catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("NoDBConnectTitle"), JOptionPane.ERROR_MESSAGE);
      disconnect();
    }
    return success;
  }

  public void disconnect() {
    if (existsConnection()) try {
      _connection.close();
    }
    catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("NoDBDiconnectTitle"), JOptionPane.ERROR_MESSAGE);
    }
  }

  public boolean executeUpdate(String query) {
    boolean result = false;
    if (existsConnection()) {
      try {
        _statement.executeUpdate(query);
        result = true;
      }
      catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.toString() + " \n " + query, languageBundle.getString("NoSQLUpdateTitle"), JOptionPane.ERROR_MESSAGE);
      }
    }
    else {
      JOptionPane.showMessageDialog(null, languageBundle.getString("NoDBConnectionMessage"), languageBundle.getString("NoSQLUpdateTitle"), JOptionPane.ERROR_MESSAGE);
    }
    return result;
  }

  public ResultSet executeQuery(String query) {
    ResultSet result = null;
    if (existsConnection()) {
      try {
        result = _statement.executeQuery(query);
      }
      catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.toString() + " \n " + query, languageBundle.getString("NoSQLQueryTitle"), JOptionPane.ERROR_MESSAGE);
      }
    }
    else {
      JOptionPane.showMessageDialog(null, languageBundle.getString("NoDBConnectionMessage"), languageBundle.getString("NoSQLQueryTitle"), JOptionPane.ERROR_MESSAGE);
    }
    return result;
  }

  public ArrayList<ArrayList<Object>> extractResultData(ResultSet data, int columns) {
    ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
    if (data != null) {
      try {
        while (data.next()) {
          ArrayList<Object> row = new ArrayList<Object>();
          for (int i = 1; i <= columns; i++) {
            row.add(data.getObject(i));
          }
          result.add(row);
        }
      }
      catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("NoSQLErrorTitle"), JOptionPane.ERROR_MESSAGE);
      }
    }
    return result;
  }

  public void InitializeDataBase() {
    if (connect()) {
      // executeUpdate("DROP TABLE IF EXISTS Recipes");
      // executeUpdate("DROP TABLE IF EXISTS Ingredients");
      // executeUpdate("DROP TABLE IF EXISTS Recipes_Ingredients");
      // executeUpdate("DROP TABLE IF EXISTS Date_Recipes");

      executeUpdate("CREATE TABLE IF NOT EXISTS Recipes (Recipe_ID INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, Name VARCHAR(255), Used BIT, Formula VARCHAR(MAX), Duration VARCHAR(255), Type VARCHAR(255), KindOfMeal INT NOT NULL)");
      executeUpdate("CREATE TABLE IF NOT EXISTS Ingredients (Ingredient_ID INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, Name VARCHAR(255), Amount VARCHAR(255), Recipe INT REFERENCES Recipes(Recipe_ID), Available BIT, OrderInRecipe INT NOT NULL, StorePlace VARCHAR(255))");
      executeUpdate("CREATE TABLE IF NOT EXISTS Recipes_Ingredients (Recipe INT REFERENCES Recipes(Recipe_ID), Ingredient INT REFERENCES Ingredients(Ingredient_ID))");
      executeUpdate("CREATE TABLE IF NOT EXISTS Date_Recipes (Day VARCHAR(10) NOT NULL, Recipe INT REFERENCES Recipes(Recipe_ID))");
      // executeUpdate("CREATE TABLE IF NOT EXISTS IngredientNames (Name VARCHAR(255) PRIMARY KEY, StorePlace VARCHAR(255), Available BIT)");
      disconnect();
    }
    else {
      JOptionPane.showMessageDialog(null, languageBundle.getString("NoDBConnectionMessage"), languageBundle.getString("NoDBConnectTitle"), JOptionPane.ERROR_MESSAGE);
    }
  }

  public void loadRecipeData(RecipeDetailsModel detailsModel, RecipeFinderModel finderModel) {
    if (connect()) {
      ResultSet selectRecipes = executeQuery("SELECT Recipe_ID FROM Recipes");
      ArrayList<ArrayList<Object>> resultSelectRecipes = extractResultData(selectRecipes, 1);
      for (ArrayList<Object> recipeRow : resultSelectRecipes) {
        int id = (Integer) recipeRow.get(0);
        Recipe recipe = loadRecipe(id);
        detailsModel.addRow(recipe, false);
        finderModel.addRow(recipe, false);
      }
      disconnect();
    }
    else {
      JOptionPane.showMessageDialog(null, languageBundle.getString("NoDBConnectionMessage"), languageBundle.getString("NoDBConnectTitle"), JOptionPane.ERROR_MESSAGE);
    }
  }

  public void loadIngredientData(IngredientFinderModel ingredFinderModel) {
    if (connect()) {
      ResultSet selectIngredientName = executeQuery("SELECT Name, StorePlace, Ingredient_ID FROM Ingredients;");
      ArrayList<ArrayList<Object>> resultSelectDayPlan = extractResultData(selectIngredientName, 3);
      for (ArrayList<Object> row : resultSelectDayPlan) {
        String name = (String) row.get(0);
        String storePlace = (String) row.get(1);
        if(storePlace == null) storePlace = "";
        int id = (int) row.get(2);
        boolean contains = false;
        for (Ingredient ingred : ingredFinderModel.getIngredients()) {
          if (ingred.getName().equals(name) && ingred.getStorePlace().equals(storePlace)) {
            contains = true;
          }
        }
        if (!contains) {
          ingredFinderModel.addRow(loadIngredient(null, id), false);
        }
      }
    }
    else {
      JOptionPane.showMessageDialog(null, languageBundle.getString("NoDBConnectionMessage"), languageBundle.getString("NoDBConnectTitle"), JOptionPane.ERROR_MESSAGE);
    }
  }

  public Recipe loadRecipe(int id) {   
    Recipe recipe = null;
    ResultSet selectRecipe = executeQuery("SELECT Name, Used, Formula, Duration, Type, KindOfMeal FROM Recipes WHERE Recipe_ID = " + id + ";");
    ArrayList<ArrayList<Object>> resultSelectRecipes = extractResultData(selectRecipe, 6);
    for (ArrayList<Object> recipeRow : resultSelectRecipes) {
      String name = (String) recipeRow.get(0);
      boolean used = (Boolean) recipeRow.get(1);
      String formula = (String) recipeRow.get(2);
      ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
      String duration = (String) recipeRow.get(3);
      String type = (String) recipeRow.get(4);
      Integer kindOfMeal = (Integer) recipeRow.get(5);
      long frequency = calculateFrequency(id);
      recipe = new Recipe(name, ingredients, used, formula, id, duration, type, kindOfMeal, frequency);

      ResultSet selectIngredientIDs = executeQuery("SELECT * FROM Recipes_Ingredients WHERE Recipe = " + id + ";");
      ArrayList<ArrayList<Object>> resultSelectIngredientIDs = extractResultData(selectIngredientIDs, 2);
      for (ArrayList<Object> idRow : resultSelectIngredientIDs) {
        Ingredient ingred = loadIngredient(recipe, (int) idRow.get(1));
        ingredients.add(ingred);
      }
    }
    return recipe;
  }

  private Ingredient loadIngredient(Recipe recipe, int id) {
    Ingredient ingred = null;
    ResultSet selectIngredients = executeQuery("SELECT * FROM Ingredients WHERE Ingredient_ID = " + id + ";");
    ArrayList<ArrayList<Object>> resultSelectIngredients = extractResultData(selectIngredients, 7);
    for (ArrayList<Object> ingredientRow : resultSelectIngredients) {
      ingred = new Ingredient((String) ingredientRow.get(1), (String) ingredientRow.get(2), recipe, (Boolean) ingredientRow.get(4), (Integer) ingredientRow.get(0), (Integer) ingredientRow.get(5), (String) ingredientRow.get(6));
    }
    return ingred;
  }

  private long calculateFrequency(int id) {
    long result = 0;
    if (connect()) {
      ResultSet selectIngredientIDs = executeQuery("SELECT COUNT(Recipe) FROM Date_Recipes WHERE Recipe = " + id + ";");
      ArrayList<ArrayList<Object>> resultSelectIngredientIDs = extractResultData(selectIngredientIDs, 1);
      for (ArrayList<Object> idRow : resultSelectIngredientIDs) {
        result = (Long) idRow.get(0);
      }
    }
    else {
      JOptionPane.showMessageDialog(null, languageBundle.getString("NoDBConnectionMessage"), languageBundle.getString("NoDBConnectTitle"), JOptionPane.ERROR_MESSAGE);
    }
    return result;
  }

  public DayPlan loadDayPlanData(RecipeDetailsModel model, int dayFromToday) {
    DayPlan result = null;
    if (connect()) {
      Date date = new Date();
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.DATE, dayFromToday);
      date = c.getTime();
      String dateString = ToolConstants.getDateString(date, true);
      DayPlan dayPlan = new DayPlan(date);
      ResultSet selectDayPlan = executeQuery("SELECT * FROM Date_Recipes WHERE Day = '" + dateString + "';");
      ArrayList<ArrayList<Object>> resultSelectDayPlan = extractResultData(selectDayPlan, 2);
      for (ArrayList<Object> recipeRow : resultSelectDayPlan) {
        int id = (Integer) recipeRow.get(1);
        dayPlan.addRecipe(model.getRecipeByID(id));
      }
      result = dayPlan;
      disconnect();
    }
    else {
      JOptionPane.showMessageDialog(null, languageBundle.getString("NoDBConnectionMessage"), languageBundle.getString("NoDBConnectTitle"), JOptionPane.ERROR_MESSAGE);
    }
    return result;
  }

  public void storeRecipes(ArrayList<Recipe> recipes) {
    connect();
    removeDeleteRecipe(recipes);

    boolean onlyIngredAvailabilityChanged = true;
    HashMap<Recipe, String> alteredRecipes = new HashMap<Recipe, String>();
    String alteredRecipeString = "";
    for (Recipe recipe : recipes) {
      if (isRecipeAltered(recipe)) {
        String changedValues = generateChangeMessage(recipe);
        if (!changedValues.equals("")) {
          changedValues = changedValues.substring(0, changedValues.length() - 2);
          onlyIngredAvailabilityChanged = false;
        }
        changedValues = languageBundle.getString("UpdateRecipeMessage") + ": \"" + recipe.getName() + "\"" + "\n" + changedValues;
        alteredRecipes.put(recipe, changedValues);
        if(!onlyIngredAvailabilityChanged){
          alteredRecipeString += recipe.getName() + "\n";
        }
      }
    }

    if (alteredRecipes.size() > 0) {
      JPanel dialogPanel = new JPanel();
      dialogPanel.setPreferredSize(new Dimension(100,250));
      dialogPanel.setLayout(new BorderLayout());
      JTextArea recipeListArea = new JTextArea(alteredRecipeString);
      recipeListArea.setEditable(false);
      JScrollPane recipeListScrollPane = new JScrollPane(recipeListArea);
      dialogPanel.add(recipeListScrollPane, BorderLayout.CENTER);
      int option = 0;
      if(!onlyIngredAvailabilityChanged){
        Object[] message = { languageBundle.getString("AlteredRecipes"), dialogPanel };
        Object[] options = { languageBundle.getString("SaveALL"), languageBundle.getString("ChooseForEach"), languageBundle.getString("Discard") };
        option = JOptionPane.showOptionDialog(null, message, languageBundle.getString("UpdateALLRecipesTitle"), JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
      }
      if (option == 0) {
        for (Entry<Recipe, String> entry : alteredRecipes.entrySet()) {
          storeRecipe(entry.getKey());
        }
      }
      else if (option == 1) {
        for (Entry<Recipe, String> entry : alteredRecipes.entrySet()) {
          int singleRecipeDialogResult = JOptionPane.YES_OPTION;
          if(entry.getKey().getValueChangedMap().get(9) == false){
            singleRecipeDialogResult = JOptionPane.showConfirmDialog(null, entry.getValue(), languageBundle.getString("UpdateRecipeTitle"), JOptionPane.YES_NO_OPTION);
          }
          if (singleRecipeDialogResult == JOptionPane.YES_OPTION) {
            storeRecipe(entry.getKey());
          }
        }
      }
    }

    disconnect();
  }

  private boolean isRecipeAltered(Recipe recipe) {
    boolean result = false;
    if (recipe.getID() > -1) {
      Recipe storedRecipe = loadRecipe(recipe.getID());
      if (recipe.compareTo(storedRecipe) > 0) {
        result = true;
      }
    }
    else {
      result = true;
    }
    return result;
  }

  private void storeRecipe(Recipe recipe) {
    int use = 0;// Use isnt relevant for db anymore. it will now always be saved
                // as false
    if (recipe.getID() > -1) {
      executeUpdate("UPDATE Recipes SET Name = '" + recipe.getName() + "', Used = " + use + ", Formula = '" + recipe.getFormula() + "', Duration = '" + recipe.getDuration() + "', Type = '" + recipe.getType() + "', KindOfMeal = " + ToolConstants.KINDOFMEAL.mealToInt(recipe.getKindOfMeal()) + " WHERE Recipe_ID = '" + recipe.getID() + "';");
      for (Ingredient ingred : recipe.getIngredients()) {
        storeIngredient(ingred);
      }
      removeDeletedIngredients(recipe);
    }
    else {
      executeUpdate("INSERT INTO Recipes(Name, Used, Formula, Duration, Type, KindOfMeal) VALUES('" + recipe.getName() + "', " + use + ", '" + recipe.getFormula() + "', '" + recipe.getDuration() + "', '" + recipe.getType() + "', " + ToolConstants.KINDOFMEAL.mealToInt(recipe.getKindOfMeal()) + ")");
      recipe.setID(readRecipeID(recipe));
      // System.out.println(recipe.getName() +
      // " is a new Recipe an has the ID: " + recipe.getID());
      for (Ingredient ingred : recipe.getIngredients()) {
        storeIngredient(ingred);
      }
    }
  }

  private void storeIngredient(Ingredient ingred) {
    ResultSet selectIngredientName = executeQuery("SELECT DISTINCT Name, StorePlace, Available FROM Ingredients WHERE Name = '" + ingred.getName() + "';");
    ArrayList<ArrayList<Object>> resultSelectDayPlan = extractResultData(selectIngredientName, 3);
    if (resultSelectDayPlan.size() == 0) {
      updateIngredientForStorage(ingred, resultSelectDayPlan);
    }
    else {
      boolean contains = false;
      for (ArrayList<Object> row : resultSelectDayPlan) {
        String storePlace = (String) row.get(1);
        if(storePlace == null) storePlace = "";
        if (ingred.getStorePlace().toLowerCase().equals(storePlace.toLowerCase())) {
          contains = true;
          break;
        }
      }
      if (!contains) {
        updateIngredientForStorage(ingred, resultSelectDayPlan);
      }
    }

    int available = 0;
    if (ingred.isAvailable()) available = 1;
    if (ingred.getID() > -1) {
      executeUpdate("UPDATE Ingredients SET Name = '" + ingred.getName() + "', Available = " + available + ", Amount = '" + ingred.getAmount() + "', OrderInRecipe = " + ingred.getOrder() + ", StorePlace = '" + ingred.getStorePlace() + "' WHERE Ingredient_ID = '" + ingred.getID() + "';");
    }
    else {
      executeUpdate("INSERT INTO Ingredients(Name, Available, Amount, Recipe, OrderInRecipe, StorePlace) VALUES('" + ingred.getName() + "', " + available + ", '" + ingred.getAmount() + "', " + ingred.getRecipe().getID() + ", " + ingred.getOrder() + ", '" + ingred.getStorePlace() + "')");
      ingred.setID(readIngredientID(ingred));
      
      // System.out.println(ingred.getName() +
      // " is a new Ingredient an has the ID: " + ingred.getID());
      executeUpdate("INSERT INTO Recipes_Ingredients(Recipe, Ingredient) VALUES(" + ingred.getRecipe().getID() + ", " + ingred.getID() + ")");
    }
  }

  private void updateIngredientForStorage(Ingredient ingred, ArrayList<ArrayList<Object>> resultSelectDayPlan) {
    JTextField ingredientField = new JTextField(ingred.getName());
    JTextField storePlaceField = new JTextField(ingred.getStorePlace());
    Object[] message = { languageBundle.getString("NewIngredientMessage"), languageBundle.getString("Name"), ingredientField, languageBundle.getString("StorePlace"), storePlaceField };
    Object[] options = { languageBundle.getString("OK") };
    JOptionPane.showOptionDialog(null, message, languageBundle.getString("NewIngredientTitle"), JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    ingred.setName(ingredientField.getText());
    ingred.setStorePlace(storePlaceField.getText());
    for (ArrayList<Object> row : resultSelectDayPlan) {
      String ingredName = (String) row.get(0);
      String storePlace = (String) row.get(1);
      boolean available = (boolean) row.get(2);
      if(ingred.getName().equals(ingredName) && ingred.getStorePlace().equals(storePlace)){
        ingred.setAvailable(available);
      }
    }
  }

  public void storeDayPlan(PlanerPanel recipePlanerPanel, RecipeDetailsModel model) {
    connect();
    for (PlanerDayPanel panel : recipePlanerPanel.getDayPanelList()) {
      ArrayList<Recipe> storedRecipes = new ArrayList<Recipe>();
      String dateString = ToolConstants.getDateString(panel.getDayPlan().getDay(), true);
      ResultSet selectDayPlan = executeQuery("SELECT * FROM Date_Recipes WHERE Day = '" + dateString + "';");
      ArrayList<ArrayList<Object>> resultSelectDayPlan = extractResultData(selectDayPlan, 2);
      for (ArrayList<Object> recipeRow : resultSelectDayPlan) {
        int id = (Integer) recipeRow.get(1);
        storedRecipes.add(model.getRecipeByID(id));
      }

      for (Recipe recipe : storedRecipes) {
        if (!panel.getDayPlan().getRecipes().contains(recipe)) {
          executeUpdate("DELETE FROM Date_Recipes WHERE Recipe = " + recipe.getID() + " AND Day = '" + dateString + "';");
          recipe.setFrequency(calculateFrequency(recipe.getID()));
        }
      }

      for (Recipe recipe : panel.getDayPlan().getRecipes()) {
        if (!storedRecipes.contains(recipe)) {
          executeUpdate("INSERT INTO Date_Recipes(Day, Recipe) VALUES('" + dateString + "', " + recipe.getID() + ")");
          recipe.setFrequency(calculateFrequency(recipe.getID()));
        }
      }
    }
    disconnect();
  }

  public void removeDeleteRecipe(ArrayList<Recipe> recipes) {
    ResultSet selectRecipeIDs = executeQuery("SELECT Recipe_id FROM Recipes;");
    ArrayList<ArrayList<Object>> resultSelectRecipeIDs = extractResultData(selectRecipeIDs, 1);

    int currentID = -1;
    int lastDeletedID = -2;

    for (ArrayList<Object> idRow : resultSelectRecipeIDs) {
      int recipeID = (Integer) idRow.get(0);
      boolean contains = false;
      for (Recipe recipe : recipes) {
        if (recipe.getID() == recipeID) {
          contains = true;
        }
      }
      if (!contains) {
        currentID = recipeID;
        if (currentID != lastDeletedID) {
          executeUpdate("DELETE FROM Recipes_Ingredients WHERE Recipe = (" + recipeID + ");");
          executeUpdate("DELETE FROM Ingredients WHERE Recipe = (" + recipeID + ");");
          executeUpdate("DELETE FROM Date_Recipes WHERE Recipe = (" + recipeID + ");");
          executeUpdate("DELETE FROM Recipes WHERE Recipe_ID = (" + recipeID + ");");
          lastDeletedID = recipeID;
        }
      }
    }
  }

  private void removeDeletedIngredients(Recipe recipe) {
    ResultSet selectIngredientIDs = executeQuery("SELECT * FROM Recipes_Ingredients WHERE Recipe = " + recipe.getID() + ";");
    ArrayList<ArrayList<Object>> resultSelectIngredientIDs = extractResultData(selectIngredientIDs, 2);
    for (ArrayList<Object> idRow : resultSelectIngredientIDs) {
      int recipeID = (Integer) idRow.get(0);
      int ingredientID = (Integer) idRow.get(1);
      boolean contains = false;
      for (Ingredient ingred : recipe.getIngredients()) {
        if (ingred.getID() == ingredientID) contains = true;
      }
      if (!contains) {
        executeUpdate("DELETE FROM Recipes_Ingredients WHERE Recipe = " + recipeID + " AND Ingredient = " + ingredientID + ";");
        executeUpdate("DELETE FROM Ingredients WHERE Ingredient_ID = " + ingredientID + ";");
      }
    }
  }

  private int readRecipeID(Recipe recipe) {
    ResultSet select = executeQuery("SELECT Recipe_ID FROM Recipes WHERE Name = '" + recipe.getName() + "';");
    ArrayList<ArrayList<Object>> resultSelect = extractResultData(select, 1);
    return (Integer) resultSelect.get(0).get(0);
  }

  private int readIngredientID(Ingredient ingred) {
    ResultSet select = executeQuery("SELECT Ingredient_ID FROM Ingredients WHERE Name = '" + ingred.getName() + "' AND Amount = '" + ingred.getAmount() + "' AND Recipe = " + ingred.getRecipe().getID() + ";");
    ArrayList<ArrayList<Object>> resultSelect = extractResultData(select, 1);
    return (Integer) resultSelect.get(0).get(0);
  }

  private boolean existsConnection() {
    boolean result = false;
    try {
      if (_connection != null) {
        if (!_connection.isClosed()) {
          result = true;
        }
      }
    }
    catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("NoDBConnectTitle"), JOptionPane.ERROR_MESSAGE);
    }
    return result;
  }

  public String generateChangeMessage(Recipe recipe) {
    String result = "";
    if (recipe.getID() > -1) {
      if (recipe.getValueChangedMap().get(0)) {
        result += languageBundle.getString("Name") + " ,";
      }
      if (recipe.getValueChangedMap().get(1)) {
        result += languageBundle.getString("Use") + " ,";
      }
      if (recipe.getValueChangedMap().get(2)) {
        result += languageBundle.getString("Formula") + " ,";
      }
      if (recipe.getValueChangedMap().get(3)) {
        result += languageBundle.getString("Duration") + " ,";
      }
      if (recipe.getValueChangedMap().get(4)) {
        result += languageBundle.getString("Type") + " ,";
      }
      if (recipe.getValueChangedMap().get(5)) {
        result += languageBundle.getString("KindOfMeal") + " ,";
      }
      if (recipe.getValueChangedMap().get(6)) {
        result += languageBundle.getString("ID") + " ,";
      }
      if (recipe.getValueChangedMap().get(7)) {
        result += languageBundle.getString("IngredientValues") + " ,";
      }
      if (recipe.getValueChangedMap().get(8)) {
        result += languageBundle.getString("IngredientNumber") + " ,";
      }
    }
    else {
      result = languageBundle.getString("NewRecipe");
    }
    return result;
  }
}
