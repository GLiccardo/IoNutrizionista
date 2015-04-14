package it.giuseppeliccardo.ionutrizionista;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class FragmentListaSezioni extends Fragment {

    private static final String TAG = "ioNutrizionista";

    private final FragmentDatiAnagrafici mFragmentDatiAnagrafici = new FragmentDatiAnagrafici();
    private final FragmentMisurazioni mFragmentMisurazioni = new FragmentMisurazioni();
    private final FragmentRisultati mFragmentRisultati = new FragmentRisultati();

    LinearLayout mListaSezioneDatiAnagrafici, mListaSezioneMisurazioni, mListaSezioneRisultati;

    public FragmentListaSezioni() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onCreateView()");
        // Inflate the layout for this fragment

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_destro, mFragmentDatiAnagrafici);
        fragmentTransaction.commit();

        return inflater.inflate(R.layout.fragment_lista_sezioni, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            // Gestione del click sugli elementi del LinearLayout
            mListaSezioneDatiAnagrafici = (LinearLayout) getView().findViewById(R.id.lista_sezione_dati_anagrafici);
            mListaSezioneDatiAnagrafici.setOnClickListener(myListener);
            mListaSezioneMisurazioni= (LinearLayout) getView().findViewById(R.id.lista_sezione_misurazioni);
            mListaSezioneMisurazioni.setOnClickListener(myListener);
            mListaSezioneRisultati = (LinearLayout) getView().findViewById(R.id.lista_sezione_risultati);
            mListaSezioneRisultati.setOnClickListener(myListener);
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

    }


    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == mListaSezioneDatiAnagrafici) clickLinearLayout1();
            if (view == mListaSezioneMisurazioni) clickLinearLayout2();
            if (view == mListaSezioneRisultati) clickLinearLayout3();
        }
    };

    void clickLinearLayout1() {
        //Toast.makeText(getActivity(), "Icona 1", Toast.LENGTH_SHORT).show();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_destro, mFragmentDatiAnagrafici);
        ft.addToBackStack(null);
        ft.commit();
    }

    void clickLinearLayout2() {
        // I risultati non vangono ricalcolati all'apertura del fragment "Risultati"
        ((CalcoloValoriEnergeticiActivity) getActivity()).calcolaRisultati = false;

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_destro, mFragmentMisurazioni);
        ft.addToBackStack(null);
        ft.commit();
    }

    void clickLinearLayout3() {
        //Toast.makeText(getActivity(), "Icona 3", Toast.LENGTH_SHORT).show();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_destro, mFragmentRisultati);
        ft.addToBackStack(null);
        ft.commit();

        /*
        if (mFragmentDatiAnagrafici.isAdded()) {
            Toast.makeText(getActivity(), "Dati Anagrafici Visibile", Toast.LENGTH_SHORT).show();
        }
        */
    }

}
