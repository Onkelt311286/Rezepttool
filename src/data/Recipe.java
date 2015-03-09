package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.ToolConstants;

public class Recipe implements Serializable, Comparable<Recipe> {

  private static final long        serialVersionUID = -4849235700318146994L;
  private static final int         bitSetLength     = 10;
  private String                   name;
  private boolean                  used;
  private String                   formula;
  private int                      id;
  private String                   duration;
  private BitSet                   valueChangedMap;
  private long                     frequency;

  private ArrayList<Ingredient>    ingredients;
  private ArrayList<String>        types;
  private Map<Ingredient, String>  ingredientAmountMap;
  private Map<Ingredient, Integer> ingredientOrderMap;

  // public Recipe(String name) {
  // _name = name;
  // if (_name.contains("Chefkoch.de Rezept: ")) {
  // _name = _name.replace("Chefkoch.de Rezept: ", "");
  // }
  // if (_name.contains("'")) {
  // _name = _name.replace("'", "");
  // }
  // _ingredients = new ArrayList<Ingredient>();
  // _used = false;
  // _formula = "";
  // _id = -1;
  // _duration = "";
  // types = new ArrayList<String>();
  // _valueChangedMap = new BitSet(bitSetLength);
  // }

  // public Recipe(String name, ArrayList<Ingredient> ingredients, boolean used,
  // String formula, int id, String duration, String type, Integer kindOfMeal,
  // long frequency) {
  // _name = name;
  // if (_name.contains("Chefkoch.de Rezept: ")) {
  // _name = _name.replace("Chefkoch.de Rezept: ", "");
  // }
  // if (_name.contains("'")) {
  // _name = _name.replace("'", "");
  // }
  // if (ingredients != null) {
  // _ingredients = ingredients;
  // }
  // else {
  // _ingredients = new ArrayList<Ingredient>();
  // }
  // _used = used;
  // _formula = formula;
  // _id = id;
  // if (duration != null) {
  // _duration = duration;
  // }
  // else {
  // _duration = "";
  // }
  // if (type != null) {
  // _type = type;
  // }
  // else {
  // _type = "";
  // }
  // this.frequency = frequency;
  // _kindOfMeal = ToolConstants.KINDOFMEAL.intToMeal(kindOfMeal);
  // _valueChangedMap = new BitSet(bitSetLength);
  // }

  public Recipe(int id, String name, String formula, String duration, long frequency) {
    this.id = id;
    this.name = name;
    if (this.name.contains("Chefkoch.de Rezept: ")) {
      this.name = this.name.replace("Chefkoch.de Rezept: ", "");
    }
    if (this.name.contains("'")) {
      this.name = this.name.replace("'", "");
    }
    this.formula = formula;
    this.duration = duration;
    this.frequency = frequency;

    ingredients = new ArrayList<Ingredient>();
    types = new ArrayList<String>();
    ingredientAmountMap = new HashMap<Ingredient, String>();
    ingredientOrderMap = new HashMap<Ingredient, Integer>();

    valueChangedMap = new BitSet(bitSetLength);
  }

  @Override
  public int compareTo(Recipe compareRecipe) {
    return this.name.compareTo(compareRecipe.getName());
  }

  // @Override
  // public int compareTo(Recipe compareRecipe) {
  // int result = 0;
  //
  // result += checkValue(!name.equals(compareRecipe.getName()), 0);
  // // result += checkValue(_used != compareRecipe.isUsed(), 1);
  // result += checkValue(!formula.equals(compareRecipe.getFormula()), 2);
  // result += checkValue(!duration.equals(compareRecipe.getDuration()), 3);
  // // result += checkValue(!type.equals(compareRecipe.getType()), 4);
  // result += checkValue(id != compareRecipe.getID(), 6);
  //
  // boolean ingredChanged = false;
  // for (Ingredient ingredient : ingredients) {
  // for (Ingredient compareIngredient : compareRecipe.getIngredients()) {
  // if (ingredient.getID() == compareIngredient.getID()) {
  // int compareValue = ingredient.compareTo(compareIngredient);
  // if (compareValue > 0) {
  // if (compareValue != 100) {
  // ingredChanged = true;
  // }
  // else {
  // checkValue(true, 9);
  // }
  // result++;
  // }
  // }
  // }
  // }
  // checkValue(ingredChanged, 7);
  //
  // result += checkValue(ingredients.size() !=
  // compareRecipe.getIngredients().size(), 8);
  //
  // return result;
  // }

  // private int checkValue(boolean check, int index) {
  // int result = 0;
  // if (check) {
  // valueChangedMap.set(index, true);
  // result = 1;
  // }
  // else {
  // valueChangedMap.set(index, false);
  // }
  // return result;
  // }

  public void addIngredient(Ingredient ingred) {
    ingredients.add(ingred);
  }

  public void addType(String type) {
    types.add(type);
  }

  public void addIngredientAmount(Ingredient ingred, String amount) {
    ingredientAmountMap.put(ingred, amount);
  }

  public void addIngredientOrder(Ingredient ingred, Integer order) {
    ingredientOrderMap.put(ingred, order);
  }

  public List<Map.Entry<Ingredient, String>> getIngredientAmount() {
    Set<Map.Entry<Ingredient, String>> ingredientAmountSet = ingredientAmountMap.entrySet();
    return new ArrayList<Map.Entry<Ingredient, String>>(ingredientAmountSet);
  }
  
  public List<Map.Entry<Ingredient, Integer>> getIngredientOrder() {
    Set<Map.Entry<Ingredient, Integer>> ingredientOrderSet = ingredientOrderMap.entrySet();
    return new ArrayList<Map.Entry<Ingredient, Integer>>(ingredientOrderSet);
  }

  public ArrayList<Ingredient> getIngredients() {
    return ingredients;
  }

  public String getFormula() {
    return formula;
  }

  public boolean isUsed() {
    return used;
  }

  public void setUsed(Boolean value) {
    used = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String value) {
    name = value;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setFormula(String formula) {
    this.formula = formula;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public ArrayList<String> getType() {
    return types;
  }

  public void setType(ArrayList<String> types) {
    this.types = types;
  }

  public BitSet getValueChangedMap() {
    return valueChangedMap;
  }

  public long getFrequency() {
    return frequency;
  }

  public void setFrequency(long frequency) {
    this.frequency = frequency;
  }

  public Map<Ingredient, String> getIngredientAmountMap() {
    return ingredientAmountMap;
  }

  public void setIngredientAmountMap(Map<Ingredient, String> ingredientAmountMap) {
    this.ingredientAmountMap = ingredientAmountMap;
  }

  public Map<Ingredient, Integer> getIngredientOrderMap() {
    return ingredientOrderMap;
  }

  public void setIngredientOrderMap(Map<Ingredient, Integer> ingredientOrderMap) {
    this.ingredientOrderMap = ingredientOrderMap;
  }
}
