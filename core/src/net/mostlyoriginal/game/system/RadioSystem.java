package net.mostlyoriginal.game.system;

import com.artemis.BaseSystem;
import com.artemis.E;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class RadioSystem extends BaseSystem {
    private boolean wasSubmerged;

    GameScreenAssetSystem gameScreenAssetSystem;

    @Override
    protected void processSystem() {
        E e = E.withTag("radio");

        if ( e != null ) {
            if ( e.hasSubmerged() != wasSubmerged )
            {
                wasSubmerged = e.hasSubmerged();

                if ( wasSubmerged ) {
                    if ( GameRules.music.isPlaying() )
                    GameRules.music.pause();
                } else                     if ( !GameRules.music.isPlaying() )
                     GameRules.music.play();
            }
        }
    }
}
