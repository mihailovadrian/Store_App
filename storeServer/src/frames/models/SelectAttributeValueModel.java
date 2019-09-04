package frames.models;

import app.*;
import dbTools.*;
import entities.*;
import frames.models.comparator.*;
import frames.tools.*;

public class SelectAttributeValueModel extends GenericModel<AttributeValues>
{
	private static final long serialVersionUID = 1L;

	public SelectAttributeValueModel( AppContext context, Attribute attribute )
	{
		super();

		DBOperationsAttributeValues.listValues(context, list, attribute.getId_Attribute());
	}

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
					result = "Value";
					break;

			}
		}
		return result;
	}
}
