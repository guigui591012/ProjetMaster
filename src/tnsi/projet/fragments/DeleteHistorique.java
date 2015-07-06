package tnsi.projet.fragments;

import java.util.ArrayList;
import java.util.ListIterator;
import tnsi.projet.base.FonctionHistorique;
import tnsi.projet.base.Historique;
import com.pongiste.calping.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * ACTIVITE DeleteHistorique
 * Elle est appelé si l'utilisateur décide de supprimé un ou plusieurs historiques
 * Elle permet de sélectionner dans une liste, les historiques à supprimer.
 *   
 * @author guillaumehautcoeur
 */
public class DeleteHistorique extends Activity implements OnClickListener {

	Button button;
	ListView listView;
	ArrayAdapter<Historique> adapter;
	int idjoueur ;
	double pointCourantPrécédent, pointCourantActuel, pointGagné ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vue_select_historique);
		Bundle extras = getIntent().getExtras();
		//on récupére le parametre place dans idjoueur
		if (extras != null) {
		  idjoueur = extras.getInt("idjoueur");
		}
		creerListe();
	
		button.setOnClickListener(this);
		
	}

	/**
	 * Créer la liste des historiques de la personnes
	 */
	public void creerListe() {
		FonctionHistorique bd_historique = new FonctionHistorique(this);
		bd_historique.open();
		ArrayList<Historique> j = bd_historique.ListerAllHistorique(idjoueur);

		findViewsById();

		adapter = new ArrayAdapter<Historique>(this,
				android.R.layout.simple_list_item_multiple_choice, j);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setAdapter(adapter);
		
		
	}

	/**
	 * recherche pas id
	 */
	private void findViewsById() {
		listView = (ListView) findViewById(R.id.ListView01);
		button = (Button) findViewById(R.id.testbutton);
	}

	public void onClick(View v) {
		SparseBooleanArray checked = listView.getCheckedItemPositions();
		ArrayList<Historique> selectedItems = new ArrayList<Historique>();
		for (int i = 0; i < checked.size(); i++) {

			int position = checked.keyAt(i);

			//On récupére tout les éléments cochés
			if (checked.valueAt(i))
				selectedItems.add(adapter.getItem(position));
			// On va récupérer les informations dont l'on a besoin
			Historique j2 = adapter.getItem(position);
			
			//Historique du joueur a supprimer
			int idhistoriqueasupprimer = j2.getIdhistorique();

			FonctionHistorique bd_historique = new FonctionHistorique(this);
			bd_historique.open();
			//On supprime tout les historique à supprimer
			bd_historique.SupprimerHistoriqueByID(idhistoriqueasupprimer);

			bd_historique.close();

		}
		mise_à_jour_points();
		creerListe();
		DeleteHistorique.this.finish();
	}
	
	public void mise_à_jour_points()
	{
		FonctionHistorique bd_historique = new FonctionHistorique(this);
		bd_historique.open();
		ArrayList<Historique> liste = new ArrayList<Historique>();
		liste = bd_historique.ListerAllHistorique(idjoueur);
				
		ArrayList<Integer> list_des_id = new ArrayList<Integer>(); 
		
			for (Historique s : liste)
			{
				list_des_id.add(s.getIdhistorique());
			}
			
			ListIterator it = list_des_id.listIterator();
			
			
			for(int i = 0; i < list_des_id.size() ; i++)
			{
		
					it.next();
					int idHistoriqueprécédent = (Integer) it.previous();
					int idHistoriqueactuel = (Integer) it.next();
					
					if (i == 0)
					{
						 pointCourantPrécédent = bd_historique.DonnerPointsCourantHistorique(idHistoriqueactuel);
						 pointCourantActuel = pointCourantPrécédent ;
					}
					else
					{
						
						 pointCourantPrécédent = pointCourantActuel;
						 pointGagné = bd_historique.DonnerPointsGagnéHistorique(idHistoriqueactuel);
							
						 pointCourantActuel = pointCourantPrécédent + pointGagné ;
							
						    Historique j = bd_historique.ListerHistorique(idHistoriqueactuel);
							j.setIdhistorique(idHistoriqueactuel);
							j.setPointsCourant(pointCourantActuel);
							bd_historique.majHistorique(idHistoriqueactuel, j);
					}
					
				
			}
		
	
	}

	public void OnPause() {
		super.onPause();
	}

	public void onResume() {
		super.onResume();
	}

	public void onRestart() {
		super.onRestart();
	}

}