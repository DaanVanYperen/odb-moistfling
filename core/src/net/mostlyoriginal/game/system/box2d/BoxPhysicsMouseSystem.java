package net.mostlyoriginal.game.system.box2d;

import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.mouse.MouseCursorSystem;
import net.mostlyoriginal.game.component.box2d.Boxed;
import net.mostlyoriginal.game.component.interact.Tappable;
import net.mostlyoriginal.game.system.common.FluidSystem;

import static com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape.point;

/**
 * @author Daan van Yperen
 */
@All({Boxed.class, Tappable.class})
public class BoxPhysicsMouseSystem extends FluidSystem {

    private BoxPhysicsSystem boxPhysicsSystem;
    private CameraSystem cameraSystem;

    @Override
    protected void initialize() {
        super.initialize();
    }

    private Vector3 mouse = new Vector3();

    @Override
    protected void begin() {
        super.begin();
        mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        cameraSystem.camera.unproject(mouse);
    }


    QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture (Fixture fixture) {
            if (fixture.testPoint(mouse.x/BoxPhysicsSystem.PPM, mouse.y/BoxPhysicsSystem.PPM)) {
                E e = (E)fixture.getBody().getUserData();
                if ( e != null && e.hasTappable() ) {
                    e.tapped();
                }
                return false;
            } else
                return true;
        }
    };

    @Override
    protected void end() {
        super.end();
        if ( Gdx.input.isTouched()) {
            World box2d = boxPhysicsSystem.getBox2d();
            box2d.QueryAABB(callback, (mouse.x / BoxPhysicsSystem.PPM) - 1, (mouse.y / BoxPhysicsSystem.PPM) - 1, (mouse.x / BoxPhysicsSystem.PPM) + 1, (mouse.y / BoxPhysicsSystem.PPM) + 1);
        }
    }

    @Override
    protected void process(E e) {
        if ( e.hasTapped() ) {
            e.removeTapped();
        }
    }
}
