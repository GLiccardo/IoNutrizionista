package it.giuseppeliccardo.ionutrizionista;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class FragmentRisultati extends Fragment {

    private static final String TAG = "ioNutrizionista";

    // Esternalizzazione stringhe
    private static final String TAG_DATI_MANCANTI = "Inserire i seguenti dati:";

    // Conterrà l'elenco dei parametri che non sono stati digitati dall'utente
    private StringBuilder mStringaToast;

    // TODO: Aggiungere qui le variabili che conterranno l'esito del valore, cioè se è stato inserito dall'utente o meno
    private boolean mCheckAltezza;
    private boolean mCheckPeso;
    private boolean mCheckPlicheGirovita;
    private boolean mCheckPlicheSchiena;
    private boolean mCheckPlicheBraccio;
    private boolean mCheckCirconferenzaAddome;
    private boolean mCheckCirconferenzaFianchi;
    private boolean mCheckCirconferenzaCoscia;
    private boolean mCheckCirconferenzaPolso;
    private boolean mCheckCirconferenzaBraccio;


    public FragmentRisultati() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_risultati, container, false);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onResume()");

        // Verifico se devo ricalcolare i risultati o meno
        boolean controlloRicalcolo = ((CalcoloValoriEnergeticiActivity) getActivity()).calcolaRisultati;
        if (controlloRicalcolo) {
            // Controllo valori mancanti
            if (checkValoriInseriti()) {

            }

            // BMI
            //float bmi = calcolaBMI();
            //float bmiArrotondato = (float) Math.round(bmi * 100) / 100;

            int altezzaCm = ((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm;


            // TODO: calcolare tutti i seguenti valori
            // Peso Calcolato Ideale
            // Metabolismo Basale
            // Fabbisogno Energetico
            // Razione Calorica Giornaliera

            // Toast.makeText(getActivity(), "Risultato: " + altezzaCm, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), "Clicca su 'Calcola'", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkSingoloValoreInserito (int valoreInt) {
        boolean res = (valoreInt != -1) ? true : false;
        return res;
    }

    private boolean checkSingoloValoreInserito (float valoreFloat) {
        boolean res = (valoreFloat != -1) ? true : false;
        return res;
    }


    private boolean checkValoriInseriti() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in checkValoriInseriti()");

        // TODO: fare controlli sugli inserimenti, sui range di valori consentiti
        mStringaToast = new StringBuilder(TAG_DATI_MANCANTI);

        mCheckAltezza = checkSingoloValoreInserito(((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm);
        if (!mCheckAltezza) mStringaToast.append("\nAltezza");

        mCheckPeso = checkSingoloValoreInserito(((CalcoloValoriEnergeticiActivity) getActivity()).mPesoKg);
        if (!mCheckPeso) mStringaToast.append("\nPeso");

        mCheckPlicheGirovita = checkSingoloValoreInserito(((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheGirovita);
        if (!mCheckPlicheGirovita) mStringaToast.append("\nPliche Girovita");

        mCheckPlicheSchiena = checkSingoloValoreInserito(((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheSchiena);
        if (!mCheckPlicheSchiena) mStringaToast.append("\nPliche Schiena");

        mCheckPlicheBraccio = checkSingoloValoreInserito(((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheBraccio);
        if (!mCheckPlicheBraccio) mStringaToast.append("\nPliche Braccio");

        mCheckCirconferenzaAddome = checkSingoloValoreInserito(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaAddome);
        if (!mCheckCirconferenzaAddome) mStringaToast.append("\nCirconferenza Addome");

        mCheckCirconferenzaFianchi = checkSingoloValoreInserito(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaFianchi);
        if (!mCheckCirconferenzaFianchi) mStringaToast.append("\nCirconferenza Fianchi");

        mCheckCirconferenzaCoscia = checkSingoloValoreInserito(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaCoscia);
        if (!mCheckCirconferenzaCoscia) mStringaToast.append("\nCirconferenza Coscia");

        mCheckCirconferenzaPolso = checkSingoloValoreInserito(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaPolso);
        if (!mCheckCirconferenzaPolso) mStringaToast.append("\nCirconferenza Polso");

        mCheckCirconferenzaBraccio = checkSingoloValoreInserito(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaBraccio);
        if (!mCheckCirconferenzaBraccio) mStringaToast.append("\nCirconferenza Braccio");

        //Toast.makeText(getActivity(), "Altezza: " + mCheckAltezza + "\nPeso: " + mCheckPeso + "\nEsito: " + esitoPositivo + mStringaToast, Toast.LENGTH_SHORT).show();
        if (mStringaToast.toString().equals(TAG_DATI_MANCANTI)) {
            // Tutti i valori sono stati inseriti
            return true;
        } else {
            Toast.makeText(getActivity(), mStringaToast, Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    /*
    private float calcolaBMI() {
    Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaBMI()");

        float bmi = 0.0f;

        int altezzaCm = ((CalcoloValoriEnergeticiActivityv2) getActivity()).mAltezzaCm;
        float pesoKg = ((CalcoloValoriEnergeticiActivityv2) getActivity()).mPesoKg;

        if (mAltezzaEditText.getText().toString().equals("") && mPesoEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), TAG_INSERIRE_ALTEZZA_PESO , Toast.LENGTH_SHORT).show();
        } else if (mPesoEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), TAG_INSERIRE_PESO, Toast.LENGTH_SHORT).show();
        } else if (mAltezzaEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), TAG_INSERIRE_ALTEZZA, Toast.LENGTH_SHORT).show();
        } else {
            int altezzaCM = Integer.parseInt(mAltezzaEditText.getText().toString());
            float altezzaM = (float) altezzaCM / 100;
            float peso = Float.parseFloat(mPesoEditText.getText().toString());

            if (altezzaCM < 80 || altezzaCM > 220) {
                Toast.makeText(getActivity(), TAG_INSERIRE_ALTEZZA_CORRETTA, Toast.LENGTH_SHORT).show();
            } else if (peso < 30 || peso > 250) {
                Toast.makeText(getActivity(), TAG_INSERIRE_PESO_CORRETTO, Toast.LENGTH_SHORT).show();
            } else {
                return (float) ((1.3f * peso) / (Math.pow(altezzaM, 2.5f)));
            }

            //BigDecimal bmiNuovo = new BigDecimal(bmi);
            //float bmiArrotondato = (float) Math.round(bmi * 100) / 100;
            //Toast.makeText(getActivity(), "Altezza: " + altezzaM + "\nPeso: " + peso + "\nBMI Puro: " + bmi + "\nBMI arrotondato: " + bmiArrotondato, Toast.LENGTH_SHORT).show();
        }

        return bmi;
    }
    */
}
