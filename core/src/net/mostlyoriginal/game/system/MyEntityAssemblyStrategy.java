package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Pickup;
import net.mostlyoriginal.game.component.future.FutureEntity;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;
import net.mostlyoriginal.game.system.future.FutureEntitySystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static net.mostlyoriginal.game.EntityType.*;
import static net.mostlyoriginal.game.component.Pickup.Type.OXYGEN;

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
    public static final short CAT_PICKUP = 32;
    public static final short CAT_BORDER = 64;


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
        }
    }


    private E decorateInstance(E source) {
        final FutureEntity futureEntity = source.getFutureEntity();
        switch (futureEntity.type) {
            case DEBRIS:
                return decorateDebris(source, futureEntity.subType);
            case PLAYER:
                return decoratePlayer(source);
            case PICKUP:
                return decoratePickup(source, futureEntity.subType);
            case EXIT:
                return decorateExit(source, futureEntity.subType);
            case BLINKER:
                return decorateBlinker(source);
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
                .oxygenPercentage(100)
                .tag("player")
                .camera()
                .renderLayer(GameRules.LAYER_PLAYER);
        Body body = boxPhysicsSystem.addAsCircle(decoratePlayer, decoratePlayer.getBounds().cy(), 20f, CAT_PLAYER, (short) (CAT_DEBRIS|CAT_PICKUP|CAT_BORDER), 0, 16, 1.0f, 0F, BodyDef.BodyType.DynamicBody);

        return decoratePlayer;
    }

    private E decorateBlinker(E e) {
        e
                .anim("orb_off")
                .bounds(4, 4, 16 - 4, 16-4)
                .renderLayer(GameRules.LAYER_ITEM-2);
//        Body body = boxPhysicsSystem.addAsCircle(decoratePlayer, decoratePlayer.getBounds().cy(), 20f, CAT_PLAYER, (short) (CAT_DEBRIS|CAT_PICKUP|CAT_BORDER), 0, 16, 1.0f, 0F);
        return e;
    }

    GameScreenAssetSystem gameScreenAssetSystem;

    private E decorateDebris(E e, String type) {

        Animation<TextureRegion> animation = gameScreenAssetSystem.get(type);
        final TextureRegion frame = animation.getKeyFrame(0, false);

        E item = e
                .bounds(0, 0, frame.getRegionWidth(), frame.getRegionHeight())
                .tappable()
                .sharp()
                .anim(type)
                .renderLayer(GameRules.LAYER_ITEM);

        int size = (frame.getRegionWidth() / 2) - 4;

        boxPhysicsSystem.addAsCircle(item, item.getBounds().cy(), size * size * 0.15f, CAT_DEBRIS, (short) (CAT_DEBRIS|CAT_PLAYER|CAT_GRAPPLE|CAT_CHAIN|CAT_PICKUP|CAT_BORDER), MathUtils.random(0,360f), size, 0.0f, 0.2f,
                "debris_immovable".equals(type) ? BodyDef.BodyType.StaticBody:
                BodyDef.BodyType.DynamicBody);

        if("debris_cactus".equals(type)){
            e.sharpChance(100).sharpSharpness(3);
        }

        if ( MathUtils.random(0, 100) < 20) {
            final Body body = e.boxedBody();
            final Vector2 vel = body.getLinearVelocity();
            worldOrigin.x = (e.posX() + e.boundsCx()) / BoxPhysicsSystem.PPM;
            worldOrigin.y = (e.posY() + e.boundsCy()) / BoxPhysicsSystem.PPM;
//            body.setLinearVelocity(1f, 0);
            body.applyAngularImpulse( MathUtils.random(-0.1f,0.1f) * body.getMass(), true);
        }


        return item;
    }


    private E decoratePickup(E e, String type) {

        Animation<TextureRegion> animation = gameScreenAssetSystem.get(type);
        final TextureRegion frame = animation.getKeyFrame(0, false);

        E item = e
                .bounds(0, 0, frame.getRegionWidth(), frame.getRegionHeight())
                .tappable()
                .anim(type)
                .pickupType(OXYGEN)
                .renderLayer(GameRules.LAYER_ITEM);

        int size = (frame.getRegionWidth() / 2) + 4;

        boxPhysicsSystem.addAsCircle(item, item.getBounds().cy(), 1f, CAT_PICKUP, (short) (CAT_DEBRIS|CAT_PLAYER|CAT_GRAPPLE|CAT_PICKUP|CAT_BORDER), MathUtils.random(0,360f), size, 0.0f, 0.1F, BodyDef.BodyType.DynamicBody);


        Body body = e.boxedBody();
        final Vector2 vel = body.getLinearVelocity();
        worldOrigin.x = (e.posX() + e.boundsCx()) / BoxPhysicsSystem.PPM;
        worldOrigin.y = (e.posY() + e.boundsCy()) / BoxPhysicsSystem.PPM;
        body.applyAngularImpulse( MathUtils.random(-0.1f,0.1f) * body.getMass(), true);


        return item;
    }

    private E decorateExit(E e, String type) {

        Animation<TextureRegion> animation = gameScreenAssetSystem.get(type);
        final TextureRegion frame = animation.getKeyFrame(0, false);

        E item = e
                .bounds(0, 0, frame.getRegionWidth(), frame.getRegionHeight())
                .tappable()
                .anim(type)
                .pickupType(Pickup.Type.EXIT)
                .renderLayer(GameRules.LAYER_ITEM);

        boxPhysicsSystem.addAsBox(item, item.getBounds().cx(), item.getBounds().cy(), 999999999f, CAT_PICKUP, (short) (CAT_PLAYER|CAT_BORDER), 0);

        return item;
    }
}
