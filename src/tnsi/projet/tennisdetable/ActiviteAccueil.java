package tnsi.projet.tennisdetable;
 
import tnsi.projet.tennisdetable.AffichagePage;

import com.pongiste.calping.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

 
public class ActiviteAccueil extends FragmentActivity implements
        ActionBar.TabListener {
 
    private ViewPager viewPager;
    private AffichagePage mAdapter;
    private ActionBar actionBar;
    // Titre des onglets
    private String[] tabs = { "  Accueil", "  Joueur(s)", "  Historique", "  Statistiques" ,"  Graphiques"};
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_accueil);
 
        // Initilisation
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        mAdapter = new AffichagePage(getSupportFragmentManager());
 
        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);  
        
        // Ajout des onglets
        int indice = 0;
        for (String tab_name : tabs) {
        	if (indice == 0){
        		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.accueil).setText(tab_name)
                        .setTabListener(this));
        	}else if(indice == 1){
        		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.joueur).setText(tab_name)
                        .setTabListener(this));
        	}else if(indice == 2){
        		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.historique).setText(tab_name)
                        .setTabListener(this));
            	}
        	else if(indice == 3){
        		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.statistique).setText(tab_name)
                        .setTabListener(this));
            	}
        	else if(indice == 4){
        		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.graphique).setText(tab_name)
                        .setTabListener(this));
            	}
        	indice = indice + 1;
        }
 
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
 
            
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
 
  
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
 
    
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }
 
    
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }
 
}