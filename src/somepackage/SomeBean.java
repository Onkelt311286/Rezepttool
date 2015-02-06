package somepackage;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.bean.*;
import javax.swing.JOptionPane;

import mainwindow.MainWindow;

import org.eclipse.jdt.internal.compiler.batch.Main;

import util.XMLConfigurationLoader;

@ManagedBean
public class SomeBean {
  private String someProperty = "Blah, blah";

  public String getSomeProperty() {
    return(someProperty);
  }

  public void setSomeProperty(String someProperty) {
    this.someProperty = someProperty;
  }
  
  public String someActionControllerMethod() {
    return("page-b");  // Means to go to page-b.xhtml (since condition is not mapped in faces-config.xml)
  }
  
  public String someOtherActionControllerMethod() {
    return("index");  // Means to go to index.xhtml (since condition is not mapped in faces-config.xml)
  }
}
