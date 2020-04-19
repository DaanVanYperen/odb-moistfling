package net.mostlyoriginal.game.system.render;

import com.artemis.E;
import com.artemis.Entity;
import com.artemis.annotations.All;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Beamed;
import net.mostlyoriginal.game.system.common.FluidSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
@All(Beamed.class)
public class BeamedSystem extends FluidSystem {

    E beam = null;
    private GameScreenAssetSystem gameScreenAssetSystem;

    @Override
    public void inserted(Entity e) {
        E valueE = E.E(e);
        valueE.tint(Color.valueOf("9cdb43"));
    }

    @Override
    public void removed(Entity e) {
        E valueE = E.E(e);
        valueE.removeTint();
    }

    Vector2 v2 = new Vector2();
    Vector2 beamOrigin = new Vector2();

    @Override
    protected void end() {
        super.end();
        if ( beam != null && subscription.getEntities().isEmpty() ) {
            beam.deleteFromWorld();
            beam = null;
        }
    }

    @Override
    protected void process(E e) {
        if (beam == null) {
            beam = E.E()
                    .anim("tractor_beam")
                    .pos(0, 0)
                    .renderLayer(GameRules.LAYER_ITEM - 1);
        }
        E p = E.withTag("player");
        if (p != null) {

            Animation<TextureRegion> animation = gameScreenAssetSystem.get(e.animId());
            final TextureRegion frame = animation.getKeyFrame(0, false);

            beamOrigin.set(-18, 20).rotate(p.angleRotation()).add(p.posX(),p.posY());

            beam.posX(beamOrigin.x + p.boundsCx());
            beam.posY(beamOrigin.y);

            v2.set(e.boundsCx() + e.posX(), e.boundsCy() + e.posY()).sub(beamOrigin.x+ p.boundsCx(), beamOrigin.y+p.boundsCy());
            float distance = v2.len();

            beam.origin(0f,0.5f).animSize(distance, 48f).angleRotation(v2.angle());
        }
    }
}
