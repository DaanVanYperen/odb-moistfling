package net.mostlyoriginal.game.system.logic;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.logic.Transition;
import net.mostlyoriginal.game.screen.TransitionableWorldScreen;
import net.mostlyoriginal.game.system.view.FeatureScreenAssetSystem;

import static com.artemis.E.*;
import static net.mostlyoriginal.api.operation.OperationFactory.*;
import static net.mostlyoriginal.api.utils.Duration.*;

/**
 * Transition between screens.
 *
 * @author Daan van Yperen
 */
@Wire
public class TransitionSystem extends EntityProcessingSystem {

    private Game game;
    private final TransitionableWorldScreen parent;

    private float percentCovered = 100;
    private boolean closeCurtains = false;
    private SpriteBatch batch;
    private AbstractAssetSystem abstractAssetSystem;
    private TextureRegion transition_red;
    private TextureRegion transition_white;
    private CameraSystem cameraSystem;


    public TransitionSystem(Game game, TransitionableWorldScreen parent) {
        super(Aspect.all(Transition.class));
        this.game = game;
        this.parent = parent;
    }

    @Override
    protected void initialize() {
        super.initialize();
        batch = new SpriteBatch(2000);
    }

    /**
     * Transition to screen after delay in seconds.
     */
    public void transition(Class<? extends Screen> screen, float delay) {
        closeCurtains=true;
        E()
                .script(
                        sequence(
                                delay(seconds(delay)),
                                add(new Transition(screen))
                        )
                );
    }

    @Override
    protected void begin() {
        super.begin();

        if ( closeCurtains ) {
            percentCovered += world.delta*200f;
            if ( percentCovered > 100 )percentCovered=100;
        } else {
            percentCovered -= world.delta*200f;
            if ( percentCovered < 0 )percentCovered=0;
        }

        if (percentCovered > 0) {
            if (transition_red == null) {
                transition_red = (TextureRegion) abstractAssetSystem.get("transition_red").getKeyFrame(0);
                transition_white = (TextureRegion) abstractAssetSystem.get("transition_white").getKeyFrame(0);
            }

            batch.setProjectionMatrix(cameraSystem.camera.combined);
            batch.setColor(1f,1f,1f,1f);
            batch.begin();
            int actualWidth = (int)(GameRules.SCREEN_WIDTH / cameraSystem.zoom);
            int actualHeight = (int)(GameRules.SCREEN_HEIGHT / cameraSystem.zoom);
            batch.draw(transition_red, 0, 0, actualWidth * percentCovered*0.01f, actualHeight);
            batch.draw(transition_white, actualWidth -(actualWidth * percentCovered*0.01f), 0, actualWidth * percentCovered*0.01f, actualHeight);
            batch.end();
        }

    }

    @Override
    protected void dispose() {
        batch.dispose();
    }

    @Override
    protected void process(Entity e) {

        try {
            parent.target = E(e).transitionScreen();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
