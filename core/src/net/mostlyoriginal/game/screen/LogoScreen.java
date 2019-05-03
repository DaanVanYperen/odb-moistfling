package net.mostlyoriginal.game.screen;

import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import net.mostlyoriginal.api.manager.FontManager;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.api.system.render.ClearScreenSystem;
import net.mostlyoriginal.game.GdxArtemisGame;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.render.MyLabelRenderSystem;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.system.view.LogoScreenAssetSystem;
import net.mostlyoriginal.game.system.view.LogoScreenSetupSystem;
import net.mostlyoriginal.plugin.OperationsPlugin;

/**
 * Intro screen that also shows all enabled artemis-odb features for a couple of seconds.
 *
 * @author Daan van Yperen
 */
public class LogoScreen extends TransitionableWorldScreen {

    protected World createWorld() {

        final RenderBatchingSystem renderBatchingSystem;

        return new World(new WorldConfigurationBuilder()
                .dependsOn(OperationsPlugin.class)
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        // supportive
                        new SuperMapper(),
                        new TagManager(),
                        new FontManager(),
                        new CameraSystem(1),
                        new LogoScreenAssetSystem()
                ).with(WorldConfigurationBuilder.Priority.LOW,
                        // processing
                        // animation
                        new ClearScreenSystem(Color.valueOf("969291")),
                        renderBatchingSystem = new RenderBatchingSystem(),
                        new MyAnimRenderSystem(renderBatchingSystem),
                        new MyLabelRenderSystem(renderBatchingSystem),
                        new LogoScreenSetupSystem(),
                        new TransitionSystem(GdxArtemisGame.getInstance(), this)
                ).build());
    }

}
