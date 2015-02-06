package mainwindow;

import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;

public class MainUndoManager extends UndoManager {

  ArrayList<JComponent> undoComponents;
  ArrayList<JComponent> redoComponents;
  
  public MainUndoManager() {
    super();
    undoComponents = new ArrayList<JComponent>();
    redoComponents = new ArrayList<JComponent>();
  }
  
  public void checkComponents(){
    if (canUndo()) {
      for (JComponent comp : undoComponents) {
        comp.setEnabled(true);
      }
    }
    else {
      for (JComponent comp : undoComponents) {
        comp.setEnabled(false);
      }
    }
    if (canRedo()) {
      for (JComponent comp : redoComponents) {
        comp.setEnabled(true);
      }
    }
    else {
      for (JComponent comp : redoComponents) {
        comp.setEnabled(false);
      }
    }
  }

  @Override
  public void undoableEditHappened(UndoableEditEvent event) {
    super.undoableEditHappened(event);
    checkComponents();
  }
  
  @Override
  public synchronized void undo() throws javax.swing.undo.CannotUndoException {
    super.undo();
    checkComponents();
  }
  
  @Override
  public synchronized void redo() throws javax.swing.undo.CannotUndoException {
    super.redo();
    checkComponents();
  }
  
  public void watchUndoComponent(JComponent component){
    undoComponents.add(component);
    checkComponents();
  }
  
  public void watchRedoComponent(JComponent component){
    redoComponents.add(component);
    checkComponents();
  }
  
  public void registerUndoableEdit(AbstractUndoableEdit action, AbstractTableModel model) {
    UndoableEditEvent editEvent = new UndoableEditEvent(model, action);
    undoableEditHappened(editEvent);
  }
}
