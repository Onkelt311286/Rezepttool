package util;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

public class AbstractTable extends JTable {
  private AbstractTableModel model;

  public AbstractTable() {
    super();
    setAutoCreateRowSorter(true);
  }

  public void setModel(AbstractTableModel model) {
    super.setModel(model);
    this.model = model;
  }

  public int[] getConvertedRows(int[] rows) {
    int[] convertedRows = new int[rows.length];
    for (int i = 0; i < rows.length; i++) {
      convertedRows[i] = getRowSorter().convertRowIndexToModel(rows[i]);
    }
    return convertedRows;
  }

  public void setColumnWidths(Dimension preferredSize, float[] columnWidth) {
    for (int i = 0; i < model.getColumnCount(); i++) {
      getColumnModel().getColumn(i).setPreferredWidth(Math.round(preferredSize.width * columnWidth[i]));
    }
  }
}
