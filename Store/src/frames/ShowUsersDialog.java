package frames;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import app.AppContext;
import dbTools.DBOperationsUser;
import entities.User;
import frames.models.*;
import tools.*;

public class ShowUsersDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private User user;
	private AppContext context;
	private JTable tblUsers;
	private JTextField txtSearchUser;
	private JScrollPane tableScrollPane;
	private UsersModel mdlUsers;
	private JLabel lblRecords;
	private User userToUpdate;

	public static User showUsers( JFrame parentFrame, AppContext context )
	{
		User result = null;

		ShowUsersDialog d = new ShowUsersDialog(parentFrame, context);

		GuiTools.showDialog(d, null, parentFrame);
		result = d.user;
		return result;

	}

	public ShowUsersDialog( JFrame parent, AppContext context )
	{
		super(parent);
		user = new User();

		setup(context);
	}

	public void setup( AppContext context )
	{
		setTitle("View Users");

		setSize(new Dimension(700, 500));

		this.context = context;

		txtSearchUser = new JTextField();

		lblRecords = new JLabel("Records :");

		JButton btnAddUser = new JButton("Add");
		btnAddUser.addActionListener(this);
		btnAddUser.setActionCommand("ADDUSER");

		JButton btnDeleteUser = new JButton("Delete");
		btnDeleteUser.addActionListener(this);
		btnDeleteUser.setActionCommand("DELETEUSER");

		JButton btnEditUser = new JButton("Edit");
		btnEditUser.addActionListener(this);
		btnEditUser.setActionCommand("EDITUSER");

		JButton btnSearchUser = new JButton("Search");
		btnSearchUser.addActionListener(this);
		btnSearchUser.setActionCommand("APPLY_FILTER");

		JButton btnExit = new JButton("Close");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("CLOSE");

		mdlUsers = new UsersModel();

		tblUsers = new JTable(mdlUsers);

		tblUsers.getTableHeader().addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked( MouseEvent e )
			{
				int columnIndex = tblUsers.columnAtPoint(e.getPoint());

				User selectedUser = mdlUsers.get(tblUsers.getSelectedRow());

				mdlUsers.setSortColumnIndex(columnIndex);

				updateTableContent(selectedUser, 0, false);
			}
		});

		for (int i = 0; i < mdlUsers.getColumnCount(); i++)
		{
			GuiTools.setColumnTitle(tblUsers, i, mdlUsers.getColumnName(i));
		}

		tableScrollPane = new JScrollPane(tblUsers);
		tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 0.0, 0.0, 1, 1, new JLabel("Filter"));
		pan.addObject(1, 0, 1.0, 0.0, 1, 1, txtSearchUser);
		pan.addSizing(2, 0, 4.0, 0.0, 1, 1, 1, 1);
		pan.addObject(3, 0, 0.0, 0.0, 1, 1, btnSearchUser);

		pan.addObject(0, 1, 1.0, 1.0, 4, 4, tableScrollPane);

		pan.addObject(4, 1, 0.0, 0.0, 1, 1, btnAddUser);
		pan.addObject(4, 2, 0.0, 0.0, 1, 1, btnEditUser);
		pan.addObject(4, 3, 0.0, 0.0, 1, 1, btnDeleteUser);
		pan.addSizing(4, 4, 0.0, 1.0, 1, 1, 0, 10);

		pan.addObject(0, 5, 0.0, 0.0, 4, 1, lblRecords);
		pan.addObject(4, 5, 0.0, 0.0, 1, 1, btnExit);
		pan.addSizing(5, 6, 0.0, 0.0, 1, 1, 0, 0);

		setContentPane(pan);
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("APPLY_FILTER"))
		{
			updateTableContent(null, 0, true);
		}

		if (command.equalsIgnoreCase("Edituser"))
		{
			userToUpdate = null;
			if (tblUsers.getSelectedRow() >= 0)
			{
				userToUpdate = mdlUsers.get(tblUsers.getSelectedRow());
				if (userToUpdate != null)
				{
					User user = EditUserDataDialog.editUser(this, context, userToUpdate);
					if (user != null)
					{
						updateTableContent(userToUpdate, 0, true);

						JOptionPane.showMessageDialog(this, "The user '" + user.getName() + "' was updated!", "Information", JOptionPane.INFORMATION_MESSAGE);
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

		if (command.equalsIgnoreCase("addUser"))
		{
			User user = EditUserDataDialog.addUserDialog(this, context);
			if (user != null)
			{
				user = DBOperationsUser.getUser(context, user.getId());

				updateTableContent(user, 0, true);

				JOptionPane.showMessageDialog(this, "The user '" + user.getName() + "' was added!", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		if (command.equalsIgnoreCase("deleteuser"))
		{
			int reply = JOptionPane.showConfirmDialog(null, "Do you really want to delete the user?", "Warning !", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				int row = tblUsers.getSelectedRow();
				if (row >= 0)
				{
					DBOperationsUser.deleteUser(context, mdlUsers.get(row));

					updateTableContent(null, row, true);
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Select the User that you want to update !", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}

		if (command.equalsIgnoreCase("Close"))

		{
			dispose();

		}
	}

	public void updateTableContent( User user, int row, boolean refresh )
	{
		if (refresh)
		{
			DBOperationsUser.listUsers(context, mdlUsers.getList(), txtSearchUser.getText());
		}

		mdlUsers.sort();
		mdlUsers.fireTableDataChanged();

		lblRecords.setText("Records: " + String.valueOf(tblUsers.getRowCount()));

		if (user == null)
		{
			GuiTools.setCurrentRow(tblUsers, row);
		}
		else
		{
			GuiTools.setCurrentRow(tblUsers, mdlUsers.indexOf(user));
		}
	}

}
