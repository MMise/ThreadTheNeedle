package neula.sovellus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import java.util.Random;
import android.view.View;

public class NeedleView extends View implements Runnable{

    int canvasW;
    int canvasH;

    Lanka lanka = new Lanka(200,300);
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
        lanka.moveToPosition(150, 250);
        neula.move_to_position(700, 250);
    }

    public NeedleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void getValues(float y, float x){
        int xx = (int) (x*0.7),
            yy = (int) (y*0.7);
        System.out.println(neula.ball_center_point_x);
        if(xx < 0){
            if(neula.ball_center_point_x < canvasW){
                neula.ball_center_point_x += (xx+2);
            }
        }
        else if(xx > 0){
            if(neula.ball_center_point_x > 0){
                neula.ball_center_point_x += (xx-2);
            }
        }
        if(yy > 0){
            if(lanka.startY < canvasH){
                lanka.startY += yy;
            }
        }
        else if(yy < 0){
            if(lanka.startY > 0){
                 lanka.startY += yy;
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

            lanka.moveToPosition(250 + (int) newPosition, lanka.getY() );

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
