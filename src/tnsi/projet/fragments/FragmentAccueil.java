package tnsi.projet.fragments;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import tnsi.projet.base.FonctionHistorique;
import tnsi.projet.base.FonctionJoueur;
import tnsi.projet.base.Joueur;
import tnsi.projet.notifications.Notification;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.ads.*;
import com.pongiste.calping.R;

/**
 * Fragment ACCEUIL 
 * 1er page de l'application 
 * Elle permettra d'effectuer 
 * 
 * *** Calcul de points
 * *** Recherche de point d'un joueur sur internet
 * *** Enregistrer le résultat ou non dans la base de donnée historique
 * 
 * @author guillaumehautcoeur
 *
 */
public class FragmentAccueil extends Fragment {

	 View rootView; 
	 EditText text,text2; 
     Button monbouton;
     ImageButton rechercher;
     private Spinner coefficient,selectpoints;
     private Switch victoire_defaite;
     String choixListe,Victoire_Defaite="";
     double nouveauxpoints,nombre1,nombre2,points=0;
     int ini=0;
     int nombredejoueur;
     Notification alarm = new Notification();
     private WifiManager Wifi;
     private ConnectivityManager internet;
     private AlarmManager am;
 
     

	@SuppressWarnings({ "unused", "static-access" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		
		 FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
		 bd_joueur.open();
		 nombredejoueur = bd_joueur.CompterNbJoueur();
		 if(nombredejoueur==0)
		 {
			Toast.makeText(getActivity(), "Aucun joueur dans la base de donnée", Toast.LENGTH_SHORT).show();
		 }
		 bd_joueur.close();
		 
		    
		rootView = inflater.inflate(R.layout.vue_accueil, container, false);
		Wifi = (WifiManager)getActivity().getSystemService(getActivity().WIFI_SERVICE);
		internet = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
		am=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
		
		
		 AdView mAdView = new AdView(getActivity(),AdSize.SMART_BANNER,"ca-app-pub-1496017384993341/2597258515");  
		 AdRequest request = new AdRequest()
		 .addTestDevice(AdRequest.TEST_EMULATOR);
		 mAdView.loadAd(request);

		//Tous les mois appels alarmes pour mise à jour des points
		//tous les 1er du mois à 17H08
		ajouterAlarme(1,17,8);
		AlimenterCoefficient(); //chargé le spinner coéfficient
		chargerSpinner();
		
			
			Typeface font_titre = Typeface.createFromAsset(getActivity().getAssets(), "Atelas.ttf");
			Typeface font_text = Typeface.createFromAsset(getActivity().getAssets(), "Lucida.ttf");
			
			TextView titre = (TextView)rootView.findViewById(R.id.textView3);
			TextView joueur = (TextView)rootView.findViewById(R.id.joueur);
			TextView adversaires = (TextView)rootView.findViewById(R.id.adversaires);
			titre.setTypeface(font_titre);
			
		  text = (EditText)rootView.findViewById(R.id.nombre1); text.setTypeface(font_text);
		  text.setGravity(Gravity.CENTER_HORIZONTAL);
		  
	      text2 = (EditText)rootView.findViewById(R.id.nombre2); text2.setTypeface(font_text);
	      text2.setGravity(Gravity.CENTER_HORIZONTAL);
	      
	      monbouton = (Button)rootView.findViewById(R.id.calcul);	
	      rechercher = (ImageButton)rootView.findViewById(R.id.btn_rechercher);	

	      victoire_defaite = (Switch) rootView.findViewById(R.id.victoire_defaite);
	      victoire_defaite.setChecked(true);// Par défaut victoire
	 
	     

	      selectpoints = (Spinner) rootView.findViewById(R.id.selectpoints);
	      	monbouton.setTypeface(font_titre);
	        monbouton.setOnClickListener(new View.OnClickListener() {
	        	  public void onClick(View v) 
	        	  {
	        		  if (( text.getText().toString().trim().equals("")) || text2.getText().toString().trim().equals(""))
	        		  	{Toast.makeText(getActivity(), "Vous avez oublie un champ", Toast.LENGTH_SHORT).show(); }
	        		  else
	        		  { 
	        			  Calcul_points(); //Appel la fonction calcul de points
	        		  }

	        	  }
	        	  });
	        

	       rechercher.setOnClickListener(new View.OnClickListener() {
	        	  @SuppressLint("NewApi") public void onClick(View v2) 
	        	  {	
	        		  	//On test si le wifi est activé ou la 3G
	        		  	if(Wifi.isWifiEnabled() || internet.isActiveNetworkMetered())
	        		  		{
			        			Intent intent = new Intent(getActivity(), Rechercher_Joueur.class);
			        			startActivity(intent);
	        		  		}
	        		  	else
	        		  		{
	        		  			POPUP_activationWIFI_3G(); 			
	        		  		}
	        	  }

	});

		return rootView;
	}
	

	public void AlimenterCoefficient() {
		List<Double> list_coefficient = new ArrayList<Double>();
		list_coefficient.add(1.0);
	  	list_coefficient.add(0.5);
	  	list_coefficient.add(0.75);
	  	list_coefficient.add(1.25);
	  	list_coefficient.add(1.5);
	  	
	  	ArrayAdapter<Double> dataAdapter = new ArrayAdapter<Double>(
	  			getActivity(),android.R.layout.simple_spinner_item, list_coefficient);
	  	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	
	  	 coefficient = (Spinner) rootView.findViewById(R.id.coefficient);
	     coefficient.setAdapter(dataAdapter);
	     
		
	}

	@Override
	public void setMenuVisibility(final boolean visible) {
		super.setMenuVisibility(visible);
		if (visible) {
			
			if (ini > 0) 
			{
			 chargerSpinner();	
			} else {
				ini = ini + 1;
			}
		}
	}
	
	private void chargerSpinner() {

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
				text.setText(""); //On remet l'ediText vide
				text.append(bd_joueur.RemplirPoints(nom,prenom)); //On charge les points de la personne selectionner dans le spinner
				bd_joueur.close();
			}
			public void onNothingSelected(AdapterView<?> parent)
			{
				
			}
		});
   
		List<String> list = new ArrayList<String>();
		FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
		bd_joueur.open();
		 ArrayList<Integer> listeidjoueur = new ArrayList<Integer>();
		 listeidjoueur = bd_joueur.ListerIDjoueur();

		for (Integer s : listeidjoueur)
		{
			list.add(bd_joueur.DonnerNomPrenom(s));
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity().getBaseContext(),
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapter.notifyDataSetChanged();
		selectpoints.setAdapter(dataAdapter);
	}
		
	
		
	private double Calcul_points()
	{
		if( text.getText().toString().length() == 0 )
	            text.setError( "Vos points sont obligatoires!" );
		  
	        		 nombre1 = Float.parseFloat(text.getText().toString());
	        		 nombre2 = Float.parseFloat(text2.getText().toString());
	        		  double lecoefficient = Double.parseDouble(String.valueOf(coefficient.getSelectedItem()));
	        		  double difference = (nombre1 - nombre2);
	        		  difference= Math.abs(difference);
	        		   

	        		  			//Si victoire
						        if (victoire_defaite.isChecked())
						        {
						        	Victoire_Defaite="Victoire";
						        	//Victoire anormale
						        	if(nombre1 <= nombre2)
						        	{
						        		
							        	if(difference >=500){ points = 40.0 * lecoefficient;}
							        	if(difference < 500 && difference>= 300){ points = 28.0 * lecoefficient;}
							        	if(difference < 400 && difference>= 200){ points = 22.0 * lecoefficient;}
							        	if(difference < 300 && difference>= 100){ points = 17.0 * lecoefficient;}
							        	if(difference < 200 && difference>= 150){ points = 13.0 * lecoefficient;}
							        	if(difference < 150 && difference>= 100){ points = 10.0 * lecoefficient;}
							        	if(difference < 100 && difference>= 50){ points = 8.0 * lecoefficient;}
							        	if(difference < 50 && difference>= 25){ points = 7.0 * lecoefficient;}
							        	if(difference < 25 && difference>= 0){ points = 6.0 * lecoefficient;}
							        	
						        	}
						        	else if(nombre1 >= nombre2)
						        		
						        	{
						        		if(difference >= 500){ points = 0.0 * lecoefficient;}
							        	if(difference < 500 && difference>= 300){ points = 0.5 * lecoefficient;}
							        	if(difference < 400 && difference>= 200){ points = 1.0 * lecoefficient;}
							        	if(difference < 300 && difference>= 200){ points = 2.0 * lecoefficient;}
							        	if(difference < 200 && difference>= 150){ points = 3.0 * lecoefficient;}
							        	if(difference < 150 && difference>= 100){ points = 4.0 * lecoefficient;}
							        	if(difference < 100 && difference>= 50){ points = 5.0 * lecoefficient;}
							        	if(difference < 50 && difference >= 25){ points = 5.5 * lecoefficient;}
							        	if(difference < 25 && difference>= 0){ points = 6.0 * lecoefficient;}
						        	}	
						        }
						        //Si defaite 
						        else
						        {
						        	Victoire_Defaite="Defaite";
						        	//defaite normale
						        	if(nombre1 <= nombre2)
						        	{
							        	if(difference >= 500){ points = 0.0 * lecoefficient;}
							        	if(difference < 500 && difference>= 300){ points = 0.0 * lecoefficient;}
							        	if(difference < 400 && difference>= 200){ points = -0.5 * lecoefficient;}
							        	if(difference < 300 && difference>= 100){ points = -1.0 * lecoefficient;}
							        	if(difference < 200 && difference>= 150){ points = -2.0 * lecoefficient;}
							        	if(difference < 150 && difference>= 100){ points = -3.0 * lecoefficient;}
							        	if(difference < 100 && difference>= 50){ points = -4.0 * lecoefficient;}
							        	if(difference < 50 && difference>= 25){ points = -4.5 * lecoefficient;}
							        	if(difference < 25 && difference>= 0){ points = -5.0 * lecoefficient;}
						        	}
						        	//defaite anormale
						        	else if(nombre1 >= nombre2)
						        	{
						        		if(difference >= 500){ points = -29.0 * lecoefficient;}
							        	if(difference < 500 && difference>= 300){ points = -20.0 * lecoefficient;}
							        	if(difference < 400 && difference>= 200){ points = -16.0 * lecoefficient;}
							        	if(difference < 300 && difference>= 100){ points = -12.5 * lecoefficient;}
							        	if(difference < 200 && difference>= 150){ points = -10.0 * lecoefficient;}
							        	if(difference < 150 && difference>= 100){ points = -8.0 * lecoefficient;}
							        	if(difference < 100 && difference>= 50){ points = -7.0 * lecoefficient;}
							        	if(difference < 50 && difference>= 25){ points = -6.0 * lecoefficient;}
							        	if(difference < 25 && difference>= 0){ points = -5.0 * lecoefficient;}
						        	}
						        }

			nouveauxpoints = nombre1 + points;
			
			
			
			AlertDialog.Builder ald_memoire = new AlertDialog.Builder(getActivity());
			ald_memoire.setTitle("Informations sur vos points");
			ald_memoire.setIcon(R.drawable.information);
			ald_memoire.setMessage(Html.fromHtml(
					"Gain : <b>"+ points + "</b>"+ "</p>" + " et "
							+ "</b>"+ "vos nouveaux points: <b>" + nouveauxpoints + "</b>"
					));
			ald_memoire.setPositiveButton("Enregistrer ce match dans votre historique",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							 FonctionHistorique bd_Historique = new FonctionHistorique(getActivity());
							 bd_Historique.open();
							 
							 FonctionJoueur bd_joueur = new FonctionJoueur(getActivity());
							 bd_joueur.open();
							 
							 nombredejoueur = bd_joueur.CompterNbJoueur();
							 if(nombredejoueur==0)
							 {
								 Toast.makeText(getActivity(), "Impossible d'enregistrer car vous n'avez pas de joueur créé", Toast.LENGTH_LONG).show();
							 }

							 else
							 {
							 
							 String donnees = choixListe;
							 String[] parts = donnees.split(" - ");
							 String nom = parts[0]; 
							 String prenom = parts[1]; 
							 int idjoueur = bd_joueur.RecupererIdByJoueur(nom, prenom);
							 int nbhistorique = bd_Historique.SelectDernierHistorique()+1;
							 
							 double DernierPointJoueur = bd_Historique.DonnerPointHistoriqueJoueur(idjoueur);
							 
							 if(DernierPointJoueur==0)
							 {
								 //C'est à dire que la personne n'as pas encore d'historique de matchs
								 DernierPointJoueur = nouveauxpoints; 
							 }
							 else
							 {
								 //La personne posséde déjà un historique
								 DernierPointJoueur = DernierPointJoueur+ points;
							 }
							  
							 bd_Historique.insererHistorique(nbhistorique, idjoueur, nombre2, points, Victoire_Defaite,DernierPointJoueur);
							 
							 
							 //on prend notre joueur que l'on veut mettre à jour
							 Joueur j = bd_joueur.ListerJoueur(idjoueur);
							 j.setidjoueur(idjoueur);
							 j.setPointsjoueur_courant(DernierPointJoueur);			
							 bd_joueur.majJoueur(idjoueur, j);
							 text2.setText("");
							 text2.setHint(R.string.nombre2);
							 bd_joueur.close();
							 bd_Historique.close();
							 }
						}
					});
			ald_memoire.setNegativeButton("Ne pas sauvegarder",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			AlertDialog helpDialog = ald_memoire.create();
			helpDialog.show();

			return nouveauxpoints;
	}
	
	public void POPUP_activationWIFI_3G() 
	{
		AlertDialog.Builder ald_memoire = new AlertDialog.Builder(getActivity());
		ald_memoire.setTitle("Aucune connexion internet trouvé ");
		ald_memoire.setMessage("Voullez-vous activer internet ? ");
		ald_memoire.setIcon(R.drawable.wifi);
		ald_memoire.setPositiveButton("Oui",new DialogInterface.OnClickListener() {
					@SuppressLint("NewApi") public void onClick(DialogInterface dialog, int which) {
						Wifi.setWifiEnabled(true); //activation du wifi
						activer_ou_desactiver_connexion_donnees(getActivity(),true); //ACTIVATION 3G
						
					}
				});
		ald_memoire.setNegativeButton("Non",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		AlertDialog helpDialog = ald_memoire.create();
		helpDialog.show();
	}
	
	private static void activer_ou_desactiver_connexion_donnees(Context paramContext, boolean enable) {
		try 
		{
			ConnectivityManager connectivityManager = (ConnectivityManager) paramContext.getSystemService("connectivity");
			Method setMobileDataEnabledMethod = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(connectivityManager, enable);
		} catch (Exception e) {
		e.printStackTrace();
		}
		}
	
	public void onPause()
	{
		super.onPause();
		
	}
	
	public void onResume()
	{
		super.onResume();
		
	}
	

	public void ajouterAlarme(int day, int hour, int minute)
	{
		 Calendar c = Calendar.getInstance();
	     c.setTimeInMillis(System.currentTimeMillis());
	     GregorianCalendar calendar = new GregorianCalendar();
	     calendar.setTime(c.getTime());
	     int jour = calendar.get(Calendar.DAY_OF_MONTH);
	     System.out.println("jour :" + jour);
	   
	     if(jour > 1)
	     {
	    	 int mois = calendar.get(Calendar.MONTH);
		     int mois_suivant = mois + 1;
		     c.set(Calendar.MONTH,mois_suivant);
		     c.set(Calendar.DAY_OF_MONTH,day);
		     c.set(Calendar.HOUR_OF_DAY,hour);
		     c.set(Calendar.MINUTE,minute);
		     Intent i = new Intent(getActivity(), Notification.class);
		     PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, i, 0);
		     am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi); // Millisec * Second * Minute 
	     }
	     else
	     { 
	   
		     c.set(Calendar.DAY_OF_MONTH,day);
		     c.set(Calendar.HOUR_OF_DAY,hour);
		     c.set(Calendar.MINUTE,minute);
		     Intent i = new Intent(getActivity(), Notification.class);
		     PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, i, 0);
		     am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi); // Millisec * Second * Minute
	     }    
	}
	

}



