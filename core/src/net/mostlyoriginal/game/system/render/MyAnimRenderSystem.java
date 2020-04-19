package net.mostlyoriginal.game.system.render;
/**
 * @author Daan van Yperen
 */

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Origin;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.basic.Scale;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.graphics.Invisible;
import net.mostlyoriginal.api.component.graphics.Render;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.api.manager.FontManager;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Item;

/**
 * Render and progress animations.
 *
 * @author Daan van Yperen
 * @see Anim
 */
@Wire
public class MyAnimRenderSystem extends DeferredEntityProcessingSystem {

    protected M<Pos> mPos;
    protected M<Anim> mAnim;
    protected M<Tint> mTint;
    protected M<Angle> mAngle;
    protected M<Scale> mScale;
    protected M<Origin> mOrigin;
    protected M<Item> mItem;
    private FontManager fontManager;
    private BitmapFont font;

    protected CameraSystem cameraSystem;
    protected AbstractAssetSystem abstractAssetSystem;

    protected SpriteBatch batch;
    private Origin DEFAULT_ORIGIN = new Origin(0.5f, 0.5f);

    public MyAnimRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(Pos.class, Anim.class, Render.class).exclude(Invisible.class), principal);
    }

    @Override
    protected void initialize() {
        super.initialize();
        batch = new SpriteBatch(2000);
        font = fontManager.getFont("5x5");
    }

    @Override
    protected void processSystem() {
        super.processSystem();
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.begin();
    }

    @Override
    protected void end() {
        batch.end();
    }

    private GlyphLayout glyphLayout = new GlyphLayout();

    protected void process(final int e) {

        final Anim anim = mAnim.get(e);
        final Pos pos = mPos.get(e);
        final Angle angle = mAngle.getSafe(e, Angle.NONE);
        final float scale = mScale.getSafe(e, Scale.DEFAULT).scale;
        final Origin origin = mOrigin.getSafe(e, DEFAULT_ORIGIN);

        batch.setColor(mTint.getSafe(e, Tint.WHITE).color);

        if (anim.id != null) drawAnimation(anim, angle, origin, pos, anim.id, scale);
        if (anim.id2 != null) drawAnimation(anim, angle, origin, pos, anim.id2, scale);

        anim.age += world.delta * anim.speed;
        if (mItem.has(e)) {
            renderStackCount(e, pos);
        }
    }

    Tint FONT_TINT = new Tint(1f,1f,1f,0.8f);
    Tint FONT_SHADOW_TINT = new Tint(0f,0f,0f,0.6f);

    private void renderStackCount(int e, Pos pos) {
        int count = mItem.get(e).count;
        if (count > 1) {
            font.getData().setScale(1f);
            final String countText = "x"+count ;
            glyphLayout.setText(font, countText);

            font.setColor(FONT_SHADOW_TINT.color);
            font.draw(batch, countText, pos.xy.x + GameRules.CELL_SIZE - glyphLayout.width -3, pos.xy.y + 10 -1);
            font.setColor(FONT_TINT.color);
            font.draw(batch, countText, pos.xy.x + GameRules.CELL_SIZE - glyphLayout.width -4, pos.xy.y + 10);
        }
    }

    /**
     * Pixel perfect aligning.
     */
    public float roundToPixels(final float val) {
        // since we use camera zoom rounding to integers doesn't work properly.
        return ((int) (val * cameraSystem.zoom)) / (float) cameraSystem.zoom;
    }

    private void drawAnimation(final Anim animation, final Angle angle, final Origin origin, final Pos position, String id, float scale) {

        // don't support backwards yet.
        if (animation.age < 0) return;

        final Animation<TextureRegion> gdxanim = (Animation<TextureRegion>) abstractAssetSystem.get(id);
        if (gdxanim == null) return;

        final TextureRegion frame = gdxanim.getKeyFrame(animation.age, gdxanim.getPlayMode() != Animation.PlayMode.NORMAL && animation.loop);

        float ox = frame.getRegionWidth() * scale * origin.xy.x;
        float oy = frame.getRegionHeight() * scale * origin.xy.y;
        float y = roundToPixels(position.xy.y);
        float x = roundToPixels(position.xy.x);

        if (animation.flippedX && angle.rotation == 0) {
            // mirror
            batch.draw(frame.getTexture(),
                    x,
                    y,
                    ox,
                    oy,
                    frame.getRegionWidth() * scale,
                    frame.getRegionHeight() * scale,
                    1f,
                    1f,
                    angle.rotation,
                    frame.getRegionX(),
                    frame.getRegionY(),
                    frame.getRegionWidth(),
                    frame.getRegionHeight(),
                    true,
                    false);

        } else if (angle.rotation != 0) {
            batch.draw(frame,
                    x,
                    y,
                    ox,
                    oy,
                    frame.getRegionWidth() * scale,
                    frame.getRegionHeight() * scale, 1, 1,
                    angle.rotation);
        } else {
            batch.draw(frame,
                    x,
                    y,
                    frame.getRegionWidth() * scale,
                    frame.getRegionHeight() * scale);
        }
    }
}