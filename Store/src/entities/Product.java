package entities;

public class Product
{
	private int id;
	private String Name;
	private String Description;
	private int id_Category;
	private int quantity;

	public Product( int id, String name, String description, int id_Category, int quantity )
	{
		super();
		this.id = id;
		Name = name;
		Description = description;
		this.id_Category = id_Category;
		this.quantity = quantity;
	}

	public Product()
	{
		super();
		this.id = 0;
		Name = "";
		Description = "";
		this.id_Category = 0;
		this.quantity = 0;
	}

	@Override
	public String toString( )
	{
		return "Product [id=" + id + ", Name=" + Name + ", Description=" + Description + ", id_Category=" + id_Category + ", quantity=" + quantity + "]";
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
		Product other = (Product) obj;
		if (Description == null)
		{
			if (other.Description != null)
				return false;
		}
		else
			if (!Description.equals(other.Description))
				return false;
		if (Name == null)
		{
			if (other.Name != null)
				return false;
		}
		else
			if (!Name.equals(other.Name))
				return false;
		if (id != other.id)
			return false;
		if (id_Category != other.id_Category)
			return false;
		if (quantity != other.quantity)
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

	public int getId_Category( )
	{
		return id_Category;
	}

	public void setId_Category( int id_Category )
	{
		this.id_Category = id_Category;
	}

	public int getQuantity( )
	{
		return quantity;
	}

	public void setQuantity( int quantity )
	{
		this.quantity = quantity;
	}

}
