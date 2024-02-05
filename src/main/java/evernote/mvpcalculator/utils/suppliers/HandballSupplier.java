package evernote.mvpcalculator.utils.suppliers;

import evernote.mvpcalculator.model.Game;
import evernote.mvpcalculator.model.Handball;

import java.util.function.Supplier;

public class HandballSupplier implements Supplier<Game> {
    @Override
    public Game get() {
        return new Handball();
    }
}
