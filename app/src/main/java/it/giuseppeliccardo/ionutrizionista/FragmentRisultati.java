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

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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
        int fabbisognoEnergetico = 0;
        int[] razioneCaloricaGiornaliera;


        boolean controlloRicalcolo = ((CalcoloValoriEnergeticiActivity) getActivity()).flagClicButtonCalcola;
        boolean controlloDatiAnagrafici = ((CalcoloValoriEnergeticiActivity) getActivity()).flagParamAnagraficiInseriti;
        boolean controlloMisurazioni = ((CalcoloValoriEnergeticiActivity) getActivity()).flagParamMisurazioniInseriti;

        // Verifico se devo ricalcolare i risultati o meno
        if (controlloRicalcolo) {

            // Verifico se i dati del paziente e le misurazioni sono stati tutti inseriti
            if (controlloMisurazioni && controlloDatiAnagrafici) {
                //Toast.makeText(getActivity(), "Nessun parametro mancante", Toast.LENGTH_SHORT).show();
                ((CalcoloValoriEnergeticiActivity) getActivity()).flagRisultatiGiaCalcolati++;

                String sesso = ((CalcoloValoriEnergeticiActivity) getActivity()).mSesso;
                String dataDiNascita = ((CalcoloValoriEnergeticiActivity) getActivity()).mDataDiNascita;
                int eta = calcolaEta(dataDiNascita);

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

                // Fabbisogno Energetico
                fabbisognoEnergetico = (int) calcolaFabbisognoEnergetico(sesso, eta, metabolismoBasale);
                mFabbisognoEnergeticoTextView.setText(Integer.toString(fabbisognoEnergetico));

                // TODO: Razione Calorica Giornaliera


                salvaRisultatiTemporanei(bmiArrotondato, pesoCalcolatoIdealeArrotondato, metabolismoBasale, fabbisognoEnergetico);

            } else {

                String parametriMancantiIntro = "Parametri mancanti:";
                String parametriMancantiDatiAnagrafici = String.valueOf(((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiDatiAnagrafici);
                String parametriMancantiMisurazioni = String.valueOf(((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni);

                Toast.makeText(getActivity(), parametriMancantiIntro + parametriMancantiDatiAnagrafici + parametriMancantiMisurazioni, Toast.LENGTH_SHORT).show();

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
      Metodo che usa la libreria esterna Joda Time per calcolare facilmente l'età del paziente.
      @Param
        format              > formato italiano della data inserita [dd/MM/yyyy]
        strDataDiNascita    > dataDiNascita di tipo String impostata tramite il DatePicker
        dateDataDiNascita   > conversione di strDataDiNascita da String a Date
        ldDataDiNascita     > conversione di dateDataDiNascita da Date a LocalDate
        ldDataOdierna       > data corrente di tipo LocalDate
     */
    private int calcolaEta(String strDataDiNascita) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        Date dateDataDiNascita = null;
        try {
            dateDataDiNascita = format.parse(strDataDiNascita);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LocalDate ldDataDiNascita = new LocalDate(dateDataDiNascita);
        LocalDate ldDataOdierna = new LocalDate();
        Years eta = Years.yearsBetween(ldDataDiNascita, ldDataOdierna);

        return eta.getYears();
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
    private void salvaRisultatiTemporanei(float bmi, float pesoCalcolatoIdeale, int metabolismoBasale, int fabbisognoEnergetico) {
        ((CalcoloValoriEnergeticiActivity) getActivity()).mIndiceMassaCorporea = bmi;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale = pesoCalcolatoIdeale;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mMetabolismoBasale = metabolismoBasale;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mFabbisognoEnergetico = fabbisognoEnergetico;
    }


    /*
      Metodo che salva i risultati calcolati in modo da poterli visualizzare successivamente quando
      si apre il fragment "Risultati" senza aver cliccato sul button "Calcola"
    */
    private void azzeraRisultatiTemporanei() {
        ((CalcoloValoriEnergeticiActivity) getActivity()).mIndiceMassaCorporea = 0.0f;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale = 0.0f;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mMetabolismoBasale = 0;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mFabbisognoEnergetico = 0;
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
            mPesoCalcolatoIdealeTextView.setText(Float.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale));

        // Metabolismo Basale
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mMetabolismoBasale != 0)
            mMetabolismoBasaleTextView.setText(Integer.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mMetabolismoBasale));

        // Fabbisogno Energetico
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mFabbisognoEnergetico != 0)
            mFabbisognoEnergeticoTextView.setText(Float.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mFabbisognoEnergetico));
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
      Metodo che calcola il fabbisogno energetico in base al sesso, all'età e al metabolismo basale
      del paziente, passati come parametri al metodo
    */
    private float calcolaFabbisognoEnergetico(String sesso, int eta, int metabolismoBasale) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaFabbisgnoEnergetico()");

        float laf = 0;

        // TODO: Implementare un menu in cui si seleziona il metodo per il calcolo del LAF preferito.Qui mettere un IF.

        // Metodo Commission of the European Community
        if (sesso.equals("M")) {

            if (isBetween(eta, 10, 13))           laf = 1.65f;
            else if (isBetween(eta, 14, 17))      laf = 1.58f;
            else if (isBetween(eta, 18, 59))      laf = 1.55f;
            else if (isBetween(eta, 60, 110))     laf = 1.51f;

        } else if (sesso.equals("F")) {

            if (isBetween(eta, 10, 13))           laf = 1.55f;
            else if (isBetween(eta, 14, 17))      laf = 1.50f;
            else if (isBetween(eta, 18, 110))     laf = 1.56f;

        }


        // Metodo FAO/WHO
        /*
        if (sesso.equals("M")) {

            if (eta == 10))                       laf = 1.76f;
            else if (eta == 11)                   laf = 1.73f;
            else if (eta == 12)                   laf = 1.69f;
            else if (eta == 13)                   laf = 1.67f;
            else if (eta == 14)                   laf = 1.65f;
            else if (eta == 15)                   laf = 1.62f;
            else if (eta == 16 || eta == 17)      laf = 1.60f;
            else if (isBetween(eta, 18, 59))      laf = 1.55f;
            else if (isBetween(eta, 60, 110))     laf = 1.45f;

        } else if (sesso.equals("F")) {

            if (eta == 10))                       laf = 1.65f;
            else if (eta == 11)                   laf = 1.63f;
            else if (eta == 12)                   laf = 1.60f;
            else if (eta == 13)                   laf = 1.58f;
            else if (eta == 14)                   laf = 1.57f;
            else if (eta == 15)                   laf = 1.54f;
            else if (eta == 16)                   laf = 1.53f;
            else if (eta == 17)                   laf = 1.52f;
            else if (isBetween(eta, 18, 59))      laf = 1.56f;
            else if (isBetween(eta, 60, 110))     laf = 1.48f;

        }
        */

        return metabolismoBasale * laf;
    }


    /*
      Metodo che calcola il fabbisogno energetico in base al sesso, all'età e al metabolismo basale
      del paziente, passati come parametri al metodo
    */
    private void calcolaRazioneCalorica(float pesoCalcolatoIdeale) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaRazioneCalorica()");


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
