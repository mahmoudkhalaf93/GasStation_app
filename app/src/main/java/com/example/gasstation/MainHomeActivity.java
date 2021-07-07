package com.example.gasstation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.sql.Connection;
import java.util.ArrayList;

public class MainHomeActivity extends AppCompatActivity {
    ArrayList<CartShoppingModel> CartSho;
    DBLIGHT dblight=new DBLIGHT(this);
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Shopping Cart", Snackbar.LENGTH_LONG)
//                        .setAction("Action y", null).show();
                CartSho=new ArrayList<>(dblight.GetData());
                if(CartSho.isEmpty())
                {
                    Toast.makeText(MainHomeActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }
                else
                    startActivity(new Intent(MainHomeActivity.this, ShopingCartActivity.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_Requests)
                .setDrawerLayout(drawer)
                .build();

        TextView username, usermail;
        ImageView Userimage;
        View headerView = navigationView.getHeaderView(0);
        username =  headerView.findViewById(R.id.user_name);
        usermail = headerView.findViewById(R.id.user_email);
        Userimage =  headerView.findViewById(R.id.user_image);

       //  String userImageLink="https://i.pinimg.com/originals/51/f6/fb/51f6fb256629fc755b8870c801092942.png",usernamestring="user name",useremailstring="GasStation@gmail.com";
      //  try {
        // Database db = new Database();
      //   Connection con = db.connect(this);
//         if (!(con == null)) {
//             ResultSet   rs = db.GetData("SELECT * FROM Customer WHERE CustomerNo='" + StaticsClass.IDLogin + "';");
//             if (rs.next()) {
//                  userImageLink=rs.getString("Image");
//                 usernamestring=rs.getString("Name");
//                 useremailstring=rs.getString("Email");
//             }
//         }
//     } catch (SQLException throwables) {
//         throwables.printStackTrace();
//     }

        username.setText(StaticsClass.nameLogin);
        usermail.setText(StaticsClass.emailLogin);
        Picasso.get().load(StaticsClass.imgProfile).error(R.drawable.logfitt).
                resize(150,150).into(Userimage);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_home, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        Database db = new Database();
        Connection con = db.connect(this);
        if(id==R.id.action_Logout) {

            if (!(con == null))
            {  getSharedPreferences("gasStation", MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
                 }else {}
        }   if(id==R.id.action_delete)
        {
            if (!(con == null))
            { AlertDialog.Builder m = new AlertDialog.Builder(this, R.style.MyDialogThemeDark)
                    .setTitle("Delete account...")
                    .setMessage("are you want to delete your account?")
                    .setIcon(R.drawable.logfitt)
                    .setPositiveButton("No", null)
                    .setNegativeButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getSharedPreferences("gasStation",MODE_PRIVATE).edit().clear().apply();
                            Database db=new Database();
                            db.connect(MainHomeActivity.this);
                            db.Rundml("delete from Customer where CustomerNo='"+StaticsClass.IDLogin+"'");
                            startActivity(new Intent(MainHomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                m.create().show();  }else {}


        }
        if(id==R.id.action_edit_profile)
        {
            if (!(con == null))
            { startActivity(new Intent(this, MyProfileActivity.class)); }else {}


        }
        if(id==R.id.action_Gas_Stations)
        {
            if (!(con == null))
            {
                startActivity(new Intent(this, MapGasStationActivity.class));  }else {}

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}