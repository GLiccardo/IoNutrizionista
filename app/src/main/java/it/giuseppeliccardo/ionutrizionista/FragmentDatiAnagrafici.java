package it.giuseppeliccardo.ionutrizionista;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class FragmentDatiAnagrafici extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "ioNutrizionista";

    private String mUltimoParametroModificato;
    private String mElementoSpinnerSelezionato;

    private boolean mCheckNome;
    private boolean mCheckCognome;
    private boolean mCheckDataDiNascita;
    private boolean mCheckOccupazione;

    private EditText mNomeEditText;
    private EditText mCognomeEditText;
    private EditText mDataDiNascitaEditText;
    private RadioGroup mSessoRadioGroup;
    private RadioButton mSessoRadioButton;
    private Spinner mOccupazioneSpinner;
    private EditText mTelefonoFissoEditText;
    private EditText mCellulareEditText;
    private EditText mEmailEditText;

    private DatePickerDialog mEtaPickerDialog;
    private SimpleDateFormat mDateFormatter;
    private AlertDialog.Builder mBuilder;

    // Esternalizzazione stringhe
    private static final String EDIT_TEXT_DATA_DI_NASCITA = "Data di Nascita";
    private static final String EDIT_TEXT_TELEFONO_FISSO = "Telefono Fisso";
    private static final String EDIT_TEXT_CELLULARE = "Cellulare";
    private static final String EDIT_TEXT_EMAIL = "Email";


    /*
      Costruttore
     */
    public FragmentDatiAnagrafici() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_dati_anagrafici, container, false);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onActivityCreated()");

        // Ottengo un riferimento alle Views della UI
        findViewsById();

        // Inizializzo Spinner delle occupazioni
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.array_occupazioni, R.layout.spinner_item); //android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOccupazioneSpinner.setAdapter(adapter);
        // Imposto un listener per ottenere l'elemento selezionato nello spinner
        mOccupazioneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mElementoSpinnerSelezionato = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        // Caso particolare: si clicca su "Calcola" prima di aver aperto il fragment "Misurazioni"
        // In questo caso, i parametri mancanti di "Misurazioni" sarebbero null, ecco perchè è
        // necessario inizializzarlo in questo modo
        ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni = new StringBuilder("");
        ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiMisurazioni.append("\nAltezza\nPeso\nPliche\nCirconferenze");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onResume()");

        // TODO: Cambiare colore linea orizzontale edit text

        // Setto i filtri per gli EditText
        setFiltriEditText();

        // Mostro il DatePicker se clicco sulla EditText relativa all'età
        mostraDatePickerAndCheckValoreData();

        // Controllo se i valori dei parametri inseriti sono corretti
        checkValoriParametri();
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onPause()");

        // Cancello un eventuale valore errato della data inserita
        checkUltimoParametroModificato(mUltimoParametroModificato);

        // Recupero i valori dei parametri inseriti
        getDatiAnagrafici();

        // Verifico se tutti i parametri sono stati inseriti:
        // - [caso positivo] setto il flag a true
        // - [caso negativo] setto il flag a false
        if (checkInserimentoParametri()) {
            ((CalcoloValoriEnergeticiActivity) getActivity()).flagParamAnagraficiInseriti= true;
        } else {
            ((CalcoloValoriEnergeticiActivity) getActivity()).flagParamAnagraficiInseriti= false;
        }

        //Toast.makeText(getActivity(), mStringaToast, Toast.LENGTH_SHORT).show();
    }


    /*
      Metodo che consente di ottenere i reference per tutte le View che verranno utilizzate
     */
    private void findViewsById() {

        try {
            // Dati Anagrafici
            mNomeEditText = (EditText) getView().findViewById(R.id.edit_text_nome);
            mCognomeEditText = (EditText) getView().findViewById(R.id.edit_text_cognome);
            mDataDiNascitaEditText = (EditText) getView().findViewById(R.id.edit_text_eta);
            mSessoRadioGroup = (RadioGroup) getView().findViewById(R.id.radio_group_sesso);
            mOccupazioneSpinner = (Spinner) getView().findViewById(R.id.spinner_occupazione);

            // Contatti
            mTelefonoFissoEditText = (EditText) getView().findViewById(R.id.edit_text_telefono_fisso);
            mCellulareEditText = (EditText) getView().findViewById(R.id.edit_text_cellulare);
            mEmailEditText = (EditText) getView().findViewById(R.id.edit_text_email);

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

    }


    /*
      Metodo che fa in modo che gli EditText di nome e cognome accettino soltanto caratteri
     */
    private void setFiltriEditText(){
        Log.i(TAG, getClass().getSimpleName() + ": entrato in setFiltriEditText()");

        mNomeEditText.setFilters(new InputFilter[] {
                new InputFilter() {
                    public CharSequence filter (CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
                        if (src.equals(" ")) { // per accettare il carattere "backspace"
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z]+")){
                            return src;
                        }
                        return "";
                    }
                }
        });

        mCognomeEditText.setFilters(new InputFilter[] {
                new InputFilter() {
                    public CharSequence filter (CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
                        if (src.equals(" ")) { // per accettare il carattere "backspace"
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z]+")){
                            return src;
                        }
                        return "";
                    }
                }
        });

    }


    /*
      Metodo che consente la visualizzazione di un DatePicker quando si effettua un clic sulla
      EditText relativa alla data di nascita del paziente
     */
    private void mostraDatePickerAndCheckValoreData() {
        mUltimoParametroModificato = "";
        mDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        mDateFormatter.setLenient(false);

        mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Attenzione!!!");

        mDataDiNascitaEditText.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        // Gestione del primo tocco (onFocusChange) e di quelli successivi (onClick)
        mDataDiNascitaEditText.setOnFocusChangeListener(this);
        mDataDiNascitaEditText.setOnClickListener(this);

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // Quando il Dialog Box viene chiuso, viene invocato questo metodo
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar dataImpostata = Calendar.getInstance();
                dataImpostata.set(year, monthOfYear, dayOfMonth);
                mDataDiNascitaEditText.setText(mDateFormatter.format(dataImpostata.getTime()));
                mDataDiNascitaEditText.clearFocus();
            }
        };

        Calendar dataInizialeDatePicker = Calendar.getInstance(); // = data corrente
        mEtaPickerDialog = new DatePickerDialog(
                getActivity(),
                datePickerListener,
                dataInizialeDatePicker.get(Calendar.YEAR),
                dataInizialeDatePicker.get(Calendar.MONTH),
                dataInizialeDatePicker.get(Calendar.DAY_OF_MONTH));

    }


    /*
      Questo listener serve per gestire due situazioni:
        - la prima volta che si clicca sull'EditText della "data di nascita"
        - effettuare la validazione della data inserita
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {

            //Toast.makeText(getActivity(), "Primo click", Toast.LENGTH_SHORT).show();
            mUltimoParametroModificato = EDIT_TEXT_DATA_DI_NASCITA;
            mEtaPickerDialog.show();

        } else if (!hasFocus && !(mDataDiNascitaEditText.getText().toString().equals(""))) {

            String dateToValidate = mDataDiNascitaEditText.getText().toString();

            try {
                Date date = mDateFormatter.parse(dateToValidate);
            } catch (ParseException exc) {
                // Reset "Data di nascita" field
                mDataDiNascitaEditText.setText("");
                mDataDiNascitaEditText.clearFocus();

                // Show alert
                mBuilder.setMessage("Formato corretto:\n\ngg/mm/aaaa\n\nReinserire data corretta");
                mBuilder.setPositiveButton("OK", null);
                AlertDialog dialog = mBuilder.show();
                TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                dialog.show();
            }
        }
    }


    /*
      Questo listener serve per far apparire il DatePicker dal secondo clic in poi sull'EditText
      della "data di nascita"
     */
    @Override
    public void onClick(View v) {
        //Toast.makeText(getActivity(), "Click successivi", Toast.LENGTH_SHORT).show();
        mEtaPickerDialog.show();
    }


    /*
      Metodo che viene invocato in "checkInserimentoParametri()".
      Serve per verificare se l'elemento è stato inserito.
      Se è stato inserito allora ritorno true
      Se non è stato inserito (cioè == "-1") allora ritorna false
     */
    private boolean checkInserimentoSingoloParametro(String string) {
        boolean res = !(string.equals("-1"));
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

        ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiDatiAnagrafici = new StringBuilder("");

        mCheckNome = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mNome);
        if (!mCheckNome) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiDatiAnagrafici.append("\nNome");

        mCheckCognome = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mCognome);
        if (!mCheckCognome) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiDatiAnagrafici.append("\nCognome");

        mCheckDataDiNascita = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mDataDiNascita);
        if (!mCheckDataDiNascita) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiDatiAnagrafici.append("\nData di Nascita");

        mCheckOccupazione = checkInserimentoSingoloParametro(((CalcoloValoriEnergeticiActivity) getActivity()).mOccupazione);
        if (!mCheckOccupazione) ((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiDatiAnagrafici.append("\nOccupazione");


        if (((CalcoloValoriEnergeticiActivity) getActivity()).mParametriMancantiDatiAnagrafici.toString().equals("")) {
            // Tutti i valori sono stati inseriti
            return true;
        } else {
            //Toast.makeText(getActivity(), mStringaToast, Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    /*
      Metodo che effettua la validazione dell'email
     */
    private boolean emailValidation(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /*
      Metodo che effettua la validazione di un numero di telefono
     */
    private boolean phoneValidation(String phoneNumber) {
        return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
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

        mTelefonoFissoEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mTelefonoFissoEditText.getText().toString().equals(""))) {
                    String telefonoFisso = mTelefonoFissoEditText.getText().toString();
                    if (!hasFocus && !(telefonoFisso.equals(""))) {
                        if (!phoneValidation(telefonoFisso)) {
                            // Reset "Telefono Fisso" field
                            mTelefonoFissoEditText.setText("");

                            // Show alert
                            builder.setMessage("Numero di telefono errato.\n\n" + "Reinserire valore corretto");
                            builder.setPositiveButton("OK", null);
                            AlertDialog dialog = builder.show();
                            TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                            messageText.setGravity(Gravity.CENTER);
                            dialog.show();
                        }
                    } else if (hasFocus) {
                        mUltimoParametroModificato = EDIT_TEXT_TELEFONO_FISSO;
                    }
                }
            }
        });

        mCellulareEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(mCellulareEditText.getText().toString().equals(""))) {
                    String telefonoCellulare = mCellulareEditText.getText().toString();
                    if (!hasFocus && !(telefonoCellulare.equals(""))) {
                        if (!phoneValidation(telefonoCellulare)) {
                            // Reset "Cellulare" field
                            mCellulareEditText.setText("");

                            // Show alert
                            builder.setMessage("Numero di telefono errato.\n\n" + "Reinserire valore corretto");
                            builder.setPositiveButton("OK", null);
                            AlertDialog dialog = builder.show();
                            TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                            messageText.setGravity(Gravity.CENTER);
                            dialog.show();
                        }
                    } else if (hasFocus) {
                        mUltimoParametroModificato = EDIT_TEXT_CELLULARE;
                    }
                }
            }
        });

        mEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                String email = mEmailEditText.getText().toString();
                if (!hasFocus && !(email.equals(""))) {
                    if (!emailValidation(email)) {
                        // Reset "Email" field
                        mEmailEditText.setText("");

                        // Show alert
                        builder.setMessage("Email errata.\n\n" + "Reinserire valore corretto");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                } else if (hasFocus) {
                    mUltimoParametroModificato = EDIT_TEXT_EMAIL;
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

        // Verifico quale dei RadioButton è stato cliccato
        int radioButtonSelezionato = mSessoRadioGroup.getCheckedRadioButtonId();
        mSessoRadioButton = (RadioButton) getView().findViewById(radioButtonSelezionato);

        // Get Dati Anagrafici
        ((CalcoloValoriEnergeticiActivity) getActivity()).mNome = !(mNomeEditText.getText().toString().equals("")) ? mNomeEditText.getText().toString() : "-1";
        ((CalcoloValoriEnergeticiActivity) getActivity()).mCognome = !(mCognomeEditText.getText().toString().equals("")) ? mCognomeEditText.getText().toString() : "-1";
        ((CalcoloValoriEnergeticiActivity) getActivity()).mSesso = (mSessoRadioButton.getText().equals("M")) ? "M" : "F";
        ((CalcoloValoriEnergeticiActivity) getActivity()).mDataDiNascita = !(mDataDiNascitaEditText.getText().toString().equals("")) ? mDataDiNascitaEditText.getText().toString() : "-1";
        ((CalcoloValoriEnergeticiActivity) getActivity()).mOccupazione = !(mElementoSpinnerSelezionato.equals("")) ? mElementoSpinnerSelezionato : "-1";

                // Get Contatti
        ((CalcoloValoriEnergeticiActivity) getActivity()).mTelefonoFisso = !(mTelefonoFissoEditText.getText().toString().equals("")) ? mTelefonoFissoEditText.getText().toString() : "";
        ((CalcoloValoriEnergeticiActivity) getActivity()).mCellulare = !(mCellulareEditText.getText().toString().equals("")) ? mCellulareEditText.getText().toString() : "";
        ((CalcoloValoriEnergeticiActivity) getActivity()).mEmail = !(mEmailEditText.getText().toString().equals("")) ? mEmailEditText.getText().toString() : "";

    }


    /*
       Metodo necessario quando si clicca su 'Calcola' dopo aver inserito un valore errato in una
       delle EditText. Va a cancellare l'ultimo parametro inserito, se errato.
     */
    private void checkUltimoParametroModificato(String parametro) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in checkUltimoParametroModificato()");

        switch (parametro) {

            case EDIT_TEXT_DATA_DI_NASCITA:
                SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
                formatDate.setLenient(false);

                String dateToValidate = mDataDiNascitaEditText.getText().toString();

                try {
                    Date date = formatDate.parse(dateToValidate);
                } catch (ParseException exc) {
                    mDataDiNascitaEditText.setText("");
                }

                break;

            case EDIT_TEXT_TELEFONO_FISSO:
                if (!mTelefonoFissoEditText.getText().toString().equals("")) {
                    String telefonoFisso = mTelefonoFissoEditText.getText().toString();
                    if (!phoneValidation(telefonoFisso)) {
                        mTelefonoFissoEditText.setText("");
                    }
                }
                break;

            case EDIT_TEXT_CELLULARE:
                if (!mCellulareEditText.getText().toString().equals("")) {
                    String telefonoCellulare = mCellulareEditText.getText().toString();
                    if (!phoneValidation(telefonoCellulare)) {
                        mCellulareEditText.setText("");
                    }
                }
                break;

            case EDIT_TEXT_EMAIL:
                if (!mEmailEditText.getText().toString().equals("")) {
                    String email = mEmailEditText.getText().toString();
                    if (!emailValidation(email)) {
                        mEmailEditText.setText("");
                    }
                }
                break;

        }
    }
}
