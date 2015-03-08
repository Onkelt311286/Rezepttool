package data;

import java.io.Serializable;
import java.util.BitSet;

public class Ingredient implements Serializable, Comparable<Ingredient> {

  private static final long serialVersionUID = 3158605227817051789L;
  private String            name;
  private String            _amount;
  private Recipe            _recipe;
  private boolean           _available;
  private int               id;
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
  // public Ingredient(String name, String amount, Recipe recipe, int order) {
  // name = name;
  // _amount = amount;
  // _recipe = recipe;
  // _available = false;
  // id = -1;
  // _order = order;
  // this.storePlace = "";
  // _valueChangedMap = new BitSet(7);
  // }

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
  // public Ingredient(String name, String amount, Recipe recipe, boolean
  // available, int id, int order, String storePlace) {
  // name = name;
  // _amount = amount;
  // _recipe = recipe;
  // _available = available;
  // id = id;
  // _order = order;
  // _valueChangedMap = new BitSet(7);
  // if (storePlace == null) {
  // this.storePlace = "";
  // }
  // else {
  // this.storePlace = storePlace;
  // }
  // }

  /**
   * Create a temporary ingredient to update availability after purchase
   * complete. Unused values are set to null on porpuse. They shall not be used
   * 
   * @param name
   * @param amount
   * @param recipe
   * @param order
   */
  // public Ingredient(String name, String storePlace) {
  // name = name;
  // _amount = null;
  // _recipe = null;
  // _available = true;
  // id = -2;
  // _order = -2;
  // this.storePlace = storePlace;
  // _valueChangedMap = null;
  // }

  public Ingredient(Integer id, String name, String storePlace) {
    this.id = id;
    this.name = name;
    this.storePlace = storePlace;
  }

  @Override
  public int compareTo(Ingredient compareIngred) {
    return this.storePlace.compareTo(compareIngred.getStorePlace());
  }

  // @Override
  // public int compareTo(Ingredient o) {
  // Ingredient compareIngredient = (Ingredient) o;
  // int result = 0;
  // result += checkValue(!name.equals(compareIngredient.getName()), 0);
  // result += checkValue(!_amount.equals(compareIngredient.getAmount()), 1);
  // if (checkValue(_available != compareIngredient.isAvailable(), 2) == 1) {
  // result += 100;
  // }
  // result += checkValue(id != compareIngredient.getID(), 3);
  // result += checkValue(_recipe.getID() !=
  // compareIngredient.getRecipe().getID(), 4);
  // result += checkValue(_order != compareIngredient.getOrder(), 5);
  // result += checkValue(!storePlace.equals(compareIngredient.getStorePlace()),
  // 6);
  //
  // return result;
  // }

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
    return name;
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
    this.name = name;
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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
