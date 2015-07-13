package tnsi.projet.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.pongiste.calping.R;

import java.util.ArrayList;

import tnsi.projet.base.FonctionJoueur;
import tnsi.projet.base.Joueur;

/**
 * ACTIVITE MAJ Classement
 * Elle est appelé une fois par mois à l'aide des timers et de l'alarme
 * afin de mettre à jour tout les classements mensuels d'un joueur
 * elle prend le nombre de point courant du joueur et le met dans son nombre de points mensuel
 *
 * @author guillaumehautcoeur
 */
public class MAJClassement extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maj);

        ImageView image = (ImageView) findViewById(R.id.majIMAGE);

    }

    public void onStart() {
        super.onStart();


        FonctionJoueur bd_joueur = new FonctionJoueur(this);
        bd_joueur.open();

        ArrayList<Integer> listeidjoueur = new ArrayList<Integer>();
        listeidjoueur = bd_joueur.ListerIDjoueur();

        for (Integer s : listeidjoueur) {

            int idjoueur = s;
            double pointcourant = bd_joueur.DonnerPointCourantJoueur(idjoueur);
            //on prend notre joueur que l'on veut mettre à jour
            Joueur j = bd_joueur.ListerJoueur(idjoueur);
            j.setPointsjoueur_mensuel(pointcourant);
            bd_joueur.majJoueur(idjoueur, j);

        }

        bd_joueur.close();

        Toast.makeText(this, "Vos points ont été mis à jour !", Toast.LENGTH_LONG).show();
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }
}
