package neula.sovellus;

import android.graphics.*;

class Lanka
{
    // Nämä kaksi ovat langan pää, joka täytyy saada neulaan.
    int startX  =  0;
    int startY  =  0;
    int threadColor  =  Color.RED;

    public Lanka(int givenStartX, int givenStartY)
    {
        startX = givenStartX;
        startY = givenStartY;
    }


    public int getX()
    {
        return startX;
    }

    public void setX(int newX) { startX = newX; }

    public int getY()
    {
        return startY;
    }

    public void setY(int newY){
        startY = newY;
    }


    public void moveToPosition( int newStartX, int newStartY)
    {
        startX = newStartX;
        startY = newStartY;
    }


    public void draw( Canvas canvas )
    {
        Paint threadPaint = new Paint();
        threadPaint.setColor(threadColor);
        /*
        langan x-koordinaattien alku- ja loppupäät pysyvät samana, näin saadaan suora viiva.
        startY on tässä se koordinaattipiste, joka muuttuu kun puhelinta kallistetaan.
        stopY:nä käytetään canvaksen korkeutta, näin langan Y-akselin loppupää piirretään aina canvaksen pohjalle
         */
        canvas.drawLine(startX, startY, startX, canvas.getHeight(), threadPaint);
    }
}