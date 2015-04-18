package it.giuseppeliccardo.ionutrizionista;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;


public class CalcoloValoriEnergeticiActivity extends ActionBarActivity {

    private static final String TAG = "ioNutrizionista";

    private final FragmentListaSezioni mFragmentListaSezioni = new FragmentListaSezioni();
    private final FragmentBottoni mFragmentBottoni = new FragmentBottoni();
    private final FragmentDatiAnagrafici mFragmentDatiAnagrafici = new FragmentDatiAnagrafici();
    //private final FragmentMisurazioni mFragmentDatiMisurazioni = new FragmentMisurazioni();
    //private final FragmentRisultati mFragmentDatiRisultati = new FragmentRisultati();

    // Variabili condivise con i fragment
    public boolean flagClicButtonCalcola = false;        // Quando si clicca sul button 'Calcola'
    public boolean flagParamAnagraficiInseriti = false;  // Flag che indica se tutti i parametri sono stati inseriti
    public boolean flagParamMisurazioniInseriti = false; // Flag che indica se tutti i parametri sono stati inseriti
    public int flagRisultatiGiaCalcolati = 0;            // Per visualizzare in "Risultati" i parametri calcolati in precedenza
    public StringBuilder mParametriMancantiDatiAnagrafici;
    public StringBuilder mParametriMancantiMisurazioni;

    public int provaValoriEnergetici1;
    //int prova = ((CalcoloValoriEnergeticiActivity) getActivity()).provaValoriEnergetici1;

    public String mNome;
    public String mCognome;
    public String mSesso;
    public Date mDataDiNascita;
    public int mEta;

    public int mAltezzaCm;
    public float mPesoKg;
    public float mPlicheGirovita;
    public float mPlicheSchiena;
    public float mPlicheBraccio;
    public float mCirconferenzaAddome;
    public float mCirconferenzaFianchi;
    public float mCirconferenzaCoscia;
    public float mCirconferenzaPolso;
    public float mCirconferenzaBraccio;

    public float mIndiceMassaCorporea;
    public float mPesoCalcolatoIdeale;
    public int mMetabolismoBasale;
    public int mFabbisognoEnergetico;
    public int mRazioneCaloricaLeggera;
    public int mRazioneCaloricaModerata;
    public int mRazioneCaloricaPesante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ": entrato in onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcolo_valori_energetici_v2);

        // Carico i fragment dinamicamente nell'activity host
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        fragmentTransaction.add(R.id.container_sinistro, mFragmentListaSezioni);
        fragmentTransaction.add(R.id.container_destro_basso, mFragmentBottoni);
        fragmentTransaction.commit();

        //FragmentListaSezioni myFragment = (FragmentListaSezioni) getFragmentManager().findFragmentByTag("LISTA_SEZIONI");
        if (mFragmentDatiAnagrafici.isVisible()) {
            Toast.makeText(getApplicationContext(), "Lista Sezioni Visibile", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calcolo_valori_energetici_activityv2, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
