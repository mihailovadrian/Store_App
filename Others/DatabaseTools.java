package tools;

import java.sql.*;
import java.util.*;
import org.apache.log4j.*;

import objects.*;

public class DatabaseTools
{

	// ~ Static fields/initializers -----------------------------------------------------------------

	public static Logger logger = Logger.getLogger(DatabaseTools.class.getName());

	// ~ Methods ------------------------------------------------------------------------------------

	public static String prepareName( String name )
	{
		String result = name;

		if (result != null)
		{
			result = result.trim().toUpperCase();
			if (result.length() == 0)
			{
				result = null;
			}
		}

		return result;
	}

	public static String prepareAlias( String alias )
	{
		String result = alias;

		if (result != null)
		{
			result = result.trim().toUpperCase();
		}
		else
		{
			result = "";
		}

		return result;
	}

	public static Vector<String> getFieldValuesAsList( Connection connection, String sql, String field )
	{

		Vector<String> result = null;

		if (connection != null)
		{

			Statement s = null;
			ResultSet rs = null;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery(sql);
					if (rs != null)
					{
						result = new Vector<String>();
						while ((rs.next()) && (result != null))
						{
							result.add(rs.getString(field));
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = null;
				logger.error("Error: " + e.getMessage());
			}
		}

		return result;
	}

	public static Vector<String> getFieldValuesAsList( Connection connection, String sql, String field1, String field2 )
	{

		Vector<String> result = null;

		if (connection != null)
		{

			Statement s = null;
			ResultSet rs = null;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery(sql);
					if (rs != null)
					{
						result = new Vector<String>();
						while ((rs.next()) && (result != null))
						{
							result.add(rs.getString(field1));
							result.add(rs.getString(field2));
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = null;
				logger.error("Error: " + e.getMessage());
			}
		}

		return result;
	}

	// returns the list with the values of a field from the records selected with a SQL statement
	public static Vector<String> getFieldsValuesAsList( Connection connection, String sql, Vector<String> fields )
	{

		Vector<String> result = null;

		if ((connection != null) && (fields != null))
		{

			Statement s = null;
			ResultSet rs = null;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery(sql);
					if (rs != null)
					{
						result = new Vector<String>();
						while ((rs.next()) && (result != null))
						{
							for (int i = 0; i < fields.size(); i++)
							{
								result.add(rs.getString(fields.get(i)));
							}
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = null;
				logger.error("Error: " + e.getMessage());
			}
		}

		return result;
	}

	public static boolean executeStoredProc( Connection connection, String spname )
	{

		boolean result = false;

		CallableStatement s = null;
		if (connection != null)
		{
			try
			{
				s = connection.prepareCall("{ call " + spname + "() }");

				s.execute();

				result = true;

				logger.info("The stored procedure \"" + spname + "\" has run succesfully!");
			}
			catch (Exception e)
			{
				result = false;
				logger.error("The stored procedure: " + spname + "\nError: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.error("Cannot close the statement: " + e.getMessage());
			}
		}

		return result;
	}

	public static boolean executeSQL( Connection connection, String sql )
	{
		return executeSQL(connection, sql, -1);
	}

	public static boolean executeSQL( Connection connection, String sql, int expected_rows_affected )
	{

		boolean result = false;

		int ra;

		Statement s = null;
		if (connection != null)
		{
			try
			{
				System.out.println("SQL: " + sql);

				s = connection.createStatement();
				ra = s.executeUpdate(sql);
				if ((expected_rows_affected < 0) || (expected_rows_affected == ra))
				{
					result = true;
				}

				s.close();

				if (result)
				{
					connection.commit();
				}
				else
				{
					connection.rollback();
				}
			}
			catch (Exception e)
			{
				try
				{
					connection.rollback();
				}
				catch (Exception er)
				{
					logger.fatal("Error when rollback: " + er.getMessage());
				}

				result = false;

				logger.fatal("The SQL statement: " + sql + "\nError: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.error("Cannot close the cursor: " + e.getMessage());
			}
		}

		return result;
	}

	public static boolean executeSQL( Connection connection, String sql, boolean show_unique_err_msg )
	{

		boolean result = false;

		Statement s = null;
		if (connection != null)
		{
			try
			{
				s = connection.createStatement();
				s.executeUpdate(sql);

				result = true;

				s.close();

				if (result)
				{
					connection.commit();
				}
				else
				{
					connection.rollback();
				}
			}
			catch (Exception e)
			{
				try
				{
					connection.rollback();
				}
				catch (Exception er)
				{

					String res = er.getMessage();

					if ((show_unique_err_msg) || (res.indexOf("unique") < 0))
					{
						logger.fatal("Error when rollback: " + er.getMessage());
					}
				}

				result = false;

				String es = e.getMessage();

				if ((show_unique_err_msg) || (es.indexOf("unique") < 0))
				{
					logger.fatal("The SQL statement: " + sql + "\nError: " + e.getMessage());
				}
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.error("Cannot close the cursor: " + e.getMessage());
			}
		}

		return result;
	}

	public static String getString( Connection connection, String sql, String field, String defval )
	{

		String result = defval;

		if (connection != null)
		{

			Statement s = null;
			ResultSet rs = null;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery(sql);
					if (rs != null)
					{
						if (rs.next())
						{
							result = rs.getString(field);
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = defval;

				logger.fatal("Error: " + e.getMessage() + "\nSQL: " + sql);
			}

			try
			{
				if (rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the result set: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the statement: " + e.getMessage());
			}
		}

		return result;
	}

	public static String getString( Connection connection, String sql, String field )
	{
		return getString(connection, sql, field, null);
	}

	public static long getLong( Connection connection, String sql, String field, int defval )
	{

		long result = defval;

		if (connection != null)
		{

			Statement s = null;
			ResultSet rs = null;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery(sql);
					if (rs != null)
					{
						if (rs.next())
						{
							result = rs.getLong(field);
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = defval;

				logger.fatal("Error: " + e.getMessage() + "\nSQL: " + sql);
			}

			try
			{
				if (rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the result set: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the statement: " + e.getMessage());
			}
		}

		return result;
	}

	public static int getInt( Connection connection, String sql, String field, int defval )
	{

		int result = defval;

		if (connection != null)
		{

			Statement s = null;
			ResultSet rs = null;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery(sql);
					if (rs != null)
					{
						if (rs.next())
						{
							result = rs.getInt(field);
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = defval;

				logger.fatal("Error: " + e.getMessage() + "\nSQL: " + sql);
			}

			try
			{
				if (rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the result set: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the statement: " + e.getMessage());
			}
		}

		return result;
	}

	public static int getInt( Connection connection, String sql, String field )
	{
		return getInt(connection, sql, field, Integer.MIN_VALUE);
	}

	public static long getLong( Connection connection, String sql, String field, long defval )
	{

		long result = defval;

		if (connection != null)
		{

			Statement s = null;
			ResultSet rs = null;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery(sql);
					if (rs != null)
					{
						if (rs.next())
						{
							result = rs.getLong(field);
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = defval;

				logger.fatal("Error: " + e.getMessage() + "\nSQL: " + sql);
			}

			try
			{
				if (rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the result set: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the statement: " + e.getMessage());
			}
		}

		return result;
	}

	public static long getLong( Connection connection, String sql, String field )
	{
		return getLong(connection, sql, field, Long.MIN_VALUE);
	}

	public static java.util.Date getDate( Connection connection, String sql, String field, java.util.Date defval )
	{

		java.util.Date result = defval;

		if (connection != null)
		{

			Statement s = null;
			ResultSet rs = null;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery(sql);
					if (rs != null)
					{
						if (rs.next())
						{
							// result = DateTools.readDate( rs, field );
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = defval;

				logger.fatal("Error: " + e.getMessage() + "\nSQL: " + sql);
			}

			try
			{
				if (rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the result set: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the statement: " + e.getMessage());
			}
		}

		return result;
	}

	public static java.util.Date getDate( Connection connection, String sql, String field )
	{
		return getDate(connection, sql, field, null);
	}

	public static java.util.Date getDBCurrentDate( Connection connection )
	{
		return getDate(connection, "select SYSDATE as cdate from dual", "cdate", null);
	}

	public static boolean getBoolean( ResultSet rs, String columnName, boolean defaultValue )
	{
		boolean result = false;

		if (rs != null)
		{
			try
			{
				if (rs.getInt(columnName) > 0)
				{

				}
			}
			catch (Exception e)
			{
				result = defaultValue;

				logger.error(e);
			}
		}

		return result;
	}

	public static Record getRecord( Connection connection, Entity entity, long pkID, String preffix )
	{
		Record result = null;

		if (entity != null)
		{
			result = new Record(entity);

			if (!getRecord(connection, entity, pkID, result, preffix))
			{
				result = null;
			}
		}

		return result;
	}

	public static boolean getRecord( Connection connection, Entity entity, long pkID, Record record, String preffix )
	{
		boolean result = false;

		if ((entity != null) && (entity.getPkFields().size() == 1) && (entity.getPkFields().get(0).getDbTypeCode() == EntityField.INT))
		{
			result = getRecord(connection, entity.getDbName(), entity.getFieldsForSQL(), entity.getPkFields().get(0).getDbName(), pkID, record, preffix);
		}

		return result;
	}

	public static boolean getRecord( Connection connection, String tableName, String fieldList, String pkFieldName, long pkID, Record record, String preffix )
	{
		boolean result = false;

		if (connection != null)
		{

			Statement s = null;
			ResultSet rs = null;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery("select " + fieldList + " from " + preffix + tableName + " where " + pkFieldName + "=" + pkID);
					if (rs != null)
					{
						if (rs.next())
						{
							if (record != null)
							{
								record.read(rs);
								result = true;
							}
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = false;

				logger.fatal("Error: " + e.getMessage() + "\nSQL: " + "select * from " + preffix + tableName + " where " + pkFieldName + "=" + pkID);
			}

			try
			{
				if (rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the result set: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the statement: " + e.getMessage());
			}
		}

		return result;
	}

	public static List<Record> getRecords( Connection connection, Entity entity, EntityField field, long id, String preffix )
	{
		List<Record> result = null;

		if (connection != null)
		{
			Statement s = null;
			ResultSet rs = null;
			Record record;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery("select " + entity.getFieldsForSQL() + " from " + preffix + entity.getDbName() + " where " + field.getDbName() + "=" + id);
					if (rs != null)
					{
						while (rs.next())
						{
							record = new Record(entity);
							record.read(rs);

							if (result == null)
							{
								result = new ArrayList<>();
							}

							result.add(record);
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = null;

				logger.fatal("Error: " + e.getMessage() + "\nSQL: " + "select * from " + preffix + entity.getDbName() + " where " + field.getDbName() + "=" + id);
			}

			try
			{
				if (rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the result set: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the statement: " + e.getMessage());
			}
		}

		return result;
	}

	public static int countRecords( Connection connection, Entity entity, EntityField field, long id, String preffix )
	{
		int result = 0;

		if (connection != null)
		{
			Statement s = null;
			ResultSet rs = null;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery("select count(*) as c from " + preffix + entity.getDbName() + " where " + field.getDbName() + "=" + id);
					if (rs != null)
					{
						if (rs.next())
						{
							result = rs.getInt("c");
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = -1;

				logger.fatal("Error: " + e.getMessage() + "\nSQL: " + "select count(*) from " + preffix + entity.getDbName() + " where " + field.getDbName() + "=" + id);
			}

			try
			{
				if (rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the result set: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the statement: " + e.getMessage());
			}
		}

		return result;
	}

	public static RecordSet getRecords( Connection connection, Entity entity, String preffix )
	{
		RecordSet result = new RecordSet(entity);

		if (connection != null)
		{
			Statement s = null;
			ResultSet rs = null;
			Record record;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery("select * from " + preffix + entity.getDbName());
					if (rs != null)
					{
						while (rs.next())
						{
							record = new Record(entity);
							record.read(rs);

							result.add(rs);
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = null;

				logger.fatal("Error: " + e.getMessage() + "\nSQL: " + "select * from " + preffix + entity.getDbName());
			}

			try
			{
				if (rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the result set: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the statement: " + e.getMessage());
			}
		}

		return result;
	}

	public static boolean getRecord( Connection connection, Record record, String preffix )
	{
		boolean result = false;

		if (record != null)
		{
			result = getRecord(connection, record.getEntity(), record.ival(record.getEntity().getPkFields().get(0).getDbName()), record, preffix);
		}

		return result;
	}

	public static Record getRecord( Connection connection, PrimaryKey pk, String preffix )
	{
		Record result = null;

		if ((connection != null) && (pk != null))
		{
			Statement s = null;
			ResultSet rs = null;

			try
			{
				s = connection.createStatement();
				if (s != null)
				{
					rs = s.executeQuery(pk.getSelect(preffix));
					if (rs != null)
					{
						if (rs.next())
						{
							result = new Record(pk.getEntity());
							result.read(rs);
						}
						rs.close();
					}
					s.close();
				}
			}
			catch (Exception e)
			{
				result = null;

				logger.fatal("Error: " + e.getMessage() + "\nSQL: " + pk.getSelect(preffix));
			}

			try
			{
				if (rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the result set: " + e.getMessage());
			}

			try
			{
				if (s != null)
				{
					s.close();
				}
			}
			catch (Exception e)
			{
				logger.info("Error when closing the statement: " + e.getMessage());
			}
		}

		return result;
	}
}
