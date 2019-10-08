package com.artemis;

/**
 * Plugin to enable fluid entity functionality in your world.
 *
 * - Enables fluid entity functionality.
 * - Adds support for {@code com.artemis.ESubscription} field dependency injection.
 *   (when annotated with {@code @com.artemis.annotations.All}, {@code @com.artemis.annotations.One} and/or {@code @com.artemis.annotations.Exclude})
 * <p>
 * This file is generated.
 * <p>
 * For artemis-odb developers: Make sure you edit the file in  artemis-fluid-core-utility-sources, and not a
 * generated-sources version.
 */
public final class MyFluidEntityPlugin implements ArtemisPlugin {
    public void setup(WorldConfigurationBuilder b) {
        b.dependsOn(WorldConfigurationBuilder.Priority.HIGH, SuperMapper.class);
        b.register(new MyESubscriptionAspectResolver());
    }

}
