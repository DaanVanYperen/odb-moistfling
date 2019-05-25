package net.mostlyoriginal.game.events;

import net.mostlyoriginal.api.event.common.Event;

/**
 * @author Daan van Yperen
 */
public class EventItemPickup implements Event {
    public int id;

    public EventItemPickup(int id) {
        this.id = id;
    }
}
