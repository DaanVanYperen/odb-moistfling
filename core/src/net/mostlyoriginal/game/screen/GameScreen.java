package net.mostlyoriginal.game.screen;

import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import net.mostlyoriginal.api.manager.FontManager;
import net.mostlyoriginal.api.screen.core.WorldScreen;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.game.manager.ItemRepository;
import net.mostlyoriginal.game.manager.RecipeRepository;
import net.mostlyoriginal.game.system.*;
import net.mostlyoriginal.game.system.control.*;
import net.mostlyoriginal.game.system.map.*;
import net.mostlyoriginal.game.system.mechanics.HopperDetectionSystem;
import net.mostlyoriginal.game.system.mechanics.MachineHopperDetectionSystem;
import net.mostlyoriginal.game.system.mechanics.MachineRecipeSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;
import net.mostlyoriginal.game.system.view.MyClearScreenSystem;
import net.mostlyoriginal.plugin.OperationsPlugin;
import net.mostlyoriginal.plugin.ProfilerPlugin;

/**
 * Example main game screen.
 *
 * @author Daan van Yperen
 */
public class GameScreen extends WorldScreen {

    public static final String BACKGROUND_COLOR_HEX = "969291";

    Class nextScreen;

    @Override
    protected World createWorld() {
        RenderBatchingSystem renderBatchingSystem;
        return new World(new WorldConfigurationBuilder()
                .dependsOn(EntityLinkManager.class, ProfilerPlugin.class, OperationsPlugin.class)
                .with(
                        new SuperMapper(),
                        //new EmotionService(),
                        new FontManager(),
                        new TagManager(),
                        new ItemRepository(),
                        new RecipeRepository(),
                        new PickupManager()
                        //new TutorialService()
                )
                .with(
                        // Replace with your own systems!
                        new CameraSystem(2),
                        new MyClearScreenSystem(Color.valueOf(BACKGROUND_COLOR_HEX)),
                        new GameScreenAssetSystem(),

                        new MapSpawnerSystem(),
                        new MapSystem(),
                        new MapCollisionSystem(),

                        new PlayerControlSystem(),
                        new ShopperControlSystem(),

                        new PickupSystem(),
                        new DesireSystem(),
                        new GridPosSystem(),

                        new PayingSystem(),

                        new HopperDetectionSystem(),
                        new MachineHopperDetectionSystem(),
                        new MachineRecipeSystem(),

                        new MapRenderSystem(),
                        renderBatchingSystem = new RenderBatchingSystem(),
                        new MyAnimRenderSystem(renderBatchingSystem),
                        new MyLabelRenderSystem(renderBatchingSystem),
                        new MapRenderInFrontSystem()
//                        new TransitionSystem(GdxArtemisGame.getInstance(), this)
                ).build());
    }

}
