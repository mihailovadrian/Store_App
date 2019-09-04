package frames.models.comparator;

import frames.tools.GenericComparator;
import tools.ComparatorString;
import entities.*;

public class ComparatorForCartProducts extends GenericComparator<CartProducts>
{

	@Override
	public int compare( CartProducts cartProd1, CartProducts cartProd2 )
	{
		int result = 0;

		String s1 = "";
		String s2 = "";

		switch (columnIndex)
		{
			case 0:
				s1 = "" + cartProd1.getId_product();
				s2 = "" + cartProd2.getId_product();
				break;
			case 1:
				s1 = "" + cartProd1.getQuantity();
				s2 = "" + cartProd2.getQuantity();
				break;
			case 2:
				s1 = cartProd1.getEntry_DataTime();
				s2 = cartProd2.getEntry_DataTime();
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
