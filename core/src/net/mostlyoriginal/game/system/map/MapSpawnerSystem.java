package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.EBag;
import com.artemis.FluidIteratingSystem;
import com.badlogic.gdx.maps.MapProperties;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Hopper;
import net.mostlyoriginal.game.component.Machine;
import net.mostlyoriginal.game.manager.ItemRepository;
import net.mostlyoriginal.game.system.common.FluidSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daan van Yperen
 */
public class MapSpawnerSystem extends BaseSystem {
    ItemRepository itemRepository;

    private List<E> machines = new ArrayList<>();
    private List<E> hoppers = new ArrayList<>();

    @Override
    protected void processSystem() {
    }

    public void finalizeSpawns() {

        if (machines.size() != 1) throw new RuntimeException("Only one machine supported");
        if (hoppers.size() == 0) throw new RuntimeException("No hoppers found");

        final E altar = machines.get(0);
        hookupHoppers(altar, hoppers);
    }

    private static void hookupHoppers(E machine, List<E> hoppers) {

        // point hoppers at machine, and machine at hoppers.
        for (E hopper : hoppers) {
            hopper.getHopper().machineId = machine.id();
            machine.getMachine().hoppers.add(hopper.id());
        }
    }

    public boolean spawn(int x, int y, MapProperties properties) {

        String entity = (String) properties.get("entity");
        String type = (String) properties.get("type");
        if ("item".equals(entity)) {
            spawnItem(x, y, type);
            return true;
        } else if ("player".equals(entity)) {
            spawnPlayer(x, y);
            return true;
        } else if ("hopper".equals(entity)) {
            spawnHopper(x, y);
            return true;
        } else if ("machine".equals(entity)) {
            spawnMachine(x, y, type);
            return true;
        }

        return false;
    }

    private void spawnMachine(int x, int y, String type) {
        E altar = E.E()
                .gridPos(x, y)
                .pos()
                .anim("altar")
                .machineType(Machine.Type.ALTAR)
                .renderLayer(GameRules.LAYER_MACHINES);
        machines.add(altar);
    }

    private void spawnHopper(int x, int y) {
        E hopper = E.E()
                .gridPos(x, y)
                .pos()
                .anim("hopper")
                .hopper()
                .renderLayer(GameRules.LAYER_MACHINES);
        hoppers.add(hopper);
    }

    private void spawnPlayer(int x, int y) {
        E.E()
                .gridPos(x, y)
                .pos()
                .anim("player")
                .itemType("item_player")
                .renderLayer(GameRules.LAYER_PLAYER);
    }

    public void spawnItem(int x, int y, String type) {
        E.E()
                .gridPos(x, y)
                .pos()
                .itemType(type)
                .anim(itemRepository.get(type).sprite)
                .renderLayer(GameRules.LAYER_ITEM);
    }
}
