package frames.models;

import entities.*;
import frames.models.comparator.ComparatorForProducts;
import frames.tools.GenericComparator;
import frames.tools.GenericModel;

public class ProductsModel extends GenericModel<Product>
{
	private static final long serialVersionUID = 1L;

	public ProductsModel()
	{
		super();
	}

	@Override
	public int getColumnCount( )
	{
		return 3;
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		Object result = null;

		if (list != null)
		{
			if ((0 <= rowIndex) && (rowIndex < list.size()))
			{
				Product product = list.get(rowIndex);

				if (product != null)
				{
					switch (columnIndex)
					{
						case 0:
							result = product.getName();
							break;
						case 1:
							result = product.getDescription();
							break;
						case 2:
							result = product.getQuantity();
							break;

					}
				}
			}
		}
		return result;

	}

	@Override
	protected GenericComparator<Product> createComparator( )
	{
		return new ComparatorForProducts();
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
					result = "Name";
					break;
				case 1:
					result = "Description";
					break;
				case 2:
					result = "Quantity";
					break;

			}
		}
		return result;

	}

}
