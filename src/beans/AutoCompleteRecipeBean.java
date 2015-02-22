package beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;

import data.Recipe;
import ungenutzt.XMLConfigurationLoader;
import util.DataBaseControl;
import util.ServeletBackbone;

@ManagedBean
public class AutoCompleteRecipeBean {

  private String filterString;

  public AutoCompleteRecipeBean() {
  }

  public List<String> completeText(String query) {
    List<Recipe> recipes = ServeletBackbone.DataBaseControl.loadFilteredRecipesByName(query.toUpperCase());
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
}