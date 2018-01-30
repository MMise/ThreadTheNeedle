package neula.sovellus;

import android.graphics.* ;  // Classes Canvas, Color, Paint, RectF, etc.

class Ball
{
    int ball_center_point_x  =  0 ;
    int ball_center_point_y  =  0 ;

    int ball_color  =  Color.RED ;

    int ball_diameter  =  128;

    public Ball( int given_center_point_x,
                 int given_center_point_y,
                 int given_color )
    {
        ball_center_point_x  =  given_center_point_x ;
        ball_center_point_y  =  given_center_point_y ;
        ball_color           =  given_color ;
    }


    public int get_ball_center_point_x()
    {
        return ball_center_point_x ;
    }

    public int get_ball_center_point_y()
    {
        return ball_center_point_y ;
    }


    public void move_to_position( int new_center_point_x,
                                  int new_center_point_y )
    {
        ball_center_point_x  =  new_center_point_x ;
        ball_center_point_y  =  new_center_point_y ;
    }


    public void draw( Canvas canvas )
    {
        Paint filling_paint = new Paint() ;
        filling_paint.setStyle( Paint.Style.FILL ) ;
        filling_paint.setColor( ball_color ) ;
        Paint outline_paint = new Paint() ;
        outline_paint.setStyle( Paint.Style.STROKE ) ;

        canvas.drawCircle( ball_center_point_x, ball_center_point_y,
                ball_diameter / 2, filling_paint ) ;

        canvas.drawCircle( ball_center_point_x, ball_center_point_y,
                ball_diameter / 2, outline_paint ) ;
    }
}
