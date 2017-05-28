-- Deleting duplicate CardID records
DELETE from CMM2.dbo.tb_Cards where Cardcode IN (558,652,1526,381,755,1892);

CREATE INDEX INDEX_ROMCode ON CMM2.dbo.tb_Cards (ROMCode);