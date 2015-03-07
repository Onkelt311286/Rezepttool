package util;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.AuthenticationBean;
import data.Recipe;

public class ServeletBackbone extends HttpServlet {
  
  public static final String DATABASE_SOURCE_FILE = ".\\Rezepttool - Datenbank";
  public static DataBaseControl DataBaseControl;
  
  public void init() throws ServletException {
    Locale aktiveLocale = new Locale("de");
    Locale.setDefault(aktiveLocale);
    ResourceBundle languageBundle = ResourceBundle.getBundle("LanguageBundle", aktiveLocale);
    DataBaseControl = new DataBaseControl(DATABASE_SOURCE_FILE, languageBundle);
    System.out.println("Initialized Servelet Backbone with DataBase Connection to: " + DataBaseControl.getDataBaseName());
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
  }
}
