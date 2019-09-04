package frames.models;

import frames.models.comparator.ComparatorForCartProducts;
import frames.tools.GenericComparator;
import frames.tools.GenericModel;

import java.util.List;

import app.AppContext;
import dbTools.DBOperationsProduct;
import entities.*;;

public class CartProductsModel extends GenericModel<CartProducts>
{
	private static final long serialVersionUID = 1L;
	private List<Product> products;
	private AppContext context;

	public CartProductsModel( AppContext context )
	{
		super();
		this.context = context;
		products = DBOperationsProduct.listProducts(this.context, products, null, 0);
	}

	@Override
	public int getColumnCount( )
	{

		return 3;
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		Object result = null;

		if (list != null)
		{
			if ((0 <= rowIndex) && (rowIndex < list.size()))
			{
				CartProducts cartProd = list.get(rowIndex);

				if (cartProd != null)
				{
					Product searched = new Product();
					searched.setId(cartProd.getId_product());
					int i = searchProductById(products, searched.getId());
					if (i >= 0)
					{
						Product product = products.get(i);
						switch (columnIndex)
						{
							case 0:
								result = product.getName();
								break;
							case 1:
								result = cartProd.getQuantity();
								break;
							case 2:
								result = cartProd.getEntry_DataTime();
								break;

						}
					}
				}
			}
		}

		return result;
	}

	@Override
	protected GenericComparator<CartProducts> createComparator( )
	{

		return new ComparatorForCartProducts();
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
					result = "Product";
					break;
				case 1:
					result = "Quantity";
					break;
				case 2:
					result = "Entry time";
					break;

			}
		}
		return result;
	}

	public int searchProductById( List<Product> list, int id )
	{
		int result = -1;
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).getId() == id)
			{
				result = i;
			}
		}

		return result;
	}

}
