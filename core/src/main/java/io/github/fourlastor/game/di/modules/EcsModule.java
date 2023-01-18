package io.github.fourlastor.game.di.modules;

import com.badlogic.ashley.core.ComponentMapper;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.game.component.ActorComponent;
import io.github.fourlastor.game.component.AnimationImageComponent;
import io.github.fourlastor.game.component.BodyBuilderComponent;
import io.github.fourlastor.game.component.BodyComponent;
import io.github.fourlastor.game.component.ChunkRemovalComponent;
import io.github.fourlastor.game.component.HpComponent;
import io.github.fourlastor.game.component.InputComponent;
import io.github.fourlastor.game.component.MovingComponent;
import io.github.fourlastor.game.component.PlayerComponent;
import io.github.fourlastor.game.component.ShadowComponent;
import io.github.fourlastor.game.component.SoundComponent;

import javax.inject.Singleton;

@Module
public class EcsModule {

    @Provides
    @Singleton
    public ComponentMapper<AnimationImageComponent> animatedImageComponent() {
        return ComponentMapper.getFor(AnimationImageComponent.class);
    }

    @Provides
    @Singleton
    public ComponentMapper<ActorComponent> imageComponent() {
        return ComponentMapper.getFor(ActorComponent.class);
    }

    @Provides
    @Singleton
    public ComponentMapper<BodyComponent> bodyComponent() {
        return ComponentMapper.getFor(BodyComponent.class);
    }

    @Provides
    @Singleton
    public ComponentMapper<ShadowComponent> shadowComponent() {
        return ComponentMapper.getFor(ShadowComponent.class);
    }

    @Provides
    @Singleton
    public ComponentMapper<BodyBuilderComponent> bodyBuilderComponent() {
        return ComponentMapper.getFor(BodyBuilderComponent.class);
    }

    @Provides
    @Singleton
    public ComponentMapper<PlayerComponent> playerComponent() {
        return ComponentMapper.getFor(PlayerComponent.class);
    }

    @Provides
    @Singleton
    public ComponentMapper<ChunkRemovalComponent> chunkRemovalComponent() {
        return ComponentMapper.getFor(ChunkRemovalComponent.class);
    }

    @Provides
    @Singleton
    public ComponentMapper<MovingComponent> movingComponent() {
        return ComponentMapper.getFor(MovingComponent.class);
    }

    @Provides
    @Singleton
    public ComponentMapper<SoundComponent> soundComponent() {
        return ComponentMapper.getFor(SoundComponent.class);
    }

    @Provides
    @Singleton
    public ComponentMapper<HpComponent> hpComponent() {
        return ComponentMapper.getFor(HpComponent.class);
    }

    @Provides
    @Singleton
    public ComponentMapper<InputComponent> inputComponent() {
        return ComponentMapper.getFor(InputComponent.class);
    }
}
