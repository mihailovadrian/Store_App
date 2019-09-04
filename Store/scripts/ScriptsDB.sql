drop table productrequests;
drop table productattributes;
drop table cartproducts;
drop table products;
drop table attributevalues;
drop table attributes;
drop table categories;
drop table carts;
drop table users;



CREATE TABLE Categories (
	ID_Category int IDENTITY(1,1) NOT NULL,
	Name varchar(200) NOT NULL UNIQUE,
	Description varchar(2000),
	PathToImage varchar(500),
  CONSTRAINT PK_CATEGORIES PRIMARY KEY (ID_Category)
);

CREATE TABLE ProductRequests (
	ID_ProdReq int IDENTITY(1,1) NOT NULL,
	ID_Product int NOT NULL,
	ID_Cart int NOT NULL,
	Entry_DateTime datetime NOT NULL,
	Quantity int NOT NULL,
  CONSTRAINT PK_PRODUCTREQUESTS PRIMARY KEY  (ID_ProdReq)
);

CREATE TABLE Products (
	ID_Product int IDENTITY(1,1) NOT NULL,
	Name varchar(200) NOT NULL UNIQUE,
	Description varchar(2000),
	ID_Category int NOT NULL UNIQUE,
	Quantity int NOT NULL,
  CONSTRAINT PK_PRODUCTS PRIMARY KEY  (ID_Product)
);

CREATE TABLE Users (
	ID_User int IDENTITY(1,1) NOT NULL,
	Name varchar(200) NOT NULL,
	Email varchar(200) NOT NULL,
	Password varchar(200) NOT NULL,
	Address varchar(200),
	IsAdmin int NOT NULL,
  CONSTRAINT PK_USERS PRIMARY KEY  (ID_User)
);

CREATE TABLE Carts (
	ID_Cart int IDENTITY(1,1) NOT NULL,
	ID_User int NOT NULL,
	Status int NOT NULL,
  CONSTRAINT PK_CARTS PRIMARY KEY  (ID_Cart)
);

CREATE TABLE CartProducts (
	ID_CartProduct int IDENTITY(1,1) NOT NULL,
	ID_Cart int NOT NULL,
	ID_Product int NOT NULL,
	Entry_DateTime datetime NOT NULL,
	Quantity int NOT NULL,
  CONSTRAINT PK_CARTPRODUCTS PRIMARY KEY  (ID_CartProduct)
);

CREATE TABLE Attributes (
	ID_Attribute int IDENTITY(1,1) NOT NULL,
	Name varchar(200) NOT NULL,
	Description varchar(2000),
	ID_Category int NOT NULL,
	ValueType int NOT NULL,
	IsList int NOT NULL,
	IsMandatory int NOT NULL,
  CONSTRAINT PK_ATTRIBUTES PRIMARY KEY  (ID_Attribute)
);

CREATE TABLE ProductAttributes (
	ID_ProdAttr int IDENTITY(1,1) NOT NULL,
	ID_Product int NOT NULL,
	ID_Attribute int NOT NULL,
	ID_AttrVal int NOT NULL,
	Value varchar,
  CONSTRAINT PK_PRODUCTATTRIBUTES PRIMARY KEY  (ID_ProdAttr)
);

CREATE TABLE AttributeValues (
	ID_AttrVal int IDENTITY(1,1) NOT NULL,
	ID_Attribute int NOT NULL,
	Value varchar(200) NOT NULL,
  CONSTRAINT PK_ATTRIBUTEVALUES PRIMARY KEY  (ID_AttrVal)
);


ALTER TABLE ProductRequests WITH CHECK ADD CONSTRAINT ProductRequests_fk0 FOREIGN KEY (ID_Product) REFERENCES Products(ID_Product);
ALTER TABLE ProductRequests WITH CHECK ADD CONSTRAINT ProductRequests_fk1 FOREIGN KEY (ID_Cart) REFERENCES Carts(ID_Cart);

ALTER TABLE Products WITH CHECK ADD CONSTRAINT Products_fk0 FOREIGN KEY (ID_Category) REFERENCES Categories(ID_Category);

ALTER TABLE Carts WITH CHECK ADD CONSTRAINT Carts_fk0 FOREIGN KEY (ID_User) REFERENCES Users(ID_User);

ALTER TABLE CartProducts WITH CHECK ADD CONSTRAINT CartProducts_fk0 FOREIGN KEY (ID_Cart) REFERENCES Carts(ID_Cart);
ALTER TABLE CartProducts WITH CHECK ADD CONSTRAINT CartProducts_fk1 FOREIGN KEY (ID_Product) REFERENCES Products(ID_Product);

ALTER TABLE Attributes WITH CHECK ADD CONSTRAINT Attributes_fk0 FOREIGN KEY (ID_Category) REFERENCES Categories(ID_Category);

ALTER TABLE ProductAttributes WITH CHECK ADD CONSTRAINT ProductAttributes_fk0 FOREIGN KEY (ID_Product) REFERENCES Products(ID_Product);
ALTER TABLE ProductAttributes WITH CHECK ADD CONSTRAINT ProductAttributes_fk1 FOREIGN KEY (ID_Attribute) REFERENCES Attributes(ID_Attribute);
ALTER TABLE ProductAttributes WITH CHECK ADD CONSTRAINT ProductAttributes_fk2 FOREIGN KEY (ID_AttrVal) REFERENCES AttributeValues(ID_AttrVal);

ALTER TABLE AttributeValues WITH CHECK ADD CONSTRAINT AttributeValues_fk0 FOREIGN KEY (ID_Attribute) REFERENCES Attributes(ID_Attribute);
