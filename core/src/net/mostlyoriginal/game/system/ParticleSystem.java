package net.mostlyoriginal.game.system;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.badlogic.gdx.math.MathUtils.random;
import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class ParticleSystem extends BaseSystem {

    private Color BLOOD_COLOR = Color.valueOf("4B1924");
    private Color COLOR_WHITE = Color.valueOf("FFFFFF");
    private Color COLOR_DUST = Color.valueOf("D4CFB899");
    private Color COLOR_ACID = Color.valueOf("5F411CDD");
    private Color COLOR_LASER = Color.valueOf("FEE300");

    private Builder bakery = new Builder();
    private GameScreenAssetSystem assetSystem;
    private CameraSystem cameraSystem;

    public float cooldown = 0;

    private void triggerSprinkler(E e) {
        for (int i = 0; i < MathUtils.random(1, 2); i++) {
            sand(e.posX() + MathUtils.random(0, e.boundsMaxx()), e.posY(), -90 + MathUtils.random(-2, 2), MathUtils.random(10, 40));
        }
    }


    public void dust(float x, float y, float angle) {
        bakery
                .color(COLOR_DUST)
                .at(x, y)
                .angle(angle, angle)
                .speed(10, 15)
                .fadeAfter(0.1f)
                .rotateRandomly()
                .size(1, 3)
                .create(1, 3);
    }

    public void sand(float x, float y, float angle, int force) {
        bakery
                .color(COLOR_LASER)
                .at(x, y)
                .angle(angle, angle)
                .speed(force, force + 5)
                .fadeAfter(1f + MathUtils.random(0f, 2f))
                .rotateRandomly()
                .size(1f, 2f)
                .solid()
                .create(1, 1);
    }

    public void confettiBomb(float x, float y) {
        confetti(x,y, "confetti_white", 15, 20);
        confetti(x,y, "confetti_red", 15, 20);
        confetti(x,y, "confetti_blue", 15, 20);
        balloons(x,y, "balloon_white", 2,5);
        balloons(x,y, "balloon_red", 2,5);
        balloons(x,y, "balloon_blue", 2,5);
//
//
//        if ( MathUtils.random(1,100) < 30 ) {
//            bakery
//                    .at((int) x, (int) y, (int) x , (int) y )
//                    .angle(0, 360)
//                    .speed(5, 10)
//                    .anim("hamburger")
//                    .fadeAfter(8f)
//                    .rotateRandomly()
//                    .slowlySplatDown()
//                    .friction(0)
//                    .size(2, 2)
//                    .angularMomentum(5)
//                    .create(1, 3);
//        }
        if ( MathUtils.random(1,100) < 50 ) {
            bakery
                    .at((int) x, (int) y, (int) x , (int) y )
                    .angle(45, 75)
                    .speed(100)
                    .spriteAngle(0)
                    .anim("eagle")
                    .fadeAfter(8f)
                    .friction(0f)
                    .size(1, 1)
                    .angularMomentum(0)
                    .create(1,1);
        }
    }

    public void confetti(float x, float y, String art, int minCount, int maxCount) {
        bakery
                .at((int) x - 5, (int) y - 5, (int) x + 5, (int) y + 5)
                .angle(0, 360)
                .speed(5, 100)
                .anim(art)
                .fadeAfter(8f)
                .rotateRandomly()
                .slowlySplatDown()
                .friction(1f)
                .size(1, 2)
                .angularMomentum(40)
                .create(minCount, maxCount);
    }


    public void balloons(float x, float y, String art, int minCount, int maxCount) {
        bakery
                .at((int) x - 5, (int) y - 5, (int) x + 5, (int) y + 5)
                .angle(0, 360)
                .speed(5, 20)
                .anim(art)
                .fadeAfter(8f)
                .rotateRandomly()
                .slowlyFloatUp()
                .friction(0f)
                .size(1, 1)
                .angularMomentum(0)
                .create(minCount, maxCount);
    }

    Vector2 v2 = new Vector2();

    public E spawnVanillaParticle(String anim, float x, float y, float angle, float speed, float scale, float emitterVx, float emitterVy, float friction, float angularMomentum, float spriteAngle) {

        v2.set(speed, 0).setAngle(angle);

        TextureRegion frame = ((Animation<TextureRegion>) assetSystem.get(anim)).getKeyFrame(0);

        return E.E()
                .pos(x - (scale * frame.getRegionWidth() * 0.5f), y - (scale * frame.getRegionHeight() * 0.5f))
                .anim(anim != null ? anim : "particle")
                .scale(scale)
                .angleRotate(angle - spriteAngle)
                .renderLayer(GameScreenAssetSystem.LAYER_PARTICLES)
                .origin(scale / 2f, scale / 2f)
                .bounds(0, 0, scale, scale)
                .physicsVx(v2.x + emitterVx)
                .physicsVy(v2.y + emitterVy)
                .physicsVr(angularMomentum)
                .physicsFriction(friction);
    }

    float cooldown2 = 0;

    @Override
    protected void processSystem() {

    }

    private class Builder {
        private Color color;
        private boolean withGravity;
        private int minX;
        private int maxX;
        private int minY;
        private int maxY;
        private float minAngle;
        private float maxAngle;
        private int minSpeed;
        private int maxSpeed;
        private float minScale;
        private float maxScale;
        private boolean withSolid;
        private float gravityY;
        private float fadeDelay;

        private Tint tmpFrom = new Tint();
        private Tint tmpTo = new Tint();
        private float rotateR = 0;
        private float emitterVx;
        private float emitterVy;
        private String anim;
        private float friction;
        private float angularMomentum;
        private float spriteAngle = -90;

        public Builder() {
            reset();
        }

        Builder color(Color color) {
            this.color = color;
            return this;
        }

        void create(int count) {
            create(count, count);
        }

        void create(int minCount, int maxCount) {
            for (int i = 0, s = random(minCount, maxCount); i < s; i++) {
                final E e = spawnVanillaParticle(
                        anim,
                        random(minX, maxX),
                        random(minY, maxY),
                        random(minAngle, maxAngle),
                        random(minSpeed, maxSpeed),
                        random(minScale, maxScale),
                        emitterVx, emitterVy, friction, angularMomentum, spriteAngle)
                        .tint(color.r, color.g, color.b, color.a);

                if (withGravity) {
                    e.gravity();
                    e.gravityY(gravityY);
                }
                if (rotateR != 0) {
                    e.physicsVr(rotateR).angle();
                }
                if (fadeDelay > 0) {
                    e.script(sequence(
                            delay(fadeDelay),
                            JamOperationFactory.tintBetween(tmpFrom, tmpTo, 0.5f),
                            deleteFromWorld()
                    ));
                }
            }
            reset();
        }

        Builder slowlySplatDown() {
            this.withGravity = true;
            this.gravityY = -0.5f;
            return this;
        }

        Builder slowlyFloatUp() {
            this.withGravity = true;
            this.gravityY = 0.5f;
            return this;
        }

        private void reset() {
            color = COLOR_WHITE;
            withGravity = false;
            minX = 0;
            maxX = 0;
            minY = 0;
            maxY = 0;
            minAngle = 0;
            maxAngle = 0;
            minSpeed = 0;
            maxSpeed = 0;
            minScale = 1;
            maxScale = 1;
            gravityY = 1;
            fadeDelay = -1;
            withSolid = false;
            rotateR = 0;
        }

        public Builder angle(float minAngle, float maxAngle) {
            this.minAngle = minAngle;
            this.maxAngle = maxAngle;
            return this;
        }

        public Builder angle(int angle) {
            this.minAngle = angle;
            this.maxAngle = angle;
            return this;
        }

        public Builder speed(int minSpeed, int maxSpeed) {
            this.minSpeed = minSpeed;
            this.maxSpeed = maxSpeed;
            return this;
        }

        public Builder speed(int speed) {
            this.minSpeed = speed;
            this.maxSpeed = speed;
            return this;
        }

        public Builder size(float minScale, float maxScale) {
            this.minScale = minScale;
            this.maxScale = maxScale;
            return this;
        }

        public Builder size(int size) {
            this.minScale = size;
            this.maxScale = size;
            return this;
        }

        public Builder solid() {
            withSolid = true;
            return this;
        }

        public Builder at(float x, float y) {
            minX = maxX = (int) x;
            minY = maxY = (int) y;
            return this;
        }

        public Builder at(int minX, int minY, int maxX, int maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
            return this;
        }

        public Builder fadeAfter(float delay) {
            this.fadeDelay = delay;
            tmpFrom = new Tint(color);
            tmpTo = new Tint(color);
            tmpTo.color.a = 0;
            return this;
        }

        public Builder friction(float friction) {
            this.friction = friction;
            return this;
        }

        public Builder angularMomentum(float angularMomentum) {
            this.angularMomentum = angularMomentum;
            return this;
        }
        public Builder spriteAngle(float spriteAngle) {
            this.spriteAngle = spriteAngle;
            return this;
        }

        public Builder rotateRandomly() {
            rotateR = MathUtils.random(-100f, 100f);
            return this;
        }

        public Builder emitterVelocity(float emitterVx, float emitterVy) {
            this.emitterVx = emitterVx;
            this.emitterVy = emitterVy;
            return this;
        }

        public Builder anim(String anim) {
            this.anim = anim;
            return this;
        }
    }

}
