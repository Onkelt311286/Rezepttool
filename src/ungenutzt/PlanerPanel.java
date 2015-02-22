package ungenutzt;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.DayPlan;
import data.Recipe;
import ungenutzt.PlanerDayPanel.MOUSESTATUS;
import util.DataBaseControl;

public class PlanerPanel extends JPanel {

  private DataBaseControl           dataBaseControl;
  private RecipeTable               recipeTable;
  private RecipeDetailsModel        recipeModel;
  private ResourceBundle            languageBundle;
  private JPanel                    dayListPanel;
  private ArrayList<PlanerDayPanel> dayPanelList;
  private MouseListener             mouseListener;
  private int                       currentHighestDay;
  private int                       currentLowestDay;

  /**
   * Create the panel.
   * 
   * @param TODO
   * 
   * @param _recipeTable
   * @param languageBundle
   */
  public PlanerPanel(DataBaseControl dataBaseControl, RecipeTable recipeTable, ResourceBundle languageBundle) {
    setLayout(new BorderLayout(0, 0));

    this.dataBaseControl = dataBaseControl;
    this.recipeTable = recipeTable;
    this.recipeModel = (RecipeDetailsModel) recipeTable.getModel();
    this.languageBundle = languageBundle;
    dayPanelList = new ArrayList<PlanerDayPanel>();
    dayListPanel = new JPanel();
    JScrollPane dayListScrollPane = new JScrollPane(dayListPanel);
    dayListPanel.setLayout(new BoxLayout(dayListPanel, BoxLayout.X_AXIS));
    add(dayListScrollPane, BorderLayout.CENTER);

    JButton previousDayButton = new JButton(languageBundle.getString("PreviousDay"));
    add(previousDayButton, BorderLayout.WEST);
    previousDayButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        currentLowestDay--;
        addDayPanel(currentLowestDay);
      }
    });

    JButton nextDayButton = new JButton(languageBundle.getString("NextDay"));
    add(nextDayButton, BorderLayout.EAST);
    nextDayButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        currentHighestDay++;
        addDayPanel(currentHighestDay);
      }
    });

    currentHighestDay = -1;
    currentLowestDay = 0;
  }

  public void initDayPanelList() {
    int daysToPlan = 7;
    for (int day = 0; day < daysToPlan; day++) {
      currentHighestDay++;
      addDayPanel(day);
    }
    dayPanelList.get(0).changePlanStatus(MOUSESTATUS.CLICKED);
    recipeTable.getRowSorter().toggleSortOrder(0);
    recipeTable.getRowSorter().toggleSortOrder(5);
    recipeTable.getRowSorter().toggleSortOrder(5);
  }

  public void addDayPanel(int dayFromToday) {
    DayPlan dayPlan = dataBaseControl.loadDayPlanData(recipeModel, dayFromToday);
    PlanerDayPanel dayPanel = new PlanerDayPanel(dayPlan, recipeModel, languageBundle);
    dayPanelList.add(dayPanel);
    for (Recipe recipe : dayPlan.getRecipes()) {
      JLabel recipeNameLabel = new JLabel(recipe.getName());
      dayPanel.getRecipeListPanel().add(recipeNameLabel);
    }
    if (dayFromToday == currentHighestDay) {
      dayListPanel.add(dayPanel);
    }
    else {
      dayListPanel.add(dayPanel, 0);
    }
    dayPanel.addMouseListener(mouseListener);
    updateUI();
  }

  public ArrayList<PlanerDayPanel> getSelectedPanels() {
    ArrayList<PlanerDayPanel> result = new ArrayList<PlanerDayPanel>();
    for (PlanerDayPanel dayPanel : dayPanelList) {
      if (dayPanel.isClicked) {
        result.add(dayPanel);
      }
    }
    return result;
  }

  @Override
  public void addMouseListener(MouseListener mouseListener) {
    this.mouseListener = mouseListener;
    super.addMouseListener(mouseListener);
  }

  public ArrayList<PlanerDayPanel> getDayPanelList() {
    return dayPanelList;
  }
}
