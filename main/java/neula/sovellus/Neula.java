package neula.sovellus;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.* ;  // Classes Canvas, Color, Paint, RectF, etc.
import android.graphics.drawable.Drawable;
import android.os.Build;

class Neula
{
    //Arvoja, joita tarvitaan kuvan ulottuvuuksien ymmärtämiseksi
    final int w = 522,
              h = 70,
              centerX = 475,
              centerY = 39,
              holeStart = 460,
              holeEnd = 491;

    //reiän keskipiste
    int x = centerX,
        y = centerY;

    Rect bounds = new Rect(0, 0, w, h);
    Drawable sprite;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Neula(Context context) {
        sprite = context.getDrawable(R.drawable.needle);
    }

    public void moveToPosition(int posX, int posY) {
        x = posX;
        y = posY;

        //Kuva tarvitsee boundit, jotka lasketaan kuvan koon ja koordinaattien perusteella
        bounds.set(x - centerX, y - centerY, x + w - centerX, y + h - centerY);
    }

    public int[] getHitbox() { //palauttaa reiän X-koordinaatit, Y-koordinaatti on y
        return new int[] {x - centerX + holeStart, x - centerX + holeEnd};
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getHEdge() { return h - centerY + y; }

    public int[] getEdges() { return new int[] {x - centerX, w - centerX + x}; }

    public void draw( Canvas canvas )
    {
        sprite.setBounds(bounds);
        sprite.draw(canvas);
    }
}
