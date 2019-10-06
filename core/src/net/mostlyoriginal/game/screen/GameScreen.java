package net.mostlyoriginal.game.screen;

import com.artemis.FluidEntityPlugin;
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
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.GdxArtemisGame;
import net.mostlyoriginal.game.system.*;
import net.mostlyoriginal.game.system.action.*;
import net.mostlyoriginal.game.system.control.*;
import net.mostlyoriginal.game.system.future.FutureEntitySystem;
import net.mostlyoriginal.game.system.logic.MyPhysicsSystem;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.system.map.*;
import net.mostlyoriginal.game.system.mechanics.*;
import net.mostlyoriginal.game.system.render.*;
import net.mostlyoriginal.game.system.repository.ItemTypeManager;
import net.mostlyoriginal.game.system.repository.RecipeManager;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;
import net.mostlyoriginal.game.system.view.MyClearScreenSystem;
import net.mostlyoriginal.plugin.DebugPlugin;
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
                        FluidEntityPlugin.class,
                        EntityLinkManager.class,
                        ProfilerPlugin.class,
                        OperationsPlugin.class,
                        SingletonPlugin.class)
                .with(DebugPlugin.thatLogsErrorsIn("net.mostlyoriginal").enable(false))
                .with(  new EventSystem())
                .with(
                        new FontManager(),
                        new TagManager(),
                        new TiledMapManager("map0.tmx"),
                        new ItemTypeManager(), // @todo phase2: decouple? convert to component?
                        new RecipeManager(), // @todo phase2: decouple? convert to component?
                        new PickupManager(), // @todo phase2: get rid of this!
                        new SlotManager()
                )
                .with(
                        // Setup.

                        new CameraSystem(2), // @todo phase 2: Make reusable?
                        new MyClearScreenSystem(Color.valueOf(BACKGROUND_COLOR_HEX)), // probably fine.
                        new GameScreenAssetSystem(), // @todo phase 2: Make reusable?

                        // Entity spawning

                        new MapEntitySpawnerSystem(), // Converts Tiled maps to FutureEntities to be spawned.
                        new FutureEntitySystem(new MyEntityAssemblyStrategy()), // Responsible for spawning entities.
                        new ParticleEffectSystem(new MyParticleEffectStrategy()),
                        //new ShopperSpawnSystem(),

                        // UI handlers.

                        new DialogUISystem(new MyDialogFactory()),

                        //new TutorialSystem(), // @todo phase 2: internals to singleton. Move dialog definitions to MyDialogFactory.

                        // Input

                        new PlayerControlSystem(), // @todo phase 2: separate movement from key binding to control.
                        //new ShopperAISystem(),

                        // Mechanics.

                        new PassiveSpawnerSystem(), // @todo phase 2: move collision logic to separate bit based system.

                        // Actor Actions

                        new InteractActionSystem(), // Determines WHICH action actors will perform

                        //new TalkActionSystem(),
                        //new TradeActionSystem(),
                        new PickupActionSystem(),
                        new DropActionSystem(),
                        new BuildActionSystem(),

                        // Secondary effects lifecycle management.

                        //new DesireSystem(),
                        //new ShadowSystem(), // @todo phase 2: Separate shadow, create a relationship component with parent.
                        new SlotHighlightingSystem(),
                        new RaftExtensionPointSystem(),

                        // Movement

                        new CarriedItemPositioningSystem(),
                        new MapCollisionSystem(), // clamp physics to avoid map collision.
                        new MyPhysicsSystem(), // @todo phase2: replace. box2d?
                        new BruteforceCollisionSystem(new MyCollisionHandler()),
                        new GridPosSystem(), // @todo phase2: Separate the Snap to grid behaviour.
                        new GridPosFloatSystem(), // @todo phase2: Move floating to separate mechanic. (Mount it on something?)

                        //new HopperTalleySystem(), // talley hoppered items on the machine.
                        //new PlayerOnHopperTalleySystem(), // talley hoppered players on the machine.
                        //new MachineRecipeSystem(),
                        //new RecipeIngredientHintSystem(),

                        new MapSwimmingSystem(),
                        new PlayerAnimationSystem(), // @todo phase2: is there a more generic what to do this?
                        //new PaymentAnimationSystem(), // @todo phase2: do we need a whole system for this?

                        //new NightSystem(),

                        //new ScoreSystem(),
                        new SubmergingSystem(),

                        new ShadedWaterRenderSystem(),
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
                                "sfx_hag"
                        ),

                        //new InventoryDebugSystem(), // @todo allow optional systems (nullable in gamescreen)
                        new TransitionSystem(GdxArtemisGame.getInstance(), this)
                );

        if (GameRules.DEBUG_ENABLED) {
            worldConfigurationBuilder.with(new DebugOptionControlSystem());
        }

        return new World(worldConfigurationBuilder.build());
    }
}
