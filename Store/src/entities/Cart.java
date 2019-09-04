package entities;

public class Cart
{
	public static final int OPEN = 1;
	public static final int CLOSED = 2;
	public static final int CANCELLED = 3;
	
	private int id;
	private int id_user;
	private int Status;

	public Cart( int id, int id_user, int status )
	{
		super();
		this.id = id;
		this.id_user = id_user;
		Status = status;
	}

	public Cart()
	{
		super();
		this.id = 0;
		this.id_user = 0;
		Status = -1;
	}

	public int getId( )
	{
		return id;
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public int getId_user( )
	{
		return id_user;
	}

	public void setId_user( int id_user )
	{
		this.id_user = id_user;
	}

	public int getStatus( )
	{
		return Status;
	}

	public void setStatus( int status )
	{
		Status = status;
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
		Cart other = (Cart) obj;
		if (id != other.id)
			return false;
		if (id_user != other.id_user)
			return false;
		return true;
	}

	@Override
	public String toString( )
	{
		return "Carts [id=" + id + ", id_user=" + id_user + ", Status=" + Status + "]";
	}

}