package mainwindow.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;

import util.ToolConstants;

public class Recipe implements Serializable, Comparable<Recipe> {

  private static final long        serialVersionUID = -4849235700318146994L;
  private static final int         bitSetLength     = 10;
  private String                   _name;
  private ArrayList<Ingredient>    _ingredients;
  private boolean                  _used;
  private String                   _formula;
  private int                      _id;
  private String                   _duration;
  private String                   _type;
  private ToolConstants.KINDOFMEAL _kindOfMeal;
  private BitSet                   _valueChangedMap;
  private long                     frequency;

  public Recipe(String name) {
    _name = name;
    if (_name.contains("Chefkoch.de Rezept: ")) {
      _name = _name.replace("Chefkoch.de Rezept: ", "");
    }
    if (_name.contains("'")) {
      _name = _name.replace("'", "");
    }
    _ingredients = new ArrayList<Ingredient>();
    _used = false;
    _formula = "";
    _id = -1;
    _duration = "";
    _type = "";
    _kindOfMeal = ToolConstants.KINDOFMEAL.Undefined;
    _valueChangedMap = new BitSet(bitSetLength);
  }

  public Recipe(String name, ArrayList<Ingredient> ingredients, boolean used, String formula, int id, String duration, String type, Integer kindOfMeal, long frequency) {
    _name = name;
    if (_name.contains("Chefkoch.de Rezept: ")) {
      _name = _name.replace("Chefkoch.de Rezept: ", "");
    }
    if (_name.contains("'")) {
      _name = _name.replace("'", "");
    }
    if (ingredients != null) {
      _ingredients = ingredients;
    }
    else {
      _ingredients = new ArrayList<Ingredient>();
    }
    _used = used;
    _formula = formula;
    _id = id;
    if (duration != null) {
      _duration = duration;
    }
    else {
      _duration = "";
    }
    if (type != null) {
      _type = type;
    }
    else {
      _type = "";
    }
    this.frequency = frequency;
    _kindOfMeal = ToolConstants.KINDOFMEAL.intToMeal(kindOfMeal);
    _valueChangedMap = new BitSet(bitSetLength);
  }

  @Override
  public int compareTo(Recipe compareRecipe) {
    int result = 0;

    result += checkValue(!_name.equals(compareRecipe.getName()), 0);
    // result += checkValue(_used != compareRecipe.isUsed(), 1);
    result += checkValue(!_formula.equals(compareRecipe.getFormula()), 2);
    result += checkValue(!_duration.equals(compareRecipe.getDuration()), 3);
    result += checkValue(!_type.equals(compareRecipe.getType()), 4);
    result += checkValue(_kindOfMeal != compareRecipe.getKindOfMeal(), 5);
    result += checkValue(_id != compareRecipe.getID(), 6);

    boolean ingredChanged = false;
    for (Ingredient ingredient : _ingredients) {
      for (Ingredient compareIngredient : compareRecipe.getIngredients()) {
        if (ingredient.getID() == compareIngredient.getID()) {
          int compareValue = ingredient.compareTo(compareIngredient);
          if (compareValue > 0) {
            if (compareValue != 100) {
              ingredChanged = true;
            }
            else {
              checkValue(true, 9);
            }
            result++;
          }
        }
      }
    }
    checkValue(ingredChanged, 7);

    result += checkValue(_ingredients.size() != compareRecipe.getIngredients().size(), 8);

    return result;
  }

  private int checkValue(boolean check, int index) {
    int result = 0;
    if (check) {
      _valueChangedMap.set(index, true);
      result = 1;
    }
    else {
      _valueChangedMap.set(index, false);
    }
    return result;
  }

  public void addIngredient(Ingredient ingred) {
    _ingredients.add(ingred);
  }

  public ArrayList<Ingredient> getIngredients() {
    return _ingredients;
  }

  public String getFormula() {
    return _formula;
  }

  public boolean isUsed() {
    return _used;
  }

  public void setUsed(Boolean value) {
    _used = value;
  }

  public String getName() {
    return _name;
  }

  public void setName(String value) {
    _name = value;
  }

  public int getID() {
    return _id;
  }

  public void setID(int id) {
    _id = id;
  }

  public void setFormula(String formula) {
    _formula = formula;
  }

  public String getDuration() {
    return _duration;
  }

  public void setDuration(String duration) {
    _duration = duration;
  }

  public String getType() {
    return _type;
  }

  public void setType(String type) {
    _type = type;
  }

  public ToolConstants.KINDOFMEAL getKindOfMeal() {
    return _kindOfMeal;
  }

  public void setKindOfMeal(ToolConstants.KINDOFMEAL kindOfMeal) {
    _kindOfMeal = kindOfMeal;
  }

  public BitSet getValueChangedMap() {
    return _valueChangedMap;
  }

  public long getFrequency() {
    return frequency;
  }

  public void setFrequency(long frequency) {
    this.frequency = frequency;
  }
}
