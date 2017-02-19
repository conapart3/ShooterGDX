package com.libgdx.shooter.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.libgdx.shooter.entities.SpaceObject;
import com.libgdx.shooter.entities.enemies.ParachuteBomber;
import com.libgdx.shooter.game.ShooterGame;

/**
 * Created by Conal on 16/10/2016.
 */

public class Level {

    public static final String TAG = Level.class.getName();
    private float pixmapToWorldTranslationX;

    public enum BLOCK_TYPE {
        EMPTY(0, 0, 0), //black
        PLAYER_SPAWNPOINT(255, 255, 255), //white
        ENEMY_BOMBER(0, 255, 255), //green
        ENEMY_SHOOTER(250, 0, 250),
        ENEMY_BOSS(250, 0, 0);

        private int colour;

        private BLOCK_TYPE(int r, int g, int b){
            colour = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColour(int colour){
            return this.colour == colour;
        }

        public int getColour(){
            return colour;
        }
    }

    // Bombers
    public Array<ParachuteBomber> bombers;

    // Decoration
//    public Background background;

    public Level (String filename){
        init(filename);
    }

    private void init(String filename){
        bombers = new Array<ParachuteBomber>();

        // Load image file that represents the level data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));

        // Scan pixels from top-left to bottom right
        int lastpixel = -1;
        for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++){
            for(int pixelX = 0; pixelX < pixmap.getHeight(); pixelX++) {
                SpaceObject obj = null;
                float offsetHeight = 0;

                // Height grows from the bottom to the top
//                float baseHeight = pixmap.getHeight() - pixelY;

                // Get colour of current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);

                // Find matching colour value to identify block type at (x,y) point and create corresponding game object

                // Empty space
                if(BLOCK_TYPE.EMPTY.sameColour(currentPixel)){
                    // empty space - do nothing
                }

                // Bomber
                else if(BLOCK_TYPE.ENEMY_BOMBER.sameColour(currentPixel)){
//                    if(lastpixel != currentPixel){
                    obj = new ParachuteBomber();
//                    float heightIncreaseFactor = 0.25f;
//                    offsetHeight = -2.5f;
                    float worldX = pixelX * (ShooterGame.WORLD_WIDTH/pixmap.getWidth());
                    float worldY = pixelY * (ShooterGame.WORLD_HEIGHT/pixmap.getHeight());

                    obj.setPosition(worldX, worldY);
                    bombers.add((ParachuteBomber)obj);
//                    }
                }

                // Player
                else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColour(currentPixel)){

                }

                // Shooter
                else if(BLOCK_TYPE.ENEMY_SHOOTER.sameColour(currentPixel)){

                }

                // Boss
                else if(BLOCK_TYPE.ENEMY_BOSS.sameColour(currentPixel)){

                }

                // Unknown
                else {
                    int r = 0xff & (currentPixel >>> 24);
                    int g = 0xff & (currentPixel >>> 16);
                    int b = 0xff & (currentPixel >>> 8);
                    int a = 0xff & currentPixel;

                    Gdx.app.error(TAG, "Unknown object at x<" +pixelX +"> y<" +pixelY +">: r<" +r +"> g<" +g +"> b<" +b +"> a<" +a +">");
                }
            }
        }

        // todo: read in background files and have a background for each Level object?
        //decoration
//        background = new Background();

        // Free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "level '" +filename +"' loaded");

    }

    public void render(SpriteBatch spriteBatch) {
        for(ParachuteBomber bomber : bombers){
            bomber.render(spriteBatch);
        }
    }
}
