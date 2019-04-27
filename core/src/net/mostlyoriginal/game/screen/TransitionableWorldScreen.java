package net.mostlyoriginal.game.screen;

import com.artemis.utils.reflect.ClassReflection;
import com.artemis.utils.reflect.ReflectionException;
import com.badlogic.gdx.Screen;
import net.mostlyoriginal.api.screen.core.WorldScreen;
import net.mostlyoriginal.game.GdxArtemisGame;

/**
 * @author Daan van Yperen
 */
public abstract class TransitionableWorldScreen extends WorldScreen {

    public Class<? extends Screen> target;

    @Override
    public void render(float delta) {
        super.render(delta);
        if (target != null) {
            try {
                GdxArtemisGame.getInstance().setScreen(ClassReflection.newInstance(target));
                dispose();
            } catch (ReflectionException e) {
            }
        }
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
