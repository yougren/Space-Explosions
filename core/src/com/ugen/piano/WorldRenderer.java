package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private long initTimeD, initTimeB, initHit;
    private Random rand;
    private ParticleSystem ps;
    private ArrayList<BadGuy> badGuys;
    private ParticleSystemPool systemPool;
    private Array<ParticleSystemPool.PooledSystem> systems;
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


    public WorldRenderer(GameWorld world){
        this.world = world;

        initTimeD = initTimeB = System.currentTimeMillis();

        rand = new Random();

        badGuys = new ArrayList<BadGuy>();

        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        cam = new OrthographicCamera(1.0f, (float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth());
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
        viewport.apply();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        width = cam.viewportWidth;
        height = cam.viewportHeight;

        dude = new Dude(new Vector2(width/2, height/2), new Vector2(1.0f, 1.0f));

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
    }

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setColor(new Color(0, 0, 1, 1));
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setProjectionMatrix(cam.combined);


        if(System.currentTimeMillis() - initTimeD > 1000){
            initTimeD = System.currentTimeMillis();
            badGuys.add(new BadGuy(new Vector2(rand.nextFloat() * width, rand.nextFloat() * height)));
            Gdx.app.log("DEBUG", "FPS: " + Gdx.graphics.getFramesPerSecond() +  "FREE: " + systemPool.getFree() + " , IN USE: " + systems.size + " , MAX: " + systemPool.getMax() + " , TOTAL PARTICLES: " + totalParicles);

        }
        if(System.currentTimeMillis() - initTimeB > 250 && Math.abs(touchPadR.getKnobPercentX() + touchPadR.getKnobPercentY()) > 0){
            initTimeB = System.currentTimeMillis();
            dude.shoot(new Vector2(dude.getPosition().x + touchPadR.getKnobPercentX(), dude.getPosition().y + touchPadR.getKnobPercentY()));
        }
        Gdx.app.log("DEBUG", "FPS: " + Gdx.graphics.getFramesPerSecond() +  "FREE: " + systemPool.getFree() + " , IN USE: " + systems.size + " , MAX: " + systemPool.getMax() + " , TOTAL PARTICLES: " + totalParicles);

        totalParicles = 0;

        renderer.setColor(0.0f, 0.0f, 1.0f, 1.0f);

        dude.setVelocity(new Vector2(touchPadL.getKnobPercentX() * 10, touchPadL.getKnobPercentY() * 10));
        dude.draw(renderer, batch);
        drawHealthBar();

        for(BadGuy b : badGuys){
            b.update(dude.getPosition());
            b.draw(renderer);
            if(System.currentTimeMillis() - initHit > dude.getDamageTimer())
                if(dude.intersects(b.getHitbox())){
                    Gdx.app.log("DEBUG", "OW");
                    dude.setHealth(dude.getHealth() - 5);
                    initHit = System.currentTimeMillis();
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
        font.draw(batch, "SCORE: " + score, width/2, height/2);
        font.getData().setScale(5.0f);

        renderer.end();
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        checkBulletCollisions();
    }

    public void drawHealthBar(){
        int rectNum = dude.getHealth() / 10;

        for(int i = 0; i < rectNum; i++){
            renderer.rect((4 * i + 1) * width / 123, height - 50, width / 41, width / 41);
        }
    }

    public void checkBulletCollisions(){
        for(int ii = dude.getBullets().size() - 1; ii >= 0; ii--){
            Particle p = dude.getBullets().get(ii);
            for(int i = badGuys.size() - 1; i > -1; i--){
                if (p.intersects(badGuys.get(i).getHitbox())) {
                    ParticleSystemPool.PooledSystem temp = systemPool.obtain();
                    temp.setPosition(new Vector2(p.getX(), p.getY()));
                    systems.add(temp);
                    dude.getBullets().remove(p);
                    badGuys.remove(i);
                    score += 420;

                    break;
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
