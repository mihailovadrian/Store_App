package frames.models.comparator;

import entities.ProductAttribute;
import frames.tools.GenericComparator;
import tools.ComparatorString;

public class ComparatorForProductValues extends GenericComparator<ProductAttribute>
{

	@Override
	public int compare( ProductAttribute pr1, ProductAttribute pr2 )
	{

		int result = 0;

		String s1 = "";
		String s2 = "";

		switch (columnIndex)
		{
			case 0:
				s1 = pr1.getValue();
				s2 = pr2.getValue();
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
