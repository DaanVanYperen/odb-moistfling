package net.mostlyoriginal.game.system.logic;

import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.GameRules;
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

    @Override
    protected void process(E e) {
        Leak leak = e.getLeak();
        e.oxygenPercentage(e.oxygenPercentage() - (world.delta * leak.leaks));

        leak.age -= world.delta;
        leak.lastLeakAge += world.delta;
        if ( leak.age <= 0 && !e.isDead()) {
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

        if ( e.oxygenPercentage() <= 0 && !e.isDead() ) {
            e.dead();
            GameRules.nextMap = GameRules.currentMap;
            transitionSystem.transition(GameScreen.class,10);
        }
    }

    @Override
    public void beginContact(E a, E b) {
        if ( a.hasSharp() && b.hasPlayer() ) {
            if ( b.leakLastLeakAge() > 0.5f ) { // don't spam leaks.
                b.leakLastLeakAge(0f);
                b.leakLeaks(Math.min(MAXLEAKS, b.leakLeaks() + 1));
            }
        }
    }

    @Override
    public void endContact(E a, E b) {

    }
}
