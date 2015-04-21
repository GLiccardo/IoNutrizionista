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
        float pesoCalcolatoIdeale = 0;
        float pesoCalcolatoIdealeArrotondato = 0;
        int metabolismoBasale = 0;


        boolean controlloRicalcolo = ((CalcoloValoriEnergeticiActivity) getActivity()).flagClicButtonCalcola;
        boolean controlloDatiAnagrafici = ((CalcoloValoriEnergeticiActivity) getActivity()).flagParamAnagraficiInseriti;
        boolean controlloMisurazioni = ((CalcoloValoriEnergeticiActivity) getActivity()).flagParamMisurazioniInseriti;

        // Verifico se devo ricalcolare i risultati o meno
        if (controlloRicalcolo) {

            // Verifico se i dati del paziente e le misurazioni sono stati tutti inseriti
            if (controlloMisurazioni /* && controlloDatiAnagrafici */) { // TODO: Rimuovere commenti
                //Toast.makeText(getActivity(), "Nessun parametro mancante", Toast.LENGTH_SHORT).show();

                ((CalcoloValoriEnergeticiActivity) getActivity()).flagRisultatiGiaCalcolati++;

                // TODO: Recuperare età e sesso dai DatiAnagrafici
                String sesso = "M";
                int eta = 27;

                // BMI
                bmi = calcolaBMI();
                bmiArrotondato = (float) Math.round(bmi * 100) / 100;
                mBmiTextView.setText(Float.toString(bmiArrotondato));

                // Barra colorata
                settaBordoBarraColorata(bmi);

                // Peso Calcolato Ideale
                pesoCalcolatoIdeale = calcolaPesoCalcolatoIdeale(sesso);
                pesoCalcolatoIdealeArrotondato = (float) Math.round(pesoCalcolatoIdeale * 10) / 10;
                mPesoCalcolatoIdealeTextView.setText(Float.toString(pesoCalcolatoIdealeArrotondato));

                // Metabolismo Basale
                metabolismoBasale = (int) calcolaMetabolismoBasale(sesso, eta);
                mMetabolismoBasaleTextView.setText(Integer.toString(metabolismoBasale));


                // TODO: calcolare tutti i seguenti valori
                // Fabbisogno Energetico
                // Razione Calorica Giornaliera

                // TODO: salvare i risultati temporanei nelle variabili "old" per essere visualizzati successivamente
                salvaRisultatiTemporanei(bmiArrotondato, pesoCalcolatoIdealeArrotondato);

            } else {
                String parametriMancantiIntro = "Parametri mancanti:";
                //String parametriMancantiDatiAnagrafici = String.valueOf(((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiDatiAnagrafici);
                String parametriMancantiMisurazioni = String.valueOf(((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni);

                Toast.makeText(getActivity(), parametriMancantiIntro + parametriMancantiMisurazioni, Toast.LENGTH_SHORT).show();

                azzeraRisultatiTemporanei();
            }

        } else {
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
      Metodo che verifica se un intero è compreso all'interno di un intervallo
    */
    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }


    /*
      Metodo che salva i risultati calcolati in modo da poterli visualizzare successivamente quando
      si apre il fragment "Risultati" senza aver cliccato sul button "Calcola"
    */
    private void salvaRisultatiTemporanei(float bmi, float pesoCalcolatoIdeale) {
        ((CalcoloValoriEnergeticiActivity) getActivity()).mIndiceMassaCorporea = bmi;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale = pesoCalcolatoIdeale;
    }


    /*
      Metodo che salva i risultati calcolati in modo da poterli visualizzare successivamente quando
      si apre il fragment "Risultati" senza aver cliccato sul button "Calcola"
    */
    private void azzeraRisultatiTemporanei() {
        ((CalcoloValoriEnergeticiActivity) getActivity()).mIndiceMassaCorporea = 0.0f;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale = 0.0f;
    }


    /*
      Metodo che carica i valori dei parametri "risultati" calcolati precedentemente, in modo da
      visualizzarli a ogni refresh della pagina
    */
    private void caricaValori() {
        // BMI e barra colorata
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mIndiceMassaCorporea != 0) {
            mBmiTextView.setText(Float.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mIndiceMassaCorporea));
            settaBordoBarraColorata(((CalcoloValoriEnergeticiActivity) getActivity()).mIndiceMassaCorporea);
        }

        // Peso Calcolato Ideale
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale != 0)
            mBmiTextView.setText(Float.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale));
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
    }


    /*
      Metodo che calcola il Peso Calcolato Ideale del paziente in base al sesso passato come parametro
    */
    private float calcolaPesoCalcolatoIdeale(String sesso) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaPesoCalcolatoIdeale()");

        int altezzaCm = ((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm;
        float altezzaM = (float) altezzaCm / 100;
        float pesoRealeKg = ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoKg;
        float bmiRiferimento = (sesso.equals("M")) ? 22.5f : 20.6f;

        float pesoIdealeKg = (float) (bmiRiferimento * (Math.pow(altezzaM, 2.0f)));
        float pesoCalcolatoIdeale = (float) ((pesoRealeKg - pesoIdealeKg) * 0.25 + pesoIdealeKg);

        return pesoCalcolatoIdeale;
    }


    /*
      Metodo che calcola il Peso Calcolato Ideale del paziente in base al sesso passato come parametro
    */
    private float calcolaMetabolismoBasale(String sesso, int eta) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaMetabolismoBasale()");

        float pesoRealeKg = ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoKg;
        float metabolismoBasale = 0;

        if (sesso.equals("M")) {

            if (isBetween(eta, 0, 2)) {
                metabolismoBasale = (59.5f * pesoRealeKg) - 31;
            } else if (isBetween(eta, 3, 9)) {
                metabolismoBasale = (22.7f * pesoRealeKg) + 504;
            } else if (isBetween(eta, 10, 17)) {
                metabolismoBasale = (17.7f * pesoRealeKg) + 650;
            } else if (isBetween(eta, 18, 29)) {
                metabolismoBasale = (15.3f * pesoRealeKg) + 679;
            } else if (isBetween(eta, 30, 59)) {
                metabolismoBasale = (11.6f * pesoRealeKg) + 879;
            } else if (isBetween(eta, 60, 74)) {
                metabolismoBasale = (11.9f * pesoRealeKg) + 700;
            } else if (isBetween(eta, 75, 110)) {
                metabolismoBasale = (8.4f * pesoRealeKg) + 819;
            }

        } else if (sesso.equals("F")) {

            if (isBetween(eta, 0, 2)) {
                metabolismoBasale = (58.3f * pesoRealeKg) - 31;
            } else if (isBetween(eta, 3, 9)) {
                metabolismoBasale = (20.3f * pesoRealeKg) + 485;
            } else if (isBetween(eta, 10, 17)) {
                metabolismoBasale = (13.4f * pesoRealeKg) + 693;
            } else if (isBetween(eta, 18, 29)) {
                metabolismoBasale = (14.7f * pesoRealeKg) + 496;
            } else if (isBetween(eta, 30, 59)) {
                metabolismoBasale = (8.7f * pesoRealeKg) + 829;
            } else if (isBetween(eta, 60, 74)) {
                metabolismoBasale = (9.2f * pesoRealeKg) + 688;
            } else if (isBetween(eta, 75, 110)) {
                metabolismoBasale = (9.8f * pesoRealeKg) + 624;
            }

        }

        return metabolismoBasale;
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
        backgroundGradient.setStroke(0, getResources().getColor(R.color.nero_chiaro));
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
