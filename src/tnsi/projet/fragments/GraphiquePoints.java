package tnsi.projet.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.pongiste.calping.R;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

import tnsi.projet.base.FonctionHistorique;
import tnsi.projet.base.FonctionJoueur;

/**
 * ACTIVITE GRAPHIQUE POINTS
 * Elle est appelé lors de l'appel d'un bouton sur le fragment GRAPHIQUE
 * Elle permettra de voir l'évolution du nombre de point en graphique d'un joueur
 *
 * @author guillaumehautcoeur
 */
public class GraphiquePoints extends Activity {
    XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
    XYSeries incomeSeries = new XYSeries("Evolution de vos points");
    private Spinner selectpoints;
    private View mChart;
    private String choixListe = "";
    private int idjoueur, nbpoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphique_point);

    }

    public void onStart() {
        super.onStart();
        chargerSpinner();
    }

    public void onDestroy() {
        super.onDestroy();

    }


    /**
     * FONCTION QUI CREE LE GRAPHIQUE
     */
    public void openChart() {
        //on reset les données
        multiRenderer.removeAllRenderers();
        incomeSeries.clear();
        dataset.clear();

        FonctionHistorique bd_historique = new FonctionHistorique(this);

        @SuppressWarnings("unused")
        double nombredematch = bd_historique.CompterHistorique(idjoueur);


        ArrayList<Integer> Points = bd_historique.HistoriquePoints(idjoueur);
        for (int j = 0; j < Points.size(); j++) {
            int i = 0;

            nbpoints = Points.get(j);
            incomeSeries.add(i, nbpoints);
            i++;
        }


        dataset.addSeries(incomeSeries);

        incomeRenderer.setColor(Color.WHITE);
        incomeRenderer.setChartValuesTextSize(30);
        incomeRenderer.setPointStyle(PointStyle.CIRCLE);
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setLineWidth(3);
        incomeRenderer.setPointStrokeWidth(3);
        incomeRenderer.setDisplayChartValues(true);


        multiRenderer.setXLabels(0);


        multiRenderer.setChartTitle("Evolution de votre classement");
        multiRenderer.setLabelsTextSize(30);
        multiRenderer.setLegendTextSize(30);
        multiRenderer.setYTitle("Nombre de points");
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setBarSpacing(-0.7);
        multiRenderer.setShowGrid(true);
        multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        multiRenderer.setPanEnabled(false, false);
        //ajout
        multiRenderer.addSeriesRenderer(incomeRenderer);


        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
        chartContainer.removeAllViews();
        mChart = ChartFactory.getLineChartView(GraphiquePoints.this, dataset, multiRenderer);
        chartContainer.addView(mChart);

    }

    public void chargerSpinner() {
        selectpoints = (Spinner) findViewById(R.id.selectpoints);
        selectpoints.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {

                FonctionJoueur bd_joueur = new FonctionJoueur(getBaseContext());

                choixListe = selectpoints.getSelectedItem().toString();
                String donnees = choixListe;
                String[] parts = donnees.split(" - ");
                String nom = parts[0];
                String prenom = parts[1];

                bd_joueur.open();
                idjoueur = bd_joueur.RecupererIdByJoueur(nom, prenom);
                openChart();
                bd_joueur.close();

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> list = new ArrayList<String>();
        FonctionJoueur bd_joueur = new FonctionJoueur(this);
        bd_joueur.open();
        ArrayList<Integer> listeidjoueur = new ArrayList<Integer>();
        listeidjoueur = bd_joueur.ListerIDjoueur();

        for (Integer s : listeidjoueur) {
            list.add(bd_joueur.DonnerNomPrenom(s));
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getBaseContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.notifyDataSetChanged();
        selectpoints.setAdapter(dataAdapter);


    }

    public void onPause() {
        super.onPause();

    }

    public void onResume() {
        super.onResume();

    }

}
