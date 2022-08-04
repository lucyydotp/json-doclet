package net.lucypoulton.jsondoclet.option;

import java.util.List;

public class StringOption extends AbstractOption<String> {
    public StringOption(final String description, final List<String> names) {
        super(description, names);
    }

    @Override
    public String getParameters() {
        return getNames().get(0) + " <p1>";
    }

    @Override
    public boolean process(final String option, final List<String> arguments) {
        value = arguments.get(0);
        return value != null;
    }
}
