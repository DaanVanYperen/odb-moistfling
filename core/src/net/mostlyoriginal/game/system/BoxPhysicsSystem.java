package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Boxed;
import net.mostlyoriginal.game.system.common.FluidSystem;

/**
 * @author Daan van Yperen
 */
public class BoxPhysicsSystem extends FluidSystem {

    public static final int FLOOR_LEVEL_Y = 80;
    public static float SCALING = 8f;
    public Body groundBody;

    //private MouseThrowSystem mouseThrowSystem;

    public BoxPhysicsSystem() {
        super(Aspect.all(Pos.class, Boxed.class));
        box2d = new World(new Vector2(0, -98f), true);
        box2d.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
              /*  bulletHit(contact.getFixtureA(), contact.getFixtureB());
                bulletHit(contact.getFixtureB(), contact.getFixtureA());
                touchingFloor(contact.getFixtureB(), contact.getFixtureA(),true);
                touchingFloor(contact.getFixtureA(), contact.getFixtureB(),true);
                grabHeli(contact.getFixtureA(), contact.getFixtureB());
                grabHeli(contact.getFixtureB(), contact.getFixtureA());*/
            }

//            private void grabHeli(Fixture fixtureA, Fixture fixtureB) {
//                if (fixtureA.getFilterData().categoryBits == StagepieceSystem.CAT_HELI) {
//                    short cat = fixtureB.getFilterData().categoryBits;
//                    if ( cat == StagepieceSystem.CAT_AGENT ) {
//                        E heli = (E) fixtureA.getBody().getUserData();
//                        E agent = (E) fixtureB.getBody().getUserData();
//                        agent.grabTargetId(heli.id());
//                    }
//                }
//            }
//
//            private void touchingFloor(Fixture fixtureA, Fixture fixtureB, boolean state) {
//                if (fixtureA.getFilterData().categoryBits == StagepieceSystem.CAT_BOUNDARY) {
//                    short cat = fixtureB.getFilterData().categoryBits;
//                    if (  cat == StagepieceSystem.CAT_AGENT ) {
//                        ((E) fixtureB.getBody().getUserData()).touchingFloor(state);
//                    }
//                }
//            }
//
//            private void bulletHit(Fixture fixtureA, Fixture fixtureB) {
//                if (fixtureA.getFilterData().categoryBits == StagepieceSystem.CAT_BULLET) {
//                    short cat = fixtureB.getFilterData().categoryBits;
//                    if ( cat == StagepieceSystem.CAT_CAR || cat == StagepieceSystem.CAT_AGENT  || cat == StagepieceSystem.CAT_PRESIDENT) {
//                        ((E) fixtureB.getBody().getUserData()).struck();
//                        ((E) fixtureA.getBody().getUserData()).struck();
//                    }
//                }
//            }

            @Override
            public void endContact(Contact contact) {
//                touchingFloor(contact.getFixtureB(), contact.getFixtureA(),false);
//                touchingFloor(contact.getFixtureA(), contact.getFixtureB(),false);
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
        addGroundBody();
    }

    public World box2d;

    @Override
    protected void initialize() {
        super.initialize();
    }

    public Body addAsBox(E e, float cx, float cy, float density, short categoryBits, short maskBits, float radianAngle) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.x = (e.getPos().xy.x - e.boundsCx()) / SCALING;
        bodyDef.angle = radianAngle;
        bodyDef.position.y = (e.getPos().xy.y - e.boundsCy()) / SCALING;

        Body body = box2d.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(cx / SCALING, cy / SCALING);

        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = maskBits;

        body.createFixture(fixtureDef);

        e.boxedBody(body);
        body.setUserData(e);

        shape.dispose();

        return body;
    }

    protected void addGroundBody() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.x = 0;
        bodyDef.position.y = 0 / SCALING;

        groundBody = box2d.createBody(bodyDef);

        EdgeShape shape = new EdgeShape();
        shape.set(-9999, FLOOR_LEVEL_Y / SCALING, 9999 / SCALING, FLOOR_LEVEL_Y / SCALING);
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(20 / scaling, 1000 / scaling);

        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        groundBody.createFixture(fixtureDef);
        shape.dispose();
    }

    float cooldown = 0;

    public static final float timeStep = (1.0f / 45.0f);
    boolean updating = false;

    @Override
    protected void begin() {
        super.begin();
        //if (Gdx.input.isKeyJustPressed(Input.Keys.F7)) slowmotion = !slowmotion;

        cooldown = cooldown -= world.delta * 1f;
        if (cooldown <= 0) {
            cooldown += timeStep;
            box2d.step(timeStep, 6, 2);
            updating = true;
        } else
            updating = false;
    }

    @Override
    protected void end() {
        super.end();
        if (cooldown2 <= 0) {
            cooldown2 += 3f;
        }
        cooldown2 -= world.delta;
    }

    float cooldown2 = 0;

    @Override
    public void removed(Entity e) {
        Body body = E.E(e).boxedBody();
        if (body != null) {
            for (JointEdge jointEdge : body.getJointList()) {
                // bit hacky but it should suffice.
                //mouseThrowSystem.forgetJoint(jointEdge);
            }
            while ( body.getJointList().size > 0 ) {
                box2d.destroyJoint(body.getJointList().first().joint);
            }
            box2d.destroyBody(body);
        }
    }

    Vector2 v3 = new Vector2();
    Vector2 v4 = new Vector2();

    @Override
    protected void process(E e) {
/*
        if ( e.hasGuard()) return;
        Body body = e.boxedBody();
        e.pos(body.getPosition().x * SCALING - e.boundsCx(), body.getPosition().y * SCALING - e.boundsCy());
        e.angleRotation((float) Math.toDegrees(body.getAngle()));

        if ( e.posY() < -50 ) {
            e.deleteFromWorld();
        }*/
    }

    private boolean within(float val, float deviation) {
        return val >= -deviation && val <= deviation;
    }

    @Override
    protected void dispose() {
        super.dispose();
        if (box2d != null) {
            box2d.dispose();
            box2d = null;
        }
    }
}
