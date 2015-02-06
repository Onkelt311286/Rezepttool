package mainwindow.header;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

import util.DataMailConfiguration;


public class HeaderMenuBar extends JMenuBar {

  private JRadioButtonMenuItem germanButton;
  private JRadioButtonMenuItem englishButton;
  private ResourceBundle       languageBundle;
  private String               dbSource;
  private DataMailConfiguration    mailConfig;

  public HeaderMenuBar(String dbSource, DataMailConfiguration mailConfig, ResourceBundle languageBundle) {
    super();

    this.dbSource = dbSource;
    this.mailConfig = mailConfig;
    this.languageBundle = languageBundle;

    JMenu dbSourceMenu = new JMenu(languageBundle.getString("MenuDBSourceTitle"));
    add(dbSourceMenu);
    JMenuItem changeDBSource = new JMenuItem(languageBundle.getString("MenuChangeDB"));
    changeDBSource.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(getDbSource());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showDialog(null, getLanguageBundle().getString("SelectDBDir"));
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          // source += "\\RecipeDB.h2.db";
          setDbSource(fileChooser.getSelectedFile().getAbsoluteFile().toString());
          JOptionPane.showMessageDialog(null, getLanguageBundle().getString("ChangeDBSourceMessage"), getLanguageBundle().getString("ChangeDBSourceTitle"), JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });
    dbSourceMenu.add(changeDBSource);

    JMenu mailMenu = new JMenu(languageBundle.getString("MenuMailTitle"));
    add(mailMenu);
    JMenuItem changeMail = new JMenuItem(languageBundle.getString("MenuChangeMail"));
    changeMail.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JTextField to = new JTextField(getMailConfig().getTo());
        JTextField from = new JTextField(getMailConfig().getFrom());
        JTextField host = new JTextField(getMailConfig().getHost());
        JTextField port = new JTextField(getMailConfig().getPort());
        JTextField username = new JTextField(getMailConfig().getUsername());
        JTextField password = new JPasswordField(getMailConfig().getPassword());
        Object[] message = { getLanguageBundle().getString("MailTo"), to, getLanguageBundle().getString("MailFrom"), from, getLanguageBundle().getString("MailHost"), host, getLanguageBundle().getString("MailPort"), port, getLanguageBundle().getString("MailUser"), username, getLanguageBundle().getString("MailPW"), password };
        int option = JOptionPane.showConfirmDialog(null, message, getLanguageBundle().getString("MailDialogTitle"), JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
          getMailConfig().setTo(to.getText());
          getMailConfig().setFrom(from.getText());
          getMailConfig().setHost(host.getText());
          getMailConfig().setPort(port.getText());
          getMailConfig().setUsername(username.getText());
          getMailConfig().setPassword(password.getText());
        }
      }
    });
    mailMenu.add(changeMail);

    JMenu languageMenu = new JMenu(languageBundle.getString("MenuLanguageTitle"));
    add(languageMenu);

    ButtonGroup languageGroup = new ButtonGroup();
    germanButton = new JRadioButtonMenuItem(languageBundle.getString("german"));
    englishButton = new JRadioButtonMenuItem(languageBundle.getString("english"));

    germanButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, getLanguageBundle().getString("ChangeLanguageMessage"), getLanguageBundle().getString("ChangeLanguageTitle"), JOptionPane.INFORMATION_MESSAGE);
      }
    });

    englishButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, getLanguageBundle().getString("ChangeLanguageMessage"), getLanguageBundle().getString("ChangeLanguageTitle"), JOptionPane.INFORMATION_MESSAGE);
      }
    });

    if (languageBundle.getLocale().getLanguage().equals("de")) {
      germanButton.setSelected(true);
    }
    else {
      englishButton.setSelected(true);
    }

    languageGroup.add(germanButton);
    languageGroup.add(englishButton);
    languageMenu.add(germanButton);
    languageMenu.add(englishButton);
  }

  public String getSelectedLanguage() {
    if (germanButton.isSelected()) {
      return "de";
    }
    else {
      return "en";
    }
  }

  public ResourceBundle getLanguageBundle() {
    return languageBundle;
  }

  public String getDbSource() {
    return dbSource;
  }

  public void setDbSource(String dbSource) {
    this.dbSource = dbSource;
  }

  public DataMailConfiguration getMailConfig() {
    return mailConfig;
  }

  public void setMailConfig(DataMailConfiguration mailConfig) {
    this.mailConfig = mailConfig;
  }
}