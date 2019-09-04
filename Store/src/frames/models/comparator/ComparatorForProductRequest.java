package frames.models.comparator;

import entities.ProductRequest;
import frames.tools.GenericComparator;
import tools.ComparatorString;

public class ComparatorForProductRequest extends GenericComparator<ProductRequest>
{

	@Override
	public int compare( ProductRequest pr1, ProductRequest pr2 )
	{
		int result = 0;

		String s1 = "";
		String s2 = "";

		switch (columnIndex)
		{
			case 0:
				s1 = "" + pr1.getStatus();
				s2 = "" + pr2.getStatus();
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
