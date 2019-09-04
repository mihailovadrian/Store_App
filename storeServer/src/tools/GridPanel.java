package tools;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class GridPanel extends JPanel
{
	private static final long serialVersionUID = 1945343489782451529L;

	private GridBagLayout gbl = null;
	private GridBagConstraints gbc = null;

	public GridPanel( int top, int left, int bottom, int right )
	{
		super();

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(top, left, bottom, right);

		gbl = new GridBagLayout();
		setLayout(gbl);
	}

	public void setBorder( int width, Color color )
	{
		setBorder(new LineBorder(color, width));
	}

	public void setBorder( int width )
	{
		setBorder(new LineBorder(Color.lightGray, width));
	}

	public void addObject( int x, int y, double wx, double wy, int gw, int gh, JComponent object )
	{
		gbc.gridwidth = gw; // no of cells in a row
		gbc.gridheight = gh; // no of cells in a column

		gbc.weightx = wx; // horizontal resizing behavior
		gbc.weighty = wy; // vertical resizing behavior

		gbc.gridx = x; // x of the cell containing the leading corner
		gbc.gridy = y; // y of the cell containing the leading corner

		gbl.setConstraints(object, gbc);

		this.add(object);
	}

	public void addSizing( int x, int y, double wx, double wy, int gw, int gh, int width, int height )
	{
		addObject(x, y, wx, wy, gw, gh, sizingPanel(width, height));
	}

	public void addSizing( int x, int y, double wx, double wy, int gw, int gh )
	{
		addObject(x, y, wx, wy, gw, gh, sizingPanel(0, 0));
	}

	public static JPanel sizingPanel( int width, int height )
	{
		JPanel result = new JPanel();

		result.setBorder(new EmptyBorder(height / 2, width / 2, height / 2, width / 2));

		GridBagLayout gbl = new GridBagLayout();
		result.setLayout(gbl);

		return result;
	}
}
