CREATE TABLE Company
(
    CompanyID             UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    CompanyNumber         VARCHAR(50) NOT NULL UNIQUE,
    CountryOfRegistration VARCHAR(100) NOT NULL,
    Name                  VARCHAR(100) NOT NULL,
    AddressID             UNIQUEIDENTIFIER NOT NULL UNIQUE,
    IsActive              BIT NOT NULL DEFAULT 1,
    Regon                 VARCHAR(14) UNIQUE
);

CREATE TABLE ProductVariant
(
    ProductVariantID UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    ProductID        UNIQUEIDENTIFIER NOT NULL,
    Code             INT NOT NULL UNIQUE,
    StockID          UNIQUEIDENTIFIER NOT NULL,
    VariantPrice     DECIMAL(10, 2) NOT NULL,
    IsActive         BIT NOT NULL DEFAULT 1,
    DimensionsID     UNIQUEIDENTIFIER,
    WeightID         UNIQUEIDENTIFIER,
    VariantName      VARCHAR(100)
);

CREATE TABLE Weight
(
    WeightID   UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    UnitWeight VARCHAR(3) CHECK (UnitWeight IN ('MG', 'G', 'KG')) NOT NULL DEFAULT 'KG',
    Amount     FLOAT CHECK (Amount > 0.0) NOT NULL
);

CREATE TABLE Attribute
(
    AttributeName VARCHAR(50) NOT NULL PRIMARY KEY
);

CREATE TABLE Dimensions
(
    DimensionsID   UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    UnitDimensions VARCHAR(3) CHECK (UnitDimensions IN ('MM', 'CM', 'M', 'DM')) NOT NULL DEFAULT 'M',
    Height         FLOAT CHECK (Height > 0.0) NOT NULL,
    Length         FLOAT CHECK (Length > 0.0) NOT NULL,
    Width          FLOAT CHECK (Width > 0.0) NOT NULL
);

CREATE TABLE Person
(
    PersonID    UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    Name        VARCHAR(30) NOT NULL,
    Surname     VARCHAR(30) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Sex         VARCHAR(1) CHECK (Sex IN ('M', 'F', 'O')) NOT NULL
);

CREATE TABLE Category
(
    CategoryID       UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    CompanyID        UNIQUEIDENTIFIER NOT NULL,
    Name             VARCHAR(30) NOT NULL,
    CategoryLevel    SMALLINT NOT NULL DEFAULT 1,
    ParentCategoryID UNIQUEIDENTIFIER
);

CREATE TABLE Customer
(
    CustomerID               UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    ContactPersonID          UNIQUEIDENTIFIER NOT NULL,
    CompanyID                UNIQUEIDENTIFIER NOT NULL,
    DefaultDeliveryAddressID UNIQUEIDENTIFIER
);

CREATE TABLE Employee
(
    EmployeeID      UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    UserID          CHAR(21) NOT NULL UNIQUE,
    ContactPersonID UNIQUEIDENTIFIER NOT NULL UNIQUE,
    PersonID        UNIQUEIDENTIFIER NOT NULL UNIQUE,
    RoleInCompany   VARCHAR(10) CHECK (RoleInCompany IN ('OWNER', 'REGULAR')),
    CompanyID       UNIQUEIDENTIFIER,
    EmploymentDate  DATE
);

CREATE TABLE Stock
(
    StockID         UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    AmountAvailable INT NOT NULL DEFAULT 0 CHECK (AmountAvailable >= 0),
    AlarmingAmount  INT
);

CREATE TABLE Users
(
    UserID     CHAR(21) NOT NULL PRIMARY KEY,
    SystemRole VARCHAR(10) CHECK (SystemRole IN ('USER', 'ADMIN')) NOT NULL DEFAULT 'USER'
);

CREATE TABLE CustomerB2C
(
    CustomerID UNIQUEIDENTIFIER PRIMARY KEY,
    PersonID   UNIQUEIDENTIFIER NOT NULL UNIQUE
);

CREATE TABLE ProductOrderItem
(
    ProductOrderItemID UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    ProductVariantID   UNIQUEIDENTIFIER NOT NULL,
    ProductOrderID     UNIQUEIDENTIFIER NOT NULL,
    UnitPrice          DECIMAL(10, 2) NOT NULL,
    Amount             INT NOT NULL CHECK (Amount > 0),
    ProductName        VARCHAR(100) NOT NULL,
    CONSTRAINT UC_ProductOrderItem UNIQUE (ProductVariantID, ProductOrderID)
);

