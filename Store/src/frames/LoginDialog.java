package frames;

import javax.swing.*;

import app.AppContext;
import dbTools.DBOperationsUser;
import entities.User;
import tools.*;

import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1112179998087224413L;

	private AppContext context;
	private User user;

	private JTextField txtEmail;
	private JPasswordField txtPassword;

	public static User login( JFrame parentFrame, AppContext context )
	{
		User result = null;

		LoginDialog d = new LoginDialog(parentFrame, context);

		GuiTools.showDialog(d, null, parentFrame);

		result = d.user;

		return result;
	}

	public LoginDialog( JFrame parent, AppContext context )
	{
		super(parent);

		user = null;

		setupGUI(context, user);
	}


	protected void setupGUI( AppContext context, User user )
	{
		setTitle("Login");

		setSize(new Dimension(300, 180));

		this.context = context;

		txtEmail = new JTextField("adriann");
		txtPassword = new JPasswordField("adriann");

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(this);
		btnOk.setActionCommand("OK");

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("CANCEL");

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 1.0, 0.0, 1, 1, new JLabel("Email"));
		pan.addObject(0, 1, 1.0, 0.0, 1, 1, txtEmail);

		pan.addObject(0, 2, 1.0, 0.0, 1, 1, new JLabel("Password"));
		pan.addObject(0, 3, 1.0, 0.0, 1, 1, txtPassword);

		GridPanel panButtons = new GridPanel(5, 5, 0, 0);
		panButtons.addSizing(0, 0, 1.0, 0.0, 1, 1, 10, 0);
		panButtons.addObject(1, 0, 0.0, 0.0, 1, 1, btnOk);
		panButtons.addObject(2, 0, 0.0, 0.0, 1, 1, btnCancel);

		pan.addSizing(0, 4, 0.0, 1.0, 1, 1, 0, 0);
		pan.addObject(0, 5, 1.0, 0.0, 1, 1, panButtons);
		pan.addSizing(1, 6, 0.0, 0.0, 1, 1, 0, 0);

		setContentPane(pan);
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("OK"))
		{
			user = DBOperationsUser.getUser(context, txtEmail.getText(), String.valueOf(txtPassword.getPassword()));
			if (user == null)
			{
				JOptionPane.showMessageDialog(this, "There is no user with these credentials!", "Login failed", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				dispose();
			}
		}
		if (command.equalsIgnoreCase("CANCEL"))
		{
			dispose();
		}
	}
}
