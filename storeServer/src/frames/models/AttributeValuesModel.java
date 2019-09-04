package frames.models;

import entities.*;
import frames.models.comparator.ComparatorForValues;
import frames.tools.GenericComparator;
import frames.tools.GenericModel;

public class AttributeValuesModel extends GenericModel<AttributeValues>
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
				AttributeValues value = list.get(rowIndex);

				if (value != null)
				{
					switch (columnIndex)
					{
						case 0:
							result = value.getValue();
							break;

					}
				}
			}
		}

		return result;
	}

	public String getValueTypeString( int value )
	{
		String result = null;
		if (value > -1)
		{
			switch (value)
			{
				case 0:
					result = "Integer";
					break;
				case 1:
					result = "Double";
					break;
				case 2:
					result = "String";
					break;
			}
		}
		return result;
	}

	@Override
	protected GenericComparator<AttributeValues> createComparator( )
	{

		return new ComparatorForValues();
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
					result = "Values";
					break;

			}
		}
		return result;
	}

	public Boolean CheckValue( AttributeValues value )
	{
		boolean result = true;
		if (list != null && value != null)
		{

			for (AttributeValues item : list)
			{

				if (item.getValue().trim().toLowerCase().equals(value.getValue().trim().toLowerCase()))
					result = false;
			}
		}
		return result;
	}

}
