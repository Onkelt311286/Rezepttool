package ungenutzt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.DayPlan;
import data.Recipe;


import util.ToolConstants;


public class PlanerDayPanel extends JPanel {

  public static enum MOUSESTATUS {
    NONE, HOVERED, CLICKED;
  }

  public DayPlan     dayPlan;
  public String      date;
  public boolean     isClicked;
  public JPanel      recipeListPanel;
  public RecipeDetailsModel recipeModel;

  /**
   * Create the panel.
   * 
   * @param i
   */
  public PlanerDayPanel(DayPlan plan, RecipeDetailsModel recipeModel, ResourceBundle languageBundle) {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    this.dayPlan = plan;
    this.recipeModel = recipeModel;

    Date planDay = plan.getDay();
    date = ToolConstants.getDateString(planDay, false);

    Date today = new Date();
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    cal1.setTime(planDay);
    cal2.setTime(today);
    boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    if (sameDay) {
      date += ", " + languageBundle.getString("Today");
    }

    setBorder(BorderFactory.createTitledBorder(date));

    recipeListPanel = new JPanel();
    recipeListPanel.setLayout(new GridLayout(7, 1));
    recipeListPanel.setPreferredSize(new Dimension(200, 10));
    // JScrollPane recipeListPane = new JScrollPane(recipeListPanel);
    add(recipeListPanel, BorderLayout.CENTER);
  }

  public void changePlanStatus(MOUSESTATUS status) {
    if (status == MOUSESTATUS.NONE) {
      if (!isClicked) {
        setBackground(javax.swing.UIManager.getDefaults().getColor("SystemColor.window"));
      }
      else {
        setBackground(new Color(163, 184, 204));
      }
    }
    else if (status == MOUSESTATUS.HOVERED) {
      if (!isClicked) {
        setBackground(javax.swing.UIManager.getDefaults().getColor("Table.selectionBackground"));
      }
      else {
        setBackground(new Color(120, 148, 177));
      }
    }
    else if (status == MOUSESTATUS.CLICKED) {
      setBackground(new Color(163, 184, 204));
      isClicked = true;

      for (Recipe recipe : dayPlan.getRecipes()) {
        recipe.setUsed(true);
      }
    }
  }

  public void addRecipe(Recipe selectedRecipe) {
    dayPlan.addRecipe(selectedRecipe);
    JLabel recipeNameLabel = new JLabel(selectedRecipe.getName());
    recipeListPanel.add(recipeNameLabel);
    updateUI();
  }

  public void removeRecipe(Recipe selectedRecipe) {
    dayPlan.removeRecipe(selectedRecipe);
    for (Component comp : recipeListPanel.getComponents()) {
      if (comp instanceof JLabel) {
        JLabel label = (JLabel) comp;
        if (label.getText().equals(selectedRecipe.getName())) {
          recipeListPanel.remove(label);
        }
      }
    }
    updateUI();
  }

  public String getDate() {
    return date;
  }

  public boolean isClicked() {
    return isClicked;
  }

  public void setClicked(boolean isClicked) {
    this.isClicked = isClicked;
  }

  public DayPlan getDayPlan() {
    return dayPlan;
  }

  public JPanel getRecipeListPanel() {
    return recipeListPanel;
  }
}
