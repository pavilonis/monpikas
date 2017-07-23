CREATE TABLE Supplier (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   code        VARCHAR(255) NOT NULL,
   name        VARCHAR(255) NOT NULL,
   dateCreated DATETIME     NOT NULL             DEFAULT NOW(),

   UNIQUE (code)
);

CREATE TABLE ProductGroup (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name        VARCHAR(255) NOT NULL,
   kcal100     MEDIUMINT    NOT NULL,
   dateCreated DATETIME     NOT NULL             DEFAULT NOW(),

   UNIQUE (name)
);

CREATE TABLE Product (
   id              BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name            VARCHAR(255) NOT NULL,
   measureUnit     VARCHAR(15)  NOT NULL,
   unitWeight      MEDIUMINT    NOT NULL,
   productGroup_id BIGINT(20)   NOT NULL,
   dateCreated     DATETIME     NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (productGroup_id) REFERENCES ProductGroup (id)
);

CREATE TABLE ReceiptStatement (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   supplier_id BIGINT(20) NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (supplier_id) REFERENCES Supplier (id)
);

CREATE TABLE ReceiptItem (
   id                         BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   unitPrice                  DECIMAL(10, 3) NOT NULL,
   quantity                   DECIMAL(10, 3) NOT NULL,
   product_id                 BIGINT(20)     NOT NULL,
   receiptStatement_id        BIGINT(20)     NOT NULL,
   productNameSnapshot        VARCHAR(255)   NOT NULL,
   productMeasureUnitSnapshot VARCHAR(15)    NOT NULL,
   productUnitWeightSnapshot  MEDIUMINT      NOT NULL,
   dateCreated                DATETIME       NOT NULL             DEFAULT NOW(),

   UNIQUE (product_id, receiptStatement_id),
   FOREIGN KEY (product_id) REFERENCES Product (id),
   FOREIGN KEY (receiptStatement_id) REFERENCES ReceiptStatement (id)
      ON DELETE CASCADE
);

CREATE TABLE MenuRequirement (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,

   date        DATE       NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW()
);


CREATE TABLE TechCardGroup (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name        VARCHAR(255) NOT NULL,
   dateCreated DATETIME     NOT NULL             DEFAULT NOW()
);

CREATE TABLE TechCard (
   id               BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name             VARCHAR(255) NOT NULL,
   techCardGroup_id BIGINT(20)   NOT NULL,
   dateCreated      DATETIME     NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (techCardGroup_id) REFERENCES TechCardGroup (id)
);

CREATE TABLE TechCardProduct (
   id              BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   techCard_id     BIGINT(20) NOT NULL,
   productGroup_id BIGINT(20) NOT NULL,
   outputWeight    INTEGER    NOT NULL,
   dateCreated     DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (productGroup_id) REFERENCES ProductGroup (id),
   FOREIGN KEY (techCard_id) REFERENCES TechCard (id)
      ON DELETE CASCADE
);

CREATE TABLE TechCardSetType (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name        VARCHAR(255) NOT NULL,
   dateCreated DATETIME     NOT NULL             DEFAULT NOW(),

   UNIQUE (name)
);

CREATE TABLE TechCardSet (
   id                 BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name               VARCHAR(255) NOT NULL,
   techCardSetType_id BIGINT(20)   NOT NULL,
   dateCreated        DATETIME     NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (techCardSetType_id) REFERENCES TechCardSetType (id)
);

CREATE TABLE TechCardSetTechCard (
   id             BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   techCardSet_id BIGINT(20) NOT NULL,
   techCard_id    BIGINT(20) NOT NULL,
   dateCreated    DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (techCard_id) REFERENCES TechCard (id),
   FOREIGN KEY (techCardSet_id) REFERENCES TechCardSet (id)
      ON DELETE CASCADE
);

CREATE TABLE MenuRequirementTechCardSet (
   menuRequirement_id BIGINT(20) NOT NULL,
   techCardSet_id     BIGINT(20) NOT NULL,
   dateCreated        DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (menuRequirement_id) REFERENCES MenuRequirement (id),
   FOREIGN KEY (techCardSet_id) REFERENCES TechCardSet (id)
      ON DELETE CASCADE
);

CREATE TABLE WriteOffStatement (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   periodStart DATE       NOT NULL,
   periodEnd   DATE       NOT NULL,
   confirmed   BIT(1)     NOT NULL             DEFAULT b'0',
   dateCreated DATETIME   NOT NULL             DEFAULT NOW()
);

CREATE TABLE WriteOffItem (
   id                     BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   quantity               DECIMAL(10, 3) NOT NULL,
   receiptItem_id         BIGINT(20)     NOT NULL,
   writeOffStatement_id   BIGINT(20)     NOT NULL,
   techCardProduct_id     BIGINT(20)     NOT NULL,
   techCardSetTechCard_id BIGINT(20)     NOT NULL,
   dateCreated            DATETIME       NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (receiptItem_id) REFERENCES ReceiptItem (id),
   FOREIGN KEY (techCardProduct_id) REFERENCES TechCardProduct (id),
   FOREIGN KEY (techCardSetTechCard_id) REFERENCES TechCardSetTechCard (id),
   FOREIGN KEY (writeOffStatement_id) REFERENCES WriteOffStatement (id)
      ON DELETE CASCADE
);

CREATE TABLE Tax (
   id          BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   dateCreated DATETIME       NOT NULL             DEFAULT NOW(),
   percent     DECIMAL(10, 3) NOT NULL
);

CREATE TABLE TaxCurrent (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   tax_id      BIGINT(20) NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW(),
   FOREIGN KEY (tax_id) REFERENCES Tax (id)
);