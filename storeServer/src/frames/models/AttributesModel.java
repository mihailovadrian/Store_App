package frames.models;

import app.AppConstants;
import entities.*;
import frames.models.comparator.ComparatorForAttributes;
import frames.tools.GenericComparator;
import frames.tools.GenericModel;

public class AttributesModel extends GenericModel<Attribute>
{
	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount( )
	{
		return 5;
	}

	public Attribute getAttributeById( int id )
	{
		Attribute result = null;

		for (Attribute item : list)
		{

			if (item.getId_Attribute() == id)
			{
				result = item;
			}
		}
		return result;

	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		Object result = null;

		if (list != null)
		{
			if ((0 <= rowIndex) && (rowIndex < list.size()))
			{
				Attribute atr = list.get(rowIndex);

				if (atr != null)
				{
					switch (columnIndex)
					{
						case 0:
							result = atr.getName();
							break;
						case 1:
							result = atr.getDescription();
							break;
						case 2:
							switch (atr.getValueType())
							{
								case AppConstants.ATTR_TYPE_STRING:
									result = "String";
									break;
								case AppConstants.ATTR_TYPE_DOUBLE:
									result = "Double";
									break;
								case AppConstants.ATTR_TYPE_INTEGER:
									result = "Integer";
									break;

							}
							break;
						case 3:
							result = atr.isList() ? "yes" : "";
							break;
						case 4:
							result = atr.isMandatory() ? "yes" : "";
							break;

					}
				}
			}
		}

		return result;
	}

	@Override
	protected GenericComparator<Attribute> createComparator( )
	{
		return new ComparatorForAttributes();
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
					result = "Value Type";
					break;
				case 3:
					result = "List";
					break;
				case 4:
					result = "Mandatory";
					break;

			}
		}
		return result;
	}

}
