package net.mostlyoriginal.game.system.box2d;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.box2d.Boxed;
import net.mostlyoriginal.game.system.common.FluidSystem;

import static net.mostlyoriginal.game.system.MyEntityAssemblyStrategy.*;

/**
 * @author Daan van Yperen
 */
public class BoxPhysicsSystem extends FluidSystem {

    public static final int FLOOR_LEVEL_Y = 80;
    private static final float GRAVITY_Y = 0;// -98f;
    public static float PPM = 100f / (180f / 48f);
    public Body groundBody;
    boolean welded = false;
    private Body weldTargetB = null;
    private Body weldTargetA = null;

    //private MouseThrowSystem mouseThrowSystem;

    public BoxPhysicsSystem() {
        super(Aspect.all(Pos.class, Boxed.class));
        box2d = new World(new Vector2(0, GRAVITY_Y), true);
        box2d.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {

                considerWeld(contact.getFixtureA(), contact.getFixtureB());
                considerWeld(contact.getFixtureB(), contact.getFixtureA());


              /*  bulletHit(contact.getFixtureA(), contact.getFixtureB());
                bulletHit(contact.getFixtureB(), contact.getFixtureA());
                touchingFloor(contact.getFixtureB(), contact.getFixtureA(),true);
                touchingFloor(contact.getFixtureA(), contact.getFixtureB(),true);
                grabHeli(contact.getFixtureA(), contact.getFixtureB());
                grabHeli(contact.getFixtureB(), contact.getFixtureA());*/
            }

            private void considerWeld(Fixture a, Fixture b) {
                if (a.getFilterData().categoryBits == CAT_GRAPPLE) {
                    weldTargetA = a.getBody();
                    weldTargetB = b.getBody();
                }
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
        //addGroundBody();
    }

    public World box2d;

    @Override
    protected void initialize() {
        super.initialize();
    }

    public void spawnChain(Body source, E e) {

        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.x = (e.getPos().xy.x - e.boundsCx()) / PPM;
        bodyDef.position.y = (e.getPos().xy.y - e.boundsCy()) / PPM;
        bodyDef.angularVelocity = 0f;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2 / PPM, 4 / PPM);

        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.3f;
        fixtureDef.filter.categoryBits = CAT_CHAIN;
        fixtureDef.filter.maskBits = (short) CAT_DEBRIS;

        CircleShape circle = new CircleShape();
        circle.setRadius(8 / PPM);

        final FixtureDef grappleDef = new FixtureDef();
        grappleDef.shape = circle;
        grappleDef.density = 1f;
        grappleDef.friction = 0.1f;
        grappleDef.restitution = 0.3f;
        grappleDef.filter.categoryBits = CAT_GRAPPLE;
        grappleDef.filter.maskBits = (short) CAT_DEBRIS;
        grappleDef.isSensor = true;

        int chainLength = 6;
        Body link = null;
        int links = 50;
        for (int i = 0; i < links; i++) {
            bodyDef.position.y = bodyDef.position.y + (chainLength / PPM);
            Body newLink = box2d.createBody(bodyDef);
            final boolean isLastlink = i == links - 1;
            E.E().bounds(0, 0, 16, 16)
                    .anim(isLastlink ? "grappling_hook" : "grappling_joint")
                    .pos()
                    .boxedBody(newLink)
                    .renderLayer(GameRules.LAYER_ITEM + (GameRules.SCREEN_HEIGHT - (int) e.posY()));

            newLink.createFixture(isLastlink ? grappleDef : fixtureDef);
            if (link != null) {
//                DistanceJointDef distanceJointDef = new DistanceJointDef();
//                distanceJointDef.localAnchorA.set(0, chainLength / PPM);
//                distanceJointDef.localAnchorB.set(0, -chainLength / PPM);
//                distanceJointDef.bodyA = link;
//                distanceJointDef.length = (chainLength * 0.1f) / PPM;
//                distanceJointDef.bodyB = newLink;
//                distanceJointDef.frequencyHz = 0;
//                distanceJointDef.dampingRatio = 1f;
//
//                box2d.createJoint(distanceJointDef);

                RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                revoluteJointDef.localAnchorA.set(0, chainLength / PPM);
                revoluteJointDef.localAnchorB.set(0, -chainLength / PPM);
                revoluteJointDef.bodyA = link;
                revoluteJointDef.bodyB = newLink;
                revoluteJointDef.maxMotorTorque = 0.1f;
                revoluteJointDef.collideConnected = false;
                revoluteJointDef.enableMotor = true;
                box2d.createJoint(revoluteJointDef);

                if (isLastlink) {
//                    DistanceJointDef distanceJointDef = new DistanceJointDef();
//                    distanceJointDef.localAnchorA.set(0, chainLength / PPM);
//                    distanceJointDef.localAnchorB.set(0, -chainLength / PPM);
//                    distanceJointDef.bodyA = source;
//                    distanceJointDef.bodyB = newLink;
//                    distanceJointDef.length = 100 / PPM;
//                    distanceJointDef.frequencyHz = 30;
//                    distanceJointDef.dampingRatio = 1f;
//                    box2d.createJoint(distanceJointDef);
                }
            } else {
//                DistanceJointDef distanceJointDef = new DistanceJointDef();
//                distanceJointDef.localAnchorA.set(0, chainLength / PPM);
//                distanceJointDef.localAnchorB.set(0, -chainLength / PPM);
//                distanceJointDef.bodyA = source;
//                distanceJointDef.bodyB = newLink;
//                distanceJointDef.length = (chainLength * 0.1f) / PPM;
//                distanceJointDef.frequencyHz = 0;
//                distanceJointDef.dampingRatio = 1f;
//                                box2d.createJoint(distanceJointDef);
                RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                revoluteJointDef.localAnchorA.set(0, chainLength / PPM);
                revoluteJointDef.localAnchorB.set(0, -chainLength / PPM);
                revoluteJointDef.bodyA = source;
                revoluteJointDef.bodyB = newLink;
                revoluteJointDef.collideConnected = false;
                revoluteJointDef.maxMotorTorque = 0.1f;
                revoluteJointDef.enableMotor = true;
                box2d.createJoint(revoluteJointDef);
            }

            link = newLink;
        }
        shape.dispose();
    }

