package frames.models;

import entities.Carts;
import frames.models.comparator.ComparatorForCarts;
import frames.tools.GenericComparator;
import frames.tools.GenericModel;

public class CartsModel extends GenericModel<Carts>
{
	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount( )
	{

		return 1;
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		Object result = null;

		if (list != null)
		{
			if ((0 <= rowIndex) && (rowIndex < list.size()))
			{
				Carts cart = list.get(rowIndex);

				if (cart != null)
				{
					switch (columnIndex)
					{
						case 0:
							result = cart.getStatus();
							break;

					}
				}
			}
		}

		return result;
	}

	@Override
	protected GenericComparator<Carts> createComparator( )
	{

		return new ComparatorForCarts();
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
					result = "Status";
					break;

			}
		}
		return result;
	}

	public int getIdCartOpen( int userId )
	{
		int result = 0;
		for (Carts item : list)
		{
			if ((item.getId_user() == userId) && (item.getStatus() == 1))
			{
				result = item.getId();
			}
		}
		return result;
	}

	public Carts getCartById( int id )
	{
		Carts result = null;
		for (Carts item : list)
		{
			if (item.getId() == id)
				result = item;
		}
		return result;

	}

}
