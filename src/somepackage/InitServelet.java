package somepackage;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitServelet extends HttpServlet {
  
  static String testString;

  public void init() throws ServletException {
    System.out.println("init");
    testString = "bla";
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
  }
}
