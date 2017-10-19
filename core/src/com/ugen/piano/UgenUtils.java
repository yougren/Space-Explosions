package com.ugen.piano;

import java.lang.reflect.Array;

/**
 * Created by Eugene Munblit on 10/18/2017.
 */

public  class UgenUtils {
    private UgenUtils(){

    }

    public static <T> T[] concatArrays(T[] a, T[] b){
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen+bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }
}
