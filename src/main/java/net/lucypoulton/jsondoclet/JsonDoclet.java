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
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Parameterizable;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.NoType;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

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

    @Override
    public Set<? extends Option> getSupportedOptions() {
        return Collections.emptySet();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean run(final DocletEnvironment environment) {
        for (final Element element : environment.getSpecifiedElements()) {
            System.out.println(gson.toJson(serialise(element, environment)));
        }
        return true;
    }

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static JsonElement comment(DocCommentTree tree) {
        final var object = new JsonObject();
        object.addProperty("text", tree.getFirstSentence().toString() + '\n' + tree.getBody().toString());
        final var tags = new JsonObject();
        for (final DocTree docTree : tree.getBlockTags()) {
            final var value = docTree.toString().split(" ", 1);
            tags.addProperty(
                    docTree.getKind().toString().toLowerCase(),
                    value.length == 2 ? value[1] : "");
        }
        object.add("tags", tags);
        return object;
    }

    private static JsonArray toElementArray(Collection<? extends Element> collection, final DocletEnvironment env) {
        final var array = new JsonArray();
        for (final var element : collection) array.add(serialise(element, env));
        return array;
    }

    private static JsonObject serialise(final Element element, final DocletEnvironment env) {
        final var object = new JsonObject();
        object.addProperty("kind", element.getKind().toString());
        object.addProperty("name", element.getSimpleName().toString());

        addIfNotEmpty(object, "modifiers", toStringArray(element.getModifiers(), Enum::name));
        addIfNotEmpty(object, "decorations", toStringArray(element.getAnnotationMirrors(), d -> d.toString().substring(1)));

        final var doc = env.getDocTrees().getDocCommentTree(element);
        if (doc != null) object.add("doc", comment(doc));

        if (element instanceof QualifiedNameable qn)
            object.addProperty("qualifiedName", qn.getQualifiedName().toString());

        if (element instanceof Parameterizable param) {
            addIfNotEmpty(object, "typeParameters", toElementArray(param.getTypeParameters(), env));
        }
        if (element instanceof TypeElement type) {
            final var superclass = type.getSuperclass();
            if (!(superclass instanceof NoType)) {
                object.addProperty("extends", superclass.toString());
            }
            object.add("implements", toStringArray(type.getInterfaces()));

        } else if (element instanceof ExecutableElement executable) {
            object.addProperty("returnType", executable.getReturnType().toString());
            object.add("parameters", toElementArray(executable.getParameters(), env));
        } else if (element instanceof VariableElement param) {
            object.addProperty("type", param.asType().toString());
        } else if (element instanceof TypeParameterElement type) {
            object.add("extends", toStringArray(type.getBounds()));
        }

        final var children = toElementArray(element.getEnclosedElements(), env);
        if (children.size() > 0) object.add("children", children);
        return object;
    }
}
