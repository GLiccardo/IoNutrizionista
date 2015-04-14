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
    private static final float ZERO_MIN = 0;
    private static final int ALTEZZA_MIN = 80;                      // cm
    private static final int ALTEZZA_MAX = 220;                     // cm
    private static final float PESO_MIN = 25;                       // kg
    private static final float PESO_MAX = 250;                      // kg
    private static final float PLICHE_GIROVITA_MAX = 9999;          // mm
    private static final float PLICHE_SCHIENA_MAX = 9999;           // mm
    private static final float PLICHE_BRACCIO_MAX = 9999;           // mm
    private static final float CIRCONFERENZA_ADDOME_MAX = 9999;     // mm
    private static final float CIRCONFERENZA_FIANCHI_MAX = 9999;    // mm
    private static final float CIRCONFERENZA_COSCIA_MAX = 9999;     // mm
    private static final float CIRCONFERENZA_POLSO_MAX = 9999;      // mm
    private static final float CIRCONFERENZA_BRACCIO_MAX = 9999;    // mm

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
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onActivityCreated()");

        // Ottengo un riferimento alle Views della UI
        findViewsById();

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onResume()");

        checkValoriCorretti();

    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onStop()");

        // ((CalcoloValoriEnergeticiActivityv2) getActivity()).provaValoriEnergetici2 = 21;

        // Rilevo i parametri e i valori del paziente
        //getDatiAnagrafici();
        getPesoForma();
        getPliche();
        getCirconferenza();

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
    private void checkValoriCorretti() {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in checkValoriCorretti()");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Attenzione!!!");

        // Altezza: 80 - 220 cm
        mAltezzaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mAltezzaEditText.getText().toString().equals(""))) {
                    int altezzaPaziente = Integer.parseInt(mAltezzaEditText.getText().toString());
                    if (altezzaPaziente < ALTEZZA_MIN || altezzaPaziente > ALTEZZA_MAX) {
                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + ALTEZZA_MIN + " - " + ALTEZZA_MAX + " cm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();

                        // Reset "Altezza" field
                        mAltezzaEditText.setText("");
                    }
                }
            }
        });

        // Peso: 25 - 250 kg
        mPesoEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mPesoEditText.getText().toString().equals(""))) {
                    float pesoPaziente = Float.parseFloat(mPesoEditText.getText().toString());
                    if (pesoPaziente < PESO_MIN || pesoPaziente > PESO_MAX) {
                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + PESO_MIN + " - " + PESO_MAX + " kg\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();

                        // Reset "Peso" field
                        mPesoEditText.setText("");
                    }
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Pliche Girovita: 0 - 9999 mm
        mPlicheGirovitaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mPlicheGirovitaEditText.getText().toString().equals(""))) {
                    float plicheGirovitaPaziente = Float.parseFloat(mPlicheGirovitaEditText.getText().toString());
                    if (plicheGirovitaPaziente < ZERO_MIN || plicheGirovitaPaziente > PLICHE_GIROVITA_MAX) {
                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + ZERO_MIN + " - " + PLICHE_GIROVITA_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();

                        // Reset "Pliche Girovita" field
                        mPlicheGirovitaEditText.setText("");
                    }
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Pliche Schiena: 0 - 9999 mm
        mPlicheSchienaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mPlicheSchienaEditText.getText().toString().equals(""))) {
                    float plicheSchienaPaziente = Float.parseFloat(mPlicheSchienaEditText.getText().toString());
                    if (plicheSchienaPaziente < ZERO_MIN || plicheSchienaPaziente > PLICHE_SCHIENA_MAX) {
                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + ZERO_MIN + " - " + PLICHE_SCHIENA_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();

                        // Reset "Pliche Schiena" field
                        mPlicheSchienaEditText.setText("");
                    }
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Pliche Braccio: 0 - 9999 mm
        mPlicheBraccioEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mPlicheBraccioEditText.getText().toString().equals(""))) {
                    float plicheBraccioPaziente = Float.parseFloat(mPlicheBraccioEditText.getText().toString());
                    if (plicheBraccioPaziente < ZERO_MIN || plicheBraccioPaziente > PLICHE_BRACCIO_MAX) {
                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + ZERO_MIN + " - " + PLICHE_BRACCIO_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();

                        // Reset "Pliche Braccio" field
                        mPlicheBraccioEditText.setText("");
                    }
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Circonferenza Addome: 0 - 9999 mm
        mCirconferenzaAddomeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaAddomeEditText.getText().toString().equals(""))) {
                    float circonferenzaAddomePaziente = Float.parseFloat(mCirconferenzaAddomeEditText.getText().toString());
                    if (circonferenzaAddomePaziente < ZERO_MIN || circonferenzaAddomePaziente > CIRCONFERENZA_ADDOME_MAX) {
                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + ZERO_MIN + " - " + CIRCONFERENZA_ADDOME_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();

                        // Reset "Pliche Braccio" field
                        mCirconferenzaAddomeEditText.setText("");
                    }
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Circonferenza Fianchi: 0 - 9999 mm
        mCirconferenzaFianchiEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaFianchiEditText.getText().toString().equals(""))) {
                    float circonferenzaFianchiPaziente = Float.parseFloat(mCirconferenzaFianchiEditText.getText().toString());
                    if (circonferenzaFianchiPaziente < ZERO_MIN || circonferenzaFianchiPaziente > CIRCONFERENZA_FIANCHI_MAX) {
                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + ZERO_MIN + " - " + CIRCONFERENZA_FIANCHI_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();

                        // Reset "Pliche Braccio" field
                        mCirconferenzaFianchiEditText.setText("");
                    }
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Circonferenza Coscia: 0 - 9999 mm
        mCirconferenzaCosciaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaCosciaEditText.getText().toString().equals(""))) {
                    float circonferenzaCosciaPaziente = Float.parseFloat(mCirconferenzaCosciaEditText.getText().toString());
                    if (circonferenzaCosciaPaziente < ZERO_MIN || circonferenzaCosciaPaziente > CIRCONFERENZA_COSCIA_MAX) {
                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + ZERO_MIN + " - " + CIRCONFERENZA_COSCIA_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();

                        // Reset "Pliche Braccio" field
                        mCirconferenzaCosciaEditText.setText("");
                    }
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Circonferenza Polso: 0 - 9999 mm
        mCirconferenzaPolsoEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaPolsoEditText.getText().toString().equals(""))) {
                    float circonferenzaPolsoPaziente = Float.parseFloat(mCirconferenzaPolsoEditText.getText().toString());
                    if (circonferenzaPolsoPaziente < ZERO_MIN || circonferenzaPolsoPaziente > CIRCONFERENZA_POLSO_MAX) {
                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + ZERO_MIN + " - " + CIRCONFERENZA_POLSO_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();

                        // Reset "Pliche Braccio" field
                        mCirconferenzaPolsoEditText.setText("");
                    }
                }
            }
        });

        // TODO: Aggiornare range valori consentiti
        // Circonferenza Braccio: 0 - 9999 mm
        mCirconferenzaBraccioEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCirconferenzaBraccioEditText.getText().toString().equals(""))) {
                    float circonferenzaBraccioPaziente = Float.parseFloat(mCirconferenzaBraccioEditText.getText().toString());
                    if (circonferenzaBraccioPaziente < ZERO_MIN || circonferenzaBraccioPaziente > CIRCONFERENZA_BRACCIO_MAX) {
                        // Show alert
                        builder.setMessage("Valori consentiti:\n\n" + ZERO_MIN + " - " + CIRCONFERENZA_BRACCIO_MAX + " mm\n\nReinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();

                        // Reset "Pliche Braccio" field
                        mCirconferenzaBraccioEditText.setText("");
                    }
                }
            }
        });

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
        // TODO: altezzaM magari lo mettiamo dove serve, ad esempio nel calcolo del BMI
        ((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaM = (float) ((CalcoloValoriEnergeticiActivity) getActivity()).mAltezzaCm / 100;
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

}
