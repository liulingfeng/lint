package com.mistong.lint;

import android.annotation.SuppressLint;
import android.support.annotation.Keep;

import java.io.Serializable;

@SuppressLint("all")
@Keep
public class User implements Serializable{
    public String name;
}
