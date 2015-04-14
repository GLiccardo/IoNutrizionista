package it.giuseppeliccardo.ionutrizionista;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class FragmentDatiAnagrafici extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "ioNutrizionista";

    // TODO: Dichiarare qui tutti gli elementi grafici che saranno individuati nel metodo findViewsById
    private EditText mNomeEditText;
    private EditText mCognomeEditText;
    private EditText mDataDiNascitaEditText;
    // TODO: Dichiarare qui l'elemento relativo al sesso

    private DatePickerDialog mEtaPickerDialog;
    private SimpleDateFormat mDateFormatter;


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

        // Ottengo un riferimento alle Views della UI
        findViewsById();
        // Mostro il DatePicker se clicco sulla EditText relativa all'età
        mostraDatePicker();

        ((CalcoloValoriEnergeticiActivity) getActivity()).provaValoriEnergetici1 = 17;

    }


    /*
      Metodo che consente di ottenere i reference per tutte le View che verranno utilizzate
     */
    private void findViewsById() {

        try {
            mNomeEditText = (EditText) getView().findViewById(R.id.edit_text_nome);
            mCognomeEditText = (EditText) getView().findViewById(R.id.edit_text_cognome);
            mDataDiNascitaEditText = (EditText) getView().findViewById(R.id.edit_text_eta);

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

    }


    /*
      Metodo che consente la visualizzazione di un DatePicker quando si effettua un clic sulla
      EditText relativa alla data di nascita del paziente
     */
    private void mostraDatePicker() {
        // Invoco il Date Picker
        mDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Toast.makeText(getActivity(), "Primo click", Toast.LENGTH_SHORT).show();
            mEtaPickerDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Click successivi", Toast.LENGTH_SHORT).show();
        mEtaPickerDialog.show();
    }

}
