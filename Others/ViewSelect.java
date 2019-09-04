package frames;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import generic.table.*;
import tools.*;

public class ViewSelect<T> extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 5340223608159092755L;

	private T object;

	private TableManager<T> tblObjects;

	public static <T> T select( JDialog parentDialog, JFrame parentFrame, String title, TableManager<T> tblObjects )
	{
		T result = null;

		ViewSelect<T> d = null;

		if (parentDialog != null)
		{
			d = new ViewSelect<T>(parentDialog, title, tblObjects);
		}
		else
		{
			d = new ViewSelect<T>(parentFrame, title, tblObjects);
		}

		GUITools.showDialog(d, parentDialog, parentFrame);

		result = d.object;

		return result;
	}

	public ViewSelect( JDialog parent, String title, TableManager<T> tblObjects )
	{
		super(parent);

		object = null;

		this.tblObjects = tblObjects;

		createGUI(title);
	}

	public ViewSelect( JFrame parent, String title, TableManager<T> tblObjects )
	{
		super(parent);

		object = null;

		this.tblObjects = tblObjects;

		createGUI(title);
	}

	public void createGUI( String title )
	{
		setTitle(title);

		setSize(new Dimension(700, 400));
		GUITools.ensureMinimumSize(this, 700, 400);

		// -----------------------------------------------------------------------------------------
		// -----------------------------------------------------------------------------------------
		tblObjects.setActionListener(this);

		// -----------------------------------------------------------------------------------------
		// -----------------------------------------------------------------------------------------
		JButton btnCancel = GUITools.createButton(this, "Cancel", "CANCEL");
		JButton btnSelect = GUITools.createButton(this, "Select", "SELECT");

		// -----------------------------------------------------------------------------------------
		// create the general panel
		// -----------------------------------------------------------------------------------------
		GridPanel pnlGeneral = new GridPanel(5, 5, 0, 0);

		pnlGeneral.addObject(0, 0, 1.0, 1.0, 5, 1, tblObjects.getScrollPane());
		pnlGeneral.addObject(0, 1, 1.0, 0.0, 1, 1, GUITools.createBottomPanel(btnSelect, btnCancel));
		pnlGeneral.addSizing(1, 2, 0.0, 0.0, 1, 1, 5, 5);

		setContentPane(pnlGeneral);

		getRootPane().setDefaultButton(btnCancel);

		tblObjects.getModel().applyFilter();
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equals("SELECT"))
		{
			object = tblObjects.getSelected();
			if (object != null)
			{
				dispose();
			}
		}

		if (command.equals("CANCEL"))
		{
			object = null;
			dispose();
		}
	}
}
