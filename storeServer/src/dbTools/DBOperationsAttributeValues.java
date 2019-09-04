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

public class DBOperationsAttributeValues
{
	static protected AttributeValues readValue( ResultSet rs, AttributeValues value )
	{
		AttributeValues result = null;

		try
		{
			if (rs != null)
			{
				if (value == null)
				{
					value = new AttributeValues();
				}

				value.setId(rs.getInt("id_attrval"));
				value.setId_Attribute(rs.getInt("id_attribute"));
				value.setValue(rs.getString("value"));

				result = value;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	static public AttributeValues getValue( AppContext context, int id )
	{
		AttributeValues result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select * from AttributeValues where id_AttrVal = ?";

		try
		{
			ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next())
			{
				result = readValue(rs, null);
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

	static public void deleteValue( AppContext context, AttributeValues value )
	{
		if (value != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;

			String sql = "DELETE FROM AttributeValues WHERE id_attrval=?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setInt(1, value.getId());
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

	static public int addValue( AppContext context, AttributeValues value )
	{
		int result = 0;

		if (value != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = " insert attributeValues (id_attribute, value)  values (?, ?)";

			try
			{
				ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				ps.setInt(1, value.getId_Attribute());
				ps.setString(2, value.getValue());
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

	static public boolean editAttributeValue( AppContext context, AttributeValues value )
	{
		boolean result = false;

		if (value != null)
		{
			Connection con = context.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = "update attributeValues set id_attribute = ?, value = ? where id_attrval = ?";

			try
			{
				ps = con.prepareStatement(sql);

				ps.setInt(1, value.getId_Attribute());
				ps.setString(2, value.getValue());
				ps.setInt(3, value.getId());

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

	static public List<AttributeValues> listValues( AppContext context, List<AttributeValues> list, int idAttr )
	{
		List<AttributeValues> result = null;

		Connection con = context.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		AttributeValues value;

		String sql = "select * from attributeValues where id_attribute=? ";
		try
		{
			ps = con.prepareStatement(sql);
			ps.setInt(1, idAttr);
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
				value = readValue(rs, null);
				if (value != null)
				{
					list.add(value);
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
