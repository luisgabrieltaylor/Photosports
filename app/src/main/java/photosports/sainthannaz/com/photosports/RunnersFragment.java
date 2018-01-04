package photosports.sainthannaz.com.photosports;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
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

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import photosports.sainthannaz.com.photosports.models.Customers;
import photosports.sainthannaz.com.photosports.tools.DatabaseHandler;
import photosports.sainthannaz.com.photosports.tools.DividerItemDecoration;

/**
 * Created by Gabriel on 29/12/2017.
 */

public class RunnersFragment extends Fragment{
    DatabaseHandler db;
    private List<Customers> customersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomersAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    public Button deleteDB, uploadDB, exportDB;
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
                AlertDialog builder = new AlertDialog.Builder(getActivity())
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_info)
                        .setTitle("Eliminar BD SQLite")
                        .setMessage("¡Se eliminaran todos los registros guardados!")
                        .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                db.DeleteData();
                                refreshData();
                            }

                        }).show();

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
                                createCSV();

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


}
