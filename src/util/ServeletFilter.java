package util;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.AuthenticationBean;

public class ServeletFilter implements Filter {

  @Override
  public void init(FilterConfig arg0) throws ServletException {
    System.out.println("init Filter: " + arg0.getFilterName());
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    System.out.println("Filtering");

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    AuthenticationBean userManager = (AuthenticationBean) req.getSession().getAttribute("authenticationBean");

//    Enumeration e = req.getSession().getAttributeNames();
//    while (e.hasMoreElements()) {
//      String name = (String) e.nextElement();
//      String value = req.getSession().getAttribute(name).toString();
//      System.out.println("name is: " + name + " value is: " + value);
//    }

    if (userManager == null || !userManager.isLoggedIn()) {
      System.out.println("no loggin user found");
      res.sendRedirect(req.getContextPath() + "/index.jsf");
    }
    else {
      System.out.println("still logged in");
      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy() {
    System.out.println("destroy");
  }
}
