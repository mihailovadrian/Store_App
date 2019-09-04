package tools;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Map.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import application.*;

public class GUITools
{
	// ~ Static fields/initializers
	// -----------------------------------------------------------------

	public static final int ALIGN_TO_LEFT = SwingConstants.LEFT;
	public static final int ALIGN_TO_RIGHT = SwingConstants.RIGHT;
	public static final int ALIGN_TO_CENTER = SwingConstants.CENTER;

	// ~ Methods
	// ------------------------------------------------------------------------------------

	public static void ensureMinimumSize( JDialog dialog, int width, int height )
	{
		if (dialog != null)
		{
			if ((width > 0) && (height > 0))
			{
				int newWidth = dialog.getWidth();
				int newHeight = dialog.getHeight();

				if (newWidth < width)
				{
					newWidth = width;
				}
				if (newHeight < height)
				{
					newHeight = height;
				}
				if ((newWidth != dialog.getWidth()) || (newHeight != dialog.getHeight()))
				{
					dialog.setSize(new Dimension(newWidth, newHeight));
				}
			}
		}
	}

	public static void ensureMinimumSize( JDialog dialog )
	{
		if (dialog != null)
		{
			Dimension size = dialog.getMinimumSize();

			ensureMinimumSize(dialog, (int) size.getWidth(), (int) size.getHeight());
		}
	}

	public static JPanel sizingPanel( int width, int height )
	{

		JPanel result = new JPanel();

		result.setBorder(new EmptyBorder(height / 2, width / 2, height / 2, width / 2));

		GridBagLayout gbl = new GridBagLayout();
		result.setLayout(gbl);

		return result;
	}

	public static JPanel createClosePanel( ActionListener dialog )
	{
		return createBottomPanelL(null, createButton(dialog, "Close", AppConstants.CANCEL_COMMAND));
	}

	public static JPanel createOkCancelPanel( ActionListener dialog )
	{
		return createBottomPanelL(null, createButton(dialog, "Ok", AppConstants.OK_COMMAND), createButton(dialog, "Cancel", AppConstants.CANCEL_COMMAND));
	}

	public static JPanel createBottomPanel( JButton button )
	{
		return createBottomPanelL(null, button);
	}

	public static JPanel createBottomPanelL( JComponent label, JButton button )
	{
		GridPanel panel = new GridPanel(0, 0, 0, 0);

		panel.addSizing(0, 0, 1.0, 0.0, 1, 1, 10, 0);
		panel.addSizing(1, 0, 0.0, 0.0, 1, 1, 10, 0);
		panel.addSizing(2, 0, 0.0, 0.0, 1, 1, 80, 0);

		if (label != null)
		{
			panel.addObject(0, 1, 1.0, 0.0, 1, 1, label);
		}

		panel.addSizing(1, 1, 0.0, 0.0, 1, 1, 10, 30);
		panel.addObject(2, 1, 0.0, 0.0, 1, 1, button);

		return panel;
	}

	public static JPanel createBottomPanel( JButton button1, JButton button2 )
	{
		return createBottomPanelL(null, button1, button2);
	}

	public static JPanel createBottomPanelL( JComponent label, JButton button1, JButton button2 )
	{
		GridPanel panel = new GridPanel(0, 0, 0, 0);

		panel.addSizing(1, 0, 0.0, 0.0, 1, 1, 80, 0);
		panel.addSizing(3, 0, 0.0, 0.0, 1, 1, 80, 0);

		if (label != null)
		{
			panel.addObject(0, 1, 1.0, 0.0, 1, 1, label);
		}
		else
		{
			panel.addSizing(0, 1, 1.0, 0.0, 1, 1, 10, 0);
		}

		panel.addObject(1, 1, 0.0, 0.0, 1, 1, button1);
		panel.addSizing(2, 1, 0.0, 0.0, 1, 1, 5, 30);
		panel.addObject(3, 1, 0.0, 0.0, 1, 1, button2);

		return panel;
	}

	public static JPanel horizBevel( )
	{

		JPanel result = new JPanel();

		result.setBorder(new LineBorder(Color.lightGray, 1));

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 0, 0, 0);

		result.setLayout(gbl);

