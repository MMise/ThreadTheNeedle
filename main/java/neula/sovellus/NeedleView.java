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

    Lanka lanka = new Lanka(0,0);
    Ball neula = new Ball(0,0,Color.BLUE);

    Thread animationThread = null;
    boolean threadMustBeExecuted = false;
    long ticksSinceStart = 0;
    int randomSeed = 0;
    Random generator = new Random();
    long newPosition = 0;
    boolean firstCycle = true; //Käytetään neulan ja langan aloitussijainnin asettamiseen ensimmäisellä ohjelman suoritussyklillä

    public void onSizeChanged( int current_width_of_this_view,
                               int current_height_of_this_view,
                               int old_width_of_this_view,
                               int old_height_of_this_view )
    {
        /*
        System.out.println("canvasW: " + canvasW + " CanvasH: " + canvasH);
        lanka.moveToPosition((canvasW / 2),  canvasH); // lanka piirtyy näytön leveyssuunnassa keskelle
        neula.move_to_position(canvasW / 2, canvasH / 3);
        System.out.println("NeulaX: " + neula.ball_center_point_x + " neulaY: " + neula.ball_center_point_y);
        */
    }

    public NeedleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void getValues(float y, float x){
        int xx = (int) (x*0.7),
                yy = (int) (y*0.7);


        if(xx > 0){
            if(neula.ball_center_point_x > 0){
                neula.ball_center_point_x -= xx;
            }
        }
        else if(xx < 0){
            if(neula.ball_center_point_x < canvasW){
                neula.ball_center_point_x -= xx;
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
        if(firstCycle){
            lanka.moveToPosition(canvasW / 2,  (int) (canvasH / 1.5));
            neula.move_to_position(canvasW / 2, canvasH / 4);
            firstCycle = false;
        }else{
            lanka.draw(canvas);
            neula.draw(canvas);
        }

    }

    public void startThread(){
        if(animationThread == null){
            animationThread = new Thread(this);
            threadMustBeExecuted = true;
            animationThread.start();
        }
    }

    public void stopThread(){
        if(animationThread != null){
            animationThread.interrupt();
            threadMustBeExecuted = false;
            animationThread = null;
        }
    }


    @Override
    public void run() {
        while(threadMustBeExecuted){

            ticksSinceStart++;
            //Arvotaan langan sijainti
            randomSeed = generator.nextInt((1800 - 200 + 1) + 200);
            newPosition = (long) (Math.sin(ticksSinceStart) * (randomSeed/100));
            //Langan Y-sijainti ei muutu, tärinää lisätään X-sijainnille. CanvasW / 2 on langan X-sijainti aloitushetkellä
            lanka.moveToPosition((canvasW / 2 + (int) newPosition), lanka.getY());

            //arvotaan neulan sijainti
            randomSeed = generator.nextInt((1300 - 700 + 1) + 700);
            newPosition = (long) (Math.sin(ticksSinceStart) * (randomSeed/100));
            //Neulan X-sijainti ei muutu, tärinää lisätään Y-sijainnille. CanvasH / 4 on neulan Y-sijainti aloitushetkellä
            neula.move_to_position(neula.get_ball_center_point_x(), (canvasH / 4) + (int) newPosition);
            try{
                animationThread.sleep(10);
            }catch(InterruptedException ex){
                threadMustBeExecuted = false;
            }

            postInvalidate();
        }
    }
}
