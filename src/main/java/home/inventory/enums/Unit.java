package home.inventory.enums;

import java.util.EnumSet;

/**
 *
 * @author BRudowski
 */
public enum Unit {
    PINCH, OUNCES, POUNDS, FLUID_OUNCES, CUPS, PINTS, QUARTS, GALLONS, TEASPOON,
    TABLESPOON, MILLIGRAM, GRAM, KILOGRAM, MILLILITER, LITER;
    
    public static EnumSet<Unit> getVolumeUnits() {
        return EnumSet.of(FLUID_OUNCES, CUPS, PINTS, QUARTS, GALLONS, PINCH, TEASPOON, TABLESPOON, MILLILITER, LITER);
    }
    
    public static EnumSet<Unit> getMassUnits() {
        return EnumSet.of(OUNCES, POUNDS, MILLIGRAM, GRAM, KILOGRAM);
    }
    
}
