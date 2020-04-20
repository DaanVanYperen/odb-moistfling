package net.mostlyoriginal.game.system.logic;

import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Dead;
import net.mostlyoriginal.game.component.Leak;
import net.mostlyoriginal.game.component.Oxygen;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.MyParticleEffectStrategy;
import net.mostlyoriginal.game.system.box2d.BoxContactListener;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;
import net.mostlyoriginal.game.system.common.FluidSystem;

import static net.mostlyoriginal.game.GameRules.currentMap;
import static net.mostlyoriginal.game.component.Leak.MAXLEAKS;

/**
 * @author Daan van Yperen
 */
@All({Oxygen.class, Pos.class, Bounds.class, Leak.class})
public class LeakSystem extends FluidSystem implements BoxContactListener {

    private BoxPhysicsSystem boxPhysicsSystem;

    private float leakY[]= new float[MAXLEAKS];
    private float leakX[]= new float[MAXLEAKS];
    private float angles[] = new float[MAXLEAKS];
    private TransitionSystem transitionSystem;
    private float deathCooldown;

    {
        for (int i = 0; i < MAXLEAKS; i++) {
            angles[i]= MathUtils.random(0,360);
            leakX[i]= MathUtils.random(8,36);
            leakY[i]= MathUtils.random(8,36);
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        boxPhysicsSystem.register(this);
    }

    Vector2 tmp = new Vector2();

    boolean almostDeadSoundPlayed=false;

    @Override
    protected void process(E e) {
        Leak leak = e.getLeak();
        e.oxygenPercentage(e.oxygenPercentage() - (world.delta * leak.leaks));

        leak.age -= world.delta;
        leak.lastLeakAge += world.delta;
        if ( leak.age <= 0 && !e.isDead() && !e.isInvisible()) {
            leak.age+= 1/30f;
            leak.age+= Math.max(0,(100f-e.oxygenPercentage())*0.005f); // slow leak if oxygen is low.
            for (int i = 0; i < leak.leaks; i++) {

                tmp.set(leakX[i],leakY[i]).sub(e.boundsCx(),e.boundsCy()).rotate(e.angleRotation()).add(e.boundsCx(), e.boundsCy());

                E.E().particleEffect(
                        e.oxygenPercentage() < 50 ?
                                MyParticleEffectStrategy.EFFECT_SLOW_LEAK :
                        MyParticleEffectStrategy.EFFECT_LEAK).pos(
                        e.posX() + tmp.x,
                        e.posY() + tmp.y).angleRotation(angles[i] + e.angleRotation());
            }
        }

        if ( e.oxygenPercentage() <= 0 && !almostDeadSoundPlayed ) {
            almostDeadSoundPlayed=true;
            deathCooldown=2.5f;
            E.E().playSound("suit-last-oxygen-escapes");
        }
        if ( almostDeadSoundPlayed && !e.isDead() && !e.isInvisible()) {
            deathCooldown -= world.delta;
            if (deathCooldown <= 0 ) {
                forceDeath();
            }
        }
    }

    public void forceDeath() {
        E.withTag("player").dead();
        GameRules.nextMap = GameRules.currentMap;
        transitionSystem.transition(GameScreen.class, 4);
    }

    @Override
    public void beginContact(E a, E b) {
        if ( a.hasSharp() && b.hasPlayer() && !b.isDead() && !b.isInvisible() ) {
            if ( b.leakLastLeakAge() > 0.2f &&  b.leakLastLeakAge() < 1.0f ) { // don't spam leaks.
                // be a little forgiving.
                E.E().playSound("suit-almost-puncture");
            }
            else if ( b.leakLastLeakAge() > 0.5f ) { // don't spam leaks.
                if ( MathUtils.random(0,100) > a.sharpChance()) {
                    // sometimes you are lucky.
                    E.E().playSound("suit-almost-puncture");
                } else {
                    E.E().playSound("suit-puncture");
                    b.leakLeaks(Math.min(MAXLEAKS, b.leakLeaks() + a.sharpSharpness()));
                }
                b.leakLastLeakAge(0f);
            }
        }
    }

    @Override
    public void endContact(E a, E b) {

    }
}
