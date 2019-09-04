package frames.models;

import java.util.*;

import javax.swing.table.*;

import models.*;

public class UsersModel extends AbstractTableModel
{
	private static final long serialVersionUID = 7493525111304021786L;

	protected List<User> list = null;
	protected ComparatorForUser comparator;

	public UsersModel()
	{
		list = new ArrayList<>();
		comparator = new ComparatorForUser();
	}

	public List<User> getList( )
	{
		return list;
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
		return 5;
	}

	public String getColumnName( int columnIndex )
	{
		String result = null;

		if ((0 <= columnIndex) && (columnIndex < getColumnCount()))
		{
			switch (columnIndex)
			{
				case 0:
					result = "Name";
					break;
				case 1:
					result = "Email";
					break;
				case 2:
					result = "Password";
					break;
				case 3:
					result = "Address";
					break;
				case 4:
					result = "Admin";
					break;
			}
		}

		return result;
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		Object result = null;

		if (list != null)
		{
			if ((0 <= rowIndex) && (rowIndex < list.size()))
			{
				User user = list.get(rowIndex);

				if (user != null)
				{
					switch (columnIndex)
					{
						case 0:
							result = user.getName();
							break;
						case 1:
							result = user.getEmail();
							break;
						case 2:
							result = user.getPassword();
							break;
						case 3:
							result = user.getAddress();
							break;
						case 4:
							result = user.isAdmin() ? "yes" : "";
							break;
					}
				}
			}
		}

		return result;
	}

	public User get( int rowIndex )
	{
		User result = null;

		if ((list != null) && (0 <= rowIndex) && (rowIndex < list.size()))
		{
			result = list.get(rowIndex);
		}

		return result;
	}

	public int indexOf( User user )
	{
		int result = -1;

		if (list != null && user != null)
		{
			result = list.indexOf(user);

		}

		return result;
	}

	public int getColumnIndex( )
	{
		return comparator.getColumnIndex();
	}

	public void setColumnIndex( int columnIndex )
	{
		if (columnIndex == comparator.getColumnIndex())
		{
			comparator.setAscending(!comparator.isAscending());
		}
		else
		{
			comparator.setColumnIndex(columnIndex);
			comparator.setAscending(true);
		}
	}

	public boolean isAscending( )
	{
		return comparator.isAscending();
	}

	public void setAscending( boolean ascending )
	{
		comparator.setAscending(ascending);
	}

	public void sort( int columnIndex, boolean ascending )
	{
		if ((0 <= columnIndex) && (columnIndex < getColumnCount()))
		{
			comparator.setColumnIndex(columnIndex);
			comparator.setAscending(ascending);

			Collections.sort(list, comparator);
		}
	}

	public void sort( )
	{
		Collections.sort(list, comparator);
	}
}
