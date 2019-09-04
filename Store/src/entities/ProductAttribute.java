package entities;

public class ProductAttribute
{
	private int id;
	private int productId;
	private int attributeId;
	private int attrValId;
	private String value;

	public ProductAttribute( int id, int idProduct, int idAttr, int idAttrVal, String value )
	{
		super();

		this.id = id;
		this.productId = idProduct;
		this.attributeId = idAttr;
		this.attrValId = idAttrVal;
		this.value = value;
	}

	public ProductAttribute()
	{
		super();

		this.id = 0;
		this.productId = 0;
		this.attributeId = 0;
		this.attrValId = 0;
		this.value = null;
	}

	@Override
	public String toString( )
	{
		return "ProductAttr [id=" + id + ", idProduct=" + productId + ", idAttr=" + attributeId + ", Value=" + value + "]" + " idAttrVal: " + attrValId;
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

		ProductAttribute other = (ProductAttribute) obj;
		if ((this.productId != other.productId) || (this.attributeId != other.attributeId))
		{
			return false;
		}

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

	public int getProductId( )
	{
		return productId;
	}

	public void setProductId( int productId )
	{
		this.productId = productId;
	}

	public int getAttributeId( )
	{
		return attributeId;
	}

	public void setAttributeId( int attributeId )
	{
		this.attributeId = attributeId;
	}

	public int getAttrValId( )
	{
		return attrValId;
	}

	public void setAttrValId( int attrValId )
	{
		this.attrValId = attrValId;
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
