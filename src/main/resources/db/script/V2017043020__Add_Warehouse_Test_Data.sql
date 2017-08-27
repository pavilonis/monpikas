INSERT INTO Supplier (id, code, name, dateCreated) VALUES (1, 'M1', 'Maxima', CURRENT_TIMESTAMP);
INSERT INTO Supplier (id, code, name, dateCreated) VALUES (2, 'N1', 'Norfa', CURRENT_TIMESTAMP);
INSERT INTO Supplier (id, code, name, dateCreated) VALUES (3, 'R1', 'Rimi', CURRENT_TIMESTAMP);
INSERT INTO ProductGroup (id, name, kcal100, dateCreated) VALUES (1, 'Makaronai', 350, CURRENT_TIMESTAMP);
INSERT INTO ProductGroup (id, name, kcal100, dateCreated) VALUES (2, 'Grikiai', 350, CURRENT_TIMESTAMP);
INSERT INTO ProductGroup (id, name, kcal100, dateCreated) VALUES (3, 'Aliejus', 900, CURRENT_TIMESTAMP);
INSERT INTO ProductGroup (id, name, kcal100, dateCreated) VALUES (4, 'Bananai', 150, CURRENT_TIMESTAMP);
INSERT INTO ProductGroup (id, name, kcal100, dateCreated) VALUES (5, 'Bulvės', 120, CURRENT_TIMESTAMP);
INSERT INTO ProductGroup (id, name, kcal100, dateCreated) VALUES (6, 'Burokėliai', 130, CURRENT_TIMESTAMP);
INSERT INTO ProductGroup (id, name, kcal100, dateCreated) VALUES (7, 'Klauliena', 250, CURRENT_TIMESTAMP);
INSERT INTO ProductGroup (id, name, kcal100, dateCreated) VALUES (8, 'Pienas', 42, CURRENT_TIMESTAMP);
INSERT INTO ProductGroup (id, name, kcal100, dateCreated) VALUES (9, 'Kava', 1, CURRENT_TIMESTAMP);
INSERT INTO ProductGroup (id, name, kcal100, dateCreated) VALUES (10, 'Cukrus', 385, CURRENT_TIMESTAMP);

INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (1, 'Gintariniai', 'GRAM', 500, 1, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (2, 'Optima Linija', 'GRAM', 400, 1, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (3, 'FASMA', 'GRAM', 800, 2, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (4, 'Skanėja', 'GRAM', 800, 2, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (5, 'Rapsų aliejus', 'GRAM', 1000, 3, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (6, 'Saulegražų aliejus', 'GRAM', 1000, 3, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (7, 'Geltoni bananai', 'GRAM', 1000, 4, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (8, 'Žali bananai', 'GRAM', 1000, 4, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (9, 'Šviežios bulvės', 'GRAM', 1000, 5, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (10, 'Extra burokeliai', 'GRAM', 1000, 6, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (11, 'Atšaldyta kiauliena', 'GRAM', 1000, 7, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (12, 'Kava Jacobs', 'GRAM', 500, 9, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (13, 'Marijampolės pienas', 'GRAM', 900, 8, CURRENT_TIMESTAMP);
INSERT INTO Product (id, name, measureUnit, unitWeight, productGroup_id, dateCreated)
VALUES (14, 'Panevežio cukrus', 'GRAM', 1000, 10, CURRENT_TIMESTAMP);

INSERT INTO TechCardGroup (id, name, dateCreated) VALUES (1, 'Sriubos', CURRENT_TIMESTAMP);
INSERT INTO TechCardGroup (id, name, dateCreated) VALUES (2, 'Košės', CURRENT_TIMESTAMP);
INSERT INTO TechCardGroup (id, name, dateCreated) VALUES (3, 'Salotos', CURRENT_TIMESTAMP);
INSERT INTO TechCardGroup (id, name, dateCreated) VALUES (4, 'Vaisiai', CURRENT_TIMESTAMP);
INSERT INTO TechCardGroup (id, name, dateCreated) VALUES (5, 'Desertas', CURRENT_TIMESTAMP);
INSERT INTO TechCardGroup (id, name, dateCreated) VALUES (6, 'Gerimai', CURRENT_TIMESTAMP);
INSERT INTO TechCardGroup (id, name, dateCreated) VALUES (7, 'Karšti patiekalai', CURRENT_TIMESTAMP);

INSERT INTO TechCard (id, name, techCardGroup_id, dateCreated) VALUES (1, 'Barščiai', 1, CURRENT_TIMESTAMP);
INSERT INTO TechCardProduct (techCard_id, productGroup_id, outputWeight, dateCreated)
VALUES (1, 5, 50, CURRENT_TIMESTAMP);
INSERT INTO TechCardProduct (techCard_id, productGroup_id, outputWeight, dateCreated)
VALUES (1, 6, 40, CURRENT_TIMESTAMP);
INSERT INTO TechCardProduct (techCard_id, productGroup_id, outputWeight, dateCreated)
VALUES (1, 7, 30, CURRENT_TIMESTAMP);

INSERT INTO TechCard (id, name, techCardGroup_id, dateCreated) VALUES (2, 'Makaronai su kotletu', 7, CURRENT_TIMESTAMP);
INSERT INTO TechCardProduct (techCard_id, productGroup_id, outputWeight, dateCreated)
VALUES (2, 1, 60, CURRENT_TIMESTAMP);
INSERT INTO TechCardProduct (techCard_id, productGroup_id, outputWeight, dateCreated)
VALUES (2, 7, 30, CURRENT_TIMESTAMP);
INSERT INTO TechCardProduct (techCard_id, productGroup_id, outputWeight, dateCreated)
VALUES (2, 3, 5, CURRENT_TIMESTAMP);

INSERT INTO TechCard (id, name, techCardGroup_id, dateCreated) VALUES (3, 'Kava su pienu', 6, CURRENT_TIMESTAMP);
INSERT INTO TechCardProduct (techCard_id, productGroup_id, outputWeight, dateCreated)
VALUES (3, 8, 50, CURRENT_TIMESTAMP);
#pienas
INSERT INTO TechCardProduct (techCard_id, productGroup_id, outputWeight, dateCreated)
VALUES (3, 9, 5, CURRENT_TIMESTAMP);
#kava
INSERT INTO TechCardProduct (techCard_id, productGroup_id, outputWeight, dateCreated)
VALUES (3, 10, 5, CURRENT_TIMESTAMP);
#cukrus

INSERT INTO TechCardSetType (name, dateCreated) VALUES
   ('Pusryčiai', CURRENT_TIMESTAMP),
   ('Priešpiečiai', CURRENT_TIMESTAMP),
   ('Pietus', CURRENT_TIMESTAMP),
   ('Vakariene ir pavakariai', CURRENT_TIMESTAMP);

INSERT INTO Receipt (id, supplier_id, dateCreated, dateCreated)
VALUES (1, 1, NOW() - INTERVAL 6 DAY, CURRENT_TIMESTAMP);
INSERT INTO ReceiptItem (id, receipt_id, product_id, unitPrice, quantity, productNameSnapshot, productMeasureUnitSnapshot, productUnitWeightSnapshot, dateCreated)
VALUES (1, 1, 2, 0.45, 15, (SELECT name
                            FROM Product
                            WHERE id = 2), (SELECT measureUnit
                                            FROM Product
                                            WHERE id = 2), (SELECT unitWeight
                                                            FROM Product
                                                            WHERE id = 2), CURRENT_TIMESTAMP);
INSERT INTO ReceiptItem (id, receipt_id, product_id, unitPrice, quantity, productNameSnapshot, productMeasureUnitSnapshot, productUnitWeightSnapshot, dateCreated)
VALUES (2, 1, 9, 0.8, 8, (SELECT name
                          FROM Product
                          WHERE id = 9), (SELECT measureUnit
                                          FROM Product
                                          WHERE id = 9), (SELECT unitWeight
                                                          FROM Product
                                                          WHERE id = 9), CURRENT_TIMESTAMP);

INSERT INTO Receipt (id, supplier_id, dateCreated, dateCreated)
VALUES (2, 2, NOW() - INTERVAL 4 DAY, CURRENT_TIMESTAMP);
INSERT INTO ReceiptItem (id, receipt_id, product_id, unitPrice, quantity, productNameSnapshot, productMeasureUnitSnapshot, productUnitWeightSnapshot, dateCreated)
VALUES (3, 2, 6, 1.17, 8, (SELECT name
                           FROM Product
                           WHERE id = 6), (SELECT measureUnit
                                           FROM Product
                                           WHERE id = 6), (SELECT unitWeight
                                                           FROM Product
                                                           WHERE id = 6), CURRENT_TIMESTAMP);

INSERT INTO Receipt (id, supplier_id, dateCreated, dateCreated)
VALUES (3, 3, NOW() - INTERVAL 2 DAY, CURRENT_TIMESTAMP);
INSERT INTO ReceiptItem (id, receipt_id, product_id, unitPrice, quantity, productNameSnapshot, productMeasureUnitSnapshot, productUnitWeightSnapshot, dateCreated)
VALUES (4, 3, 11, 3.65, 5.5, (SELECT name
                              FROM Product
                              WHERE id = 11), (SELECT measureUnit
                                               FROM Product
                                               WHERE id = 11), (SELECT unitWeight
                                                                FROM Product
                                                                WHERE id = 11), CURRENT_TIMESTAMP);
INSERT INTO ReceiptItem (id, receipt_id, product_id, unitPrice, quantity, productNameSnapshot, productMeasureUnitSnapshot, productUnitWeightSnapshot, dateCreated)
VALUES (5, 3, 3, 1, 2, (SELECT name
                        FROM Product
                        WHERE id = 3), (SELECT measureUnit
                                        FROM Product
                                        WHERE id = 3), (SELECT unitWeight
                                                        FROM Product
                                                        WHERE id = 3), CURRENT_TIMESTAMP);
INSERT INTO ReceiptItem (id, receipt_id, product_id, unitPrice, quantity, productNameSnapshot, productMeasureUnitSnapshot, productUnitWeightSnapshot, dateCreated)
VALUES (6, 3, 5, 1.3, 3, (SELECT name
                          FROM Product
                          WHERE id = 5), (SELECT measureUnit
                                          FROM Product
                                          WHERE id = 5), (SELECT unitWeight
                                                          FROM Product
                                                          WHERE id = 5), CURRENT_TIMESTAMP);

INSERT INTO Receipt (id, supplier_id, dateCreated, dateCreated) VALUES (4, 2, NOW(), CURRENT_TIMESTAMP);
INSERT INTO ReceiptItem (id, receipt_id, product_id, unitPrice, quantity, productNameSnapshot, productMeasureUnitSnapshot, productUnitWeightSnapshot, dateCreated)
VALUES (7, 4, 12, 2.5, 2, (SELECT name
                           FROM Product
                           WHERE id = 12), (SELECT measureUnit
                                            FROM Product
                                            WHERE id = 12), (SELECT unitWeight
                                                             FROM Product
                                                             WHERE id = 12), CURRENT_TIMESTAMP);
INSERT INTO ReceiptItem (id, receipt_id, product_id, unitPrice, quantity, productNameSnapshot, productMeasureUnitSnapshot, productUnitWeightSnapshot, dateCreated)
VALUES (8, 4, 13, 1, 5, (SELECT name
                         FROM Product
                         WHERE id = 13), (SELECT measureUnit
                                          FROM Product
                                          WHERE id = 13), (SELECT unitWeight
                                                           FROM Product
                                                           WHERE id = 13), CURRENT_TIMESTAMP);

INSERT INTO MenuRequirement (id, date, dateCreated) VALUES (1, CURDATE(), CURRENT_TIMESTAMP);
INSERT INTO TechCardSet (id, name, techCardSetType_id, dateCreated) VALUES (1, 'Pigus pusryčiai', 1, CURRENT_TIMESTAMP);
#pusryciai
INSERT INTO TechCardSetTechCard (techCardSet_id, techCard_id, dateCreated) VALUES (1, 1, CURRENT_TIMESTAMP);
#barsciai
INSERT INTO TechCardSetTechCard (techCardSet_id, techCard_id, dateCreated) VALUES (1, 2, CURRENT_TIMESTAMP);
#makaronai + kotletas

INSERT INTO MenuRequirement (id, date, dateCreated) VALUES (2, CURDATE(), CURRENT_TIMESTAMP);
INSERT INTO TechCardSet (id, name, techCardSetType_id, dateCreated) VALUES (2, 'Kava', 2, CURRENT_TIMESTAMP);
#priespeciai
INSERT INTO TechCardSetTechCard (techCardSet_id, techCard_id, dateCreated) VALUES (2, 3, CURRENT_TIMESTAMP);
#kava su pienu
