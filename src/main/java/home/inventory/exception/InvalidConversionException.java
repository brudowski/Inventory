package home.inventory.exception;

/**
 * A custom Exception to alert if one unit cannot be converted into another unit
 * 
 * @author BRudowski
 */
public class InvalidConversionException extends Exception{
    
    public InvalidConversionException (String initialUnit, String targetUnit) {
        super(String.format("Error during conversion: Cannot convert from %s to %s.", 
                initialUnit, targetUnit));
    }
}
