package frames.models.comparator;

import entities.*;
import frames.tools.GenericComparator;
import tools.ComparatorString;

public class ComparatorForProducts extends GenericComparator<Product>
{

	@Override
	public int compare( Product pr1, Product pr2 )
	{

		int result = 0;

		String s1 = "";
		String s2 = "";

		switch (columnIndex)
		{
			case 0:
				s1 = pr1.getName();
				s2 = pr2.getName();
				break;
			case 1:
				s1 = pr1.getDescription();
				s2 = pr2.getDescription();
				break;

			case 2:
				s1 = "" + pr1.getId_Category();
				s2 = "" + pr2.getId_Category();
				break;
			case 3:
				s1 = "" + pr1.getQuantity();
				s2 = "" + pr2.getQuantity();
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
