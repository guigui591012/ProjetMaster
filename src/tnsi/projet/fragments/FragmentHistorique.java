package tnsi.projet.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pongiste.calping.R;

import java.util.ArrayList;
import java.util.List;

import tnsi.projet.base.FonctionHistorique;
import tnsi.projet.base.FonctionJoueur;

/**
 * Fragment HISTORIQUE 3ème page de l'application Elle permettra de
 * 
 * *** Voir le détail de l'historique d'un joueur *** Supprimer l'historique
 * d'un joueur
 * 
 * @author guillaumehautcoeur
 * 
 */
public class FragmentHistorique extends Fragment {
	public Spinner selectpoints;
	View rootView;
	String choixListe = "";
	int idjoueur;
	ImageButton corbeille;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.vue_historique, container, false);
		chargerSpinner();
		corbeille = (ImageButton) rootView.findViewById(R.id.corbeille);
		corbeille.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				supprimerHistorique();
			}
		});

		return rootView;
	}

	public void chargerSpinner() {
		final Spinner selectpoints = (Spinner) rootView
				.findViewById(R.id.selectpoints);
		selectpoints.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				// On récupére les données de la liste
				choixListe = selectpoints.getSelectedItem().toString();

				String donnees = choixListe;
				String[] parts = donnees.split(" - ");
				String nom = parts[0];
				String prenom = parts[1];

				FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
				bd_joueur.open();
				idjoueur = bd_joueur.RecupererIdByJoueur(nom, prenom);
				afficherHistorique();
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
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapter.notifyDataSetChanged();
		selectpoints.setAdapter(dataAdapter);
		bd_joueur.close();
	}

	public void afficherHistorique() {
		GridLayout grl_TableauResultat = (GridLayout) rootView
				.findViewById(R.id.grl_resultat2);

		@SuppressWarnings("unused")
		final Spinner selectpoints = (Spinner) rootView
				.findViewById(R.id.selectpoints);
		FonctionHistorique bd_historique = new FonctionHistorique(getActivity());
		bd_historique.open();

		grl_TableauResultat.removeAllViews();
		grl_TableauResultat.setColumnCount(4); // nombre de collones

		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		int largeurEcran = size.x;
		int tailleElement = (int) (largeurEcran * 0.21);
		int hauteurElement = 100;

		// CrÈation de la premiËre ligne de la gridView
		TextView txvPointsAdversaires = new TextView(getActivity());
		txvPointsAdversaires.setTextSize(11);
		txvPointsAdversaires.setWidth(tailleElement);
		txvPointsAdversaires.setHeight(hauteurElement);
		txvPointsAdversaires.setGravity(Gravity.CENTER);
		txvPointsAdversaires.setTypeface(Typeface.SERIF, Typeface.BOLD);

		TextView txvVictoire_Defaite = new TextView(getActivity());
		txvVictoire_Defaite.setTextSize(11);
		txvVictoire_Defaite.setWidth(tailleElement);
		txvVictoire_Defaite.setHeight(hauteurElement);
		txvVictoire_Defaite.setGravity(Gravity.CENTER);
		txvVictoire_Defaite.setTypeface(Typeface.SERIF, Typeface.BOLD);

		TextView txvPointsGagne = new TextView(getActivity());
		txvPointsGagne.setTextSize(11);
		txvPointsGagne.setWidth(tailleElement);
		txvPointsGagne.setHeight(hauteurElement);
		txvPointsGagne.setGravity(Gravity.CENTER);
		txvPointsGagne.setTypeface(Typeface.SERIF, Typeface.BOLD);

		TextView txvPoints_courant = new TextView(getActivity());
		txvPoints_courant.setTextSize(11);
		txvPoints_courant.setWidth(tailleElement);
		txvPoints_courant.setHeight(hauteurElement);
		txvPoints_courant.setGravity(Gravity.CENTER);
		txvPoints_courant.setTypeface(Typeface.SERIF, Typeface.BOLD);

		txvPointsAdversaires.setBackgroundColor(Color.rgb(198, 226, 255));
		txvVictoire_Defaite.setBackgroundColor(Color.rgb(198, 226, 255));
		txvPointsGagne.setBackgroundColor(Color.rgb(198, 226, 255));
		txvPoints_courant.setBackgroundColor(Color.rgb(198, 226, 255));

		txvPointsAdversaires.setText("Pts Adv.");
		txvPointsAdversaires.setBackgroundResource(R.drawable.degrade_titre);
		txvVictoire_Defaite.setText("V/D");
		txvVictoire_Defaite.setBackgroundResource(R.drawable.degrade_titre);
		txvPointsGagne.setText("Pts Gagnés");
		txvPointsGagne.setBackgroundResource(R.drawable.degrade_titre);
		txvPoints_courant.setText("Pts actuels");
		txvPoints_courant.setBackgroundResource(R.drawable.degrade_titre);
		grl_TableauResultat.addView(txvPointsAdversaires); // ajouter à chaque
															// fois
		grl_TableauResultat.addView(txvVictoire_Defaite);
		grl_TableauResultat.addView(txvPointsGagne);
		grl_TableauResultat.addView(txvPoints_courant);

		ArrayList<ArrayList<String>> informations = bd_historique
				.DonnerHistorique(idjoueur);

		for (int i = 0; i < informations.size(); i++) {

			// ////////////////////Points
			// adversaires//////////////////////////////

			TextView txvPointsAdversaires1 = new TextView(getActivity());
			txvPointsAdversaires1.setText(informations.get(i).get(0));
			txvPointsAdversaires1.setWidth(tailleElement);
			txvPointsAdversaires1.setHeight(hauteurElement);
			txvPointsAdversaires1.setGravity(Gravity.CENTER);
			grl_TableauResultat.addView(txvPointsAdversaires1);

			// ///////////////////////////Victoire ou
			// dÉFAITE////////////////////////////////

			TextView txvVictoire_Defaite1 = new TextView(getActivity());
			if (informations.get(i).get(1).equals("Victoire")) {
				txvVictoire_Defaite1.setBackgroundResource(R.drawable.degrade_btnvert);
			}
			if (informations.get(i).get(1).equals("Defaite")) {
				txvVictoire_Defaite1.setBackgroundResource(R.drawable.degrade_btnrouge);
			}
			if (informations.get(i).get(1).equals("Derive")) {
				txvVictoire_Defaite1.setBackgroundResource(R.drawable.degrade_btnjaune);
			}


			txvVictoire_Defaite1.setText(informations.get(i).get(1));
			txvVictoire_Defaite1.setWidth(tailleElement);
			txvVictoire_Defaite1.setHeight(hauteurElement);
			txvVictoire_Defaite1.setGravity(Gravity.CENTER);
			grl_TableauResultat.addView(txvVictoire_Defaite1);

			// /////////////////////Points gagné///////////////////////////////

			TextView txvPointsGagne1 = new TextView(getActivity());
			txvPointsGagne1.setText(informations.get(i).get(2));
			txvPointsGagne1.setWidth(tailleElement);
			txvPointsGagne1.setHeight(hauteurElement);
			txvPointsGagne1.setGravity(Gravity.CENTER);
			grl_TableauResultat.addView(txvPointsGagne1);

			// /////////////////////Points cournat///////////////////////////////////

			TextView txvPoint_courant = new TextView(getActivity());
			txvPoint_courant.setText(informations.get(i).get(3));
			txvPoint_courant.setWidth(tailleElement);
			txvPoint_courant.setHeight(hauteurElement);
			txvPoint_courant.setGravity(Gravity.CENTER);
			grl_TableauResultat.addView(txvPoint_courant);

			// ///////////////////////////////////////////////////////////////////////
			bd_historique.close();

		}
	}

	/**
	 * Cette mÈthode est appelÈe lorsque le fragment est affichÈ ‡ l'Ècran
	 */
	@Override
	public void setMenuVisibility(final boolean visible) {
		super.setMenuVisibility(visible);
		if (visible) {
			chargerSpinner();
			afficherHistorique();
		}
	}

	private void supprimerHistorique() {
		AlertDialog.Builder ald_memoire = new AlertDialog.Builder(getActivity());
		ald_memoire.setTitle("Suppression de l'historique du joueur !");
		ald_memoire.setIcon(R.drawable.corbeille);
		ald_memoire.setMessage("Que voulez-vous supprimer  ? ");
		ald_memoire.setPositiveButton("Tout",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						FonctionHistorique bd_historique = new FonctionHistorique(
								getActivity());
						bd_historique.open();
						bd_historique.SupprimerHistorique(idjoueur);
						afficherHistorique();
						bd_historique.close();
						Toast.makeText(getActivity(), "Historique supprimé",
								Toast.LENGTH_SHORT).show();
					}
				});
		ald_memoire.setNegativeButton("Sélectionner",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						 Intent i = new Intent(getActivity(), DeleteHistorique.class);
						 i.putExtra("idjoueur", idjoueur); //placer en parametre idjoueur
			    		 startActivity(i); 

					}
				});
		AlertDialog helpDialog = ald_memoire.create();
		helpDialog.show();

	}

	
	public void OnPause() {
		afficherHistorique();
		super.onPause();

	}

	public void onResume() {
		afficherHistorique();
		super.onResume();

	}
}
