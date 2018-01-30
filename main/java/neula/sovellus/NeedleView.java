package neula.sovellus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;


public class NeedleView extends View implements Runnable {
    Ball palloX = new Ball(100,100,Color.RED);
    Ball palloY = new Ball(100,100,Color.BLUE);

    Thread animationThread = null;
    boolean threadMustBeExecuted = false;
    long ticksSinceStart = 0;
    int randomSeed = 0;
    Random generator = new Random();
    long newPosition = 0;

    ArrayList<Float> xListView = new ArrayList<>();

    public NeedleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void onSizeChanged( int current_width_of_this_view,
                               int current_height_of_this_view,
                               int old_width_of_this_view,
                               int old_height_of_this_view )
    {
        palloX.move_to_position(250, 150);
        palloY.move_to_position(250, 500);
    }

    protected void onDraw(Canvas canvas) {
        palloX.draw(canvas);
        palloY.draw(canvas);
        Paint outline_paint = new Paint();
        outline_paint.setStyle(Paint.Style.STROKE);
    }

    public void startThread(){
        if(animationThread == null){
            animationThread = new Thread(this);
            threadMustBeExecuted = true;
            animationThread.start();
        }
        System.out.println("Thread started!");
    }

    public void stopThread(){
        if(animationThread != null){
            animationThread.interrupt();
            threadMustBeExecuted = false;
            animationThread = null;
        }
        System.out.println("Thread stopped!");
    }


    @Override
    public void run() {
        System.out.println("The thread is running my dudes!");
        while(threadMustBeExecuted){

            ticksSinceStart++;
            //Arvotaan neulan sijainti
            randomSeed = generator.nextInt((1300 - 700 + 1) + 700);
            newPosition = (long) (Math.sin(ticksSinceStart) + (randomSeed/100));

            palloX.move_to_position(palloX.get_ball_center_point_x(), 150 + (int) newPosition);

            //arvotaan langan sijainti
            randomSeed = generator.nextInt((1800 - 200 + 1) + 200);
            newPosition = (long) (Math.sin(ticksSinceStart) + (randomSeed/100));
            palloY.move_to_position(250 + (int) newPosition, palloY.get_ball_center_point_y());
            try{
                animationThread.sleep(10);
            }catch(InterruptedException ex){
                threadMustBeExecuted = false;
            }

            postInvalidate();
        }
        System.out.println("The thread has stopped my dudes!");
    }
}

