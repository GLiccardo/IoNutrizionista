package it.giuseppeliccardo.ionutrizionista;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Funzionalita1Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funzionalita1);
        if (savedInstanceState == null) {
            // Aggiungo FragmentDatiAnagrfici al componente FrameLayout1 dell'activity host
            getFragmentManager().beginTransaction()
                    .add(R.id.container1, new FragmentDatiAnagrafici())
                    .commit();
            // Aggiungo FragmentBarraColorata al componente FrameLayout2 dell'activity host
            getFragmentManager().beginTransaction()
                    .add(R.id.container2, new FragmentBarraColorata())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_funzionalita1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fragment per la gestione dei dati anagrafici della "Funzionalità 1":
     * - Nome
     * - Cognome
     * - Sesso
     * - Età
     */
    public static class FragmentDatiAnagrafici extends Fragment {

        public FragmentDatiAnagrafici() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Aggiungo la UI al fragment
            View rootView = inflater.inflate(R.layout.fragment1_funzionalita1, container, false);
            return rootView;
        }
    }

    /**
     * Fragment per la gestione dei dati anagrafici della "Funzionalità 1":
     * - Barra colorata
     */
    public static class FragmentBarraColorata extends Fragment {

        public FragmentBarraColorata() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Aggiungo la UI al fragment
            View rootView = inflater.inflate(R.layout.fragment2_funzionalita1, container, false);

            // Aggiungere il bordo ad una delle TextView della barra colorata
            TextView rettangolino = (TextView) rootView.findViewById(R.id.prova);
            GradientDrawable backgroundGradient = (GradientDrawable) rettangolino.getBackground();
            //backgroundGradient.setStroke(5, getResources().getColor(R.color.nero_chiaro));

            return rootView;
        }


    }

}