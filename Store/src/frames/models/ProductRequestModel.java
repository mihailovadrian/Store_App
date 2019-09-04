package frames.models;

import entities.*;
import frames.models.comparator.ComparatorForProductRequest;
import frames.tools.GenericComparator;
import frames.tools.GenericModel;

public class ProductRequestModel extends GenericModel<ProductRequest>
{
	private static final long serialVersionUID = 1L;

	public ProductRequestModel()
	{
		super();
	}

	@Override
	public int getColumnCount( )
	{

		return 4;
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		Object result = null;

		if (list != null)
		{
			if ((0 <= rowIndex) && (rowIndex < list.size()))
			{
				ProductRequest productRequest = list.get(rowIndex);

				if (productRequest != null)
				{
					switch (columnIndex)
					{
						case 0:
							result = productRequest.getId();
							break;
						case 1:
							result = productRequest.getEntryDateTime();
							break;
						case 2:
							result = productRequest.getQuantity();
							break;
						case 3:
							result = productRequest.getStatus();
							break;

					}
				}
			}
		}
		return result;

	}

	@Override
	protected GenericComparator<ProductRequest> createComparator( )
	{

		return new ComparatorForProductRequest();
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
					result = "Id";
					break;
				case 1:
					result = "Date time";
					break;
				case 2:
					result = "Quantity";
					break;
				case 3:
					result = "Status";
					break;

			}
		}
		return result;
	}

}
