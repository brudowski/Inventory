package home.inventory.exception;

/**
 *
 * @author BRudowski
 */
public class InvalidConversionException extends Exception{
    
    public InvalidConversionException (String initialUnit, String targetUnit) {
        super(String.format("Error during conversion: Cannot convert from %s to %s.", 
                initialUnit, targetUnit));
    }
}
