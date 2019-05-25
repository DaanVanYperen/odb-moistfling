package net.mostlyoriginal.game.system;

import com.artemis.E;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.game.CollisionLayers;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.AffectedByNight;
import net.mostlyoriginal.game.component.Machine;
import net.mostlyoriginal.game.component.future.FutureEntity;
import net.mostlyoriginal.game.component.future.Properties;
import net.mostlyoriginal.game.component.inventory.Inventory;
import net.mostlyoriginal.game.system.future.FutureEntitySystem;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;
import net.mostlyoriginal.game.system.repository.ItemTypeManager;
import net.mostlyoriginal.game.util.Scripts;

import java.util.ArrayList;
import java.util.List;

import static net.mostlyoriginal.game.EntityType.*;

/**
 * Handles assembling entities based on spawn requests.
 *
 * @author Daan van Yperen
 */
public class MyEntityAssemblyStrategy implements FutureEntitySystem.EntityAssemblyStrategy {

    private List<E> altars = new ArrayList<>();
    private List<E> hoppers = new ArrayList<>();
    private boolean finalized = false;

    ItemTypeManager itemManager;

    @Override
    public void createInstance(int entityId) {
        final E source = E.E(entityId);
        decorateInstance(source);
    }

    @Override
    public void end() {
        if (!finalized) {
            finalized = true;

            if (altars.size() != 1) {
                // @todo resolve.
                throw new RuntimeException("Only one machine supported");
            }
            // @todo resolve.
            if (hoppers.size() == 0) throw new RuntimeException("No hoppers found");

            final E altar = altars.get(0);
            hookupHoppers(altar, hoppers);
        }
    }

    private static void hookupHoppers(E machine, List<E> hoppers) {

        // point hoppers at machine, and machine at hoppers.
        for (E hopper : hoppers) {
            hopper.getHopper().machineId = machine.id();
            machine.getMachine().hoppers.add(hopper.id());
        }
    }


    private E decorateInstance(E source) {
        final FutureEntity futureEntity = source.getFutureEntity();
        switch (futureEntity.type) {
            case ITEM:
                return decorateItem(source, futureEntity.subType, futureEntity.count);
            case PLAYER:
                return decoratePlayer(source);
            case WINDOW:
                return decorateWindow(source);
            case DOOR:
                return decorateDoor(source);
            case SHOPPER_SPAWNER:
                return decorateShopperSpawner(source);
            case SLOT:
                final Properties p = source.getProperties();
                return decorateSlot(source, p.getEnum("mode", Inventory.Mode.class), p.getInt("x"), p.getInt("y"), p.getString("accepts").split(","));
            case ALTAR:
                return decorateAltar(source);
            case SHOPPER:
                return decorateShopper(source);
        }
        throw new RuntimeException("Unknown entity type " + source.futureEntityType());
    }

    private E decorateShopper(E e) {
        final Properties p = e.getProperties();

        String desiredItem = p.getString(FutureSpawnUtility.KEY_DESIRED_ITEM);
        if ("random".equals(desiredItem)) {
            desiredItem = itemManager.randomReward();
        }

        e
                .pos(500, 300)
                .desireDesiredItem(desiredItem)
                .shopper()
                .tint(Tint.TRANSPARENT)
                .lifter()
                .castsShadow()
                .castsShadowYOffset(-4)
                .script(Scripts.appearOverTime())
                .renderLayer(GameRules.LAYER_SHOPPER);

        String rewardItem = p.getString(FutureSpawnUtility.KEY_REWARD_ITEM);
        if (rewardItem != null) {
            if ("random".equals(rewardItem)) {
                rewardItem = itemManager.randomReward();
            }
            E item = decorateItem(E.E(), rewardItem, p.getInt(FutureSpawnUtility.KEY_REWARD_ITEM_COUNT));
            e.actionPickupTarget(item.id());
        }
        return e;
    }

    private E decorateAltar(E e) {
        e
                .anim("altar")
                .machineType(Machine.Type.ALTAR)
                .renderLayer(GameRules.LAYER_MACHINES)
                .tag("altar");
        altars.add(e);
        return e;
    }

    private E decorateSlot(E e, Inventory.Mode mode, int x, int y, String... acceptsItems) {
        E slot = e
                .tint(1f, 1f, 1f, 0.7f)
                .bounds(0, 0, 32, 32)
                .inventoryAccepts(acceptsItems)
                .inventoryMode(mode)
                .inventoryX(x)
                .inventoryY(y)
                .renderLayer(GameRules.LAYER_SLOTS);
        if ( mode == Inventory.Mode.HOPPER) {
            slot.hopper()
                .collider(CollisionLayers.HOPPER);
            hoppers.add(slot);
        }
        return slot;
    }

    private E decorateShopperSpawner(E e) {
        return e.shopperSpawner();
    }

    private E decorateDoor(E e) {
        return e
                .tint(Tint.WHITE)
                .affectedByNightVisibleAt(AffectedByNight.Moment.NIGHT)
                .affectedByNightDuration(0.5f)
                .collider(CollisionLayers.DOOR)
                .anim("locked_door")
                .renderLayer(GameRules.LAYER_DOORS);
    }

    private E decorateWindow(E e) {
        return e
                .affectedByNight()
                .tint(Tint.TRANSPARENT)
                .anim("godray_window")
                .renderLayer(GameRules.LAYER_WINDOWS);
    }

    private E decoratePlayer(E e) {
        return e
                .anim("player_kid")
                .itemType("item_player")
                .gridPosDeriveFromPos(true)
                .bounds(4, 0, 16 - 4, 16 - 4)
                .player()
                .collider(CollisionLayers.PLAYER, CollisionLayers.HOPPER | CollisionLayers.DOOR) // player will collide with items and slots.
                .castsShadow()
                .physics()
                .tag("player")
                .lifter()
                .castsShadowYOffset(-4)
                .renderLayer(GameRules.LAYER_PLAYER);
    }

    private E decorateItem(E e, String type, int count) {
        if (type == null || "".equals(type)) return null;
        return e
                .canPickup(true)
                .itemType(type)
                .itemCount(count)
                .bounds(0, 0, 32, 32)
                .castsShadow()
                .renderLayer(GameRules.LAYER_ITEM);
    }
}
