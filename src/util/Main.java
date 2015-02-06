package util;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;


import mainwindow.MainWindow;



public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    XMLConfigurationLoader config = new XMLConfigurationLoader();
    Locale aktiveLocale = config.getLocale();
    Locale.setDefault(aktiveLocale);
    ResourceBundle languageBundle = ResourceBundle.getBundle("LanguageBundle", aktiveLocale);
    
    try {

      new MainWindow(languageBundle, config);
    }
    catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("CriticalError"), JOptionPane.ERROR_MESSAGE);
    }
  }
}
