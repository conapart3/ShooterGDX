package com.libgdx.shooter.entities;

/**
 * Created by Conal on 19/02/2017.
 */

public enum SoundToPlay {
    HIT_SOUND_BULLET("data/Sound/hitSoundBullet.wav"),
    HIT_SOUND_LASER("data/Sound/hitSoundLaser.wav"),
    HIT_SOUND_MISSILE("data/Sound/hitSoundMissile.wav"),

    EXPLOSION_SOUND_BOSS("data/Sound/explosionBoss.wav"),
    EXPLOSION_SOUND_PARACHUTEBOMBER("data/Sound/explosionParachute.wav"),
    EXPLOSION_SOUND_SHOOTERENEMY("data/Sound/explosionShooterEnemy.wav"),

    PICKUP_SOUND_ITEM("data/Sound/pickupShotgun.wav"),
    PICKUP_SOUND_HEALTH("data/Sound/pickupHealth.wav"),
    PICKUP_SOUND_MEDAL("data/Sound/pickupMedal.wav"),
    PICKUP_SOUND_SHIELD("data/Sound/pickupShield.wav"),

    SHOOT_SOUND_LIGHTLASERCANNON("data/Sound/shootSoundLightLaserCannon.wav"),
    SHOOT_SOUND_HEAVYLASERCANNON("data/Sound/shootSoundHeavyLaserCannon.wav"),
    SHOOT_SOUND_MINIGUN("data/Sound/shootSoundMinigun.wav"),
    SHOOT_SOUND_MISSILE("data/Sound/shootSoundMissile.wav"),
    SHOOT_SOUND_SHOTGUN("data/Sound/shootSoundShotgun.wav");

    private final String wavFilePath;

    SoundToPlay(String wavFilePath)
    {
        this.wavFilePath = wavFilePath;
    }

    public String getWavFilePath(){
        return wavFilePath;
    }
}
