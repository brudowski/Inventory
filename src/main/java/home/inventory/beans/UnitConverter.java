package home.inventory.beans;

import home.inventory.enums.Unit;
import home.inventory.exception.InvalidConversionException;
import java.util.EnumSet;

/**
 * This class will contain various conversions to allow for recipes that
 * reference different units than the stored items, such as pounds when the item
 * uses ounces
 * 
 * Note: Currently in-development and not used elsewhere in the code
 *
 * @author BRudowski
 */
public class UnitConverter {

    /**
     * Attempts to convert the quantity of one unit into an equivalent quantity
     * of a new unit. If the two units are incompatible, an
     * InvalidConversionException is thrown.
     *
     * @param initialUnit Starting unit
     * @param targetUnit Unit to convert to
     * @param quantity Amount of the starting unit
     * @return Converted quantity value
     * @throws InvalidConversionException
     */
    public static double convert(Unit initialUnit, Unit targetUnit, double quantity) throws InvalidConversionException {
        if (initialUnit.equals(targetUnit)) {
            return quantity;
        }
        EnumSet<Unit> massUnits = Unit.getMassUnits();
        EnumSet<Unit> volumeUnits = Unit.getVolumeUnits();
        if(!(massUnits.contains(initialUnit) && massUnits.contains(targetUnit)) 
                && !(volumeUnits.contains(initialUnit) && volumeUnits.contains(targetUnit))) {
            throw new InvalidConversionException(initialUnit.name(), targetUnit.name());
        }
        switch (initialUnit) {
            default:
                throw new InvalidConversionException(initialUnit.name(), targetUnit.name());
        }
    }

    public static double ouncesToPounds(double ounces) {
        return ounces / 16.0;
    }
    
    public static double pinchToTeaspoon(int pinch) {
        return pinch/16.0;
    }
}