CREATE TABLE Invoice
(
    InvoiceID       UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    InvoiceNumber   INT IDENTITY(1,1) NOT NULL UNIQUE,
    AmountOnInvoice DECIMAL(10, 2) NOT NULL,
    IssueDate       DATE NOT NULL
);

CREATE TABLE ContactPerson
(
    ContactPersonID UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    ContactNumber   VARCHAR(20) NOT NULL UNIQUE,
    EmailAddress    VARCHAR(100) UNIQUE
);

CREATE TABLE Product
(
    ProductID            UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    CategoryID           UNIQUEIDENTIFIER NOT NULL,
    CompanyID            UNIQUEIDENTIFIER NOT NULL,
    Name                 VARCHAR(100) NOT NULL,
    Price                DECIMAL(10, 2) NOT NULL,
    IsActive             BIT NOT NULL DEFAULT 1,
    MainProductVariantID UNIQUEIDENTIFIER,
    Description          VARCHAR(1000),
    Brand                VARCHAR(50)
);

CREATE TABLE ProductOrder
(
    ProductOrderID UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    Status         VARCHAR(10) CHECK (Status IN ('NEW', 'ORDERED', 'SHIPPED', 'DELIVERED', 'CANCELLED')) NOT NULL DEFAULT 'NEW',
    CustomerID     UNIQUEIDENTIFIER,
    AddressID      UNIQUEIDENTIFIER,
    InvoiceID      UNIQUEIDENTIFIER,
    TrackingNumber VARCHAR(10),
    TimeOfSending  DATETIME,
    TimeOfCreation DATETIME NOT NULL DEFAULT GETDATE()
);

CREATE TABLE CustomerB2B
(
    CustomerID    UNIQUEIDENTIFIER PRIMARY KEY,
    AddressID     UNIQUEIDENTIFIER NOT NULL,
    CompanyNumber VARCHAR(30) NOT NULL UNIQUE,
    NameOnInvoice VARCHAR(255) NOT NULL
);

CREATE TABLE ProductAttribute
(
    ProductAttributeID UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    ProductID          UNIQUEIDENTIFIER NOT NULL,
    AttributeName      VARCHAR(50) NOT NULL,
    Value              VARCHAR(255),
    CONSTRAINT UC_ProductAttribute UNIQUE (ProductID, AttributeName)
);

CREATE TABLE VariantAttribute
(
    VariantAttributeID UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    ProductVariantID   UNIQUEIDENTIFIER NOT NULL,
    AttributeName      VARCHAR(50) NOT NULL,
    Value              VARCHAR(255),
    CONSTRAINT UC_VariantAttribute UNIQUE (ProductVariantID, AttributeName)
);

CREATE TABLE Address
(
    AddressID             UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    City                  VARCHAR(255) NOT NULL,
    Street                VARCHAR(255) NOT NULL,
    StreetNumber          VARCHAR(10) NOT NULL,
    ApartmentNumber       VARCHAR(10) NOT NULL,
    PostalCode            VARCHAR(10) NOT NULL,
    AdditionalInformation VARCHAR(255)
);

CREATE TABLE Invitation
(
    InvitationID UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    CompanyID    UNIQUEIDENTIFIER NOT NULL,
    EmployeeID   UNIQUEIDENTIFIER NOT NULL,
    Token        UNIQUEIDENTIFIER NOT NULL UNIQUE,
    CONSTRAINT UC_Invitation UNIQUE (CompanyID, EmployeeID)
);

ALTER TABLE Category
    ADD CONSTRAINT FK_Category_ParentCategoryID FOREIGN KEY (ParentCategoryID) REFERENCES Category (CategoryID);
ALTER TABLE Category
    ADD CONSTRAINT FK_Category_CompanyID FOREIGN KEY (CompanyID) REFERENCES Company (CompanyID);

ALTER TABLE Company
    ADD CONSTRAINT FK_Company_AddressID FOREIGN KEY (AddressID) REFERENCES Address (AddressID);

ALTER TABLE Customer
    ADD CONSTRAINT FK_Customer_ContactPersonID FOREIGN KEY (ContactPersonID) REFERENCES ContactPerson (ContactPersonID);
ALTER TABLE Customer
    ADD CONSTRAINT FK_Customer_CompanyID FOREIGN KEY (CompanyID) REFERENCES Company (CompanyID);
ALTER TABLE Customer
    ADD CONSTRAINT FK_Customer_DefaultDeliveryAddressID FOREIGN KEY (DefaultDeliveryAddressID) REFERENCES Address (AddressID);

