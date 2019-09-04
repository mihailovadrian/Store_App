package frames.models.comparator;

import entities.*;
import frames.tools.*;
import tools.ComparatorString;

public class ComparatorForUser extends GenericComparator<User>
{
	public ComparatorForUser()
	{
		super();
	}

	@Override
	public int compare( User user1, User user2 )
	{
		int result = 0;

		String s1 = "";
		String s2 = "";

		switch (columnIndex)
		{
			case 0:
				s1 = user1.getName();
				s2 = user2.getName();
				break;
			case 1:
				s1 = user1.getEmail();
				s2 = user2.getEmail();
				break;

			case 2:
				s1 = user1.getPassword();
				s2 = user2.getPassword();
				break;

			case 3:
				s1 = user1.getAddress();
				s2 = user2.getAddress();
				break;
			case 4:
				s1 = "" + user1.isAdmin();
				s2 = "" + user2.isAdmin();
				break;
		}

		ComparatorString.stringComparator(s1, s2);
		result = s1.compareTo(s2);
		if (!isAscending())
		{
			result = -result;
		}

		return result;
	}

}
