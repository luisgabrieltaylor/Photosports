package photosports.sainthannaz.com.photosports;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Gabriel on 29/12/2017.
 */

public class SplashScreen extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 500;
    private static String TAG = MainActivity.class.getSimpleName();
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Permite la visualizacion de la aplicacion sin ventana de titulo
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,// Permite la visualizacion de la aplicacion sin ventana de titulo
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  // Permite la visualizacion de la aplicacion sin ventana de titulo
        setContentView(R.layout.activity_splashscreen);

        // Thread for displaying the SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(200);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    onStop();
                }
            }
        };
        splashTread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _active = false;
        }
        return true;
    }

}
