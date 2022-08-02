package net.lucypoulton.jsondoclet.test;

@SuppressWarnings("unused")
public interface ExampleInterface<T, U extends String> {
    T doTheThing(U thing);
}
