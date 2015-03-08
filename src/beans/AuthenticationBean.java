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

  private String  userName;
  private String  password;
  private User    current;
  private boolean dialogVisible;

  public String login() {
    current = ServeletBackbone.DataBaseControl.loadUserData(userName.toUpperCase(), password);
    if (current == null) {
      System.out.println("User unbekannt, bitte versuchen Sie es noch einmal");
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User unbekannt, bitte versuchen Sie es noch einmal"));
      return (userName = password = null);
    }
    else {
      dialogVisible = true;
      return "authSites/recipe?faces-redirect=true";
    }
  }

  public String logout() {
    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    return "index?faces-redirect=true";
  }

  public boolean isInvisibleDialog() {
    dialogVisible = false;
    return dialogVisible;
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

  public boolean isDialogVisible() {
    return dialogVisible;
  }

  public void setDialogVisible(boolean dialogVisible) {
    this.dialogVisible = dialogVisible;
  }
}
