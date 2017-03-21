package com.ugen.piano;

import com.badlogic.gdx.Gdx;
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

    long initTime;

    Random rand;

    private ArrayList<Particle> particles;

    private Particle p;
    private float width, height;
    private OrthographicCamera cam;
    private Viewport viewport;
    private GameWorld world;

    public WorldRenderer(GameWorld world){
        this.world = world;

        particles = new ArrayList<Particle>();

        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        cam = new OrthographicCamera(1.0f, (float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth());
        viewport = new ExtendViewport(108, 192, cam);
        viewport.apply();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        width = cam.viewportWidth;
        height = cam.viewportHeight;
    }

    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setProjectionMatrix(cam.combined);

        if((System.currentTimeMillis() - initTime) > 3000) {
            initTime = System.currentTimeMillis();
            particles.clear();
            explosion(new Vector2(width / 2, height / 2), particles, 500);

        }

        for(int i = particles.size() - 1; i > -1; i--){
            if(particles.get(i).isDead())
                particles.remove(i);
            else
                particles.get(i).run(renderer);
        }


        renderer.end();
    }

    public void explosion(Vector2 origin, ArrayList<Particle> particles, int density){
        for(int i = 0; i < density; i++){
            particles.add(new Particle(new Vector2(origin.x, origin.y)));
        }

        for(Particle p : particles){
            p.draw(renderer);
        }
    }
}
