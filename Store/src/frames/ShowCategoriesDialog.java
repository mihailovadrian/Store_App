package frames;

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import app.*;
import dbTools.DBOperationsCategories;
import entities.*;
import frames.models.*;
import tools.*;

public class ShowCategoriesDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Categories cat;
	private Categories catToUpdate;
	private AppContext context;
	private JTable TblCategories;
	private JTextField txtSearchCategory;
	private JScrollPane tableScrollPane;
	private CategoriesModel mdlCategories;
	private JLabel lblRecords;

	public static Categories showCategories( JFrame parentFrame, AppContext context )
	{
		Categories result = null;

		ShowCategoriesDialog d = new ShowCategoriesDialog(parentFrame, context);

		GuiTools.showDialog(d, null, parentFrame);
		result = d.cat;
		return result;

	}

	public ShowCategoriesDialog( JFrame parent, AppContext context )
	{
		super(parent);
		cat = new Categories();

		setup(context);
	}

	public void setup( AppContext context )
	{
		setTitle("View Categories");

		setSize(new Dimension(700, 500));

		this.context = context;

		txtSearchCategory = new JTextField();

		lblRecords = new JLabel("Records :");

		JButton btnAddUser = new JButton("Add");
		btnAddUser.addActionListener(this);
		btnAddUser.setActionCommand("ADDCATEGORY");

		JButton btnOpenImage = new JButton("Open Image");
		btnOpenImage.addActionListener(this);
		btnOpenImage.setActionCommand("OPENIMAGE");

		JButton btnDeleteUser = new JButton("Delete");
		btnDeleteUser.addActionListener(this);
		btnDeleteUser.setActionCommand("DELETECATEGORY");

		JButton btnEditUser = new JButton("Edit");
		btnEditUser.addActionListener(this);
		btnEditUser.setActionCommand("EDITCATEGORY");

		JButton btnSearchUser = new JButton("Search");
		btnSearchUser.addActionListener(this);
		btnSearchUser.setActionCommand("APPLY_FILTER");

		JButton btnExit = new JButton("Close");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("CLOSE");

		mdlCategories = new CategoriesModel(false);

		TblCategories = new JTable(mdlCategories);

		TblCategories.getTableHeader().addMouseListener(new MouseAdapter()
		{
			public void mouseClicked( MouseEvent e )
			{
				int columnIndex = TblCategories.columnAtPoint(e.getPoint());

				Categories selectedCat = mdlCategories.get(TblCategories.getSelectedRow());

				mdlCategories.setSortColumnIndex(columnIndex);

				updateTableContent(selectedCat, 0, false);
			}
		});

		for (int i = 0; i < mdlCategories.getColumnCount(); i++)
		{
			GuiTools.setColumnTitle(TblCategories, i, mdlCategories.getColumnName(i));
		}

		tableScrollPane = new JScrollPane(TblCategories);
		tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		GridPanel pan = new GridPanel(5, 5, 0, 0);

		pan.addObject(0, 0, 0.0, 0.0, 1, 1, new JLabel("Filter"));
		pan.addObject(1, 0, 1.0, 0.0, 1, 1, txtSearchCategory);
		pan.addSizing(2, 0, 4.0, 0.0, 1, 1, 1, 1);
		pan.addObject(3, 0, 0.0, 0.0, 1, 1, btnSearchUser);

		pan.addObject(0, 1, 1.0, 1.0, 4, 5, tableScrollPane);

		pan.addObject(4, 1, 0.0, 0.0, 1, 1, btnAddUser);
		pan.addObject(4, 2, 0.0, 0.0, 1, 1, btnEditUser);
		pan.addObject(4, 3, 0.0, 0.0, 1, 1, btnDeleteUser);
		pan.addObject(4, 4, 0.0, 0.0, 1, 1, btnOpenImage);
		pan.addSizing(4, 5, 0.0, 1.0, 1, 1, 0, 10);

		pan.addObject(0, 5, 0.0, 0.0, 4, 1, lblRecords);
		pan.addObject(4, 6, 0.0, 0.0, 1, 1, btnExit);
		pan.addSizing(5, 7, 0.0, 0.0, 1, 1, 0, 0);

		setContentPane(pan);
		updateTableContent(null, 0, true);
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();
		if (command.equalsIgnoreCase("ADDCATEGORY"))
		{
			Categories cat = EditCategoryDialog.addCategoriesDialog(this, context);
			if (cat != null)
			{
				cat = DBOperationsCategories.getCategories(context, cat.getId());
				updateTableContent(cat, 0, true);

				JOptionPane.showMessageDialog(this, "The user '" + cat.getName() + "' was added!", "Information", JOptionPane.INFORMATION_MESSAGE);

			}

		}

		if (command.equalsIgnoreCase("deletecategory"))
		{
			int reply = JOptionPane.showConfirmDialog(null, "Do you really want to delete this category?", "Warning !", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				int row = TblCategories.getSelectedRow();
				if (row >= 0)
				{
					DBOperationsCategories.deleteCategory(context, mdlCategories.get(row));

					updateTableContent(null, row, true);
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Select the category that you want to delete !", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		if (command.equalsIgnoreCase("EDITCATEGORY"))
		{
			catToUpdate = null;
			if (TblCategories.getSelectedRow() >= 0)
			{
				catToUpdate = mdlCategories.get(TblCategories.getSelectedRow());

				if (catToUpdate != null)
				{
					Categories cat = EditCategoryDialog.editCategory(this, context, catToUpdate);

					if (cat != null)
					{
						updateTableContent(cat, 0, true);

						JOptionPane.showMessageDialog(this, "The category '" + cat.getName() + "' was updated!", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Cannot edit the selected category !", "Warining", JOptionPane.WARNING_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Select the user that you want to update !", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		if (command.equalsIgnoreCase("APPLY_FILTER"))
		{
			updateTableContent(null, 0, true);
		}

		if (command.equalsIgnoreCase("OPENIMAGE"))
		{
			Categories selectedCat = mdlCategories.get(TblCategories.getSelectedRow());
			String path = selectedCat.getPathToImage();
			BufferedImage originalImage, resizeImageJpg = null;
			try
			{
				File file = new File(path);
				if (file.exists())
				{
					if (file.isFile())
					{
						originalImage = ImageIO.read(file);

						resizeImageJpg = ResizeImage.resizeImage(originalImage, 300, 300);

						JLabel picLabel = new JLabel(new ImageIcon(resizeImageJpg));

						JOptionPane.showMessageDialog(null, picLabel, "Image", JOptionPane.PLAIN_MESSAGE, null);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "The image path is for a folder instead of a file!", "Warning", JOptionPane.WARNING_MESSAGE, null);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "The image file was not found!", "Warning", JOptionPane.WARNING_MESSAGE, null);
				}
			}
			catch (IOException e1)
			{
				JOptionPane.showMessageDialog(this, "Error when open the file: " + path, "Error", JOptionPane.ERROR_MESSAGE, null);
				selectedCat = null;
				e1.printStackTrace();
			}
		}

		if (command.equalsIgnoreCase("CLOSE"))
		{
			dispose();
		}

	}

	public void updateTableContent( Categories cat, int row, boolean refresh )
	{

		if (refresh)
		{
			DBOperationsCategories.listCategories(context, mdlCategories.getList(), txtSearchCategory.getText());
		}

		mdlCategories.sort();
		mdlCategories.fireTableDataChanged();

		lblRecords.setText("Records: " + String.valueOf(mdlCategories.getRowCount()));

		if (cat == null)
		{
			GuiTools.setCurrentRow(TblCategories, row);
		}
		else
		{

			GuiTools.setCurrentRow(TblCategories, mdlCategories.indexOf(cat));
		}
	}

}
