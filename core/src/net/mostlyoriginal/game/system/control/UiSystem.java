package net.mostlyoriginal.game.system.control;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.mostlyoriginal.api.component.mouse.MouseCursor;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.system.common.FluidSystem;
import net.mostlyoriginal.game.system.logic.LeakSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class UiSystem extends BaseSystem {
    private boolean released = false;
    private LeakSystem leakSystem;
    GameScreenAssetSystem assetSystem;
    private CameraSystem cameraSystem;
    private SpriteBatch batch;

    @Override
    protected void initialize() {
        super.initialize();
        batch = new SpriteBatch(1000);
    }


    @Override
    protected void begin() {
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.setColor(new Color(1f,1f,1f,0.4f));
        batch.begin();
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void processSystem() {

        batch.draw(((TextureRegion)assetSystem.get(GameRules.sfxOn?"icon_sound":"icon_sound_off").getKeyFrame(0)),GameRules.SCREEN_WIDTH / 2 - 50,GameRules.SCREEN_HEIGHT / 2 - 50);
        batch.draw(((TextureRegion)assetSystem.get(GameRules.musicOn ?"icon_music":"icon_music_off").getKeyFrame(0)),GameRules.SCREEN_WIDTH / 2 - 100,GameRules.SCREEN_HEIGHT / 2 - 50);
        batch.draw(((TextureRegion)assetSystem.get("icon_reset").getKeyFrame(0)),GameRules.SCREEN_WIDTH / 2 - 150,GameRules.SCREEN_HEIGHT / 2 - 50);

            if (Gdx.input.isTouched()) {
                float posX = Gdx.input.getX()/2;
                float posY = GameRules.SCREEN_HEIGHT/2 - Gdx.input.getY()/2;
                if (released && posY > GameRules.SCREEN_HEIGHT / GameRules.CAMERA_ZOOM - 50) {
                    if (posX > GameRules.SCREEN_WIDTH / GameRules.CAMERA_ZOOM - 50) {
                        released = false;
                        GameRules.sfxOn = !GameRules.sfxOn;
                        //sfxButton.anim(GameRules.sfxOn ? "icon_sound" : "icon_sound_off");

                    } else if (posX > GameRules.SCREEN_WIDTH / GameRules.CAMERA_ZOOM - 100) {
                        released = false;
                        GameRules.musicOn = !GameRules.musicOn;
                        if (GameRules.musicOn) {
                            GameRules.music.play();
                        } else {
                            GameRules.music.pause();
                        }
                        //musicButton.anim(GameRules.musicOn ? "icon_music" : "icon_music_off");
                    } else if (posX > GameRules.SCREEN_WIDTH / GameRules.CAMERA_ZOOM - 150) {
                        released=false;
                        leakSystem.forceDeath();
                        E.E().playSound("astronaut-pops");
                    }
                }
            } else {
                released=true;
            }

    }
}
