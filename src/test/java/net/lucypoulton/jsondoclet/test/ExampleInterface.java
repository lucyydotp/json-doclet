package net.lucypoulton.jsondoclet.test;

/**
 * An example interface.
 * @param <T> some arbitrary return type idk
 * @param <U> something that extends a string
 */
@SuppressWarnings("unused")
public interface ExampleInterface<T, U extends String> {
    /**
     * Do the thing.
     * @param thing the thing to do.
     * @return something idk
     */
    T doTheThing(U thing);
}
