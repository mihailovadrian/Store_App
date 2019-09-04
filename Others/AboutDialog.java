package frames;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import application.*;
import tools.*;

public class AboutDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = -7854872627767257146L;

	public static boolean open( JDialog parentDialog, JFrame parentFrame )
	{
		boolean result = false;

		AboutDialog d = null;

		if (parentDialog != null)
		{
			d = new AboutDialog(parentDialog);
		}
		else
		{
			d = new AboutDialog(parentFrame);
		}

		GUITools.showDialog(d, parentDialog, parentFrame);

		return result;
	}

	public AboutDialog( JDialog parent )
	{
		super(parent);

		createGUI();
	}

	public AboutDialog( JFrame parent )
	{
		super(parent);

		createGUI();
	}

	public void createGUI( )
	{
		setTitle("About");
		setSize(new Dimension(390, 155));
		setResizable(false);

		Font f = new Font("Arial", Font.PLAIN, 11);

		// -----------------------------------------------------------------------------------------
		// create the panel with the application title
		// -----------------------------------------------------------------------------------------
		JLabel lblTitle = new JLabel(AppConstants.appOwner + " ®  " + AppConstants.appName);
		lblTitle.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked( MouseEvent e )
			{
				JOptionPane.showMessageDialog(null, "Ovidiu (aka Maretzu') created this fantastic application!");
			}
		});

		GridPanel pnlTitle = new GridPanel(5, 0, 0, 0);

		pnlTitle.addSizing(0, 0, 1.0, 0.0, 1, 1);
		pnlTitle.addObject(1, 0, 0.0, 0.0, 1, 1, lblTitle);
		pnlTitle.addSizing(2, 0, 1.0, 0.0, 1, 1);

		// -----------------------------------------------------------------------------------------
		// create the panel with the application version
		// -----------------------------------------------------------------------------------------
		GridPanel pnlVersion = new GridPanel(5, 0, 0, 0);

		pnlVersion.addSizing(0, 0, 1.0, 0.0, 1, 1);
		pnlVersion.addObject(1, 0, 0.0, 0.0, 1, 1, new JLabel("Version " + AppConstants.appVersion));
		pnlVersion.addSizing(2, 0, 1.0, 0.0, 1, 1);

		// -----------------------------------------------------------------------------------------
		// create the panel with the copyright
		// -----------------------------------------------------------------------------------------
		JLabel lblCopyright = new JLabel("Copyright © 2016 " + AppConstants.appOwner + ". All rights reserved.");
		lblCopyright.setFont(f);

		GridPanel pnlCopyright = new GridPanel(5, 0, 0, 0);

		pnlCopyright.addSizing(0, 0, 1.0, 0.0, 1, 1);
		pnlCopyright.addObject(1, 0, 0.0, 0.0, 1, 1, lblCopyright);
		pnlCopyright.addSizing(2, 0, 1.0, 0.0, 1, 1);

		// -----------------------------------------------------------------------------------------
		// create the panel with the warning message and the Close button
		// -----------------------------------------------------------------------------------------
		JButton btnClose = GUITools.createButton(this, "Close", AppConstants.CANCEL_COMMAND);

		GridPanel pnlWarning = new GridPanel(0, 0, 0, 0);

		pnlWarning.addSizing(0, 0, 0.0, 0.0, 1, 1, 5, 5);
		pnlWarning.addSizing(0, 1, 0.0, 0.0, 1, 1, 300, 50);
		pnlWarning.addObject(1, 1, 1.0, 1.0, 1, 1, btnClose);
		pnlWarning.addSizing(2, 2, 0.0, 0.0, 1, 1, 5, 5);

		// -----------------------------------------------------------------------------------------
		// create the general panel
		// -----------------------------------------------------------------------------------------
		GridPanel pnlGeneral = new GridPanel(0, 0, 0, 0);

		pnlGeneral.addObject(0, 0, 0.0, 0.0, 1, 1, pnlTitle);
		pnlGeneral.addObject(0, 1, 0.0, 0.0, 1, 1, pnlVersion);
		pnlGeneral.addObject(0, 2, 0.0, 0.0, 1, 1, pnlCopyright);
		pnlGeneral.addSizing(0, 3, 1.0, 1.0, 1, 1);
		pnlGeneral.addObject(0, 4, 1.0, 0.0, 1, 1, GUITools.horizBevel());
		pnlGeneral.addObject(0, 5, 0.0, 0.0, 1, 1, pnlWarning);

		setContentPane(pnlGeneral);

		getRootPane().setDefaultButton(btnClose);
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equals(AppConstants.CANCEL_COMMAND))
		{
			dispose();
		}
	}
}
