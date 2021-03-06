package photosports.sainthannaz.com.photosports;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import photosports.sainthannaz.com.photosports.tools.PermissionUtils;
import photosports.sainthannaz.com.photosports.tools.SessionManager;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    private long back_pressed;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FragmentManager FM;
    FragmentTransaction FT;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView= findViewById(R.id.shitstuff);
        View hView = navigationView.getHeaderView(0);

        FM= getSupportFragmentManager();
        FT= FM.beginTransaction();
        FT.replace(R.id.containerView, new RegisterFragment()).commit();
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();

                if (item.getItemId()== R.id.nav_item_home) {
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new RegisterFragment(), "myRegister").commit();
                }

                if (item.getItemId()== R.id.nav_item_runners) {
                    FragmentTransaction fragmentTransaction=FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new RunnersFragment(), "myRunners").commit();
                }

                if (item.getItemId()== R.id.nav_item_users) {
                    Intent intent = new Intent(MainActivity.this, RegisterUsersActivity.class);
                    startActivity(intent);
                }

                if (item.getItemId()== R.id.nav_item_about) {
                    FragmentTransaction fragmentTransaction= FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new AboutFragment(), "myAbout").commit();
                }

                if (item.getItemId()==R.id.nav_item_exit)
                {
                    session.setLogin(false);
                    // Launching the login activity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);

                    //FragmentTransaction fragmentTransaction1=FM.beginTransaction();
                    //fragmentTransaction1.replace(R.id.containerView,new TabFragment()).commit();
                }
                return false;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }


    @Override
    public void onBackPressed() {

    }

    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this,
                        ActivityAbout.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
