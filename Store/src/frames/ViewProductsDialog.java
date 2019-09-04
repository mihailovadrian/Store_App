package frames;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import app.AppContext;
import dbTools.DBOperationsCategories;
import dbTools.DBOperationsProduct;
import entities.*;
import frames.models.CategoriesModel;
import frames.models.ProductsModel;
import tools.GridPanel;
import tools.GuiTools;

public class ViewProductsDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Product pr;
	private Product prToUpdate;

	private AppContext context;
	private JTable tblProducts;
	private JTable tblCategories;
	private JTextField txtSearchProduct;
	private JScrollPane scpProducts;
	private JScrollPane scpCategories;
	private JLabel lblRecords;
	private ProductsModel mdlProduct;
	private CategoriesModel mdlCategories;

	public static Product showProducts( JFrame parentFrame, AppContext context )
	{
		Product result = null;

		ViewProductsDialog d = new ViewProductsDialog(parentFrame, context);

		GuiTools.showDialog(d, null, parentFrame);
		result = d.pr;
		return result;

	}

	ViewProductsDialog( JFrame parentFrame, AppContext context )
	{
		super(parentFrame);
		pr = new Product();

		setup(context);
	}

	private void setup( AppContext context )
	{
		setTitle("View Products");

		setSize(new Dimension(700, 500));
		setMinimumSize(new Dimension(300, 300));
		this.context = context;

		txtSearchProduct = new JTextField();

		lblRecords = new JLabel("Records :");

		JButton btnAddProduct = new JButton("Add");
		btnAddProduct.addActionListener(this);
		btnAddProduct.setActionCommand("ADDPRODUCT");

		JButton btnDeleteProduct = new JButton("Delete");
		btnDeleteProduct.addActionListener(this);
		btnDeleteProduct.setActionCommand("DELETEPRODUCT");

		JButton btnEditProduct = new JButton("Edit");
		btnEditProduct.addActionListener(this);
		btnEditProduct.setActionCommand("EDITPRODUCT");

		JButton btnSearchProduct = new JButton("Search");
		btnSearchProduct.addActionListener(this);
		btnSearchProduct.setActionCommand("APPLY_FILTER");

		JButton btnAddProductValue = new JButton("Add Values");
		btnAddProductValue.addActionListener(this);
		btnAddProductValue.setActionCommand("ADDVALUE");

		JButton btnExit = new JButton("Close");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("CLOSE");

		mdlProduct = new ProductsModel();
		tblProducts = new JTable(mdlProduct);

		mdlCategories = new CategoriesModel(true);
		tblCategories = new JTable(mdlCategories);

		tblProducts.getTableHeader().addMouseListener(new MouseAdapter()
		{
			public void mouseClicked( MouseEvent e )
			{
				int columnIndex = tblProducts.columnAtPoint(e.getPoint());

				Product selectedProduct = mdlProduct.get(tblProducts.getSelectedRow());

				mdlProduct.setSortColumnIndex(columnIndex);

				updateTableContent(selectedProduct, 0, false, 0);
			}
		});
		tblCategories.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent event )
			{
				Categories cat = mdlCategories.get(tblCategories.getSelectedRow());
				int idCategory = cat.getId();
				updateTableContent(null, 0, true, idCategory);
			}
		});

		for (int i = 0; i < mdlProduct.getColumnCount(); i++)
		{
			GuiTools.setColumnTitle(tblProducts, i, mdlProduct.getColumnName(i));
		}

		scpProducts = new JScrollPane(tblProducts);
		scpProducts.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpProducts.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		scpCategories = new JScrollPane(tblCategories);
		scpCategories.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpCategories.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scpCategories.setPreferredSize(new Dimension(120, 200));

		GridPanel pnlFilter = new GridPanel(0, 0, 0, 0);

		pnlFilter.addObject(0, 0, 0.0, 0.0, 1, 1, new JLabel("Filter"));
		pnlFilter.addSizing(1, 0, 0.0, 0.0, 1, 1, 5, 0);
		pnlFilter.addObject(2, 0, 0.0, 0.0, 1, 1, txtSearchProduct);
		pnlFilter.addSizing(2, 1, 0.0, 0.0, 1, 1, 200, 0);
		pnlFilter.addSizing(3, 0, 1.0, 0.0, 1, 1, 10, 0);

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 1.0, 0.0, 2, 1, pnlFilter);
		pan.addObject(2, 0, 0.0, 0.0, 1, 1, btnSearchProduct);

		pan.addObject(0, 1, 0.0, 1.0, 1, 6, scpCategories);
		pan.addObject(1, 1, 1.0, 1.0, 1, 6, scpProducts);

		pan.addObject(2, 1, 0.0, 0.0, 1, 1, btnAddProduct);
		pan.addObject(2, 2, 0.0, 0.0, 1, 1, btnEditProduct);
		pan.addObject(2, 3, 0.0, 0.0, 1, 1, btnDeleteProduct);
		pan.addSizing(2, 4, 0.0, 0.0, 1, 1, 10, 50);
		pan.addObject(2, 5, 0.0, 0.0, 1, 1, btnAddProductValue);
		pan.addSizing(2, 6, 0.0, 1.0, 1, 1, 10, 10);

		pan.addSizing(0, 7, 0.0, 0.0, 1, 1, 120, 10);
		pan.addSizing(1, 7, 1.0, 0.0, 1, 1, 10, 10);
		pan.addObject(2, 7, 0.0, 0.0, 1, 1, btnExit);
		pan.addSizing(3, 8, 0.0, 0.0, 1, 1, 0, 0);

		setContentPane(pan);

		updateTableContentCategories(null, 0, true);

	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("ADDPRODUCT"))
		{
			Product pr = EditProductDialog.addProductDialog(this, context);
			if (pr != null)
			{
				pr = DBOperationsProduct.getProduct(context, pr.getId());
				Categories cat = mdlCategories.get(tblCategories.getSelectedRow());
				GuiTools.setCurrentRow(tblCategories, mdlCategories.indexOf(cat));

				updateTableContent(pr, 0, true, cat.getId());

				JOptionPane.showMessageDialog(this, "The Product '" + pr.getName() + "' was added!", "Information", JOptionPane.INFORMATION_MESSAGE);

			}

		}

		if (command.equalsIgnoreCase("DELETEPRODUCT"))
		{
			int reply = JOptionPane.showConfirmDialog(null, "Do you really want to delete this product?", "Warning !", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				int row = tblProducts.getSelectedRow();
				if (row >= 0)
				{
					DBOperationsProduct.deleteProduct(context, mdlProduct.get(row));
					updateTableContent(null, row, true, 0);
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Select the product that you want to delete !", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}

		if (command.equalsIgnoreCase("EDITPRODUCT"))
		{
			prToUpdate = null;
			if (tblProducts.getSelectedRow() >= 0)
			{
				prToUpdate = mdlProduct.get(tblProducts.getSelectedRow());
				if (prToUpdate != null)
				{
					Product product = EditProductDialog.editProductDialog(this, context, prToUpdate);

					if (product != null)
					{
						Categories cat = mdlCategories.get(tblCategories.getSelectedRow());
						updateTableContent(product, 0, true, cat.getId());
						JOptionPane.showMessageDialog(this, "The product '" + product.getName() + "' was updated!", "Information", JOptionPane.INFORMATION_MESSAGE);
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

		if (command.equalsIgnoreCase("APPLY_FILTER"))
		{
			if (tblCategories.getSelectedRow() >= 0)
			{
				Categories cat = mdlCategories.get(tblCategories.getSelectedRow());
				int idCategory = cat.getId();
				updateTableContent(null, 0, true, idCategory);
				return;
			}
			updateTableContent(null, 0, true, 0);
		}
		if (command.equalsIgnoreCase("ADDVALUE"))
		{
			if (tblProducts.getSelectedRow() >= 0)
			{
				Product pr = mdlProduct.get(tblProducts.getSelectedRow());

				ViewProductAttributes.showProductAttr(this, context, pr);
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Select the Product first !", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		if (command.equalsIgnoreCase("close"))
		{
			dispose();
		}
	}

	public void updateTableContent( Product pr, int row, boolean refresh, int idCategory )
	{

		if (refresh)
		{
			DBOperationsProduct.listProducts(context, mdlProduct.getList(), txtSearchProduct.getText(), idCategory);
		}

		mdlProduct.sort();
		mdlProduct.fireTableDataChanged();

		lblRecords.setText("Records: " + String.valueOf(mdlProduct.getRowCount()));

		if (pr == null)
		{
			GuiTools.setCurrentRow(tblProducts, row);
		}
		else
		{
			GuiTools.setCurrentRow(tblProducts, mdlProduct.indexOf(pr));
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
