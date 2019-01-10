package com.example.zhao.fivechess;

import android.graphics.Point;

import java.util.List;

/**
 * 判断是否五子连珠
 */
public class checkFiveInLine {

    private int MAX_COUNT_IN_LINE = 5;

    public checkFiveInLine() {

    }

    public  boolean checkFiveInLine(List<Point> points) {
        for(Point p : points){
            int x = p.x;
            int y = p.y;

            boolean win = checkHorizontal(x,y,points);
            if(win) return true;
            win = checkVertical(x,y,points);
            if(win) return true;
            win = checkYouxie(x,y,points);
            if(win) return true;
            win = checkZuoxie(x,y,points);
            if(win) return true;
        }
        return false;
    }

    //判断x,y位置的棋子是否横向有五个相邻的
    public boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        //左边
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x-i,y)))
                count++;
            else
                break;
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        //右边
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x+i,y)))
                count++;
            else
                break;
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        return false;
    }

    //判断x,y位置的棋子是否纵向有五个相邻的
    public boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        //上
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x,y-i)))
                count++;
            else
                break;
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        //下
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x,y+i)))
                count++;
            else
                break;
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        return false;
    }

    //判断x,y位置的棋子是否左斜向向有五个相邻的
    public boolean checkZuoxie(int x, int y, List<Point> points) {
        int count = 1;
        //左下斜
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x-i,y+i)))
                count++;
            else
                break;
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        //左上斜
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x+i,y-i)))
                count++;
            else
                break;
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        return false;
    }

    //判断x,y位置的棋子是否右斜向有五个相邻的
    public boolean checkYouxie(int x, int y, List<Point> points) {
        int count = 1;
        //右上斜
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x-i,y-i)))
                count++;
            else
                break;
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        //右下斜
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x+i,y+i)))
                count++;
            else
                break;
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        return false;
    }
}