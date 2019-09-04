package frames;

import java.awt.event.*;

import javax.swing.*;

import app.*;
import dbTools.DBOpeationsCart;
import dbTools.DBOperationsCartProduct;
import dbTools.DBOperationsProduct;
import entities.*;
import tools.*;
import frames.models.*;

public class MainFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 8544513875314876016L;

	private AppContext context;
	private JMenu AppMenu, AdminMenu, CartMenu;
	private JMenuItem Login, Logout, Exit, Categories, Attributes, Products, ViewCartItem, ViewReg, AddUser, ViewUsers;
	private JMenuBar Menu;

	private JTable tblProducts;
	private ProductToBuyModel mdlProduct;
	private JButton btnAddProduct;

	private JScrollPane scpProducts;

	private CartProducts cartProduct;
	private Carts cart;
	private CartsModel mdlCartModel;

	public MainFrame()
	{
		super();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		setTitle("Store");

		context = new AppContext(ConnectionTools.openConnection("192.168.200.112:1433;instanceName=meseriashu", "Adrian", "sa", "Meseriashu10"));
		cart = new Carts();
		createMenus();
		setupGuiForCurrentUser();
	}

	public void createMenus( )
	{
		Menu = new JMenuBar();

		AppMenu = new JMenu("Application");
		AdminMenu = new JMenu("Entities");
		CartMenu = new JMenu("Carts");

		Login = new JMenuItem("LogIn");
		Login.setActionCommand("LOGIN");
		Login.addActionListener(this);

		Logout = new JMenuItem("LogOut");
		Logout.addActionListener(this);
		Logout.setActionCommand("LOGOUT");

		Exit = new JMenuItem("Exit");
		Exit.setActionCommand("CLOSE");
		Exit.addActionListener(this);

		Categories = new JMenuItem("Categories");
		Categories.setActionCommand("CATEGORIES");
		Categories.addActionListener(this);

		Attributes = new JMenuItem("Attributes");
		Attributes.setActionCommand("ATTRIBUTES");
		Attributes.addActionListener(this);

		Products = new JMenuItem("Products");
		Products.setActionCommand("PRODUCTS");
		Products.addActionListener(this);

		ViewCartItem = new JMenuItem("View Cart");
		ViewCartItem.setActionCommand("VIEWCART");
		ViewCartItem.addActionListener(this);

		ViewReg = new JMenuItem("View Request");
		AddUser = new JMenuItem("Add User");
		AddUser.setActionCommand("ADDUSER");
		AddUser.addActionListener(this);

		ViewUsers = new JMenuItem("View Users");
		ViewUsers.setActionCommand("ViewUsers");
		ViewUsers.addActionListener(this);

		btnAddProduct = new JButton("Add");
		btnAddProduct.addActionListener(this);
		btnAddProduct.setActionCommand("ADDPRODUCT");

		AppMenu.add(Login);
		AppMenu.add(Logout);
		AppMenu.addSeparator();
		AppMenu.add(ViewUsers);
		AppMenu.addSeparator();
		AppMenu.add(Exit);
		AdminMenu.add(Categories);
		AdminMenu.add(Attributes);
		AdminMenu.add(Products);
		AdminMenu.add(AddUser);
		AdminMenu.add(ViewReg);

		CartMenu.add(ViewCartItem);

		Menu.add(AppMenu);
		Menu.add(AdminMenu);
		Menu.add(CartMenu);

		AdminMenu.setEnabled(true);
		ViewReg.setEnabled(true);

		setJMenuBar(Menu);

		mdlProduct = new ProductToBuyModel();
		tblProducts = new JTable(mdlProduct);

		mdlCartModel = new CartsModel();

		for (int i = 0; i < mdlProduct.getColumnCount(); i++)
		{
			GuiTools.setColumnTitle(tblProducts, i, mdlProduct.getColumnName(i));
		}
		scpProducts = new JScrollPane(tblProducts);
		scpProducts.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpProducts.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 1.0, 0.0, 1, 2, scpProducts);
		pan.addObject(1, 0, 1.0, 0.0, 1, 1, btnAddProduct);
		pan.addSizing(2, 1, 0.0, 0.0, 1, 1, 0, 0);
		setContentPane(pan);
		updateTableContent();

	}

	private void updateTableContent( )
	{
		DBOperationsProduct.listProducts(context, mdlProduct.getList(), "", 0);
		mdlProduct.sort();
		mdlProduct.fireTableDataChanged();

	}

	private void setupGuiForCurrentUser( )
	{
		setTitle("Store");

		if (context.getUser() != null)
		{
			setTitle("Store - logged in user '" + context.getUser().getName() + "'" + (context.getUser().isAdmin() ? " - as administrator" : ""));

			AdminMenu.setEnabled(context.getUser().isAdmin());
			CartMenu.setEnabled(!context.getUser().isAdmin());
			Login.setEnabled(false);
			Logout.setEnabled(true);
			ViewUsers.setEnabled(context.getUser().isAdmin());
			
			btnAddProduct.setEnabled(!context.getUser().isAdmin());
			updateCarts();

		}
		else
		{	
			AdminMenu.setEnabled(false);
			CartMenu.setEnabled(false);
			Login.setEnabled(true);
			Logout.setEnabled(false);
			ViewUsers.setEnabled(false);
			btnAddProduct.setEnabled(false);
		

		}
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("LOGIN"))
		{
			User user = LoginDialog.login(this, context);
			if (user != null)
			{
				context.setUser(user);
			}
			setupGuiForCurrentUser();
		}
		if (command.equalsIgnoreCase("LOGOUT"))
		{
			context.setUser(null);
			setupGuiForCurrentUser();
		}
		if (command.equalsIgnoreCase("ADDUSER"))
		{
			User user = EditUserDataDialog.addUser(this, context);
			if (user != null)
			{
				JOptionPane.showMessageDialog(this, "The user '" + user.getName() + "' was added!", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		if (command.equalsIgnoreCase("ViewUsers"))
		{
			ShowUsersDialog.showUsers(this, context);
		}
		if (command.equalsIgnoreCase("CATEGORIES"))
		{
			ShowCategoriesDialog.showCategories(this, context);
		}
		if (command.equalsIgnoreCase("ATTRIBUTES"))
		{
			ShowAttributesDialog.showAttributeCategories(this, context);
		}
		if (command.equalsIgnoreCase("PRODUCTS"))
		{
			ViewProductsDialog.showProducts(this, context);
		}
		//VIEW CART
		if (command.equalsIgnoreCase("VIEWCART"))
		{
			
		}
		//Add to cart
		if (command.equalsIgnoreCase("ADDPRODUCT"))
		{
			cartProduct = new CartProducts();
			if (tblProducts.getSelectedRow() >= 0)
			{
				String value = JOptionPane.showInputDialog(this, "Enter the quantity");
				//
				updateCarts();
				try
				{
					int quantity = Integer.parseInt(value);
					cartProduct.setQuantity(quantity);
				}
				catch (NumberFormatException ex)
				{
					System.out.println(e.toString());
					return;
				}

				cartProduct.setId_product(mdlProduct.get(tblProducts.getSelectedRow()).getId());

				cartProduct.setId_cart(mdlCartModel.getIdCartOpen(context.getUser().getId()));

				if (cartProduct.getId_cart() <= 0)
				{
					System.out.println("No cart opened");
					cartProduct = null;
				}
				if (cartProduct != null)
				{
					int id = DBOperationsCartProduct.addCartProduct(context, cartProduct);
					if (id > 0)
					{
						JOptionPane.showMessageDialog(this, "The product has been added !", "Information", JOptionPane.INFORMATION_MESSAGE);

					}
					else
					{
						JOptionPane.showMessageDialog(this, "Could not add the product to cart !", "Information", JOptionPane.INFORMATION_MESSAGE);

					}
				}
			}
		}

		if (command.equalsIgnoreCase("CLOSE"))
		{
			dispose();
		}

	}

	private void updateCarts( )
	{
		DBOpeationsCart.listCarts(context, mdlCartModel.getList(), context.getUser().getId());
		cart = mdlCartModel.getCartById(mdlCartModel.getIdCartOpen(context.getUser().getId()));
		if (cart == null)
		{
			int is_user = context.getUser().getId();
			cart = new Carts();
			cart.setId_user(is_user);
			cart.setStatus(1);
			DBOpeationsCart.addCart(context, cart);
		}
	}
}
