package io.github.fourlastor.game.level.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import io.github.fourlastor.game.component.BodyBuilderComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.level.HurtData;
import io.github.fourlastor.game.level.Message;

import javax.inject.Inject;
import java.util.List;

public class PhysicsSystem extends IntervalSystem {

    private static final Family FAMILY_BUILDER =
            Family.all(BodyBuilderComponent.class).get();
    private static final Family FAMILY_BODY = Family.all(BodyComponent.class).get();
    private static final float STEP = 1f / 60f;

    private final World world;
    private final ComponentMapper<BodyBuilderComponent> bodyBuilders;
    private final ComponentMapper<BodyComponent> bodies;
    private final MessageDispatcher messageDispatcher;
    private final Factory factory;
    private final Cleaner cleaner;

    @Inject
    public PhysicsSystem(
            World world,
            ComponentMapper<BodyBuilderComponent> bodyBuilders,
            ComponentMapper<BodyComponent> bodies,
            MessageDispatcher messageDispatcher) {
        super(STEP);
        this.world = world;
        this.bodyBuilders = bodyBuilders;
        this.bodies = bodies;
        this.messageDispatcher = messageDispatcher;
        factory = new Factory();
        cleaner = new Cleaner();
    }

    @Override
    protected void updateInterval() {
        world.step(STEP, 6, 2);
    }

    @Override
    public void addedToEngine(Engine engine) {
        world.setContactListener(contactListener);
        engine.addEntityListener(FAMILY_BUILDER, factory);
        engine.addEntityListener(FAMILY_BODY, cleaner);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(factory);
        engine.removeEntityListener(cleaner);
        world.setContactListener(null);
    }

    /**
     * Creates a body in the world every time a body builder is added.
     */
    public class Factory implements EntityListener {

        @Override
        public void entityAdded(Entity entity) {
            BodyBuilderComponent component = bodyBuilders.get(entity);
            List<Component> components = component.factory.build(world);
            for (Component it : components) {
                entity.add(it);
            }
            entity.remove(BodyBuilderComponent.class);
        }

        @Override
        public void entityRemoved(Entity entity) {
            // nothing
        }
    }

    /**
     * Cleans up a body which has been removed from the engine.
     */
    public class Cleaner implements EntityListener {

        @Override
        public void entityAdded(Entity entity) {
            // nothing
        }

        @Override
        public void entityRemoved(Entity entity) {
            BodyComponent component = bodies.get(entity);
            if (component.body != null) {
                world.destroyBody(component.body);
                component.body = null;
                component.hitboxes.clear();
            }
        }
    }

    private final ContactListener contactListener = new ContactListener() {
        @Override
        public void beginContact(Contact contact) {
            Fixture fixtureA = contact.getFixtureA();
            Fixture fixtureB = contact.getFixtureB();
            if (isHitbox(fixtureA) && isHurtbox(fixtureB)) {
                propagateHit(fixtureA, fixtureB);
            } else if (isHurtbox(fixtureA) && isHitbox(fixtureB)) {
                propagateHit(fixtureB, fixtureA);
            }
        }

        private boolean isHurtbox(Fixture fixture) {
            return fixture.getFilterData().categoryBits == Bits.Category.HURTBOX.bits;
        }

        private boolean isHitbox(Fixture fixture) {
            return fixture.getFilterData().categoryBits == Bits.Category.HITBOX.bits;
        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            Fixture fixtureA = contact.getFixtureA();
            Fixture fixtureB = contact.getFixtureB();
            if (fixtureA.getUserData() == fixtureB.getUserData()) {
                contact.setEnabled(false);
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }
    };

    private void propagateHit(@SuppressWarnings("unused") Fixture hitbox, Fixture hurtbox) {
        Object hitUserData = hitbox.getUserData();
        Object hurtUserData = hurtbox.getUserData();
        if (!(hurtUserData instanceof Entity) || !(hitUserData instanceof Entity)) {
            return;
        }
        Entity hurt = (Entity) hurtUserData;
        Entity hit = (Entity) hitUserData;

        messageDispatcher.dispatchMessage(Message.PLAYER_HIT.ordinal(), new HurtData(
                hurt,
                hit.getComponent(PlayerComponent.class).fighter.damage
        ));
    }
}
