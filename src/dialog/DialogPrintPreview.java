package dialog;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import mainwindow.MainWindow;
import mainwindow.data.Ingredient;
import mainwindow.data.Recipe;
import mainwindow.planer.PlanerDayPanel;
import mainwindow.planer.PlanerPanel;

import recipedetails.ingredient.IngredientDetailsModel;
import recipedetails.recipe.RecipeDetailsModel;
import recipedetails.recipe.RecipeTable;
import util.AbstractRecipeModel;
import util.DataMailConfiguration;
import util.ToolConstants;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

public class DialogPrintPreview extends JDialog {

  private MainWindow           mainWindow;
  private JTable               ingredientListTable;
  private IngredientPrintModel houseHoldModel;
  private JTable               recipeFormulaTable;
  private JTextArea            recipeFormulaArea;
  private JTable               dayPlanTable;
  private JTextArea            dayPlanArea;
  private JTabbedPane          _tabbedPane;
  private ResourceBundle       languageBundle;

  public DialogPrintPreview(MainWindow mainWindow, ResourceBundle languageBundle) {
    super();
    setSize(1024, 768);
    setTitle(languageBundle.getString("Print"));

    this.mainWindow = mainWindow;
    this.languageBundle = languageBundle;

    JPanel mainPanel = new JPanel();
    getContentPane().add(mainPanel);

    _tabbedPane = new JTabbedPane();

    final String[] ingredientListColumnNames = { languageBundle.getString("Amount"), languageBundle.getString("Name"), languageBundle.getString("StorePlace"), languageBundle.getString("Recipe") };
    JComponent ingredientListSplitPane = initializeIngredientListPane(ingredientListColumnNames);
    _tabbedPane.addTab(languageBundle.getString("PrintTableTitle"), ingredientListSplitPane);

    int selectedRow = mainWindow.getRecipeDetailsTable().getSelectedRow();
    final String[] recipePaneColumnNames = { languageBundle.getString("Amount"), languageBundle.getString("Order"), languageBundle.getString("Name") };
    if (selectedRow >= 0) {
      JComponent formulaSplitPane = initializeRecipePane(recipePaneColumnNames, selectedRow);
      _tabbedPane.addTab(languageBundle.getString("PrintRecipeTitle"), formulaSplitPane);
    }

    final String[] dayPlanColumnNames = { languageBundle.getString("Amount"), languageBundle.getString("Name"), languageBundle.getString("Order"), languageBundle.getString("Recipe") };
    JComponent planSplitPane = initializeDayPlanPane(dayPlanColumnNames);
    _tabbedPane.addTab(languageBundle.getString("PrintDayPlan"), planSplitPane);

    JPanel buttonPanel = new JPanel();
    JButton okButton = new JButton(languageBundle.getString("Print"));
    okButton.setPreferredSize(new Dimension(235, 25));
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        print();
      }
    });

    JButton cancelButton = new JButton(languageBundle.getString("Cancel"));
    cancelButton.setPreferredSize(new Dimension(235, 25));
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        setVisible(false);
      }
    });

    JButton emailButton = new JButton(languageBundle.getString("EMail"));
    emailButton.setPreferredSize(new Dimension(235, 25));
    emailButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        if (_tabbedPane.getTitleAt(_tabbedPane.getSelectedIndex()).equals(getLanguageBundle().getString("PrintTableTitle"))) {
          sendEmail(ingredientListColumnNames);
        }
        else if (_tabbedPane.getTitleAt(_tabbedPane.getSelectedIndex()).equals(getLanguageBundle().getString("PrintRecipeTitle"))) {
          sendEmail(recipePaneColumnNames);
        }
        else if (_tabbedPane.getTitleAt(_tabbedPane.getSelectedIndex()).equals(getLanguageBundle().getString("PrintDayPlan"))) {
          sendEmail(dayPlanColumnNames);
        }
      }
    });

    buttonPanel.add(okButton);
    buttonPanel.add(emailButton);
    buttonPanel.add(cancelButton);
    mainPanel.setLayout(new BorderLayout(0, 0));

    mainPanel.add(_tabbedPane, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
  }

  private JSplitPane initializeIngredientListPane(String[] ingredientListColumnNames) {
    DefaultTableModel model = new DefaultTableModel();
    for (String string : ingredientListColumnNames) {
      model.addColumn(string);
    }
    ingredientListTable = new JTable(model);
    ingredientListTable.setAutoCreateRowSorter(true);
    ingredientListTable.setEnabled(false);
    for (PlanerDayPanel selectedPanel : mainWindow.getRecipePlanerPanel().getSelectedPanels()) {
      for (Recipe recipe : selectedPanel.getDayPlan().getRecipes()) {
        for (Ingredient ingred : recipe.getIngredients()) {
          if (!ingred.isAvailable()) {
            Object[] row = { ingred.getAmount(), ingred.getName(), ingred.getStorePlace(), ingred.getRecipe().getName() };
            model.addRow(row);
          }
        }
      }
    }
    JScrollPane IngredientListScrollPane = new JScrollPane(ingredientListTable);

    String[] houseHoldColumnNames = { languageBundle.getString("Amount"), languageBundle.getString("Ingredient"), languageBundle.getString("Order"), languageBundle.getString("StorePlace"), languageBundle.getString("Recipe"), languageBundle.getString("Available") };
    houseHoldModel = new IngredientPrintModel(houseHoldColumnNames, languageBundle);
    houseHoldModel.addTableModelListener(new TableModelListener() {
      @Override
      public void tableChanged(TableModelEvent e) {
        if (e.getColumn() != -1) {
          if (houseHoldModel.getColumnName(e.getColumn()).equals(languageBundle.getString("Available"))) {
            Ingredient ingred = houseHoldModel.getIngredients().get(e.getFirstRow());
            if (!ingred.isAvailable()) {
              Object[] row = { ingred.getAmount(), ingred.getName(), ingred.getStorePlace(), ingred.getRecipe().getName() };
              ((DefaultTableModel) ingredientListTable.getModel()).addRow(row);
            }
            else {
              DefaultTableModel model = ((DefaultTableModel) ingredientListTable.getModel());
              int row = -1;
              for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).equals(ingred.getName()) && model.getValueAt(i, 2).equals(ingred.getStorePlace())) {
                  row = i;
                  break;
                }
              }
              model.removeRow(row);
            }
            ingredientListTable.updateUI();
          }
        }
      }
    });
    JTable HouseHoldListTable = new JTable(houseHoldModel);
    HouseHoldListTable.setAutoCreateRowSorter(true);
    ArrayList<Recipe> houseHoldList = new ArrayList<Recipe>();
    for (Recipe recipe : mainWindow.getRecipeListPanel().getAllRecipes()) {
      if (recipe.getName().contains("Haushalt")) {
        houseHoldList.add(recipe);
      }
    }
    for (Recipe recipe : houseHoldList) {
      for (Ingredient ingred : recipe.getIngredients()) {
        houseHoldModel.addRow(ingred, false);
        if (!ingred.isAvailable()) {
          Object[] row = { ingred.getAmount(), ingred.getName(), ingred.getStorePlace(), ingred.getRecipe().getName() };
          model.addRow(row);
        }
      }
    }
    JScrollPane houseHoldListScrollPane = new JScrollPane(HouseHoldListTable);

    ingredientListTable.getRowSorter().toggleSortOrder(1);
    ingredientListTable.getRowSorter().toggleSortOrder(2);

    JButton checkIngredientsButton = new JButton(languageBundle.getString("PurchaseComplete"));
    checkIngredientsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        for (int row = 0; row < ingredientListTable.getRowCount(); row++) {
          String name = (String) ingredientListTable.getModel().getValueAt(row, 1);
          String storePlace = (String) ingredientListTable.getModel().getValueAt(row, 2);
          Ingredient tempIngred = new Ingredient(name, storePlace);
          tempIngred.setAvailable(true);
          mainWindow.updateAllIngredients(tempIngred, tempIngred);
          mainWindow.restoreTableSelection(mainWindow.getIngredientDetailsTable());
          mainWindow.restoreTableSelection(mainWindow.getIngredientFinderTable());
          setVisible(false);
        }
      }
    });
    JPanel checkPanel = new JPanel();
    checkPanel.setLayout(new BorderLayout());
    checkPanel.add(houseHoldListScrollPane, BorderLayout.CENTER);
    checkPanel.add(checkIngredientsButton, BorderLayout.SOUTH);

    JSplitPane ingredientListSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, IngredientListScrollPane, checkPanel);
    ingredientListSplitPane.setDividerLocation(400);
    return ingredientListSplitPane;
  }

  private JSplitPane initializeRecipePane(String[] recipePaneColumnNames, int selectedRow) {
    recipeFormulaArea = new JTextArea();
    recipeFormulaArea.setEditable(false);
    recipeFormulaArea.setLineWrap(true);
    recipeFormulaArea.setWrapStyleWord(true);
    JScrollPane formulaAreaScrollPane = new JScrollPane(recipeFormulaArea);
    DefaultTableModel formulaModel = new DefaultTableModel();
    for (String string : recipePaneColumnNames) {
      formulaModel.addColumn(string);
    }
    recipeFormulaTable = new JTable(formulaModel);
    recipeFormulaTable.setAutoCreateRowSorter(true);
    recipeFormulaTable.setEnabled(false);
    int convertedRow = mainWindow.getRecipeDetailsTable().convertRowIndexToModel(selectedRow);
    if (convertedRow != -1) {
      Recipe recipe = ((RecipeDetailsModel) mainWindow.getRecipeDetailsTable().getModel()).getRecipes().get(convertedRow);
      for (Ingredient ingred : recipe.getIngredients()) {
        Object[] row = { ingred.getAmount(), ingred.getOrder(), ingred.getName() };
        formulaModel.addRow(row);
      }
      recipeFormulaArea.setText(recipe.getFormula());
    }

    JScrollPane formulaTableScrollPane = new JScrollPane(recipeFormulaTable);
    JSplitPane formulaSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formulaAreaScrollPane, formulaTableScrollPane);
    formulaSplitPane.setDividerLocation(625);
    formulaSplitPane.setResizeWeight(0.5);
    return formulaSplitPane;
  }

  private JSplitPane initializeDayPlanPane(String[] dayPlanColumnNames) {
    dayPlanArea = new JTextArea();
    dayPlanArea.setEditable(false);
    dayPlanArea.setLineWrap(true);
    dayPlanArea.setWrapStyleWord(true);
    JScrollPane dayPlanAreaScrollPane = new JScrollPane(dayPlanArea);
    DefaultTableModel formulaModel = new DefaultTableModel();
    for (String string : dayPlanColumnNames) {
      formulaModel.addColumn(string);
    }
    dayPlanTable = new JTable(formulaModel);
    dayPlanTable.setAutoCreateRowSorter(true);
    dayPlanTable.setEnabled(false);
    JScrollPane dayPlanScrollPane = new JScrollPane(dayPlanTable);
    String formulaListText = "";
    for (PlanerDayPanel selectedPanel : mainWindow.getRecipePlanerPanel().getSelectedPanels()) {
      if (selectedPanel.getDayPlan().getRecipes().size() > 0) {
        for (Recipe recipe : selectedPanel.getDayPlan().getRecipes()) {
          formulaListText += " === " + recipe.getName() + " === \n\n";
          formulaListText += recipe.getFormula();
          formulaListText += "\n\n";

          for (Ingredient ingred : recipe.getIngredients()) {
            Object[] row = { ingred.getAmount(), ingred.getName(), ingred.getOrder(), ingred.getRecipe().getName() };
            formulaModel.addRow(row);
          }
        }
      }
    }
    dayPlanArea.setText(formulaListText);
    JSplitPane planSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dayPlanAreaScrollPane, dayPlanScrollPane);
    planSplitPane.setDividerLocation(625);
    planSplitPane.setResizeWeight(0.5);
    return planSplitPane;
  }

  private void print() {
    if (_tabbedPane.getTitleAt(_tabbedPane.getSelectedIndex()).equals(getLanguageBundle().getString("PrintTableTitle"))) {
      try {
        ingredientListTable.print();
      }
      catch (PrinterException e) {
        JOptionPane.showMessageDialog(null, e.toString(), getLanguageBundle().getString("NoPrintList"), JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
      }
    }
    else if (_tabbedPane.getTitleAt(_tabbedPane.getSelectedIndex()).equals(getLanguageBundle().getString("PrintRecipeTitle"))) {
      try {
        recipeFormulaArea.print();
        recipeFormulaTable.print();
      }
      catch (PrinterException e) {
        JOptionPane.showMessageDialog(null, e.toString(), getLanguageBundle().getString("NoPrintRecipe"), JOptionPane.ERROR_MESSAGE);
      }
    }
    else if (_tabbedPane.getTitleAt(_tabbedPane.getSelectedIndex()).equals(getLanguageBundle().getString("PrintDayPlan"))) {
      try {
        dayPlanArea.print();
        dayPlanTable.print();
      }
      catch (PrinterException e) {
        JOptionPane.showMessageDialog(null, e.toString(), getLanguageBundle().getString("NoPrintRecipe"), JOptionPane.ERROR_MESSAGE);
      }
    }
    setVisible(false);
  }

  private void sendEmail(String[] columnNames) {
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    Properties properties = System.getProperties();
    properties.setProperty("mail.smtp.host", mainWindow.getConfigLoader().getMailConfig().getHost());
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.smtp.port", mainWindow.getConfigLoader().getMailConfig().getPort());
    properties.setProperty("mail.smtp.starttls.enable", "true");
    Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mainWindow.getConfigLoader().getMailConfig().getUsername(), mainWindow.getConfigLoader().getMailConfig().getPassword());
      }
    });
    String attachement = "data.pdf";

    try {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(mainWindow.getConfigLoader().getMailConfig().getFrom()));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(mainWindow.getConfigLoader().getMailConfig().getTo()));
      Document document = null;
      if (_tabbedPane.getTitleAt(_tabbedPane.getSelectedIndex()).equals(getLanguageBundle().getString("PrintTableTitle"))) {
        document = createDocument("", columnNames, ingredientListTable, attachement);
        message.setSubject(languageBundle.getString("MailIngredientListSubject") + ", " + ToolConstants.getDateString(mainWindow.getRecipePlanerPanel().getSelectedPanels().get(0).getDayPlan().getDay(), false));
      }
      else if (_tabbedPane.getTitleAt(_tabbedPane.getSelectedIndex()).equals(getLanguageBundle().getString("PrintRecipeTitle"))) {
        document = createDocument(recipeFormulaArea.getText(), columnNames, recipeFormulaTable, attachement);
        String subject = languageBundle.getString("MailRecipeFormulaSubject");
        int selectedRow = mainWindow.getRecipeDetailsTable().getSelectedRow();
        int convertedRow = mainWindow.getRecipeDetailsTable().convertRowIndexToModel(selectedRow);
        if (convertedRow != -1) {
          Recipe recipe = ((RecipeDetailsModel) mainWindow.getRecipeDetailsTable().getModel()).getRecipes().get(convertedRow);
          subject += ", " + recipe.getName();
        }
        message.setSubject(subject);
      }
      else if (_tabbedPane.getTitleAt(_tabbedPane.getSelectedIndex()).equals(getLanguageBundle().getString("PrintDayPlan"))) {
        document = createDocument(dayPlanArea.getText(), columnNames, dayPlanTable, attachement);
        message.setSubject(languageBundle.getString("MailDayPlanSubject") + ", " + ToolConstants.getDateString(mainWindow.getRecipePlanerPanel().getSelectedPanels().get(0).getDayPlan().getDay(), false));
      }
      else {
        message.setSubject(languageBundle.getString("MailSubject"));
      }

      BodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setText(languageBundle.getString("MailText"));
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);
      messageBodyPart = new MimeBodyPart();
      DataSource source = new FileDataSource(attachement);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(attachement);
      multipart.addBodyPart(messageBodyPart);
      message.setContent(multipart);
      Transport.send(message);
    }
    catch (MessagingException | DocumentException | IOException e) {
      JOptionPane.showMessageDialog(null, getLanguageBundle().getString("FailedToMailMessage") + e.toString(), getLanguageBundle().getString("FailedToMailTitle"), JOptionPane.ERROR_MESSAGE);
    }
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    setVisible(false);
  }

  private Document createDocument(String text, String[] columnNames, JTable sourceTable, String attachement) throws DocumentException, FileNotFoundException {
    Document document = new Document();
    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(attachement));
    document.open();
    PdfPTable table = new PdfPTable(columnNames.length);
    for (String string : columnNames) {
      table.addCell(string);
    }
    for (int row = 0; row < sourceTable.getRowCount(); row++) {
      for (int column = 0; column < columnNames.length; column++) {
        table.addCell(sourceTable.getModel().getValueAt(sourceTable.convertRowIndexToModel(row), column).toString());
      }
    }
    document.add(table);
    document.add(new Paragraph(text));
    document.close();
    return document;
  }

  public ResourceBundle getLanguageBundle() {
    return languageBundle;
  }

  public void setLanguageBundle(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
  }
}
