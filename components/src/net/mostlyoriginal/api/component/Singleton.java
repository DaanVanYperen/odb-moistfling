package net.mostlyoriginal.api.component;

import com.artemis.annotations.UnstableApi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks component as a singleton.
 *
 * SingletonPlugin will inject singleton dependencies into systems, handling singleton lifecycle.
 *
 * @see SingletonPlugin
 * @author Daan van Yperen
 */
@Retention(RetentionPolicy.RUNTIME)
@UnstableApi
@Target(ElementType.TYPE)
public @interface Singleton {
}
