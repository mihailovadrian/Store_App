package entities;

public class Categories
{
	private int id;
	private String Name;
	private String Description;
	private String pathToImage;

	public Categories( int id, String name, String description, String pathToImage )
	{
		
		this.id = id;
		this.Name = name;
		this.Description = description;
		this.pathToImage = pathToImage;
	}
	public Categories( )
	{
		
		this.id = 0;
		this.Name = "";
		this.Description = "";
		this.pathToImage = "";
	}

	public int getId( )
	{
		return id;
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
		Categories other = (Categories) obj;
		if (Description == null)
		{
			if (other.Description != null)
				return false;
		}
		else
			if (!Description.equals(other.Description))
				return false;
		if (Name == null)
		{
			if (other.Name != null)
				return false;
		}
		else
			if (!Name.equals(other.Name))
				return false;
		if (id != other.id)
			return false;
		if (pathToImage == null)
		{
			if (other.pathToImage != null)
				return false;
		}
		else
			if (!pathToImage.equals(other.pathToImage))
				return false;
		return true;
	}

	@Override
	public String toString( )
	{
		return "Categories [id=" + id + ", Name=" + Name + ", Description=" + Description + ", pathToImage=" + pathToImage + "]";
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public String getName( )
	{
		return Name;
	}

	public void setName( String name )
	{
		Name = name;
	}

	public String getDescription( )
	{
		return Description;
	}

	public void setDescription( String description )
	{
		Description = description;
	}

	public String getPathToImage( )
	{
		return pathToImage;
	}

	public void setPathToImage( String pathToImage )
	{
		this.pathToImage = pathToImage;
	}

}
