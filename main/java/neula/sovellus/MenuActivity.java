package neula.sovellus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

/**
 * Created by Mikko on 6.2.2018.
 */

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
    }

    public void startButtonClicked(View v){
        Intent intent = new Intent(this,NeedleActivity.class);
        startActivity(intent);
    }
}
