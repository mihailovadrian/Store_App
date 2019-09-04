package dbTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.AppContext;
import entities.Attribute;

public class DBOperationsAttribute
{

	static protected Attribute readAttribute( ResultSet rs, Attribute atr )
	{
		Attribute result = null;

		try
		{
			if (rs != null)
			{
				if (atr == null)
				{
					atr = new Attribute();
				}

				atr.setId_Attribute(rs.getInt("id_attribute"));
				atr.setName(rs.getString("name"));
				atr.setDescription(rs.getString("description"));
				atr.setId_Category(rs.getInt("id_category"));
				atr.setValueType(rs.getInt("valuetype"));
				atr.setList(rs.getInt("islist") == 1);
				atr.setMandatory(rs.getInt("ismandatory") == 1);

				result = atr;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	static public Attribute getAttribute( AppContext context, int id )
	{
		Attribute result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select * from attributes where id_attribute= ?";

		try
		{
			ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next())
			{
				result = readAttribute(rs, null);
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

	static public int addAttribute( AppContext context, Attribute atr )
	{
		int result = 0;

		if (atr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = " insert attributes (name, description, id_category,valuetype,islist,ismandatory)  values (?, ?, ?, ?, ?, ?)"; //OUTPUT Inserted.ID_USER

			try
			{
				ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				ps.setString(1, atr.getName());
				ps.setString(2, atr.getDescription());
				ps.setInt(3, atr.getId_Category());
				ps.setInt(4, atr.getValueType());
				ps.setInt(5, atr.isList() ? 1 : 0);
				ps.setInt(6, atr.isMandatory() ? 1 : 0);

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

	static public List<Attribute> listAttributes( AppContext context, List<Attribute> list, int idFilter )
	{
		List<Attribute> result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Attribute atr;

		String sql = "select * from Attributes ";
		if (idFilter > 0)
		{

			sql = sql + "where id_category = ? ";

		}
		sql = sql + "order by name";

		try
		{
			ps = con.prepareStatement(sql);

			if (idFilter > 0)
			{
				ps.setInt(1, idFilter);
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
				atr = readAttribute(rs, null);
				if (atr != null)
				{
					list.add(atr);
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

	static public void deleteAttribute( AppContext context, Attribute atr )
	{
		if (atr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;

			String sql = "DELETE FROM Attributes WHERE id_attribute=?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setInt(1, atr.getId_Attribute());
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

	static public boolean editAttribute( AppContext context, Attribute atr )
	{
		boolean result = false;

		if (atr != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = "update attributes set name = ?, description = ?, id_category = ? ,valuetype=? ,islist= ? ,ismandatory= ? where id_attribute = ?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setString(1, atr.getName());
				ps.setString(2, atr.getDescription());
				ps.setInt(3, atr.getId_Category());
				ps.setInt(4, atr.getValueType());
				ps.setInt(5, atr.isList() ? 1 : 0);
				ps.setInt(6, atr.isMandatory() ? 1 : 0);
				ps.setInt(7, atr.getId_Attribute());
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
