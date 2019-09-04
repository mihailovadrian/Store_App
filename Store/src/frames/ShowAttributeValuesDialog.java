package frames;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

import app.AppContext;
import dbTools.DBOperationsAttributeValues;
import entities.*;
import frames.models.*;
import tools.*;

public class ShowAttributeValuesDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private AttributeValues value;
	private AttributeValues valueToUpdate;
	private AppContext context;
	private JTable tblValues;
	private JScrollPane tableScrollPane;
	private AttributeValuesModel mdlValue;
	private Attribute attr;

	public static AttributeValues showValues( JDialog parentDialog, AppContext context, Attribute attrRecevied )
	{
		AttributeValues result = null;

		ShowAttributeValuesDialog d = new ShowAttributeValuesDialog(parentDialog, context, attrRecevied);

		GuiTools.showDialog(d, parentDialog, null);
		result = d.value;
		return result;

	}

	public ShowAttributeValuesDialog( JDialog parent, AppContext context, Attribute attrRecevied )
	{
		super(parent);
		value = new AttributeValues();
		attr = attrRecevied;
		setup(context);
	}

	private void setup( AppContext context )
	{
		setTitle("View Values");

		setSize(new Dimension(300, 300));

		this.context = context;

		JButton btnAddValue = new JButton("Add");
		btnAddValue.addActionListener(this);
		btnAddValue.setActionCommand("ADDVALUE");

		JButton btnDeleteValue = new JButton("Delete");
		btnDeleteValue.addActionListener(this);
		btnDeleteValue.setActionCommand("DELETEVALUE");

		JButton btnEditValue = new JButton("Edit");
		btnEditValue.addActionListener(this);
		btnEditValue.setActionCommand("EDITVALUE");

		JButton btnExit = new JButton("Close");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("CLOSE");

		mdlValue = new AttributeValuesModel();

		tblValues = new JTable(mdlValue);

		for (int i = 0; i < mdlValue.getColumnCount(); i++)
		{
			GuiTools.setColumnTitle(tblValues, i, mdlValue.getColumnName(i));
		}

		tableScrollPane = new JScrollPane(tblValues);
		tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 1.0, 1.0, 1, 5, tableScrollPane);

		pan.addObject(1, 1, 0.0, 0.0, 1, 1, btnAddValue);
		pan.addObject(1, 2, 0.0, 0.0, 1, 1, btnEditValue);
		pan.addObject(1, 3, 0.0, 0.0, 1, 1, btnDeleteValue);

		pan.addSizing(1, 4, 0.0, 1.0, 1, 1, 10, 10);

		pan.addObject(1, 5, 0.0, 0.0, 1, 1, btnExit);
		pan.addSizing(2, 6, 0.0, 0.0, 1, 1, 0, 0);

		setContentPane(pan);
		updateTableContent(null, 0, true);
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();
		if (command.equalsIgnoreCase("ADDVALUE"))
		{
			AttributeValues val = EditAttributeValuesDialog.addAttributeValueDialog(this, context, attr);
			if (val != null)
			{
				val = DBOperationsAttributeValues.getValue(context, val.getId());
				updateTableContent(val, 0, true);

				JOptionPane.showMessageDialog(this, "The value  '" + val.getValue() + "' was added!", "Information", JOptionPane.INFORMATION_MESSAGE);

			}
		}
		if (command.equalsIgnoreCase("DELETEVALUE"))
		{
			int reply = JOptionPane.showConfirmDialog(null, "Do you really want to delete this attribute?", "Warning !", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				int row = tblValues.getSelectedRow();
				if (row >= 0)
				{
					DBOperationsAttributeValues.deleteValue(context, mdlValue.get(row));
					updateTableContent(null, row, true);
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Select the value that you want to delete !", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}

		if (command.equalsIgnoreCase("EDITVALUE"))
		{
			valueToUpdate = null;
			if (tblValues.getSelectedRow() >= 0)
			{
				valueToUpdate = mdlValue.get(tblValues.getSelectedRow());

				if (valueToUpdate != null)
				{
					AttributeValues val = EditAttributeValuesDialog.editValue(this, context, valueToUpdate, attr);
					if (val != null)
					{
						updateTableContent(val, 0, true);
						JOptionPane.showMessageDialog(this, "The value '" + valueToUpdate.getValue() + "' was updated!", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Cannot edit the selected value !", "Warining", JOptionPane.WARNING_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Select the value that you want to update !", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		if (command.equalsIgnoreCase("CLOSE"))
		{
			dispose();
		}

	}

	public void updateTableContent( AttributeValues value, int row, boolean refresh )
	{

		if (refresh)
		{
			DBOperationsAttributeValues.listValues(context, mdlValue.getList(), attr.getId_Attribute());
		}

		mdlValue.sort();
		mdlValue.fireTableDataChanged();

		if (value == null)
		{
			GuiTools.setCurrentRow(tblValues, row);
		}
		else
		{

			GuiTools.setCurrentRow(tblValues, mdlValue.indexOf(value));
		}
	}

}
