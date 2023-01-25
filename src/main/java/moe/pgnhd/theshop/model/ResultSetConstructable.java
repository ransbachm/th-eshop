package moe.pgnhd.theshop.model;

/**
 * Class can be constructed from ResultSet.
 * Class needs to have single parameter constructor that takes java.sql.ResultSet.
 * In case a table column is not found the constructor should not
 * throw but instead leave the object with null attributes.
 * Table name will be equal to class name.
 * Not enforced by Java!
 */
public interface ResultSetConstructable {}
