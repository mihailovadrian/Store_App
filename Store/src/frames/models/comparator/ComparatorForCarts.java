package frames.models.comparator;

import frames.tools.GenericComparator;
import tools.ComparatorString;
import entities.*;

public class ComparatorForCarts extends GenericComparator<Cart>
{

	@Override
	public int compare( Cart c1, Cart c2 )
	{
		int result = 0;

		String s1 = "";
		String s2 = "";

		switch (columnIndex)
		{
			case 0:
				s1 = "" + c1.getStatus();
				s2 = "" + c2.getStatus();
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
