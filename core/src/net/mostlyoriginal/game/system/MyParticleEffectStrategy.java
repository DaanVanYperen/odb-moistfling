package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.game.component.future.ParticleEffect;
import net.mostlyoriginal.game.system.render.ParticleEffectSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.badlogic.gdx.math.MathUtils.random;
import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * Handles assembling entities based on spawn requests.
 *
 * @todo this is a mess.
 * @author Daan van Yperen
 */
public class MyParticleEffectStrategy implements ParticleEffectSystem.ParticleEffectStrategy {

    public static final String EFFECT_POOF = "poof";
    public static final String EFFECT_BLACK_SPUTTER = "sputter";
    public static final String EFFECT_DROPLET = "droplet";
    public static final String EFFECT_SPLASH = "splash";

    private Builder bakery = new Builder();
    private Color BLOOD_COLOR = Color.valueOf("4B1924");
    private Color COLOR_WHITE = Color.valueOf("FFFFFF");
    private Color COLOR_ACID = Color.valueOf("5F411CDD");
    private Color COLOR_LASER = Color.valueOf("FEE300");
    private Color WATER_DROPLET = Color.valueOf("20D6C7");

    @Override
    public void process(int entityId) {
        final E source = E.E(entityId);

        ParticleEffect effect = source.getParticleEffect();
        switch (effect.type) {
            case EFFECT_POOF:
                spawnPoof(source.posX(), source.posY(), 40, 40, WATER_DROPLET);
                source.deleteFromWorld();
                break;
                case EFFECT_SPLASH:
                spawnSplash(source.posX(), source.posY(), 40, 40, WATER_DROPLET);
                source.deleteFromWorld();
                break;
            case EFFECT_DROPLET:
                spawnDroplet(source.posX(), source.posY(), 1, 1, WATER_DROPLET);
                source.deleteFromWorld();
                break;
            case EFFECT_BLACK_SPUTTER:
                spawnPoof(source.posX(), source.posY(), 2, 3, ParticleEffectSystem.COLOR_BLACK_TRANSPARENT);
                source.deleteFromWorld();
                break;
            default:
                throw new RuntimeException("Unknown particle effect " + entityId);
        }
    }


    public void spawnDroplet(float x, float y, int minCount, int maxCount, Color color) {
        bakery
                .at((int) x, (int) y, (int) x, (int) y)
                .anim("dot")
                .angle(-90,-90)
                .speed(5, 100)
                .color(color)
                .fadeAfter(0.05f)
                .rotateRandomly()
                .slowlySplatDown()
                .friction(1f)
                .size(1f, 1f)
                .angularMomentum(40)
                .create(minCount, maxCount);
    }


    public void spawnPoof(float x, float y, int minCount, int maxCount, Color color) {
        bakery
                .at((int) x - 5, (int) y - 5, (int) x + 5, (int) y + 5)
                .anim("dot")
                .angle(0, 360)
                .speed(5, 100)
                .color(color)
                .fadeAfter(0.1f)
                .rotateRandomly()
                .slowlySplatDown()
                .friction(1f)
                .size(0.5f, 2f)
                .angularMomentum(40)
                .create(minCount, maxCount);
    }


    public void spawnSplash(float x, float y, int minCount, int maxCount, Color color) {
        bakery
                .at((int) x - 5, (int) y - 5, (int) x + 5, (int) y + 5)
                .anim("dot")
                .angle(45, 180-45)
                .speed(20, 40)
                .color(color)
                .fadeAfter(0.5f)
                .rotateRandomly()
                .slowlySplatDown()
                .friction(0f)
                .size(0.5f, 2f)
                .angularMomentum(40)
                .create(minCount, maxCount);
    }


    Vector2 v2 = new Vector2();

    public E spawnVanillaParticle(String anim, float x, float y, float angle, float speed, float scale, float emitterVx, float emitterVy, float friction, float angularMomentum, float spriteAngle) {

        v2.set(speed, 0).setAngle(angle);

        return E.E()
                .pos(x - (scale * 0.5f), y - (scale * 0.5f))
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


    public class Builder {
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
        private String anim = "particle";
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
