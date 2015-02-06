package mainwindow.data;

import java.io.Serializable;
import java.util.BitSet;

public class Ingredient implements Serializable, Comparable<Ingredient> {

  private static final long serialVersionUID = 3158605227817051789L;
  private String            _name;
  private String            _amount;
  private Recipe            _recipe;
  private boolean           _available;
  private int               _id;
  private int               _order;
  private String            storePlace;
  private BitSet            _valueChangedMap;

  /**
   * Create a new ingredient with default values
   * 
   * @param name
   * @param amount
   * @param recipe
   * @param order
   */
  public Ingredient(String name, String amount, Recipe recipe, int order) {
    _name = name;
    _amount = amount;
    _recipe = recipe;
    _available = false;
    _id = -1;
    _order = order;
    this.storePlace = "";
    _valueChangedMap = new BitSet(7);
  }

  /**
   * Load complete ingredient data from database
   * 
   * @param name
   * @param amount
   * @param recipe
   * @param available
   * @param id
   * @param order
   * @param storePlace
   */
  public Ingredient(String name, String amount, Recipe recipe, boolean available, int id, int order, String storePlace) {
    _name = name;
    _amount = amount;
    _recipe = recipe;
    _available = available;
    _id = id;
    _order = order;
    _valueChangedMap = new BitSet(7);
    if (storePlace == null) {
      this.storePlace = "";
    }
    else {
      this.storePlace = storePlace;
    }
  }

  /**
   * Create a temporary ingredient to update availability after purchase
   * complete. Unused values are set to null on porpuse. They shall not be used
   * 
   * @param name
   * @param amount
   * @param recipe
   * @param order
   */
  public Ingredient(String name, String storePlace) {
    _name = name;
    _amount = null;
    _recipe = null;
    _available = true;
    _id = -2;
    _order = -2;
    this.storePlace = storePlace;
    _valueChangedMap = null;
  }

  @Override
  public int compareTo(Ingredient o) {
    Ingredient compareIngredient = (Ingredient) o;
    int result = 0;
    result += checkValue(!_name.equals(compareIngredient.getName()), 0);
    result += checkValue(!_amount.equals(compareIngredient.getAmount()), 1);
    if (checkValue(_available != compareIngredient.isAvailable(), 2) == 1) {
      result += 100;
    }
    result += checkValue(_id != compareIngredient.getID(), 3);
    result += checkValue(_recipe.getID() != compareIngredient.getRecipe().getID(), 4);
    result += checkValue(_order != compareIngredient.getOrder(), 5);
    result += checkValue(!storePlace.equals(compareIngredient.getStorePlace()), 6);

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

  public String getName() {
    return _name;
  }

  public String getAmount() {
    return _amount;
  }

  public Recipe getRecipe() {
    return _recipe;
  }

  public boolean isAvailable() {
    return _available;
  }

  public void setName(String name) {
    _name = name;
  }

  public void setAmount(String menge) {
    _amount = menge;
  }

  public void setRecipe(Recipe recipe) {
    _recipe = recipe;
  }

  public void setAvailable(boolean available) {
    _available = available;
  }

  public int getID() {
    return _id;
  }

  public void setID(int id) {
    _id = id;
  }

  public int getOrder() {
    return _order;
  }

  public void setOrder(Integer order) {
    _order = order;
  }

  public BitSet getValueChangedMap() {
    return _valueChangedMap;
  }

  public String getStorePlace() {
    return storePlace;
  }

  public void setStorePlace(String storePlace) {
    this.storePlace = storePlace;
  }
}
