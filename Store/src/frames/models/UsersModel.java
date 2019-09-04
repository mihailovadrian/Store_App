package frames.models;

import entities.*;
import frames.models.comparator.ComparatorForUser;
import frames.tools.*;

public class UsersModel extends GenericModel<User>
{
	private static final long serialVersionUID = 7493525111304021786L;

	public UsersModel()
	{
		super();
	}

	@Override
	public int getColumnCount( )
	{
		return 5;
	}

	@Override
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

	@Override
	protected GenericComparator<User> createComparator( )
	{
		return new ComparatorForUser();
	}
}
