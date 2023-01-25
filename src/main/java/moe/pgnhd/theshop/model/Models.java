package moe.pgnhd.theshop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Models {
    private static Logger LOG = LoggerFactory.getLogger(Models.class);

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
        List<T> res = new ArrayList<T>();
        int i=0;
        try {
            while(rs.next()) {
                if(i < rs.getInt(table_one + "." + pk_one)) {
                    i++;
                    res.add((T) one.getDeclaredConstructor(ResultSet.class).newInstance(rs));
                }
                T obj = res.get(i - 1);
                Field container_field = obj.getClass().getDeclaredField(container_name_one);
                container_field.setAccessible(true); // Ignore private attribute
                List<Object> container = (List<Object>) container_field.get(obj);
                container.add(many.getDeclaredConstructor(ResultSet.class).newInstance(rs));
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
        return multiple_one_to_many(rs, one, many, container_name_one, one.getSimpleName(), "id");
    }

}