		return result;
	}

	public static JLabel thinLabel( String text )
	{

		JLabel result = new JLabel(text);

		Font f = result.getFont().deriveFont(Font.PLAIN);
		result.setFont(f);

		return result;
	}

	public static void showLoginSettingsDialog( JDialog dialog )
	{
		if (dialog != null)
		{

			Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

			// calculate the position of the top left corner
			int x = (screen_size.width - dialog.getWidth()) / 2;
			int y = (screen_size.height - dialog.getHeight()) / 2;

			dialog.setModal(true);
			dialog.setLocation(x, y);
			dialog.setVisible(true);
		}
	}

	public static void showLoginDialog( JDialog dialog, JDialog parentDialog, JFrame parentFrame )
	{
		if (dialog != null)
		{

			Point p = null;
			int w = 0;
			int h = 0;

			int x = 0;
			int y = 0;

			if (parentDialog != null)
			{
				p = parentDialog.getLocation();
				w = parentDialog.getWidth();
				h = parentDialog.getHeight();

				x = (int) (p.getX()) + (w - dialog.getWidth()) + 20;
				y = (int) (p.getY() + h) + 5;
			}
			else
				if (parentFrame != null)
				{
					p = parentFrame.getLocation();
					w = parentFrame.getWidth();
					h = parentFrame.getHeight();

					x = (int) (p.getX()) + ((w - dialog.getWidth()) / 2);
					y = (int) (p.getY()) + ((h - dialog.getHeight()) / 2);
				}

			dialog.setModal(true);
			dialog.setLocation(x, y);
			dialog.setVisible(true);
		}
	}

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

	public static void showWindow( JWindow window, JDialog parentDialog, JFrame parentFrame )
	{
		if (window != null)
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

			int x = (int) (p.getX()) + ((w - window.getWidth()) / 2);
			int y = (int) (p.getY()) + ((h - window.getHeight()) / 2);

			window.setLocation(x, y);
			window.setVisible(true);
		}
	}

	public static JButton createButton( ActionListener dialog, String text, String actionCommand )
	{
		JButton result = new JButton(text);
		result.addActionListener(dialog);
		result.setActionCommand(actionCommand);

		return result;
	}

	public static JMenuItem createMenuItem( ActionListener listener, String text, int key, String command )
	{

		JMenuItem result = new JMenuItem(text, key);

		result.setMnemonic(key);
		if (key != 0)
		{
			result.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.CTRL_MASK));
		}
		result.addActionListener(listener);
		result.setActionCommand(command);

		return result;
	}

	public static JMenuItem createMenuItem( ActionListener listener, String text, int key, String command, boolean with_acc )
	{

		JMenuItem result = new JMenuItem(text, key);

		result.setMnemonic(key);
		if (with_acc)
		{
			result.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.CTRL_MASK));
		}
		result.addActionListener(listener);
		if (command != null)
		{
			result.setActionCommand(command);
		}

		return result;
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

	public static void ensureRowIsVisible( JTable table, int row )
	{
		if (table != null)
		{
			Rectangle rect = table.getCellRect(row, 0, true);

			table.scrollRectToVisible(rect);
		}
	}

	public static void ensureCellIsVisible( JTable table, int row, int col )
	{
		if (table != null)
		{
			Rectangle rect = table.getCellRect(row, col, true);

			table.scrollRectToVisible(rect);
		}
	}

	public static int resizeColumn( JTable table, int column_index, int column_width, int table_witdh )
	{
		int result = 0;

		if (table != null)
		{
			if (table.getColumnModel() != null)
			{
				TableColumn column = table.getColumnModel().getColumn(column_index);
				if (column != null)
				{
					column.setPreferredWidth(column_width);
					result = table_witdh - column_width;
				}
			}
		}

		return result;
	}

	public static void resizeColumn( JTable table, int column_index, int column_width )
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

	public static void setColumnMinWidth( JTable table, int column_index, int column_min_width )
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
						column.setMinWidth(column_min_width);
					}
				}
			}
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

	public static void setCurrentRow( JTable table, AbstractTableModel model, int index )
	{
		if ((table != null) && (model != null))
		{
			if (index >= model.getRowCount())
			{
				index = model.getRowCount() - 1;
			}
			if (index < 0)
			{
				index = 0;
			}
			if ((0 <= index) && (index < model.getRowCount()))
			{
				table.changeSelection(index, 0, false, false);
				table.scrollRectToVisible(table.getCellRect(index, 0, true));
			}
		}
	}

	public static void setLabelText( JLabel label, String text )
	{
		if (label != null)
		{
			label.setText(text);
			label.repaint();
		}
	}

	public static void setProgress( JProgressBar progress, int min, int max, int value )
	{
		if (progress != null)
		{
			if (min >= 0)
			{
				progress.setMinimum(min);
			}
			if (max >= 0)
			{
				progress.setMaximum(max);
			}
			if (value >= 0)
			{
				progress.setValue(value);
			}
			progress.repaint();
		}
	}

	public static void enumSystemColors( )
	{
		ArrayList<String> colorKeys = new ArrayList<String>();
		Set<Entry<Object, Object>> entries = UIManager.getLookAndFeelDefaults().entrySet();
		for (Entry<Object, Object> entry : entries)
		{
			if (entry.getValue() instanceof Color)
			{
				colorKeys.add((String) entry.getKey());
			}
		}

		Collections.sort(colorKeys);

		for (String colorKey : colorKeys)
		{
			System.out.println(colorKey);
		}
	}
}
