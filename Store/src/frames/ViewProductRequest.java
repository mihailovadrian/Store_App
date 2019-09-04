package frames;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import app.AppContext;
import dbTools.*;
import entities.*;
import frames.models.ProductRequestModel;
import tools.GridPanel;
import tools.GuiTools;

public class ViewProductRequest extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private AppContext context;

	private ProductRequest prodReq;

	private JTable tblProductRequest;
	private ProductRequestModel mdlProdReq;
	private JScrollPane scpProductRequest;

	public static ProductRequest showProductRequest( JFrame parent, AppContext context )
	{
		ProductRequest result = null;
		ViewProductRequest d = new ViewProductRequest(parent, context);
		GuiTools.showDialog(d, null, parent);
		result = d.prodReq;
		return result;
	}

	public ViewProductRequest( JFrame parent, AppContext context )
	{
		super(parent);
		prodReq = new ProductRequest();

		setupGUI(context);

		JButton btnExit = new JButton("Close");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("CLOSE");

		mdlProdReq = new ProductRequestModel();
		tblProductRequest = new JTable(mdlProdReq);

		for (int i = 0; i < mdlProdReq.getColumnCount(); i++)
		{
			GuiTools.setColumnTitle(tblProductRequest, i, mdlProdReq.getColumnName(i));
		}
		scpProductRequest = new JScrollPane(tblProductRequest);
		scpProductRequest.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpProductRequest.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		GridPanel pan = new GridPanel(0, 0, 0, 0);

		pan.addObject(0, 0, 0.0, 1.0, 1, 2, scpProductRequest);

		pan.addObject(1, 0, 0.0, 1.0, 1, 1, btnExit);
		pan.addSizing(2, 1, 1.0, 0.0, 1, 1, 1, 1);
		setContentPane(pan);
		updateTableContent();

	}

	private void setupGUI( AppContext context )
	{
		setTitle("View Products Requests");

		setSize(new Dimension(700, 500));
		setMinimumSize(new Dimension(300, 300));
		this.context = context;
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{

	}

	public void updateTableContent( )
	{

		DBOperationsProductRequest.listProductRequests(context, mdlProdReq.getList(), 1);

		mdlProdReq.sort();
		mdlProdReq.fireTableDataChanged();

	}

}
