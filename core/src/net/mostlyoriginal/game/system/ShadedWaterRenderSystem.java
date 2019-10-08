package net.mostlyoriginal.game.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.GameRules;

import java.util.Vector;

import static com.artemis.C.Sprite;

/**
 * Borrowed from
 * https://gamedev.stackexchange.com/questions/163941/2d-water-animation-is-jagged-and-not-smooth
 *
 * @author Daan van Yperen
 */
public class ShadedWaterRenderSystem extends BaseSystem {

    public ShaderProgram waterProgram;
    final SpriteBatch batch = new SpriteBatch(500);
    private GLTexture noiseTexture;
    private CameraSystem cameraSystem;
    private boolean compiled;
    private Texture img;

    @Override
    protected void initialize() {
        super.initialize();
    }

    float time = 0f;

    float[] velocity = new float[] {.006f, .007f};

    @Override
    protected void processSystem() {

        if ( img == null ) {
            waterProgram = new ShaderProgram(Gdx.files.internal("shader/water.vertex"), Gdx.files.internal("shader/water.fragment"));
            compiled = waterProgram.isCompiled();
            if ( !compiled) Gdx.app.error("Shader", waterProgram.getLog());

            img = new Texture("water.png");
            //img.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            //img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            if ( compiled ) {
                noiseTexture = new Texture("waternoise.png");
                //noiseTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                noiseTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            }
        }

        time += world.delta;

        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.setColor(new Color(1f,1f,1f,1f));
        batch.begin();
        if ( compiled ) {
            noiseTexture.bind();
            waterProgram.setUniformf("u_time", time);
            batch.setShader(waterProgram);
        }
        batch.draw(img, -32,-32 , GameRules.SCREEN_WIDTH/2+32, GameRules.SCREEN_HEIGHT/2+32);
        batch.end();
    }
}
