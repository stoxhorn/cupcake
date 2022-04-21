package dat.cupcake.model.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NotACupcakeException extends Exception {

    public NotACupcakeException(String message) {
        super(message);
        Logger.getLogger("web").log(Level.SEVERE, message);
    }
    public NotACupcakeException(Exception ex, String message) {
        super(message);
        Logger.getLogger("web").log(Level.SEVERE, message, ex.getMessage());
    }
}
