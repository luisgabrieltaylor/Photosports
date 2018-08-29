package photosports.sainthannaz.com.photosports;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;

import photosports.sainthannaz.com.photosports.tools.DatabaseHandler;
import photosports.sainthannaz.com.photosports.tools.PermissionUtils;

/**
 * Created by Gabriel on 29/12/2017.
 */

public class RegisterUsersFragment extends Fragment {
    FormEditText inputName, inputLastname, inputPhone, inputEmail, inputRunner;
    String name, lastname, email, phone, runner, origin;
    private ProgressDialog pDialog;
    DatabaseHandler db;
    Button register;
    private static final int MY_READ_WRITE_PERMISSION_REQUEST_CODE = 1;
    private static final int READ_WRITE_PERMISSION_REQUEST_CODE = 2;

    public RegisterUsersFragment() {
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
        requestReadWritePermission(MY_READ_WRITE_PERMISSION_REQUEST_CODE);
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

    public void requestReadWritePermission(int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Display a dialog with rationale.
            PermissionUtils.RationaleDialog
                    .newInstance(requestCode, false).show(
                    getFragmentManager(), "dialog");
        } else {
            // Location permission has not been granted yet, request it.
            PermissionUtils.requestPermission((AppCompatActivity) getContext(), requestCode,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_WRITE_PERMISSION_REQUEST_CODE) {
            // Enable the My Location button if the permission has been granted.
            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {

            }
        } else if (requestCode == READ_WRITE_PERMISSION_REQUEST_CODE) {
            // Enable the My Location layer if the permission has been granted.
            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

            } else {
               //
            }
        }
    }


}
