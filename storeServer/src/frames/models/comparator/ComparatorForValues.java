package frames.models.comparator;

import entities.AttributeValues;
import frames.tools.GenericComparator;
import tools.ComparatorString;

public class ComparatorForValues extends GenericComparator<AttributeValues>
{

	@Override
	public int compare( AttributeValues val1, AttributeValues val2 )
	{
		int result = 0;

		String s1 = "";
		String s2 = "";

		switch (columnIndex)
		{
			case 0:
				s1 = "" + val1.getId_Attribute();
				s2 = "" + val2.getId_Attribute();
				break;
			case 1:
				s1 = val1.getValue();
				s2 = val2.getValue();
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
