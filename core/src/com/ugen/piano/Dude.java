package com.ugen.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by WilsonCS30 on 3/21/2017.
 */

public class Dude {

    private Random random;
    private Sprite bullet;
    private Vector2 position, velocity, acceleration;
    private ArrayList<Particle> bullets;
    boolean shot = false;
    private Color color;
    private int health, damageTimer;
    private Circle hitbox;
    private String shootType = "normal";
    private long fireRate, powerupLength, powerupInit;
    private Barrier barrier;
    private ArrayList<Powerup> powerups;

    public Dude(Vector2 position){
        random = new Random();
        damageTimer = 250;
        health = 300;
        bullet = new Sprite(new Texture("particle.png"));
        bullet.setColor(new Color(1, 1, 1, 1));
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.acceleration = new Vector2(0, 0);
        bullets = new ArrayList<Particle>();
        color = new Color(0.1f, 0.3f, 0.8f, 1.0f);
        hitbox = new Circle(position, 50.0f);
        fireRate = 300;
        powerupLength = 5000;
        powerups = new ArrayList<Powerup>();
    }

    public void update(){
        if(UgenUtils.getMagnitude(velocity) < 10)
            velocity.add(acceleration);
        else
            velocity.sub(new Vector2(velocity.x/10, velocity.y/10));


        position.add(velocity);
        hitbox.setPosition(position);
    }

    public void draw(ShapeRenderer renderer, SpriteBatch batch, boolean update){
        if(update)
            update();

        if(!isDead()) {

            if (shootType.equals("barrier")) {
                barrier.setPosition(position);
                barrier.draw(renderer);
            }
            renderer.setColor(color);
            renderer.circle(position.x, position.y, 50.0f);
        }
    }

    public void shoot(Vector2 target, ArrayList<Particle> bullets){
        double mag = Math.sqrt((target.x - position.x) * (target.x - position.x)
                + (target.y - position.y) * (target.y - position.y));

        float velocityX = 15 * (float) ((target.x - position.x) / mag);
        float velocityY = 15 * (float) ((target.y - position.y) / mag);

        float theta = (float) Math.atan(velocityY / velocityX);

        for(Particle p : bullets){
            p.setPosition(position.x, position.y);
            p.setFaction("good");
            p.setColor(Color.WHITE);
        }

        if(System.currentTimeMillis() - powerupInit > powerupLength){
            shootType = "normal";
        }

        if(shootType.equals("normal") || shootType.equals("barrier")) {
            fireRate = 300;
            for (Particle bullet : bullets) {
                bullet.rotate(theta * 180 / (float) Math.PI);
                bullet.setVelocity(new Vector2(velocityX, velocityY));
                bullet.setAcceleration(new Vector2(0.0f, 0.0f));
            }
        }

        else if(shootType.equals("nova")){
            fireRate = 1000;
            float randAngle = (float)(random.nextFloat() * 2 * Math.PI);

            for(int i = 0; i < bullets.size(); i++){
                theta = (float)(i*2*Math.PI/bullets.size()) + randAngle;

                bullets.get(i).setColor(Color.YELLOW);
                bullets.get(i).rotate((float)(180*theta/Math.PI));
                bullets.get(i).setVelocity(new Vector2((float)(15*Math.cos(theta)), (float)(15*Math.sin(theta))));
                bullets.get(i).setAcceleration(new Vector2(0.0f, 0.0f));
            }
        }

        else if(shootType.equals("piercing")){
            fireRate = 300;
            for (Particle bullet : bullets) {
                bullet.setColor(Color.BLUE);
                bullet.rotate(theta * 180 / (float) Math.PI);
                bullet.setVelocity(new Vector2(velocityX, velocityY));
                bullet.setAcceleration(new Vector2(0.0f, 0.0f));
            }
        }


        shot = true;
    }

    public void applyPowerup(Powerup p){
        powerups.add(p);
    }

    public boolean isDead(){
        return health <= 0;
    }

    public void setVelocity(Vector2 velocity){
        this.velocity = velocity;
    }

    public Vector2 getVelocity(){return velocity;}

    public void setAcceleration(Vector2 acceleration){this.acceleration = acceleration;}

    public void applyForce(Vector2 force){this.acceleration.add(force);}

    public void setHealth(int health){
        this.health = health;
    }

    public int getHealth(){return health;}

    public boolean hasShot(){
        return shot;
    }

    public Vector2 getPosition(){
        return this.position;
    }

    public ArrayList<Particle> getBullets(){
        return bullets;
    }

    public int getDamageTimer(){
        return damageTimer;
    }

    public boolean intersects(Rectangle rect){
        return rect.contains(position.x, position.y) || hitbox.contains(rect.getX(), rect.getY()) ||
        hitbox.contains(rect.getX() + rect.getWidth(), rect.getY()) || hitbox.contains(rect.getX(),
                rect.getY() + rect.getHeight())
        || hitbox.contains(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
    }

    public boolean intersects(Circle circle){
        return hitbox.overlaps(circle);
    }

    public String getShootType(){
        return shootType;
    }

    public void setShootType(String shootType){
        powerupInit = System.currentTimeMillis();

        if(shootType.equals("barrier")){
            barrier = new Barrier(position, 75);
        }

        this.shootType = shootType;
    }

    public Barrier getBarrier(){
        return barrier;
    }

    public long getFireRate(){
        return fireRate;
    }
}