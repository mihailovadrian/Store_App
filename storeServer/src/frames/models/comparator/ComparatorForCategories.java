package frames.models.comparator;

import entities.*;
import frames.tools.GenericComparator;
import tools.ComparatorString;

public class ComparatorForCategories extends GenericComparator<Categories>
{
	public ComparatorForCategories()
	{
		super();
	}

	@Override
	public int compare( Categories cat1, Categories cat2 )
	{
		int result = 0;

		String s1 = "";
		String s2 = "";

		switch (columnIndex)
		{
			case 0:
				s1 = cat1.getName();
				s2 = cat2.getName();
				break;
			case 1:
				s1 = cat1.getDescription();
				s2 = cat2.getDescription();
				break;

			case 2:
				s1 = cat1.getPathToImage();
				s2 = cat2.getPathToImage();
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
