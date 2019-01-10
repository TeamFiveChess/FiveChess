package com.example.zhao.fivechess;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.List;
/**
 *   绘制棋盘和棋子
 */
public class Draw {

    private int MAX_LINE = 10;

    private float pieceRatio = 3 * 1.0f / 4;

    public void drawPieces(Canvas canvas,List<Point> mWhiteArray,List<Point> mBlackArray,Bitmap mWhitePiece,
                           Bitmap mBlackPiece,float mLineHeight) {

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
    public void drawBoard(Canvas canvas, Paint mPaint, int mPanelWidth, float mLineHeight) {
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


}
