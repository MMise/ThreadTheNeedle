package neula.sovellus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import java.util.Random;
import android.view.View;

public class NeedleView extends View implements Runnable{

    int ballCenterX = 500;
    int ballCenterY = 700;
    int ballRadius = 50;
    int canvasW;
    int canvasH;

    Ball lanka = new Ball(100,100,Color.RED);
    Ball neula = new Ball(100,100,Color.BLUE);

    Thread animationThread = null;
    boolean threadMustBeExecuted = false;
    long ticksSinceStart = 0;
    int randomSeed = 0;
    Random generator = new Random();
    long newPosition = 0;

    public void onSizeChanged( int current_width_of_this_view,
                               int current_height_of_this_view,
                               int old_width_of_this_view,
                               int old_height_of_this_view )
    {
        lanka.move_to_position(150, 250);
        neula.move_to_position(700, 250);
    }

    public NeedleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void getValues(float y, float x){
        int xx = (int) (x*0.7),
            yy = (int) (y*0.7);

        if(x > 0){
            if(lanka.ball_center_point_x < canvasW){
                lanka.ball_center_point_x += xx+2;
            }
        }
        else if(x < 0){
            if(lanka.ball_center_point_x > 0){
                lanka.ball_center_point_x += xx-2;
            }
        }
        if(y > 0){
            if(neula.ball_center_point_y < canvasH){
                neula.ball_center_point_y += yy+2;
            }
        }
        else if(y < 0){
            if(neula.ball_center_point_y > 0){
                neula.ball_center_point_y += yy-2;
            }
        }
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        canvasW = canvas.getWidth();
        canvasH = canvas.getHeight();
        lanka.draw(canvas);
        neula.draw(canvas);
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
            //Arvotaan langan sijainti
            randomSeed = generator.nextInt((1800 - 200 + 1) + 200);
            newPosition = (long) (Math.sin(ticksSinceStart) + (randomSeed/100));

            lanka.move_to_position(lanka.get_ball_center_point_x(), 250 + (int) newPosition);

            //arvotaan neulan sijainti
            randomSeed = generator.nextInt((1300 - 700 + 1) + 700);
            newPosition = (long) (Math.sin(ticksSinceStart) * (randomSeed/100 - 1301/200));
            neula.move_to_position(neula.get_ball_center_point_x(), neula.get_ball_center_point_y() + (int) newPosition);
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
