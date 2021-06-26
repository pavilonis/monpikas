-- Deleting duplicate CardID records
DELETE from CMM2.dbo.tb_Cards where Cardcode IN (558,652,1526,381,755,1892);
CREATE INDEX INDEX_ROMCode ON CMM2.dbo.tb_Cards (ROMCode);
CMM2..sp_rename 'KeyLog', 'mm_KeyLog';
CMM2..sp_rename 'ScanLog', 'mm_ScanLog';
ALTER TABLE mm_ScanLog ADD location VARCHAR(255) NULL;
CMM2..sp_rename 'Scanner', 'mm_Scanner';