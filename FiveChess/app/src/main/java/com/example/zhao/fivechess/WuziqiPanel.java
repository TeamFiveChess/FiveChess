package com.example.zhao.fivechess;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class WuziqiPanel extends View {

    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;

    private  Paint mPaint = new Paint();

    //棋子
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float pieceRatio = 3 * 1.0f / 4;

    //白棋先下或当前轮到白棋
    private boolean mIsWhite = true;
    private List<Point> mWhiteArray = new ArrayList<>();  //存储白棋坐标
    private List<Point> mBlackArray = new ArrayList<>();

    private boolean mIsGameOver;
    private  boolean mIsWhiteWinner;

    private int MAX_COUNT_IN_LINE = 5;

    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44ff0000);
        init();
    }

    //对画笔paint进行初始化
    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize,heightSize);

//        如果宽高最小为0，则取两者最大值
        if(widthMode == MeasureSpec.UNSPECIFIED)
            width = heightSize;
        else if(heightSize == MeasureSpec.UNSPECIFIED)
            width = widthSize;
        setMeasuredDimension(width,width);

    }

    //初始化棋盘
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth*1.0f / MAX_LINE;

        int pieceWidth = (int) (mLineHeight * pieceRatio);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);
    }

    //点击事件处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIsGameOver) return false;

        int action = event.getAction();
        if(action == MotionEvent.ACTION_UP){
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x, y);

            if(mWhiteArray.contains(p)||mBlackArray.contains(p)){
                return false;
            }

            if(mIsWhite)
                mWhiteArray.add(p);
            else
                mBlackArray.add(p);
            invalidate();
            mIsWhite = !mIsWhite;
        }
        return true;
    }

    //避免不同子落在同一位置
    private Point getValidPoint(int x, int y) {
        return new Point((int)(x / mLineHeight), (int)(y / mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        drawBoard(canvas);

        drawPieces(canvas); //绘制棋子
        checkGameOver();   //判断游戏是否结束
    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);

        if(whiteWin || blackWin){
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;
            String text = mIsWhiteWinner?"白棋胜利" :"黑棋胜利";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }
	
	private void drawPieces(Canvas canvas) {

        //绘制白子
        for(int i = 0,n = mWhiteArray.size();i < n ;i++){
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x + (1 - pieceRatio) / 2) * mLineHeight,
                    (whitePoint.y + (1 - pieceRatio) / 2) * mLineHeight, null);
        }

        //绘制黑子
        for(int i = 0,n = mBlackArray.size();i < n ;i++){
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x + (1 - pieceRatio) / 2) * mLineHeight,
                    (blackPoint.y + (1 - pieceRatio) / 2) * mLineHeight, null);
        }
    }

    //绘制棋盘
    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;


        for(int i = 0; i < MAX_LINE; i++){
            int startX = (int)(lineHeight/2);
            int endX = (int)(w-lineHeight/2);

            int y = (int) (( 0.5 + i) * lineHeight);
            canvas.drawLine(startX, y , endX, y, mPaint);//划横线
            canvas.drawLine(y, startX, y, endX, mPaint);
        }

    }
	
	
	
	
	
	
	
	
	
	private boolean checkFiveInLine(List<Point> points) {
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
    private boolean checkHorizontal(int x, int y, List<Point> points) {
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
    private boolean checkVertical(int x, int y, List<Point> points) {
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
    private boolean checkZuoxie(int x, int y, List<Point> points) {
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
    private boolean checkYouxie(int x, int y, List<Point> points) {
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