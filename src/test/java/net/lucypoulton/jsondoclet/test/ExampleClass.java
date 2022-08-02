package net.lucypoulton.jsondoclet.test;


/**
 * An example class.
 * Loreum ipsum dolor sit amet.
 *
 * @since the 21st night of september
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
}
