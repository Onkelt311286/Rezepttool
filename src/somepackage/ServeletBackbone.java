package somepackage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mainwindow.data.Recipe;
import util.DataBaseControl;
import util.XMLConfigurationLoader;

public class ServeletBackbone extends HttpServlet {
  
  public static DataBaseControl DataBaseControl;
  
  public void init() throws ServletException {
    XMLConfigurationLoader config = new XMLConfigurationLoader();
    Locale aktiveLocale = config.getLocale();
    Locale.setDefault(aktiveLocale);
    ResourceBundle languageBundle = ResourceBundle.getBundle("LanguageBundle", aktiveLocale);
    DataBaseControl = new DataBaseControl(config.getDBSourceFileName(), languageBundle);
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
  }
}
