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
   id             BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name           VARCHAR(255) NOT NULL,
   measureUnit    VARCHAR(15)  NOT NULL,
   unitWeight     MEDIUMINT    NOT NULL,
   productGroupId BIGINT(20)   NOT NULL,

   FOREIGN KEY (productGroupId) REFERENCES ProductGroup (id)
);

CREATE TABLE ReceiptStatement (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   supplierId  BIGINT(20) NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (supplierId) REFERENCES Supplier (id)
);

CREATE TABLE ReceiptItem (
   id                         BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   unitPrice                  DECIMAL(10, 3) NOT NULL,
   quantity                   DECIMAL(10, 3) NOT NULL,
   productId                  BIGINT(20)     NOT NULL,
   receiptStatementId         BIGINT(20)     NOT NULL,
   productNameSnapshot        VARCHAR(255)   NOT NULL,
   productMeasureUnitSnapshot VARCHAR(15)    NOT NULL,
   productUnitWeightSnapshot  MEDIUMINT      NOT NULL,
   dateCreated                DATETIME       NOT NULL             DEFAULT NOW(),

   UNIQUE (productId, receiptStatementId),
   FOREIGN KEY (productId) REFERENCES Product (id),
   FOREIGN KEY (receiptStatementId) REFERENCES ReceiptStatement (id)
      ON DELETE CASCADE
);

CREATE TABLE Menu (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,

   date        DATE       NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW()
);


CREATE TABLE DishGroup (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name        VARCHAR(255) NOT NULL,
   dateCreated DATETIME     NOT NULL             DEFAULT NOW()
);

CREATE TABLE Dish (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name        VARCHAR(255) NOT NULL,
   dishGroupId BIGINT(20)   NOT NULL,
   dateCreated DATETIME     NOT NULL             DEFAULT NOW(),
   FOREIGN KEY (dishGroupId) REFERENCES DishGroup (id)
);

CREATE TABLE DishItem (
   id             BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   dishId         BIGINT(20)     NOT NULL,
   productGroupId BIGINT(20)     NOT NULL,
   outputWeight   DECIMAL(10, 3) NOT NULL,
   dateCreated    DATETIME       NOT NULL             DEFAULT NOW(),
   FOREIGN KEY (productGroupId) REFERENCES ProductGroup (id),
   FOREIGN KEY (dishId) REFERENCES Dish (id)
      ON DELETE CASCADE
);

CREATE TABLE MealType (
   id          BIGINT(20)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name        VARCHAR(255) NOT NULL,
   dateCreated DATETIME     NOT NULL             DEFAULT NOW(),

   UNIQUE (name)
);

CREATE TABLE Meal (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   mealTypeId  BIGINT(20) NOT NULL,
   menuId      BIGINT(20) NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (mealTypeId) REFERENCES MealType (id),
   FOREIGN KEY (menuId) REFERENCES Menu (id)
      ON DELETE CASCADE
);

CREATE TABLE MealItem (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   mealId      BIGINT(20) NOT NULL,
   dishId      BIGINT(20) NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (dishId) REFERENCES Dish (id),
   FOREIGN KEY (mealId) REFERENCES Meal (id)
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
   id                  BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   quantity            DECIMAL(10, 3) NOT NULL,
   receiptItemId       BIGINT(20)     NOT NULL,
   writeOffStatementId BIGINT(20)     NOT NULL,
   dishItemId          BIGINT(20)     NOT NULL,
   mealItemId          BIGINT(20)     NOT NULL,
   dateCreated         DATETIME       NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (receiptItemId) REFERENCES ReceiptItem (id),
   FOREIGN KEY (dishItemId) REFERENCES DishItem (id),
   FOREIGN KEY (mealItemId) REFERENCES MealItem (id),
   FOREIGN KEY (writeOffStatementId) REFERENCES WriteOffStatement (id)
      ON DELETE CASCADE
);

CREATE TABLE Tax (
   id          BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   dateCreated DATETIME       NOT NULL             DEFAULT NOW(),
   percent     DECIMAL(10, 3) NOT NULL
);

CREATE TABLE TaxCurrent (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   taxId       BIGINT(20) NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW(),
   FOREIGN KEY (taxId) REFERENCES Tax (id)
);