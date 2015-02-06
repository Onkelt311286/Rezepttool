package recipedetails.recipe;

import java.util.ResourceBundle;


import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import mainwindow.data.Recipe;





public class RecipeFormulaDocumentListener implements DocumentListener {

  private RecipeTable _table;
  ResourceBundle languageBundle;

  public RecipeFormulaDocumentListener(RecipeTable table, ResourceBundle languageBundle) {
    _table = table;
    this.languageBundle = languageBundle;
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    updateFormula(e.getDocument());  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    updateFormula(e.getDocument());
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    updateFormula(e.getDocument());
  }
  
  private void updateFormula(Document doc){
    try {
      if(_table.getSelectedRow() != -1){
        Recipe recipe = ((RecipeDetailsModel) _table.getModel()).getRecipes().get(_table.convertRowIndexToModel(_table.getSelectedRow()));
        recipe.setFormula(doc.getText(0, doc.getLength()));
      }
    }
    catch (BadLocationException e) {
      JOptionPane.showMessageDialog(null, e.toString(), languageBundle.getString("NoDocumentTitle"), JOptionPane.ERROR_MESSAGE);
    }
  }
}
