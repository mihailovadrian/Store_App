package frames.models;

import java.util.List;

import app.*;
import dbTools.DBOperationsAttribute;
import dbTools.DBOperationsProductAttributes;
import entities.*;
import frames.models.comparator.ComparatorForProductValues;
import frames.tools.*;

public class ProductAttributesModel extends GenericModel<ProductAttribute>
{
	private static final long serialVersionUID = 1L;

	private AppContext context;
	private List<Attribute> attributes;

	private Product product;

	public ProductAttributesModel( AppContext context, Product product )
	{
		super();

		this.product = product;
		this.context = context;

		attributes = DBOperationsAttribute.listAttributes(this.context, null, this.product.getId_Category());

		DBOperationsProductAttributes.listProductAttribute(context, list, product.getId(), 0);

		ProductAttribute prodAttr;
		for (int i = 0; i < attributes.size(); i++)
		{
			if (getProductAttribute(attributes.get(i).getId_Attribute()) == null)
			{
				prodAttr = new ProductAttribute();
				prodAttr.setAttributeId(attributes.get(i).getId_Attribute());
				prodAttr.setProductId(product.getId());
				prodAttr.setId(0);
				prodAttr.setAttrValId(0);
				prodAttr.setValue(null);

				list.add(prodAttr);
			}
		}
	}

	public ProductAttribute getProductAttribute( int attributeId )
	{
		ProductAttribute result = null;

		for (int i = 0; (i < list.size()) && (result == null); i++)
		{
			if (list.get(i).getAttributeId() == attributeId)
			{
				result = list.get(i);
			}
		}

		return result;
	}

	@Override
	public int getColumnCount( )
	{
		return 5;
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		Object result = null;

		if (list != null)
		{
			if ((0 <= rowIndex) && (rowIndex < list.size()))
			{
				ProductAttribute value = list.get(rowIndex);

				if (value != null)
				{
					Attribute searched = new Attribute();

					searched.setId_Attribute(value.getAttributeId());
					int i = attributes.indexOf(searched);

					if (i >= 0)
					{
						Attribute attribute = attributes.get(i);

						switch (columnIndex)
						{
							case 0:
								result = attribute.getName();
								break;
							case 1:
								result = attribute.getDescription();
								break;
							case 2:

								switch (attribute.getValueType())
								{
									case 0:
										result = "Integer";
										break;
									case 1:
										result = "Double";
										break;

									case 2:
										result = "String";
										break;

								}
								break;
							case 3:
								result = attribute.isMandatory();
								break;
							case 4:
								if (attribute.isList())
								{
									result = value.getValue();
								}
								else
								{
									result = value.getValue();
								}
								break;
						}
					}
				}
			}
		}
		return result;

	}

	@Override
	protected GenericComparator<ProductAttribute> createComparator( )
	{

		return new ComparatorForProductValues();
	}

	public List<Attribute> getAttributes( )
	{
		return attributes;
	}

	public void setAttributes( List<Attribute> attributes )
	{
		this.attributes = attributes;
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
					result = "Value";
					break;
				case 1:
					result = "Description";
					break;
				case 2:
					result = "Type";
					break;
				case 3:
					result = "Mand";
					break;
				case 4:
					result = "Value";
					break;
			}
		}
		return result;
	}

	public boolean CheckValues( )
	{
		boolean result = true;
		for (ProductAttribute item : list)
		{
			if ((item.getValue() == null))
				result = false;
		}
		return result;
	}

}