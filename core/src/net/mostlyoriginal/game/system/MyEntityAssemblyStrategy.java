package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.badlogic.gdx.physics.box2d.Body;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.game.CollisionLayers;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.future.FutureEntity;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;
import net.mostlyoriginal.game.system.future.FutureEntitySystem;

import java.util.ArrayList;
import java.util.List;

import static net.mostlyoriginal.game.EntityType.*;

/**
 * Handles assembling entities based on spawn requests.
 *
 * @author Daan van Yperen
 */
public class MyEntityAssemblyStrategy implements FutureEntitySystem.EntityAssemblyStrategy {

    private boolean finalized = false;

    public static final short CAT_PLAYER = 1;
    public static final short CAT_METEORITE = 2;
    public static final short CAT_GRAPPLE = 4;
    public static final short CAT_DEBRIS = 8;
    public static final short CAT_CHAIN = 16;

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

            // @todo resolve.
            //if (hoppers.size() == 0) throw new RuntimeException("No hoppers found");

            //final E altar = altars.get(0);
            //hookupHoppers(altar, hoppers);
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

    private E decoratePlayer(E e) {
        E decoratePlayer = e
                .pos(20*16-4, 13*16+4)
                .anim("player_idle")
                .bounds(4, 4, 48 - 4, 48-4)
                .player()
                .tag("player")
                .renderLayer(GameRules.LAYER_PLAYER);
        boxPhysicsSystem.addAsBox(decoratePlayer, decoratePlayer.getBounds().cx(), decoratePlayer.getBounds().cy(), 20f, CAT_PLAYER, (short) (CAT_DEBRIS), 0);
        return decoratePlayer;
    }

    private E decorateItem(E e, String type) {
        E item = e
                .bounds(0, 0, 48, 48)
                .tappable()
                .anim("debris_small_1")
                .renderLayer(GameRules.LAYER_ITEM);

        boxPhysicsSystem.addAsBox(item, item.getBounds().cx(), item.getBounds().cy(), 1000f, CAT_DEBRIS, (short) (CAT_PLAYER|CAT_GRAPPLE|CAT_CHAIN), 15);
        return item;
    }
}
