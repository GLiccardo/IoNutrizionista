package it.giuseppeliccardo.ionutrizionista;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
    private TextView mBordoCellaBarraColorata,
            mBmiTextView,
            mPesoCalcolatoIdealeTextView,
            mMetabolismoBasaleTextView,
            mFabbisognoEnergeticoTextView,
            mRazioneCaloricaLeggeraTextView,
            mRazioneCaloricaModerataTextView,
            mRazioneCaloricaPesanteTextView;

    private EditText mNomeEditText,
            mCognomeEditText,
            mEtaEditText,
            mAltezzaEditText,
            mPesoEditText,
            mPlicheGirovitaEditText,
            mPlicheSchienaEditText,
            mPlicheBraccioEditText,
            mCirconferenzaAddomeEditText,
            mCirconferenzaFianchiEditText,
            mCirconferenzaCosciaEditText,
            mCirconferenzaPolsoEditText,
            mCirconferenzaBraccioEditText;

    private Button mButtonCalcola;

    private DatePickerDialog mEtaPickerDialog;
    private SimpleDateFormat mDateFormatter;


    // TODO: Gestire gli inserimenti anche nella versione v2
    // Esternalizzazione stringhe
    private static final String TAG_INSERIRE_ALTEZZA_PESO           = "Inserire Altezza e Peso";
    private static final String TAG_INSERIRE_ALTEZZA                = "Inserire Altezza";
    private static final String TAG_INSERIRE_ALTEZZA_CORRETTA       = "Altezza non valida";
    private static final String TAG_INSERIRE_PESO                   = "Inserire Peso";
    private static final String TAG_INSERIRE_PESO_CORRETTO          = "Peso non valido";


    /*
        Costruttore
     */
    public FragmentCalcoloValoriEnergetici() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Aggiungo la UI al fragment
        View rootView = inflater.inflate(R.layout.fragment_calcolo_valori_energetici, container, false);


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


        //create a variable that contain your button
        mButtonCalcola.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                float bmi = calcolaBMI();
                float bmiArrotondato = (float) Math.round(bmi * 100) / 100;
                mBmiTextView.setText(Float.toString(bmiArrotondato));
                settaBordoBarraColorata(bmi);
            }
        });

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
            mBmiTextView = (TextView) getView().findViewById(R.id.text_view_bmi);

            // Buttons
            mButtonCalcola = (Button) getView().findViewById((R.id.button_calcola));

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



    private float calcolaBMI() {

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
    }

    private void settaBordoBarraColorata(float bmi) {

        // Aggiungere il bordo ad una delle TextView della barra colorata
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
        GradientDrawable backgroundGradient = (GradientDrawable) mBordoCellaBarraColorata.getBackground();
        backgroundGradient.setStroke(0, getResources().getColor(R.color.nero_chiaro));

        // TODO: Rimuovere vecchi bordi

    }

}