package io.github.fourlastor.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

public class WaveActor extends Image {
    private final ShaderProgram shaderProgram;
    private final Vector2 imageSize = new Vector2();


    private static final float AMPLITUDE = 0;
    private static final float WAVE_LENGTH = 0;
    private static final float VELOCITY = 0f;

    private float time = 0f;


    public WaveActor(TextureRegion region, ShaderProgram shaderProgram) {
        super(region);
        this.shaderProgram = shaderProgram;
        this.setOrigin(Align.center);
        this.setScale(1.01f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setShader(shaderProgram);
        shaderProgram.setUniformf("u_time", time);
        imageSize.set(getWidth(), getHeight());
        shaderProgram.setUniformf("u_imageSize", imageSize);
        shaderProgram.setUniformf("u_amplitude", new Vector2(0.1f, 0.5f));
        shaderProgram.setUniformf("u_wavelength", new Vector2(17f, 40f));
        shaderProgram.setUniformf("u_velocity", new Vector2(5f, 6f));
        super.draw(batch, parentAlpha);
        batch.setShader(null);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
    }
}
