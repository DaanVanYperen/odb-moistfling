package net.mostlyoriginal.game.system.render;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class RenderBackgroundSystem extends BaseSystem {

    private SpriteBatch batch;
    private SpriteBatch batch2;
    private CameraSystem cameraSystem;
    private GameScreenAssetSystem assetSystem;

    private float lax1;
    private float lax2;
    private float lax3;


    @Override
    protected void initialize() {
        super.initialize();
        batch = new SpriteBatch(200);
        batch2 = new SpriteBatch(200);
    }

    @Override
    protected void processSystem() {
        TextureRegion stars = (TextureRegion) assetSystem.get("background_1").getKeyFrame(0);
        TextureRegion clouds = (TextureRegion) assetSystem.get("background_2").getKeyFrame(0);
        TextureRegion clouds2 = (TextureRegion) assetSystem.get("background_3").getKeyFrame(0);

        lax1 += world.delta * 10f;
        lax2 += world.delta * 20f;
        lax3 += world.delta * 30f;

        if (lax1 >= stars.getRegionHeight()) lax1 -= stars.getRegionHeight();
        if (lax2 >= stars.getRegionHeight()) lax2 -= stars.getRegionHeight();
        if (lax3 >= stars.getRegionHeight()) lax3 -= stars.getRegionHeight();


        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
        for (int y = 0; y < (GameRules.SCREEN_HEIGHT / stars.getRegionHeight()) + 2; y++) {
            for (int x = 0; x < (GameRules.SCREEN_WIDTH / stars.getRegionWidth()) + 1; x++) {
                batch.draw(stars, x * stars.getRegionWidth(), y * stars.getRegionHeight() - lax1);
            }
        }

        batch.end();

        E player = E.withTag("player");

        float parX = (player.posX()*0.25f) % stars.getRegionWidth();
        float parY = (player.posY()*0.25f) % stars.getRegionHeight();

        batch2.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch2.begin();
        //batch2.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int y = 0; y < (GameRules.SCREEN_HEIGHT / stars.getRegionHeight()) + 2; y++) {
            for (int x = 0; x < (GameRules.SCREEN_WIDTH / stars.getRegionWidth()) + 2; x++) {
                batch2.draw(clouds, x * clouds.getRegionWidth() - parX, y * clouds.getRegionHeight() - parY);
            }
        }

        parX = (player.posX() * 0.5f) % stars.getRegionWidth();
        parY = (player.posY() * 0.5f) % stars.getRegionHeight();

        for (int y = 0; y < (GameRules.SCREEN_HEIGHT / stars.getRegionHeight()) + 2; y++) {
            for (int x = 0; x < (GameRules.SCREEN_WIDTH / stars.getRegionWidth()) + 2; x++) {
                batch2.draw(clouds2, x * clouds.getRegionWidth() - parX, y * clouds.getRegionHeight()  - parY);
            }
        }
        batch2.end();


    }

}