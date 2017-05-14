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


CREATE TABLE TechnologicalCardGroup (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name        VARCHAR(255) NOT NULL,
   dateCreated DATETIME     NOT NULL             DEFAULT NOW()
);

CREATE TABLE TechnologicalCard (
   id                        BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name                      VARCHAR(255) NOT NULL,
   technologicalCardGroup_id BIGINT(20)   NOT NULL,
   dateCreated               DATETIME     NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (technologicalCardGroup_id) REFERENCES TechnologicalCardGroup (id)
);

CREATE TABLE TechnologicalCardItem (
   id                   BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   technologicalCard_id BIGINT(20)     NOT NULL,
   productGroup_id      BIGINT(20)     NOT NULL,
   outputWeight         DECIMAL(10, 3) NOT NULL,
   dateCreated          DATETIME       NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (productGroup_id) REFERENCES ProductGroup (id),
   FOREIGN KEY (technologicalCard_id) REFERENCES TechnologicalCard (id)
      ON DELETE CASCADE
);

CREATE TABLE MealType (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name        VARCHAR(255) NOT NULL,
   dateCreated DATETIME     NOT NULL             DEFAULT NOW(),

   UNIQUE (name)
);

CREATE TABLE Meal (
   id                 BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   mealType_id        BIGINT(20) NOT NULL,
   menuRequirement_id BIGINT(20) NOT NULL,
   dateCreated        DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (mealType_id) REFERENCES MealType (id),
   FOREIGN KEY (menuRequirement_id) REFERENCES MenuRequirement (id)
      ON DELETE CASCADE
);

CREATE TABLE MealTechnologicalCard (
   id                   BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   meal_id              BIGINT(20) NOT NULL,
   technologicalCard_id BIGINT(20) NOT NULL,
   dateCreated          DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (technologicalCard_id) REFERENCES TechnologicalCard (id),
   FOREIGN KEY (meal_id) REFERENCES Meal (id)
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
   id                       BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   quantity                 DECIMAL(10, 3) NOT NULL,
   receiptItem_id           BIGINT(20)     NOT NULL,
   writeOffStatement_id     BIGINT(20)     NOT NULL,
   technologicalCardItem_id BIGINT(20)     NOT NULL,
   mealTechnologicalCard_id BIGINT(20)     NOT NULL,
   dateCreated              DATETIME       NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (receiptItem_id) REFERENCES ReceiptItem (id),
   FOREIGN KEY (technologicalCardItem_id) REFERENCES TechnologicalCardItem (id),
   FOREIGN KEY (mealTechnologicalCard_id) REFERENCES MealTechnologicalCard (id),
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