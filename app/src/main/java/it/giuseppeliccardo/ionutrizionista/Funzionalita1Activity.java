package it.giuseppeliccardo.ionutrizionista;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
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
            // Aggiungo FragmentFunzionalita1 al componente FrameLayout1 dell'activity host
            getFragmentManager().beginTransaction()
                    .add(R.id.container1, new FragmentFunzionalita1())
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
     * Fragment per la gestione della "Funzionalità 1":
     * - Dati Anagrafici (Nome, Cognome, Sesso, Età)
     * - Misurazioni (Altezza, Peso, Pliche, Circonferenze)
     * - Barra Colorata
     * - Risultati (BMI, Peso Calc Ideale, Metab Basale, Fabb Energetico, Raz Calorica)
     */
    public static class FragmentFunzionalita1 extends Fragment {

        public FragmentFunzionalita1() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Aggiungo la UI al fragment
            View rootView = inflater.inflate(R.layout.fragment_funzionalita1, container, false);

            // Aggiungere il bordo ad una delle TextView della barra colorata
            TextView rettangolino = (TextView) rootView.findViewById(R.id.barra_colorata_25_30);
            GradientDrawable backgroundGradient = (GradientDrawable) rettangolino.getBackground();
            backgroundGradient.setStroke(5, getResources().getColor(R.color.nero_chiaro));

            TextView kgm2 = (TextView) rootView.findViewById(R.id.text_view_kgm2);
            //kgm2.setText(Html.fromHtml("kg/m<sup><small>" + 2 + "</small></sup>"));

            return rootView;
        }
    }

}