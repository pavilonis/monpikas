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
   productGroup_id BIGINT(20)   NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (productGroup_id) REFERENCES ProductGroup (id)
);

CREATE TABLE ReceiptStatement (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   supplier_id  BIGINT(20) NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (supplier_id) REFERENCES Supplier (id)
);

CREATE TABLE ReceiptItem (
   id                         BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   unitPrice                  DECIMAL(10, 3) NOT NULL,
   quantity                   DECIMAL(10, 3) NOT NULL,
   product_id                  BIGINT(20)     NOT NULL,
   receiptStatement_id         BIGINT(20)     NOT NULL,
   productNameSnapshot        VARCHAR(255)   NOT NULL,
   productMeasureUnitSnapshot VARCHAR(15)    NOT NULL,
   productUnitWeightSnapshot  MEDIUMINT      NOT NULL,
   dateCreated                DATETIME       NOT NULL             DEFAULT NOW(),

   UNIQUE (product_id, receiptStatement_id),
   FOREIGN KEY (product_id) REFERENCES Product (id),
   FOREIGN KEY (receiptStatement_id) REFERENCES ReceiptStatement (id)
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
   dishGroup_id BIGINT(20)   NOT NULL,
   dateCreated DATETIME     NOT NULL             DEFAULT NOW(),
   FOREIGN KEY (dishGroup_id) REFERENCES DishGroup (id)
);

CREATE TABLE DishItem (
   id             BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   dish_id         BIGINT(20)     NOT NULL,
   productGroup_id BIGINT(20)     NOT NULL,
   outputWeight   DECIMAL(10, 3) NOT NULL,
   dateCreated    DATETIME       NOT NULL             DEFAULT NOW(),
   FOREIGN KEY (productGroup_id) REFERENCES ProductGroup (id),
   FOREIGN KEY (dish_id) REFERENCES Dish (id)
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
   mealType_id  BIGINT(20) NOT NULL,
   menu_id      BIGINT(20) NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (mealType_id) REFERENCES MealType (id),
   FOREIGN KEY (menu_id) REFERENCES Menu (id)
      ON DELETE CASCADE
);

CREATE TABLE MealItem (
   id          BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   meal_id      BIGINT(20) NOT NULL,
   dish_id      BIGINT(20) NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (dish_id) REFERENCES Dish (id),
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
   id                  BIGINT(20)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
   quantity            DECIMAL(10, 3) NOT NULL,
   receiptItem_id       BIGINT(20)     NOT NULL,
   writeOffStatement_id BIGINT(20)     NOT NULL,
   dishItem_id          BIGINT(20)     NOT NULL,
   mealItem_id          BIGINT(20)     NOT NULL,
   dateCreated         DATETIME       NOT NULL             DEFAULT NOW(),

   FOREIGN KEY (receiptItem_id) REFERENCES ReceiptItem (id),
   FOREIGN KEY (dishItem_id) REFERENCES DishItem (id),
   FOREIGN KEY (mealItem_id) REFERENCES MealItem (id),
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
   tax_id       BIGINT(20) NOT NULL,
   dateCreated DATETIME   NOT NULL             DEFAULT NOW(),
   FOREIGN KEY (tax_id) REFERENCES Tax (id)
);