DECLARE @ConstraintName_Regon NVARCHAR(128);
DECLARE @TableName_Company NVARCHAR(128);
DECLARE @SqlCommand_Company NVARCHAR(MAX);

SELECT @ConstraintName_Regon = CONSTRAINT_NAME,
       @TableName_Company = TABLE_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'Company'
  AND COLUMN_NAME = 'Regon';

SET @SqlCommand_Company = 'ALTER TABLE ' + QUOTENAME(@TableName_Company) +
                          ' DROP CONSTRAINT ' + QUOTENAME(@ConstraintName_Regon);

EXEC sp_executesql @SqlCommand_Company;

DECLARE @ConstraintName NVARCHAR(128);
DECLARE @TableName NVARCHAR(128);
DECLARE @SqlCommand NVARCHAR(MAX);

SELECT @ConstraintName = CONSTRAINT_NAME,
       @TableName = TABLE_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'ContactPerson'
  AND COLUMN_NAME = 'EmailAddress';

SET @SqlCommand = 'ALTER TABLE ' + QUOTENAME(@TableName) +
                  ' DROP CONSTRAINT ' + QUOTENAME(@ConstraintName);

EXEC sp_executesql @SqlCommand;

CREATE UNIQUE NONCLUSTERED INDEX UQ_ContactPerson_Email
    ON ContactPerson (emailAddress)
    WHERE emailAddress IS NOT NULL;

CREATE UNIQUE NONCLUSTERED INDEX UQ_Company_Regon
    ON Company (Regon)
    WHERE Regon IS NOT NULL;
