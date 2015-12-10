package reflection;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
/**
 * Created by alla on 02.11.15.
 */
public class Reflection {
    public static Object createObject(String className) {
        try {
            Constructor c = Class.forName(className).getConstructor(String.class, String.class, String.class, long.class);
            return c.newInstance("", "", "", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldValue(Object object, String fieldName, String value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            if (field.getType().equals(String.class)) {
                field.set(object, value);
            } else if (field.getType().equals(Integer.class)) {
                field.set(object, Integer.decode(value));
            }
            field.setAccessible(false);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }


}
