package net.mostlyoriginal.game.util;

import net.mostlyoriginal.api.component.graphics.Invisible;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.operation.common.Operation;

/**
 * @author Daan van Yperen
 */
public class Scripts {

    public static Operation appearOverTime() {
        return OperationFactory.sequence(JamOperationFactory.tintBetween(Tint.TRANSPARENT,Tint.WHITE,0.8f));
    }

    public static Operation appearOverTime(Tint startTint) {
        return OperationFactory.sequence(JamOperationFactory.tintBetween(startTint,Tint.WHITE,0.8f));
    }

    public static Operation fadeOverTime() {
        return fadeOverTime(Tint.WHITE);
    }

    private static Operation fadeOverTime(Tint startTint) {
        return OperationFactory.sequence(
                JamOperationFactory.tintBetween(startTint,Tint.TRANSPARENT,0.8f),
                OperationFactory.add(Invisible.class));
    }

}
