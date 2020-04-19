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
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.api.system.physics.AttachmentSystem;
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
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;
import net.mostlyoriginal.game.system.view.MyClearScreenSystem;
import net.mostlyoriginal.plugin.OperationsPlugin;

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
                        MyFluidEntityPlugin.class,
                        EntityLinkManager.class,
                        OperationsPlugin.class,
                        SingletonPlugin.class)
                .with(  new EventSystem())
                .with(
                        new FontManager(),
                        new TagManager(),
                        new TiledMapManager("map0.tmx"),
                        new ItemTypeManager(), // @todo phase2: decouple? convert to component?
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

//                        new DialogUISystem(new MyDialogFactory()),

                        // Input

                        new PlayerControlSystem(), // @todo phase 2: separate movement from key binding to control.

                        // Mechanics.

                        new PassiveSpawnerSystem(), // @todo phase 2: move collision logic to separate bit based system.

                        // Actor Actions

                        //new InteractActionSystem(), // Determines WHICH action actors will perform

                        //new PickupActionSystem(),
                        //new DropActionSystem(),
                        //new BuildActionSystem(),
                        //new UseActionSystem(),
                        //new LootSpawnSystem(),

                        // Secondary effects lifecycle management.

                        //new SlotHighlightingSystem(),
                        //new RaftExtensionPointSystem(),

                        // Movement

                        //new CarriedItemPositioningSystem(),
                        //new MapCollisionSystem(), // clamp physics to avoid map collision.
//                        new MyPhysicsSystem(), // @todo phase2: replace. box2d?
                        new BoxPhysicsSystem(),
//                        new BruteforceCollisionSystem(new MyCollisionHandler()),
                        //new AttachmentSystem(),

                        //new HopperTalleySystem(), // talley hoppered items on the machine.
                        //new PlayerOnHopperTalleySystem(), // talley hoppered players on the machine.
                        //new MachineRecipeSystem(),
                        //new RecipeIngredientHintSystem(),

                        //new MapSwimmingSystem(),
                        //new PlayerAnimationSystem(), // @todo phase2: is there a more generic what to do this?
                        //new BlinkingSystem(),
                        //new PaymentAnimationSystem(), // @todo phase2: do we need a whole system for this?

                        //new NightSystem(),

//                        new ScoreSystem(),
//                        new SubmergedSystem(),
//                        new StaminaSystem(),

//                        new ShadedWaterRenderSystem(),
                        renderBatchingSystem = new RenderBatchingSystem(),
                        new MyAnimRenderSystem(renderBatchingSystem),
                        new MyLabelRenderSystem(renderBatchingSystem),
                        new MapLayerRenderSystem(renderBatchingSystem, batch),
//                        new DrippingSystem(),
//                        new RadioSystem(),

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
                        //new InventoryDebugSystem(), // @todo allow optional systems (nullable in gamescreen)
                        new TransitionSystem(GdxArtemisGame.getInstance(), this)
                );

        if (GameRules.DEBUG_ENABLED) {
            worldConfigurationBuilder.with(new DebugOptionControlSystem());
        }

        return new World(worldConfigurationBuilder.build());
    }
}
