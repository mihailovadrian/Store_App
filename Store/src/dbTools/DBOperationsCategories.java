package dbTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.AppContext;
import entities.Categories;

public class DBOperationsCategories
{

	static protected Categories readCategory( ResultSet rs, Categories cat )
	{
		Categories result = null;

		try
		{
			if (rs != null)
			{
				if (cat == null)
				{
					cat = new Categories();
				}

				cat.setId(rs.getInt("id_category"));
				cat.setName(rs.getString("name"));
				cat.setDescription(rs.getString("description"));
				cat.setPathToImage(rs.getString("PathToImage"));

				result = cat;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	static public Categories getCategory( AppContext context, int id )
	{
		Categories result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select * from categories where id_category= ?";

		try
		{
			ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next())
			{
				result = readCategory(rs, null);
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

	static public Categories getCategories( AppContext context, int id )
	{
		Categories result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select * from Categories where id_category = ?";

		try
		{
			ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next())
			{
				result = readCategory(rs, null);
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

	static public int addCategory( AppContext context, Categories cat )
	{
		int result = 0;
		if (cat != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = " insert categories (name, description, pathToImage)  values (?, ?, ?)"; //OUTPUT Inserted.ID_USER

			try
			{
				ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				ps.setString(1, cat.getName());
				ps.setString(2, cat.getDescription());
				ps.setString(3, cat.getPathToImage());

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

	static public List<Categories> listCategories( AppContext context, List<Categories> list, String nameFilter )
	{
		List<Categories> result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Categories cat;

		String sql = "select * from categories ";
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
		sql = sql + "order by name";

		try
		{
			ps = con.prepareStatement(sql);

			if (nameFilter != null)
			{
				ps.setString(1, "%" + nameFilter + "%");
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
				cat = readCategory(rs, null);
				if (cat != null)
				{
					list.add(cat);
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

	static public void deleteCategory( AppContext context, Categories cat )
	{
		if (cat != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;

			String sql = "DELETE FROM categories WHERE id_category=?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setInt(1, cat.getId());
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

	static public boolean editCategory( AppContext context, Categories cat )
	{
		boolean result = false;

		if (cat != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = "update categories set name = ?, description = ?, pathtoimage = ? where id_category = ?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setString(1, cat.getName());
				ps.setString(2, cat.getDescription());
				ps.setString(3, cat.getPathToImage());
				ps.setInt(4, cat.getId());
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
