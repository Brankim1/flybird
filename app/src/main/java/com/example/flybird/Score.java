package com.example.flybird;

import org.litepal.crud.LitePalSupport;

public class Score extends LitePalSupport {
    private int score;

    public int getScore(){
        return score;
    }
    public void setScore(int score){
        this.score=score;
    }
}
