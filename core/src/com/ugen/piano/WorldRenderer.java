package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ugen.piano.BadGuys.BadGuy;
import com.ugen.piano.BadGuys.HexagonBadGuy;
import com.ugen.piano.BadGuys.RangedBadGuy;
import com.ugen.piano.BadGuys.SpinningBadGuy;
import com.ugen.piano.Pools.BadGuyPool;
import com.ugen.piano.Pools.HexBadGuyPool;
import com.ugen.piano.Pools.ParticlePool;
import com.ugen.piano.Pools.ParticleSystemPool;
import com.ugen.piano.Pools.RangedBadGuyPool;
import com.ugen.piano.Pools.SpinningBadGuyPool;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by WilsonCS30 on 3/21/2017.
 */

public class WorldRenderer {
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private long initTimeD, initTimeB, initTimeP, initHit, bounceTime;
    private Random rand;

    private com.ugen.piano.Pools.BadGuyPool badGuyPool;
    private Array<BadGuy> badGuys;

    private com.ugen.piano.Pools.RangedBadGuyPool rangedBadGuyPool;
    private Array<RangedBadGuy> rangedBadGuys;

    private com.ugen.piano.Pools.SpinningBadGuyPool sbgPool;
    private Array<SpinningBadGuy> spinningBadGuys;

    private com.ugen.piano.Pools.HexBadGuyPool hexBadGuyPool;
    private Array<HexagonBadGuy> hexBadGuys;

    private com.ugen.piano.Pools.ParticleSystemPool systemPool;
    private Array<com.ugen.piano.Pools.ParticleSystemPool.PooledSystem> systems;

    private com.ugen.piano.Pools.ParticlePool particlePool;
    private Array<com.ugen.piano.Pools.ParticlePool.PooledParticle> pooledParticles;


    private float width, height, x1, y1, x2, y2;
    private OrthographicCamera cam;
    private GameWorld world;
    private Dude dude;
    private Touchpad touchPadR, touchPadL;
    private Stage stage;
    private int score, totalParticles;
    private BitmapFont font;
    private ArrayList<Rectangle> healthBlocks;
    private Rectangle boundingBox;
    private ArrayList<Hexagon> hexagons;
    private Vector2 bouncePos;
    private int bounceCase;

    private ArrayList<Powerup> powerups;

    private boolean spawn = true;

