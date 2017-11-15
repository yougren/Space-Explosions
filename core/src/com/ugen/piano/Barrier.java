package com.ugen.piano;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Eugene Munblit on 11/8/2017.
 */

public class Barrier {
    private Vector2 position;
    private float radius;
    private float theta;
    private ArrayList<Triangle> triangles;

    public Barrier(Vector2 position, float radius){
        this.position = position;
        this.radius = radius;
        this.theta = 0;
        this.triangles = new ArrayList<Triangle>();

        for(int i = 0; i < 8 ; i++){
           triangles.add(new Triangle(new Vector2((float)(position.x + radius*Math.cos(i*2*Math.PI/8)),
                   (float)(position.y + radius*Math.sin(i*2*Math.PI/8))), 15));
        }
    }

    public void draw(ShapeRenderer renderer){
        renderer.setColor(Color.BLUE);
        theta += .01f;

        for(int i = 0; i < triangles.size(); i++){
            if(triangles.get(i).isActive()) {
                Triangle tri = triangles.get(i);

                tri.setPosition(new Vector2((float) (position.x + radius * Math.cos(i * 2 * Math.PI / 8 + theta)),
                        (float) (position.y + radius * Math.sin(i * 2 * Math.PI / 8 + theta))));

                tri.rotate(theta);
                tri.draw(renderer);
            }
        }
    }

    public void setPosition(Vector2 pos){
        this.position = pos;
        for(Triangle tri : triangles){
            tri.setPosition(position);
        }
    }

    public ArrayList<Triangle> getTriangles(){
        return triangles;
    }

    public void deactivate(int i){
        triangles.get(i).setActive(false);
    }
}
