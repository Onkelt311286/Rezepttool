package somepackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;

import mainwindow.data.Recipe;
import util.DataBaseControl;
import util.XMLConfigurationLoader;

@ManagedBean
public class AutoCompleteView {

  private String filterString;

  public AutoCompleteView() {
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