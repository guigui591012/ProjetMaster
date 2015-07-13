package tnsi.projet.fragments;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pongiste.calping.R;

import java.util.ArrayList;
import java.util.List;

import tnsi.projet.base.FonctionHistorique;
import tnsi.projet.base.FonctionJoueur;

/**
 * Fragment PERFORMANCE
 * 4ème page de l'application
 * Elle permettra de renseigner pour un joueur quelques performances
 * <p/>
 * *** Sa  performance
 * *** Sa plus grosse contre performance
 * *** Son meilleure classement
 * *** Son plus faible classement
 * *** Son nombre de match gagné
 * *** Son nombre de match perdu
 *
 * @author guillaumehautcoeur
 */
public class FragmentPerformance extends Fragment {
    public Spinner selectpoints;
    View rootView;
    String choixListe = "";
    int idjoueur;
    ImageView poucevert, poucerouge, top, descendante, match_gagne, match_perdu, moyenne;

    //limite un double à deux chiffre apres la virgule
    public static double floor(double a, int n) {
        double p = Math.pow(10.0, n);
        return Math.floor((a * p) + 0.5) / p;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.vue_performance, container, false);
        poucevert = (ImageView) rootView.findViewById(R.id.poucevert);
        poucerouge = (ImageView) rootView.findViewById(R.id.poucerouge);
        match_gagne = (ImageView) rootView.findViewById(R.id.match_gagne);
        match_perdu = (ImageView) rootView.findViewById(R.id.match_perdu);
        top = (ImageView) rootView.findViewById(R.id.top);
        descendante = (ImageView) rootView.findViewById(R.id.descendante);
        moyenne = (ImageView) rootView.findViewById(R.id.moyenne);
        chargerSpinner();
        afficherPerformance();

