package net.lucypoulton.jsondoclet.test;


/**
 * An example class.
 * Loreum ipsum dolor sit amet.
 *
 * @since the 21st night of september
 */
public class ExampleClass implements ExampleInterface<String> {

    /**
     * An example field.
     *
     * @deprecated
     */
    public @Deprecated String field() { return ""; }

    public String doTheThing() {
        return "";
    }
}