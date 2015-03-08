package beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.SelectEvent;

import data.Recipe;
import ungenutzt.XMLConfigurationLoader;
import util.DataBaseControl;
import util.ServeletBackbone;

@ManagedBean
public class AutoCompleteRecipeBean {

  private String filterString;
  private List<Recipe> recipes;
  
//  @PostConstruct
//  public void init() {
//	recipes = ServeletBackbone.DataBaseControl.loadFilteredRecipesByName("");
//  }

  public AutoCompleteRecipeBean() {
	  recipes = ServeletBackbone.DataBaseControl.loadFilteredRecipesByName("");
  }
  
  public void handleSelect(SelectEvent event) {
	  Object item = event.getObject();
	  FacesMessage msg = new FacesMessage("Selected", "Item:" + item);
	  System.out.println("Item " + item + " ListSize = " + recipes.size());
  }

  public List<String> completeText(String query) {
	recipes.clear();  
    recipes = ServeletBackbone.DataBaseControl.loadFilteredRecipesByName(query.toUpperCase());
    System.out.println("updatesize = " + recipes.size());
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