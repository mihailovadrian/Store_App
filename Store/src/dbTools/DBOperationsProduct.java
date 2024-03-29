package dbTools;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import app.AppContext;
import entities.Product;

public class DBOperationsProduct
{
	static public Product readProduct( ResultSet rs, Product pr )
	{
		Product result = null;
		try
		{
			if (rs != null)
			{
				if (pr == null)
				{
					pr = new Product();
				}
				pr.setId(rs.getInt("id_product"));
				pr.setName(rs.getString("name"));
				pr.setDescription(rs.getString("description"));
				pr.setId_Category(rs.getInt("id_category"));
				pr.setQuantity(rs.getInt("quantity"));
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

	static public Product getProduct( AppContext context, int id )
	{
		Product result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from products where id_product= ?";
		try
		{
			ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next())
			{
				result = readProduct(rs, null);
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

	static public int addProduct( AppContext context, Product pr )
	{
		int result = 0;
		if (pr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = " insert products(name,description,id_category,quantity)  values (?, ?, ?, ?)"; //OUTPUT Inserted.ID_USER

			try
			{
				ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				ps.setString(1, pr.getName());
				ps.setString(2, pr.getDescription());
				ps.setInt(3, pr.getId_Category());
				ps.setInt(4, pr.getQuantity());

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

	static public List<Product> listProducts( AppContext context, List<Product> list, String nameFilter, int idCategory )
	{
		List<Product> result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Product pr;
		String sql = "select * from products ";
		if (nameFilter != null)
		{
			nameFilter = nameFilter.trim().toLowerCase();
			if (nameFilter.length() > 0)
			{
				sql = sql + "where lower(name) like ? ";
			}
			else
			{
				nameFilter = null;
			}
		}
		if (nameFilter != null && idCategory > 0)
		{

			sql = sql + "and id_category=? ";

		}
		else
			if (nameFilter == null && idCategory > 0)
			{
				sql = sql + "where id_category=? ";
			}
		sql = sql + "order by name";

		try
		{
			ps = con.prepareStatement(sql);

			if (nameFilter != null)
			{
				ps.setString(1, "%" + nameFilter + "%");
			}
			if (nameFilter != null && idCategory > 0)
			{
				ps.setInt(2, idCategory);

			}
			if (nameFilter == null && idCategory > 0)
			{
				ps.setInt(1, idCategory);

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
				pr = readProduct(rs, null);
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

	static public void deleteProduct( AppContext context, Product pr )
	{
		if (pr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;

			String sql = "DELETE FROM products WHERE id_product=?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setInt(1, pr.getId());
				ps.executeUpdate();

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
			}
			finally
			{
				onFinally(null, null, ps);
			}
		}
	}

	static public boolean editProduct( AppContext context, Product pr )
	{
		boolean result = false;

		if (pr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = "update products set name = ?, description = ?, id_category = ?,quantity=? where id_product = ?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setString(1, pr.getName());
				ps.setString(2, pr.getDescription());
				ps.setInt(3, pr.getId_Category());
				ps.setInt(4, pr.getQuantity());
				ps.setInt(5, pr.getId());
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
