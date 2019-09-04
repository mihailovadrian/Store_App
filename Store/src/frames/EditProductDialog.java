package frames;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import app.AppContext;
import dbTools.DBOperationsCategories;
import dbTools.DBOperationsProduct;
import entities.*;
import frames.models.CategoriesModel;
import tools.GridPanel;
import tools.GuiTools;

public class EditProductDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private boolean success;
	private AppContext context;

	private Product prod;
	private Product prodToUpdate;

	private JLabel lblName;
	private JLabel lblDescription;
	private JLabel lblQuantity;

	private JTable TblCategories;
	private JScrollPane tableScrollPane;
	private CategoriesModel mdlCategories;

	private JTextField txtName;
	private JTextArea txtDescription;
	private JTextField txtQuantity;

	public static Product addProductDialog( JDialog parentFrame, AppContext context )
	{
		Product result = null;

		EditProductDialog d = new EditProductDialog(context, parentFrame, null);

		GuiTools.showDialog(d, parentFrame, null);

		if (d.success)
		{

			result = d.prod;

		}

		return result;
	}

	public static Product editProductDialog( JDialog parentDialog, AppContext context, Product prodToUpdate )
	{
		Product result = null;

		EditProductDialog d = new EditProductDialog(context, parentDialog, prodToUpdate);

		GuiTools.showDialog(d, parentDialog, null);

		if (d.success)
		{
			result = d.prod;
		}

		return result;
	}

	public EditProductDialog( AppContext context, JDialog parentFrame, Product prodToUpdate )
	{
		super(parentFrame);
		success = false;
		this.prod = null;
		this.prodToUpdate = prodToUpdate;

		setupGUI(context);
	}

	private void setupGUI( AppContext context )
	{
		setSize(new Dimension(400, 350));
		setMinimumSize(new Dimension(300, 300));
		this.context = context;
		if (prodToUpdate == null)
		{
			setTitle("Add Product");
		}
		else
		{
			setTitle("Edit Product");
		}

		lblName = new JLabel("Name");
		lblDescription = new JLabel("Description");
		lblQuantity = new JLabel("Quantity");

		txtName = new JTextField();
		txtDescription = new JTextArea();
		txtQuantity = new JTextField();

		mdlCategories = new CategoriesModel(true);
		TblCategories = new JTable(mdlCategories);

		if (prodToUpdate != null)
		{
			txtName.setText(prodToUpdate.getName());
			txtDescription.setText(prodToUpdate.getDescription());
			txtQuantity.setText(String.valueOf(prodToUpdate.getQuantity()));

		}
		JButton btnAddProduct = new JButton(prodToUpdate != null ? "Update" : "Add");
		btnAddProduct.addActionListener(this);
		btnAddProduct.setActionCommand("SAVE");

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(this);
		btnClose.setActionCommand("CLOSE");

		tableScrollPane = new JScrollPane(TblCategories);
		tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 0.0, 1.0, 2, 7, tableScrollPane);
		pan.addSizing(1, 0, 1.0, 0.0, 1, 6, 10, 0);
		pan.addObject(2, 0, 1.0, 0.0, 1, 1, lblName);
		pan.addObject(2, 1, 1.0, 0.0, 1, 1, txtName);

		pan.addObject(2, 2, 1.0, 0.0, 1, 1, lblDescription);

		pan.addObject(2, 3, 1.0, 1.0, 1, 1, new JScrollPane(txtDescription));
		//pan.addSizing(2, 4, 1.0, 0.0, 1, 1, 10, 0);
		pan.addObject(2, 5, 1.0, 0.0, 1, 1, lblQuantity);
		pan.addObject(2, 6, 1.0, 0.0, 1, 1, txtQuantity);

		// Buttons GridPanel

		GridPanel panButtons = new GridPanel(5, 3, 0, 0);

		panButtons.addSizing(0, 0, 1.0, 0.0, 1, 1, 10, 0);
		panButtons.addObject(1, 0, 0.0, 0.0, 1, 1, btnAddProduct);
		panButtons.addObject(2, 0, 0.0, 0.0, 1, 1, btnClose);

		pan.addSizing(0, 7, 0.0, 1.0, 1, 1, 10, 0);
		pan.addObject(2, 8, 1.0, 0.0, 1, 1, panButtons);
		pan.addSizing(3, 9, 0, 0, 1, 1, 0, 0);

		setContentPane(pan);
		updateTableContent(null, 0, true);

	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("SAVE"))
		{
			if (TblCategories.getSelectedRow() >= 0)
			{
				Product productToSave = new Product();
				Categories selectedCat = mdlCategories.get(TblCategories.getSelectedRow());
				productToSave.setId_Category(selectedCat.getId());
				productToSave.setName(txtName.getText());
				productToSave.setDescription(txtDescription.getText());

				try
				{
					productToSave.setQuantity(Integer.parseInt(txtQuantity.getText()));
				}
				catch (NumberFormatException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();

				}

				if (prodToUpdate == null)
				{

					int id = DBOperationsProduct.addProduct(context, productToSave);
					if (id > 0)
					{
						prod = DBOperationsProduct.getProduct(context, id);
						success = true;
						dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(this, "The product couldn't be added!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					productToSave.setId(prodToUpdate.getId());

					if (DBOperationsProduct.editProduct(context, productToSave))
					{

						prod = DBOperationsProduct.getProduct(context, prodToUpdate.getId());
						success = true;
						dispose();
					}
					else
					{
						String msg = "The Product couldn't be added!";
						if (productToSave != null)
						{
							msg = "The Product data couldn't be updated!";
						}

						JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Select a category first", "Warning", JOptionPane.WARNING_MESSAGE);

			}
		}
		if (command.equalsIgnoreCase("CLOSE"))
		{
			dispose();
		}
	}

	public void updateTableContent( Categories cat, int row, boolean refresh )
	{

		if (refresh)
		{
			DBOperationsCategories.listCategories(context, mdlCategories.getList(), "");
		}

		mdlCategories.sort();
		mdlCategories.fireTableDataChanged();
		if (cat == null)
		{
			GuiTools.setCurrentRow(TblCategories, row);
		}
		else
		{
			GuiTools.setCurrentRow(TblCategories, mdlCategories.indexOf(cat));
		}
	}

}
