package photosports.sainthannaz.com.photosports;

/**
 * Created by Gabriel on 29/12/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import photosports.sainthannaz.com.photosports.models.Customers;
import photosports.sainthannaz.com.photosports.tools.DatabaseHandler;


public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.MyViewHolder>{

    private Context context;
    private List<Customers> customersList;
    private CustomersAdapterListener listener;
    DatabaseHandler db;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, lastname, email, phone, runner;
        public ImageButton editButton;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            lastname = view.findViewById(R.id.lastname);
            email = view.findViewById(R.id.email);
            phone = view.findViewById(R.id.phone);
            runner = view.findViewById(R.id.runner);
            editButton = view.findViewById(R.id.editButton);
            db = new DatabaseHandler(context);
        }
    }

    public CustomersAdapter(Context context, List<Customers> customersList) {
        this.context = context;
        this.listener = listener;
        this.customersList = customersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.runners_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(position%2 == 0){
            System.out.println("Gris");
            holder.itemView.setBackgroundColor(0xFFF5F5F5);
        } else {
            System.out.println("Blanco");
            holder.itemView.setBackgroundColor(0xFFFFFFFF);
        }
        final Customers customers = customersList.get(position);
        holder.name.setText(customers.getcustomer_name());
        holder.lastname.setText(customers.getcustomer_lastname());
        holder.email.setText(customers.getcustomer_email());
        holder.phone.setText(customers.getcustomer_phone());
        holder.runner.setText(customers.getcustomer_runner());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idCustomer = customers.getcustomer_id();
                String runner = customers.getcustomer_runner();
                System.out.println("ID: " + idCustomer);
                System.out.println("Runner: " + runner);
                showPopupMenu(holder.editButton, position, idCustomer);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position, int idCustomer) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_edit, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position, idCustomer));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        int position;
        int idCustomer;

        public MyMenuItemClickListener(int position, int idCustomer) {
            this.position = position;
            this.idCustomer = idCustomer;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    Toast.makeText(context, "Edit " + idCustomer, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context.getApplicationContext(), EditActivity.class);
                    intent.putExtra("idCustomer", idCustomer);
                    context.startActivity(intent);

                    return true;
                case R.id.action_delete:
                    Toast.makeText(context, "Delete" + idCustomer, Toast.LENGTH_SHORT).show();
                    db.DeleteSingleData(idCustomer);
                    customersList.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return customersList.size();
    }



    public interface CustomersAdapterListener {
        void onCustomersSelected(Customers customers);
    }

}
