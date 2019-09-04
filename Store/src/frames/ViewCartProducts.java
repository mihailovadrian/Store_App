package frames;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import app.AppContext;

import entities.*;
import frames.models.CartProductsModel;
import tools.GridPanel;
import tools.GuiTools;

public class ViewCartProducts extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private AppContext context;

	private CartProducts cartProdFromView;

	private JTable tblCartProduct;
	private CartProductsModel mdlCartProd;
	private JScrollPane tableScrollPane;

	public static CartProducts showCart( JFrame parent, AppContext context, Cart cart )
	{
		CartProducts result = null;
		ViewCartProducts d = new ViewCartProducts(parent, context, cart);
		GuiTools.showDialog(d, null, parent);
		result = d.cartProdFromView;
		return result;
	}

	public ViewCartProducts( JFrame parent, AppContext context, Cart cart )
	{
		super(parent);
		this.context = context;

		setupGUI(context);
	}

	private void setupGUI( AppContext context )
	{
		setTitle("View your cart");

		setSize(new Dimension(700, 500));

		this.context = context;

		mdlCartProd = new CartProductsModel(this.context);
		tblCartProduct = new JTable(mdlCartProd);

		for (int i = 0; i < mdlCartProd.getColumnCount(); i++)
		{
			GuiTools.setColumnTitle(tblCartProduct, i, mdlCartProd.getColumnName(i));
		}

		tableScrollPane = new JScrollPane(tblCartProduct);
		tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		JButton btnCloseCart = new JButton("Finish Command");
		btnCloseCart.addActionListener(this);
		btnCloseCart.setActionCommand("CLOSECART");

		JButton btnDeleteCartProduct = new JButton("Delete");
		btnDeleteCartProduct.addActionListener(this);
		btnDeleteCartProduct.setActionCommand("DELETECARTPRODUCT");

		JButton btnEditQuantity = new JButton("Edit Quantity");
		btnEditQuantity.addActionListener(this);
		btnEditQuantity.setActionCommand("EDITQUANTITY");

		JButton btnExit = new JButton("Close");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("CLOSE");

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 1.0, 1.0, 1, 4, tableScrollPane);

		pan.addObject(1, 0, 0.0, 0.0, 1, 1, btnCloseCart);
		pan.addObject(1, 1, 0.0, 0.0, 1, 1, btnDeleteCartProduct);
		pan.addObject(1, 2, 0.0, 0.0, 1, 1, btnEditQuantity);

		pan.addSizing(1, 3, 0.0, 1.0, 1, 1, 0, 10);
		pan.addSizing(0, 3, 0.0, 1.0, 1, 1, 0, 10);
		pan.addObject(1, 4, 0.0, 0.0, 1, 1, btnExit);
		pan.addSizing(2, 5, 0.0, 0.0, 1, 1, 0, 0);

		setContentPane(pan);

	}

	@Override
	public void actionPerformed( ActionEvent e )
	{

	}

	public void updateTableContent( )
	{

	}
}
