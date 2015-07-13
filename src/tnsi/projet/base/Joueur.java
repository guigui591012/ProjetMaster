package tnsi.projet.base;

public class Joueur {
    public int idjoueur;
    public String NomJoueur;
    public String PrenomJoueur;
    public double Pointsjoueur_mensuel;
    public double Pointsjoueur_courant;

    public Joueur() {
    }

    /**
     * CONSTRUCTEUR JOUEUR
     *
     * @param idjoueur
     * @param NomJoueur
     * @param PrenomJoueur
     * @param Pointsjoueur_mensuel
     * @param Pointsjoueur_courant
     */
    public Joueur(int idjoueur, String NomJoueur, String PrenomJoueur, double Pointsjoueur_mensuel, double Pointsjoueur_courant) {
        this.idjoueur = idjoueur;
        this.NomJoueur = NomJoueur;
        this.PrenomJoueur = PrenomJoueur;
        this.Pointsjoueur_mensuel = Pointsjoueur_mensuel;
        this.Pointsjoueur_courant = Pointsjoueur_courant;
    }

    public double getPointsjoueur_mensuel() {
        return Pointsjoueur_mensuel;
    }

    public void setPointsjoueur_mensuel(double pointsjoueur_mensuel) {
        Pointsjoueur_mensuel = pointsjoueur_mensuel;
    }

    public double getPointsjoueur_courant() {
        return Pointsjoueur_courant;
    }

    public void setPointsjoueur_courant(double pointsjoueur_courant) {
        Pointsjoueur_courant = pointsjoueur_courant;
    }

    public int getIdjoueur() {
        return idjoueur;
    }

    public void setidjoueur(int idjoueur) {
        this.idjoueur = idjoueur;
    }

    public String getNomJoueur() {
        return NomJoueur;
    }

    public void setNomJoueur(String NomJoueur) {
        this.NomJoueur = NomJoueur;
    }

    public String getPrenomJoueur() {
        return PrenomJoueur;
    }

    public void setPrenomJoueur(String PrenomJoueur) {
        this.PrenomJoueur = PrenomJoueur;
    }


    // Sera utilisée par ArrayAdapter dans la ListView
    @Override
    public String toString() {
        return NomJoueur + " - " + PrenomJoueur + " - " + Pointsjoueur_mensuel;
    }
}
