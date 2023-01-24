package moe.pgnhd.theshop.model;

import moe.pgnhd.theshop.Verwaltung;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Models {
    private static Logger LOG = LoggerFactory.getLogger(Models.class);

    /**
     *
     * @param rs ResultSet that contains the "one" and "many" tables joined
     * @param one Binding class of "one" table
     * @param many Binding class of "many" table
     * @param container_name_one Container on "one" side that will receive "many"-side objects
     * @param table_one Name of the "many" table
     * @param pk_one Name of the pk on the "one" side
     * @apiNote ResultSet needs to be ordered by at least the "one"-side's pk
     * @return A List containing every "one-many" relation found
     * @param <T>
     */

    public static <T> List<T> multiple_one_to_many(ResultSet rs, Class one, Class many, String container_name_one,
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
     * Sets sensible default values for the "one"-side table name and pk
     * @param rs ResultSet that contains the "one" and "many" tables joined
     * @param one Binding class of "one" table
     * @param many Binding class of "many" table
     * @param container_name_one Container on "one" side that will receive "many"-side objects
     * @apiNote ResultSet needs to be ordered by at least the "one"-side's pk
     * @return A List containing every "one-many" relation found
     * @param <T>
     */
    public static <T> List<T> multiple_one_to_many(ResultSet rs, Class one, Class many, String container_name_one) {
        return multiple_one_to_many(rs, one, many, container_name_one, one.getSimpleName(), "id");
    }

}
