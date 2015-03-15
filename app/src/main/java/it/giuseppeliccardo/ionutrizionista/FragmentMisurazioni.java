package it.giuseppeliccardo.ionutrizionista;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMisurazioni extends Fragment {


    private static final String TAG = "ioNutrizionista";

    public FragmentMisurazioni() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_misurazioni, container, false);
    }


}
