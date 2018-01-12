package photosports.sainthannaz.com.photosports;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.opencsv.CSVWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import photosports.sainthannaz.com.photosports.models.Customers;
import photosports.sainthannaz.com.photosports.tools.DatabaseHandler;
import photosports.sainthannaz.com.photosports.tools.DividerItemDecoration;
import photosports.sainthannaz.com.photosports.tools.Hosts;
import photosports.sainthannaz.com.photosports.tools.VolleySingleton;

/**
 * Created by Gabriel on 29/12/2017.
 */

public class RunnersFragment extends Fragment{
    private static final String TAG = RunnersFragment.class.getSimpleName();
    DatabaseHandler db;
    private List<Customers> customersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomersAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    public Button deleteDB, uploadDB, exportDB;
    private ProgressDialog pDialog;
    private static final String SAMPLE_DB_NAME = "contactsManager";
    private static final String SAMPLE_TABLE_NAME = "customers";

    public RunnersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_runners, null);
        db = new DatabaseHandler(getActivity());
        deleteDB = view.findViewById(R.id.btnDelete);
        uploadDB = view.findViewById(R.id.btnUpload);
        exportDB = view.findViewById(R.id.btnExport);
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        List<Customers> customers = db.getAllContacts();
        mAdapter = new CustomersAdapter(getContext(), customers);
        recyclerView.setAdapter(mAdapter);


        refreshLayout = view.findViewById(R.id.swipeRefresh);
        refreshLayout.setColorSchemeResources(
                R.color.s1,
                R.color.s2,
                R.color.s3,
                R.color.s4
        );

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshData();
                        refreshLayout.setRefreshing(false);
                    }
                }
        );

        insertFakeData();

        deleteDB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                deleteDialog();
                refreshData();
            }
        });

        uploadDB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog builder = new AlertDialog.Builder(getActivity())
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_info)
                        .setTitle("Subir DB al servidor")
                        .setMessage("¡Se subiran los registros al servidor y posteriormente se eliminaran!")
                        .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                uploadData();
                                refreshData();
                            }
                        }).show();
                refreshData();
            }
        });

        exportDB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog builder = new AlertDialog.Builder(getActivity())
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_info)
                        .setTitle("Exportar DB")
                        .setMessage("¡Se exportara la DB a CSV!")
                        .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                pDialog.setMessage("Generando CSV...");
                                showDialog();
                                createCSV();
                                hideDialog();
                                Toast.makeText(getContext(), getString(R.string.success_csv),
                                        Toast.LENGTH_LONG).show();
                                deleteDialog();
                            }
                        }).show();

                refreshData();
            }
        });

        return view;
    }

    public void refreshData(){
        List<Customers> customers = db.getAllContacts();
        mAdapter = new CustomersAdapter(getContext(), customers);
        recyclerView.setAdapter(mAdapter);
    }

    public void insertFakeData(){
        db.addCustomerData("test","test","test@test.com","9211111111","test");
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    public void createCSV(){
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date);
        List<Customers> list = new ArrayList<Customers>();
        list = db.getAllContacts();
        if(list.size() <= 0) {
            Toast.makeText(getContext(), getString(R.string.nodata_error),
                    Toast.LENGTH_LONG).show();
        } else {
            //List<Customers> customers = db.getAllContacts();
            System.out.println(list);
            File exportDir = new File(Environment.getExternalStorageDirectory(), ""); //

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "Runners_"+ timeStamp +".csv");
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));   //Writing data in csv file
                String columnsData[] = {"ID", "Nombre", "Apellido", "Email", "Teléfono", "N Corredor"};
                csvWrite.writeNext(columnsData);
                System.out.println("Tamaño de la lista:" + list.size());
                for (int index = 0; index < list.size(); index++) {
                    int id = list.get(index).getcustomer_id();
                    String name = list.get(index).getcustomer_name();
                    System.out.println(name);
                    String lastname = list.get(index).getcustomer_lastname();
                    String email = list.get(index).getcustomer_email();
                    String phone = list.get(index).getcustomer_phone();
                    String runner = list.get(index).getcustomer_runner();
                    String customerData[] = {String.valueOf(id), name, lastname, email, phone, runner};
                    csvWrite.writeNext(customerData);
                }
                csvWrite.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public void deleteDialog(){
        AlertDialog builder = new AlertDialog.Builder(getActivity())
            .setCancelable(true)
            .setIcon(R.drawable.ic_info)
            .setTitle("Eliminar BD local")
            .setMessage("¿Desea eliminar la BD local? todos los datos se perderan")
            .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    pDialog.setMessage("Eliminando BD...");
                    showDialog();
                    db.DeleteData();
                    hideDialog();
                    refreshData();
                    Toast.makeText(getContext(), getString(R.string.success_erase),
                            Toast.LENGTH_LONG).show();
                }
            }).show();
    }

    public void uploadData(){
        List<Customers> list = new ArrayList<Customers>();
        list = db.getAllContacts();
        if(list.size() <= 0) {
            Toast.makeText(getContext(), getString(R.string.nodata_error),
                    Toast.LENGTH_LONG).show();
        } else {
            pDialog.setMessage("Subiendo registros...");
            showDialog();
            System.out.println("Se subiran los siguientes registros ----->");
            for (int index = 0; index < list.size(); index++) {

                final int id = list.get(index).getcustomer_id();
                System.out.println(id);
                String name = list.get(index).getcustomer_name();
                System.out.println(name);
                String lastname = list.get(index).getcustomer_lastname();
                String email = list.get(index).getcustomer_email();
                String phone = list.get(index).getcustomer_phone();
                String runner = list.get(index).getcustomer_runner();

                HashMap<String, String> map = new HashMap<>();// Mapeo previo
                map.put("name", name);
                map.put("lastname", lastname);
                map.put("email", email);
                map.put("phone", phone);
                map.put("runner", runner);
                // Crear nuevo objeto Json basado en el mapa
                JSONObject jobject = new JSONObject(map);
                System.out.println(jobject.toString());
                // Depurando objeto Json...
                Log.d(TAG, jobject.toString());
                VolleySingleton.getInstance(getActivity()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Hosts.INSERT,
                                jobject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println(response);
                                        // Procesar la respuesta del servidor
                                        System.out.println("Procesaremos la respuesta! de" + id);
                                        procesarRespuesta(response, id);
                                        refreshData();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("Ocurrio un error! " + id);
                                        Log.d(TAG, "Error Volley: " + error.getMessage());
                                        refreshData();
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
            hideDialog();
            System.out.println("Solicitando limpiar vista");
            Toast.makeText(
                    getActivity(),
                    "¡Subida de datos concluida!",
                    Toast.LENGTH_LONG).show();
            refreshData();
        }
        refreshData();
    }

    /**
     * Procesa la respuesta obtenida desde el sevidor
     *
     * @param response Objeto Json
     */
    private void procesarRespuesta(JSONObject response, Integer id) {
        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");
            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de éxito
                    getActivity().setResult(Activity.RESULT_OK);
                    db.DeleteSingleData(id);

                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    // Terminar actividad
                   //getActivity().finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
