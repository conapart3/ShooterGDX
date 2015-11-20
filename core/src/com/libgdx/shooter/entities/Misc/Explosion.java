package com.libgdx.shooter.entities.Misc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Conal on 17/11/2015.
 */
public class Explosion extends Animation {


    public Explosion(float frameDuration, Array<? extends TextureRegion> keyFrames) {
        super(frameDuration, keyFrames);
    }

    public Explosion(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode) {
        super(frameDuration, keyFrames, playMode);
    }

    public Explosion(float frameDuration, TextureRegion... keyFrames) {
        super(frameDuration, keyFrames);
    }
}
