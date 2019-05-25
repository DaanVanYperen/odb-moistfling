package net.mostlyoriginal.game.system.render;

import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import net.mostlyoriginal.game.component.future.ParticleEffect;

/**
 * @author Daan van Yperen
 */
@All(ParticleEffect.class)
public class ParticleEffectSystem extends IteratingSystem {

    private final ParticleEffectStrategy particleEffectStrategy;

    public static final Color COLOR_WHITE_TRANSPARENT = new Color(1f, 1f, 1f, 0.5f);
    public static final Color COLOR_BLACK_TRANSPARENT = new Color(0f, 0f, 0f, 0.5f);

    public ParticleEffectSystem(ParticleEffectStrategy particleEffectStrategy) {
        this.particleEffectStrategy = particleEffectStrategy;
    }

    @Override
    protected void initialize() {
        super.initialize();
        world.inject(this.particleEffectStrategy);
    }

    @Override
    protected void process(int id) {
        particleEffectStrategy.process(id);
    }

    public interface ParticleEffectStrategy {
        void process(int entityId);
    }
}
