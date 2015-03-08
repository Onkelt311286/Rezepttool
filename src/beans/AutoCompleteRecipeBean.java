package beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.SelectEvent;

import data.Ingredient;
import data.Recipe;
import util.ServeletBackbone;

@ManagedBean
public class AutoCompleteRecipeBean {

  private String       filterString;
  private List<Recipe> recipes;

  @PostConstruct
  public void init() {
    recipes = ServeletBackbone.DataBaseControl.loadFilteredRecipesByName("");
  }

  // public AutoCompleteRecipeBean() {
  // recipes = ServeletBackbone.DataBaseControl.loadFilteredRecipesByName("");
  // }

  public void handleSelect(SelectEvent event) {
    Object item = event.getObject();
    FacesMessage msg = new FacesMessage("Selected", "Item:" + item);
    recipes = ServeletBackbone.DataBaseControl.loadFilteredRecipesByName(filterString.toUpperCase());
    System.out.println("Item " + item + " ListSize = " + recipes.size() + " filterString = " + filterString);
  }

  public List<String> completeText(String query) {
    recipes = ServeletBackbone.DataBaseControl.loadFilteredRecipesByName(query.toUpperCase());
    List<String> results = new ArrayList<String>();
    for (Recipe recipe : recipes) {
      results.add(recipe.getName());
    }
    return results;
  }

  public String getFilterString() {
    return filterString;
  }

  public void setFilterString(String filterString) {
    this.filterString = filterString;
  }

  public List<Recipe> getRecipes() {
    return recipes;
  }

  public void setRecipes(List<Recipe> recipes) {
    this.recipes = recipes;
  }
}