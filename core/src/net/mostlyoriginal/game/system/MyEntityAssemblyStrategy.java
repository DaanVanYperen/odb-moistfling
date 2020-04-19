package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.future.FutureEntity;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;
import net.mostlyoriginal.game.system.future.FutureEntitySystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

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

    Vector2 worldOrigin = new Vector2();
    Vector2 vel = new Vector2();

    private E decoratePlayer(E e) {
        E decoratePlayer = e
                .anim("player_idle")
                .bounds(4, 4, 48 - 4, 48-4)
                .player()
                .tag("player")
                .camera()
                .renderLayer(GameRules.LAYER_PLAYER);
        Body body = boxPhysicsSystem.addAsCircle(decoratePlayer, decoratePlayer.getBounds().cy(), 20f, CAT_PLAYER, (short) (CAT_DEBRIS), 0, 20);

        return decoratePlayer;
    }

    GameScreenAssetSystem gameScreenAssetSystem;

    private E decorateItem(E e, String type) {

        Animation<TextureRegion> animation = gameScreenAssetSystem.get(type);
        final TextureRegion frame = animation.getKeyFrame(0, false);

        E item = e
                .bounds(0, 0, frame.getRegionWidth(), frame.getRegionHeight())
                .tappable()
                .anim(type)
                .renderLayer(GameRules.LAYER_ITEM);

        int size = (frame.getRegionWidth() / 2) - 4;

        boxPhysicsSystem.addAsCircle(item, item.getBounds().cy(), size * size * 0.15f, CAT_DEBRIS, (short) (CAT_DEBRIS|CAT_PLAYER|CAT_GRAPPLE|CAT_CHAIN), MathUtils.random(0,360f), size);


        if ( MathUtils.random(0, 100) < 20) {
            Body body = e.boxedBody();
            final Vector2 vel = body.getLinearVelocity();
            worldOrigin.x = (e.posX() + e.boundsCx()) / BoxPhysicsSystem.PPM;
            worldOrigin.y = (e.posY() + e.boundsCy()) / BoxPhysicsSystem.PPM;
//            body.setLinearVelocity(1f, 0);
            body.applyAngularImpulse( MathUtils.random(-0.1f,0.1f) * body.getMass(), true);
        }


        return item;
    }
}
