package frames;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.sql.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import app.AppContext;
import tools.ConnectionTools;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = 8544513875314876016L;

	private AppContext context;

	private JPanel contentPane;
	private JTextField TextFieldSearch;
	private JTable table;
	private JTextField TxtName;
	private JTextField TxtAddress;
	private JTextField TxtEmail;
	private JPasswordField passwordField;

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 * @throws InstantiationException
	 */
	public MainFrame()
	{
		super();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		context = new AppContext(ConnectionTools.openConnection("dub-mssql1:1433", "internship", "sa", "imsmaxims2013"));
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		TextFieldSearch = new JTextField();
		TextFieldSearch.setBounds(191, 47, 178, 29);
		contentPane.add(TextFieldSearch);
		TextFieldSearch.setColumns(10);

		JButton BtnSearch = new JButton("Search");
		BtnSearch.setBounds(400, 47, 89, 29);
		contentPane.add(BtnSearch);

		table = new JTable();
		table.setCellSelectionEnabled(true);
		table.setColumnSelectionAllowed(true);
		table.setBackground(Color.WHITE);
		table.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setBounds(169, 284, 450, -146);
		contentPane.add(table);

		JLabel lblNewLabel = new JLabel("User Login");
		lblNewLabel.setBounds(771, 54, 111, 14);
		contentPane.add(lblNewLabel);
		try
		{
			BufferedImage myPicture = ImageIO.read(new File("C:\\Users\\adrian.mihailov\\eclipse-workspace\\test\\Image\\images.png"));
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JButton btnAddToCart = new JButton("Add to cart");

		// Action Button Add to cart
		btnAddToCart.addActionListener(new ActionListener()
		{
			public void actionPerformed( ActionEvent arg0 )
			{
				//New Dialog
				JDialog d = new JDialog(new JFrame("New Frame"), "New Dialog");
				d.setVisible(true);
				d.setBounds(100, 100, 927, 566);

			}
		});
		btnAddToCart.setBounds(790, 163, 89, 53);
		contentPane.add(btnAddToCart);
		JPopupMenu popupmenu = new JPopupMenu("Meniu");
		JMenuItem it1 = new JMenuItem("Masini de Spalat");
		JMenuItem it2 = new JMenuItem("Tractoare");
		JMenuItem it3 = new JMenuItem("TV");
		popupmenu.add(it1);
		popupmenu.add(it2);
		popupmenu.add(it3);

		JLabel lblMeniu = new JLabel("Meniu (2)");
		lblMeniu.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered( MouseEvent arg0 )
			{
				//popupmenu.show(this, arg0.getX(),arg0.getY());
			}
		});
		lblMeniu.setBounds(32, 23, 46, 14);
		contentPane.add(lblMeniu);
		ConnectionTest ct = new ConnectionTest();
		ct.ReadUser();
		ct.FillTable(table, "select * from users");
		TxtName = new JTextField();
		TxtName.setBounds(155, 329, 86, 20);
		contentPane.add(TxtName);
		TxtName.setColumns(10);
		TxtAddress = new JTextField();
		TxtAddress.setBounds(155, 385, 86, 20);
		contentPane.add(TxtAddress);
		TxtAddress.setColumns(10);
		TxtEmail = new JTextField();
		TxtEmail.setBounds(319, 329, 190, 20);
		contentPane.add(TxtEmail);
		TxtEmail.setColumns(10);
		JLabel lblNewLabel_1 = new JLabel("Name");
		lblNewLabel_1.setBounds(61, 332, 46, 14);
		contentPane.add(lblNewLabel_1);
		JLabel lblAddres = new JLabel("Address");
		lblAddres.setBounds(61, 388, 46, 14);
		contentPane.add(lblAddres);
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(261, 332, 46, 14);
		contentPane.add(lblEmail);
		JCheckBox ChkAdmin = new JCheckBox("Is admin?");
		ChkAdmin.setBounds(61, 430, 97, 23);
		contentPane.add(ChkAdmin);
		JButton BtnSend = new JButton("Send");
		BtnSend.addActionListener(new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{

				String connectionUrl = "jdbc:sqlserver://dub-mssql1:1433;databaseName=internship";

				Connection con = null;
				Statement stmt = null;
				ResultSet rs = null;

				try
				{

					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					con = DriverManager.getConnection(connectionUrl, "sa", "imsmaxims2013");

					// SQL
					String query = " insert users (name,email,password,address,isadmin)"

							+ " values (?, ?, ?, ?,?)";

					PreparedStatement preparedStmt = con.prepareStatement(query);

					preparedStmt.setString(1, TxtName.getText());

					preparedStmt.setString(2, TxtEmail.getText());
					preparedStmt.setString(3, passwordField.getText());
					preparedStmt.setString(4, TxtAddress.getText());
					if (ChkAdmin.isSelected())
					{
						preparedStmt.setLong(5, 1);

					}
					else
					{

						preparedStmt.setLong(5, 0);
					}
					//stmt = con.createStatement();

					preparedStmt.execute();

					JOptionPane.showMessageDialog(null, "Saved !", "InfoBox: ", JOptionPane.INFORMATION_MESSAGE);

				}

				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					if (rs != null)
						try
						{
							rs.close();
						}
						catch (Exception ex)
						{
						}
					if (stmt != null)
						try
						{
							stmt.close();
						}
						catch (Exception ex)
						{
						}
					if (con != null)
						try
						{
							con.close();
						}
						catch (Exception ex)
						{
						}
				}
			}
		});
		BtnSend.setBounds(559, 328, 89, 23);
		contentPane.add(BtnSend);
		passwordField = new JPasswordField();
		passwordField.setBounds(319, 385, 89, 20);
		contentPane.add(passwordField);
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(261, 388, 46, 14);
		contentPane.add(lblPassword);

	}
}
