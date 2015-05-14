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

    private EditText mAltezzaEditText;
    private EditText mPesoEditText;
    private EditText mPlicaGirovitaEditText;
    private EditText mPlicaSchienaEditText;
    private EditText mPlicaBraccioEditText;
    private EditText mCirconferenzaAddomeEditText;
    private EditText mCirconferenzaFianchiEditText;
    private EditText mCirconferenzaCosciaEditText;
    private EditText mCirconferenzaPolsoEditText;
    private EditText mCirconferenzaBraccioEditText;
    private EditText mCirconferenzaColloEditText;

    // TODO: Verificare se le pliche sono valori interi
    // Esternalizzazione valori
    private static final float UNO_MIN = 1;
    private static final int ALTEZZA_MIN = 70;                  // cm
    private static final int ALTEZZA_MAX = 220;                 // cm
    private static final float PESO_MIN = 25;                   // kg
    private static final float PESO_MAX = 250;                  // kg
    private static final float PLICHE_GIROVITA_MAX = 70;        // mm
    private static final float PLICHE_SCHIENA_MAX = 70;         // mm
    private static final float PLICHE_BRACCIO_MAX = 70;         // mm
    private static final float CIRCONFERENZA_ADDOME_MIN = 50;   // cm
    private static final float CIRCONFERENZA_ADDOME_MAX = 180;  // cm
    private static final float CIRCONFERENZA_FIANCHI_MIN = 50;  // cm
    private static final float CIRCONFERENZA_FIANCHI_MAX = 160; // cm
    private static final float CIRCONFERENZA_COSCIA_MIN = 20;   // cm
    private static final float CIRCONFERENZA_COSCIA_MAX = 140;  // cm
    private static final float CIRCONFERENZA_POLSO_MIN = 10;    // cm
    private static final float CIRCONFERENZA_POLSO_MAX = 30;    // cm
    private static final float CIRCONFERENZA_BRACCIO_MIN = 10;  // cm
    private static final float CIRCONFERENZA_BRACCIO_MAX = 60;  // cm
    private static final float CIRCONFERENZA_COLLO_MIN = 20;    // cm
    private static final float CIRCONFERENZA_COLLO_MAX = 70;    // cm

    // Esternalizzazione stringhe
    private static final String EDIT_TEXT_ALTEZZA = "Altezza";
    private static final String EDIT_TEXT_PESO = "Peso";
    private static final String EDIT_TEXT_PLICHE_GIROVITA = "Plica Girovita";
    private static final String EDIT_TEXT_PLICHE_SCHIENA = "Plica Schiena";
    private static final String EDIT_TEXT_PLICHE_BRACCIO = "Plica Braccio";
    private static final String EDIT_TEXT_CIRCONFERENZA_ADDOME = "Circonferenza Addome";
    private static final String EDIT_TEXT_CIRCONFERENZA_FIANCHI = "Circonferenza Fianchi";
    private static final String EDIT_TEXT_CIRCONFERENZA_COSCIA = "Circonferenza Coscia";
    private static final String EDIT_TEXT_CIRCONFERENZA_POLSO = "Circonferenza Polso";
    private static final String EDIT_TEXT_CIRCONFERENZA_BRACCIO = "Circonferenza Braccio";
    private static final String EDIT_TEXT_CIRCONFERENZA_COLLO = "Circonferenza Collo";


    /*
      Costruttore
     */
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
        // Setto gli hint per gli EditText dei vari parametri
        setHintEditText();
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

        // Recupero i valori dei parametri inseriti
        getPesoForma();
        getPliche();
        getCirconferenze();

        // Verifico se tutti i parametri sono stati inseriti:
        // - [caso positivo] setto il flag a true
        // - [caso negativo] setto il flag a false
        ((CalcoloValoriEnergeticiActivity) getActivity()).flagParamMisurazioniInseriti = checkInserimentoParametri();

    }


    /*
      Metodo che consente di ottenere i reference per tutte le View che verranno utilizzate
     */
    private void findViewsById() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in findViewsById()");

        try {
            mAltezzaEditText = (EditText) getView().findViewById(R.id.edit_text_altezza);
            mPesoEditText = (EditText) getView().findViewById(R.id.edit_text_peso);
            mPlicaGirovitaEditText = (EditText) getView().findViewById(R.id.edit_text_pliche_girovita);
            mPlicaSchienaEditText = (EditText) getView().findViewById(R.id.edit_text_pliche_schiena);
            mPlicaBraccioEditText = (EditText) getView().findViewById(R.id.edit_text_pliche_braccio);
            mCirconferenzaAddomeEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_addome);
            mCirconferenzaFianchiEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_fianchi);
            mCirconferenzaCosciaEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_coscia);
            mCirconferenzaPolsoEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_polso);
            mCirconferenzaBraccioEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_braccio);
            mCirconferenzaColloEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_collo);
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

    }


    /*
      Metodo che consente di dichiarare gli hint per gli EditText di ciascun parametro
     */
    private void setHintEditText() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in setHintEditText()");

        mAltezzaEditText.setHint(ALTEZZA_MIN + " - " + ALTEZZA_MAX);
        mAltezzaEditText.setHint(ALTEZZA_MIN + " - " + ALTEZZA_MAX);
        mPesoEditText.setHint(PESO_MIN + " - " + PESO_MAX);
        mPlicaGirovitaEditText.setHint(UNO_MIN + " - " + PLICHE_GIROVITA_MAX);
        mPlicaSchienaEditText.setHint(UNO_MIN + " - " + PLICHE_SCHIENA_MAX);
        mPlicaBraccioEditText.setHint(UNO_MIN + " - " + PLICHE_BRACCIO_MAX);
        mCirconferenzaAddomeEditText.setHint(CIRCONFERENZA_ADDOME_MIN + " - " + CIRCONFERENZA_ADDOME_MAX);
        mCirconferenzaFianchiEditText.setHint(CIRCONFERENZA_FIANCHI_MIN + " - " + CIRCONFERENZA_FIANCHI_MAX);
        mCirconferenzaCosciaEditText.setHint(CIRCONFERENZA_COSCIA_MIN + " - " + CIRCONFERENZA_COSCIA_MAX);
        mCirconferenzaPolsoEditText.setHint(CIRCONFERENZA_POLSO_MIN + " - " + CIRCONFERENZA_POLSO_MAX);
        mCirconferenzaBraccioEditText.setHint(CIRCONFERENZA_BRACCIO_MIN + " - " + CIRCONFERENZA_BRACCIO_MAX);
        mCirconferenzaColloEditText.setHint(CIRCONFERENZA_COLLO_MIN + " - " + CIRCONFERENZA_COLLO_MAX);
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

        // Plica Girovita: 1 - 70 mm
        mPlicaGirovitaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mPlicaGirovitaEditText.getText().toString().equals(""))) {
                    float plicheGirovitaPaziente = Float.parseFloat(mPlicaGirovitaEditText.getText().toString());
                    if (plicheGirovitaPaziente < UNO_MIN || plicheGirovitaPaziente > PLICHE_GIROVITA_MAX) {
                        // Reset "Plica Girovita" field
                        mPlicaGirovitaEditText.setText("");

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

        // Plica Schiena: 1 - 70 mm
        mPlicaSchienaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mPlicaSchienaEditText.getText().toString().equals(""))) {
                    float plicheSchienaPaziente = Float.parseFloat(mPlicaSchienaEditText.getText().toString());
                    if (plicheSchienaPaziente < UNO_MIN || plicheSchienaPaziente > PLICHE_SCHIENA_MAX) {
                        // Reset "Plica Schiena" field
                        mPlicaSchienaEditText.setText("");

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

        // Plica Braccio: 1 - 70 mm
        mPlicaBraccioEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mPlicaBraccioEditText.getText().toString().equals(""))) {
                    float plicheBraccioPaziente = Float.parseFloat(mPlicaBraccioEditText.getText().toString());
                    if (plicheBraccioPaziente < UNO_MIN || plicheBraccioPaziente > PLICHE_BRACCIO_MAX) {
                        // Reset "Plica Braccio" field
                        mPlicaBraccioEditText.setText("");

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

        // Circonferenza Addome: 50 - 180 cm
        mCirconferenzaAddomeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaAddomeEditText.getText().toString().equals(""))) {
                    float circonferenzaAddomePaziente = Float.parseFloat(mCirconferenzaAddomeEditText.getText().toString());
                    if (circonferenzaAddomePaziente < CIRCONFERENZA_ADDOME_MIN || circonferenzaAddomePaziente > CIRCONFERENZA_ADDOME_MAX) {
                        // Reset "Circonferenza Addome" field
                        mCirconferenzaAddomeEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + CIRCONFERENZA_ADDOME_MIN + " - " + CIRCONFERENZA_ADDOME_MAX + " mm\n\nReinserire valore corretto");
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

        // Circonferenza Fianchi: 70 - 150 cm
        mCirconferenzaFianchiEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaFianchiEditText.getText().toString().equals(""))) {
                    float circonferenzaFianchiPaziente = Float.parseFloat(mCirconferenzaFianchiEditText.getText().toString());
                    if (circonferenzaFianchiPaziente < CIRCONFERENZA_FIANCHI_MIN || circonferenzaFianchiPaziente > CIRCONFERENZA_FIANCHI_MAX) {
                        // Reset "Circonferenza Fianchi" field
                        mCirconferenzaFianchiEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + CIRCONFERENZA_FIANCHI_MIN + " - " + CIRCONFERENZA_FIANCHI_MAX + " mm\n\nReinserire valore corretto");
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

        // Circonferenza Collo: 30 - 70 cm
        mCirconferenzaColloEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaColloEditText.getText().toString().equals(""))) {
                    float circonferenzaColloPaziente = Float.parseFloat(mCirconferenzaColloEditText.getText().toString());
                    if (circonferenzaColloPaziente < CIRCONFERENZA_COLLO_MIN || circonferenzaColloPaziente > CIRCONFERENZA_COLLO_MAX) {
                        // Reset "Circonferenza Collo" field
                        mCirconferenzaColloEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + CIRCONFERENZA_COLLO_MIN + " - " + CIRCONFERENZA_COLLO_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_CIRCONFERENZA_COLLO;
                }
            }
        });

        // Circonferenza Coscia: 20 - 80 cm
        mCirconferenzaCosciaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaCosciaEditText.getText().toString().equals(""))) {
                    float circonferenzaCosciaPaziente = Float.parseFloat(mCirconferenzaCosciaEditText.getText().toString());
                    if (circonferenzaCosciaPaziente < CIRCONFERENZA_COSCIA_MIN || circonferenzaCosciaPaziente > CIRCONFERENZA_COSCIA_MAX) {
                        // Reset "Circonferenza Coscia" field
                        mCirconferenzaCosciaEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + CIRCONFERENZA_COSCIA_MIN + " - " + CIRCONFERENZA_COSCIA_MAX + " mm\n\nReinserire valore corretto");
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

        // Circonferenza Polso: 10 - 26 cm
        mCirconferenzaPolsoEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaPolsoEditText.getText().toString().equals(""))) {
                    float circonferenzaPolsoPaziente = Float.parseFloat(mCirconferenzaPolsoEditText.getText().toString());
                    if (circonferenzaPolsoPaziente < CIRCONFERENZA_POLSO_MIN || circonferenzaPolsoPaziente > CIRCONFERENZA_POLSO_MAX) {
                        // Reset "Circonferenza Polso" field
                        mCirconferenzaPolsoEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + CIRCONFERENZA_POLSO_MIN + " - " + CIRCONFERENZA_POLSO_MAX + " mm\n\nReinserire valore corretto");
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

        // Circonferenza Braccio: 20 - 50 cm
        mCirconferenzaBraccioEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaBraccioEditText.getText().toString().equals(""))) {
                    float circonferenzaBraccioPaziente = Float.parseFloat(mCirconferenzaBraccioEditText.getText().toString());
                    if (circonferenzaBraccioPaziente < CIRCONFERENZA_BRACCIO_MIN || circonferenzaBraccioPaziente > CIRCONFERENZA_BRACCIO_MAX) {
                        // Reset "Circonferenza Braccio" field
                        mCirconferenzaBraccioEditText.setText("");

                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + CIRCONFERENZA_BRACCIO_MIN + " - " + CIRCONFERENZA_BRACCIO_MAX + " mm\n\nReinserire valore corretto");
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

    /*
      Metodo che viene invocato in "checkInserimentoParametri()".
      Serve per verificare se l'elemento è stato inserito.
      - Ritorna TRUE se è stato inserito (!= -1)
      - Ritorna FALSE se non è stato inserito (== -1)
     */
    private boolean checkInserimentoSingoloParametro(int valoreInt) {
        return (valoreInt != -1);
    }

    private boolean checkInserimentoSingoloParametro(float valoreFloat) {
        return (valoreFloat != -1);
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

        boolean checkAltezza = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm);
        if (!checkAltezza) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nAltezza");

        boolean checkPeso = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mPesoKg);
        if (!checkPeso) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nPeso");

        boolean checkPlicheGirovita = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheGirovita);
        if (!checkPlicheGirovita) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nPlica Girovita");

        boolean checkPlicheSchiena = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheSchiena);
        if (!checkPlicheSchiena) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nPlica Schiena");

        boolean checkPlicheBraccio = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheBraccio);
        if (!checkPlicheBraccio) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nPlica Braccio");

        boolean checkCirconferenzaAddome = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaGirovita);
        if (!checkCirconferenzaAddome) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nCirconferenza Addome");

        boolean checkCirconferenzaFianchi = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaFianchi);
        if (!checkCirconferenzaFianchi) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nCirconferenza Fianchi");

        boolean checkCirconferenzaCollo = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaCollo);
        if (!checkCirconferenzaCollo) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nCirconferenza Collo");

        boolean checkCirconferenzaCoscia = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaCoscia);
        if (!checkCirconferenzaCoscia) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nCirconferenza Coscia");

        boolean checkCirconferenzaPolso = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaPolso);
        if (!checkCirconferenzaPolso) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nCirconferenza Polso");

        boolean checkCirconferenzaBraccio = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaBraccio);
        if (!checkCirconferenzaBraccio) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nCirconferenza Braccio");

        // Ritorna TRUE se tutti i valori sono stati inseriti, altrimenti FALSE
        return ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.toString().equals("");
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

        ((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheBraccio = !(mPlicaBraccioEditText.getText().toString().equals("")) ? Float.parseFloat(mPlicaBraccioEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheGirovita = !(mPlicaGirovitaEditText.getText().toString().equals("")) ? Float.parseFloat(mPlicaGirovitaEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mPlicheSchiena = !(mPlicaSchienaEditText.getText().toString().equals("")) ? Float.parseFloat(mPlicaSchienaEditText.getText().toString()) : -1;
    }


    /*
      Metodo che:
        1.  Verifica se i cinque valori delle circonferenze sono stati digitati dall'utente nelle varie View
        2a. Se digitati, li assegna alle variabili globali dell'activity host
        2b. Se non digitati, assegna il valore -1 alle variabili globali dell'activity host
     */
    private void getCirconferenze() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in getCirconferenze()");

        ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaGirovita = !(mCirconferenzaAddomeEditText.getText().toString().equals("")) ? Float.parseFloat(mCirconferenzaAddomeEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaBraccio = !(mCirconferenzaBraccioEditText.getText().toString().equals("")) ? Float.parseFloat(mCirconferenzaBraccioEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaCoscia = !(mCirconferenzaCosciaEditText.getText().toString().equals("")) ? Float.parseFloat(mCirconferenzaCosciaEditText.getText().toString()) : -1;
        ((CalcoloValoriEnergeticiActivity) getActivity()).mCirconferenzaCollo = !(mCirconferenzaColloEditText.getText().toString().equals("")) ? Float.parseFloat(mCirconferenzaColloEditText.getText().toString()) : -1;
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
                if (!mAltezzaEditText.getText().toString().equals("")) {
                    int altezzaPaziente = Integer.parseInt(mAltezzaEditText.getText().toString());
                    if (altezzaPaziente < ALTEZZA_MIN || altezzaPaziente > ALTEZZA_MAX)
                        mAltezzaEditText.setText("");
                }
                break;

            case EDIT_TEXT_PESO:
                if (!mPesoEditText.getText().toString().equals("")) {
                    float pesoPaziente = Float.parseFloat(mPesoEditText.getText().toString());
                    if (pesoPaziente < PESO_MIN || pesoPaziente > PESO_MAX)
                        mPesoEditText.setText("");
                }
                break;

            case EDIT_TEXT_PLICHE_GIROVITA:
                if (!mPlicaGirovitaEditText.getText().toString().equals("")) {
                    float plicheGirovitaPaziente = Float.parseFloat(mPlicaGirovitaEditText.getText().toString());
                    if (plicheGirovitaPaziente < UNO_MIN || plicheGirovitaPaziente > PLICHE_GIROVITA_MAX)
                        mPlicaGirovitaEditText.setText("");
                }
                break;

            case EDIT_TEXT_PLICHE_SCHIENA:
                if (!mPlicaSchienaEditText.getText().toString().equals("")) {
                    float plicheSchienaPaziente = Float.parseFloat(mPlicaSchienaEditText.getText().toString());
                    if (plicheSchienaPaziente < UNO_MIN || plicheSchienaPaziente > PLICHE_SCHIENA_MAX)
                        mPlicaSchienaEditText.setText("");
                }
                break;

            case EDIT_TEXT_PLICHE_BRACCIO:
                if (!mPlicaBraccioEditText.getText().toString().equals("")) {
                    float plicheBraccioPaziente = Float.parseFloat(mPlicaBraccioEditText.getText().toString());
                    if (plicheBraccioPaziente < UNO_MIN || plicheBraccioPaziente > PLICHE_BRACCIO_MAX)
                        mPlicaBraccioEditText.setText("");
                }
                break;

            case EDIT_TEXT_CIRCONFERENZA_ADDOME:
                if (!mCirconferenzaAddomeEditText.getText().toString().equals("")) {
                    float circonferenzaAddomePaziente = Float.parseFloat(mCirconferenzaAddomeEditText.getText().toString());
                    if (circonferenzaAddomePaziente < CIRCONFERENZA_ADDOME_MIN || circonferenzaAddomePaziente > CIRCONFERENZA_ADDOME_MAX)
                        mCirconferenzaAddomeEditText.setText("");
                }
                break;

            case EDIT_TEXT_CIRCONFERENZA_FIANCHI:
                if (!mCirconferenzaFianchiEditText.getText().toString().equals("")) {
                    float circonferenzaFianchiPaziente = Float.parseFloat(mCirconferenzaFianchiEditText.getText().toString());
                    if (circonferenzaFianchiPaziente < CIRCONFERENZA_FIANCHI_MIN || circonferenzaFianchiPaziente > CIRCONFERENZA_FIANCHI_MAX)
                        mCirconferenzaFianchiEditText.setText("");
                }
                break;

            case EDIT_TEXT_CIRCONFERENZA_COLLO:
                if (!mCirconferenzaColloEditText.getText().toString().equals("")) {
                    float circonferenzaColloPaziente = Float.parseFloat(mCirconferenzaColloEditText.getText().toString());
                    if (circonferenzaColloPaziente < CIRCONFERENZA_COLLO_MIN || circonferenzaColloPaziente > CIRCONFERENZA_COSCIA_MAX)
                        mCirconferenzaColloEditText.setText("");
                }
                break;

            case EDIT_TEXT_CIRCONFERENZA_COSCIA:
                if (!mCirconferenzaCosciaEditText.getText().toString().equals("")) {
                    float circonferenzaCosciaPaziente = Float.parseFloat(mCirconferenzaCosciaEditText.getText().toString());
                    if (circonferenzaCosciaPaziente < CIRCONFERENZA_COSCIA_MIN || circonferenzaCosciaPaziente > CIRCONFERENZA_COSCIA_MAX)
                        mCirconferenzaCosciaEditText.setText("");
                }
                break;

            case EDIT_TEXT_CIRCONFERENZA_POLSO:
                if (!mCirconferenzaPolsoEditText.getText().toString().equals("")) {
                    float circonferenzaPolsoPaziente = Float.parseFloat(mCirconferenzaPolsoEditText.getText().toString());
                    if (circonferenzaPolsoPaziente < CIRCONFERENZA_POLSO_MIN || circonferenzaPolsoPaziente > CIRCONFERENZA_POLSO_MAX)
                        mCirconferenzaPolsoEditText.setText("");
                }
                break;

            case EDIT_TEXT_CIRCONFERENZA_BRACCIO:
                if (!mCirconferenzaBraccioEditText.getText().toString().equals("")) {
                    float circonferenzaBraccioPaziente = Float.parseFloat(mCirconferenzaBraccioEditText.getText().toString());
                    if (circonferenzaBraccioPaziente < CIRCONFERENZA_BRACCIO_MIN || circonferenzaBraccioPaziente > CIRCONFERENZA_BRACCIO_MAX)
                        mCirconferenzaBraccioEditText.setText("");
                }
                break;

            default:
                //Toast.makeText(getActivity(), "Nessun parametro modificato", Toast.LENGTH_SHORT).show();
        }

    }

}
