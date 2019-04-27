package net.mostlyoriginal.game.system.view;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

/**
 * @author Daan van Yperen
 */
public class MyClearScreenSystem extends BaseSystem {

    public Color color;

    public MyClearScreenSystem() {
        this(Color.BLACK);
    }

    public MyClearScreenSystem(Color color) {
        this.color = color;
    }

    @Override
    protected void processSystem( ) {
        Gdx.gl.glClearColor(color.r, color.g,color.b,color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