    public WorldRenderer(GameWorld world){
        this.world = world;

        initTimeP = initTimeD = initTimeB = System.currentTimeMillis();

        rand = new Random();

        Sprite particleSprite = new Sprite(new Texture("particle.png"));

        com.ugen.piano.BadGuys.BadGuy bg = new com.ugen.piano.BadGuys.BadGuy(new Vector2(0, 0));
        com.ugen.piano.BadGuys.RangedBadGuy rbg = new com.ugen.piano.BadGuys.RangedBadGuy(new Vector2(0, 0));
        com.ugen.piano.BadGuys.SpinningBadGuy sbg = new SpinningBadGuy(new Vector2(0, 0));
        com.ugen.piano.BadGuys.HexagonBadGuy hbg = new com.ugen.piano.BadGuys.HexagonBadGuy(new Vector2(0, 0));
        Particle p = new Particle(particleSprite, false);

        badGuyPool = new com.ugen.piano.Pools.BadGuyPool(bg, 50, 100);
        badGuys = new Array<BadGuy>();
        rangedBadGuyPool = new com.ugen.piano.Pools.RangedBadGuyPool(rbg, 50, 100);
        rangedBadGuys = new Array<RangedBadGuy>();
        sbgPool = new com.ugen.piano.Pools.SpinningBadGuyPool(sbg, 50, 100);
        spinningBadGuys = new Array<SpinningBadGuy>();
        hexBadGuyPool = new com.ugen.piano.Pools.HexBadGuyPool(hbg, 50, 100);
        hexBadGuys = new Array<HexagonBadGuy>();
        particlePool = new com.ugen.piano.Pools.ParticlePool(p, 200, 1000);
        pooledParticles = new Array<com.ugen.piano.Pools.ParticlePool.PooledParticle>();



        cam = new OrthographicCamera(1.0f, (float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth());
        Viewport viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
        viewport.apply();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        width = cam.viewportWidth;
        height = cam.viewportHeight;
        boundingBox = new Rectangle(0, 0, width, height);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        renderer.setProjectionMatrix(cam.combined);

        dude = new Dude(new Vector2(width/2, height/2));

        ParticleSystem ps = new ParticleSystem(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2),
                boundingBox, 100);

        systemPool = new com.ugen.piano.Pools.ParticleSystemPool(ps, 10, 100);
        systems = new Array<com.ugen.piano.Pools.ParticleSystemPool.PooledSystem>();

        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", AssetManager.getJoystickBackground());
        touchpadSkin.add("touchForeground", AssetManager.getJoystickForeground());

        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();

        Drawable touchpadBack = touchpadSkin.getDrawable("touchBackground");
        Drawable touchpadFront = touchpadSkin.getDrawable("touchForeground");

        touchpadStyle.background = touchpadBack;
        touchpadStyle.knob = touchpadFront;

        touchPadL = new Touchpad(0, touchpadStyle);
        touchPadL.setBounds(100, height / 2 - 100, 200, 200);

        touchPadR = new Touchpad(0, touchpadStyle);
        touchPadR.setBounds(width - 300, height / 2 - 100, 200, 200);

        stage = new Stage(viewport, batch);
        stage.addActor(touchPadL);
        stage.addActor(touchPadR);
        Gdx.input.setInputProcessor(stage);

        initHit = 0;
        score = 0;

        font = new BitmapFont();
        font.getData().setScale(10);

        healthBlocks = new ArrayList<Rectangle>();
        int rectNum = dude.getHealth() / 10;

        for(int i = 0; i < rectNum; i++){
            healthBlocks.add(new Rectangle((4 * i + 1) * width / 123, height - 50, width / 41, width / 41));
        }

        hexagons = new ArrayList<Hexagon>();

        int hexLength = 200;

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(i % 2 == 0)
                    hexagons.add(new Hexagon(((2-(float)Math.cos(Math.PI/3))*i*hexLength),
                            (float)Math.sqrt(3)*j*hexLength, hexLength));
                else
                    hexagons.add(new Hexagon((2-(float)Math.cos(Math.PI/3))*i*hexLength,
                            (float)Math.sqrt(3)*j*hexLength - (float)Math.sqrt(3)*hexLength/2, hexLength));
            }
        }

        powerups = new ArrayList<Powerup>();
    }

    public void preRender(){
        if(spawn) {
            if (System.currentTimeMillis() - initTimeD > 1000) {
                initTimeD = System.currentTimeMillis();

                float tempF = rand.nextFloat();

                /*
                randomly choose a type of badguy to spawn
                TODO: DYNAMIC IF STATEMENTS???
                */
                Vector2 randCellPos = getRandomCellPos();

                if (tempF < .25f) {
                    badGuys.add(badGuyPool.obtain());
                    badGuys.get(badGuys.size - 1).setPosition(randCellPos.x, randCellPos.y);
                } else if (tempF > .25f && tempF < .50f) {
                    rangedBadGuys.add(rangedBadGuyPool.obtain());
                    rangedBadGuys.get(rangedBadGuys.size - 1).setPosition(randCellPos.x, randCellPos.y);

                } else if (tempF > .50f && tempF < .75f) {
                    spinningBadGuys.add(sbgPool.obtain());
                    spinningBadGuys.get(spinningBadGuys.size - 1).setPosition(randCellPos.x, randCellPos.y);
                } else {
                    hexBadGuys.add(hexBadGuyPool.obtain());
                    hexBadGuys.get(hexBadGuys.size - 1).setPosition(randCellPos.x, randCellPos.y);
                }
            }
        }

        if(System.currentTimeMillis() - initTimeP > 2000){
            initTimeP = System.currentTimeMillis();
            powerups.add(new Powerup(getRandomCellPos(), new Sprite(new Texture("powerup.png")),
                    rand.nextInt(4)));
        }

        //if dude is allowed to fire again...
        if(System.currentTimeMillis() - initTimeB > dude.getFireRate() && Math.abs(touchPadR.getKnobPercentX() + touchPadR.getKnobPercentY()) > 0){
            initTimeB = System.currentTimeMillis();

            //give him a "clip" based on his powerup status
            ArrayList<Particle> tempBullets = new ArrayList<Particle>();

            if(dude.getShootType().equals("normal") || dude.getShootType().equals("piercing") || dude.getShootType().equals("barrier")){
                pooledParticles.add(particlePool.obtain());
                tempBullets.add(pooledParticles.get(pooledParticles.size - 1));
            }
            else if(dude.getShootType().equals("nova")){
                for(int i = 0; i < 16; i++) {
                    pooledParticles.add(particlePool.obtain());
                    tempBullets.add(pooledParticles.get(pooledParticles.size - 1));
                }
            }

            dude.shoot(new Vector2(dude.getPosition().x + touchPadR.getKnobPercentX(), dude.getPosition().y + touchPadR.getKnobPercentY()),
                    tempBullets);
        }

        x1 = dude.getPosition().x;
        y1 = dude.getPosition().y;

        dude.setAcceleration(new Vector2(touchPadL.getKnobPercentX(), touchPadL.getKnobPercentY()));
        dude.update();

        //handle out of bounds collisions
        if(dude.getPosition().x < hexagons.get(0).getX() || dude.getPosition().x > hexagons.get(91).getX()
                || dude.getPosition().y < hexagons.get(0).getY() || dude.getPosition().y > hexagons.get(89).getY()){
            bounceTime = System.currentTimeMillis();
            bouncePos = new Vector2(dude.getPosition().x, dude.getPosition().y);
            dude.setAcceleration(new Vector2(0, 0));
            dude.update();

            if(dude.getPosition().x < hexagons.get(0).getX()){
                bounceCase = 0;
                dude.setVelocity(new Vector2(5, dude.getVelocity().y));
            } else if(dude.getPosition().x > hexagons.get(91).getX()){
                bounceCase = 0;
                dude.setVelocity(new Vector2(-5, dude.getVelocity().y));
            } else if(dude.getPosition().y < hexagons.get(0).getY()){
                bounceCase = 1;
                dude.setVelocity(new Vector2(dude.getVelocity().x, 5));
            } else if(dude.getPosition().y > hexagons.get(89).getY()){
                bounceCase = 1;
                dude.setVelocity(new Vector2(dude.getVelocity().x, -5));
            }
        }


    }

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        preRender();

        //////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////RENDERING STUFF///////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        //batch.setColor(new Color(0, 0, 1, 1));

        for(int i = powerups.size()-1; i>=0; i--) {
            Powerup p = powerups.get(i);

            p.draw(batch);
            if (dude.intersects(p.getHitbox())) {
                if (!p.getType().equals("health"))
                    dude.setShootType(p.getType());
                else
                    dude.setHealth(dude.getHealth() + 60);
            }
            if(dude.intersects(p.getHitbox()) || !p.isActive())
                powerups.remove(p);
        }

        //iterate through and update particles
        for(int i = pooledParticles.size - 1; i >= 0; i--){
            com.ugen.piano.Pools.ParticlePool.PooledParticle p = pooledParticles.get(i);

            p.update();
            p.draw(batch);

            if(p.getX() < boundingBox.getX() || p.getX() > boundingBox.getX() + width
                    || p.getY() < boundingBox.getY() || p.getY() > boundingBox.getY() + height){
                p.free();
                pooledParticles.removeIndex(i);
            }
        }

        totalParticles = 0;
        //iterate through and update particle systems
        for(int i = systems.size - 1; i >= 0; i--){
            com.ugen.piano.Pools.ParticleSystemPool.PooledSystem system = systems.get(i);
            system.setBoundary(boundingBox);
            system.draw(batch, delta);

            totalParticles += system.getActiveParticles();

            if(system.isComplete()){
                system.free();
                systems.removeIndex(i);
            }
        }

        checkBadGuyCollisions();

        batch.end();

        renderer.setProjectionMatrix(cam.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        if(System.currentTimeMillis() - bounceTime < 1000){
            renderer.setColor(new Color(0.2f, 0.2f, 0.5f, 1.0f - (float) (System.currentTimeMillis() - bounceTime) / 1000));

            if(bounceCase == 0) {
                renderer.line(bouncePos.x, bouncePos.y + 150.0f - 150.0f * ((float) (System.currentTimeMillis() - bounceTime) / 1000),
                        bouncePos.x, bouncePos.y - 150.0f + 150.0f * ((float) (System.currentTimeMillis() - bounceTime) / 1000));
            }
            else if(bounceCase == 1){
                renderer.line(bouncePos.x + 150.0f - 150.0f * ((float) (System.currentTimeMillis() - bounceTime) / 1000), bouncePos.y,
                        bouncePos.x - 150.0f + 150.0f * ((float) (System.currentTimeMillis() - bounceTime) / 1000), bouncePos.y);
            }
        }

        dude.draw(renderer, batch, false);

        x2 = dude.getPosition().x;
        y2 = dude.getPosition().y;

        //scroll static parts of the screen to be relative to dude
        scroll(x2-x1, y2-y1);

        drawBackground();
        //checkBulletCollisions();

        for(int i = pooledParticles.size - 1; i >= 0; i--){
            ParticlePool.PooledParticle p = pooledParticles.get(i);

            if(p.intersects(dude.getHitbox()) && p.getFaction().equals("bad")){
                p.free();
                pooledParticles.removeIndex(i);
                dude.setHealth(dude.getHealth() - 5);
            }
        }

        //TODO: MAKE THIS WORK
        /*font.setColor(Color.WHITE);
        font.draw(batch, "SCORE: " + score, 0,0);
        //font.getData().setScale(5.0f);*/


        renderer.end();
        ////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////END OF RENDERING STUFF///////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////


        while(dude.isDead()){
            //TODO: make something happen when dude dies
        }

        log();
    }

    public void log(){
        Gdx.app.log("DEBUG", "FPS: " + Gdx.graphics.getFramesPerSecond() +  " , FREE: " + systemPool.getFree()
                + " , IN USE: " + systems.size + " , MAX: " + systemPool.getMax() + " , TOTAL PARTICLES: " + totalParticles);
        Gdx.app.log("DEBUG", "BADGUYS: " + badGuys.size + " , FREE BADGUYS: " + badGuyPool.getFree());
        Gdx.app.log("DEBUG", "RANGEDBADGUYS: " + rangedBadGuys.size + " , FREE RANGEDBADGUYS: " + rangedBadGuyPool.getFree());

        Gdx.app.log("DEBUG", "SPINNINGBADGUYS: " + spinningBadGuys.size + " , FREE SPINNINGBADGUYS: " + sbgPool.getFree());

        Gdx.app.log("DEBUG", "HEXBADGUYS: " + hexBadGuys.size + " , FREE HEXBADGUYS: " + hexBadGuyPool.getFree());

    }

    public Vector2 getRandomCellPos(){
        return new Vector2(hexagons.get(rand.nextInt(hexagons.size())).getX(),
                hexagons.get(rand.nextInt(hexagons.size())).getY());
    }

    private void drawBackground(){
        for(Hexagon hex : hexagons){
            hex.draw(renderer, new Color(0.3f, 0.2f, 0.7f, 0.7f));
        }
    }

    private void scroll(float dx, float dy){
        drawHealthBar(dx, dy);
        cam.translate(dx, dy);
        touchPadL.moveBy(dx, dy);
        touchPadR.moveBy(dx, dy);
        boundingBox.setPosition(boundingBox.getX() + dx, boundingBox.getY() + dy);
    }

    private void drawHealthBar(float x, float y){
        for(Rectangle r : healthBlocks){
            r.setPosition(r.getX() + x, r.getY() + y);
        }
        for(int i = 0; i < dude.getHealth()/10; i++){
            Rectangle r = healthBlocks.get(i);
            renderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }
    }

    public boolean killBadGuy(BadGuy b){
        boolean dead = false;
        if(dude.getShootType().equals("barrier")){
            for(int j = dude.getBarrier().getTriangles().size() - 1; j >= 0; j--){
                Triangle t = dude.getBarrier().getTriangles().get(j);

                if(t.intersects(b.getHitbox())){
                    dude.getBarrier().deactivate(j);
                    dead = true;
                }
            }
        }
        else {
            for (int j = pooledParticles.size - 1; j >= 0; j--) {
                ParticlePool.PooledParticle p = pooledParticles.get(j);

                if (p.intersects(b.getHitbox()) && p.getFaction().equals(("good"))) {
                    if (!dude.getShootType().equals("piercing")) {
                        p.free();
                        pooledParticles.removeIndex(j);
                    }

                    dead = true;
                }
            }
        }

        if(dead){
            ParticleSystemPool.PooledSystem temp = systemPool.obtain();
            temp.setBoundary(boundingBox);
            temp.setPosition(new Vector2(b.getX(), b.getY()));
            systems.add(temp);

            score += 420;
            return true;
        }

        return false;
    }

    private void checkBadGuyCollisions(){
        for(int i = badGuys.size - 1; i >= 0; i--){
            BadGuy b = badGuys.get(i);

            if(killBadGuy(b)){
               badGuyPool.free(b);
               badGuys.removeIndex(i);
               continue;
            }

            updateBadGuy(b);
        }

        for(int i = rangedBadGuys.size - 1; i >= 0; i--){
            RangedBadGuy b = rangedBadGuys.get(i);

            if(killBadGuy(b)){
                rangedBadGuyPool.free(b);
                rangedBadGuys.removeIndex(i);
                continue;
            }

            if(System.currentTimeMillis() - b.getLastShot() > 1000/b.getFireRate()){
                pooledParticles.add(particlePool.obtain());
                b.shoot(dude.getPosition(), pooledParticles.get(pooledParticles.size - 1));
            }

            updateBadGuy(b);
        }

        for(int i = spinningBadGuys.size - 1; i >= 0; i--){
            SpinningBadGuy b = spinningBadGuys.get(i);

            if(killBadGuy(b)){
                sbgPool.free(b);
                spinningBadGuys.removeIndex(i);
                continue;
            }

            b.update(new Vector2(dude.getPosition().x, dude.getPosition().y));
            b.draw(batch);

            if(System.currentTimeMillis() - initHit > dude.getDamageTimer()) {
                if (dude.intersects(b.getHitbox())) {
                    Gdx.app.log("DEBUG", "OW");
                    dude.setHealth(dude.getHealth() - 20);
                    initHit = System.currentTimeMillis();
                    ParticleSystemPool.PooledSystem temp = systemPool.obtain();
                    temp.setBoundary(boundingBox);
                    temp.setPosition(new Vector2(b.getX(), b.getY()));
                    systems.add(temp);

                    sbgPool.free(b);
                    spinningBadGuys.removeIndex(i);
                }
            }
        }

        for(int i = hexBadGuys.size - 1; i >= 0; i--){
            HexagonBadGuy b = hexBadGuys.get(i);

            if(killBadGuy(b)){
                hexBadGuyPool.free(b);
                hexBadGuys.removeIndex(i);
                Array<SpinningBadGuy> temp = b.explode(sbgPool);

                for(SpinningBadGuy s : temp){
                    spinningBadGuys.add(s);
                }

                continue;
            }

            updateBadGuy(b);
        }
    }

    public void updateBadGuy(BadGuy b){
        b.update(new Vector2(dude.getPosition().x - b.getHitbox().radius, dude.getPosition().y - b.getHitbox().radius));
        b.draw(batch);

        if(System.currentTimeMillis() - initHit > dude.getDamageTimer()) {
            if (dude.intersects(b.getHitbox())) {
                Gdx.app.log("DEBUG", "OW");
                dude.setHealth(dude.getHealth() - 5);
                initHit = System.currentTimeMillis();
            }
        }
    }

    public OrthographicCamera getCam(){
        return cam;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public Dude getDude(){
        return dude;
    }
}
