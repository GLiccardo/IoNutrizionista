package it.giuseppeliccardo.ionutrizionista;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


public class FragmentRisultati extends Fragment {

    private static final String TAG = "ioNutrizionista";

    // Esternalizzazione stringhe
    private static final String TAG_DATI_MANCANTI           = "Inserire i seguenti dati:";

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
        boolean controlloRicalcolo = ((CalcoloValoriEnergeticiActivityv2) getActivity()).calcolaRisultati;
        if (controlloRicalcolo) {
            // Controllo valori mancanti
            if (controlloTuttiValori()) {

            }

            // BMI
            //float bmi = calcolaBMI();
            //float bmiArrotondato = (float) Math.round(bmi * 100) / 100;

            int altezzaCm = ((CalcoloValoriEnergeticiActivityv2) getActivity()).mAltezzaCm;


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


    private boolean controlloSingoloValore (int intero) {
        boolean res = (intero != -1) ? true : false;
        return res;
    }

    private boolean controlloSingoloValore (float decimale) {
        boolean res = (decimale != -1) ? true : false;
        return res;
    }


    private boolean controlloTuttiValori() {

        // TODO: fare controlli sugli inserimenti, sui range di valori consentiti
        mStringaToast = new StringBuilder(TAG_DATI_MANCANTI);

        mCheckAltezza = controlloSingoloValore(((CalcoloValoriEnergeticiActivityv2) getActivity()).mAltezzaCm);
        if (!mCheckAltezza) mStringaToast.append("\nAltezza");

        mCheckPeso = controlloSingoloValore(((CalcoloValoriEnergeticiActivityv2) getActivity()).mPesoKg);
        if (!mCheckPeso) mStringaToast.append("\nPeso");

        /*
        mCheckPlicheGirovita = controlloSingoloValore(((CalcoloValoriEnergeticiActivityv2) getActivity()).mPlicheGirovita);
        esitoPositivo = (mCheckPlicheGirovita && esitoPositivo) ? true : false;
        mCheckPlicheSchiena = controlloSingoloValore(((CalcoloValoriEnergeticiActivityv2) getActivity()).mPlicheSchiena);
        esitoPositivo = (mCheckPlicheSchiena && esitoPositivo) ? true : false;
        mCheckPlicheBraccio = controlloSingoloValore(((CalcoloValoriEnergeticiActivityv2) getActivity()).mPlicheBraccio);
        esitoPositivo = (mCheckPlicheBraccio && esitoPositivo) ? true : false;
        mCheckCirconferenzaAddome = controlloSingoloValore(((CalcoloValoriEnergeticiActivityv2) getActivity()).mCirconferenzaAddome);
        esitoPositivo = (mCheckCirconferenzaAddome && esitoPositivo) ? true : false;
        mCheckCirconferenzaFianchi = controlloSingoloValore(((CalcoloValoriEnergeticiActivityv2) getActivity()).mCirconferenzaFianchi);
        esitoPositivo = (mCheckCirconferenzaFianchi && esitoPositivo) ? true : false;
        mCheckCirconferenzaCoscia = controlloSingoloValore(((CalcoloValoriEnergeticiActivityv2) getActivity()).mCirconferenzaCoscia);
        esitoPositivo = (mCheckCirconferenzaCoscia && esitoPositivo) ? true : false;
        mCheckCirconferenzaPolso = controlloSingoloValore(((CalcoloValoriEnergeticiActivityv2) getActivity()).mCirconferenzaPolso);
        esitoPositivo = (mCheckCirconferenzaPolso && esitoPositivo) ? true : false;
        mCheckCirconferenzaBraccio = controlloSingoloValore(((CalcoloValoriEnergeticiActivityv2) getActivity()).mCirconferenzaBraccio);
        esitoPositivo = (mCheckCirconferenzaBraccio && esitoPositivo) ? true : false;
        */

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
