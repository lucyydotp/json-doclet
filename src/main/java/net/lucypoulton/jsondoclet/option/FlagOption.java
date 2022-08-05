package net.lucypoulton.jsondoclet.option;

import java.util.List;
import java.util.Objects;

public class FlagOption extends AbstractOption<Boolean> {
    public FlagOption(final String description, final List<String> names) {
        super(description, names);
    }

    @Override
    public boolean process(final String option, final List<String> arguments) {
        value = true;
        return true;
    }

    @Override
    public int getArgumentCount() {
        return 0;
    }

    @Override
    public Boolean getValue() {
        return Objects.requireNonNullElse(super.getValue(), false);
    }
}
