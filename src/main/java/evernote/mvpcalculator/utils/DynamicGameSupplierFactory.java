package evernote.mvpcalculator.utils;

import evernote.mvpcalculator.exception.GameNotSupportedException;
import evernote.mvpcalculator.exception.NoFileHeaderException;
import evernote.mvpcalculator.model.Game;
import evernote.mvpcalculator.utils.suppliers.BasketballSupplier;
import evernote.mvpcalculator.utils.suppliers.HandballSupplier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class DynamicGameSupplierFactory {

    public static final Map<String, Supplier<? extends Game>> registeredSuppliers = new HashMap<>();

    static {
        registeredSuppliers.put("Basketball", new BasketballSupplier());
        registeredSuppliers.put("Handball", new HandballSupplier());
    }

    public static void registerSupplier(String type, Supplier<? extends Game> supplier) {
        registeredSuppliers.put(type, supplier);
    }

    public static Game getGame(String gameName) {
        if (gameName.isEmpty() || gameName.isBlank()) {
            throw new NoFileHeaderException();
        }
        for (String key : registeredSuppliers.keySet()) {
            if (key.equalsIgnoreCase(gameName)) {
                return registeredSuppliers.get(key).get();
            }
        }
        throw new GameNotSupportedException();
    }
}
