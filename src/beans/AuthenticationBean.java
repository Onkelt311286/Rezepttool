package beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import util.ServeletBackbone;
import data.User;

@ManagedBean
@SessionScoped
public class AuthenticationBean {

  private String userName;
  private String password;
  private User   current;

  public String login() {
    current = ServeletBackbone.DataBaseControl.loadUserData(userName.toUpperCase(), password);
    if (current == null) {
      System.out.println("User unbekannt, bitte versuchen Sie es noch einmal");
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User unbekannt, bitte versuchen Sie es noch einmal"));
      return (userName = password = null);
    }
    else {
      System.out.println("Login complete");
      return "authSites/week?faces-redirect=true";
    }
  }

  public String logout() {
    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    return "index?faces-redirect=true";
  }

  public boolean isLoggedIn() {
    return current != null;
  }

  public User getCurrent() {
    return current;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String user) {
    this.userName = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
