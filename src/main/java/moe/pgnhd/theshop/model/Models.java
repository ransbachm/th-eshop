package moe.pgnhd.theshop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Models {
    private static Logger LOG = LoggerFactory.getLogger(Models.class);

    /**
     * Classes have to implement the interface ResultSetConstructable
     * Due to reflection it is not possible to (allways) enforce this at
     * compile time. The check will therefore be done at runtime.
     * @param classes Classes that must implement ResultSetConstructable
     * @throws ClassCastException If at least one class does not implement ResultSetConstructable
     */
    private static void checkForInterface(Class ... classes) {
        for(Class clazz : classes) {
            boolean success = ResultSetConstructable.class.isAssignableFrom(clazz);
            if(!success) {
                throw new ClassCastException("Class " + clazz.getName() +
                        " must implement ResultSetConstructable.");
            }
        }
    }


    /**
     * Returns multiple "one to many" relations
     * @param rs ResultSet that contains the "one" and "many" tables joined. It will be consumed.
     *           It needs to be ordered by at least the "one"-side's pk asc.
     * @param one Binding class of "one" table
     * @param many Binding class of "many" table
     * @param container_name_one Container on "one" side that will receive "many"-side objects
     * @param table_one Name of the "many" table
     * @param pk_one Name of the pk on the "one" side
     * @return A List containing every "one-many" relation found
     */

    public static <T extends ResultSetConstructable> List<T> multiple_one_to_many(ResultSet rs, Class one, Class many, String container_name_one,
                                                   String table_one, String pk_one) {
        checkForInterface(one, many);
        List<T> res = new ArrayList<T>();
        int last=0;
        int i=0;
        try {
            while(rs.next()) {
                if(last != rs.getInt(table_one + "." + pk_one)) {
                    last = rs.getInt(table_one + "." + pk_one);
                    i++;
                    Method from = one.getDeclaredMethod("from", ResultSet.class);
                    res.add((T) from.invoke(null, rs));
                }
                T obj = res.get(i - 1);
                Field container_field = obj.getClass().getDeclaredField(container_name_one);
                container_field.setAccessible(true); // Ignore private attribute
                List<Object> container = (List<Object>) container_field.get(obj);
                Method from = many.getDeclaredMethod("from", ResultSet.class);
                from.setAccessible(true);
                container.add(from.invoke(null, rs));
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
        return res;
    }

    /**
     * Returns multiple "one to many" relations
     * @param rs ResultSet that contains the "one" and "many" tables joined. It will be consumed.
     *           It needs to be ordered by at least the "one"-side's pk.
     * @param one Binding class of "one" table needs to be ResultSetConstructable
     * @param many Binding class of "many" table to be ResultSetConstructable
     * @param container_name_one Container on "one" side that will receive "many"-side objects
     * @return A List containing every "one-many" relation found
     */
    public static <T extends ResultSetConstructable> List<T> multiple_one_to_many(ResultSet rs, Class one, Class many, String container_name_one) {
        // checkForInterface(what); will be checked in delegated method
        return multiple_one_to_many(rs, one, many, container_name_one, one.getSimpleName(), "id");
    }

    /**
     * Returns a list of type specified by "what".
     * @param rs Result set that contains the table.
     *           It will be consumed.
     *           It needs to be ordered by at least the tables' pk asc.
     * @param what Binding class of the table.
     * @return A list of type specified by "what".
     */
    public static <T extends ResultSetConstructable> List<T> list_of(ResultSet rs, Class what) {
        checkForInterface(what);
        List<T> res = new ArrayList<>();
        try {
            while(rs.next()) {
                Method from = what.getDeclaredMethod("from", ResultSet.class);
                T obj = (T) from.invoke(null, rs);
                res.add(obj);
            }
            return res;
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    public static <T extends ResultSetConstructable> T single(ResultSet rs, Class<T> what) {
        try {
            rs.next();
            Method from = what.getDeclaredMethod("from", ResultSet.class);
            return (T) from.invoke(null, rs);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
    }
}
