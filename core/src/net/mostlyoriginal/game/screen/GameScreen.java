package net.mostlyoriginal.game.screen;

import com.artemis.MyFluidEntityPlugin;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.api.SingletonPlugin;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.manager.FontManager;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.camera.EntityCameraSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.api.system.physics.PhysicsSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.GdxArtemisGame;
import net.mostlyoriginal.game.system.*;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsDebugRenderSystem;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsMouseSystem;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;
import net.mostlyoriginal.game.system.control.*;
import net.mostlyoriginal.game.system.future.FutureEntitySystem;
import net.mostlyoriginal.game.system.logic.LeakSystem;
import net.mostlyoriginal.game.system.logic.PopSystem;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.system.map.*;
import net.mostlyoriginal.game.system.render.*;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;
import net.mostlyoriginal.game.system.view.MyClearScreenSystem;
import net.mostlyoriginal.plugin.OperationsPlugin;

/**
 * Example main game screen.
 *
 * @author Daan van Yperen
 */
public class GameScreen extends TransitionableWorldScreen {

    public static final String BACKGROUND_COLOR_HEX = "000000";

    Class nextScreen;

    @Override
    protected World createWorld() {
        RenderBatchingSystem renderBatchingSystem;

        final SpriteBatch batch = new SpriteBatch(2000);

        WorldConfigurationBuilder worldConfigurationBuilder = new WorldConfigurationBuilder()
                .dependsOn(
                        MyFluidEntityPlugin.class,
                        EntityLinkManager.class,
                        OperationsPlugin.class,
                        SingletonPlugin.class)
                .with(  new EventSystem())
                .with(
                        new FontManager(),
                        new TagManager(),
                        new TiledMapManager(GameRules.nextMap)
                )
                .with(
                        new CameraSystem(2),
                        new MyClearScreenSystem(Color.valueOf(BACKGROUND_COLOR_HEX)), // probably fine.
                        new GameScreenAssetSystem(),
                        new MapEntitySpawnerSystem(), // Converts Tiled maps to FutureEntities to be spawned.

                        new FutureEntitySystem(new MyEntityAssemblyStrategy()), // Responsible for spawning entities.
                        new ParticleEffectSystem(new MyParticleEffectStrategy()),
                        new PlayerControlSystem(), // @todo phase 2: separate movement from key binding to control.

                        new PhysicsSystem(), // for particles.
                        new BoxPhysicsSystem(),
                        new BoxPhysicsMouseSystem(),
                        new LatchingSystem(),


                        new PopSystem(),
                        new LeakSystem(),

                        new CameraFollowSystem(),
                        new PlayerAnimationSystem(),
                        new RenderBackgroundSystem(),
                        renderBatchingSystem = new RenderBatchingSystem(),
                        new MyAnimRenderSystem(renderBatchingSystem),
                        new MyLabelRenderSystem(renderBatchingSystem),
                        new MapLayerRenderSystem(renderBatchingSystem, batch),

                        new SoundPlaySystem(
                                "sfx_interact_6",
                                "sfx_magic_1",
                                "sfx_magic_2",
                                "sfx_magic_3",
                                "sfx_money_1",
                                "sfx_pickup",
                                "sfx_putdown",
                                "sfx_walk",
                                "sfx_hag",
                                "burp",
                                "drowned",
                                "water1",
                                "water2",
                                "LD45_dogwhine",
                                "LD45_mermaid"
                        ),
                        new BoxPhysicsDebugRenderSystem(),
                        new TransitionSystem(GdxArtemisGame.getInstance(), this)
                );

        if (GameRules.DEBUG_ENABLED) {
//            worldConfigurationBuilder.with(new DebugOptionControlSystem());
        }

        return new World(worldConfigurationBuilder.build());
    }
}
