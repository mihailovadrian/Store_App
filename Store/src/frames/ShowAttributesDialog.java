package frames;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import app.AppContext;
import dbTools.DBOperationsAttribute;
import dbTools.DBOperationsCategories;
import entities.Attribute;
import entities.Categories;
import frames.models.*;
import tools.*;

public class ShowAttributesDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private AppContext context;

	private JTable tblAttributes;
	private JTable tblCategories;

	private JScrollPane scpAttributes;
	private JScrollPane scpCategories;

	private AttributesModel mdlAttributes;
	private CategoriesModel mdlCategories;

	private Attribute attrToSend;

	private Attribute attr;
	private Attribute attrToUpdate;

	public static Attribute showAttributeCategories( JFrame parentFrame, AppContext context )
	{
		Attribute result = null;

		ShowAttributesDialog d = new ShowAttributesDialog(parentFrame, context);

		GuiTools.showDialog(d, null, parentFrame);
		result = d.attr;
		return result;

	}

	ShowAttributesDialog( JFrame parentFrame, AppContext context )
	{
		super(parentFrame);
		attr = new Attribute();
		attrToSend = null;
		setup(context);
	}

	private void setup( AppContext context )
	{
		setTitle("View Attributes of Categories");

		setSize(new Dimension(700, 500));

		this.context = context;

		JButton btnAddAttribute = new JButton("Add");
		btnAddAttribute.addActionListener(this);
		btnAddAttribute.setActionCommand("ADDATTR");

		JButton btnDeleteAttribute = new JButton("Delete");
		btnDeleteAttribute.addActionListener(this);
		btnDeleteAttribute.setActionCommand("DELETEATTR");

		JButton btnEditAttribute = new JButton("Edit");
		btnEditAttribute.addActionListener(this);
		btnEditAttribute.setActionCommand("EDITATTR");

		JButton btnValue = new JButton("Show Values");
		btnValue.addActionListener(this);
		btnValue.setActionCommand("VALUE");
		btnValue.setEnabled(false);

		JButton btnExit = new JButton("Cancel");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("CLOSE");

		mdlAttributes = new AttributesModel();
		mdlCategories = new CategoriesModel(true);

		tblAttributes = new JTable(mdlAttributes);
		tblCategories = new JTable(mdlCategories);

		tblAttributes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		tblAttributes.getTableHeader().addMouseListener(new MouseAdapter()
		{
			public void mouseClicked( MouseEvent e )
			{
				int columnIndex = tblAttributes.columnAtPoint(e.getPoint());

				Attribute selectedAttr = mdlAttributes.get(tblAttributes.getSelectedRow());

				mdlAttributes.setSortColumnIndex(columnIndex);

				updateTableContentAttributes(selectedAttr, 0, false, 0);
			}
		});
		tblCategories.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent event )
			{
				Categories cat = mdlCategories.get(tblCategories.getSelectedRow());
				int idCategory = cat.getId();
				updateTableContentAttributes(null, 0, true, idCategory);

			}
		});
		//EXISTA DE DOUA ORI
		tblAttributes.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent event )
			{
				Attribute atr = mdlAttributes.get(tblAttributes.getSelectedRow());
				if (atr != null)
				{
					boolean islist = atr.isList();
					if (islist)
					{
						btnValue.setEnabled(true);
						attrToSend = atr;
					}
					else
						btnValue.setEnabled(false);
				}
			}
		});

		for (int i = 0; i < mdlAttributes.getColumnCount(); i++)
		{
			GuiTools.setColumnTitle(tblAttributes, i, mdlAttributes.getColumnName(i));
		}

		scpAttributes = new JScrollPane(tblAttributes);
		scpAttributes.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpAttributes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scpCategories = new JScrollPane(tblCategories);
		scpCategories.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpCategories.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		GridPanel pan = new GridPanel(5, 5, 0, 0);
		pan.addObject(0, 0, 0.0, 1.0, 1, 5, scpCategories);

		pan.addObject(1, 0, 1.0, 1.0, 1, 5, scpAttributes);

		pan.addObject(2, 0, 0.0, 0.0, 1, 1, btnAddAttribute);
		pan.addObject(2, 1, 0.0, 0.0, 1, 1, btnEditAttribute);
		pan.addObject(2, 2, 0.0, 0.0, 1, 1, btnDeleteAttribute);
		pan.addObject(2, 3, 0.0, 0.0, 1, 1, btnValue);

		pan.addSizing(2, 4, 0.0, 1.0, 1, 1, 10, 10);

		pan.addSizing(0, 5, 0.0, 0.0, 1, 1, 120, 1);
		pan.addObject(2, 5, 0.0, 0.0, 1, 1, btnExit);
		pan.addSizing(3, 6, 0.0, 0.0, 1, 1, 0, 0);

		setContentPane(pan);

		updateTableContentAttributes(null, 0, true, 1);
		updateTableContentCategories(null, 0, true);

		GuiTools.alignColumnToCenter(tblAttributes, 3);
		GuiTools.alignColumnToCenter(tblAttributes, 4);

		addComponentListener(new ComponentAdapter()
		{
			public void componentResized( ComponentEvent e )
			{
				resizeDialog();
			}
		});
	}

	private void resizeDialog( )
	{
		GuiTools.setColumnWidth(tblAttributes, 2, 100);
		GuiTools.setColumnWidth(tblAttributes, 3, 30);
		GuiTools.setColumnWidth(tblAttributes, 4, 70);

		int w = GuiTools.getScrollPaneWidth(scpAttributes);
		w = w - 100 - 30 - 70;

		GuiTools.setColumnWidth(tblAttributes, 0, w / 2);
		GuiTools.setColumnWidth(tblAttributes, 1, w / 2);
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("ADDATTR"))
		{
			Attribute atr = EditAttributeDialog.addAttributesDialog(this, context);
			if (atr != null)
			{
				atr = DBOperationsAttribute.getAttribute(context, atr.getId_Attribute());
				Categories cat = mdlCategories.get(tblCategories.getSelectedRow());
				GuiTools.setCurrentRow(tblCategories, mdlCategories.indexOf(cat));
				updateTableContentAttributes(atr, 0, true, cat.getId());

				JOptionPane.showMessageDialog(this, "The Attribute '" + atr.getName() + "' was added!", "Information", JOptionPane.INFORMATION_MESSAGE);

			}

		}
		if (command.equalsIgnoreCase("DELETEATTR"))
		{
			int reply = JOptionPane.showConfirmDialog(null, "Do you really want to delete this attribute?", "Warning !", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				int row = tblAttributes.getSelectedRow();
				if (row >= 0)
				{
					DBOperationsAttribute.deleteAttribute(context, mdlAttributes.get(row));
					updateTableContentAttributes(null, row, true, 0);
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Select the attribute that you want to delete !", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		if (command.equalsIgnoreCase("EDITATTR"))
		{
			attrToUpdate = null;
			if (tblAttributes.getSelectedRow() >= 0)
			{
				attrToUpdate = mdlAttributes.get(tblAttributes.getSelectedRow());

				if (attrToUpdate != null)
				{
					Attribute attribute = EditAttributeDialog.editAttributes(this, context, attrToUpdate);
					if (attribute != null)
					{
						Categories cat = mdlCategories.get(tblCategories.getSelectedRow());
						GuiTools.setCurrentRow(tblCategories, mdlCategories.indexOf(cat));
						updateTableContentAttributes(attribute, 0, true, cat.getId());
						JOptionPane.showMessageDialog(this, "The attribute '" + attrToUpdate.getName() + "' was updated!", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Cannot edit the selected user !", "Warining", JOptionPane.WARNING_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Select the user that you want to update !", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		if (command.equalsIgnoreCase("close"))
		{
			dispose();
		}
		if (command.equalsIgnoreCase("value"))
		{
			ShowAttributeValuesDialog.showValues(this, context, attrToSend);

		}
	}

	public void updateTableContentAttributes( Attribute attr, int row, boolean refresh, int SelectedCategory )
	{
		if (refresh)
		{
			DBOperationsAttribute.listAttributes(context, mdlAttributes.getList(), SelectedCategory);
		}

		mdlAttributes.sort();
		mdlAttributes.fireTableDataChanged();

		if (attr == null)
		{
			GuiTools.setCurrentRow(tblAttributes, row);
		}
		else
		{
			GuiTools.setCurrentRow(tblAttributes, mdlAttributes.indexOf(attr));
		}
	}

	public void updateTableContentCategories( Categories cat, int row, boolean refresh )
	{
		if (refresh)
		{
			DBOperationsCategories.listCategories(context, mdlCategories.getList(), "");
		}

		mdlCategories.sort();
		mdlCategories.fireTableDataChanged();

	}
}
