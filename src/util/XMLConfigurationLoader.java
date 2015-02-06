package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class XMLConfigurationLoader {

  private XMLConfiguration  _config;
  private String            _dbSourceFileName = "";
  private Locale            locale;
  private ResourceBundle    languageBundle;
  private DataMailConfiguration mailConfig;

  public XMLConfigurationLoader() {
    String configFilename = "config/HTMLParseCK.properties.xml";
    _config = new XMLConfiguration();
    _config.setDelimiterParsingDisabled(true);
    File configFile = new File(configFilename);
    Writer writer = null;
    try {
      if (!configFile.isFile()) {
        writer = createConfigFile(configFile);
      }
      
      _config.setFileName(configFilename);
      _config.load();
      
      _dbSourceFileName = _config.getString("DataBaseFile");
      if (_dbSourceFileName == null) {
        _config.addProperty("DataBaseFile", "");
        _dbSourceFileName = _config.getString("DataBaseFile");
      }

      languageBundle = createLanguageBundle();
      createDataBaseFile();

      String password = _config.getString("Mail.Password");
      if(password != null){
        password = decrypt(password);
      }
      mailConfig = new DataMailConfiguration(_config.getString("Mail.To"), _config.getString("Mail.From"), _config.getString("Mail.Host"), _config.getString("Mail.Port"), _config.getString("Mail.Username"), password);

    }
    catch (IOException | ConfigurationException e) {
      JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("NoXMLErrorTitle"), JOptionPane.ERROR_MESSAGE);
      try {
        writer.close();
      }
      catch (Exception ex) {
      }
    }
  }

  private String decrypt(String password) {
    String result = "";
    try {
      SecretKeySpec secretKeySpec = createSecretKeySpec();
      BASE64Decoder myDecoder = new BASE64Decoder();
      byte[] crypted = myDecoder.decodeBuffer(password);
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
      byte[] cipherData2 = cipher.doFinal(crypted);
      result = new String(cipherData2);
    }
    catch (NoSuchAlgorithmException | IOException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("FailedToDecryptTitle"), JOptionPane.WARNING_MESSAGE);
    }
    return result;
  }

  public String encrypt(String password) {
    String result = "";
    try {
      SecretKeySpec secretKeySpec = createSecretKeySpec();
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      byte[] encrypted = cipher.doFinal(password.getBytes());
      BASE64Encoder myEncoder = new BASE64Encoder();
      result = myEncoder.encode(encrypted);
    }
    catch (NoSuchAlgorithmException | IOException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("FailedToEncryptTitle"), JOptionPane.WARNING_MESSAGE);
    }
    return result;
  }

  private SecretKeySpec createSecretKeySpec() throws UnsupportedEncodingException, NoSuchAlgorithmException {
    String keyStr = "RecipeDBPWKey";
    byte[] key = (keyStr).getBytes("UTF-8");
    MessageDigest sha = MessageDigest.getInstance("MD5");
    key = sha.digest(key);
    key = Arrays.copyOf(key, 16);
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    return secretKeySpec;
  }

  private void createDataBaseFile() throws IOException, ConfigurationException {
    File dataBaseFile = new File(_dbSourceFileName + "\\RecipeDB.h2.db");
    if (!dataBaseFile.isFile()) {
      JFileChooser fileChooser = new JFileChooser(_dbSourceFileName);
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int returnValue = fileChooser.showDialog(null, languageBundle.getString("CreateDBDir"));
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        _dbSourceFileName = fileChooser.getSelectedFile().getAbsoluteFile().toString();
        dataBaseFile = new File(_dbSourceFileName + "\\RecipeDB.h2.db");
        dataBaseFile.createNewFile();
        _config.setProperty("DataBaseFile", _dbSourceFileName);
        // _dbSourceFileName = _config.getString("DataBaseFile");
        _config.save();
      }
      else {
        JOptionPane.showMessageDialog(null, languageBundle.getString("NoXMLExitingMessage"), languageBundle.getString("NoXMLExitingTitle"), JOptionPane.ERROR_MESSAGE);
        System.exit(1);
      }
    }
  }

  private ResourceBundle createLanguageBundle() {
    String localeString = _config.getString("Locale");
    if (localeString == null) {
      _config.addProperty("Locale", "en");
      localeString = "en";
    }
    else if (!localeString.equals("de") && !localeString.equals("en")) {
      _config.setProperty("Locale", "en");
      localeString = "en";
    }
    locale = new Locale(localeString);
    return ResourceBundle.getBundle("LanguageBundle", locale);
  }

  private Writer createConfigFile(File configFile) throws IOException, UnsupportedEncodingException, FileNotFoundException {
    Writer writer;
    File configDirectory = new File("config");
    configDirectory.mkdirs();
    configFile.createNewFile();
    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile.getAbsolutePath()), "utf-8"));
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
    writer.write("<config>\n");
    writer.write("</config>");
    writer.close();
    return writer;
  }

  public void storeProperty(String key, String value) {
    try {
      _config.setProperty(key, value);
      _config.save();
    }
    catch (ConfigurationException e) {
      JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("NoXMLErrorTitle"), JOptionPane.ERROR_MESSAGE);
    }
  }

  public String getDBSourceFileName() {
    return _dbSourceFileName;
  }

  public Locale getLocale() {
    return locale;
  }

  public DataMailConfiguration getMailConfig() {
    return mailConfig;
  }

  public void setMailConfig(DataMailConfiguration mailConfig) {
    this.mailConfig = mailConfig;
  }
}
