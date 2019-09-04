package frames;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import app.*;
import dbTools.DBOperationsUser;
import entities.*;
import tools.*;

public class EditUserDataDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1089001605263687239L;

	private AppContext context;

	private User user;
	private User userToUpdate;
	private boolean success;

	private JLabel lblName;
	private JLabel lblEmail;
	private JLabel lblAddress;
	private JLabel lblPassword;
	private JCheckBox isAdmin;
	private JTextField txtName;
	private JTextField txtEmail;
	private JTextField txtAddress;
	private JTextField txtPassword;

	// Used for JFRAME in MainFrame
	public static User addUser( JFrame parentFrame, AppContext context )//
	{
		User result = null;

		EditUserDataDialog d = new EditUserDataDialog(parentFrame, context, null);

		GuiTools.showDialog(d, null, parentFrame);
		if (d.success)
		{
			result = d.user;
		}

		return result;
	}

	//used for JDialog in ShowUsersDialog
	public static User addUserDialog( JDialog parentFrame, AppContext context )//
	{
		User result = null;

		EditUserDataDialog d = new EditUserDataDialog(parentFrame, context, null);

		GuiTools.showDialog(d, parentFrame, null);
		if (d.success)
		{
			result = d.user;
		}

		return result;
	}

	//modified for update action
	public static User editUser( JDialog parentDialog, AppContext context, User userToUpdate )
	{
		User result = null;

		EditUserDataDialog d = new EditUserDataDialog(parentDialog, context, userToUpdate);

		GuiTools.showDialog(d, parentDialog, null);
		if (d.success)
		{
			result = d.user;
		}

		return result;
	}

	public EditUserDataDialog( JFrame parent, AppContext context, User userToUpdate )
	{
		super(parent);

		success = false;
		this.user = null;
		this.userToUpdate = userToUpdate;

		setupGUI(context);
	}

	//modified
	public EditUserDataDialog( JDialog parent, AppContext context, User userToUpdate )
	{
		super(parent);

		success = false;
		this.user = null;
		this.userToUpdate = userToUpdate;

		setupGUI(context);
	}

	public void setupGUI( AppContext context )
	{
		setSize(new Dimension(400, 300));
		setMinimumSize(new Dimension(300, 300));

		this.context = context;
		//Modified  from use to usertoupdate
		if (userToUpdate == null)
		{
			setTitle("Add user");
		}
		else
		{
			setTitle("Edit user data");
		}

		lblName = new JLabel("Name");
		lblEmail = new JLabel("Email");
		lblAddress = new JLabel("Address");
		lblPassword = new JLabel("Password");

		txtName = new JTextField();
		txtEmail = new JTextField();
		txtAddress = new JTextField();
		txtPassword = new JTextField();
		isAdmin = new JCheckBox("It is the user an admin?");

		if (userToUpdate != null)
		{
			txtName.setText(userToUpdate.getName());
			txtEmail.setText(userToUpdate.getEmail());
			txtPassword.setText(userToUpdate.getPassword());
			txtAddress.setText(userToUpdate.getAddress());
			isAdmin.setSelected(userToUpdate.isAdmin());
		}

		JButton btnAddUser = new JButton(userToUpdate != null ? "Update" : "Add");
		btnAddUser.addActionListener(this);
		btnAddUser.setActionCommand("SAVE");

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(this);
		btnClose.setActionCommand("CLOSE");

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 1.0, 0.0, 1, 1, lblName);
		pan.addObject(0, 1, 1.0, 0.0, 1, 1, txtName);

		pan.addObject(0, 2, 1.0, 0.0, 1, 1, lblEmail);
		pan.addObject(0, 3, 1.0, 0.0, 1, 1, txtEmail);

		pan.addObject(0, 4, 1.0, 0.0, 1, 1, lblPassword);
		pan.addObject(0, 5, 1.0, 0.0, 1, 1, txtPassword);

		pan.addObject(0, 6, 1.0, 0.0, 1, 1, lblAddress);
		pan.addObject(0, 7, 1.0, 0.0, 1, 1, txtAddress);

		pan.addObject(0, 8, 1.0, 0.0, 1, 1, isAdmin);

		// Buttons GridPanel

		GridPanel panButtons = new GridPanel(5, 3, 0, 0);

		panButtons.addSizing(0, 0, 1.0, 0.0, 1, 1, 10, 0);
		panButtons.addObject(1, 0, 0.0, 0.0, 1, 1, btnAddUser);
		panButtons.addObject(2, 0, 0.0, 0.0, 1, 1, btnClose);

		pan.addSizing(0, 9, 0.0, 1.0, 1, 1, 10, 0);
		pan.addObject(0, 10, 1.0, 0.0, 1, 1, panButtons);
		pan.addSizing(2, 11, 0, 0, 1, 1, 0, 0);

		setContentPane(pan);
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("CLOSE"))
		{
			dispose();
		}

		if (command.equalsIgnoreCase("SAVE"))
		{
			User userToSave = new User();

			userToSave.setName(txtName.getText());
			userToSave.setAddress(txtAddress.getText());
			userToSave.setEmail(txtEmail.getText());
			userToSave.setPassword(txtPassword.getText());
			userToSave.setAdmin(isAdmin.isSelected());
			if (userToUpdate == null)
			{
				int id = DBOperationsUser.addUser(context, userToSave);
				if (id > 0)
				{
					user = DBOperationsUser.getUser(context, id);
					success = true;
					dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The user couldn't be added!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				userToSave.setId(userToUpdate.getId());

				if (DBOperationsUser.editUser(context, userToSave))
				{
					user = DBOperationsUser.getUser(context, userToUpdate.getId());
					success = true;
					dispose();
				}
				else
				{
					String msg = "The user couldn't be added!";
					if (userToUpdate != null)
					{
						msg = "The user data couldn't be updated!";
					}

					JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

}
