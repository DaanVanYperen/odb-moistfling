package net.mostlyoriginal.api.plugin.fluidextensions;

import com.artemis.ArtemisPlugin;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.annotations.UnstableApi;
import com.artemis.injection.AspectFieldResolver;
import com.artemis.injection.FieldResolver;
import com.artemis.utils.reflect.Field;

/**
 * Extends aspect subscription dependency injection with fluid entity subscription support.
 *
 * Usage:
 * {@code @All(Enemies.class) ESubscription mySubscription;}
 *
 * @author Daan van Yperen
 */
@UnstableApi
public class ESubscriptionPlugin implements ArtemisPlugin {
    @Override
    public void setup(WorldConfigurationBuilder b) {
        b.register(new EBagAspectResolver());
    }

    /** Resolves singleton fields in systems. */
    private static class EBagAspectResolver implements FieldResolver {

        // we need to delegate to the aspect field resolver.
        private AspectFieldResolver aspectFieldResolver = new AspectFieldResolver();

        @Override
        public void initialize(World world) {
            aspectFieldResolver.initialize(world);
        }

        @Override
        public Object resolve(Object target, Class<?> fieldType, Field field) {
            if (ESubscription.class == fieldType) {
                return new ESubscription(((EntitySubscription)aspectFieldResolver.resolve(target, EntitySubscription.class, field)));
            }
            return null;
        }
    }
}
