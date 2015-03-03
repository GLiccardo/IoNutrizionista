package it.giuseppeliccardo.ionutrizionista;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class FragmentFunzionalita1 extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    // TODO: Dichiarare qui tutti gli elementi grafici che saranno individuati nel metodo findViewsById
    // TODO: Eliminare questi inseriti come esempio
    private TextView mBordoCellaBarraColorata;
    private EditText mEtaEditText;
    private DatePickerDialog mEtaPickerDialog;
    private SimpleDateFormat mDateFormatter;

    public FragmentFunzionalita1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Aggiungo la UI al fragment
        View rootView = inflater.inflate(R.layout.fragment_funzionalita1, container, false);

        // Aggiungere il bordo ad una delle TextView della barra colorata
        TextView rettangolino = (TextView) rootView.findViewById(R.id.barra_colorata_25_30);
        GradientDrawable backgroundGradient = (GradientDrawable) rettangolino.getBackground();
        backgroundGradient.setStroke(5, getResources().getColor(R.color.nero_chiaro));

        TextView kgm2 = (TextView) rootView.findViewById(R.id.text_view_kgm2);
        //kgm2.setText(Html.fromHtml("kg/m<sup><small>" + 2 + "</small></sup>"));

        // Invoco il Date Picker
        final EditText editTextDataDiNascita = (EditText) rootView.findViewById(R.id.edit_text_eta);
        editTextDataDiNascita.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);

        /*
            Gestisco il tocco sulla edit text DATA DI NASCITA
            Il primo tocco effettua il focus della view, mentre quelli successivi il click sulla view
         */
        editTextDataDiNascita.setOnFocusChangeListener(this);
        editTextDataDiNascita.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // Quando il Dialog Box viene chiuso, viene invocato questo metodo
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextDataDiNascita.setText(mDateFormatter.format(newDate.getTime()));
            }
        };

        mEtaPickerDialog = new DatePickerDialog(
                getActivity(),
                datePickerListener,
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);

        findViewsById();
    }

    private void findViewsById() {
        // TODO: Inserire qui tutti i findViewById
        EditText mEtaEditText = (EditText) getView().findViewById(R.id.edit_text_eta);
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