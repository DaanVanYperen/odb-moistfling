package net.mostlyoriginal.api.plugin.fluidextensions;

import com.artemis.E;
import com.artemis.EBag;
import com.artemis.EntitySubscription;

import java.util.Iterator;

/**
 * Provides {@code E} access to EntitySubscription with entity subscription.
 *
 * Does not recycle iterator so use this judiciously.
 *
 * @author Daan van Yperen
 */
public class ESubscription implements Iterable<E> {
    private final EntitySubscription wrappedSubscription;

    public ESubscription(EntitySubscription wrappedSubscription) {
        this.wrappedSubscription = wrappedSubscription;
    }

    @Override
    public Iterator<E> iterator() {
        return get().iterator();
    }

    public EBag get() {
        return new EBag(wrappedSubscription.getEntities());
    }

    public int size() {
        return wrappedSubscription.getEntities().size();
    }
}
