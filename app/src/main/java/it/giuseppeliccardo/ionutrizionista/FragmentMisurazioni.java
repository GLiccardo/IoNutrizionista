package it.giuseppeliccardo.ionutrizionista;


import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class FragmentMisurazioni extends Fragment {

    private static final String TAG = "ioNutrizionista";

    private String mUltimoParametroModificato;

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

    // TODO: Aggiornare range valori consentiti per pliche e circonferenze
    // Esternalizzazione valori
    private static final float UNO_MIN = 1;
    private static final int ALTEZZA_MIN = 80;                  // cm
    private static final int ALTEZZA_MAX = 220;                 // cm
    private static final float PESO_MIN = 25;                   // kg
    private static final float PESO_MAX = 250;                  // kg
    private static final float PLICHE_GIROVITA_MIN = 1;         // mm
    private static final float PLICHE_GIROVITA_MAX = 70;        // mm
    private static final float PLICHE_SCHIENA_MIN = 1;          // mm
    private static final float PLICHE_SCHIENA_MAX = 70;         // mm
    private static final float PLICHE_BRACCIO_MIN = 1;          // mm
    private static final float PLICHE_BRACCIO_MAX = 70;         // mm
    private static final float CIRCONFERENZA_ADDOME_MIN = 50;   // mm
    private static final float CIRCONFERENZA_ADDOME_MAX = 180;  // mm
    private static final float CIRCONFERENZA_FIANCHI_MIN = 70;  // mm
    private static final float CIRCONFERENZA_FIANCHI_MAX = 150; // mm
    private static final float CIRCONFERENZA_COSCIA_MIN = 20;   // mm
    private static final float CIRCONFERENZA_COSCIA_MAX = 80;   // mm
    private static final float CIRCONFERENZA_POLSO_MIN = 10;    // mm
    private static final float CIRCONFERENZA_POLSO_MAX = 26;    // mm
    private static final float CIRCONFERENZA_BRACCIO_MIN = 20;  // mm
    private static final float CIRCONFERENZA_BRACCIO_MAX = 50;  // mm

    // Esternalizzazione stringhe
    private static final String TAG_DATI_MANCANTI = "Inserire i seguenti dati:";
    private static final String EDIT_TEXT_ALTEZZA = "Altezza";
    private static final String EDIT_TEXT_PESO = "Peso";
    private static final String EDIT_TEXT_PLICHE_GIROVITA = "Pliche Girovita";
    private static final String EDIT_TEXT_PLICHE_SCHIENA = "Pliche Schiena";
    private static final String EDIT_TEXT_PLICHE_BRACCIO = "Pliche Braccio";
    private static final String EDIT_TEXT_CIRCONFERENZA_ADDOME = "Circonferenza Addome";
    private static final String EDIT_TEXT_CIRCONFERENZA_FIANCHI = "Circonferenza Fianchi";
    private static final String EDIT_TEXT_CIRCONFERENZA_COSCIA = "Circonferenza Coscia";
    private static final String EDIT_TEXT_CIRCONFERENZA_POLSO = "Circonferenza Polso";
    private static final String EDIT_TEXT_CIRCONFERENZA_BRACCIO = "Circonferenza Braccio";


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
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onActivityCreated()");

        // Ottengo un riferimento alle Views della UI
        findViewsById();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onResume()");

        // Controllo se i valori dei parametri inseriti sono corretti
        checkValoriParametri();
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onPause()");

        // Cancello un eventuale valore errato di un parametro se clicco su 'Calcola'
        checkUltimoParametroModificato(mUltimoParametroModificato);



        getPesoForma();
        getPliche();
        getCirconferenza();

        // Verifico se tutti i parametri sono stati inseriti:
        // - [caso positivo] acquisisco i valori dei parametri e setto il flag a true
        // - [caso negativo] setto il flag a false
        if (checkInserimentoParametri()) {
            ((CalcoloValoriEnergeticiActivity) getActivity()).flagParamMisurazioniInseriti = true;
            //getDatiAnagrafici(); TODO: Da fare nel fragment DatiAnagrafici
            //getPesoForma();
            //getPliche();
            //getCirconferenza();
        } else {
            ((CalcoloValoriEnergeticiActivity) getActivity()).flagParamMisurazioniInseriti = false;
        }

    }


    /*
      Metodo che consente di ottenere i reference per tutte le View che verranno utilizzate
    */
    private void findViewsById() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in findViewsById()");

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
      Metodo che verifica a runtime se i valori inseriti dall'utente nei vari EditText sono corretti.
      In caso negativo, compare un AlertDialog che notifica l'errore e viene resettato il valore
      errato inserito dall'utente
    */
    private void checkValoriParametri() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in checkValoriParametri()");

        mUltimoParametroModificato = "";

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Attenzione!!!");

        // Altezza: 80 - 220 cm
        mAltezzaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mAltezzaEditText.getText().toString().equals(""))) {
                    int altezzaPaziente = Integer.parseInt(mAltezzaEditText.getText().toString());
                    if (altezzaPaziente < ALTEZZA_MIN || altezzaPaziente > ALTEZZA_MAX) {
                        // Reset "Altezza" field
                        mAltezzaEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + ALTEZZA_MIN + " - " + ALTEZZA_MAX + " cm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_ALTEZZA;
                }
            }
        });

        // Peso: 25 - 250 kg
        mPesoEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mPesoEditText.getText().toString().equals(""))) {
                    float pesoPaziente = Float.parseFloat(mPesoEditText.getText().toString());
                    if (pesoPaziente < PESO_MIN || pesoPaziente > PESO_MAX) {
                        // Reset "Peso" field
                        mPesoEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + PESO_MIN + " - " + PESO_MAX + " kg\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_PESO;
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Pliche Girovita: 0 - 9999 mm
        mPlicheGirovitaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mPlicheGirovitaEditText.getText().toString().equals(""))) {
                    float plicheGirovitaPaziente = Float.parseFloat(mPlicheGirovitaEditText.getText().toString());
                    if (plicheGirovitaPaziente < UNO_MIN || plicheGirovitaPaziente > PLICHE_GIROVITA_MAX) {
                        // Reset "Pliche Girovita" field
                        mPlicheGirovitaEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + UNO_MIN + " - " + PLICHE_GIROVITA_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_PLICHE_GIROVITA;
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Pliche Schiena: 0 - 9999 mm
        mPlicheSchienaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mPlicheSchienaEditText.getText().toString().equals(""))) {
                    float plicheSchienaPaziente = Float.parseFloat(mPlicheSchienaEditText.getText().toString());
                    if (plicheSchienaPaziente < UNO_MIN || plicheSchienaPaziente > PLICHE_SCHIENA_MAX) {
                        // Reset "Pliche Schiena" field
                        mPlicheSchienaEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + UNO_MIN + " - " + PLICHE_SCHIENA_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_PLICHE_SCHIENA;
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Pliche Braccio: 0 - 9999 mm
        mPlicheBraccioEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mPlicheBraccioEditText.getText().toString().equals(""))) {
                    float plicheBraccioPaziente = Float.parseFloat(mPlicheBraccioEditText.getText().toString());
                    if (plicheBraccioPaziente < UNO_MIN || plicheBraccioPaziente > PLICHE_BRACCIO_MAX) {
                        // Reset "Pliche Braccio" field
                        mPlicheBraccioEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + UNO_MIN + " - " + PLICHE_BRACCIO_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_PLICHE_BRACCIO;
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Circonferenza Addome: 0 - 9999 mm
        mCirconferenzaAddomeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaAddomeEditText.getText().toString().equals(""))) {
                    float circonferenzaAddomePaziente = Float.parseFloat(mCirconferenzaAddomeEditText.getText().toString());
                    if (circonferenzaAddomePaziente < UNO_MIN || circonferenzaAddomePaziente > CIRCONFERENZA_ADDOME_MAX) {
                        // Reset "Pliche Braccio" field
                        mCirconferenzaAddomeEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + UNO_MIN + " - " + CIRCONFERENZA_ADDOME_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_CIRCONFERENZA_ADDOME;
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Circonferenza Fianchi: 0 - 9999 mm
        mCirconferenzaFianchiEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaFianchiEditText.getText().toString().equals(""))) {
                    float circonferenzaFianchiPaziente = Float.parseFloat(mCirconferenzaFianchiEditText.getText().toString());
                    if (circonferenzaFianchiPaziente < UNO_MIN || circonferenzaFianchiPaziente > CIRCONFERENZA_FIANCHI_MAX) {
                        // Reset "Pliche Braccio" field
                        mCirconferenzaFianchiEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + UNO_MIN + " - " + CIRCONFERENZA_FIANCHI_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_CIRCONFERENZA_FIANCHI;
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Circonferenza Coscia: 0 - 9999 mm
        mCirconferenzaCosciaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaCosciaEditText.getText().toString().equals(""))) {
                    float circonferenzaCosciaPaziente = Float.parseFloat(mCirconferenzaCosciaEditText.getText().toString());
                    if (circonferenzaCosciaPaziente < UNO_MIN || circonferenzaCosciaPaziente > CIRCONFERENZA_COSCIA_MAX) {
                        // Reset "Pliche Braccio" field
                        mCirconferenzaCosciaEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + UNO_MIN + " - " + CIRCONFERENZA_COSCIA_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_CIRCONFERENZA_COSCIA;
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Circonferenza Polso: 0 - 9999 mm
        mCirconferenzaPolsoEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaPolsoEditText.getText().toString().equals(""))) {
                    float circonferenzaPolsoPaziente = Float.parseFloat(mCirconferenzaPolsoEditText.getText().toString());
                    if (circonferenzaPolsoPaziente < UNO_MIN || circonferenzaPolsoPaziente > CIRCONFERENZA_POLSO_MAX) {
                        // Reset "Pliche Braccio" field
                        mCirconferenzaPolsoEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + UNO_MIN + " - " + CIRCONFERENZA_POLSO_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_CIRCONFERENZA_POLSO;
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Circonferenza Braccio: 0 - 9999 mm
        mCirconferenzaBraccioEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaBraccioEditText.getText().toString().equals(""))) {
                    float circonferenzaBraccioPaziente = Float.parseFloat(mCirconferenzaBraccioEditText.getText().toString());
                    if (circonferenzaBraccioPaziente < UNO_MIN || circonferenzaBraccioPaziente > CIRCONFERENZA_BRACCIO_MAX) {
                        // Reset "Pliche Braccio" field
                        mCirconferenzaBraccioEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + UNO_MIN + " - " + CIRCONFERENZA_BRACCIO_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_CIRCONFERENZA_BRACCIO;
                }
            }
        });

    }


    private boolean checkInserimentoSingoloParametro(int valoreInt) {
        boolean res = (valoreInt != -1);
        return res;
    }

    private boolean checkInserimentoSingoloParametro(float valoreFloat) {
        boolean res = (valoreFloat != -1);
        return res;
    }


    /*
      Metodo che verifica se tutti i parametri del paziente sono stati inseriti o meno. Questo
      controllo avviene costruendo uno StringBuilder che conterrà l'elenco dei parametri che non
      sono stati inseriti.
      Alla fine della sua costruzione, viene controllato se lo StringBuilder è stato modificato
      rispetto allo stato iniziale, in caso affermativo vuol dire che alcuni parametri non sono
      stati inseriti.
    */
    private boolean checkInserimentoParametri() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in checkInserimentoParametri()");

        ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni = new StringBuilder("");

        mCheckAltezza = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm);
        if (!mCheckAltezza) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nAltezza");

        mCheckPeso = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mPesoKg);
        if (!mCheckPeso) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nPeso");

        /*
        mCheckPlicheGirovita = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheGirovita);
        if (!mCheckPlicheGirovita) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nPliche Girovita");

        mCheckPlicheSchiena = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheSchiena);
        if (!mCheckPlicheSchiena) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nPliche Schiena");

        mCheckPlicheBraccio = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheBraccio);
        if (!mCheckPlicheBraccio) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nPliche Braccio");

        mCheckCirconferenzaAddome = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaAddome);
        if (!mCheckCirconferenzaAddome) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nCirconferenza Addome");

        mCheckCirconferenzaFianchi = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaFianchi);
        if (!mCheckCirconferenzaFianchi) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nCirconferenza Fianchi");

        mCheckCirconferenzaCoscia = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaCoscia);
        if (!mCheckCirconferenzaCoscia) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nCirconferenza Coscia");

        mCheckCirconferenzaPolso = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaPolso);
        if (!mCheckCirconferenzaPolso) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nCirconferenza Polso");

        mCheckCirconferenzaBraccio = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaBraccio);
        if (!mCheckCirconferenzaBraccio) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nCirconferenza Braccio");
        */

        if (((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.toString().equals("")) {
            // Tutti i valori sono stati inseriti
            //((CalcoloValoriEnergeticiActivity) getActivity()).flagParamMisurazioniInseriti = true;
            return true;
        } else {
            //Toast.makeText(getActivity(), mStringaToast, Toast.LENGTH_SHORT).show();
            //((CalcoloValoriEnergeticiActivity) getActivity()).flagParamMisurazioniInseriti = false;
            return false;
        }

    }


    /*
  Metodo che:
    1.  Verifica se i dati anagrafici sono stati digitati dall'utente nelle varie View
    2a. Se digitati, li assegna alle variabili globali dell'activity host
    2b. Se non digitati, assegna il valore -1 alle variabili globali dell'activity host
 */
    private void getDatiAnagrafici() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in getDatiAnagrafici()");

    }


    /*
      Metodo che:
        1.  Verifica se altezza e peso sono stati digitati dall'utente nelle varie View
        2a. Se digitati, li assegna alle variabili globali dell'activity host
        2b. Se non digitati, assegna il valore -1 alle variabili globali dell'activity host
     */
    private void getPesoForma() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in getPesoForma()");

        // Assegno -1 se l'utente non ha ancora digitato il valore nella View di altezza o peso
        ((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm = !(mAltezzaEditText.getText().toString().equals("")) ? Integer.parseInt(mAltezzaEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mPesoKg = !(mPesoEditText.getText().toString().equals("")) ? Float.parseFloat(mPesoEditText.getText().toString()) : -1;

    }


    /*
      Metodo che:
        1.  Verifica se i tre valori delle pliche sono stati digitati dall'utente nelle varie View
        2a. Se digitati, li assegna alle variabili globali dell'activity host
        2b. Se non digitati, assegna il valore -1 alle variabili globali dell'activity host
     */
    private void getPliche() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in getPliche()");

        ((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheBraccio = !(mPlicheBraccioEditText.getText().toString().equals("")) ? Float.parseFloat(mPlicheBraccioEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheGirovita = !(mPlicheGirovitaEditText.getText().toString().equals("")) ? Float.parseFloat(mPlicheGirovitaEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheSchiena = !(mPlicheSchienaEditText.getText().toString().equals("")) ? Float.parseFloat(mPlicheSchienaEditText.getText().toString()) : -1;

    }


    /*
      Metodo che:
        1.  Verifica se i cinque valori delle circonferenze sono stati digitati dall'utente nelle varie View
        2a. Se digitati, li assegna alle variabili globali dell'activity host
        2b. Se non digitati, assegna il valore -1 alle variabili globali dell'activity host
     */
    private void getCirconferenza() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in getCirconferenza()");

        ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaAddome = !(mCirconferenzaAddomeEditText.getText().toString().equals("")) ? Float.parseFloat(mCirconferenzaAddomeEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaBraccio = !(mCirconferenzaBraccioEditText.getText().toString().equals("")) ? Float.parseFloat(mCirconferenzaBraccioEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaCoscia = !(mCirconferenzaCosciaEditText.getText().toString().equals("")) ? Float.parseFloat(mCirconferenzaCosciaEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaFianchi = !(mCirconferenzaFianchiEditText.getText().toString().equals("")) ? Float.parseFloat(mCirconferenzaFianchiEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaPolso = !(mCirconferenzaPolsoEditText.getText().toString().equals("")) ? Float.parseFloat(mCirconferenzaPolsoEditText.getText().toString()) : -1;

    }


    /*
       Metodo necessario quando si clicca su 'Calcola' dopo aver inserito un valore errato di un
       parametro. Va a cancellare il valore inserito.
     */
    private void checkUltimoParametroModificato(String parametro) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in checkUltimoParametroModificato()");

        switch (parametro) {

            case EDIT_TEXT_ALTEZZA:
                int altezzaPaziente = Integer.parseInt(mAltezzaEditText.getText().toString());
                if (altezzaPaziente < ALTEZZA_MIN || altezzaPaziente > ALTEZZA_MAX)
                    mAltezzaEditText.setText("");
                break;

            case EDIT_TEXT_PESO:
                float pesoPaziente = Float.parseFloat(mPesoEditText.getText().toString());
                if (pesoPaziente < PESO_MIN || pesoPaziente > PESO_MAX)
                    mPesoEditText.setText("");
                break;

            case EDIT_TEXT_PLICHE_GIROVITA:
                float plicheGirovitaPaziente = Float.parseFloat(mPlicheGirovitaEditText.getText().toString());
                if (plicheGirovitaPaziente < UNO_MIN || plicheGirovitaPaziente > PLICHE_GIROVITA_MAX)
                    mPlicheGirovitaEditText.setText("");
                break;

            case EDIT_TEXT_PLICHE_SCHIENA:
                float plicheSchienaPaziente = Float.parseFloat(mPlicheSchienaEditText.getText().toString());
                if (plicheSchienaPaziente < UNO_MIN || plicheSchienaPaziente > PLICHE_SCHIENA_MAX)
                    mPlicheSchienaEditText.setText("");
                break;

            case EDIT_TEXT_PLICHE_BRACCIO:
                float plicheBraccioPaziente = Float.parseFloat(mPlicheBraccioEditText.getText().toString());
                if (plicheBraccioPaziente < UNO_MIN || plicheBraccioPaziente > PLICHE_BRACCIO_MAX)
                    mPlicheBraccioEditText.setText("");
                break;

            case EDIT_TEXT_CIRCONFERENZA_ADDOME:
                float circonferenzaAddomePaziente = Float.parseFloat(mCirconferenzaAddomeEditText.getText().toString());
                if (circonferenzaAddomePaziente < UNO_MIN || circonferenzaAddomePaziente > CIRCONFERENZA_ADDOME_MAX)
                    mCirconferenzaAddomeEditText.setText("");
                break;

            case EDIT_TEXT_CIRCONFERENZA_FIANCHI:
                float circonferenzaFianchiPaziente = Float.parseFloat(mCirconferenzaFianchiEditText.getText().toString());
                if (circonferenzaFianchiPaziente < UNO_MIN || circonferenzaFianchiPaziente > CIRCONFERENZA_FIANCHI_MAX)
                    mCirconferenzaFianchiEditText.setText("");
                break;

            case EDIT_TEXT_CIRCONFERENZA_COSCIA:
                float circonferenzaCosciaPaziente = Float.parseFloat(mCirconferenzaCosciaEditText.getText().toString());
                if (circonferenzaCosciaPaziente < UNO_MIN || circonferenzaCosciaPaziente > CIRCONFERENZA_COSCIA_MAX)
                    mCirconferenzaCosciaEditText.setText("");
                break;

            case EDIT_TEXT_CIRCONFERENZA_POLSO:
                float circonferenzaPolsoPaziente = Float.parseFloat(mCirconferenzaPolsoEditText.getText().toString());
                if (circonferenzaPolsoPaziente < UNO_MIN || circonferenzaPolsoPaziente > CIRCONFERENZA_POLSO_MAX)
                    mCirconferenzaPolsoEditText.setText("");
                break;

            case EDIT_TEXT_CIRCONFERENZA_BRACCIO:
                float circonferenzaBraccioPaziente = Float.parseFloat(mCirconferenzaBraccioEditText.getText().toString());
                if (circonferenzaBraccioPaziente < UNO_MIN || circonferenzaBraccioPaziente > CIRCONFERENZA_BRACCIO_MAX)
                    mCirconferenzaBraccioEditText.setText("");
                break;

            default:
                //Toast.makeText(getActivity(), "Nessun parametro modificato", Toast.LENGTH_SHORT).show();
        }

    }

}
