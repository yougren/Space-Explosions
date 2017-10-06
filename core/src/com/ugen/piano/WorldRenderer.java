package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by WilsonCS30 on 3/21/2017.
 */

public class WorldRenderer {
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private PolygonSpriteBatch polyBatch;
    private long initTimeD, initTimeB, initHit;
    private Random rand;

    private BadGuy bg;
    private BadGuyPool badGuyPool;
    private Array<BadGuyPool.PooledBadGuy> badGuys;

    private RangedBadGuy rbg;
    private RangedBadGuyPool rangedBadGuyPool;
    private Array<RangedBadGuyPool.PooledRangedBadGuy> rangedBadGuys;

    private ParticleSystem ps;
    private ParticleSystemPool systemPool;
    private Array<ParticleSystemPool.PooledSystem> systems;

    private Particle p;
    private Sprite particleSprite;
    private ParticlePool particlePool;
    private Array<ParticlePool.PooledParticle> pooledParticles;

    private float width, height;
    private OrthographicCamera cam;
    private Viewport viewport;
    private GameWorld world;
    private Dude dude;
    private Touchpad touchPadR, touchPadL;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchpadBack, touchpadFront;
    private Stage stage;
    private int score, totalParicles;
    private BitmapFont font;
    private ArrayList<ParticleSystemPool.PooledSystem> deathExplosions;


    public WorldRenderer(GameWorld world){
        this.world = world;

        initTimeD = initTimeB = System.currentTimeMillis();

        rand = new Random();

        particleSprite = new Sprite(new Texture("particle.png"));

        bg = new BadGuy(new Vector2(0, 0));
        rbg = new RangedBadGuy(new Vector2(0, 0));
        p = new Particle(particleSprite, false);

        badGuyPool = new BadGuyPool(bg, 10, 100);
        badGuys = new Array<BadGuyPool.PooledBadGuy>();
        rangedBadGuyPool = new RangedBadGuyPool(rbg, 10, 100);
        rangedBadGuys = new Array<RangedBadGuyPool.PooledRangedBadGuy>();
        particlePool = new ParticlePool(p, 100, 1000);
        pooledParticles = new Array<ParticlePool.PooledParticle>();

        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        polyBatch = new PolygonSpriteBatch();

        cam = new OrthographicCamera(1.0f, (float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth());
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
        viewport.apply();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        width = cam.viewportWidth;
        height = cam.viewportHeight;

        dude = new Dude(new Vector2(width/2, height/2), new Vector2(1.0f, 1.0f), width, height);

        ps = new ParticleSystem(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2),
                new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 200);

        systemPool = new ParticleSystemPool(ps, 10, 100);
        systems = new Array<ParticleSystemPool.PooledSystem>();

        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", AssetManager.getJoystickBackground());
        touchpadSkin.add("touchForeground", AssetManager.getJoystickForeground());

        touchpadStyle = new Touchpad.TouchpadStyle();

        touchpadBack = touchpadSkin.getDrawable("touchBackground");
        touchpadFront = touchpadSkin.getDrawable("touchForeground");

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

        deathExplosions = new ArrayList<ParticleSystemPool.PooledSystem>();
    }

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        polyBatch.begin();
        polyBatch.setColor(0, 0, 1, 1);
        polyBatch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.setColor(new Color(0, 0, 1, 1));
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setProjectionMatrix(cam.combined);


        if(System.currentTimeMillis() - initTimeD > 1000){
            initTimeD = System.currentTimeMillis();

            if(rand.nextInt() % 2 == 0) {
                badGuys.add(badGuyPool.obtain());
                badGuys.get(badGuys.size - 1).setPosition(new Vector2(rand.nextFloat() * width, rand.nextFloat() * height));
            }

            else {
                rangedBadGuys.add(rangedBadGuyPool.obtain());
                rangedBadGuys.get(rangedBadGuys.size - 1).setPosition(new Vector2(rand.nextFloat() * width, rand.nextFloat() * height));

            }
        }

        if(System.currentTimeMillis() - initTimeB > 250 && Math.abs(touchPadR.getKnobPercentX() + touchPadR.getKnobPercentY()) > 0){
            initTimeB = System.currentTimeMillis();
            pooledParticles.add(particlePool.obtain());
            dude.shoot(new Vector2(dude.getPosition().x + touchPadR.getKnobPercentX(), dude.getPosition().y + touchPadR.getKnobPercentY()),
                   pooledParticles.get(pooledParticles.size - 1));
        }

        Gdx.app.log("DEBUG", "FPS: " + Gdx.graphics.getFramesPerSecond() +  "FREE: " + systemPool.getFree() + " , IN USE: " + systems.size + " , MAX: " + systemPool.getMax() + " , TOTAL PARTICLES: " + totalParicles);

        totalParicles = 0;

