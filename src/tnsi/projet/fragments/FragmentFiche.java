package tnsi.projet.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pongiste.calping.R;

import java.util.ArrayList;

import tnsi.projet.base.FonctionHistorique;
import tnsi.projet.base.FonctionJoueur;
import tnsi.projet.base.Joueur;
import tnsi.projet.base.MaBaseSQLiteJoueur;

/**
 * Fragment FICHE 
 * 2ème page de l'application 
 * Elle permettra d'effectuer 
 * 
 * *** l'ajout d'un joueur
 * *** la modification d'un joueur
 * *** la suppression d'un joueur
 * 
 * @author guillaumehautcoeur
 *
 */
@SuppressLint("InflateParams") public class FragmentFiche extends Fragment {
	View rootView;
	ListView tableau;
	ArrayList<ArrayList<String>> informations ;
	MaBaseSQLiteJoueur j;
	TextView nom,prenom;
	String noms,prenoms;
	float pointsjoueur_mensuels,pointsjoueur_courants;
	int ini,i,id_joueur,nbjoueur;
	EditText nomjoueur, prenomjoueur,pointsjoueur_mensuel,pointsjoueur_courant,nbderive;
	ImageButton btnAjouterJoueur;
	Button monbouton;
	Button maderive;
	float pointcourant;
	private AlertDialog.Builder build;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		 rootView = inflater.inflate(R.layout.vue_fiche, container, false);
		 creerListe();
		 btnAjouterJoueur = (ImageButton) rootView.findViewById(R.id.btn_AlertDialog);
		 btnAjouterJoueur.setOnClickListener(new View.OnClickListener() {public void onClick(View v)
			  		{
				  	ajouterJoueur();
			  		}
		  	});
		 maderive = (Button) rootView.findViewById(R.id.derive);
		 maderive.setOnClickListener(new View.OnClickListener()

		 {public void onClick(View v)
			 {
				 ajouterDerive();
			 }
		 });
		 return rootView;
	    }
	 
	 /**
	  * permet de rafraichir le fragment lorsque celui-ci apparait à l'écran
	  */
	 @Override
		public void setMenuVisibility(final boolean visible) {
			super.setMenuVisibility(visible);
			if (visible) {
				if (ini > 0) 
				{
					creerListe();

				} else {
					ini = ini + 1;
				}
			}
		}




	  /**
	   * Permet d'afficher la liste des joueurs
	   */
	  public void creerListe()

	 {
		 ListView mylist=(ListView) rootView.findViewById(R.id.tableau);
	 	 FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
		 bd_joueur.open();
		 ArrayList<Joueur> j = bd_joueur.ListerAllJoueur();
		 
		 final ArrayAdapter<Joueur> adapter = new ArrayAdapter<Joueur>(getActivity(),android.R.layout.simple_list_item_1,j);
		 mylist.setAdapter(adapter);
	
	
		 mylist.setOnItemClickListener(new OnItemClickListener() {

			 public void onItemClick(AdapterView<?> parent, View view,
									 int position, long id) {

				 //On va récupérer les informations dont l'on a besoin
				 Joueur j2 = (Joueur) parent.getItemAtPosition(position);
				 int idjoueuramodifier = j2.getIdjoueur();
				 modifierJoueur(idjoueuramodifier);

			 }


		 });
		 
		/**
		 * Recherche de joueur ..
		 */
		 EditText myFilter = (EditText) rootView.findViewById(R.id.filtre);
		  myFilter.addTextChangedListener(new TextWatcher() {

			  public void afterTextChanged(Editable s) {
			  }

			  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			  }

			  public void onTextChanged(CharSequence s, int start, int before, int count) {
				  adapter.getFilter().filter(s.toString());
			  }
		  });
		 
		  	/**
			 * Suppression d'un joueur ..
			 */
		 mylist.setOnItemLongClickListener(new OnItemLongClickListener() {
			 FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
			
	            public boolean onItemLongClick(final AdapterView<?> parent, View view,
						final int position, long id) {

	                build = new AlertDialog.Builder(getActivity());
	             
	                Joueur j2 = (Joueur) parent.getItemAtPosition(position);
	                build.setTitle("Supprimer " +  j2.getNomJoueur() + " "+ j2.getPrenomJoueur() + " ?");
	                build.setIcon(R.drawable.corbeille);  
	                build.setMessage("Voulez-vous supprimer ce joueur ?");
	                build.setPositiveButton("Oui",new DialogInterface.OnClickListener() {
	                	
	                
	                            public void onClick(DialogInterface dialog, int which) {
	                            	  Joueur j2 = (Joueur) parent.getItemAtPosition(position);
	                            	  	Toast.makeText( getActivity(),j2.getNomJoueur() + " "+ j2.getPrenomJoueur()+ " est supprimé !", Toast.LENGTH_LONG).show();
	                                	int idjoueur = j2.getIdjoueur();
	                                	bd_joueur.SupprimerJoueur(idjoueur);
	                            		FonctionHistorique bd_historique = new FonctionHistorique(getActivity());
	                            		bd_historique.open();
	                            		bd_historique.SupprimerHistorique(idjoueur);
	                                	
	                                	creerListe();
	                                	dialog.cancel();
	                            }
	                        });

	                build.setNegativeButton("Non", new DialogInterface.OnClickListener() {

	                            public void onClick(DialogInterface dialog, int which) {
	                                    dialog.cancel();
	                            }
	                        });
	                AlertDialog alert = build.create();
	                alert.show();

	                return true;
	            }
	        });
		 bd_joueur.close();
	 }

	/**
	 * Ajouter une dérive au joueur
	 */


	 
	  /**
	   * Modification d'un joueur 
	   * @param idjoueur
	   */
	  public void modifierJoueur(int idjoueur) {
	  		
		  id_joueur =idjoueur;
		  System.out.println("idjoueur :"+ id_joueur);
		  LayoutInflater inflater = getLayoutInflater(null);
		  View alertLayout = inflater.inflate(R.layout.modifierjoueur, null);
		  FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
		  bd_joueur.open();
		  String nom_Prenom = bd_joueur.DonnerNomPrenom(id_joueur);
		  String[] parts = nom_Prenom.split(" - ");
		  String nom = parts[0]; 
		  String prenom = parts[1]; 
		  
		  System.out.println("nom :"+ nom);
		  System.out.println("prenom :"+ prenom);
		  nomjoueur = (EditText)alertLayout.findViewById(R.id.nomjoueur);
		  nomjoueur.setText(nom); //PlaceOlder
	      prenomjoueur = (EditText)alertLayout.findViewById(R.id.prenomjoueur);  
	      prenomjoueur.setText(prenom);
	      pointsjoueur_mensuel = (EditText)alertLayout.findViewById(R.id.pointsjoueurmensuel);  
	      pointsjoueur_mensuel.setText(bd_joueur.DonnerPointJoueur(id_joueur)+"");
	      pointsjoueur_courant = (EditText)alertLayout.findViewById(R.id.pointsjoueurcourant);  
	      pointsjoueur_courant.setText(bd_joueur.DonnerPointCourantJoueur(id_joueur)+"");
	      

		  AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		  alert.setTitle("Modifier le joueur");
		  alert.setView(alertLayout);
		  alert.setIcon(R.drawable.modifier);  
		  alert.setCancelable(false);
		  alert.setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
		  
		  
		  public void onClick(DialogInterface dialog, int which) 
		  	{
		  Toast.makeText(getActivity(), "Aucun joueur modifié !", Toast.LENGTH_SHORT).show();
		  	}
		  });
 
 
		  alert.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
	 		//si on choisit de modifier
			 public void onClick(DialogInterface dialog, int which) 
			 {
				  noms= nomjoueur.getText().toString();
			      prenoms= prenomjoueur.getText().toString();
			      pointsjoueur_courants = Float.parseFloat(pointsjoueur_courant.getText().toString());
			      pointsjoueur_mensuels = Float.parseFloat(pointsjoueur_mensuel.getText().toString());
				 
				 FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
				 bd_joueur.open();
				 Joueur J = bd_joueur.ListerJoueur(id_joueur);
				 J.setNomJoueur(noms);
				 J.setPrenomJoueur(prenoms);
				 J.setPointsjoueur_mensuel(pointsjoueur_mensuels);	
				 J.setPointsjoueur_courant(pointsjoueur_courants);	
				 bd_joueur.majJoueur(id_joueur, J);
				 Toast.makeText(getActivity(), noms + " " + prenoms +" a été modifié !", Toast.LENGTH_SHORT).show();
				 creerListe();
			 }
		});
			 AlertDialog dialog = alert.create();
			 dialog.show();
			 }
	
	  /**
	   * Ajout d'un joueur
	   */
	  @SuppressLint("InflateParams") public void ajouterJoueur() {
	  		
		  LayoutInflater inflater = getLayoutInflater(null);
		  View alertLayout = inflater.inflate(R.layout.vue_ajouterjoueur, null);
		  nomjoueur = (EditText)alertLayout.findViewById(R.id.nomjoueur);
		  nomjoueur.setHint("Nom"); //PlaceOlder
	      prenomjoueur = (EditText)alertLayout.findViewById(R.id.prenomjoueur);  
	      prenomjoueur.setHint("Prénom");
	      pointsjoueur_mensuel = (EditText)alertLayout.findViewById(R.id.pointsjoueur);  
	      pointsjoueur_mensuel.setHint("Points");

		  
		  AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		  alert.setTitle("Ajouter un joueur");
		  alert.setIcon(R.drawable.ajouterjoueur);  
		  alert.setView(alertLayout);
		  alert.setCancelable(false);
		  alert.setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
		  
		  
		  public void onClick(DialogInterface dialog, int which) {
		  Toast.makeText(getActivity(), "Aucun joueur ajouté !", Toast.LENGTH_SHORT).show();
  }
  });
  
  alert.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {

  public void onClick(DialogInterface dialog, int which) {

	  FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
	  bd_joueur.open();
	
      nbjoueur = bd_joueur.SelectDernierIDJoueur() + 1;
	  
	  if (( nomjoueur.getText().toString().trim().equals("")) || prenomjoueur.getText().toString().trim().equals("")
			  || pointsjoueur_mensuel.getText().toString().trim().equals(""))
	  	{Toast.makeText(getActivity(), "Vous avez oublie un champ !", Toast.LENGTH_SHORT).show(); }
	  else
	  { 
		  InsererJoueur(); //Appel la fonction calcul de points
		  Toast.makeText(getActivity(), "Mr "+ nomjoueur.getText().toString() +
				  " "+prenomjoueur.getText().toString() + " a été ajouté ! ", Toast.LENGTH_SHORT).show();
		  ReinitialiserFormulaire();
		  creerListe();
	  }
	  bd_joueur.close();
  			}
			  });
  AlertDialog dialog = alert.create();
  dialog.show();
  
  }
  
	  /**
	   * Insertion d'un joueur
	   */
	  public void InsererJoueur() {
		 FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
		 bd_joueur.open();
		  bd_joueur.insererJoueur(nbjoueur, nomjoueur.getText().toString(), prenomjoueur.getText().toString(),
				  pointsjoueur_mensuel.getText().toString() ,pointsjoueur_mensuel.getText().toString());
		 creerListe();
	}
	
	  /**
	   * Réinitialisation du formulaire d'ajout d'un joueur
	   */
	  public void ReinitialiserFormulaire() { 
	      nomjoueur.setText("");
		  nomjoueur.setHint("Nom"); //PlaceOlder
	      prenomjoueur.setText("");
	      prenomjoueur.setHint("Prénom");
	      pointsjoueur_mensuel.setText("");
	      pointsjoueur_mensuel.setHint("Points");
	}
	  
		public void OnPause()
		{
			super.onPause();
			
			
		}
		
		public void onResume()
		{
			super.onResume();
			
		}

	public void ajouterDerive() {

		LayoutInflater inflater = getLayoutInflater(null);
		View alertLayout = inflater.inflate(R.layout.vue_ajouterderive, null);
		nbderive = (EditText)alertLayout.findViewById(R.id.derive);
		nbderive.setHint("Indiquer dérive"); //PlaceOlder


		AlertDialog.Builder alert2 = new AlertDialog.Builder(getActivity());
		alert2.setTitle("Ajouter une dérive");
		alert2.setView(alertLayout);
		alert2.setIcon(R.drawable.modifier);
		alert2.setCancelable(false);
		alert2.setNegativeButton("Quitter", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), "Aucune dérive ajouté !", Toast.LENGTH_SHORT).show();
			}
		});
		alert2.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());

				bd_joueur.open();

				ArrayList<Integer> listeidjoueur = new ArrayList<Integer>();

				listeidjoueur = bd_joueur.ListerIDjoueur();


				for (Integer s : listeidjoueur) {

					int idjoueur = s;
					double pointcourant = bd_joueur.DonnerPointCourantJoueur(idjoueur);

					pointcourant = pointcourant - Float.parseFloat(nbderive.getText().toString());
					pointcourant = (double) Math.round(pointcourant * 100) / 100; //2 chiffres après la virgule


					Joueur j = bd_joueur.ListerJoueur(idjoueur);
					j.setPointsjoueur_mensuel(pointcourant);
					j.setPointsjoueur_courant(pointcourant);
					bd_joueur.majJoueur(idjoueur, j);


					FonctionHistorique bd_historique = new FonctionHistorique(getActivity());
					int idhistoriqueaajouter = bd_historique.DonnerDernierIDHistoriqueJoueur(idjoueur) + 1;
					bd_historique.open();
					double pointderive = Float.parseFloat(nbderive.getText().toString());
					pointderive = 0 - pointderive;
					bd_historique.insererHistorique(idhistoriqueaajouter, idjoueur, 0, pointderive, "Derive", pointcourant);
					bd_historique.close();
				}

				creerListe();
				bd_joueur.close();
				Toast.makeText(getActivity(), "Dérive effectué !", Toast.LENGTH_LONG).show();


			}
		});
		AlertDialog dialog2 = alert2.create();
		dialog2.show();
	}


}

	 
	
	
	 

	 