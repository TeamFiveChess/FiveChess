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
    private Bitmap mWhitePiece;     //位图    操作图片
    private Bitmap mBlackPiece;

    private float pieceRatio = 3 * 1.0f / 4;

    //白棋先下或当前轮到白棋
    private boolean mIsWhite = true;
    private List<Point> mWhiteArray = new ArrayList<>();  //动态数组存储白棋坐标
    private List<Point> mBlackArray = new ArrayList<>();

    private boolean mIsGameOver;
    private  boolean mIsWhiteWinner;


    private Draw draw = new Draw();

    private checkFiveInLine check = new checkFiveInLine();

    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setBackgroundColor(0x44ff0000);
        init();
    }

    //对画笔paint进行初始化
    public void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
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
    public Point getValidPoint(int x, int y) {
        return new Point((int)(x / mLineHeight), (int)(y / mLineHeight));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        draw.drawBoard(canvas,mPaint,mPanelWidth,mLineHeight);  //绘制棋盘

        draw.drawPieces(canvas,mWhiteArray,mBlackArray,mWhitePiece,mBlackPiece,mLineHeight); //绘制棋子
        checkGameOver();   //判断游戏是否结束
    }

    public void checkGameOver() {
        boolean whiteWin = check.checkFiveInLine(mWhiteArray);
        boolean blackWin = check.checkFiveInLine(mBlackArray);

        if(whiteWin || blackWin){
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;
            String text = mIsWhiteWinner?"白棋胜利" :"黑棋胜利";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }

//    再来一局
    public void restart(){
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver = false;
        mIsWhiteWinner = false;
        invalidate();
    }
}