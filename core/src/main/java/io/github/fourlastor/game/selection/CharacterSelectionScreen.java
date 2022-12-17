package io.github.fourlastor.game.selection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.fourlastor.game.MyGdxGame;
import io.github.fourlastor.game.level.input.controls.Controls;
import io.github.fourlastor.game.route.Router;
import io.github.fourlastor.game.ui.WaveActor;
import io.github.fourlastor.game.util.Text;

import javax.inject.Inject;

public class CharacterSelectionScreen implements Screen {
    private final InputMultiplexer inputMultiplexer;
    private final TextureAtlas atlas;
    private final Router router;

    private final AssetManager assetManager;
    private final Stage stage = new Stage(new FitViewport(320f, 180f));

    private final String[] names = new String[]{
            "nissemor", "rumpnisse", "tontut", "langnisse",
            "joulupukki", "blånisse", "nissefar", "goblin"
    };
    private final boolean[] valid = new boolean[]{
            true, false, false, true,
            false, false, true, false
    };

    private int p1Index = 0;
    private Image p1Name;
    private Image p1Avatar;
    private Image cursorP1;
    private int p2Index = 7;
    private Image p2Name;
    private Image p2Avatar;
    private Image cursorP2;
    private final InputProcessor processor = new InputAdapter() {

        @Override
        public boolean keyDown(int keycode) {
            if (Controls.Setup.P1.left().matches(keycode)) {
                p1Index = Math.max(0, p1Index - 1);
                updatePlayersData();
                return true;
            } else if (Controls.Setup.P1.right().matches(keycode)) {
                p1Index = Math.min(7, p1Index + 1);
                updatePlayersData();
                return true;
            }
            if (Controls.Setup.P2.left().matches(keycode)) {
                p2Index = Math.max(0, p2Index - 1);
                updatePlayersData();
                return true;
            } else if (Controls.Setup.P2.right().matches(keycode)) {
                p2Index = Math.min(7, p2Index + 1);
                updatePlayersData();
                return true;
            }
            if (Input.Keys.SPACE == keycode && valid[p1Index] && valid[p2Index]) {
                router.goToLevel(names[p1Index], names[p2Index]);
            }
            if (Input.Keys.M == keycode) {
                if (music.isPlaying()) {
                    music.stop();
                } else {
                    music.play();
                }
            }
            return false;
        }
    };
    private Music music;

    @Inject
    public CharacterSelectionScreen(
            InputMultiplexer inputMultiplexer,
            TextureAtlas atlas,
            Router router, AssetManager assetManager) {
        this.inputMultiplexer = inputMultiplexer;
        this.atlas = atlas;
        this.router = router;
        this.assetManager = assetManager;
    }

    @Override
    public void show() {
        inputMultiplexer.addProcessor(processor);
        setupBackgrounds();
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        root.padTop(2f);
        setupTopText(root);
        setup1P2PLine(root);
        setupCharacterNames(root);
        setupCharacterBigAvatars(root);
        music = assetManager.get("music/character_selection_bg.mp3", Music.class);
        music.play();

        root.pack();
    }

    private void setupBackgrounds() {
        Image map = new Image(atlas.findRegion("character-selection/map"));
        map.setFillParent(true);
        stage.addActor(map);
        String defShader = assetManager.get("shaders/default.vs", Text.class).getString();
        String wave = assetManager.get("shaders/wave.fs", Text.class).getString();
        ShaderProgram shaderProgram = initShaderProgram(defShader, wave);
        Image fog = new WaveActor(atlas.findRegion("character-selection/fog"), shaderProgram);
        fog.setFillParent(true);
        stage.addActor(fog);
        Image table = new Image(atlas.findRegion("character-selection/character table"));
        table.setFillParent(true);
        stage.addActor(table);
        addGridAvatar("nissemor", 0, 0);
        addGridAvatar("rumpnisse", 1, 0);
        addGridAvatar("tontut", 2, 0);
        addGridAvatar("langnisse", 3, 0);
        addGridAvatar("joulupukki", 0, 1);
        addGridAvatar("blånisse", 1, 1);
        addGridAvatar("nissefar", 2, 1);
        addGridAvatar("goblin", 3, 1);
        cursorP1 = new Image(atlas.findRegion("character-selection/1P selection cursor"));
        positionOnGrid(cursorP1, 0, 0);
        stage.addActor(cursorP1);
        cursorP2 = new Image(atlas.findRegion("character-selection/2P selection cursor"));
        positionOnGrid(cursorP2, 3, 1);
        stage.addActor(cursorP2);
    }

