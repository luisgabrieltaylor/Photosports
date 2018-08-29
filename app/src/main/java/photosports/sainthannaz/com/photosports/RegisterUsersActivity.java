package photosports.sainthannaz.com.photosports;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import photosports.sainthannaz.com.photosports.tools.DatabaseHandler;
import photosports.sainthannaz.com.photosports.tools.PermissionUtils;

import static java.security.AccessController.getContext;

public class RegisterUsersActivity extends Activity {
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.image_a, R.drawable.image_b, R.drawable.image_c, R.drawable.image_d, R.drawable.image_e, R.drawable.image_f, R.drawable.image_g, R.drawable.image_h, R.drawable.image_i,};
    String pass="admin";
    String password;
    Button register;
    FormEditText inputName, inputLastname, inputPhone, inputEmail, inputRunner;
    String name, lastname, email, phone, runner, origin;
    private ProgressDialog pDialog;
    DatabaseHandler db;
    private static final int MY_READ_WRITE_PERMISSION_REQUEST_CODE = 1;
    private static final int READ_WRITE_PERMISSION_REQUEST_CODE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_register_users);
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        db = new DatabaseHandler(getApplicationContext());
        // Dialogo de proceso para el login
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        inputName = findViewById(R.id.input_name);
        inputLastname = findViewById(R.id.input_lastname);
        inputEmail = findViewById(R.id.input_email);
        inputPhone = findViewById(R.id.input_phone);
        inputRunner = findViewById(R.id.input_runner);
        register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FormEditText[] allFields	= { inputName, inputLastname, inputEmail, inputPhone, inputRunner };
                boolean allValid = true;
                for (FormEditText field: allFields) {
                    allValid = field.testValidity() && allValid;
                }

                if (allValid) {

                    name = inputName.getText().toString();
                    lastname = inputLastname.getText().toString();
                    email = inputEmail.getText().toString();
                    phone = inputPhone.getText().toString();
                    runner = inputRunner.getText().toString();
                    insertRunnerData();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.validation_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    @Override
    public void onBackPressed() {
        // Creating alert Dialog with one Button
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterUsersActivity.this);

        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Ir al menu principal");

        // Setting Dialog Message
        alertDialog.setMessage("¿Deseas regresar al menu principal?");
        // Setting Icon to Dialog


        alertDialog.setPositiveButton(R.string.ok_text,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       finish();

                    }
                });

        alertDialog.setNegativeButton(R.string.close,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public void insertRunnerData(){
        pDialog.setMessage("Guardando registro...");
        showDialog();
        db.addCustomerData(name, lastname, email, phone, runner);
        inputName.setText("");
        inputLastname.setText("");
        inputEmail.setText("");
        inputPhone.setText("");
        inputRunner.setText("");
        hideDialog();
        Toast.makeText(getApplicationContext(), getString(R.string.success_register_user),
                Toast.LENGTH_LONG).show();
        inputName.requestFocus();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }





}
