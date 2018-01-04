package photosports.sainthannaz.com.photosports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;

import photosports.sainthannaz.com.photosports.models.Customers;
import photosports.sainthannaz.com.photosports.tools.DatabaseHandler;

/**
 * Created by Gabriel on 29/12/2017.
 */

public class EditActivity extends AppCompatActivity {
    DatabaseHandler db;
    FormEditText inputName, inputLastname, inputPhone, inputEmail, inputRunner;
    Integer idCustomer;
    String name, lastname, email, phone, runner, origin;
    Button save, close;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        inputName = findViewById(R.id.input_name);
        inputLastname = findViewById(R.id.input_lastname);
        inputEmail = findViewById(R.id.input_email);
        inputPhone = findViewById(R.id.input_phone);
        inputRunner = findViewById(R.id.input_runner);
        save = findViewById(R.id.save_button);
        close = findViewById(R.id.close_button);

        db = new DatabaseHandler(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idCustomer = extras.getInt("idCustomer");
            System.out.println("User ID: " + idCustomer);
            Customers customersData = db.getInfoCustomer(idCustomer);
            inputName.setText(customersData.getcustomer_name());
            inputLastname.setText(customersData.getcustomer_lastname());
            inputEmail.setText(customersData.getcustomer_email());
            inputPhone.setText(customersData.getcustomer_phone());
            inputRunner.setText(customersData.getcustomer_runner());

            //The key argument here must match that used in the other activity
        }  else {
            System.out.println("¡No pasaron datos o no existen!");
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNext();

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

    public void onClickNext() {
        FormEditText[] allFields	= { inputName, inputLastname, inputEmail, inputPhone, inputRunner };


        boolean allValid = true;
        for (FormEditText field: allFields) {
            allValid = field.testValidity() && allValid;
        }

        if (allValid) {
            // YAY
            name = inputName.getText().toString();
            lastname = inputLastname.getText().toString();
            email = inputEmail.getText().toString();
            phone = inputPhone.getText().toString();
            runner = inputRunner.getText().toString();
            db.updateCustomer(idCustomer, name, lastname, email, phone, runner);
            Toast.makeText(getApplication(), getString(R.string.update_message),
                    Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(getApplication(), getString(R.string.validation_error),
                    Toast.LENGTH_LONG).show();

        }
    }
}
