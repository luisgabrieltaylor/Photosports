package photosports.sainthannaz.com.photosports;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import photosports.sainthannaz.com.photosports.tools.Hosts;
import photosports.sainthannaz.com.photosports.tools.SessionManager;
import photosports.sainthannaz.com.photosports.tools.VolleySingleton;

/**
 * Created by Gabriel on 17/01/2018.
 */

public class LoginActivity extends Activity {
    FormEditText inputUser, inputPassword;
    private long back_pressed;
    Button btnLogin;
    private ProgressDialog pDialog;
    private SessionManager session;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        // Edittext
        inputUser = (FormEditText) findViewById(R.id.user);
        inputPassword = (FormEditText) findViewById(R.id.password);

        // Botones de Login y Registro (Este ultimo es temporal)
        btnLogin = (Button) findViewById(R.id.btnLogin);

        // Dialogo de proceso para el login
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(intent);

        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                FormEditText[] allFields    = { inputUser, inputPassword };
                String user = inputUser.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();


                boolean allValid = true;
                for (FormEditText field: allFields) {
                    allValid = field.testValidity() && allValid;
                }
                // Si los valores en los campo son validos se procede al logeo en el sistema
                if (allValid) {
                    checkLogin(user, password);
                } else {
                    // De lo contrario se muestra un toast indicando que los datos son incorrectos
                    Toast.makeText(getBaseContext(),
                            getString(R.string.error_credentials), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    public void checkLogin(String user, String password){
        HashMap<String, String> login = new HashMap<>();// Mapeo previo
        login.put("user", user);
        login.put("password", password);
        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(login);
        System.out.println(jobject.toString()); Log.d(TAG, jobject.toString());
        VolleySingleton.getInstance(getApplication()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Hosts.GET_LOGIN,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response);
                                // Procesar la respuesta del servidor
                                procesarRespuesta(response);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("Ocurrio un error! ");
                                Log.d(TAG, "Error Volley: " + error.getMessage());

                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    /**
     * Procesa la respuesta obtenida desde el sevidor
     *
     * @param response Objeto Json
     */
    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");
            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(
                            getApplication(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    session.setLogin(true);
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    finish();
                    startActivity(intent);
                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            getApplication(),
                            mensaje,
                            Toast.LENGTH_LONG).show();


                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.toast_PressAgain), Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }

}
