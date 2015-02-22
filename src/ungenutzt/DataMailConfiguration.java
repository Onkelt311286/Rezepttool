package ungenutzt;

public class DataMailConfiguration {

  private String to;
  private String from;
  private String host;
  private String port;
  private String username;
  private String password;

  public DataMailConfiguration(String to, String from, String host, String port, String username, String password) {
    super();
    this.setTo(to);
    this.setFrom(from);
    this.setHost(host);
    this.setPort(port);
    this.setUsername(username);
    this.setPassword(password);
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    if (port != null) {
      this.port = port;
    }
    else {
      this.port = "";
    }
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    if (from != null) {
      this.from = from;
    }
    else {
      this.from = "";
    }
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    if (to != null) {
      this.to = to;
    }
    else {
      this.to = "";
    }
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    if (host != null) {
      this.host = host;
    }
    else {
      this.host = "";
    }
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    if (username != null) {
      this.username = username;
    }
    else {
      this.username = "";
    }
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    if (password != null) {
      this.password = password;
    }
    else {
      this.password = "";
    }
  }
}
