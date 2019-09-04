package entities;

public class AttributeValues
{
	private int id;
	private int id_Attribute;
	private String value;

	public AttributeValues( int id, int id_Attribute, String value )
	{
		super();
		this.id = id;
		this.id_Attribute = id_Attribute;
		this.value = value;
	}

	public AttributeValues()
	{
		super();
		this.id = 0;
		this.id_Attribute = 0;
		this.value = null;
	}

	@Override
	public String toString( )
	{
		return "AttributeValues [id=" + id + ", id_Attribute=" + id_Attribute + ", value=" + value + "]";
	}

	@Override
	public boolean equals( Object obj )
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttributeValues other = (AttributeValues) obj;
		if (id != other.id)
			return false;
		if (id_Attribute != other.id_Attribute)
			return false;
		if (value == null)
		{
			if (other.value != null)
				return false;
		}
		else
			if (!value.equals(other.value))
				return false;
		return true;
	}

	public int getId( )
	{
		return id;
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public int getId_Attribute( )
	{
		return id_Attribute;
	}

	public void setId_Attribute( int id_Attribute )
	{
		this.id_Attribute = id_Attribute;
	}

	public String getValue( )
	{
		return value;
	}

	public void setValue( String value )
	{
		this.value = value;
	}
}