ALTER TABLE CustomerB2B
    ADD CONSTRAINT FK_CustomerB2B_CustomerID FOREIGN KEY (CustomerID) REFERENCES Customer (CustomerID);
ALTER TABLE CustomerB2B
    ADD CONSTRAINT FK_CustomerB2B_AddressID FOREIGN KEY (AddressID) REFERENCES Address (AddressID);

ALTER TABLE CustomerB2C
    ADD CONSTRAINT FK_CustomerB2C_CustomerID FOREIGN KEY (CustomerID) REFERENCES Customer (CustomerID);
ALTER TABLE CustomerB2C
    ADD CONSTRAINT FK_CustomerB2C_PersonID FOREIGN KEY (PersonID) REFERENCES Person (PersonID);

ALTER TABLE ProductVariant
    ADD CONSTRAINT FK_ProductVariant_DimensionsID FOREIGN KEY (DimensionsID) REFERENCES Dimensions (DimensionsID);
ALTER TABLE ProductVariant
    ADD CONSTRAINT FK_ProductVariant_StockID FOREIGN KEY (StockID) REFERENCES Stock (StockID);
ALTER TABLE ProductVariant
    ADD CONSTRAINT FK_ProductVariant_WeightID FOREIGN KEY (WeightID) REFERENCES Weight (WeightID);

ALTER TABLE Employee
    ADD CONSTRAINT FK_Employee_CompanyID FOREIGN KEY (CompanyID) REFERENCES Company (CompanyID);
ALTER TABLE Employee
    ADD CONSTRAINT FK_Employee_ContactPersonID FOREIGN KEY (ContactPersonID) REFERENCES ContactPerson (ContactPersonID);
ALTER TABLE Employee
    ADD CONSTRAINT FK_Employee_PersonID FOREIGN KEY (PersonID) REFERENCES Person (PersonID);
ALTER TABLE Employee
    ADD CONSTRAINT FK_Employee_UserID FOREIGN KEY (UserID) REFERENCES Users (UserID);

ALTER TABLE Invitation
    ADD CONSTRAINT FK_Invitation_CompanyID FOREIGN KEY (CompanyID) REFERENCES Company (CompanyID);
ALTER TABLE Invitation
    ADD CONSTRAINT FK_Invitation_EmployeeID FOREIGN KEY (EmployeeID) REFERENCES Employee (EmployeeID);

ALTER TABLE ProductOrder
    ADD CONSTRAINT FK_ProductOrder_InvoiceID FOREIGN KEY (InvoiceID) REFERENCES Invoice (InvoiceID);

ALTER TABLE Product
    ADD CONSTRAINT FK_Product_CategoryID FOREIGN KEY (CategoryID) REFERENCES Category (CategoryID);
ALTER TABLE Product
    ADD CONSTRAINT FK_Product_CompanyID FOREIGN KEY (CompanyID) REFERENCES Company (CompanyID);
ALTER TABLE Product
    ADD CONSTRAINT FK_Product_MainProductVariantID FOREIGN KEY (MainProductVariantID) REFERENCES ProductVariant (ProductVariantID);

ALTER TABLE ProductAttribute
    ADD CONSTRAINT FK_ProductAttribute_AttributeName FOREIGN KEY (AttributeName) REFERENCES Attribute (AttributeName);
ALTER TABLE ProductAttribute
    ADD CONSTRAINT FK_ProductAttribute_ProductID FOREIGN KEY (ProductID) REFERENCES Product (ProductID);

ALTER TABLE ProductOrder
    ADD CONSTRAINT FK_ProductOrder_CustomerID FOREIGN KEY (CustomerID) REFERENCES Customer (CustomerID);

ALTER TABLE ProductOrderItem
    ADD CONSTRAINT FK_ProductOrderItem_ProductOrderID FOREIGN KEY (ProductOrderID) REFERENCES ProductOrder (ProductOrderID);
ALTER TABLE ProductOrderItem
    ADD CONSTRAINT FK_ProductOrderItem_ProductVariantID FOREIGN KEY (ProductVariantID) REFERENCES ProductVariant (ProductVariantID);

ALTER TABLE VariantAttribute
    ADD CONSTRAINT FK_VariantAttribute_AttributeName FOREIGN KEY (AttributeName) REFERENCES Attribute (AttributeName);
ALTER TABLE VariantAttribute
    ADD CONSTRAINT FK_VariantAttribute_ProductVariantID FOREIGN KEY (ProductVariantID) REFERENCES ProductVariant (ProductVariantID);
