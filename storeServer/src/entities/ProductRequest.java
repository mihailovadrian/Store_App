package entities;

public class ProductRequest
{
	public static final int OPEN = 1;
	public static final int ALLOCATED = 2;
	public static final int CONSUMED = 3;
	public static final int CANCELLED = 4;

	private int id;
	private int productId;
	private int cartId;
	private java.util.Date entryDateTime;
	private int quantity;
	private int quantityAllocated;
	private java.util.Date allocatedDatetime;
	private int status;

	public ProductRequest( int id, int productId, int cartId, java.util.Date entry_datatime, int quantity, int quantityAllocated, java.util.Date allocatedDatetime,int status )
	{
		super();

		this.id = id;
		this.productId = productId;
		this.cartId = cartId;
		this.entryDateTime = entry_datatime;
		this.quantity = quantity;
		this.quantityAllocated = quantityAllocated;
		this.allocatedDatetime = allocatedDatetime;
		this.status = status;
	}

	public ProductRequest()
	{
		super();

		this.id = 0;
		this.productId = 0;
		this.cartId = 0;
		this.entryDateTime = null;
		this.quantity = 0;
		this.quantityAllocated = 0;
		this.allocatedDatetime = null;
		this.status = 0;
	}

	public int getId( )
	{
		return id;
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public int getProductId( )
	{
		return productId;
	}

	public void setProductId( int productId )
	{
		this.productId = productId;
	}

	public int getCartId( )
	{
		return cartId;
	}

	public void setCartId( int cartId )
	{
		this.cartId = cartId;
	}

	public java.util.Date getEntryDateTime( )
	{
		return entryDateTime;
	}

	public void setEntryDateTime( java.util.Date entry_datatime )
	{
		this.entryDateTime = entry_datatime;
	}

	public int getQuantity( )
	{
		return quantity;
	}

	public void setQuantity( int quantity )
	{
		this.quantity = quantity;
	}

	public int getQuantityAllocated( )
	{
		return quantityAllocated;
	}

	public void setQuantityAllocated( int quantityAllocated )
	{
		this.quantityAllocated = quantityAllocated;
	}

	public java.util.Date getAllocatedDatetime( )
	{
		return allocatedDatetime;
	}

	public void setAllocatedDatetime( java.util.Date allocatedDatetime )
	{
		this.allocatedDatetime = allocatedDatetime;
	}

	public int getStatus( )
	{
		return status;
	}

	public void setStatus( int status )
	{
		this.status = status;
	}

	@Override
	public String toString( )
	{
		return "ProductRequest [id=" + id + ", productId=" + productId + ", cartId=" + cartId + ", entry_datatime=" + entryDateTime + ", quantity=" + quantity + "]";
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
		ProductRequest other = (ProductRequest) obj;
		
		if (id != other.id)
			return false;
		
		
		return true;
	}

}
