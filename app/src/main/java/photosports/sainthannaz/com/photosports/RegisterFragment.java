package photosports.sainthannaz.com.photosports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;

import photosports.sainthannaz.com.photosports.tools.DatabaseHandler;

/**
 * Created by Gabriel on 29/12/2017.
 */

public class RegisterFragment extends Fragment {
    FormEditText inputName, inputLastname, inputPhone, inputEmail, inputRunner;
    String name, lastname, email, phone, runner, origin;
    private ProgressDialog pDialog;
    DatabaseHandler db;
    Button register;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_register,null);
        db = new DatabaseHandler(getActivity());
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        inputName = view.findViewById(R.id.input_name);
        inputLastname = view.findViewById(R.id.input_lastname);
        inputEmail = view.findViewById(R.id.input_email);
        inputPhone = view.findViewById(R.id.input_phone);
        inputRunner = view.findViewById(R.id.input_runner);
        register = view.findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNext(view);

            }
        });
        return view;

    }

    public void onClickNext(View view) {
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
            insertRunnerData();
        } else {
            Toast.makeText(getContext(), getString(R.string.validation_error),
                    Toast.LENGTH_LONG).show();
        }
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
        Toast.makeText(getContext(), getString(R.string.success_register),
                Toast.LENGTH_LONG).show();
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
