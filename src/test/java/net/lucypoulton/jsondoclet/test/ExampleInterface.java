package net.lucypoulton.jsondoclet.test;

import java.util.List;

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

    /**
     * Gets a list of T based off of some numbers. {@link #doTheThing(String)}
     * @param numbers the numbers
     * @return a list
     * @see #doTheThing(String)
     */
    List<? extends T> getAList(int... numbers);

    /**
     * An inner interface.
     * @param <T> the arbitrary type passed to ExampleInterface
     */
    interface InnerInterface<T> extends ExampleInterface<T, String> {
        // intentionally empty
    }
}
