package com.martinryberglaude.skyfall.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public class WindDirection {
    @IntDef({N, NE, E, SE, S, SW, W, NW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {}
    public static final int N = 0;
    public static final int NE = 1;
    public static final int E = 2;
    public static final int SE = 3;
    public static final int S = 4;
    public static final int SW = 5;
    public static final int W = 6;
    public static final int NW = 7;
}