    public Body addAsBox(E e, float cx, float cy, float density, short categoryBits, short maskBits, float radianAngle) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.x = (e.getPos().xy.x - e.boundsCx()) / PPM;
        bodyDef.angle = radianAngle;
        bodyDef.position.y = (e.getPos().xy.y - e.boundsCy()) / PPM;
        bodyDef.angularVelocity = 0.4f;

        Body body = box2d.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(cx / PPM, cy / PPM);

        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = maskBits;
        fixtureDef.friction = 0;

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
        bodyDef.position.y = 0 / PPM;

        groundBody = box2d.createBody(bodyDef);

        EdgeShape shape = new EdgeShape();
        shape.set(-9999, FLOOR_LEVEL_Y / PPM, 9999 / PPM, FLOOR_LEVEL_Y / PPM);
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
            while (body.getJointList().size > 0) {
                box2d.destroyJoint(body.getJointList().first().joint);
            }
            box2d.destroyBody(body);
        }
    }

    @Override
    protected void process(E e) {
        Body body = e.boxedBody();
        e.pos(body.getPosition().x * PPM - e.boundsCx(), body.getPosition().y * PPM - e.boundsCy());
        e.angleRotation((float) Math.toDegrees(body.getAngle()));
        if (!welded && weldTargetA != null) {
            welded = true;
            WeldJointDef weld = new WeldJointDef();
            weld.bodyA = weldTargetA;
            weld.bodyB = weldTargetB;
            weld.collideConnected = false;
            weld.type = JointDef.JointType.WeldJoint;
            box2d.createJoint(weld);
        }
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