    private ShaderProgram initShaderProgram(String vertexShader, String fragmentShader) {
        Gdx.app.debug("Shader", "Vertex: " + vertexShader);
        Gdx.app.debug("Shader", "Fragment: " + fragmentShader);
        ShaderProgram.pedantic = false;
        ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
        if (!shaderProgram.isCompiled())
            Gdx.app.error("Shader", "Couldn't compile shader: " + shaderProgram.getLog());
        return shaderProgram;
    }

    private void addGridAvatar(String name, int hIndex, int vIndex) {
        Image nissemorAvatar = new Image(atlas.findRegion("character-selection/small-portraits/" + name));
        positionOnGrid(nissemorAvatar, hIndex, vIndex);
        stage.addActor(nissemorAvatar);
    }

    private void updatePlayersData() {
        positionOnGrid(cursorP1, p1Index % 4, p1Index / 4);
        positionOnGrid(cursorP2, p2Index % 4, p2Index / 4);
        p1Avatar.setDrawable(characterPortrait(p1Index));
        p1Name.setDrawable(characterName(p1Index));
        p2Avatar.setDrawable(characterPortrait(p2Index));
        if (p1Index == p2Index) {
            p2Avatar.setColor(MyGdxGame.IMPOSTOR_COLOR);
        } else {
            p2Avatar.setColor(Color.WHITE);
        }
        p2Name.setDrawable(characterName(p2Index));
    }

    private TextureRegionDrawable characterName(int index) {
        return new TextureRegionDrawable(
                atlas.findRegion("character-selection/text/" + names[index])
        );
    }

    private TextureRegionDrawable characterPortrait(int index) {
        return new TextureRegionDrawable(
                atlas.findRegion("character-selection/big-portraits/" + names[index])
        );
    }

    private static void positionOnGrid(Image image, int hIndex, int vIndex) {
        image.setPosition(86 + 37 * hIndex, 32 - 29 * vIndex);
    }

    private void setupTopText(Table root) {
        Image playerSelectText = new Image(atlas.findRegion("character-selection/text/player select"));
        root.add(playerSelectText).expand().top().center().colspan(3);
        root.row().growY();
    }

    private void setup1P2PLine(Table root) {
        Image p1 = new Image(atlas.findRegion("character-selection/text/1P"));
        Image p2 = new Image(atlas.findRegion("character-selection/text/2P"));
        root.add(p1).left().padLeft(36f);
        root.add().expandX();
        root.add(p2).right().padRight(36f);
        root.row();
    }

    private void setupCharacterNames(Table root) {
        p1Name = new Image(characterName(p1Index));
        p2Name = new Image(characterName(p2Index));
        root.add(p1Name);
        root.add().expandX();
        root.add(p2Name);
        root.row();
    }

    private void setupCharacterBigAvatars(Table root) {
        p1Avatar = new Image(characterPortrait(p1Index));
        p2Avatar = new Image(characterPortrait(p2Index));
        p2Avatar.setOrigin(Align.center);
        p2Avatar.setScale(-1f, 1f);
        root.add(p1Avatar).left().bottom();
        root.add();
        root.add(p2Avatar).right().bottom();
        root.row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(processor);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
