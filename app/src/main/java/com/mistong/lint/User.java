package com.mistong.lint;

import android.support.annotation.Keep;
import android.view.View;

import java.io.Serializable;

@Keep
public class User implements Serializable{
    public String name;

    private void getName(){

    }

    @Keep
    public static class Student implements Serializable{

    }
}
