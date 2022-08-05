package net.lucypoulton.jsondoclet.option;

import jdk.javadoc.doclet.Doclet;
import java.util.List;

public abstract class AbstractOption<T> implements Doclet.Option {
    protected T value;

    private final String description;
    private final List<String> names;

    protected AbstractOption(final String description, final List<String> names) {
        this.description = description;
        this.names = names;
    }

    @Override
    public int getArgumentCount() {
        return 1;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<String> getNames() {
        return names;
    }

    @Override
    public String getParameters() {
        return "<param>";
    }

    public Doclet.Option.Kind getKind() {
        return Doclet.Option.Kind.STANDARD;
    }

    public T getValue() {
        return value;
    }
}
