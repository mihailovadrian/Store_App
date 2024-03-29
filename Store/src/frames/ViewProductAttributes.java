package frames;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import app.*;
import dbTools.*;
import entities.*;
import frames.models.*;
import tools.*;

public class ViewProductAttributes extends JDialog implements ActionListener
{
	static final long serialVersionUID = 1L;

	private AppContext context;

	private ProductAttribute value;

	private Product product;

	private ProductAttributesModel mdlProdAttr;
	private AttributesModel mdlAttributes;

	private JTable tblProdAttr;
	private JScrollPane scpProdAttr;

	public static ProductAttribute showProductAttr( JDialog parentDialog, AppContext context, Product prod )
	{
		ProductAttribute result = null;

		ViewProductAttributes d = new ViewProductAttributes(context, parentDialog, prod);

		GuiTools.showDialog(d, parentDialog, null);
		result = d.value;

		return result;
	}

	public ViewProductAttributes( AppContext context, JDialog parent, Product prod )
	{
		super();

		this.context = context;

		value = new ProductAttribute();
		product = prod;
		setup(context);
	}

	private void setup( AppContext context )
	{
		setTitle("View Product Attributes");

		setSize(new Dimension(700, 500));
		setMinimumSize(new Dimension(300, 300));
		this.context = context;

		JButton btnDeleteValue = new JButton("Clear");
		btnDeleteValue.addActionListener(this);
		btnDeleteValue.setActionCommand("DELETEVALUE");

		JButton btnEditValue = new JButton("Edit");
		btnEditValue.addActionListener(this);
		btnEditValue.setActionCommand("EDITVALUE");

		JButton btnExit = new JButton("Close");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("CLOSE");

		mdlAttributes = new AttributesModel();

		mdlProdAttr = new ProductAttributesModel(context, product);
		tblProdAttr = new JTable(mdlProdAttr);
		mdlProdAttr.fireTableDataChanged();

		for (int i = 0; i < mdlProdAttr.getColumnCount(); i++)
		{
			GuiTools.setColumnTitle(tblProdAttr, i, mdlProdAttr.getColumnName(i));
		}

		scpProdAttr = new JScrollPane(tblProdAttr);
		scpProdAttr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpProdAttr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 1.0, 1.0, 1, 3, scpProdAttr);

		pan.addObject(1, 0, 0.0, 0.0, 1, 1, btnEditValue);
		pan.addObject(1, 1, 0.0, 0.0, 1, 1, btnDeleteValue);
		pan.addSizing(1, 2, 0.0, 1.0, 1, 1, 10, 10);

		pan.addObject(1, 3, 0.0, 0.0, 1, 1, btnExit);

		pan.addSizing(2, 4, 0.0, 0.0, 1, 1, 0, 0);

		setContentPane(pan);

		updateTableContentPrAttr(null, 0, true);
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("EDITVALUE"))
		{
			ProductAttribute prodAttr = mdlProdAttr.get(tblProdAttr.getSelectedRow());

			if (prodAttr != null)
			{
				DBOperationsAttribute.listAttributes(context, mdlAttributes.getList(), 0);
				Attribute attr = mdlAttributes.getAttributeById(prodAttr.getAttributeId());
				if (attr != null)
				{
					boolean success = false;

					if (attr.isList())
					{
						AttributeValues attrValue = SelectAttributeValueDialog.select(this, context, attr);

						if (attrValue != null)
						{
							prodAttr.setAttrValId(attrValue.getId());
							prodAttr.setValue(attrValue.getValue());

							if (prodAttr.getId() <= 0)
							{
								if (DBOperationsProductAttributes.addProductAttr(context, prodAttr) > 0)
								{
									success = true;
								}
							}
							else
							{
								if (DBOperationsProductAttributes.editProductAttribute(context, prodAttr))
								{
									success = true;
								}
							}

							if (success)
							{
								updateTableContentPrAttr(prodAttr, 0, true);
								JOptionPane.showMessageDialog(this, "The value of the attribute was updated!", "Information", JOptionPane.INFORMATION_MESSAGE);
							}
						}

					}
					else
					{
						String newValue = JOptionPane.showInputDialog(this, "Enter value", prodAttr.getValue());
						if (newValue != null)
						{
							prodAttr.setAttrValId(0);
							prodAttr.setValue(newValue);

							if (prodAttr.getId() <= 0)
							{
								if (DBOperationsProductAttributes.addProductAttr(context, prodAttr) > 0)
								{
									success = true;
								}
							}
							else
							{
								if (DBOperationsProductAttributes.editProductAttribute(context, prodAttr))
								{
									success = true;
								}
							}

							if (success)
							{
								updateTableContentPrAttr(prodAttr, 0, true);
								JOptionPane.showMessageDialog(this, "The value of the attribute was updated!", "Information", JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}
				}
			}
		}

		if (command.equalsIgnoreCase("DELETEVALUE"))
		{
			int reply = JOptionPane.showConfirmDialog(null, "Do you really want to clear the value of this attribute?", "Warning !", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				int row = tblProdAttr.getSelectedRow();
				if (row >= 0)
				{
					if (DBOperationsProductAttributes.deleteProductAttribute(context, mdlProdAttr.get(row)))
					{
						mdlProdAttr.get(row).setValue(null);
						mdlProdAttr.get(row).setAttrValId(0);

						updateTableContentPrAttr(null, row, true);
					}
					else
					{
						JOptionPane.showMessageDialog(this, "Can t delete the selected item!", "Information", JOptionPane.INFORMATION_MESSAGE);

					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Select the attribute that you want to delete !", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}

		}

		if (command.equalsIgnoreCase("CLOSE"))
		{
			dispose();

		}

	}

	public void updateTableContentPrAttr( ProductAttribute value, int row, boolean refresh )
	{

		mdlProdAttr.sort();
		mdlProdAttr.fireTableDataChanged();

		if (value == null)
		{
			GuiTools.setCurrentRow(tblProdAttr, row);
		}
		else
		{

			GuiTools.setCurrentRow(tblProdAttr, mdlProdAttr.indexOf(value));
		}
	}

}
