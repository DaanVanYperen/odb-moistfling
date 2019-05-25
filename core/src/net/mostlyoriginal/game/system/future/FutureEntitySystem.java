package net.mostlyoriginal.game.system.future;

import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.mostlyoriginal.game.component.future.FutureEntity;
import net.mostlyoriginal.game.component.future.Properties;

/**
 * Implement EntityAssemblyStrategy to produce the entities.
 *
 * @author Daan van Yperen
 */
@All(FutureEntity.class)
public class FutureEntitySystem extends IteratingSystem {

    private final EntityAssemblyStrategy entityAssemblyStrategy;

    M<FutureEntity> mFutureEntity;
    M<Properties> mFutureEntityProperties;

    /**
     * @param strategy Strategy so we can refer to E from the game.
     */
    public FutureEntitySystem(EntityAssemblyStrategy strategy) {
        this.entityAssemblyStrategy = strategy;
    }

    @Override
    protected void initialize() {
        super.initialize();
        world.inject(entityAssemblyStrategy);
    }

    @Override
    protected void end() {
        entityAssemblyStrategy.end();
    }

    @Override
    protected void process(int entityId) {
        entityAssemblyStrategy.createInstance(entityId);

        // safe to call when components missing.
        mFutureEntity.remove(entityId);
        mFutureEntityProperties.remove(entityId);
    }

    public interface EntityAssemblyStrategy {

        /** entiyId to decorate. Will asume the method will recycle the existing entity! */
        void createInstance(int entityId);
        void end();
    }
}
