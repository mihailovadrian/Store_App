package frames;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import app.*;
import dbTools.DBOperationsCategories;
import entities.Categories;
import tools.GridPanel;
import tools.GuiTools;

public class EditCategoryDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Categories cat;
	private Categories catToUpdate;
	private boolean success;
	private AppContext context;

	private JLabel lblName;
	private JLabel lblDescription;

	private JTextField txtName;
	private JTextArea txtDescription;
	private JTextField txtImage;

	public static Categories addCategoriesDialog( JDialog parentFrame, AppContext context )//
	{
		Categories result = null;

		EditCategoryDialog d = new EditCategoryDialog(context, parentFrame, null);

		GuiTools.showDialog(d, parentFrame, null);

		if (d.success)
		{

			result = d.cat;

		}

		return result;
	}

	public static Categories editCategory( JDialog parentDialog, AppContext context, Categories catToUpdate )
	{
		Categories result = null;

		EditCategoryDialog d = new EditCategoryDialog(context, parentDialog, catToUpdate);

		GuiTools.showDialog(d, parentDialog, null);

		if (d.success)
		{
			result = d.cat;
		}

		return result;
	}

	public EditCategoryDialog( AppContext context, JDialog parentFrame, Categories catToUpdate )
	{
		super(parentFrame);

		success = false;
		this.cat = null;
		this.catToUpdate = catToUpdate;

		setupGUI(context);
	}

	private void setupGUI( AppContext context )
	{
		setSize(new Dimension(400, 300));
		setMinimumSize(new Dimension(300, 300));

		this.context = context;
		//Modified  from use to usertoupdate
		if (catToUpdate == null)
		{
			setTitle("Add Category");
		}
		else
		{
			setTitle("Edit Category");
		}

		lblName = new JLabel("Name");
		lblDescription = new JLabel("Description");

		txtName = new JTextField();
		txtDescription = new JTextArea();
		txtImage = new JTextField();

		if (catToUpdate != null)
		{
			txtName.setText(catToUpdate.getName());
			txtDescription.setText(catToUpdate.getDescription());
			txtImage.setText(catToUpdate.getPathToImage());

		}

		JButton btnAddUser = new JButton(catToUpdate != null ? "Update" : "Add");
		btnAddUser.addActionListener(this);
		btnAddUser.setActionCommand("SAVE");

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(this);
		btnClose.setActionCommand("CLOSE");

		JButton btnOpenImage = new JButton("Open Image");
		btnOpenImage.addActionListener(this);
		btnOpenImage.setActionCommand("OPENIMAGE");

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 1.0, 0.0, 2, 1, lblName);
		pan.addObject(0, 1, 1.0, 0.0, 2, 1, txtName);

		pan.addObject(0, 2, 1.0, 0.0, 2, 1, lblDescription);

		pan.addObject(0, 3, 1.0, 1.0, 2, 1, new JScrollPane(txtDescription));
		pan.addSizing(0, 4, 1.0, 0.0, 2, 1, 10, 0);
		pan.addObject(0, 5, 1.0, 0.0, 1, 1, new JLabel("Image path"));
		pan.addObject(0, 6, 7.0, 0.0, 1, 1, txtImage);
		pan.addObject(1, 6, 1.0, 0.0, 1, 1, btnOpenImage);

		// Buttons GridPanel

		GridPanel panButtons = new GridPanel(5, 3, 0, 0);

		panButtons.addSizing(0, 0, 1.0, 0.0, 1, 1, 10, 0);
		panButtons.addObject(1, 0, 0.0, 0.0, 1, 1, btnAddUser);
		panButtons.addObject(2, 0, 0.0, 0.0, 1, 1, btnClose);

		pan.addSizing(0, 7, 0.0, 1.0, 1, 1, 10, 0);
		pan.addObject(0, 8, 1.0, 0.0, 2, 1, panButtons);
		pan.addSizing(2, 9, 0, 0, 1, 1, 0, 0);

		setContentPane(pan);

	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase("SAVE"))
		{
			Categories catToSave = new Categories();

			catToSave.setName(txtName.getText());
			catToSave.setDescription(txtDescription.getText());
			catToSave.setPathToImage(txtImage.getText());
			if (catToUpdate == null)
			{

				int id = DBOperationsCategories.addCategory(context, catToSave);
				if (id > 0)
				{
					cat = DBOperationsCategories.getCategories(context, id);
					success = true;
					dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The category couldn't be added!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				catToSave.setId(catToUpdate.getId());

				if (DBOperationsCategories.editCategory(context, catToSave))
				{
					cat = DBOperationsCategories.getCategory(context, catToUpdate.getId());
					success = true;
					dispose();
				}
				else
				{
					String msg = "The category couldn't be added!";
					if (catToUpdate != null)
					{
						msg = "The Category data couldn't be updated!";
					}

					JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (command.equalsIgnoreCase("OPENIMAGE"))
		{
			JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + System.getProperty("file.separator") + "Pictures");

			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setDialogTitle("Choose a photo");
			chooser.setApproveButtonText("Get Path");
			chooser.setFileFilter(new FileFilter()
			{
				@Override
				public String getDescription( )
				{
					return "Images";
				}

				@Override
				public boolean accept( File f )
				{
					if (f.isDirectory())
					{
						return true;
					}
					else
					{
						return f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".gif") || f.getName().toLowerCase().endsWith(".png")
								|| f.getName().toLowerCase().endsWith(".bmp") || f.getName().toLowerCase().endsWith(".jpeg") || f.getName().toLowerCase().endsWith(".tiff");
					}
				}

			});
			int i = chooser.showOpenDialog(this);
			if (i == JFileChooser.APPROVE_OPTION)
			{
				String path = chooser.getSelectedFile().toString();
				txtImage.setText(path);
			}

		}

		if (command.equalsIgnoreCase("Close"))
		{
			dispose();
		}

	}

}
