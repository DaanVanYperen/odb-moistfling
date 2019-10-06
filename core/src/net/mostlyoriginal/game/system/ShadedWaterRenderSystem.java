package net.mostlyoriginal.game.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
    private Sprite sprite;

    @Override
    protected void initialize() {
        super.initialize();

        waterProgram = new ShaderProgram(Gdx.files.internal("shader/water.vertex"), Gdx.files.internal("shader/water.fragment"));
        if ( !waterProgram.isCompiled() ) throw new RuntimeException("Compilation failed." + waterProgram.getLog());

        Texture img = new Texture("water.png");
        img.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        noiseTexture = new Texture("waternoise.png");
        noiseTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        sprite = new Sprite(img);
        sprite.setSize(GameRules.SCREEN_WIDTH, GameRules.SCREEN_HEIGHT);

        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        noiseTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    float time = 0f;

    float[] velocity = new float[] {.006f, .007f};

    @Override
    protected void processSystem() {
        time += world.delta;
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        noiseTexture.bind();
        waterProgram.setUniformf("u_noise_scale", 0.1f);
        waterProgram.setUniform2fv("u_noise_scroll_velocity", velocity, 0, 2);
        waterProgram.setUniformf("u_distortion", 0.04f);
        waterProgram.setUniformf("u_time", time);
        batch.setShader(waterProgram);
        batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        batch.end();
    }
}
