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

    // Risultati in evidenza
    private TextView mBordoCellaBarraColorata;
    private TextView mStatoSaluteTextView;
    private TextView mCondizioneFisicaTextView;
    private TextView mCostituzioneTextView;
    private TextView mMetabolismoBasaleTextView1;
    private TextView mRazioneCaloricaTextView;

    // Risultati completi
    private TextView mCostituzioneValoreTextView;
    private TextView mBmiTextView;
    private TextView mMetabolismoBasaleTextView2;
    private TextView mFabbisognoEnergeticoTextView;
    private TextView mRazioneCaloricaLeggeraTextView;
    private TextView mRazioneCaloricaModerataTextView;
    private TextView mRazioneCaloricaPesanteTextView;
    private TextView mPesoAttualeTextView;
    private TextView mMassaMagraPercentualeTextView;
    private TextView mMassaMagraPesoTextView;
    private TextView mMassaGrassaPercentualeTextView;
    private TextView mMassaGrassaPesoTextView;
    private TextView mPesoCalcolatoIdealeTextView;
    private TextView mPesoObiettivoTextView;
    private TextView mGirovitaFianchiWHR;
    private TextView mGirovitaCosciaWHT;

    // Esternalizzazione stringhe
    private static final String STATO_SALUTE_SOTTOPESO_GRAVE = "Sottopeso grave";
    private static final String STATO_SALUTE_SOTTOPESO = "Sottopeso lieve";
    private static final String STATO_SALUTE_NORMOPESO = "Normopeso";
    private static final String STATO_SALUTE_SOVRAPPESO = "Sovrappeso";
    private static final String STATO_SALUTE_OBESITA_UNO = "Obesita lieve";
    private static final String STATO_SALUTE_OBESITA_DUE = "Obesita media";
    private static final String STATO_SALUTE_OBESITA_TRE = "Obesita grave";
    private static final String CONDIZIONE_FISICA_OTTIMA = "Ottima";
    private static final String CONDIZIONE_FISICA_BUONA = "Buona";
    private static final String CONDIZIONE_FISICA_SCARSA = "Scarsa";
    private static final String CONDIZIONE_FISICA_PERICOLO_SALUTE = "Pericolo";
    private static final String CONDIZIONE_FISICA_OBESITA = "Fuori forma";
    private static final String COSTITUZIONE_BREVILINEA = "Brevilinea";
    private static final String COSTITUZIONE_NORMOLINEA = "Normolinea";
    private static final String COSTITUZIONE_LONGILINEA = "Longilinea";


    public FragmentRisultati() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onCreateView()");

        return inflater.inflate(R.layout.fragment_risultati, container, false);
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

        // Flag
        int verificaRisultatiGiaCalcolatiCorrente = 0;
        boolean controlloRicalcolo = ((CalcoloValoriEnergeticiActivity) getActivity()).flagClicButtonCalcola;
        boolean controlloDatiAnagrafici = ((CalcoloValoriEnergeticiActivity) getActivity()).flagParamAnagraficiInseriti;
        boolean controlloMisurazioni = ((CalcoloValoriEnergeticiActivity) getActivity()).flagParamMisurazioniInseriti;

        // Risultati
        float costituzione;
        float bmi;
        int metabolismoBasale;
        int fabbisognoEnergetico;
        int[] razioneCaloricaGiornaliera;
        float pesoAttuale;
        float[] massaMagra;
        float[] massaGrassa;
        float pesoCalcolatoIdeale;
        float pesoObiettivo;
        float girovitaFianchiWHR;
        float girovitaCosciaWHT;

        // Verifico se devo ricalcolare i risultati o meno
        if (controlloRicalcolo) {

            // Verifico se i dati del paziente e le misurazioni sono stati tutti inseriti
            if (controlloMisurazioni && controlloDatiAnagrafici) {
                //Toast.makeText(getActivity(), "Nessun parametro mancante", Toast.LENGTH_SHORT).show();
                ((CalcoloValoriEnergeticiActivity) getActivity()).flagRisultatiGiaCalcolati++;

                // Dati Anagrafici
                String sesso = ((CalcoloValoriEnergeticiActivity) getActivity()).mSesso;
                String dataDiNascita = ((CalcoloValoriEnergeticiActivity) getActivity()).mDataDiNascita;
                int eta = calcolaEta(dataDiNascita);

                String occupazione = ((CalcoloValoriEnergeticiActivity) getActivity()).mOccupazione;
                int livelloAttivitaFisica = calcolaLivelloAttivita(occupazione);

                // BMI & Stato Salute
                bmi = (float) Math.round(calcolaBMI() * 10) / 10;
                mBmiTextView.setText(Float.toString(bmi));
                stampaStatoSalute(bmi);

                // Barra Colorata
                settaBordoBarraColorata(bmi);

                // Costituzione
                costituzione = (float) Math.round(calcolaCostituzione() * 10) / 10;
                stampaCostituzione(costituzione, sesso);
                mCostituzioneValoreTextView.setText(Float.toString(costituzione));

                // Peso Attuale
                pesoAttuale = ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoKg;
                pesoAttuale = (float) Math.round(pesoAttuale * 10) / 10;
                mPesoAttualeTextView.setText(Float.toString(pesoAttuale));

                // Massa Magra | Indice 0: Percentuale Massa Magra | Indice 1: Peso Massa Magra
                massaMagra = calcolaMassaMagra(sesso);
                massaMagra[0] = (float) Math.round(massaMagra[0] * 10) / 10;
                massaMagra[1] = (float) Math.round(massaMagra[1] * 10) / 10;
                mMassaMagraPercentualeTextView.setText(Float.toString(massaMagra[0]));
                mMassaMagraPesoTextView.setText(Float.toString(massaMagra[1]));

                // Massa Grassa
                massaGrassa = calcolaMassaGrassa(sesso);
                massaGrassa[0] = (float) Math.round(massaGrassa[0] * 10) / 10;
                massaGrassa[1] = (float) Math.round(massaGrassa[1] * 10) / 10;
                mMassaGrassaPercentualeTextView.setText(Float.toString(massaGrassa[0]));
                mMassaGrassaPesoTextView.setText(Float.toString(massaGrassa[1]));

                // Peso Calcolato Ideale
                pesoCalcolatoIdeale = (float) Math.round(calcolaPesoCalcolatoIdeale(sesso) * 10) / 10;
                mPesoCalcolatoIdealeTextView.setText(Float.toString(pesoCalcolatoIdeale));

                // TODO: Peso obiettivo

                // Metabolismo Basale
                metabolismoBasale = (int) calcolaMetabolismoBasale(sesso, eta);
                mMetabolismoBasaleTextView1.setText(Integer.toString(metabolismoBasale));
                mMetabolismoBasaleTextView2.setText(Integer.toString(metabolismoBasale));

                // Fabbisogno Energetico
                fabbisognoEnergetico = (int) calcolaFabbisognoEnergetico(sesso, eta, livelloAttivitaFisica, metabolismoBasale);
                mFabbisognoEnergeticoTextView.setText(Integer.toString(fabbisognoEnergetico));

                // Razione Calorica Giornaliera
                razioneCaloricaGiornaliera = calcolaRazioneCalorica(bmi, pesoCalcolatoIdeale);
                mRazioneCaloricaLeggeraTextView.setText(Integer.toString(razioneCaloricaGiornaliera[0]));
                mRazioneCaloricaModerataTextView.setText(Integer.toString(razioneCaloricaGiornaliera[1]));
                mRazioneCaloricaPesanteTextView.setText(Integer.toString(razioneCaloricaGiornaliera[2]));

                // Razione Calorica in "In Evidenza"
                if (livelloAttivitaFisica == 1)         mRazioneCaloricaTextView.setText(Integer.toString(razioneCaloricaGiornaliera[0]));
                else if (livelloAttivitaFisica == 2)    mRazioneCaloricaTextView.setText(Integer.toString(razioneCaloricaGiornaliera[1]));
                else if (livelloAttivitaFisica == 3)    mRazioneCaloricaTextView.setText(Integer.toString(razioneCaloricaGiornaliera[2]));

                // TODO: WHR - Rapporto Girovita Fianchi

                // TODO: WHT - Rapporto Girovita Coscia

                // TODO: Creare un array con tutti i valori da salvare e passarlo al metodo salvaRisultatiTemporanei
                // Salvo i risultati temporanei che saranno, eventualmente, ricaricati
                salvaRisultatiTemporanei(costituzione, massaMagra, massaGrassa, bmi, pesoCalcolatoIdeale, pesoObiettivo, metabolismoBasale, fabbisognoEnergetico, razioneCaloricaGiornaliera);

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
            //Toast.makeText(getActivity(), "Clicca su 'Calcola'", Toast.LENGTH_SHORT).show();
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
      Metodo che salva i risultati calcolati in modo da poterli visualizzare successivamente quando
      si apre il fragment "Risultati" senza aver cliccato sul button "Calcola"
    */
    private void salvaRisultatiTemporanei(float costituzione, float[] massaMagra, float[] massaGrassa, float bmi, float pesoCalcolatoIdeale, float pesoObiettivo, int metabolismoBasale, int fabbisognoEnergetico, int[] razioneCaloricaGiornaliera) {
        // TODO: Aggiungere costituzione e gli altri
        ((CalcoloValoriEnergeticiActivity) getActivity()).mCostituzione = costituzione;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mMassaMagra = massaMagra;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mMassaGrassa = massaGrassa;

        ((CalcoloValoriEnergeticiActivity) getActivity()).mIndiceMassaCorporea = bmi;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale = pesoCalcolatoIdeale;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mMetabolismoBasale = metabolismoBasale;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mFabbisognoEnergetico = fabbisognoEnergetico;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mRazioneCaloricaGiornaliera = razioneCaloricaGiornaliera;
    }


    /*
      Metodo che resetta i risultati calcolati nel caso in cui ci siano parametri mancanti quando
      si clicca su "Calcola"
    */
    private void azzeraRisultatiTemporanei() {
        // TODO: Aggiungere costituzione e gli altri
        ((CalcoloValoriEnergeticiActivity) getActivity()).mCostituzione = 0;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mMassaMagra[0] = 0;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mMassaMagra[1] = 0;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mMassaGrassa[0] = 0;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mMassaGrassa[1] = 0;

        ((CalcoloValoriEnergeticiActivity) getActivity()).mIndiceMassaCorporea = 0;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale = 0;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mMetabolismoBasale = 0;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mFabbisognoEnergetico = 0;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mRazioneCaloricaGiornaliera[0] = 0;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mRazioneCaloricaGiornaliera[1] = 0;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mRazioneCaloricaGiornaliera[2] = 0;
    }


    /*
      Metodo che carica i valori dei parametri "risultati" calcolati precedentemente, in modo da
      visualizzarli a ogni refresh della pagina
    */
    private void caricaValori() {
        // TODO: Aggiungere costituzione e gli altri

        String sesso = ((CalcoloValoriEnergeticiActivity) getActivity()).mSesso;
        String occupazione = ((CalcoloValoriEnergeticiActivity) getActivity()).mOccupazione;
        int livelloAttivitaFisica = calcolaLivelloAttivita(occupazione);

        // Costituzione
        float costituzione = ((CalcoloValoriEnergeticiActivity) getActivity()).mCostituzione;
        if (costituzione != 0) {
            mCostituzioneValoreTextView.setText(Float.toString(costituzione));
            stampaCostituzione(costituzione, sesso);
        }

        // Peso Attuale
        mPesoAttualeTextView.setText(Float.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mPesoKg));

        // Massa Magra
        float massaMagra, massaMagraPercentuale;
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mMassaMagra[0] != 0) {
            massaMagra = ((CalcoloValoriEnergeticiActivity) getActivity()).mMassaMagra[0];
            massaMagraPercentuale = (float) Math.round(massaMagra * 100) / 100;
            //massaMagraKg = (float) Math.round(massaMagra[1] * 10) / 10;
            mMassaMagraPercentualeTextView.setText(Float.toString(massaMagraPercentuale));
        }

        // Massa Grassa
        float massaGrassa, massaGrassaPercentuale;
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mMassaGrassa[0] != 0) {
            massaGrassa = ((CalcoloValoriEnergeticiActivity) getActivity()).mMassaGrassa[0];
            massaGrassaPercentuale = (float) Math.round(massaGrassa * 100) / 100;
            mMassaGrassaPercentualeTextView.setText(Float.toString(massaGrassaPercentuale));
        }

        // Razione Calorica Consigliata
        int[] razioneCaloricaGiornaliera = ((CalcoloValoriEnergeticiActivity) getActivity()).mRazioneCaloricaGiornaliera;
        if (livelloAttivitaFisica == 1)         mRazioneCaloricaTextView.setText(Integer.toString(razioneCaloricaGiornaliera[0]));
        else if (livelloAttivitaFisica == 2)    mRazioneCaloricaTextView.setText(Integer.toString(razioneCaloricaGiornaliera[1]));
        else if (livelloAttivitaFisica == 3)    mRazioneCaloricaTextView.setText(Integer.toString(razioneCaloricaGiornaliera[2]));

        // BMI e barra colorata
        float bmi = ((CalcoloValoriEnergeticiActivity) getActivity()).mIndiceMassaCorporea;
        if (bmi != 0) {
            mBmiTextView.setText(Float.toString(bmi));
            settaBordoBarraColorata(bmi);
            stampaStatoSalute(bmi);
        }

        // Peso Calcolato Ideale
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale != 0) {
            mPesoCalcolatoIdealeTextView.setText(Float.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale));
            mPesoIdealeTextView.setText(Float.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mPesoCalcolatoIdeale));
        }

        // Metabolismo Basale
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mMetabolismoBasale != 0) {
            mMetabolismoBasaleTextView1.setText(Integer.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mMetabolismoBasale));
            mMetabolismoBasaleTextView2.setText(Integer.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mMetabolismoBasale));
        }

        // Fabbisogno Energetico
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mFabbisognoEnergetico != 0)
            mFabbisognoEnergeticoTextView.setText(Float.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mFabbisognoEnergetico));

        // Razione Calorica Giornaliera
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mRazioneCaloricaGiornaliera[0] != 0)
            mRazioneCaloricaLeggeraTextView.setText(Integer.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mRazioneCaloricaGiornaliera[0]));
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mRazioneCaloricaGiornaliera[1] != 0)
            mRazioneCaloricaModerataTextView.setText(Integer.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mRazioneCaloricaGiornaliera[1]));
        if (((CalcoloValoriEnergeticiActivity) getActivity()).mRazioneCaloricaGiornaliera[2] != 0)
            mRazioneCaloricaPesanteTextView.setText(Integer.toString(((CalcoloValoriEnergeticiActivity) getActivity()).mRazioneCaloricaGiornaliera[2]));
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
      Metodo che calcola il livello di attività del paziente in base all'occupazione selezionata
      nel fragment "Dati Paziente"
    */
    private int calcolaLivelloAttivita(String occupazione) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaLivelloAttivita()");

        int livelloAttivita = 1;

        switch (occupazione) {

            case "Altro":
            case "Dirigente":
            case "Disoccupato":
            case "Impiegato":
            case "Libero Professionista":
            case "Tecnico":
                livelloAttivita = 1; // Leggera
                break;

            case "Addetto Vendita":
            case "Casalinga":
            case "Forze Armate":
                livelloAttivita = 2; // Moderata
                break;

            case "Agricoltore":
            case "Operaio":
            case "Pescatore":
            case "Sportivo":
                livelloAttivita = 3; // Pesante
                break;

            default:
                //Toast.makeText(getActivity(), "Nessun parametro modificato", Toast.LENGTH_SHORT).show();
        }

        return livelloAttivita;
    }


    /*
      Metodo che stampa la condizione fisica del paziente in base al bmi.
    */
    private void stampaStatoSalute(float bmi) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in stampaStatoSalute()");

        if (isBetween(bmi, 0, 16f)) {
            mStatoSaluteTextView.setText(STATO_SALUTE_SOTTOPESO_GRAVE);
        } else if (isBetween(bmi, 16f, 19f)) {
            mStatoSaluteTextView.setText(STATO_SALUTE_SOTTOPESO);
        } else if (isBetween(bmi, 19f, 25f)) {
            mStatoSaluteTextView.setText(STATO_SALUTE_NORMOPESO);
        } else if (isBetween(bmi, 25f, 30f)) {
            mStatoSaluteTextView.setText(STATO_SALUTE_SOVRAPPESO);
        } else if (isBetween(bmi, 30f, 25f)) {
            mStatoSaluteTextView.setText(STATO_SALUTE_OBESITA_UNO);
        } else if (isBetween(bmi, 35f, 40f)) {
            mStatoSaluteTextView.setText(STATO_SALUTE_OBESITA_DUE);
        } else if (isBetween(bmi, 40f, 100f)) {
            mStatoSaluteTextView.setText(STATO_SALUTE_OBESITA_TRE);
        }

    }


    /*
      Metodo che calcola il BMI del paziente
    */
    private float calcolaCostituzione() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaBMI()");

        int altezzaCm = ((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm;
        float circonferenzaPolso = ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaPolso;

        return altezzaCm / circonferenzaPolso;
    }


    /*
      Metodo che stampa la tipologia di costituzione del pazinete in base al valore di costituzione
      calcolato dal metodo calcolaCostituzione() e al sesso del paziente stesso.
    */
    private void stampaCostituzione(float costituzione, String sesso) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in stampaCostituzione()");

        if (sesso.equals("M")) {

            if (isBetween(costituzione, 0, 9.6f)) {
                mCostituzioneTextView.setText(COSTITUZIONE_BREVILINEA);
            } else if (isBetween(costituzione, 9.6f, 10.4f)) {
                mCostituzioneTextView.setText(COSTITUZIONE_NORMOLINEA);
            } else if (isBetween(costituzione, 10.4f, 50f)) {
                mCostituzioneTextView.setText(COSTITUZIONE_LONGILINEA);
            }

        } else if (sesso.equals("F")) {

            if (isBetween(costituzione, 0, 9.9f)) {
                mCostituzioneTextView.setText(COSTITUZIONE_BREVILINEA);
            } else if (isBetween(costituzione, 9.9f, 10.9f)) {
                mCostituzioneTextView.setText(COSTITUZIONE_NORMOLINEA);
            } else if (isBetween(costituzione, 10.9f, 50f)) {
                mCostituzioneTextView.setText(COSTITUZIONE_LONGILINEA);
            }

        }

    }



    /*
      Metodo che calcola la massa magra in base al sesso, al peso e all'altezza del paziente,
      passati come parametri al metodo
    */
    private float[] calcolaMassaMagra(String sesso) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaMassaMagra()");

        float pesoKg = ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoKg;
        int altezzaCm = ((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm;
        //float altezzaM = (float) altezzaCm / 100;

        // Posto 0: massa magra espressa in termini percentuali
        // Posto 1: massa magra espressa in chilogrammi
        float[] massaMagra = new float[2];

        // Calcolo la percentuale di massa magra
        if (sesso.equals("M")) {
            massaMagra[0] = (float) ( (pesoKg * 1.10f) - 128 * (Math.pow(pesoKg, 2.0f) / Math.pow(altezzaCm, 2.0f)) );
        } else if (sesso.equals("F")) {
            massaMagra[0] = (float) ( (pesoKg * 1.07f) - 148 * (Math.pow(pesoKg, 2.0f) / Math.pow(altezzaCm, 2.0f)) );
        }

        // Calcola la "quantità" di massa magra
        massaMagra[1] = pesoKg * (massaMagra[0]/100);

        return massaMagra;
    }


    /*
      Metodo che calcola la massa magra in base al sesso, al peso e all'altezza del paziente,
      passati come parametri al metodo
    */
    private float[] calcolaMassaGrassa(String sesso) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaMassaGrassa()");

        float pesoKg = ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoKg;
        int altezzaCm = ((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm;
        //float altezzaM = (float) altezzaCm / 100;

        float girovita = ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaGirovita;
        float collo = ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaCollo;
        float fianchi = ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaFianchi;

        // Posto 0: massa grassa espressa in termini percentuali
        // Posto 1: massa grassa espressa in chilogrammi
        float[] massaGrassa = new float[2];

        // Calcolo la percentuale di massa grassa
        if (sesso.equals("M")) {
            massaGrassa[0] = (float) (495 / (1.0324 - 0.19077 * (Math.log(girovita-collo)) + 0.15456 * (Math.log(altezzaCm))) - 450);
        } else if (sesso.equals("F")) {
            massaGrassa[0] = (float) (495 / (1.29579 - 0.35004 * (Math.log(girovita+fianchi-collo)) + 0.221 * (Math.log(altezzaCm))) - 450);
        }

        // Calcola la "quantità" di massa grassa
        massaGrassa[1] = pesoKg * (massaGrassa[0]/100);

        return massaGrassa;
    }


    /*
      Metodo che calcola la costituzione del paziente usando altezza e circonferenza polso
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
    private float calcolaFabbisognoEnergetico(String sesso, int eta, int livelloAttivitaFisica, int metabolismoBasale) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaFabbisgnoEnergetico()");

        float laf = 0;

        // TODO: Implementare un menu in cui si seleziona il metodo per il calcolo del LAF preferito. Qui mettere un IF.

        // Metodo Commission of the European Community
        if (sesso.equals("M")) {

            if (isBetween(eta, 10, 13))                 laf = 1.65f;
            else if (isBetween(eta, 14, 17))            laf = 1.58f;
            else if (isBetween(eta, 18, 59)) {
                if (livelloAttivitaFisica == 1)         laf = 1.55f;
                else if (livelloAttivitaFisica == 2)    laf = 1.78f;
                else if (livelloAttivitaFisica == 3)    laf = 2.10f;
            }
            else if (isBetween(eta, 60, 110))           laf = 1.51f;

        } else if (sesso.equals("F")) {

            if (isBetween(eta, 10, 13))                 laf = 1.55f;
            else if (isBetween(eta, 14, 17))            laf = 1.50f;
            else if (isBetween(eta, 18, 59)) {
                if (livelloAttivitaFisica == 1)         laf = 1.56f;
                else if (livelloAttivitaFisica == 2)    laf = 1.64f;
                else if (livelloAttivitaFisica == 3)    laf = 1.82f;
            }
            else if (isBetween(eta, 60, 110))           laf = 1.56f;

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
    private int[] calcolaRazioneCalorica(float bmi, float pesoCalcolatoIdeale) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in calcolaRazioneCalorica()");

        int razioneCalorica[] = new int[3];
        float quozienteEnergeticoAttivitaLeggera = 0;
        float quozienteEnergeticoAttivitaModerata = 0;
        float quozienteEnergeticoAttivitaPesante = 0;

        if (isBetween(bmi, 30f, 100f)) {
            quozienteEnergeticoAttivitaLeggera = 20f;
            quozienteEnergeticoAttivitaModerata = 25f;
            quozienteEnergeticoAttivitaPesante = 30f;
        } else if (isBetween(bmi, 25f, 29.999f)) {
            quozienteEnergeticoAttivitaLeggera = 22.5f;
            quozienteEnergeticoAttivitaModerata = 27.5f;
            quozienteEnergeticoAttivitaPesante = 32.5f;
        } else if (isBetween(bmi, 19f, 24.999f)) {
            quozienteEnergeticoAttivitaLeggera = 25f;
            quozienteEnergeticoAttivitaModerata = 30f;
            quozienteEnergeticoAttivitaPesante = 35f;
        } else if (isBetween(bmi, 15f, 18.999f)) {
            quozienteEnergeticoAttivitaLeggera = 30f;
            quozienteEnergeticoAttivitaModerata = 35f;
            quozienteEnergeticoAttivitaPesante = 40f;
        } else if (isBetween(bmi, 0f, 14.999f)) {
            quozienteEnergeticoAttivitaLeggera = 35f;
            quozienteEnergeticoAttivitaModerata = 40f;
            quozienteEnergeticoAttivitaPesante = 45f;
        }

        razioneCalorica[0] = (int) (pesoCalcolatoIdeale * quozienteEnergeticoAttivitaLeggera);
        razioneCalorica[1] = (int) (pesoCalcolatoIdeale * quozienteEnergeticoAttivitaModerata);
        razioneCalorica[2] = (int) (pesoCalcolatoIdeale * quozienteEnergeticoAttivitaPesante);

        return razioneCalorica;

    }


    /*
       Metodo che in base al valore di BMI ricevuto come parametro, evidenza il bordo della TextView
       corretta all'interno della barra colorata.
     */
    private void settaBordoBarraColorata(float bmi) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in settaBordoBarraColorata()");

        // Determino la cella a cui aggiungere il bordo
        try {
            if (isBetween(bmi, 0, 16f)) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_16meno);
            } else if (isBetween(bmi, 16f, 19f)) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_16_19);
            } else if (isBetween(bmi, 19f, 25f)) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_19_25);
            } else if (isBetween(bmi, 25f, 30f)) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_25_30);
            } else if (isBetween(bmi, 30f, 25f)) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_30_35);
            } else if (isBetween(bmi, 35f, 40f)) {
                mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_35_40);
            } else if (isBetween(bmi, 40f, 100f)) {
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
       Metodo che consente di ottenere i reference per tutte le View che verranno utilizzate
     */
    private void findViewsById() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in findViewsById()");

        try {
            // Risultati in evidenza
            mStatoSaluteTextView = (TextView) getView().findViewById(R.id.text_view_stato_salute);
            mCondizioneFisicaTextView = (TextView) getView().findViewById(R.id.text_view_condizione_fisica);
            mCostituzioneTextView = (TextView) getView().findViewById(R.id.text_view_costituzione);
            mMetabolismoBasaleTextView1 = (TextView) getView().findViewById(R.id.text_view_metabolismo_basale_1);
            mRazioneCaloricaTextView = (TextView) getView().findViewById(R.id.text_view_razione_calorica);

            // Risultati completi
            mCostituzioneValoreTextView = (TextView) getView().findViewById(R.id.text_view_costituzione_valore);

            mBmiTextView = (TextView) getView().findViewById(R.id.text_view_bmi);
            mMetabolismoBasaleTextView2 = (TextView) getView().findViewById(R.id.text_view_metabolismo_basale_2);
            mFabbisognoEnergeticoTextView = (TextView) getView().findViewById(R.id.text_view_fabbisogno_energetico);
            mRazioneCaloricaLeggeraTextView = (TextView) getView().findViewById(R.id.text_view_razione_calorica_leggera);
            mRazioneCaloricaModerataTextView = (TextView) getView().findViewById(R.id.text_view_razione_calorica_moderata);
            mRazioneCaloricaPesanteTextView = (TextView) getView().findViewById(R.id.text_view_razione_calorica_pesante);

            mPesoAttualeTextView = (TextView) getView().findViewById(R.id.text_view_peso_attuale);
            mMassaMagraPercentualeTextView = (TextView) getView().findViewById(R.id.text_view_massa_magra_percentuale);
            mMassaMagraPesoTextView = (TextView) getView().findViewById(R.id.text_view_massa_magra_peso);
            mMassaGrassaPercentualeTextView = (TextView) getView().findViewById(R.id.text_view_massa_grassa_percentuale);
            mMassaGrassaPesoTextView = (TextView) getView().findViewById(R.id.text_view_massa_grassa_peso);
            mPesoCalcolatoIdealeTextView = (TextView) getView().findViewById(R.id.text_view_peso_calcolato_ideale);
            mPesoObiettivoTextView = (TextView) getView().findViewById(R.id.text_view_peso_obiettivo);

            mGirovitaFianchiWHR = (TextView) getView().findViewById(R.id.text_view_whr_girovita_fianchi);
            mGirovitaCosciaWHT = (TextView) getView().findViewById(R.id.text_view_wht_girovita_coscia);

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }


    /*
      Metodo che verifica se un intero è compreso all'interno di un intervallo
    */
    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public static boolean isBetween(float x, float lower, float upper) {
        return lower <= x && x <= upper;
    }

}