package net.lucypoulton.jsondoclet.option;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

public class OutputOption extends AbstractOption<Path> {

    public OutputOption() {
        super("Output directory", List.of("-d"));
    }

    @Override
    public boolean process(final String option, final List<String> arguments) {
        try {
            value = Path.of(arguments.get(0));
        } catch (InvalidPathException e) {
            return false;
        }
        return true;
    }

}
