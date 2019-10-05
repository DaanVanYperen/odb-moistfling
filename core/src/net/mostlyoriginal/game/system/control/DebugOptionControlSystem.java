package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.game.component.ItemData;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.dialog.InDialog;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;
import net.mostlyoriginal.game.system.repository.ItemTypeManager;

/**
 * @author Daan van Yperen
 */
@All(Player.class)
@Exclude(InDialog.class)
public class DebugOptionControlSystem extends FluidIteratingSystem {

    private ItemTypeManager itemManager;

    @Override
    protected void process(E e) {
        if (Gdx.input.isKeyPressed(Input.Keys.F5)) {
            E.withTag("player").playerAge(0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F6)) {
            E.withTag("player").playerAge(1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F7)) {
            E.withTag("player").playerAge(2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F8)) {
            E.withTag("player").playerAge(3);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F9)) {
            int index = 0;
            for (ItemData data : itemManager.itemLibrary.items) {
                FutureSpawnUtility.item(data.id, 99, index++ % 20, index / 20,false);
            }

        }
    }

}
