package tnsi.projet.base;

public class Historique {
    public int idhistorique;
    public int idjoueur;
    public double PointsAdversaire;
    public double PointsGagne;
    public String Victoire_Defaite;
    public double PointsCourant;

    public Historique() {
    }

    /**
     * CONSTRUCTEUR HISTORIQUE
     *
     * @param idhistorique
     * @param idjoueur
     * @param PointsAdversaire
     * @param PointsGagne
     * @param Victoire_Defaite
     * @param PointsCourant
     */
    public Historique(int idhistorique, int idjoueur, double PointsAdversaire, double PointsGagne, String Victoire_Defaite
            , double PointsCourant) {

        this.idhistorique = idhistorique;
        this.idjoueur = idjoueur;
        this.PointsAdversaire = PointsAdversaire;
        this.PointsGagne = PointsGagne;
        this.Victoire_Defaite = Victoire_Defaite;
        this.PointsCourant = PointsCourant;

    }

    /**
     * return l'id historique
     *
     * @return
     */
    public int getIdhistorique() {
        return idhistorique;
    }

    /**
     * enregistre l'idhistorique en parametre
     *
     * @param idhistorique
     */
    public void setIdhistorique(int idhistorique) {
        this.idhistorique = idhistorique;
    }

    /**
     * retourne l'id du joueur
     *
     * @return
     */
    public int getIdjoueur() {
        return idjoueur;
    }

    /**
     * enregistre l'id du joueur en parametre
     *
     * @param idjoueur
     */
    public void setIdjoueur(int idjoueur) {
        this.idjoueur = idjoueur;
    }

    /**
     * retourne les points de l'adversaire
     *
     * @return
     */
    public double getPointsAdversaire() {
        return PointsAdversaire;
    }

    /**
     * prend les points de l'adversaire en parametre
     *
     * @param pointsAdversaire
     */
    public void setPointsAdversaire(double pointsAdversaire) {
        PointsAdversaire = pointsAdversaire;
    }

    /**
     * retourne le nombre de points gagné ou perdu
     *
     * @return
     */
    public double getPointsGagne() {
        return PointsGagne;
    }

    /**
     * prend le nombre de points gagné ou perdu en parametre
     *
     * @param pointsGagne
     */
    public void setPointsGagne(double pointsGagne) {
        PointsGagne = pointsGagne;
    }

    /**
     * retourne si victoire ou défaire
     *
     * @return
     */
    public String getVictoire_Defaite() {
        return Victoire_Defaite;
    }

    /**
     * prend en parametre le résultat du match
     *
     * @param victoire_Defaite
     */
    public void setVictoire_Defaite(String victoire_Defaite) {
        Victoire_Defaite = victoire_Defaite;
    }

    /**
     * retourne le nmbre de points courant du joueur
     *
     * @return
     */
    public double getPointsCourant() {
        return PointsCourant;
    }

    /**
     * prend en parametre le npbmre de points courant du joueur
     *
     * @param pointsCourant
     */
    public void setPointsCourant(double pointsCourant) {
        PointsCourant = pointsCourant;
    }

    @Override
    public String toString() {
        return Victoire_Defaite + " sur " + PointsAdversaire + "  " + PointsGagne + " points";
    }


}

	