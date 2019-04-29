package net.mostlyoriginal.game.system.control;

import com.artemis.E;

/**
 * @author Daan van Yperen
 */
public class NameHelper {
    public static final String getActor_player_face() {
        return "actor_player_face_" +(E.withTag("player").playerAge()+1);
    }
}
