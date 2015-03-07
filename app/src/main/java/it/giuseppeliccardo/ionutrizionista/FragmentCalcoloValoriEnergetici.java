package it.giuseppeliccardo.ionutrizionista;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragment per la gestione della "Funzionalità 1":
 * - Dati Anagrafici (Nome, Cognome, Sesso, Età)
 * - Misurazioni (Altezza, Peso, Pliche, Circonferenze)
 * - Barra Colorata
 * - Risultati (BMI, Peso Calc Ideale, Metab Basale, Fabb Energetico, Raz Calorica)
 */
public class FragmentCalcoloValoriEnergetici extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    // TODO: Dichiarare qui tutti gli elementi grafici che saranno individuati nel metodo findViewsById
    // TODO: Eliminare questi inseriti come esempio
    private TextView mBordoCellaBarraColorata;
    private EditText mNomeEditText, mCognomeEditText, mEtaEditText, mAltezzaEditText, mPesoEditText,
            mPlicheGirovitaEditText, mPlicheSchienaEditText, mPlicheBraccioEditText,
            mCirconferenzaAddomeEditText, mCirconferenzaFianchiEditText, mCirconferenzaCosciaEditText, mCirconferenzaPolsoEditText, mCirconferenzaBraccioEditText;
    private DatePickerDialog mEtaPickerDialog;
    private SimpleDateFormat mDateFormatter;

    public FragmentCalcoloValoriEnergetici() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Aggiungo la UI al fragment
        View rootView = inflater.inflate(R.layout.fragment_funzionalita1, container, false);


        // Uso la classe Html per scrivere pedice e apice
        //TextView kgm2 = (TextView) rootView.findViewById(R.id.text_view_kgm2);
        //kgm2.setText(Html.fromHtml("kg/m<sup><small>" + 2 + "</small></sup>"));




        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Ottengo un riferimento alle Views della UI
        findViewsById();
        // Aggiungo il padding dinamicamente alle EditText
        paddingEditText();
        // Mostro il DatePicker se clicco sulla EditText relativa all'età
        mostraDatePicker();







        // Aggiungere il bordo ad una delle TextView della barra colorata
        GradientDrawable backgroundGradient = (GradientDrawable) mBordoCellaBarraColorata.getBackground();
        backgroundGradient.setStroke(5, getResources().getColor(R.color.nero_chiaro));

    }

    private void findViewsById() {
        // TODO: Inserire qui tutti i findViewById
        try {
            // Dati Anagrafici
            mNomeEditText = (EditText) getView().findViewById(R.id.edit_text_nome);
            mCognomeEditText = (EditText) getView().findViewById(R.id.edit_text_cognome);
            mEtaEditText = (EditText) getView().findViewById(R.id.edit_text_eta);

            // Misurazioni
            mAltezzaEditText = (EditText) getView().findViewById(R.id.edit_text_altezza);
            mPesoEditText = (EditText) getView().findViewById(R.id.edit_text_peso);
            mPlicheGirovitaEditText = (EditText) getView().findViewById(R.id.edit_text_pliche_girovita);
            mPlicheSchienaEditText = (EditText) getView().findViewById(R.id.edit_text_pliche_schiena);
            mPlicheBraccioEditText = (EditText) getView().findViewById(R.id.edit_text_pliche_braccio);
            mCirconferenzaCosciaEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_coscia);
            mCirconferenzaAddomeEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_addome);
            mCirconferenzaBraccioEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_braccio);
            mCirconferenzaFianchiEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_fianchi);
            mCirconferenzaPolsoEditText = (EditText) getView().findViewById(R.id.edit_text_circonferenza_polso);

            // Risultati
            mBordoCellaBarraColorata = (TextView) getView().findViewById(R.id.barra_colorata_25_30);

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }

    private void paddingEditText() {
        // Calcolo il valore in pixel di 4dpi in relazione al tipo di dispositivo utilizzato
        float valoreInPixelFloat4Dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        int valoreInPixelInt4Dp = (int) valoreInPixelFloat4Dp;

        // Calcolo il valore in pixel di 8dpi in relazione al tipo di dispositivo utilizzato
        float valoreInPixelFloat8Dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        int valoreInPixelInt8Dp = (int) valoreInPixelFloat8Dp;

        mNomeEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mCognomeEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mEtaEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mAltezzaEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mPesoEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mPlicheGirovitaEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mPlicheSchienaEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mPlicheBraccioEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mCirconferenzaCosciaEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mCirconferenzaAddomeEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mCirconferenzaBraccioEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mCirconferenzaFianchiEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
        mCirconferenzaPolsoEditText.setPadding(valoreInPixelInt8Dp, 0, valoreInPixelInt8Dp, valoreInPixelInt4Dp);
    }


    private void mostraDatePicker() {
        // Invoco il Date Picker
        mDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        mEtaEditText.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);

        // Gestione del primo tocco (onFocusChange) e di quelli successivi (onClick)
        mEtaEditText.setOnFocusChangeListener(this);
        mEtaEditText.setOnClickListener(this);

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // Quando il Dialog Box viene chiuso, viene invocato questo metodo
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar dataImpostata = Calendar.getInstance();
                dataImpostata.set(year, monthOfYear, dayOfMonth);
                mEtaEditText.setText(mDateFormatter.format(dataImpostata.getTime()));
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