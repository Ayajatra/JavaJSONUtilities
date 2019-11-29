import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSONUtils {
    public static <T> JSONObject toJSONObject(T object) {
        JSONObject result = new JSONObject();
        Arrays.stream(object.getClass().getFields())
            .filter(x -> Modifier.isPublic(x.getModifiers()))
            .forEach(x -> {
                try {
                    Object data = x.get(object);
                    if (data instanceof List) {
                        result.put(x.getName(), toJSONArray((List)data));
                    } else {
                        result.put(x.getName(), data);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });

        return result;
    }

    public static <T> JSONArray toJSONArray(List<T> objects) {
        JSONArray result = new JSONArray();
        for (T object : objects) {
            result.put(JSONUtils.toJSONObject(object));
        }

        return result;
    }

    public static <T> T fromJSONObject(JSONObject jsonObject, Class<T> _class) {
        try {
            T object = _class.newInstance();
            List<Field> fields = Arrays.stream(_class.getFields())
                .filter(x -> Modifier.isPublic(x.getModifiers()))
                .collect(Collectors.toList());

            for (Field field : fields) {
                if (!jsonObject.has(field.getName())) {
                    continue;
                }

                Object value = jsonObject.get(field.getName());
                if (value instanceof JSONObject) {
                    field.set(object, JSONUtils.fromJSONObject((JSONObject)value, field.getType()));
                } else if (value instanceof JSONArray) {
                    Class<?> argumentClass = (Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                    field.set(object, JSONUtils.fromJSONArray((JSONArray)value, argumentClass));
                } else {
                    field.set(object, value);
                }
            }

            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> List<T> fromJSONArray(JSONArray jsonArray, Class<T> _class) {
        int length = jsonArray.length();
        List<T> results = Stream
            .<T>builder()
            .build()
            .collect(Collectors.toList());

        for (int i = 0; i < length; i++) {
            results.add(JSONUtils.fromJSONObject(jsonArray.getJSONObject(i), _class));
        }

        return results;
    }
}
