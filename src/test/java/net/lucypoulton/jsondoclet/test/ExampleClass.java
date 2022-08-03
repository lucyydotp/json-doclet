package net.lucypoulton.jsondoclet.test;


import java.util.List;

/**
 * An example class.
 * Loreum ipsum dolor sit amet.
 *
 * @since the 21st night of september
 * @author Lucy Poulton
 * @version 1.0.0
 */
public class ExampleClass implements ExampleInterface<ExampleClass, String> {

    public final int field = 4;

    /**
     * An example method.
     * @deprecated
     */
    public @Deprecated String method() { return ""; }

    @Override
    public ExampleClass doTheThing(String it) {
        return null;
    }

    @Override
    public List<? extends ExampleClass> getAList(final int... numbers) {
        return null;
    }
}
