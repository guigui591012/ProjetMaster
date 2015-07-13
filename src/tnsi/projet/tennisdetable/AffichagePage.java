package tnsi.projet.tennisdetable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tnsi.projet.fragments.FragmentAccueil;
import tnsi.projet.fragments.FragmentFiche;
import tnsi.projet.fragments.FragmentGraphique;
import tnsi.projet.fragments.FragmentHistorique;
import tnsi.projet.fragments.FragmentPerformance;

public class AffichagePage extends FragmentPagerAdapter {

    public AffichagePage(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new FragmentAccueil();
            case 1:
                return new FragmentFiche();
            case 2:
                return new FragmentHistorique();
            case 3:
                return new FragmentPerformance();
            case 4:
                return new FragmentGraphique();
        }

        return null;
    }

    @Override
    public int getCount() {
        // Nombre d'onglets
        return 5;
    }

}