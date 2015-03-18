package it.giuseppeliccardo.ionutrizionista;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentDatiAnagrafici extends Fragment {

    private static final String TAG = "ioNutrizionista";

    public FragmentDatiAnagrafici() {
        // Required empty public constructor
    }

    // TODO: Interfaccia per la comunicazione con l'activity
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dati_anagrafici, container, false);
    }


    // TODO: Il fragment cattura l'interfaccia nell'onAttach
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
