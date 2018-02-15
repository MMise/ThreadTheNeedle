package neula.sovellus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import java.util.Random;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class NeedleView extends View implements Runnable{

    int canvasW;
    int canvasH;
    int points = 0;
    String parsedString;

    Lanka lanka = new Lanka(0,0);
    Neula neula;

    TextView gameover; //Teksti, joka näytetään kun peli on ohi. Saadaan activitystä myöhemmin.
    Button gameOverButton;
    Thread animationThread = null;
    boolean threadMustBeExecuted = false;

    //satunnaisgeneraattori
    long ticksSinceStart = 0;
    Random generator = new Random();
    int randomSeed = 0;
    long newPosition = 0;

    boolean threadPassed = false; //Onko lanka neulan takana
    float deadZone = 0.2f;
    float multiplier = 0.7f; //sensoriarvojen vaikutus liikkeeseen
    int constantSpeed = 3; //vakionopeus kallistuksen havaittaessa
    int threadStartX = 0;
    boolean firstCycle = true; //Käytetään neulan ja langan aloitussijainnin asettamiseen ensimmäisellä ohjelman suoritussyklillä

    public NeedleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        neula = new Neula(context);
    }

    public void setGameoverTextView(TextView a) {
        gameover = a;
    }

    public void setGameOverButton(Button btn){
        gameOverButton = btn;
    }

    public void getValues(float y, float x){ //neulan ja langan liikuttaminen sensoriarvoilla
        float xx = x*multiplier,
                yy = y*multiplier;


        if(xx > deadZone){
            if(neula.x > 0){
                neula.x -= (int)xx+constantSpeed;
            }
        }
        else if(xx < -deadZone){
            if(neula.x < canvasW){
                neula.x -= (int)xx-constantSpeed;
            }
        }
        if(yy > deadZone){
            if(lanka.startY < canvasH * 0.9){ //estä lankaa karkaamasta ulos näytöltä
                lanka.startY += (int)yy+constantSpeed;
            }
        }
        else if(yy < -deadZone){
            if(lanka.startY > 0){
                lanka.startY += (int)yy-constantSpeed;
            }
        }
        invalidate();
    }

    public void setWillDraw(){
        setWillNotDraw(false);
    }

    protected void onDraw(Canvas canvas) {
        System.out.println("onDraw function");
        canvasW = canvas.getWidth();
        canvasH = canvas.getHeight();
        if(firstCycle){
            System.out.println("First Cycle!");
            //Arvotaan neulan ja langan aloitussijainnit X-akselilla
            randomSeed = (int) (Math.random() * 10) + 2;
            System.out.println("Random seed langalle: " + randomSeed);
            threadStartX = canvasW / randomSeed; //muuttujaa käytetään langan X-aloitussijainnin tallentamiseen. Tärinä stabiloidaan tämän pisteen ympärille
            System.out.println("Aloitus X langalle: " + threadStartX);
            lanka.moveToPosition(threadStartX,  (int) (canvasH / 1.5));
            randomSeed = (int) (Math.random() * 10) + 1;
            neula.moveToPosition(canvasW / randomSeed, canvasH / 4);
            firstCycle = false;
        }else{
            if(!threadPassed) { //Lanka on neulan takana, kosketus ei aiheuta pelin loppumista
                if (lanka.getY() < neula.getY()) {

                    int[] edges = neula.getEdges();
                    if(lanka.getX() < edges[0] || lanka.getX() > edges[1]) { //Lanka ei osunut neulaan
                        threadPassed = true;
                        return;
                    }

                    int[] hitBox = neula.getHitbox();
                    if (lanka.getX() > hitBox[0] && lanka.getX() < hitBox[1]) //Lanka on saatu onnistuneesti neulansilmään
                    {
                        points++;
                        stopThread();
                        parsedString = getResources().getString(R.string.victory) + getResources().getString(R.string.wins) + points;
                        gameover.setText(parsedString);
                        gameover.setTextColor(getResources().getColor(R.color.GREEN));
                    } else //Lanka osui neulaan
                    {
                        stopThread();
                        parsedString = getResources().getString(R.string.loss) + getResources().getString(R.string.wins) + points;
                        gameover.setText(parsedString);
                        gameover.setTextColor(getResources().getColor(R.color.RED));
                        points = 0;
                    }
                    gameover.setVisibility(VISIBLE);
                    gameOverButton.setVisibility(VISIBLE);
                    setWillNotDraw(true);
                    return;
                }
            } else { //Lankaa voi yrittää neulansilmään uudestaan vasta, kun se on vedetty takaisin neulan takaa
                if(lanka.getY() > neula.getHEdge())
                    threadPassed = false;
            }
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
            System.out.println("Thread function!");
            ticksSinceStart++;
            //Arvotaan langan sijainti
            randomSeed = generator.nextInt((1800 - 200 + 1) + 200);
            newPosition = (long) (Math.sin(ticksSinceStart) * (randomSeed/100));
            //Langan Y-sijainti ei muutu, tärinää lisätään X-sijainnille. Tärinä on stabiloitu aloitussijannin ympärille
            lanka.moveToPosition((threadStartX + (int) newPosition), lanka.getY());

            //arvotaan neulan sijainti
            randomSeed = generator.nextInt((1800 - 200 + 1) + 200);
            newPosition = (long) (Math.sin(ticksSinceStart) * (randomSeed/100));
            //Neulan X-sijainti ei muutu, tärinää lisätään Y-sijainnille. CanvasH / 4 on neulan Y-sijainti aloitushetkellä
            neula.moveToPosition(neula.getX(), (canvasH / 4) + (int) newPosition);
            try{
                animationThread.sleep(10);
            }catch(InterruptedException ex){
                threadMustBeExecuted = false;
            }

            postInvalidate();
        }
    }
}
