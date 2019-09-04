package app;

import java.sql.*;

import entities.*;

public class AppContext
{
	private Connection connection;
	private User user;

	public AppContext( Connection connection )
	{
		this.connection = connection;
		user = null;
	}

	public Connection getConnection( )
	{
		return connection;
	}

	public void setConnection( Connection connection )
	{
		this.connection = connection;
	}

	public User getUser( )
	{
		return user;
	}

	public void setUser( User user )
	{
		this.user = user;
	}
}