        renderer.setColor(0.0f, 0.0f, 1.0f, 1.0f);

        dude.setVelocity(new Vector2(touchPadL.getKnobPercentX() * 10, touchPadL.getKnobPercentY() * 10));
        dude.draw(renderer, batch);
        drawHealthBar();

        for(BadGuy b : badGuys){
            b.update(new Vector2(dude.getPosition().x - b.getHitbox().width/2, dude.getPosition().y - b.getHitbox().height/2));
            b.draw(renderer);
            if(System.currentTimeMillis() - initHit > dude.getDamageTimer())
                if(dude.intersects(b.getHitbox())){
                    Gdx.app.log("DEBUG", "OW");
                    dude.setHealth(dude.getHealth() - 5);
                    initHit = System.currentTimeMillis();
                }

        }

        for(RangedBadGuy b : rangedBadGuys){
            b.update(new Vector2(dude.getPosition().x - b.getHitbox().width/2,
                    dude.getPosition().y - b.getHitbox().height/2), batch);
            b.draw(renderer);

            if(System.currentTimeMillis() - b.getLastShot() > 1000/b.getFireRate()){
                pooledParticles.add(particlePool.obtain());
                b.shoot(dude.getPosition(), pooledParticles.get(pooledParticles.size - 1));
            }

            if(System.currentTimeMillis() - initHit > dude.getDamageTimer()) {
                if (dude.intersects(b.getHitbox())) {
                    Gdx.app.log("DEBUG", "OW");
                    dude.setHealth(dude.getHealth() - 5);
                    initHit = System.currentTimeMillis();
                }
            }
        }

        for(int i = pooledParticles.size - 1; i >= 0; i--){
            ParticlePool.PooledParticle p = pooledParticles.get(i);

            p.update();
            p.draw(batch);

            if(p.getX() < 0 || p.getX() > width || p.getY() < 0 || p.getY() > height){
                p.free();
                pooledParticles.removeIndex(i);
            }
        }

        for(int i = systems.size - 1; i >= 0; i--){
            ParticleSystemPool.PooledSystem system = systems.get(i);
            system.draw(batch, delta);

            totalParicles += system.getActiveParticles();

            if(system.isComplete()){
                system.free();
                systems.removeIndex(i);
            }
        }

        font.setColor(Color.WHITE);
        font.draw(batch, "SCORE: " + score, 0,0);
        //font.getData().setScale(5.0f);

        renderer.end();
        batch.end();
        polyBatch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        checkBulletCollisions();

        if(dude.isDead()){
            for(int i = 0; i < 3; i++) {
                ParticleSystemPool.PooledSystem temp = systemPool.obtain();
                temp.setPosition(new Vector2(dude.getPosition().x, dude.getPosition().y));
                systems.add(temp);
            }
        }
    }

    public void drawHealthBar(){
        int rectNum = dude.getHealth() / 10;

        for(int i = 0; i < rectNum; i++){
            renderer.rect((4 * i + 1) * width / 123, height - 50, width / 41, width / 41);
        }
    }

    public void checkBulletCollisions(){
        for(int ii = pooledParticles.size - 1; ii >= 0; ii--){
            ParticlePool.PooledParticle p = pooledParticles.get(ii);

            if(p.getFaction().equals("bad")){
                if(dude.intersects(p.getBoundingRectangle()))   {
                    Gdx.app.log("DEBUG", "OW");
                    dude.setHealth(dude.getHealth() - 5);
                    p.free();
                    pooledParticles.removeIndex(ii);
                }
            }

            else if(p.getFaction().equals("good")){
                for (int i = badGuys.size - 1; i >= 0; i--) {
                    BadGuyPool.PooledBadGuy tempbg = badGuys.get(i);

                    if (p.intersects(badGuys.get(i).getHitbox())) {
                        tempbg.free();
                        badGuys.removeIndex(i);
                        ParticleSystemPool.PooledSystem temp = systemPool.obtain();
                        temp.setPosition(new Vector2(p.getX(), p.getY()));
                        systems.add(temp);

                        p.free();
                        pooledParticles.removeIndex(ii);

                        score += 420;

                        break;
                    }
                }

                for (int i = rangedBadGuys.size - 1; i >= 0; i--) {
                    RangedBadGuyPool.PooledRangedBadGuy tempbg = rangedBadGuys.get(i);

                    if (p.intersects(rangedBadGuys.get(i).getHitbox())) {
                        tempbg.free();
                        rangedBadGuys.removeIndex(i);
                        ParticleSystemPool.PooledSystem temp = systemPool.obtain();
                        temp.setPosition(new Vector2(p.getX(), p.getY()));
                        systems.add(temp);


                        p.free();
                        pooledParticles.removeIndex(ii);
                        score += 420;

                        break;
                    }
                }
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
