package frames.models.comparator;

import entities.Attribute;
import frames.tools.GenericComparator;
import tools.ComparatorString;

public class ComparatorForAttributes extends GenericComparator<Attribute>
{
	public ComparatorForAttributes()
	{
		super();
	}

	@Override
	public int compare( Attribute atr1, Attribute atr2 )
	{

		int result = 0;

		String s1 = "";
		String s2 = "";

		switch (columnIndex)
		{
			case 0:
				s1 = atr1.getName();
				s2 = atr2.getName();
				break;
			case 1:
				s1 = atr1.getDescription();
				s2 = atr2.getDescription();
				break;

			case 2:
				s1 = "" + atr1.getId_Category();
				s2 = "" + atr2.getId_Category();
				break;
			case 3:
				s1 = "" + atr1.getValueType();
				s2 = "" + atr2.getValueType();
				break;
			case 4:
				s1 = "" + atr1.isList();
				s2 = "" + atr2.isList();
				break;
			case 5:
				s1 = "" + atr1.isMandatory();
				s2 = "" + atr2.isMandatory();
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
