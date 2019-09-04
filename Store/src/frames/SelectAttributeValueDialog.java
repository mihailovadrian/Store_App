package frames;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import app.AppContext;
import entities.*;
import frames.models.*;
import tools.*;

public class SelectAttributeValueDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 8629120409171915707L;

	private boolean success;

	private JTable tblValues;
	private JScrollPane scpValues;
	private SelectAttributeValueModel mdlValues;

	private Attribute attribute;
	private AttributeValues attributeValue;

	public static AttributeValues select( JDialog parentDialog, AppContext context, Attribute attribute )
	{
		AttributeValues result = null;

		SelectAttributeValueDialog d = new SelectAttributeValueDialog(context, parentDialog, attribute);

		GuiTools.showDialog(d, parentDialog, null);

		if (d.success)
		{
			result = d.attributeValue;
		}

		return result;
	}

	public SelectAttributeValueDialog( AppContext context, JDialog parentFrame, Attribute attribute )
	{
		super(parentFrame);
		success = false;

		this.attributeValue = null;
		this.attribute = attribute;

		setupGUI(context);
	}

	private void setupGUI( AppContext context )
	{
		setSize(new Dimension(400, 350));
		setMinimumSize(new Dimension(300, 300));

		setTitle("Select attribute value");

		mdlValues = new SelectAttributeValueModel(context, attribute);
		tblValues = new JTable(mdlValues);

		JButton btnEditValue = new JButton("Edit");
		btnEditValue.addActionListener(this);
		btnEditValue.setActionCommand("SAVE");

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(this);
		btnClose.setActionCommand("CLOSE");

		scpValues = new JScrollPane(tblValues);
		scpValues.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpValues.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 0.0, 1.0, 2, 7, scpValues);
		pan.addSizing(1, 0, 1.0, 0.0, 1, 6, 10, 0);
		pan.addObject(2, 0, 1.0, 0.0, 1, 1, btnEditValue);
		pan.addObject(2, 1, 1.0, 0.0, 1, 1, btnClose);

		pan.addSizing(3, 2, 0, 0, 1, 1, 0, 0);
		setContentPane(pan);
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("Save"))
		{
			attributeValue = mdlValues.get(tblValues.getSelectedRow());
			if (attributeValue != null)
			{
				success = true;
				dispose();
			}
		}

		if (command.equalsIgnoreCase("CLOSE"))
		{
			dispose();
		}

	}
}
