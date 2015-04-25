package it.giuseppeliccardo.ionutrizionista;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.GradientDrawable;
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

        setBackgroundAndStrokeIcone();

    }


    /*
      Metodo nel quale vengono settati colore di sfondo e bordi per ciascuna delle icone della lista
     */
    private void setBackgroundAndStrokeIcone() {

        // Dati Anagrafici - Light Blue
        GradientDrawable borderDatiAnagrafici = new GradientDrawable();
        borderDatiAnagrafici.setColor(getResources().getColor(R.color.light_blue50));
        borderDatiAnagrafici.setStroke(1, getResources().getColor(R.color.light_blue500));
        mListaSezioneDatiAnagrafici.setBackground(borderDatiAnagrafici);

        // Misurazioni - Green
        GradientDrawable borderMisurazioni = new GradientDrawable();
        borderMisurazioni.setColor(getResources().getColor(R.color.green50));
        borderMisurazioni.setStroke(1, getResources().getColor(R.color.greenA400));
        mListaSezioneMisurazioni.setBackground(borderMisurazioni);

        // Risultati - Red
        GradientDrawable borderRisultati = new GradientDrawable();
        borderRisultati.setColor(getResources().getColor(R.color.red50));
        borderRisultati.setStroke(1, getResources().getColor(R.color.red500));
        mListaSezioneRisultati.setBackground(borderRisultati);

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
        //Toast.makeText(getActivity(), "Icona 2", Toast.LENGTH_SHORT).show();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_destro, mFragmentMisurazioni);
        ft.addToBackStack(null);
        ft.commit();
    }

    void clickLinearLayout3() {
        //Toast.makeText(getActivity(), "Icona 3", Toast.LENGTH_SHORT).show();

        // I risultati non vengono ricalcolati all'apertura del fragment "Risultati"
        ((CalcoloValoriEnergeticiActivity) getActivity()).flagClicButtonCalcola = false;

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
