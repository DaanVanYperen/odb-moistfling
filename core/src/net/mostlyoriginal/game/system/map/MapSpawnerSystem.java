package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.maps.MapProperties;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.Slot;
import net.mostlyoriginal.game.component.AffectedByNight;
import net.mostlyoriginal.game.component.Machine;
import net.mostlyoriginal.game.manager.ItemRepository;

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

        if (machines.size() != 1) {
            throw new RuntimeException("Only one machine supported");
        }
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
            E item = spawnItem(x, y, type);
            if ( item != null )
                item.itemCount(properties.containsKey("count") ? (int)properties.get("count") : 1);
            return true;
        } else if ("player".equals(entity)) {
            spawnPlayer(x, y);
            return true;
        }else if ("slot".equals(entity)) {
            spawnSlot(x, y,  (String) properties.get("accepts"),
                    Slot.Mode.valueOf(((String) properties.get("mode")).toUpperCase()),
                    (int) properties.get("x"),
                    (int) properties.get("y"));
            return false;
        } else if ("hopper".equals(entity)) {
            spawnHopper(x, y);
            return true;
        } else if ("machine".equals(entity)) {
            spawnMachine(x, y, type);
            return true;
        } else if ("shopperspawner".equals(entity)) {
            spawnShopperSpawner(x, y);
            return true;
        } else  if ("window".equals(entity)) {
            spawnWindow(x, y);
            return false;
        }else  if ("door".equals(entity)) {
            spawnLockedDoor(x, y);
            return false;
        }

        return false;
    }

    private void spawnWindow(int x, int y) {
        E.E()
                .gridPos(x, y-3)
                .affectedByNight()
                .tint(Tint.TRANSPARENT)
                .anim("godray_window")
                .renderLayer(GameRules.LAYER_WINDOWS);
    }

    private void spawnLockedDoor(int x, int y) {
        E.E()
                .gridPos(x, y)
                .tint(Tint.WHITE)
                .affectedByNightVisibleAt(AffectedByNight.Moment.NIGHT)
                .affectedByNightDuration(0.5f)
                .anim("locked_door")
                .renderLayer(GameRules.LAYER_DOORS);
    }

    private void spawnSlot(int x, int y, String accepts, Slot.Mode mode, int slotX, int slotY) {
        E.E()
                .gridPos(x, y)
                .tint(1f,1f,1f,0.7f)
                .slotAccepts(accepts.split(","))
                .slotMode(mode)
                .slotX(slotX)
                .slotY(slotY)
                .renderLayer(GameRules.LAYER_SLOTS);
    }

    private void spawnMachine(int x, int y, String type) {
        E altar = E.E()
                .gridPos(x, y)
                .anim("altar")

                .machineType(Machine.Type.ALTAR)
                .renderLayer(GameRules.LAYER_MACHINES);
        machines.add(altar);
    }

    private void spawnHopper(int x, int y) {
        E hopper = E.E()
                .gridPos(x, y)
                .anim("hopper")
                .hopper()
                .renderLayer(GameRules.LAYER_MACHINES);
        hoppers.add(hopper);
    }

    private void spawnPlayer(int x, int y) {
        E.E()
                .gridPos(x, y)
                .gridPosDeriveFromPos(true)
                .anim("player_kid")
                .itemType("item_player")
                .bounds(4,0,16-4,16-4)
                .player()
                .castsShadow()
                .physics()
                .tag("player")
                .lifter()
                .castsShadowYOffset(-4)
                .renderLayer(GameRules.LAYER_PLAYER);
    }



    public void spawnShopper(int x, int y, String anim) {

        String desiredItem = itemRepository.randomDesire();
        String rewardItem  = itemRepository.randomReward();

        spawnShopperWithSpecificItems(x, y, desiredItem, rewardItem, anim, 1);
    }

    public E spawnShopperWithSpecificItems(int x, int y, String desiredItem, String rewardItem, String anim, int awardItemCount) {
        E shopper = E.E()
                .gridPos(x, y)
                .pos(500, 300)
                .anim(anim)
                .lifterAttemptLifting(true)
                .desireDesiredItem(desiredItem)
                .shopper()
                .tint(Tint.TRANSPARENT)
                .castsShadow()
                .castsShadowYOffset(-4)
                .script(Scripts.appearOverTime())
                .renderLayer(GameRules.LAYER_SHOPPER);

        if ( rewardItem != null )
        spawnItem(x,y, rewardItem).itemCount(awardItemCount);

        return shopper;
    }

    private void spawnShopperSpawner(int x, int y) {
        E.E()
                .gridPos(x, y).shopperSpawner();
    }


    public E spawnItem(int x, int y, String type) {
        if ( type == null || "".equals(type) ) return null;
        return E.E()
                .gridPos(x, y)
                .canPickup(true)
                .itemType(type)
                .castsShadow()
                .anim(itemRepository.get(type).sprite)
                .renderLayer(GameRules.LAYER_ITEM);
    }
}
