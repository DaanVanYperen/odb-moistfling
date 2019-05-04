package net.mostlyoriginal.game.screen;

import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.api.manager.FontManager;
import net.mostlyoriginal.api.plugin.fluidextensions.ESubscriptionPlugin;
import net.mostlyoriginal.api.plugin.singleton.SingletonPlugin;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.GdxArtemisGame;
import net.mostlyoriginal.game.system.control.*;
import net.mostlyoriginal.game.system.logic.MyPhysicsSystem;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.system.map.*;
import net.mostlyoriginal.game.system.mechanics.*;
import net.mostlyoriginal.game.system.render.*;
import net.mostlyoriginal.game.system.repository.ItemManager;
import net.mostlyoriginal.game.system.repository.RecipeManager;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;
import net.mostlyoriginal.game.system.view.MyClearScreenSystem;
import net.mostlyoriginal.plugin.OperationsPlugin;
import net.mostlyoriginal.plugin.ProfilerPlugin;

/**
 * Example main game screen.
 *
 * @author Daan van Yperen
 */
public class GameScreen extends TransitionableWorldScreen {

    public static final String BACKGROUND_COLOR_HEX = "969291";

    Class nextScreen;

    @Override
    protected World createWorld() {
        RenderBatchingSystem renderBatchingSystem;

        final SpriteBatch batch = new SpriteBatch(2000);

        WorldConfigurationBuilder worldConfigurationBuilder = new WorldConfigurationBuilder()
                .dependsOn(
                        EntityLinkManager.class,
                        ProfilerPlugin.class,
                        OperationsPlugin.class,
                        SingletonPlugin.class,
                        ESubscriptionPlugin.class)
                .with(
                        new SuperMapper(), // decoupled
                        new FontManager(), // decoupled
                        new TagManager(), // decoupled
                        new TiledMapManager("map0.tmx"),
                        new ItemManager(), // @todo decouple? convert to component?
                        new RecipeManager(), // @todo decouple? convert to component?
                        new PickupManager()
                )
                .with(
                        // Replace with your own systems!
                        new CameraSystem(2), // Make reusable?
                        new MyClearScreenSystem(Color.valueOf(BACKGROUND_COLOR_HEX)), // probably fine.
                        new GameScreenAssetSystem(), // Make reusable?

                        new MapEntitySpawnerSystem(), // @todo decrease coupling, can we create a non map locked solution?

                        new DialogSystem(), // @todo decouple PlayerControlSystem

                        new ShopperSpawnSystem(),
                        new NightShopperSpawnSystem(),
                        new TutorialSystem(),

                        new PlayerControlSystem(),
                        new MapCollisionSystem(),
                        new DeploySystem(),
                        new PassiveSpawnSystem(),

                        new ShopperControlSystem(),

                        new MyPhysicsSystem(),

                        new TradeSystem(), // must be before pickup system!
                        new PickupSystem(),

                        new DesireSystem(),
                        new ShadowSystem(),
                        new GridPosSystem(),
                        new GridPosFloatSystem(),

                        new PlayerAgeSystem(),

                        new PayingSystem(),

                        new HopperDetectionSystem(),
                        new MachineHopperDetectionSystem(),
                        new MachineRecipeSystem(),
                        new RecipeIngredientHintSystem(),
                        new SlotHighlightingSystem(),
                        new NightSystem(),

                        new ScoreSystem(),

                        new ParticleSystem(),
                        renderBatchingSystem = new RenderBatchingSystem(),
                        new MyAnimRenderSystem(renderBatchingSystem),
                        new MyLabelRenderSystem(renderBatchingSystem),
                        new MapLayerRenderSystem(renderBatchingSystem, batch),

                        new TransitionSystem(GdxArtemisGame.getInstance(), this)
                );

        if ( GameRules.DEBUG_ENABLED ) {
            worldConfigurationBuilder.with(new DebugOptionControlSystem());
        }

        return new World(worldConfigurationBuilder.build());
    }

}
