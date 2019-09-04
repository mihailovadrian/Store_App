package frames.models;

import java.util.*;
import javax.swing.table.*;

import application.AppConnDataServer;

public class ServersModel extends AbstractTableModel
{
	private static final long serialVersionUID = 8490932972995238573L;

	private List<AppConnDataServer> list = null;

	public ServersModel( List<AppConnDataServer> list )
	{
		this.list = list;
	}

	@Override
	public int getRowCount( )
	{
		int result = 0;

		if (list != null)
		{
			result = list.size();
		}

		return result;
	}

	@Override
	public int getColumnCount( )
	{
		return 1;
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		Object result = null;

		if (list != null)
		{
			if ((0 <= rowIndex) && (rowIndex < list.size()))
			{
				result = list.get(rowIndex).getUrl();
			}
		}

		return result;
	}

	public AppConnDataServer get( int pos )
	{
		AppConnDataServer result = null;

		if ((0 <= pos) && (pos < list.size()))
		{
			result = list.get(pos);
		}

		return result;
	}

	public boolean add( AppConnDataServer server )
	{
		boolean result = false;

		if (server != null)
		{
			result = list.add(server);
		}

		return result;
	}

	public boolean delete( int pos )
	{
		boolean result = false;

		if ((0 <= pos) && (pos < list.size()))
		{
			list.remove(pos);
			result = true;
		}

		return result;
	}
}
