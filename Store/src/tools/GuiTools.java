package tools;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

public class GuiTools
{
	public static void showDialog( JDialog dialog, JDialog parentDialog, JFrame parentFrame )
	{
		if (dialog != null)
		{
			Point p = null;
			int w = 0;
			int h = 0;

			if (parentDialog != null)
			{
				p = parentDialog.getLocation();
				w = parentDialog.getWidth();
				h = parentDialog.getHeight();
			}
			else
				if (parentFrame != null)
				{
					p = parentFrame.getLocation();
					w = parentFrame.getWidth();
					h = parentFrame.getHeight();
				}

			int x = (int) (p.getX()) + ((w - dialog.getWidth()) / 2);
			int y = (int) (p.getY()) + ((h - dialog.getHeight()) / 2);

			dialog.setModal(true);
			dialog.setLocation(x, y);
			dialog.setVisible(true);
		}
	}

	public static void setColumnTitle( JTable table, int column_index, String title )
	{
		if (table != null)
		{
			if (table.getColumnModel() != null)
			{
				if ((0 <= column_index) && (column_index < table.getColumnModel().getColumnCount()))
				{
					TableColumn column = table.getColumnModel().getColumn(column_index);
					if (column != null)
					{
						column.setHeaderValue(title);
						table.getTableHeader().repaint();
					}
				}
			}
		}
	}

	public static void alignColumn( JTable table, int column_index, int alignment )
	{
		if (table != null)
		{
			if ((column_index >= 0) && (column_index < table.getColumnCount()))
			{
				TableColumnModel tcm = table.getColumnModel();
				if (tcm != null)
				{
					TableColumn tc = tcm.getColumn(column_index);
					if (tc != null)
					{
						TableCellRenderer tcr = new DefaultTableCellRenderer();
						if (tcr != null)
						{
							((JLabel) tcr).setHorizontalAlignment(alignment);
							tc.setCellRenderer(tcr);
						}
					}
				}
			}
		}
	}

	public static void alignColumnToCenter( JTable table, int column_index )
	{
		alignColumn(table, column_index, SwingConstants.CENTER);
	}

	public static void alignColumnToRight( JTable table, int column_index )
	{
		alignColumn(table, column_index, SwingConstants.RIGHT);
	}

	public static void alignColumnToLeft( JTable table, int column_index )
	{
		alignColumn(table, column_index, SwingConstants.LEFT);
	}

	public static void setCurrentRow( JTable table, int row )
	{
		if (table != null)
		{
			if (row >= table.getModel().getRowCount())
			{
				row = table.getModel().getRowCount() - 1;
			}
			if (row >= 0)
			{
				table.setRowSelectionInterval(row, row);
			}
			else
			{
				if (table.getModel().getRowCount() > 0)
				{
					table.setRowSelectionInterval(0, 0);
				}
			}
		}
	}

	public static void setColumnWidth( JTable table, int column_index, int column_width )
	{
		if (table != null)
		{
			if (table.getColumnModel() != null)
			{
				if ((0 <= column_index) && (column_index < table.getColumnModel().getColumnCount()))
				{
					TableColumn column = table.getColumnModel().getColumn(column_index);
					if (column != null)
					{
						column.setPreferredWidth(column_width);
					}
				}
			}
		}
	}

	public static int getScrollPaneWidth( JScrollPane sp )
	{
		int result = 0;

		Dimension size = null;
		if (sp != null)
		{
			if (sp.getViewport() != null)
			{
				size = sp.getViewport().getExtentSize();
				if (size != null)
				{
					result = (int) (size.getWidth());

					if (sp.getVerticalScrollBar() != null)
					{
						if (!sp.getVerticalScrollBar().isVisible())
						{
							result = result - (int) (sp.getVerticalScrollBar().getSize().getWidth());
						}
					}
				}
			}
		}

		return result;
	}
}
