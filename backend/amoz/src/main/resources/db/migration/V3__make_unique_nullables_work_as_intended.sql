CREATE UNIQUE NONCLUSTERED INDEX UQ_ContactPerson_Email
ON ContactPerson (emailAddress)
WHERE emailAddress IS NOT NULL;

CREATE UNIQUE NONCLUSTERED INDEX UQ_Company_Regon
ON Company (Regon)
WHERE Regon IS NOT NULL;
