package it.giuseppeliccardo.ionutrizionista;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class FragmentBottoni extends Fragment {

    private static final String TAG = "ioNutrizionista";

    private final FragmentRisultati mFragmentRisultati = new FragmentRisultati();

    private Button mButtonAltro1; // ancora inutilizzato
    private Button mButtonAltro2; // ancora inutilizzato
    private Button mButtonCalcola;

    public FragmentBottoni() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_bottoni, container, false);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onResume()");

        // TODO: rinominare i button Altro1 e Altro2 alle operazioni che compiono
        // Gestione del click sui button
        try {
            mButtonAltro1 = (Button) getView().findViewById(R.id.button_altro1);
            mButtonAltro1.setOnClickListener(myListener);
            mButtonAltro2 = (Button) getView().findViewById(R.id.button_altro2);
            mButtonAltro2.setOnClickListener(myListener);
            mButtonCalcola = (Button) getView().findViewById((R.id.button_calcola));
            mButtonCalcola.setOnClickListener(myListener);
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }

    // Events Handler - Listener unico
    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == mButtonAltro1) clickButtonAltro1();
            if (view == mButtonAltro2) clickButtonAltro2();
            if (view == mButtonCalcola) clickButtonCalcola();
        }
    };

    void clickButtonAltro1() {
        Toast.makeText(getActivity(), "Altro1", Toast.LENGTH_SHORT).show();
    }

    void clickButtonAltro2() {
        Toast.makeText(getActivity(), "Altro2", Toast.LENGTH_SHORT).show();
    }

    void clickButtonCalcola() {

        // I risultati vengono ricalcolati all'apertura del fragment "Risultati"
        ((CalcoloValoriEnergeticiActivity) getActivity()).calcolaRisultati = true;

        // Show fragment "Risultati"
        // Se il fragment è già visibile, lo rimuovo e lo riaggiungo in modo che possa ricalcolare i risultati
        // Se il fragment non è visibile perchè ci troviamo in un altro fragment, lo sostituisco a quello corrente
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mFragmentRisultati.isResumed()) {
            ft.remove(mFragmentRisultati);
            ft.add(R.id.container_destro, mFragmentRisultati);
        } else {
            ft.replace(R.id.container_destro, mFragmentRisultati);
        }
        ft.addToBackStack(null);
        ft.commit();

        /*
        if (mFragmentDatiAnagrafici.isAdded()) {
            Toast.makeText(getActivity(), "Dati Anagrafici Visibile", Toast.LENGTH_SHORT).show();
        }
        */
    }

}
