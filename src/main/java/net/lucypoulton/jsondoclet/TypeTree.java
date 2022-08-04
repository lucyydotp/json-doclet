package net.lucypoulton.jsondoclet;

import java.util.Collection;
import java.util.HashMap;

public class TypeTree<T> extends HashMap<String, TypeTree<T>> {

    private final String name;
    private T value;
    public TypeTree(final String name, final T value) {
        this.name = name;
        this.value = value;
    }

    Collection<TypeTree<T>> children() {
        return this.values();
    }

    /**
     * Non-recursively finds a named child node, creating it if it doesn't exist.
     */
    TypeTree<T> directChild(String name) {
        final var child = this.get(name);
        if (child == null) {
            final var newChild = new TypeTree<T>(name, null);
            this.put(name, newChild);
            return newChild;
        }
        return child;
    }

    /**
     * Recursively finds a named child node, creating it if it doesn't exist.
     */
    TypeTree<T> namedChild(String name) {
        final var parts = name.split("\\.");
        var tree = this;
        for (final String part : parts) {
            tree = tree.directChild(part);
        }
        return tree;
    }

    void setValue(String name, T value) {
        namedChild(name).value(value);
        System.out.println(value.toString());
    }

    public String name() {
        return name;
    }

    public T value() {
        return value;
    }

    /**
     * Sets this node's own value.
     */
    public void value(T value) {
        this.value = value;
    }
}

