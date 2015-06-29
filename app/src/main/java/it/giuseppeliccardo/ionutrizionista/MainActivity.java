package it.giuseppeliccardo.ionutrizionista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    LinearLayout mMenuValoriEnergetici, mMenu2, mMenu3, mMenuContatti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Nascondo Status Bar e Action Bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        */

        // Gestione del click sugli elementi del LinearLayout
        mMenuValoriEnergetici = (LinearLayout) findViewById(R.id.nord_ovest);
        mMenuValoriEnergetici.setOnClickListener(myListener);
        mMenu2 = (LinearLayout) findViewById(R.id.nord_est);
        mMenu2.setOnClickListener(myListener);
        mMenu3 = (LinearLayout) findViewById(R.id.sud_ovest);
        mMenu3.setOnClickListener(myListener);
        mMenuContatti = (LinearLayout) findViewById(R.id.sud_est);
        mMenuContatti.setOnClickListener(myListener);

    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == mMenuValoriEnergetici) clickLinearLayout1();
            if (view == mMenu2) clickLinearLayout2();
            if (view == mMenu3) clickLinearLayout3();
            if (view == mMenuContatti) clickLinearLayout4();
        }
    };

    void clickLinearLayout1() {
        startActivity(new Intent(this, CalcoloValoriEnergeticiActivity.class));
        Toast.makeText(getApplicationContext(), "Clic Valori Energetici", Toast.LENGTH_SHORT).show();
    }

    void clickLinearLayout2() {
        startActivity(new Intent(this, CalcoloValoriEnergeticiActivityOLD.class));
        Toast.makeText(getApplicationContext(), "Menu 2 selezionato", Toast.LENGTH_SHORT).show();
    }

    void clickLinearLayout3() {
        Toast.makeText(getApplicationContext(), "Menu 3 selezionato", Toast.LENGTH_SHORT).show();
    }

    void clickLinearLayout4() {
        Toast.makeText(getApplicationContext(), "Clic Contatti", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
