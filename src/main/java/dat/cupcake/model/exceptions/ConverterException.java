package dat.cupcake.model.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConverterException extends Exception {

    public ConverterException(String message) {
        super(message);
        Logger.getLogger("web").log(Level.SEVERE, message);
    }
    public ConverterException(Exception ex, String message) {
        super(message);
        Logger.getLogger("web").log(Level.SEVERE, message, ex.getMessage());
    }
}


