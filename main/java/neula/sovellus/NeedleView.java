package neula.sovellus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class NeedleView extends View {

    ArrayList<Float> xListView = new ArrayList<>();
    int ballCenterX = 500;
    int ballCenterY = 700;
    int ballRadius = 50;

    public NeedleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void getValues(float x){
        ballCenterX += x / 2;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        Paint ballPaint = new Paint();
        ballPaint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(48);
        canvas.drawText("KEK",canvas.getWidth() / 2,canvas.getHeight() / 2,paint);
        canvas.drawCircle(ballCenterX,ballCenterY,ballRadius,ballPaint);
    }
}
