package moe.pgnhd.theshop.model;

/**
 * Class can be constructed from ResultSet.
 * Class needs to have single parameter static method that takes java.sql.ResultSet.
 * And returns a constructed object
 * In case a table column is not found the method should not
 * throw but instead return null.
 * Table name will be equal to class name.
 * Not enforced by Java!
 */
public interface ResultSetConstructable {}
