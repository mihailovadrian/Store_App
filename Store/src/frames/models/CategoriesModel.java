package frames.models;

import entities.Categories;
import frames.models.comparator.ComparatorForCategories;
import frames.tools.*;

public class CategoriesModel extends GenericModel<Categories>
{
	private static final long serialVersionUID = 1L;
	private boolean simple;

	public CategoriesModel( boolean simple )
	{
		super();
		this.simple = simple;
	}

	@Override
	public int getColumnCount( )
	{
		return (simple ? 1 : 3);
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		Object result = null;

		if (list != null)
		{
			if ((0 <= rowIndex) && (rowIndex < list.size()))
			{
				Categories cat = list.get(rowIndex);

				if (cat != null)
				{
					switch (columnIndex)
					{
						case 0:
							result = cat.getName();
							break;
						case 1:
							result = cat.getDescription();
							break;
						case 2:
							result = cat.getPathToImage();
							break;

					}
				}
			}
		}

		return result;
	}

	@Override
	protected GenericComparator<Categories> createComparator( )
	{
		return new ComparatorForCategories();
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
					result = "PathToImage";
					break;

			}
		}
		return result;
	}

	public Categories getObjectById( int id )
	{
		Categories result = null;
		if (list != null)
		{
			for (Categories item : list)
			{
				if (item.getId() == id)
				{
					result = item;
				}
			}
		}
		return result;
	}

}
