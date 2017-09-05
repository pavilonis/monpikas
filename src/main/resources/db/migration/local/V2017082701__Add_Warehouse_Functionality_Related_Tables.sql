CREATE TABLE Supplier (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   code        VARCHAR(255) NOT NULL,
   name        VARCHAR(255) NOT NULL,
   dateCreated DATETIME     NOT NULL,

   UNIQUE (code)
);

CREATE TABLE ProductGroup (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name        VARCHAR(255) NOT NULL,
   kcal100     MEDIUMINT    NOT NULL,
   dateCreated DATETIME     NOT NULL,

   UNIQUE (name)
);

CREATE TABLE Product (
   id              BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name            VARCHAR(255) NOT NULL,
   measureUnit     VARCHAR(15)  NOT NULL,
   unitWeight      MEDIUMINT    NOT NULL,
   productGroup_id BIGINT(20)   NOT NULL,
   dateCreated     DATETIME     NOT NULL,

   FOREIGN KEY (productGroup_id) REFERENCES ProductGroup (id)
);

CREATE TABLE Receipt (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   supplier_id BIGINT(20) NOT NULL,
   dateCreated DATETIME   NOT NULL,

   FOREIGN KEY (supplier_id) REFERENCES Supplier (id)
);

CREATE TABLE ReceiptItem (
   id                         BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   unitPrice                  DECIMAL(10, 3) NOT NULL,
   quantity                   DECIMAL(10, 3) NOT NULL,
   product_id                 BIGINT(20)     NOT NULL,
   receipt_id                 BIGINT(20)     NOT NULL,
#    productNameSnapshot        VARCHAR(255)   NOT NULL,
#    productMeasureUnitSnapshot VARCHAR(15)    NOT NULL,
#    productUnitWeightSnapshot  MEDIUMINT      NOT NULL,
   dateCreated                DATETIME       NOT NULL,

   UNIQUE (product_id, receipt_id),
   FOREIGN KEY (product_id) REFERENCES Product (id),
   FOREIGN KEY (receipt_id) REFERENCES Receipt (id)
      ON DELETE CASCADE
);

CREATE TABLE MenuRequirement (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,

   date        DATE       NOT NULL,
   dateCreated DATETIME   NOT NULL
);

CREATE TABLE TechCardGroup (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name        VARCHAR(255) NOT NULL,
   dateCreated DATETIME     NOT NULL
);

CREATE TABLE TechCard (
   id               BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name             VARCHAR(255) NOT NULL,
   techCardGroup_id BIGINT(20)   NOT NULL,
   dateCreated      DATETIME     NOT NULL,

   FOREIGN KEY (techCardGroup_id) REFERENCES TechCardGroup (id)
);

CREATE TABLE TechCardProduct (
   techCard_id     BIGINT(20) NOT NULL,
   productGroup_id BIGINT(20) NOT NULL,
   outputWeight    INTEGER    NOT NULL,
   dateCreated     DATETIME   NOT NULL,

   FOREIGN KEY (productGroup_id) REFERENCES ProductGroup (id),
   FOREIGN KEY (techCard_id) REFERENCES TechCard (id)
      ON DELETE CASCADE,
   CONSTRAINT UNIQUE_TECH_CARD_PRODUCT_GROUP UNIQUE (techCard_id, productGroup_id)
);

CREATE TABLE TechCardSetType (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name        VARCHAR(255) NOT NULL,
   dateCreated DATETIME     NOT NULL,

   UNIQUE (name)
);

CREATE TABLE TechCardSet (
   id                 BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name               VARCHAR(255) NOT NULL,
   techCardSetType_id BIGINT(20)   NOT NULL,
   dateCreated        DATETIME     NOT NULL,

   FOREIGN KEY (techCardSetType_id) REFERENCES TechCardSetType (id)
);

CREATE TABLE TechCardSetTechCard (
   techCardSet_id BIGINT(20) NOT NULL,
   techCard_id    BIGINT(20) NOT NULL,
   dateCreated    DATETIME   NOT NULL,

   FOREIGN KEY (techCard_id) REFERENCES TechCard (id),
   FOREIGN KEY (techCardSet_id) REFERENCES TechCardSet (id)
      ON DELETE CASCADE
);

CREATE TABLE MenuRequirementTechCardSet (
   menuRequirement_id BIGINT(20) NOT NULL,
   techCardSet_id     BIGINT(20) NOT NULL,
   number             SMALLINT   NOT NULL,
   dateCreated        DATETIME   NOT NULL,

   FOREIGN KEY (menuRequirement_id) REFERENCES MenuRequirement (id),
   FOREIGN KEY (techCardSet_id) REFERENCES TechCardSet (id)
      ON DELETE CASCADE
);

CREATE TABLE WriteOff (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   periodStart DATE       NOT NULL,
   periodEnd   DATE       NOT NULL,
   dateCreated DATETIME   NOT NULL
);

CREATE TABLE WriteOffItem (
   id                      BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   quantityAvailableBefore DECIMAL(10, 3) NOT NULL,
   quantityConsumed        DECIMAL(10, 3) NOT NULL,
   quantity                DECIMAL(10, 3) NOT NULL,
   quantityAvailableAfter  DECIMAL(10, 3) NOT NULL,
   receiptItem_id          BIGINT(20)     NOT NULL,
   writeOff_id             BIGINT(20)     NOT NULL,
   dateCreated             DATETIME       NOT NULL,

   FOREIGN KEY (receiptItem_id) REFERENCES ReceiptItem (id),
   FOREIGN KEY (writeOff_id) REFERENCES WriteOff (id)
      ON DELETE CASCADE
);