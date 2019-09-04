package entities;

public class User
{
	private int id;
	private String name;
	private String email;
	private String password;
	private String address;
	private boolean isAdmin;

	public User( int id, String name, String email, String password, String address, boolean isAdmin )
	{
		setId(id);
		setName(name);
		setEmail(email);
		setPassword(password);
		setAddress(address);
		setAdmin(isAdmin);
	}

	public User()
	{
		id = 0;
		name = null;
		email = null;
		password = null;
		address = null;
		isAdmin = false;
	}

	public int getId( )
	{
		return id;
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public String getName( )
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = null;

		if (name != null)
		{
			this.name = name.trim();
		}
	}

	public String getEmail( )
	{
		return email;
	}

	public void setEmail( String email )
	{
		this.email = null;

		if (email != null)
		{
			this.email = email.trim().toLowerCase();
		}
	}

	public String getPassword( )
	{
		return password;
	}

	public void setPassword( String password )
	{
		this.password = null;

		if (password != null)
		{
			this.password = password.trim();
		}
	}

	public String getAddress( )
	{
		return address;
	}

	public void setAddress( String address )
	{
		this.address = null;

		if (address != null)
		{
			this.address = address.trim();
		}
	}

	public boolean isAdmin( )
	{
		return isAdmin;
	}

	public void setAdmin( boolean isAdmin )
	{
		this.isAdmin = isAdmin;
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

		User other = (User) obj;
		if (id != other.id)
			return false;

		return true;
	}

	@Override
	public String toString( )
	{
		return "Users [Name=" + name + ", Email=" + email + ", Password=" + password + ", Address=" + address + ", isAdmin=" + isAdmin + "]";
	}
}
