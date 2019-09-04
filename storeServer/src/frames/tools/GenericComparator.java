package frames.tools;

import java.util.*;

public abstract class GenericComparator<T> implements Comparator<T>
{
	protected int columnIndex;
	protected boolean ascending;

	public GenericComparator()
	{
		super();

		columnIndex = -1;
		ascending = true;
	}

	public int getColumnIndex( )
	{
		return columnIndex;
	}

	public void setColumnIndex( int columnIndex )
	{
		this.columnIndex = columnIndex;
	}

	public boolean isAscending( )
	{
		return ascending;
	}

	public void setAscending( boolean ascending )
	{
		this.ascending = ascending;
	}
}
