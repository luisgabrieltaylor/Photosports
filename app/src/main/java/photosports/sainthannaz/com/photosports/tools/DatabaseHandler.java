package photosports.sainthannaz.com.photosports.tools;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import photosports.sainthannaz.com.photosports.models.Customers;

/**
 * Created by Gabriel on 29/12/2017.
 */

public class DatabaseHandler  extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_EVENTS = "events";

    // Contacts Table Columns names
    public static final String KEY_ID = "customer_id";
    public static final String KEY_NAME = "customer_name";
    public static final String KEY_LASTNAME = "customer_lastname";
    public static final String KEY_EMAIL = "customer_email";
    public static final String KEY_PHONE = "customer_phone";
    public static final String KEY_NUMBER = "customer_racernumber";
    public static final String KEY_ORIGIN = "customer_origin";

    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_EVENT_NAME = "event_name";
    public static final String KEY_EVENT_LOCATION = "event_location";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_META_TABLE = "CREATE TABLE " + TABLE_CUSTOMERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_LASTNAME + " TEXT," + KEY_EMAIL + " TEXT," + KEY_PHONE + " TEXT," + KEY_NUMBER + " TEXT," + KEY_ORIGIN + " INTEGER" + ")";

        String CREATE_SUBMETA_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_EVENT_ID + " INTEGER , " + KEY_EVENT_NAME + " TEXT," + KEY_EVENT_LOCATION + " TEXT" +")";

        db.execSQL(CREATE_META_TABLE);
        System.out.println("Base de datos creada...META");
        db.execSQL(CREATE_SUBMETA_TABLE);
        System.out.println("Base de datos creada...SUBMETA");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        // Create tables again
        onCreate(db);
    }

    /**
     *OPERATIONS
     */

    // Adding customer data
    public void addCustomerData(String customer_name, String customer_lastname, String customer_email, String customer_phone, String customer_racenumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID, customer_id);
        values.put(KEY_NAME, customer_name);
        values.put(KEY_LASTNAME, customer_lastname);
        values.put(KEY_EMAIL, customer_email);
        values.put(KEY_PHONE, customer_phone);
        values.put(KEY_NUMBER, customer_racenumber);
        //values.put(KEY_ORIGIN, customer_favorite);
        // Inserting Row
        db.insert(TABLE_CUSTOMERS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new contact
    public void addEventData(int event_id, String event_name, String event_location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_ID, event_id);
        values.put(KEY_EVENT_NAME, event_name);
        values.put(KEY_EVENT_LOCATION, event_location);

        // Inserting Row
        db.insert(TABLE_EVENTS, null, values);
        db.close(); // Closing database connection
    }


    public void DeleteData(){
        System.out.println(" -------------------- BORRAMOS CLIENTES -------------------------");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_CUSTOMERS);
        db.close(); // Closing database connection
    }

    public void DeleteSubData(){
        System.out.println(" -------------------- BORRAMOS EVENTOS -------------------------");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_EVENTS);
        db.close(); // Closing database connection
    }

    public void DeleteSingleData(int keyID){
        System.out.println(" -------------------- BORRAMOS INFORMACION ESPECIFICA -------------------------");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_CUSTOMERS + " WHERE " + KEY_ID + " = " + keyID);
        db.close(); // Closing database connection

    }

    public void DeleteSingleEvent(int keyEventID){
        System.out.println(" -------------------- BORRAMOS INFORMACION ESPECIFICA DE EVENTOS -------------------------");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_EVENTS + " WHERE " + KEY_EVENT_ID + " = " + keyEventID);
        db.close(); // Closing database connection

    }
    // Getting All Customers
    public List<Customers> getAllContacts() {
        List<Customers> customersList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMERS + " ORDER BY " + KEY_LASTNAME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customers customers = new Customers();
                customers.setcustomer_id(Integer.parseInt(cursor.getString(0)));
                customers.setcustomer_name(cursor.getString(1));
                customers.setcustomer_lastname(cursor.getString(2));
                customers.setcustomer_email(cursor.getString(3));
                customers.setcustomer_phone(cursor.getString(4));
                customers.setcustomer_runner(cursor.getString(5));
                //customers.setcustomer_origin(cursor.getString(6));
                // Adding contact to list
                customersList.add(customers);
            } while (cursor.moveToNext());
        }

        // return contact list
        return customersList;
    }

    // Getting single customer
    public Customers getInfoCustomer(int idCustomer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMERS, new String[] { KEY_ID,
                        KEY_NAME, KEY_LASTNAME, KEY_EMAIL, KEY_PHONE, KEY_NUMBER, KEY_ORIGIN}, KEY_ID + "=?",
                new String[] { String.valueOf(idCustomer) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Customers customers = new Customers(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6));
        return customers;
    }

    public void updateCustomer(int idCustomer, String name, String lastname, String email, String phone, String runner){
        //String whereFavorite = KEY_PHONE_ID + " = "+ phone_card_id;
        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_LASTNAME, lastname);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PHONE, phone);
        values.put(KEY_NUMBER, runner);
        //db.update(TABLE_METAS, values, whereFavorite, null);
        db.update(TABLE_CUSTOMERS, values, "customer_id = ?",new String[] { String.valueOf(idCustomer) });
    }

    public void openForRead() {
        SQLiteDatabase db = this.getWritableDatabase();

    }

    public void closeForRead() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }
    /*
    // Getting single number
    public Customers getInfoMeta(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_METAS, new String[] { KEY_ID,
                        KEY_name, KEY_lastname, KEY_email, KEY_phone, KEY_racenumber, KEY_FAVORITE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Customers numbers = new Customers(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)) );
        return numbers;
    } */

    /*
    // Getting All Emergency Customers
    public List<Customers> getAllContacts() {
        List<Customers> numbersList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_METAS + " ORDER BY " + KEY_FAVORITE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customers numbers = new Customers();
                numbers.setcustomer_id(Integer.parseInt(cursor.getString(0)));
                numbers.setcustomer_name(cursor.getString(1));
                numbers.setcustomer_lastname(cursor.getString(2));
                numbers.setcustomer_email(cursor.getString(3));
                numbers.setcustomer_phone(cursor.getString(4));
                numbers.setcustomer_racenumber(cursor.getString(5));
                numbers.setcustomer_favorite(Integer.parseInt(cursor.getString(6)));
                // Adding contact to list
                numbersList.add(numbers);
            } while (cursor.moveToNext());
        }

        // return contact list
        return numbersList;
    }

    // Getting All Emergency Customers
    public List<SubCustomers> getAllSpecificSubData(int id) {
        List<SubCustomers> subdataList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUBMETAS + " WHERE " + KEY_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SubCustomers subCustomers = new SubCustomers();
                subCustomers.setcustomer_phone_id(Integer.parseInt(cursor.getString(0)));
                subCustomers.setcustomer_id(Integer.parseInt(cursor.getString(1)));
                subCustomers.setcustomer_address(cursor.getString(2));
                subCustomers.setcustomer_phone(cursor.getString(3));
                subCustomers.setcustomer_map(cursor.getString(4));
                subCustomers.setcustomer_racenumber(cursor.getString(5));
                // Adding contact to list
                subdataList.add(subCustomers);
            } while (cursor.moveToNext());
        }
        // return contact list
        return subdataList;
    }
   */
}
