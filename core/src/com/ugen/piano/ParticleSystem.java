package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by eugen_000 on 3/29/2017.
 */

public class ParticleSystem {
    private Sprite sprite;
    private Vector2 position;
    private Rectangle boundary;
    private int maxParticles, activeParticles, duration;
    private ArrayList<Particle> particles;
    private boolean[] active;
    private Color color;
    private Random rand;
    private float randR, randG, randB;

    public ParticleSystem(){
        /*rand = new Random();

        randR = rand.nextFloat();
        randG = rand.nextFloat();
        randB = rand.nextFloat();

        sprite = new Sprite(new Texture("particle.png"), 4, 1);
        sprite.setColor(new Color(randR, randG, randB, rand.nextFloat()));

        this.maxParticles = 200;
        this.particles = new ArrayList<Particle>();
        this.active = new boolean[maxParticles];
        this.activeParticles = 0;
        this.duration = 1000;

        for(int i = 0; i < maxParticles; i++) {
            this.particles.add(new Particle(sprite, true));
            active[i] = true;
            particles.get(i).scale(4.0f);
            this.particles.get(i).setPosition(position.x, position.y);
            activeParticles++;
        }*/
    }

    public ParticleSystem(ParticleSystem ps){
        initialize(ps.getPosition(), ps.getBoundary(), ps.getMaxParticles());
    }

    public ParticleSystem(Vector2 position, Rectangle boundary, int maxParticles){
        initialize(position, boundary, maxParticles);
    }

    public void initialize(Vector2 position, Rectangle boundary, int maxParticles){
        rand = new Random();

        randR = rand.nextFloat();
        randG = rand.nextFloat();
        randB = rand.nextFloat();

        sprite = new Sprite(new Texture("hexagon1600.png"));
        sprite.setColor(new Color(randR, randG, randB, rand.nextFloat()));

        this.position = position;
        this.boundary = boundary;
        this.maxParticles = maxParticles;
        this.particles = new ArrayList<Particle>();
        this.active = new boolean[maxParticles];
        this.activeParticles = 0;
        this.duration = 1000;

        for(int i = 0; i < maxParticles; i++) {
            this.particles.add(new Particle(sprite, true));
            active[i] = true;
            particles.get(i).scale(2.0f);
            this.particles.get(i).setPosition(position.x, position.y);
            activeParticles++;
        }
    }

    public void draw(SpriteBatch batch,  float delta){
        duration -= delta * 1000;

        for(int i = 0; i < active.length; i++){
            if(active[i]){
                if(updateParticle(particles.get(i), delta))
                    particles.get(i).draw(batch);
                else{
                    active[i] = false;
                    activeParticles--;
                }
            }
        }
    }

    public boolean updateParticle(Particle particle, float delta){
        particle.update(delta);

        if(particle.isDead() || particle.getColor().a <= 0 || particle.getX() < boundary.getX() ||
                particle.getX() > boundary.getX() + boundary.getWidth() || particle.getY() < boundary.getY() ||
                particle.getY() > boundary.getY() + boundary.getHeight()) {
            return false;
        }

        return true;
    }

    public boolean isComplete(){
        return activeParticles == 0 || duration <= 0;
    }

    public void reset(){
        //color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        randR = rand.nextFloat();
        randG = rand.nextFloat();
        randB = rand.nextFloat();

        color = new Color(randR, randG, randB, rand.nextFloat());

        for(int i = 0; i < maxParticles; i++){
            this.particles.get(i).reset();
            this.particles.get(i).setColor(color);
            active[i] = true;
        }

        this.duration = 1000;
        this.activeParticles = maxParticles;
    }

    public void setPosition(Vector2 position){
        this.position = position;

        for(int i = 0; i < particles.size(); i++){
            //particles.get(i).initialize();
            particles.get(i).setPosition(position.x, position.y);
        }
    }
    public Vector2 getPosition() {
        return position;
    }

    public void setBoundary(Rectangle bound){
        this.boundary = bound;
    }

    public Rectangle getBoundary() {
        return boundary;
    }

    public int getMaxParticles() {
        return maxParticles;
    }

    public ArrayList<Particle> getParticles(){
        return particles;
    }

    public int getActiveParticles(){
        return activeParticles;
    }
}
