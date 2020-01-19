package dgantt;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * A {@link JTable} to be displayed alongside a {@link GanttChart}.
 *  The look-and-feel of the {@code JTable} is adjusted
 *  to conform with the look-and-feel of the {@code GanttChart}.
 *  A {@link GanttPanel} should be used to synchronize the
 *  components.
 *  
 * @author Hwrn
 */
public class GanttTitleBar
    extends JTable implements TableCellRenderer {
/**
 * =_=.
 */
private static final long serialVersionUID = 1L;

/**
 * The {@code GanttChart} synchronized with this table.
 */
private final GanttChart chart;

/**
 * The {@link GanttTimeAxis} synchronized with this table.
 */
private final GanttTimeAxis timeaxis;

/**
 * Class constructor for a {@code JTable} to be synchronized
 *  with a {@code GanttChart} and {@code GanttHeader},
 *  typically within a {@code GanttPanel}.
 * @param chart synchronized with this
 * @param timeaxis get settings of hight of timeaxis line
 */
//CHECKSTYLE:OFF:HiddenField
public GanttTitleBar(
    final GanttChart chart, final GanttTimeAxis timeaxis) {
  super(new AbstractTableModel() {

    /**
     * =_=.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public int getRowCount() {
      return chart.getTaskSetModel().getRowCount();
      
    }

    @Override
    public int getColumnCount() {
      return 1;
    }

    @Override
    public Object getValueAt(
        final int rowIndex, final int columnIndex) {
      return chart.getTaskSetModel().getTitleAt(rowIndex);
      
    }

    /**
     * the title of the table
     */
    @Override
    public String getColumnName(final int column) {
      return MagicNumber.GANTT_TITLE_HEAD;
      
    }
    
  });
  this.chart = chart;
  this.timeaxis = timeaxis;

  setRowHeight(chart.getRowHeight());

  // adjusted look-and-feel of the JTable
  for (int i = getColumnModel().getColumnCount() - 1;
      i >= 0; --i) {
    getColumnModel().getColumn(i).setHeaderRenderer(this);
    getColumnModel().getColumn(i).setCellRenderer(this);

  }
  setShowGrid(false);
  setIntercellSpacing(new Dimension(0, 0));
  setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
  setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
  // refreash itself when title change in chart.taskset
  chart.getTaskSetModel().connectGanttTitleBar(this);

}

/**
 * draw title of each rows.
 * 
 * @param table the gantt table
 * @param value each work bar
 * @param   isSelected      useless. <p>
 *                          true if the cell is to be rendered with
 *                          the selection highlighted; otherwise
 *                          false.
 * @param   hasFocus        useless. <p>
 *                          if true, render cell appropriately. For
 *                          example, put a special border on the
 *                          cell, if the cell can be edited, render
 *                          in the color used to indicate editing.
 * @param row -1 for title and >=0 for cells
 * @param   column          useless. <p>
 *                          the column index of the cell being
 *                           drawn
 */
@Override
public final Component getTableCellRendererComponent(
    final JTable table, final Object value,
    final boolean isSelected, final boolean hasFocus,
    final int row, final int column) {
  final JLabel label = new JLabel(value.toString()) {

    /**
     * =_=.
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void paintComponent(final Graphics g) {
      if (row >= 0) {
        chart.getGanttRenderer().paintRow(
            g, chart, row, g.getClipBounds(), true);
        
      }
      super.paintComponent(g);
    }
    
  };
  // paints every pixel within its bounds
  if (row < 0) {
    label.setOpaque(true);
    label.setBackground(timeaxis.getBackground());
    label.setPreferredSize(
        new Dimension(0, timeaxis.getPreferredSize().height));
    label.setBorder(BorderFactory.createCompoundBorder(
        // the color of border of title
        BorderFactory.createMatteBorder(
            0, 1, 1, 0, MagicNumber.BACKGROUND_COLOR_1),
        BorderFactory.createEmptyBorder(0, 2, 0, 0)));

  } else {
    label.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(
            0, 1, 0, 0, MagicNumber.BACKGROUND_COLOR_1),
        BorderFactory.createEmptyBorder(0, 2, 0, 0)));

  }
  return label; 

}

}