        return rootView;
    }

    public void chargerSpinner() {
        final Spinner selectpoints = (Spinner) rootView.findViewById(R.id.selectpoints);
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
                afficherPerformance();
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.notifyDataSetChanged();
        selectpoints.setAdapter(dataAdapter);
        bd_joueur.close();

    }

    public void afficherPerformance() {
        GridLayout grl_TableauResultat = (GridLayout) rootView.findViewById(R.id.grl_performance);
        @SuppressWarnings("unused")
        final Spinner selectpoints = (Spinner) rootView.findViewById(R.id.selectpoints);
        FonctionHistorique bd_historique = new FonctionHistorique(getActivity());
        bd_historique.open();

        grl_TableauResultat.removeAllViews();
        grl_TableauResultat.setColumnCount(3); //nombre de collones

        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int largeurEcran = size.x;
        int hauteurElement = 100;


        TextView Meilleure_performance = new TextView(getActivity());
        Meilleure_performance.setWidth((int) (largeurEcran * 0.5));
        Meilleure_performance.setHeight(hauteurElement);
        Meilleure_performance.setGravity(Gravity.CENTER);
        Meilleure_performance.setTypeface(Typeface.SERIF, Typeface.BOLD);
        Meilleure_performance.setText("Meilleure Perf.");

        grl_TableauResultat.addView(Meilleure_performance);

        double performance = bd_historique.DonnerMeilleurePerformanceJoueur(idjoueur);

        TextView resultat_performance = new TextView(getActivity());
        resultat_performance.setText(performance + "");
        resultat_performance.setWidth((int) (largeurEcran * 0.25));
        resultat_performance.setHeight(hauteurElement);
        resultat_performance.setGravity(Gravity.CENTER);
        grl_TableauResultat.addView(resultat_performance);

        grl_TableauResultat.addView(poucevert);

/**
 * Contre performance ////////////////////////////////////
 */
        TextView Contre_performance = new TextView(getActivity());
        Contre_performance.setWidth((int) (largeurEcran * 0.5));
        Contre_performance.setHeight(hauteurElement);
        Contre_performance.setGravity(Gravity.CENTER);
        Contre_performance.setTypeface(Typeface.SERIF, Typeface.BOLD);
        Contre_performance.setText("Contre Perf.");
        Contre_performance.setBackgroundColor(Color.rgb(239, 239, 239));
        grl_TableauResultat.addView(Contre_performance);


        double contreperformance = bd_historique.DonnerMeilleureContreJoueur(idjoueur);

        TextView resultat_contre = new TextView(getActivity());
        resultat_contre.setWidth((int) (largeurEcran * 0.25));
        resultat_contre.setHeight(hauteurElement);
        resultat_contre.setGravity(Gravity.CENTER);
        resultat_contre.setText(contreperformance + "");
        resultat_contre.setBackgroundColor(Color.rgb(239, 239, 239));
        grl_TableauResultat.addView(resultat_contre);

        grl_TableauResultat.addView(poucerouge);

////////////////////////////////////////////////////////////

        TextView best_classement = new TextView(getActivity());
        best_classement.setWidth((int) (largeurEcran * 0.5));
        best_classement.setHeight(hauteurElement);
        best_classement.setGravity(Gravity.CENTER);
        best_classement.setTypeface(Typeface.SERIF, Typeface.BOLD);
        best_classement.setText("Meilleur classement");
        grl_TableauResultat.addView(best_classement);

        FonctionJoueur fj = new FonctionJoueur(getActivity());
        fj.open();
        double pointlicence = fj.DonnerPointJoueur(idjoueur);
        double best_classements = 0;
        double points_max_courant = bd_historique.DonnerMeilleureClassementJoueur(idjoueur);

        if (pointlicence < points_max_courant) {
            best_classements = points_max_courant;
        } else {
            best_classements = pointlicence;
        }

        TextView resultats_best_classements = new TextView(getActivity());
        resultats_best_classements.setWidth((int) (largeurEcran * 0.25));
        resultats_best_classements.setHeight(hauteurElement);
        resultats_best_classements.setGravity(Gravity.CENTER);
        resultats_best_classements.setText(best_classements + "");
        grl_TableauResultat.addView(resultats_best_classements);

        grl_TableauResultat.addView(top);

        TextView Bas_classement = new TextView(getActivity());
        Bas_classement.setWidth((int) (largeurEcran * 0.5));
        Bas_classement.setHeight(hauteurElement);
        Bas_classement.setGravity(Gravity.CENTER);
        Bas_classement.setTypeface(Typeface.SERIF, Typeface.BOLD);
        Bas_classement.setText("Plus faible classement");
        Bas_classement.setBackgroundColor(Color.rgb(239, 239, 239));
        grl_TableauResultat.addView(Bas_classement);

        double moinsbon_classements = 0;
        double points_min_courant = bd_historique.DonnerPlusBasClassementJoueur(idjoueur);

        if (pointlicence > points_min_courant) {
            moinsbon_classements = points_min_courant;
        } else {
            moinsbon_classements = pointlicence;
        }

        TextView resultats_faible_classements = new TextView(getActivity());
        resultats_faible_classements.setWidth((int) (largeurEcran * 0.25));
        resultats_faible_classements.setHeight(hauteurElement);
        resultats_faible_classements.setGravity(Gravity.CENTER);
        resultats_faible_classements.setText(moinsbon_classements + "");
        resultats_faible_classements.setBackgroundColor(Color.rgb(239, 239, 239));
        grl_TableauResultat.addView(resultats_faible_classements);

        grl_TableauResultat.addView(descendante);


        TextView moy_joueur = new TextView(getActivity());
        moy_joueur.setWidth((int) (largeurEcran * 0.5));
        moy_joueur.setHeight(hauteurElement);
        moy_joueur.setGravity(Gravity.CENTER);
        moy_joueur.setTypeface(Typeface.SERIF, Typeface.BOLD);
        moy_joueur.setText("Moy. clsmt joué");
        grl_TableauResultat.addView(moy_joueur);

        double resultats_moy_joues = bd_historique.DonnerMoyenneClassementJoueurJouee(idjoueur);


        TextView resultats_moy_joue = new TextView(getActivity());
        resultats_moy_joue.setWidth((int) (largeurEcran * 0.25));
        resultats_moy_joue.setHeight(hauteurElement);
        resultats_moy_joue.setGravity(Gravity.CENTER);
        resultats_moy_joue.setText(floor((resultats_moy_joues), 2) + "");
        grl_TableauResultat.addView(resultats_moy_joue);

        grl_TableauResultat.addView(moyenne);


        /**
         * Nombre de match gagné
         *
         */
        /**
         * Contre performance ////////////////////////////////////
         */
        TextView Match_gagne = new TextView(getActivity());
        Match_gagne.setWidth((int) (largeurEcran * 0.5));
        Match_gagne.setHeight(hauteurElement);
        Match_gagne.setGravity(Gravity.CENTER);
        Match_gagne.setTypeface(Typeface.SERIF, Typeface.BOLD);
        Match_gagne.setText("Nb de matchs gagnés");
        Match_gagne.setBackgroundColor(Color.rgb(239, 239, 239));
        grl_TableauResultat.addView(Match_gagne);

        double nb_match_gagne = bd_historique.DonnerVictoire(idjoueur);

        TextView resutltat_nb_match_gagne = new TextView(getActivity());
        resutltat_nb_match_gagne.setWidth((int) (largeurEcran * 0.25));
        resutltat_nb_match_gagne.setHeight(hauteurElement);
        resutltat_nb_match_gagne.setGravity(Gravity.CENTER);
        resutltat_nb_match_gagne.setText(nb_match_gagne + "");
        resutltat_nb_match_gagne.setBackgroundColor(Color.rgb(239, 239, 239));
        grl_TableauResultat.addView(resutltat_nb_match_gagne);


        grl_TableauResultat.addView(match_gagne);

        TextView Match_perdu = new TextView(getActivity());
        Match_perdu.setWidth((int) (largeurEcran * 0.5));
        Match_perdu.setHeight(hauteurElement);
        Match_perdu.setGravity(Gravity.CENTER);
        Match_perdu.setTypeface(Typeface.SERIF, Typeface.BOLD);
        Match_perdu.setText("Nb de matchs perdus");
        grl_TableauResultat.addView(Match_perdu);

        double nb_match_perdu = bd_historique.CompterHistorique(idjoueur) - nb_match_gagne;

        TextView resutltat_nb_match_perdu = new TextView(getActivity());
        resutltat_nb_match_perdu.setWidth((int) (largeurEcran * 0.25));
        resutltat_nb_match_perdu.setHeight(hauteurElement);
        resutltat_nb_match_perdu.setGravity(Gravity.CENTER);
        resutltat_nb_match_perdu.setText(nb_match_perdu + "");
        grl_TableauResultat.addView(resutltat_nb_match_perdu);


        grl_TableauResultat.addView(match_perdu);

        fj.close();
        bd_historique.close();
    }

    /**
     * Cette mÈthode est appelÈe lorsque le fragment est affichÈ ‡ l'Ècran
     */
    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            chargerSpinner();
            afficherPerformance();
        }
    }

    public void OnPause() {
        super.onPause();

    }

    public void onResume() {
        super.onResume();

    }


}

	

