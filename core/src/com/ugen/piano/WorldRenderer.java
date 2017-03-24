package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by WilsonCS30 on 3/21/2017.
 */

public class WorldRenderer {
    SpriteBatch batch;
    ShapeRenderer renderer;

    long initTime, timeElapsed;

    Random rand;

    private ArrayList<Particle> particles;
    private ArrayList<BadGuy> badGuys;

    private Particle p;
    private float width, height, fps;
    private OrthographicCamera cam;
    private Viewport viewport;
    private GameWorld world;
    private Dude dude;
    private BadGuy badGuy;
    private Color defaultColor;
    private FPSLogger logger;

    public WorldRenderer(GameWorld world){
        this.world = world;

        initTime = System.currentTimeMillis();

        rand = new Random();

        badGuys = new ArrayList<BadGuy>();
        particles = new ArrayList<Particle>();

        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        cam = new OrthographicCamera(1.0f, (float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth());
        viewport = new ExtendViewport(108, 192, cam);
        viewport.apply();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        width = cam.viewportWidth;
        height = cam.viewportHeight;

        dude = new Dude(new Vector2(width/2, height/2), new Vector2(1.0f, 1.0f));
      //  badGuy = new BadGuy(new Vector2(width/2 - 5, height-10));

        defaultColor = new Color(Color.BLUE);

        logger = new FPSLogger();
    }

    public void render(float delta){
        logger.log();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setProjectionMatrix(cam.combined);

        if(System.currentTimeMillis() - initTime > 1000){
            badGuys.add(new BadGuy(new Vector2(rand.nextInt((int)width), rand.nextInt((int)height))));
            initTime = System.currentTimeMillis();
        }
        renderer.setColor(0.0f, 0.0f, 1.0f, 1.0f);

        //badGuy.draw(renderer);
        dude.draw(renderer);


        dude.update(renderer);

        for(BadGuy b : badGuys){
            b.update(dude.getPosition());
            b.draw(renderer);
        }

        for(int i = particles.size() - 1; i > -1; i--){
            if(particles.get(i).isDead() || particles.get(i).getPosition().y < 0 || particles.get(i).getPosition().y > height || particles.get(i).getPosition().x < 0 || particles.get(i).getPosition().x > width)
                particles.remove(i);
            else
                particles.get(i).run(renderer, defaultColor);
        }

        //Gdx.app.log("DEBUG", "PARTICLES: " + particles.size());

        renderer.end();

        checkBulletCollisions();
    }

    public void checkBulletCollisions(){

        for(Particle p : dude.getBullets()){
            for(int i = badGuys.size() - 1; i > -1; i--){
                if (p.intersects(badGuys.get(i).getHitbox())) {
                    explosion(p.getPosition(), particles, 1000);
                    badGuys.remove(i);
                }
            }
        }
    }

    public void explosion(Vector2 origin, ArrayList<Particle> particles, int density){
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setProjectionMatrix(cam.combined);

        for(int i = 0; i < density; i++){
            particles.add(new Particle(new Vector2(origin.x, origin.y)));
        }

        for(Particle p : particles){
            p.draw(renderer, defaultColor);
        }

        renderer.end();
    }

    public OrthographicCamera getCam(){
        return cam;
    }

    public ArrayList<Particle> getParticles(){
        return particles;
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
