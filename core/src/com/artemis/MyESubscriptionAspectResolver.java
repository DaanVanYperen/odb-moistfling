package com.artemis;

import com.artemis.injection.AspectFieldResolver;
import com.artemis.injection.FieldResolver;
import com.artemis.utils.reflect.Field;

/**
 *  Resolver with support for ESubscription.
 */
public class MyESubscriptionAspectResolver implements FieldResolver {

    // we need to delegate to the aspect field resolver.
    private AspectFieldResolver aspectFieldResolver = new AspectFieldResolver();

    @Override
    public void initialize(World world) {
        aspectFieldResolver.initialize(world);
    }

    @Override
    public Object resolve(Object target, Class<?> fieldType, Field field) {
        if (ESubscription.class == fieldType) {
            return new ESubscription(((EntitySubscription) aspectFieldResolver.resolve(target, EntitySubscription.class, field)));
        }
        return null;
    }
}
