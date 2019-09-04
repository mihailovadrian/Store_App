package entities;

public class Attribute
{
	private int id;
	private String Name;
	private String Description;
	private int id_Category;
	private int ValueType;
	private boolean isList;
	private boolean isMandatory;

	public Attribute( int id_Attribute, String name, String description, int id_Category, int valueType, boolean isList, boolean isMandatory )
	{
		super();
		this.id = id_Attribute;
		Name = name;
		Description = description;
		this.id_Category = id_Category;
		ValueType = valueType;
		this.isList = isList;
		this.isMandatory = isMandatory;
	}

	public Attribute()
	{

		this.id = 0;
		Name = "";
		Description = "";
		this.id_Category = 0;
		ValueType = -1;
		this.isList = false;
		this.isMandatory = false;
	}

	@Override
	public String toString( )
	{
		return "AttributesCat [id_Attribute=" + id + ", Name=" + Name + ", Description=" + Description + ", id_Category=" + id_Category + ", ValueType=" + ValueType + ", isList="
				+ isList + ", isMandatory=" + isMandatory + "]";
	}

	public int getId_Attribute( )
	{
		return id;
	}

	public void setId_Attribute( int id_Attribute )
	{
		this.id = id_Attribute;
	}

	public int getId_Category( )
	{
		return id_Category;
	}

	public void setId_Category( int id_Category )
	{
		this.id_Category = id_Category;
	}

	public int getValueType( )
	{
		return ValueType;
	}

	public void setValueType( int valueType )
	{
		ValueType = valueType;
	}

	public boolean isList( )
	{
		return isList;
	}

	public void setList( boolean isList )
	{
		this.isList = isList;
	}

	public boolean isMandatory( )
	{
		return isMandatory;
	}

	public void setMandatory( boolean isMandatory )
	{
		this.isMandatory = isMandatory;
	}

	public String getName( )
	{
		return Name;
	}

	public void setName( String name )
	{
		Name = name;
	}

	public String getDescription( )
	{
		return Description;
	}

	public void setDescription( String description )
	{
		Description = description;
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
		Attribute other = (Attribute) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
