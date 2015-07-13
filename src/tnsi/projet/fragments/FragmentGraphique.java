package tnsi.projet.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pongiste.calping.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

import tnsi.projet.base.FonctionHistorique;
import tnsi.projet.base.FonctionJoueur;

/**
 * Fragment GRAPHIQUE
 * 5ème page de l'application
 * Elle permettra de voir en graphique
 * <p/>
 * *** Camenbert: Pourcentage Victoire / Défaite
 * *** Courbe: Evolution du nombre de points d'un joueur
 *
 * @author guillaumehautcoeur
 */
@SuppressLint("InflateParams")
public class FragmentGraphique extends Fragment {

    private static int[] COLORS = new int[]{Color.GREEN, Color.RED};
    public String choixListe = "";
    public int idjoueur;
    public Spinner selectpoints;
    public Button GraphiquePoints;
    View rootView;
    LinearLayout layout;
    TextView victoire, defaite;
    private GraphicalView mChart;
    private DefaultRenderer mRenderer = new DefaultRenderer();
    @SuppressWarnings("unused")
    private XYSeries mCurrentSeries;
    private XYSeriesRenderer mCurrentRenderer;
    private CategorySeries mSeries = new CategorySeries("");

    /**
     * Limite un double à deux chiffres après la virgule
     *
     * @param a
     * @param n
     * @return
     */
    public static double floor(double a, int n) {
        double p = Math.pow(10.0, n);
        return Math.floor((a * p) + 0.5) / p;
    }

    public void ConstruireGraphe() {
        //Initialiser les données
        mRenderer.removeAllRenderers();
        mSeries.clear();
        mCurrentSeries = new XYSeries("Pourcentage Victoire / Défaite"); //nom des séries
        mCurrentRenderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
        mRenderer.setChartTitle("Victoire / Défaite"); //nom du graphe
        mRenderer.setChartTitleTextSize(50); //taille du titre
        mRenderer.setLabelsTextSize(20); //taille text label
        mRenderer.setLegendTextSize(20);
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50)); //couleur de fond du graphe
        mRenderer.setStartAngle(90);
        mRenderer.setZoomButtonsVisible(true); //Button pour visibilité
        mRenderer.setDisplayValues(true);

        FonctionHistorique bd_historique = new FonctionHistorique(getActivity());
        bd_historique.open();
        victoire = (TextView) rootView.findViewById(R.id.victoire);
        defaite = (TextView) rootView.findViewById(R.id.defaite);

        double nb_victoire = bd_historique.DonnerVictoire(idjoueur); //nb victoire d'un joueur
        double nb_match = bd_historique.CompterHistorique(idjoueur); //nb de match d'un joueur
        double pourcentage_victoire = floor((nb_victoire / nb_match) * 100, 2); //ratio victoire

        SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();

        mSeries.add("Victoire", pourcentage_victoire); //ajoute les données victoire
        mSeries.add("Defaite", (100.0 - pourcentage_victoire)); //ajoute les données défaite
        renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]); //attribution couleur
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setPanEnabled(true);
        victoire.setText("Victoire: " + pourcentage_victoire + "%"); //affichage pourcentage au dessus du graphe
        defaite.setText("Défaite: " + floor((100.0 - pourcentage_victoire), 2) + "%");

        //création graphique + définition du type de graphique
        mChart = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);
        //on ajoute le graphique a la vue
        layout.addView(mChart);
        bd_historique.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.vue_graphique, null);
        chargerSpinner();
        GraphiquePoints = (Button) rootView.findViewById(R.id.graphiquepoints);
        GraphiquePoints.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), GraphiquePoints.class);
                startActivity(i);
            }
        });

        layout = (LinearLayout) rootView.findViewById(R.id.chart);

        return rootView;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {

        super.setMenuVisibility(visible);
        if (visible) {
            chargerSpinner();

        }
    }

    public void chargerSpinner() {
        selectpoints = (Spinner) rootView.findViewById(R.id.selectpoints);
        selectpoints.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {

                //On récupére les données de la liste
                choixListe = selectpoints.getSelectedItem().toString();
                /**
                 * On splite les données pour récupérer le nom et le prénom de la personnes
                 */
                String donnees = choixListe;
                String[] parts = donnees.split(" - ");
                String nom = parts[0];
                String prenom = parts[1];
                FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
                bd_joueur.open();
                idjoueur = bd_joueur.RecupererIdByJoueur(nom, prenom);

                ConstruireGraphe();

                bd_joueur.close();


            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> list = new ArrayList<String>();
        FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
        bd_joueur.open();
        ArrayList<Integer> listeidjoueur = new ArrayList<Integer>();
        listeidjoueur = bd_joueur.ListerIDjoueur();

        for (Integer s : listeidjoueur) {
            list.add(bd_joueur.DonnerNomPrenom(s));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.notifyDataSetChanged();
        selectpoints.setAdapter(dataAdapter);


    }

    public void OnPause() {
        super.onPause();

    }

    public void onResume() {
        super.onResume();

    }
}
