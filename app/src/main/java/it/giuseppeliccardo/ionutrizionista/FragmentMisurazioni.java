package it.giuseppeliccardo.ionutrizionista;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


public class FragmentMisurazioni extends Fragment {

    private static final String TAG = "ioNutrizionista";

    // TODO: Dichiarare qui tutti gli elementi grafici che saranno individuati nel metodo findViewsById
    private EditText mAltezzaEditText;
    private EditText mPesoEditText;
    private EditText mPlicheGirovitaEditText;
    private EditText mPlicheSchienaEditText;
    private EditText mPlicheBraccioEditText;
    private EditText mCirconferenzaAddomeEditText;
    private EditText mCirconferenzaFianchiEditText;
    private EditText mCirconferenzaCosciaEditText;
    private EditText mCirconferenzaPolsoEditText;
    private EditText mCirconferenzaBraccioEditText;

    // Esternalizzazione stringhe
    private static final String TAG_INSERIRE_ALTEZZA_PESO           = "Inserire Altezza e Peso";
    private static final String TAG_INSERIRE_ALTEZZA                = "Inserire Altezza";
    private static final String TAG_INSERIRE_ALTEZZA_CORRETTA       = "Altezza non valida";
    private static final String TAG_INSERIRE_PESO                   = "Inserire Peso";
    private static final String TAG_INSERIRE_PESO_CORRETTO          = "Peso non valido";


    public FragmentMisurazioni() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_misurazioni, container, false);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Ottengo un riferimento alle Views della UI
        findViewsById();

        /*
        int altezzaCM = Integer.parseInt(mAltezzaEditText.getText().toString());
        float altezzaM = (float) altezzaCM / 100;
        float peso = Float.parseFloat(mPesoEditText.getText().toString());
        */


    }




    @Override
    public void onStop() {
        super.onStop();

        // ((CalcoloValoriEnergeticiActivityv2) getActivity()).provaValoriEnergetici2 = 21;

        //getDatiAnagrafici();
        getPesoForma();
        //getPliche();
        //getCirconferenza();

    }


    /*
      Metodo che consente di ottenere i reference per tutte le View che verranno utilizzate
     */
    private void findViewsById() {

        try {
            mAltezzaEditText = (EditText) getView().findViewById(R.id.edit_text_altezza);
            mPesoEditText = (EditText) getView().findViewById(R.id.edit_text_peso);
            mPlicheGirovitaEditText = (EditText) getView().findViewById(R.id.edit_text_pliche_girovita);
            mPlicheSchienaEditText = (EditText) getView().findViewById(R.id.edit_text_pliche_schiena);
            mPlicheBraccioEditText = (EditText) getView().findViewById(R.id.edit_text_pliche_braccio);
            mCirconferenzaAddomeEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_addome);
            mCirconferenzaFianchiEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_fianchi);
            mCirconferenzaCosciaEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_coscia);
            mCirconferenzaPolsoEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_polso);
            mCirconferenzaBraccioEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_braccio);

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

    }


    /*
      Metodo che:
        1. Verifica se i dati anagrafici sono stati digitati dall'utente nelle varie View
        2. Assegna i valori delle View alle variabili globali dichiarate nell'activity host
     */
    private void getDatiAnagrafici() {

    }


    /*
      Metodo che:
        1. Verifica se altezza e peso sono stati digitati dall'utente nelle varie View
        2. Assegna i valori delle View alle variabili globali dichiarate nell'activity host
     */
    private void getPesoForma() {

        // Assegno -1 se l'utente non ha ancora digitato il valore nella View di altezza o peso
        ((CalcoloValoriEnergeticiActivityv2) getActivity()).mAltezzaCm = !(mAltezzaEditText.getText().toString().equals("")) ? Integer.parseInt(mAltezzaEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivityv2) getActivity()).mAltezzaM = (float) ((CalcoloValoriEnergeticiActivityv2) getActivity()).mAltezzaCm / 100;
        ((CalcoloValoriEnergeticiActivityv2) getActivity()).mPesoKg = !(mPesoEditText.getText().toString().equals("")) ? Float.parseFloat(mPesoEditText.getText().toString()) : -1;

        /*
        if (mAltezzaEditText.getText().toString().equals("") && mPesoEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), TAG_INSERIRE_ALTEZZA_PESO, Toast.LENGTH_SHORT).show();
        } else if (mPesoEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), TAG_INSERIRE_PESO, Toast.LENGTH_SHORT).show();
        } else if (mAltezzaEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), TAG_INSERIRE_ALTEZZA, Toast.LENGTH_SHORT).show();
        } else {

            ((CalcoloValoriEnergeticiActivityv2) getActivity()).mAltezzaCm = Integer.parseInt(mAltezzaEditText.getText().toString());
            ((CalcoloValoriEnergeticiActivityv2) getActivity()).mAltezzaM = (float) ((CalcoloValoriEnergeticiActivityv2) getActivity()).mAltezzaCm / 100;
            ((CalcoloValoriEnergeticiActivityv2) getActivity()).mPesoKg = Float.parseFloat(mPesoEditText.getText().toString());

        }
        */
    }


    /*
      Metodo che:
        1. Verifica se i tre valori delle pliche sono stati digitati dall'utente nelle varie View
        2. Assegna i valori delle View alle variabili globali dichiarate nell'activity host
     */
    private void getPliche() {

    }


    /*
      Metodo che:
        1. Verifica se i cinque valori delle circonferenze sono stati digitati dall'utente nelle varie View
        2. Assegna i valori delle View alle variabili globali dichiarate nell'activity host
     */
    private void getCirconferenza() {

    }
}
