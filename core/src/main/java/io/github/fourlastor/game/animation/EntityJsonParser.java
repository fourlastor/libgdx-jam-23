package io.github.fourlastor.game.animation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.game.animation.json.AnimatedSlot;
import io.github.fourlastor.game.animation.json.Animation;
import io.github.fourlastor.game.animation.json.Bone;
import io.github.fourlastor.game.animation.json.EntityData;
import io.github.fourlastor.game.animation.json.KeyFrame;
import io.github.fourlastor.game.animation.json.Skin;
import io.github.fourlastor.game.animation.json.Skins;
import io.github.fourlastor.game.animation.json.Slot;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class EntityJsonParser {

    private final JsonReader json;

    @Inject
    public EntityJsonParser(JsonReader json) {
        this.json = json;
    }

    public EntityData parse(FileHandle handle) {
        JsonValue value = json.parse(handle);
        return new EntityData(
                parseBones(value.get("bones")),
                parseSlots(value.get("slots")),
                parseAllSkins(value.get("skins")),
                parseAnimations(value.get("animations"))
        );
    }

    private Map<String, Animation> parseAnimations(JsonValue value) {
        HashMap<String, Animation> animations = new HashMap<>(value.size);
        for (JsonValue it : value) {
            animations.put(it.name, parseAnimation(it));
        }
        return animations;
    }

    private Animation parseAnimation(JsonValue value) {
        return new Animation(value.name, parseAnimationSlots(value.get("slots")));
    }

    private Map<String, AnimatedSlot> parseAnimationSlots(JsonValue value) {
        HashMap<String, AnimatedSlot> slots = new HashMap<>(value.size);
        for (JsonValue it : value) {
            slots.put(it.name, parseAnimationSlot(it.get("attachment")));
        }
        return slots;
    }

    private AnimatedSlot parseAnimationSlot(JsonValue value) {
        ArrayList<KeyFrame<String>> keyFrames = new ArrayList<>(value.size);
        for (JsonValue it : value) {
            keyFrames.add(new KeyFrame<>(
                    (int) (it.getFloat("time") * 1000),
                    it.getString("name")
            ));
        }
        return new AnimatedSlot(keyFrames);
    }

    private List<Bone> parseBones(JsonValue value) {
        ArrayList<Bone> bones = new ArrayList<>(value.size);
        for (JsonValue it : value) {
            bones.add(new Bone(
                    require(getString(it, "name")),
                    getString(it, "parent"),
                    getFloat(it, "x"),
                    getFloat(it, "y"),
                    getInt(it, "rotation"),
                    getFloat(it, "scaleX"),
                    getFloat(it, "scaleY")
            ));
        }
        return bones;
    }

    private List<Slot> parseSlots(JsonValue value) {
        ArrayList<Slot> slots = new ArrayList<>(value.size);
        for (JsonValue it : value) {
            slots.add(new Slot(
                    require(getString(it, "name")),
                    getString(it, "bone"),
                    getString(it, "attachment")
            ));
        }
        return slots;
    }

    private Map<String, Skins> parseAllSkins(JsonValue value) {
        Map<String, Skins> skins = new HashMap<>(value.size);
        for (JsonValue it : value.get("default")) {
            skins.put(it.name, parseSkins(it));
        }
        return skins;
    }

    private Skins parseSkins(JsonValue value) {
        Map<String, Skin> skins = new HashMap<>(value.size);
        for (JsonValue it : value) {
            skins.put(it.name, parseSkin(it));
        }

        return new Skins(
                value.name,
                skins
        );
    }

    private Skin parseSkin(JsonValue value) {
        if ("boundingbox".equals(getString(value, "type"))) {
            return new Skin.BoundingBox(
                    require(getString(value, "name")),
                    getInt(value, "vertexCount"),
                    value.get("vertices").asFloatArray()
            );
        } else {
            return new Skin.Image(
                    require(getString(value, "name"))
            );
        }
    }

    private String require(String value) {
        if (value == null) {
            throw new NullPointerException("Required value was null");
        }
        return value;
    }

    private String getString(JsonValue value, String name) {
        return value.has(name) ? value.getString(name) : null;
    }

    private int getInt(JsonValue value, String name) {
        return value.has(name) ? value.getInt(name) : 0;
    }

    private float getFloat(JsonValue value, String name) {
        return value.has(name) ? value.getFloat(name) : 0f;
    }
}
