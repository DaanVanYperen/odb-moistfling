package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.game.CollisionLayers;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.AffectedByNight;
import net.mostlyoriginal.game.component.ItemData;
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

    public static final short CAT_PLAYER = 1;
    public static final short CAT_METEORITE = 2;
    public static final short CAT_GRAPPLE = 4;
    public static final short CAT_DEBRIS = 8;
    public static final short CAT_CHAIN = 16;


    ItemTypeManager itemManager;
    private BoxPhysicsSystem boxPhysicsSystem;

    @Override
    public void createInstance(int entityId) {
        final E source = E.E(entityId);
        decorateInstance(source);
    }

    @Override
    public void end() {
        if (!finalized) {
            finalized = true;

            if (altars.size() > 1) {
                // @todo resolve.
                throw new RuntimeException("Only one machine supported");
            }
            // @todo resolve.
            //if (hoppers.size() == 0) throw new RuntimeException("No hoppers found");

            //final E altar = altars.get(0);
            //hookupHoppers(altar, hoppers);
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
            case DEBRIS:
                return decorateItem(source, futureEntity.subType);
            case PLAYER:
                return decoratePlayer(source);
        }
        throw new RuntimeException("Unknown entity type " + source.futureEntityType());
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

    private E decorateSlot(E e, Inventory.Mode mode, int x, int y, String transform, String... acceptsItems) {
        E slot = e
                .tint(1f, 1f, 1f, 0.7f)
                .bounds(0, 0, 16, 16)
                .inventoryAccepts(acceptsItems)
                .inventoryMode(mode)
                .inventoryX(x)
                .inventoryY(y)
                .inventoryTransform(transform)
                .renderLayer(GameRules.LAYER_SLOTS);
        if (mode == Inventory.Mode.HOPPER) {
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
        E decoratePlayer = e
                .pos(20*16-4, 13*16+4)
                .anim("player_idle")
                .bounds(4, 4, 48 - 4, 48-4)
                .player()
                .tag("player")
                .castsShadowYOffset(-4)
                .renderLayer(GameRules.LAYER_PLAYER);

//        E.E().pos(240, 160).anim("indicator_power1").renderLayer(GameRules.LAYER_DESIRE_INDICATOR).staminaIndicator();

//        FutureSpawnUtility.item("item_pallet",1,20,13,false).locked();
//        FutureSpawnUtility.item("item_radio",1,20,13,false).tag("radio");

        Body body = boxPhysicsSystem.addAsBox(decoratePlayer, decoratePlayer.getBounds().cx(), decoratePlayer.getBounds().cy(), 20f, CAT_PLAYER, (short) (CAT_DEBRIS), 0);
        boxPhysicsSystem.spawnChain(body, e);

        return decoratePlayer;
    }

    private E decorateItem(E e, String type) {
        E item = e
                .bounds(0, 0, 48, 48)
                .anim("debris_small_1")
                .renderLayer(GameRules.LAYER_ITEM + (GameRules.SCREEN_HEIGHT - (int) e.posY()));

        boxPhysicsSystem.addAsBox(item, item.getBounds().cx(), item.getBounds().cy(), 1000f, CAT_DEBRIS, (short) (CAT_PLAYER|CAT_GRAPPLE|CAT_CHAIN), 15);
        return item;
    }
}
