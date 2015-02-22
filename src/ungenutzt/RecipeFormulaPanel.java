package ungenutzt;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class RecipeFormulaPanel extends JPanel {

  private JTextArea _textField;
  
  public RecipeFormulaPanel(RecipeFormulaDocumentListener documentListener) {
    super();
    setLayout(new BorderLayout());
    _textField = new JTextArea();
    JScrollPane textScrollPane = new JScrollPane(_textField);
    _textField.setLineWrap(true);
    _textField.setWrapStyleWord(true);
    _textField.getDocument().addDocumentListener(documentListener);
    add(textScrollPane);
  }
  
  public JTextArea getTextField(){
    return _textField;
  }
}
