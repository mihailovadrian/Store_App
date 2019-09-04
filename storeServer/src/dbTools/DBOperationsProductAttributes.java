package dbTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.AppContext;
import entities.*;

public class DBOperationsProductAttributes
{
	static public ProductAttribute readProductValue( ResultSet rs, ProductAttribute pr )
	{
		ProductAttribute result = null;
		try
		{
			if (rs != null)
			{
				if (pr == null)
				{
					pr = new ProductAttribute();
				}
				pr.setId(rs.getInt("id_ProdAttr"));
				pr.setProductId(rs.getInt("id_Product"));
				pr.setAttributeId(rs.getInt("id_attribute"));
				pr.setAttrValId(rs.getInt("id_attrVal"));
				pr.setValue(rs.getString("value"));

				result = pr;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	static public ProductAttribute getProductAttr( AppContext context, int id )
	{
		ProductAttribute result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from productattributes where id_prodattr= ?";
		try
		{
			ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next())
			{
				result = readProductValue(rs, null);
			}
		}
		catch (Exception e)
		{
			result = null;
			e.printStackTrace();
		}
		finally
		{
			onFinally(null, rs, ps);
		}

		return result;
	}

	static public int addProductAttr( AppContext context, ProductAttribute pr )
	{
		int result = 0;
		if (pr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = " insert into productAttributes (id_product,id_attribute,id_AttrVal,value)  values (?, ?, ?, ?)"; //OUTPUT Inserted.ID_USER

			try
			{
				ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				ps.setInt(1, pr.getProductId());
				ps.setInt(2, pr.getAttributeId());
				if (pr.getAttrValId() > 0)
				{
					ps.setInt(3, pr.getAttrValId());
				}
				else
				{
					ps.setNull(3, 0);
				}
				ps.setString(4, pr.getValue());

				ps.executeUpdate();

				rs = ps.getGeneratedKeys();
				if (rs.next())
				{
					result = rs.getInt(1);
				}

				con.commit();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				result = 0;

				try
				{
					con.rollback();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			finally
			{
				onFinally(null, rs, ps);
			}
		}
		return result;
	}

	static public boolean deleteProductAttribute( AppContext context, ProductAttribute pr )
	{
		boolean result = false;

		if (pr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;

			String sql = "DELETE FROM productAttributes WHERE id_prodattr=?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setInt(1, pr.getId());
				ps.executeUpdate();
				result = true;
				con.commit();
			}
			catch (Exception e)
			{
				e.printStackTrace();

				try
				{
					con.rollback();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
				result = false;
			}
			finally
			{
				onFinally(null, null, ps);
			}
		}
		return result;
	}

	static public boolean editProductAttribute( AppContext context, ProductAttribute pr )
	{
		boolean result = false;

		if (pr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = "update productAttributes set id_attrval = ?, value=? where id_prodattr = ?";

			try
			{
				ps = con.prepareStatement(sql);

				if (pr.getAttrValId() > 0)
				{
					ps.setInt(1, pr.getAttrValId());
				}
				else
				{
					ps.setNull(1, 0);
				}
				ps.setString(2, pr.getValue());
				ps.setInt(3, pr.getId());

				ps.executeUpdate();

				con.commit();

				result = true;
			}

			catch (Exception e)
			{
				e.printStackTrace();
				result = false;

				try
				{
					con.rollback();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			finally
			{
				onFinally(null, rs, ps);
			}
		}

		return result;
	}

	static public List<ProductAttribute> listProductAttribute( AppContext context, List<ProductAttribute> list, int idProduct, int idAttribute )
	{
		List<ProductAttribute> result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ProductAttribute pr;
		String sql = "select * from productAttributes ";

		if (idProduct > 0 && idAttribute > 0)
		{
			sql = sql + "where id_product=? " + " and id_attribute=?";
		}
		else
		{
			if (idProduct > 0 && idAttribute <= 0)
			{
				sql = sql + "where id_product=?";
			}
		}

		try
		{
			ps = con.prepareStatement(sql);

			if (idProduct > 0 && idAttribute > 0)
			{
				ps.setInt(1, idProduct);
				ps.setInt(2, idAttribute);
			}
			else
			{
				if (idProduct > 0 && idAttribute <= 0)
				{
					ps.setInt(1, idProduct);
				}
			}

			rs = ps.executeQuery();

			if (list == null)
			{
				list = new ArrayList<>();
			}
			else
			{
				list.clear();
			}

			while (rs.next())
			{
				pr = readProductValue(rs, null);
				if (pr != null)
				{
					list.add(pr);
				}
			}

			result = list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = null;
		}
		finally
		{
			onFinally(null, rs, ps);
		}

		return result;
	}

	public static void onFinally( Statement st, ResultSet rs, PreparedStatement ps )
	{
		if (rs != null)
			try
			{
				rs.close();
			}
			catch (Exception ex)
			{
			}
		if (st != null)
			try
			{
				st.close();
			}
			catch (Exception ex)
			{
			}
		try
		{
			if (ps != null)
			{
				ps.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
