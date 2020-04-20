package net.mostlyoriginal.game.system.render;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Invisible;
import net.mostlyoriginal.api.component.graphics.Render;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.BitmapFontAsset;
import net.mostlyoriginal.api.component.ui.Font;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.manager.FontManager;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;

/**
 * @author Daan van Yperen
 */
public class MyLabelRenderSystem extends DeferredEntityProcessingSystem {

    protected M<Pos> mPos;
    protected M<Label> mLabel;
    protected M<Tint> mTint;
    protected M<Font> mFont;
    protected M<BitmapFontAsset> mBitmapFontAsset;

    protected CameraSystem cameraSystem;

    protected SpriteBatch batch;
    private GlyphLayout glyphLayout = new GlyphLayout();

    public MyLabelRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(Pos.class, Label.class, Render.class, BitmapFontAsset.class).exclude(Invisible.class), principal);
        batch = new SpriteBatch(1000);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.setColor(new Color(1f,0f,0f,1f));
        batch.begin();
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    protected void process(final int e) {

        final Label label = mLabel.get(e);
        final Pos pos = mPos.get(e);

        if (label.text != null) {

            final BitmapFont font = mBitmapFontAsset.get(e).bitmapFont;

            font.getData().setScale(mFont.get(e).scale);
            font.setColor(mTint.getSafe(e, Tint.WHITE).color);

            switch ( label.align ) {
                case LEFT:
                    font.draw(batch, label.text, pos.xy.x, pos.xy.y);
                    break;
                case RIGHT:
                    glyphLayout.setText(font,label.text);
                    font.draw(batch, label.text, pos.xy.x - glyphLayout.width * 0.5f, pos.xy.y);
                    break;
            }
        }
    }
}
