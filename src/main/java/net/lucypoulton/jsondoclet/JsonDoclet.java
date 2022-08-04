package net.lucypoulton.jsondoclet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;
import net.lucypoulton.jsondoclet.option.AbstractOption;
import net.lucypoulton.jsondoclet.option.OutputOption;
import net.lucypoulton.jsondoclet.option.StringOption;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Parameterizable;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.NoType;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static net.lucypoulton.jsondoclet.JsonArrayUtils.addIfNotEmpty;
import static net.lucypoulton.jsondoclet.JsonArrayUtils.toStringArray;

/**
 * A doclet that generates JSON.
 */
@SuppressWarnings("unused")
public class JsonDoclet implements Doclet {
    @Override
    public void init(final Locale locale, final Reporter reporter) {

    }

    @Override
    public String getName() {
        return null;
    }

    private final OutputOption outputOption = new OutputOption();
    private final AbstractOption<String> nameOption = new StringOption("Title", List.of("-windowtitle"));
    private final AbstractOption<String> versionOption = new StringOption("Version", List.of("-version"));

    @Override
    public Set<? extends Option> getSupportedOptions() {
        return Set.of(outputOption, nameOption, versionOption);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean run(final DocletEnvironment environment) {
        final JsonArray children = new JsonArray();
        final TypeTree<JsonObject> outTree = new TypeTree<>("", null);
        for (final Element element : environment.getSpecifiedElements()) {
            if (element instanceof QualifiedNameable qn) {
                outTree.setValue(qn.getQualifiedName().toString(), serialise(qn, environment, null));
            }
            children.add(serialise(element, environment, null));
        }
        try {
            Files.writeString(outputOption.getValue().resolve("cod.json"), gson.toJson(outTree));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(TypeTree.class, new TypeTreeDeserializer())
            .create();

    private static JsonElement comment(DocCommentTree tree) {
        final var object = new JsonObject();
        object.addProperty("text", tree.getFirstSentence().toString() + '\n' + tree.getBody().toString());
        final var tags = new JsonObject();
        final var params = new JsonObject();
        final var typeParams = new JsonObject();
        for (final DocTree docTree : tree.getBlockTags()) {
            if (docTree.getKind() == DocTree.Kind.PARAM) {
                final var value = docTree.toString().split(" ", 3);
                if (value[1].startsWith("<"))
                    typeParams.addProperty(value[1].substring(1, value[1].length() - 1), value[2]);
                else params.addProperty(value[1], value[2]);
                continue;
            }
            final var value = docTree.toString().split(" ", 2);
            tags.addProperty(
                    docTree.getKind().toString().toLowerCase(),
                    value.length == 2 ? value[1] : "");
        }
        object.add("tags", tags);
        object.add("params", params);
        object.add("typeParams", typeParams);
        return object;
    }

    private static JsonArray toElementArray(Collection<? extends Element> collection, final DocletEnvironment env, final String parent) {
        final var array = new JsonArray();
        for (final var element : collection) array.add(serialise(element, env, parent));
        return array;
    }

    private static String elementName(final Element element) {
        final StringBuilder builder = new StringBuilder();
        builder.append(element.getSimpleName());
        if (element instanceof ExecutableElement e) {
            builder.append('(');
            builder.append(
                    e.getParameters().stream()
                            .map(VariableElement::asType)
                            .map(Object::toString)
                            .collect(Collectors.joining(", "))
            );
            builder.append(')');
        }
        return builder.toString();
    }

    private static JsonObject serialise(final Element element, final DocletEnvironment env, final String parent) {
        final var object = new JsonObject();
        object.addProperty("kind", element.getKind().toString());
        final var name = elementName(element);
        object.addProperty("name", name);

        addIfNotEmpty(object, "modifiers", toStringArray(element.getModifiers(), Enum::name));
        addIfNotEmpty(object, "decorations", toStringArray(element.getAnnotationMirrors(), d -> d.toString().substring(1)));

        final var doc = env.getDocTrees().getDocCommentTree(element);
        if (doc != null) object.add("doc", comment(doc));

        String fullName;
        if (element instanceof QualifiedNameable qn) {
            fullName = qn.getQualifiedName().toString();
            object.addProperty("qualifiedName", fullName);
        } else fullName = String.format("%s.%s", parent, name);

        object.addProperty("fullName", fullName);

        if (element instanceof Parameterizable param) {
            addIfNotEmpty(object, "typeParameters", toElementArray(param.getTypeParameters(), env, fullName));
        }
        if (element instanceof TypeElement type) {
            final var superclass = type.getSuperclass();
            if (!(superclass instanceof NoType)) {
                object.addProperty("extends", superclass.toString());
            }
            object.add("implements", toStringArray(type.getInterfaces()));

        } else if (element instanceof ExecutableElement executable) {
            object.addProperty("returnType", executable.getReturnType().toString());
            object.add("parameters", toElementArray(executable.getParameters(), env, fullName));
        } else if (element instanceof VariableElement param) {
            object.addProperty("type", param.asType().toString());
        } else if (element instanceof TypeParameterElement type) {
            object.add("extends", toStringArray(type.getBounds()));
        }

        final var children = toElementArray(element.getEnclosedElements(), env, fullName);
        if (children.size() > 0) object.add("children", children);
        return object;
    }
}
