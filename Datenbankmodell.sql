SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';


-- -----------------------------------------------------
-- Table `Recipes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Recipes` ;

CREATE  TABLE IF NOT EXISTS `Recipes` (
  `RecipeID` INT NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(45) NULL ,
  `Formula` VARCHAR(45) NULL ,
  `Duration` VARCHAR(45) NULL ,
  PRIMARY KEY (`RecipeID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `StorePlace`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `StorePlace` ;

CREATE  TABLE IF NOT EXISTS `StorePlace` (
  `StorePlaceID` INT NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(45) NULL ,
  PRIMARY KEY (`StorePlaceID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Ingredients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Ingredients` ;

CREATE  TABLE IF NOT EXISTS `Ingredients` (
  `IngredientID` INT NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(45) NULL ,
  `StorePlace` INT NULL ,
  PRIMARY KEY (`IngredientID`) ,
  INDEX `fk_Ingredients_StorePlace1_idx` (`StorePlace` ASC) ,
  CONSTRAINT `fk_Ingredients_StorePlace1`
    FOREIGN KEY (`StorePlace` )
    REFERENCES `StorePlace` (`StorePlaceID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RecipeIngredients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RecipeIngredients` ;

CREATE  TABLE IF NOT EXISTS `RecipeIngredients` (
  `Ingredient` INT NOT NULL ,
  `Recipe` INT NOT NULL ,
  `Amount` VARCHAR(45) NULL ,
  `Order` VARCHAR(45) NULL ,
  INDEX `fk_RecipeIngredients_Ingredients_idx` (`Ingredient` ASC) ,
  INDEX `fk_RecipeIngredients_Recipes1_idx` (`Recipe` ASC) ,
  CONSTRAINT `fk_RecipeIngredients_Ingredients`
    FOREIGN KEY (`Ingredient` )
    REFERENCES `Ingredients` (`IngredientID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RecipeIngredients_Recipes1`
    FOREIGN KEY (`Recipe` )
    REFERENCES `Recipes` (`RecipeID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Type` ;

CREATE  TABLE IF NOT EXISTS `Type` (
  `TypeID` INT NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(45) NULL ,
  PRIMARY KEY (`TypeID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RecipeTypes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RecipeTypes` ;

CREATE  TABLE IF NOT EXISTS `RecipeTypes` (
  `Recipe` INT NULL ,
  `Type` INT NULL ,
  INDEX `fk_RecipeTypes_Recipes1_idx` (`Recipe` ASC) ,
  INDEX `fk_RecipeTypes_Type1_idx` (`Type` ASC) ,
  CONSTRAINT `fk_RecipeTypes_Recipes1`
    FOREIGN KEY (`Recipe` )
    REFERENCES `Recipes` (`RecipeID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RecipeTypes_Type1`
    FOREIGN KEY (`Type` )
    REFERENCES `Type` (`TypeID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DayPlan`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DayPlan` ;

CREATE  TABLE IF NOT EXISTS `DayPlan` (
  `Recipe` INT NULL ,
  `Date` DATETIME NULL ,
  INDEX `fk_DayPlan_Recipes1_idx` (`Recipe` ASC) ,
  CONSTRAINT `fk_DayPlan_Recipes1`
    FOREIGN KEY (`Recipe` )
    REFERENCES `Recipes` (`RecipeID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User` ;

CREATE  TABLE IF NOT EXISTS `User` (
  `UserID` INT NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(45) NULL ,
  `EMail` VARCHAR(45) NULL ,
  PRIMARY KEY (`UserID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Group` ;

CREATE  TABLE IF NOT EXISTS `Group` (
  `GroupID` INT NOT NULL AUTO_INCREMENT ,
  `Name` VARCHAR(45) NULL ,
  PRIMARY KEY (`GroupID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `UserGroups`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserGroups` ;

CREATE  TABLE IF NOT EXISTS `UserGroups` (
  `Users` INT NULL ,
  `Group` INT NULL ,
  INDEX `fk_UserGroups_User1_idx` (`Users` ASC) ,
  INDEX `fk_UserGroups_Group1_idx` (`Group` ASC) ,
  CONSTRAINT `fk_UserGroups_User1`
    FOREIGN KEY (`Users` )
    REFERENCES `User` (`UserID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserGroups_Group1`
    FOREIGN KEY (`Group` )
    REFERENCES `Group` (`GroupID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GroupRecipes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GroupRecipes` ;

CREATE  TABLE IF NOT EXISTS `GroupRecipes` (
  `Recipe` INT NULL ,
  `Group` INT NULL ,
  INDEX `fk_GroupRecipes_Recipes1_idx` (`Recipe` ASC) ,
  INDEX `fk_GroupRecipes_Group1_idx` (`Group` ASC) ,
  CONSTRAINT `fk_GroupRecipes_Recipes1`
    FOREIGN KEY (`Recipe` )
    REFERENCES `Recipes` (`RecipeID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GroupRecipes_Group1`
    FOREIGN KEY (`Group` )
    REFERENCES `Group` (`GroupID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GroupIngredients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GroupIngredients` ;

CREATE  TABLE IF NOT EXISTS `GroupIngredients` (
  `Group` INT NULL ,
  `Ingredient` INT NULL ,
  `Available` BIT NULL ,
  INDEX `fk_GroupIngredients_Group1_idx` (`Group` ASC) ,
  INDEX `fk_GroupIngredients_Ingredients1_idx` (`Ingredient` ASC) ,
  CONSTRAINT `fk_GroupIngredients_Group1`
    FOREIGN KEY (`Group` )
    REFERENCES `Group` (`GroupID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GroupIngredients_Ingredients1`
    FOREIGN KEY (`Ingredient` )
    REFERENCES `Ingredients` (`IngredientID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
