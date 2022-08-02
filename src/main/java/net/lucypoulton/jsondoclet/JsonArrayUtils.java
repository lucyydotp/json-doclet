package net.lucypoulton.jsondoclet;

import com.google.gson.JsonArray;
import java.util.Collection;
import java.util.function.Function;

public class JsonArrayUtils {

    public static JsonArray toStringArray(Collection<?> collection) {
        return toStringArray(collection, Object::toString);
    }

    public static <T> JsonArray toStringArray(Collection<? extends T> collection, Function<? super T, String> mapper) {
        final var array = new JsonArray();
        for (final T t : collection) {
            array.add(mapper.apply(t));
        }
        return array;
    }
}
