package com.libgdx.shooter.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

/**
 * Created by Conal on 19/02/2017.
 */

public class SoundManager {

    public AssetManager assetManager;
    public HashMap<String, Sound> soundMap;

    public SoundManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        soundMap = new HashMap<>();
        loadSounds();
    }

    public void loadSounds()
    {
        /** Load in the shoot sounds into AssetManager **/
        assetManager.load("data/Sound/shootSoundMinigun.wav", Sound.class);
        assetManager.load("data/Sound/shootSoundMissile.wav", Sound.class);
        assetManager.load("data/Sound/shootSoundShotgun.wav", Sound.class);
        assetManager.load("data/Sound/shootSoundHeavyLaserCannon.wav", Sound.class);
        assetManager.load("data/Sound/shootSoundLightLaserCannon.wav", Sound.class);

        /** Load in the pickup sounds into AssetManager **/
        assetManager.load("data/Sound/pickupHeavyLaser.wav", Sound.class);
        assetManager.load("data/Sound/pickupLightLaser.wav", Sound.class);
        assetManager.load("data/Sound/pickupMinigun.wav", Sound.class);
        assetManager.load("data/Sound/pickupShotgun.wav", Sound.class);
        assetManager.load("data/Sound/pickupMissileLauncher.wav", Sound.class);
        assetManager.load("data/Sound/pickupHealth.wav", Sound.class);
        assetManager.load("data/Sound/pickupMedal.wav", Sound.class);
        assetManager.load("data/Sound/pickupShield.wav", Sound.class);

        /** load in the hit sounds for bullet types **/
        assetManager.load("data/Sound/hitSoundLaser.wav", Sound.class);
        assetManager.load("data/Sound/hitSoundBullet.wav", Sound.class);
        assetManager.load("data/Sound/hitSoundMissile.wav", Sound.class);

        /** Load in the explosion sounds into AssetManager **/
        assetManager.load("data/Sound/explosionParachute.wav", Sound.class);
        assetManager.load("data/Sound/explosionShooterEnemy.wav", Sound.class);
        assetManager.load("data/Sound/explosionPlayer.wav", Sound.class);
        assetManager.load("data/Sound/explosionBoss.wav", Sound.class);

        assetManager.finishLoading();
    }

    /**
     * If the soundMap contains the sound, play it, otherwise add it then play it.
     * @param soundToPlayKey the soundMap key of the sound to play
     */
    public void play(String soundToPlayKey) {
        System.out.println(soundToPlayKey);
        if(!soundMap.containsKey(soundToPlayKey)) {
            soundMap.put(soundToPlayKey, (Sound) assetManager.get(soundToPlayKey));
        }
        soundMap.get(soundToPlayKey).play();
    }
}
