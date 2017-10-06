/*
package com.ugen.piano;

import com.badlogic.gdx.utils.Array;

*/
/**
 * Created by Eugene Munblit on 10/3/2017.
 *//*


public class TPool<T> {

    private T t;
    public int max = 10;
    private Array<T> freeT;

    public TPool(T t, int initialCapacity, int max){
        freeT = new Array<T>(false, initialCapacity);

        for(int i = 0; i < initialCapacity; i++)
            freeT.add(returnT(T.class));


        this.max = max;
        this.t = t;
    }

    private <T> T returnT(Class<T> clazz){
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
*/
