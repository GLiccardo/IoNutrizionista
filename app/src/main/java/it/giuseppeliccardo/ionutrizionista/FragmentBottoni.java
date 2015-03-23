package it.giuseppeliccardo.ionutrizionista;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class FragmentBottoni extends Fragment {

    private static final String TAG = "ioNutrizionista";

    private Button mButtonCalcola;

    public FragmentBottoni() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onCreateView()");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottoni, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        try {
            mButtonCalcola = (Button) getView().findViewById((R.id.button3));
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

        //create a variable that contain your button
        mButtonCalcola.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int risultato = ((CalcoloValoriEnergeticiActivityv2) getActivity()).provaValoriEnergetici1;
                risultato++;
                Toast.makeText(getActivity(), "Risultato: " + risultato, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
