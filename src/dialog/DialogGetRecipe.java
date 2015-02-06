package dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import mainwindow.MainWindow;
import mainwindow.data.Ingredient;
import mainwindow.data.Recipe;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import recipefinder.ingredient.IngredientFinderModel;

public class DialogGetRecipe extends JDialog implements WindowListener {

  private Recipe         _recipe;
  private JTabbedPane    _tabbedPane;
  private JTextField     _fileTextField;
  private JTextField     _urlTextField;
  private JTextField     _customTextField;
  private boolean        _isConfirmed;
  private boolean        _success;
  private MainWindow     mainWindow;
  private ResourceBundle languageBundle;

  public DialogGetRecipe(MainWindow mainWindow, ResourceBundle languageBundle) {
    super();
    setSize(600, 150);
    setTitle(languageBundle.getString("LoadRecipeTitle"));
    setResizable(false);
    setModal(true);

    this.mainWindow = mainWindow;
    this.languageBundle = languageBundle;

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    add(mainPanel);

    JPanel filePanel = new JPanel();
    JButton fileButton = new JButton(languageBundle.getString("ChooseFile"));
    fileButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser fileChooser = new JFileChooser(".");
        int returnValue = fileChooser.showOpenDialog(getParent());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          _fileTextField.setText(fileChooser.getSelectedFile().getAbsoluteFile().toString());
        }
      }
    });

    _fileTextField = new JTextField();
    _fileTextField.setPreferredSize(new Dimension(370, 25));
    filePanel.add(fileButton);
    filePanel.add(_fileTextField);

    JPanel urlPanel = new JPanel();
    _urlTextField = new JTextField();
    _urlTextField.setPreferredSize(new Dimension(470, 25));
    urlPanel.add(_urlTextField);

    JPanel customPanel = new JPanel();
    _customTextField = new JTextField();
    _customTextField.setPreferredSize(new Dimension(470, 25));
    customPanel.add(_customTextField);

    _tabbedPane = new JTabbedPane();

    _tabbedPane.addTab(languageBundle.getString("RecipeFromURL"), urlPanel);
    _tabbedPane.addTab(languageBundle.getString("RecipeFromFile"), filePanel);
    _tabbedPane.addTab(languageBundle.getString("CustomRecipe"), customPanel);

    JPanel buttonPanel = new JPanel();
    JButton okButton = new JButton(languageBundle.getString("OK"));
    okButton.setPreferredSize(new Dimension(235, 25));
    okButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        String source = "";
        if (_tabbedPane.getSelectedIndex() == 1) {
          source = _fileTextField.getText();
          _recipe = fillRecipeFromFile(source);
        }
        else if (_tabbedPane.getSelectedIndex() == 0) {
          source = _urlTextField.getText();
          _recipe = fillRecipeFromURL(source);
        }
        else {
          source = _customTextField.getText();
          _recipe = new Recipe(source);
          _success = true;
        }

        if (_success) {
          _isConfirmed = true;
        }
        else {
          _isConfirmed = false;
        }
        setVisible(false);
      }
    });

    JButton cancelButton = new JButton(languageBundle.getString("Cancel"));
    cancelButton.setPreferredSize(new Dimension(235, 25));
    cancelButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        _isConfirmed = false;
        setVisible(false);
      }
    });
    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);

    mainPanel.add(_tabbedPane);
    mainPanel.add(buttonPanel);
    setVisible(true);
  }

  protected Recipe fillRecipeFromURL(String source) {
    Document doc = null;
    try {
      doc = Jsoup.connect(source).get();
    }
    catch (IOException | IllegalArgumentException e) {
      JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("NoURLTitle"), JOptionPane.ERROR_MESSAGE);
    }
    return fillRecipe(doc);
  }

  protected Recipe fillRecipeFromFile(String source) {
    Document doc = null;
    try {
      File input = new File(source);
      doc = Jsoup.parse(input, "ISO-8859-1", "");
    }
    catch (IOException e) {
      JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("NoFileTitle"), JOptionPane.ERROR_MESSAGE);
    }
    return fillRecipe(doc);
  }

  protected Recipe fillRecipe(Document doc) {

    Recipe recipe = null;
    _success = true;

    if (doc != null && doc.select("title").size() > 0) {
      recipe = new Recipe(doc.select("title").get(0).ownText());
      Elements elements = doc.select("tr[class$=incredients]");
      int order = 0;
      for (Element element : elements) {
        String name = "";
        String amount = "";
        for (Element child : element.children()) {
          if (child.attributes().toString().equals(" class=\"amount\"")) {
            amount = child.ownText();
          }
          else if (child.attributes().toString().equals(" valign=\"top\"")) {
            name = child.ownText();
          }
        }
        Ingredient ingred = new Ingredient(name, amount, recipe, order);
        order++;
        recipe.addIngredient(ingred);
      }
      elements = doc.select("div[class$=content-left]");
      for (Element element : elements) {
        if (element.attributes().toString().equals(" class=\"content-left\"")) {
          recipe.setFormula(element.ownText());
        }
      }
      elements = doc.select("table[id$=recipe-info]");
      for (Element element : elements) {
        Element child = element.children().get(0).children().get(0).children().get(1);
        recipe.setDuration(child.ownText());
      }
    }
    else {
      JOptionPane.showMessageDialog(null, languageBundle.getString("NoCKMessage"), languageBundle.getString("NoCKTitle"), JOptionPane.ERROR_MESSAGE);
      _success = false;
    }

    return recipe;
  }

  public Recipe getRecipe() {
    return _recipe;
  }

  public boolean isConfirmed() {
    return _isConfirmed;
  }

  @Override
  public void windowActivated(WindowEvent arg0) {
  }

  @Override
  public void windowClosed(WindowEvent arg0) {
    _isConfirmed = false;
    setVisible(false);
  }

  @Override
  public void windowClosing(WindowEvent arg0) {
  }

  @Override
  public void windowDeactivated(WindowEvent arg0) {
  }

  @Override
  public void windowDeiconified(WindowEvent arg0) {
  }

  @Override
  public void windowIconified(WindowEvent arg0) {
  }

  @Override
  public void windowOpened(WindowEvent arg0) {
  }

}
