package net.lucypoulton.jsondoclet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class TypeTreeDeserializer implements JsonSerializer<TypeTree<?>> {
    @Override
    public JsonElement serialize(final TypeTree<?> src, final Type typeOfSrc, final JsonSerializationContext context) {
        if (src.value() == null && !src.children().isEmpty()) {
            final var out = new JsonObject();
            for (final TypeTree<?> child : src.children()) {
                out.add(child.name(), serialize(child, typeOfSrc, context));
            }

            return out;
        }
        final var out = context.serialize(src.value());
        if (!src.children().isEmpty()) {
            final var children = new JsonArray();
            for (final TypeTree<?> child : src.children()) {
                children.add(serialize(child, typeOfSrc, context));
            }
            ((JsonObject) out).add("children", children);
        }
        return out;
    }
}
