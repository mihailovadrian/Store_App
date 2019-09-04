package entities;

public class CartProducts
{
	private int id;
	private int id_cart;
	private int id_product;
	private String entry_DataTime;
	private int quantity;

	public CartProducts( int id, int id_cart, int id_product, String entry_DataTime, int quantity )
	{
		super();
		this.id = id;
		this.id_cart = id_cart;
		this.id_product = id_product;
		this.entry_DataTime = entry_DataTime;
		this.quantity = quantity;
	}

	public CartProducts()
	{
		super();
		this.id = 0;
		this.id_cart = 0;
		this.id_product = 0;
		this.entry_DataTime = null;
		this.quantity = 0;
	}

	public int getId( )
	{
		return id;
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public int getId_cart( )
	{
		return id_cart;
	}

	public void setId_cart( int id_cart )
	{
		this.id_cart = id_cart;
	}

	public int getId_product( )
	{
		return id_product;
	}

	public void setId_product( int id_product )
	{
		this.id_product = id_product;
	}

	public String getEntry_DataTime( )
	{
		return entry_DataTime;
	}

	public void setEntry_DataTime( String entry_DataTime )
	{
		this.entry_DataTime = entry_DataTime;
	}

	public int getQuantity( )
	{
		return quantity;
	}

	public void setQuantity( int quantity )
	{
		this.quantity = quantity;
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
		CartProducts other = (CartProducts) obj;
		if (id != other.id)
			return false;
		if (id_cart != other.id_cart)
			return false;
		if (id_product != other.id_product)
			return false;
		return true;
	}

	@Override
	public String toString( )
	{
		return "CartProducts [id=" + id + ", id_cart=" + id_cart + ", id_product=" + id_product + ", entry_DataTime=" + entry_DataTime + ", quantity=" + quantity + "]";
	}

}
