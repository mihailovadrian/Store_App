package frames;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

import tools.GridPanel;
import tools.GuiTools;
import app.AppContext;
import dbTools.DBOperationsAttribute;
import dbTools.DBOperationsCategories;
import entities.*;
import frames.models.CategoriesModel;

public class EditAttributeDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private boolean success;
	private AppContext context;

	private CategoriesModel mdlCategories;

	private Attribute attr;
	private Attribute attrToUpdate;

	private JLabel lblName;
	private JLabel lblDescription;
	private JLabel lblType;

	private JTextField txtName;
	private JTextArea txtDescription;
	private JCheckBox chkIsList;
	private JCheckBox chkIsMandatory;
	private JComboBox<Object> ValueType;
	private JComboBox<Object> comboCategory;

	public static Attribute addAttributesDialog( JDialog parentFrame, AppContext context )//
	{
		Attribute result = null;

		EditAttributeDialog d = new EditAttributeDialog(context, parentFrame, null);

		GuiTools.showDialog(d, parentFrame, null);

		if (d.success)
		{

			result = d.attr;

		}

		return result;
	}

	public static Attribute editAttributes( JDialog parentDialog, AppContext context, Attribute attrToUpdate )
	{
		Attribute result = null;

		EditAttributeDialog d = new EditAttributeDialog(context, parentDialog, attrToUpdate);

		GuiTools.showDialog(d, parentDialog, null);

		if (d.success)
		{
			result = d.attr;
		}

		return result;
	}

	public EditAttributeDialog( AppContext context, JDialog parentFrame, Attribute attrToUpdate )
	{
		super(parentFrame);
		success = false;
		this.attr = null;
		this.attrToUpdate = attrToUpdate;

		setupGUI(context);
	}

	private void setupGUI( AppContext context )
	{
		setSize(new Dimension(450, 350));
		setMinimumSize(new Dimension(300, 300));
		this.context = context;
		if (attrToUpdate == null)
		{
			setTitle("Add Attribute");
		}
		else
		{
			setTitle("Edit Attribute");
		}
		mdlCategories = new CategoriesModel(false);

		lblName = new JLabel("Name");
		lblDescription = new JLabel("Description");
		lblType = new JLabel("Attribute Type");

		txtName = new JTextField();
		txtDescription = new JTextArea();
		chkIsList = new JCheckBox("List");
		chkIsMandatory = new JCheckBox("Mandatory");

		DBOperationsCategories.listCategories(context, mdlCategories.getList(), "");
		comboCategory = new JComboBox<Object>();

		for (Categories item : mdlCategories.getList())
		{
			comboCategory.addItem(item.getName());
		}

		String valueType[] =
		{ "Integer", "Double", "String" };
		ValueType = new JComboBox<Object>(valueType);
		ValueType.setSelectedIndex(-1);

		comboCategory.setSelectedIndex(-1);

		if (attrToUpdate != null)
		{
			txtName.setText(attrToUpdate.getName());
			txtDescription.setText(attrToUpdate.getDescription());
			Categories cat = mdlCategories.getObjectById(attrToUpdate.getId_Category());
			comboCategory.setSelectedIndex(mdlCategories.indexOf(cat));
			ValueType.setSelectedIndex(attrToUpdate.getValueType());
			chkIsList.setSelected(attrToUpdate.isList());
			chkIsMandatory.setSelected(attrToUpdate.isMandatory());
		}

		JButton btnAddAttribute = new JButton(attrToUpdate != null ? "Update" : "Add");
		btnAddAttribute.addActionListener(this);
		btnAddAttribute.setActionCommand("SAVE");

		JButton btnClose = new JButton("Cancel");
		btnClose.addActionListener(this);
		btnClose.setActionCommand("CLOSE");

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 1.0, 0.0, 3, 1, lblName);
		pan.addObject(0, 1, 1.0, 0.0, 3, 1, txtName);

		pan.addObject(0, 2, 1.0, 0.0, 3, 1, lblDescription);
		pan.addObject(0, 3, 1.0, 1.0, 3, 1, new JScrollPane(txtDescription));

		pan.addObject(0, 4, 1.0, 0.0, 3, 1, lblType);
		pan.addObject(0, 5, 1.0, 0.0, 3, 1, ValueType);

		pan.addObject(0, 6, 1.0, 0.0, 3, 1, new JLabel("Category"));
		pan.addObject(0, 7, 1.0, 0.0, 3, 1, comboCategory);

		pan.addSizing(0, 8, 0.0, 0.0, 1, 1, 1, 1);

		pan.addObject(0, 9, 0.0, 0.0, 1, 1, chkIsList);
		pan.addObject(1, 9, 0.0, 0.0, 1, 1, chkIsMandatory);
		pan.addSizing(2, 9, 1.0, 0.0, 1, 1);

		GridPanel panButtons = new GridPanel(5, 5, 0, 0);

		panButtons.addSizing(0, 0, 1.0, 0.0, 1, 1, 10, 0);
		panButtons.addObject(1, 0, 0.0, 0.0, 1, 1, btnAddAttribute);
		panButtons.addObject(2, 0, 0.0, 0.0, 1, 1, btnClose);

		pan.addObject(0, 10, 1.0, 0.0, 3, 1, panButtons);
		pan.addSizing(3, 11, 0, 0, 1, 1, 0, 0);

		setContentPane(pan);

	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("SAVE"))
		{

			Attribute attrToSave = new Attribute();
			//

			attrToSave.setName(txtName.getText());
			attrToSave.setDescription(txtDescription.getText());
			attrToSave.setList(chkIsList.isSelected());
			attrToSave.setMandatory(chkIsMandatory.isSelected());
			Categories cat = mdlCategories.get(comboCategory.getSelectedIndex());
			attrToSave.setId_Category(cat.getId());
			if (ValueType.getSelectedIndex() >= 0)
			{
				attrToSave.setValueType(ValueType.getSelectedIndex());
			}
			else
			{
				JOptionPane.showMessageDialog(this, "The value type is not selected!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (attrToUpdate == null)
			{

				int id = DBOperationsAttribute.addAttribute(context, attrToSave);
				if (id > 0)
				{
					attr = DBOperationsAttribute.getAttribute(context, id);
					success = true;
					dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The attribute couldn't be added!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				attrToSave.setId_Attribute(attrToUpdate.getId_Attribute());

				if (DBOperationsAttribute.editAttribute(context, attrToSave))
				{

					attr = DBOperationsAttribute.getAttribute(context, attrToUpdate.getId_Attribute());
					success = true;
					dispose();
				}
				else
				{
					String msg = "The Attribute couldn't be added!";
					if (attrToSave != null)
					{
						msg = "The Attribute data couldn't be updated!";
					}

					JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (command.equalsIgnoreCase("Close"))

		{
			dispose();
		}

	}

}
