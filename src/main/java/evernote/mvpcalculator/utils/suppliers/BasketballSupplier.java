package evernote.mvpcalculator.utils.suppliers;

import evernote.mvpcalculator.model.Basketball;
import evernote.mvpcalculator.model.Game;

import java.util.function.Supplier;

public class BasketballSupplier implements Supplier<Game> {

    @Override
    public Game get() {
        return new Basketball();
    }
}
