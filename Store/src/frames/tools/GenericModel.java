package frames.tools;

import java.util.*;

import javax.swing.table.*;

public abstract class GenericModel<T> extends AbstractTableModel
{
	private static final long serialVersionUID = 8074030892080770288L;

	protected List<T> list = null;
	protected GenericComparator<T> comparator = null;

	public GenericModel()
	{
		super();

		this.list = new ArrayList<T>();
		this.comparator = createComparator();
	}

	protected abstract GenericComparator<T> createComparator( );

	public abstract String getColumnName( int columnIndex );

	public int getRowCount( )
	{
		int result = 0;

		if (list != null)
		{
			result = list.size();
		}

		return result;
	}

	public List<T> getList( )
	{
		return list;
	}

	public void setList( List<T> list )
	{
		this.list = list;
	}

	public int indexOf( T object )
	{
		int result = -1;

		if ((list != null) && (object != null))
		{
			result = list.indexOf(object);
		}

		return result;
	}

	public T get( int rowIndex )
	{
		T result = null;

		if ((list != null) && (0 <= rowIndex) && (rowIndex < list.size()))
		{
			result = list.get(rowIndex);
		}

		return result;
	}

	public void sort( int columnIndex, boolean ascending )
	{
		if ((0 <= columnIndex) && (columnIndex < getColumnCount()))
		{
			comparator.setColumnIndex(columnIndex);
			comparator.setAscending(ascending);

			Collections.sort(list, comparator);
		}
	}

	public void sort( )
	{
		Collections.sort(list, comparator);
	}

	public void setSortColumnIndex( int columnIndex )
	{
		if (columnIndex == comparator.getColumnIndex())
		{
			comparator.setAscending(!comparator.isAscending());
		}
		else
		{
			comparator.setColumnIndex(columnIndex);
			comparator.setAscending(true);
		}
	}

	public int getSortColumnIndex( )
	{
		return comparator.getColumnIndex();
	}

	public boolean getSortAscending( )
	{
		return comparator.isAscending();
	}

	public void setSortAscending( boolean ascending )
	{
		comparator.setAscending(ascending);
	}
}
