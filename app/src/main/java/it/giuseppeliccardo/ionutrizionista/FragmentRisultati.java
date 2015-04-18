package it.giuseppeliccardo.ionutrizionista;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentRisultati extends Fragment {

    private static final String TAG = "ioNutrizionista";

    // TODO: Dichiarare qui tutti gli elementi grafici che saranno individuati nel metodo findViewsById
    private TextView mBordoCellaBarraColorata;
    private TextView mBmiTextView;
    private TextView mPesoCalcolatoIdealeTextView;
    private TextView mMetabolismoBasaleTextView;
    private TextView mFabbisognoEnergeticoTextView;
    private TextView mRazioneCaloricaLeggeraTextView;
    private TextView mRazioneCaloricaModerataTextView;
    private TextView mRazioneCaloricaPesanteTextView;






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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onActivityCreated()");

        findViewsById();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onResume()");

        int verificaRisultatiGiaCalcolatiCorrente = 0;
        float bmi = 0;
        float bmiArrotondato = 0;

        // Verifico se devo ricalcolare i risultati o meno
        boolean controlloRicalcolo = ((CalcoloValoriEnergeticiActivity) getActivity()).flagClicButtonCalcola;
        if (controlloRicalcolo) {
            ((CalcoloValoriEnergeticiActivity) getActivity()).flagRisultatiGiaCalcolati++;

            Toast.makeText(getActivity(), ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni, Toast.LENGTH_SHORT).show();
            /*
            // BMI
            bmi = calcolaBMI();
            bmiArrotondato = (float) Math.round(bmi * 100) / 100;
            mBmiTextView.setText(Float.toString(bmiArrotondato));

            // Barra colorata
            settaBordoBarraColorata(bmi);

            int altezzaCm = ((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm;


            // TODO: calcolare tutti i seguenti valori
            // Peso Calcolato Ideale
            // Metabolismo Basale
            // Fabbisogno Energetico
            // Razione Calorica Giornaliera

            */

            // Toast.makeText(getActivity(), "Risultato: " + altezzaCm, Toast.LENGTH_SHORT).show();

        } else {
            // TODO: implementare un modo per memorizzare in sessione i valori calcolati in modo da visualizzarli se si fa uno switch tra i fragment
            int verificaRisultatiGiaCalcolati = ((CalcoloValoriEnergeticiActivity) getActivity()).flagRisultatiGiaCalcolati;
            if (verificaRisultatiGiaCalcolati > verificaRisultatiGiaCalcolatiCorrente) {
                verificaRisultatiGiaCalcolatiCorrente = verificaRisultatiGiaCalcolati;
                caricaValori();
            }
            Toast.makeText(getActivity(), "Clicca su 'Calcola'", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onPause()");

        // Rimuovo il bordo della cella selezionata
        if (mBordoCellaBarraColorata != null) {
            GradientDrawable backgroundGradient = (GradientDrawable) mBordoCellaBarraColorata.getBackground();
            backgroundGradient.setStroke(0, getResources().getColor(R.color.nero_chiaro));
        }

    }


    /*
      Metodo che carica i valori dei parametri "risultati" calcolati precedentemente, in modo da
      visualizzarli a ogni refresh della pagina
    */
    private void caricaValori() {
        mBmiTextView.setText(Integer.toString(((CalcoloValoriEnergeticiActivity) getActivity()).flagRisultatiGiaCalcolati));
    }





    /*
      Metodo che calcola il BMI del paziente
    */
    private float calcolaBMI() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaBMI()");

        int altezzaCm = ((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm;
        float altezzaM = (float) altezzaCm / 100;
        float pesoKg = ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoKg;

        // TODO: Implementare un menu in cui si seleziona il BMI preferito. Qui mettere un IF.
        // BMI Nuovo: 1,3 x peso (Kg) / (Altezza (m))^2,5
        return (float) ((1.3f * pesoKg) / (Math.pow(altezzaM, 2.5f)));
        // BMI Vecchio: peso (kg) / (Altezza (m)) ^ 2
        // return (float) (pesoKg) / (Math.pow(altezzaM, 2.0f)));

        /*
        float bmi = 0.0f;
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
        */
    }


    /*
       Metodo che in base al valore di BMI ricevuto come parametro, evidenza il bordo della TextView
       corretta all'interno della barra colorata.
     */
    private void settaBordoBarraColorata(float bmi) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in settaBordoBarraColorata()");

        // Determino la cella a cui aggiungere il bordo
        try {
            if (bmi < 16f) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_16meno);
            } else if (16f <= bmi && bmi < 19f) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_16_19);
            } else if (19f <= bmi && bmi < 25f) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_19_25);
            } else if (25f <= bmi && bmi < 30f) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_25_30);
            } else if (30f <= bmi && bmi < 35f) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_30_35);
            } else if (35f <= bmi && bmi < 40f) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_35_40);
            } else if (40 <= bmi) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_40piu);
            }
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

        // Aggiungo il bordo
        GradientDrawable backgroundGradient = (GradientDrawable) mBordoCellaBarraColorata.getBackground();
        backgroundGradient.setStroke(4, getResources().getColor(R.color.nero_chiaro));

    }


    /*
       Metodo che elimina eventuali bordi presenti sulle TextView della barra colorata.
     */
    private void eliminaBordoBarraColorata() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in eliminaBordoBarraColorata()");

        // Aggiungere il bordo ad una delle TextView della barra colorata
        mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_16meno);
        mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_16_19);
        mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_19_25);
        mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_25_30);
        mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_30_35);
        mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_35_40);
        mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_40piu);

        GradientDrawable backgroundGradient = (GradientDrawable) mBordoCellaBarraColorata.getBackground();
        backgroundGradient.setStroke(3, getResources().getColor(R.color.nero_chiaro));
        //backgroundGradient.

    }


    /*
       Metodo che consente di ottenere i reference per tutte le View che verranno utilizzate
     */
    private void findViewsById() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in findViewsById()");

        try {
            // Risultati
            mBmiTextView = (TextView) getView().findViewById(R.id.text_view_bmi);
            mPesoCalcolatoIdealeTextView = (TextView) getView().findViewById(R.id.text_view_peso_calcolato_ideale);
            mMetabolismoBasaleTextView = (TextView) getView().findViewById(R.id.text_view_metabolismo_basale);
            mFabbisognoEnergeticoTextView = (TextView) getView().findViewById(R.id.text_view_fabbisogno_energetico);
            mRazioneCaloricaLeggeraTextView = (TextView) getView().findViewById(R.id.text_view_razione_calorica_leggera);
            mRazioneCaloricaModerataTextView = (TextView) getView().findViewById(R.id.text_view_razione_calorica_moderata);
            mRazioneCaloricaPesanteTextView = (TextView) getView().findViewById(R.id.text_view_razione_calorica_pesante);

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }

}
