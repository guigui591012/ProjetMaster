package tnsi.projet.base;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Fonction qui permette d'échanger des données avec la base de données
 * HISTORIQUE
 * 
 * @author guillaumehautcoeur
 */
public class FonctionHistorique {

	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "historique.db";
	private static final String TABLE_HISTORIQUE = "historique";
	private static final String COL_IDHISTORIQUE = "idhistorique";
	private static final int NUM_COL_IDHISTORIQUE = 0;
	private static final String COL_IDJOUEUR = "idjoueur";
	private static final int NUM_COL_IDJOUEUR = 1;
	private static final String COL_POINTSADVERSAIRE = "PointsAdversaire";
	private static final int NUM_COL_POINTSADVERSAIRE = 2;
	private static final String COL_POINTSGAGNE = "PointsGagne";
	private static final int NUM_COL_POINTSGAGNE = 3;
	private static final String COL_VICTOIRE_DEFAITE = "Victoire_Defaite";
	private static final int NUM_COL_VICTOIRE_DEFAITE = 4;
	private static final String COL_POINTSCOURANTS = "PointsCourant";
	private static final int NUM_COL_POINTSCOURANTS = 5;

	private SQLiteDatabase bdd;
	private MaBaseSQLiteHistorique MaBaseSQLiteHistorique;

	public FonctionHistorique(Context context) {

		MaBaseSQLiteHistorique = new MaBaseSQLiteHistorique(context, NOM_BDD,
				null, VERSION_BDD);
	}

	/**
	 * Ouvrir la base de donnée
	 */
	public void open() {
		bdd = MaBaseSQLiteHistorique.getWritableDatabase();
	}

	/**
	 * Fermer la base de donnée
	 */
	public void close() {
		bdd.close();
	}

	/**
	 * Retourner la base de donnée
	 */
	public SQLiteDatabase getBDD() {
		return bdd;
	}

	/**
	 * Fonction qui permet d'insérer un historique de joueur dans la base de
	 * données
	 * 
	 * @param idhistorique
	 * @param idjoueur
	 * @param PointsAdversaire
	 * @param PointsGagne
	 * @param Victoire_Defaite
	 * @param PointsCourant
	 * @return
	 */
	public long insererHistorique(int idhistorique, int idjoueur,
			double PointsAdversaire, double PointsGagne,
			String Victoire_Defaite, double PointsCourant) {

		ContentValues values = new ContentValues();
		values.put(COL_IDHISTORIQUE, idhistorique);
		values.put(COL_IDJOUEUR, idjoueur);
		values.put(COL_POINTSADVERSAIRE, PointsAdversaire);
		values.put(COL_POINTSGAGNE, PointsGagne);
		values.put(COL_VICTOIRE_DEFAITE, Victoire_Defaite);
		values.put(COL_POINTSCOURANTS, PointsCourant);
		return bdd.insert(TABLE_HISTORIQUE, null, values);
	}

	/**
	 * Fonction qui compte le nombre d'historique dans la base de données
	 * 
	 * @return int le nombre d'historique
	 */
	public int CompterNbHistorique() {
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		int numbersOfrows = 0;

		Cursor c = db.rawQuery("select count(*) from historique", null);
		if (c.moveToFirst()) {
			numbersOfrows = c.getInt(0);
		}
		return numbersOfrows;
	}

	/**
	 * Supprimer un historique par son id
	 * 
	 * @param id
	 * @return
	 */
	public int SupprimerHistoriqueByID(int id) {
		return bdd
				.delete(TABLE_HISTORIQUE, COL_IDHISTORIQUE + " = " + id, null);
	}

	/**
	 * Fonction qui donne le nombre de victoire d'un joueur
	 * 
	 * @param idjoueur
	 * @return le nombre de victoire
	 */
	public double DonnerVictoire(int idjoueur) {
		double victoire = 0.0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select count() from historique where idjoueur  = \""
						+ idjoueur + "\" " + "and Victoire_Defaite" + " = \""
						+ "Victoire" + "\" ", null);
		if (c.moveToFirst()) {
			victoire = c.getDouble(0);
		}
		return victoire;
	}

	/**
	 * Fonction qui compte le nombre d'historique pour un joueur
	 * 
	 * @param idjoueur
	 * @return le nombre d'historique
	 */
	public double CompterHistorique(int idjoueur) {
		double nb_historique = 0.0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select count() from historique where idjoueur  = \""
						+ idjoueur + "\" ", null);
		if (c.moveToFirst()) {
			nb_historique = c.getDouble(0);
		}
		return nb_historique;
	}

	/**
	 * Fonction qui permet de mettre à jour l'historique d'un joueur En entrée,
	 * il prend son idjoueur et un objet historique
	 * 
	 * @param id
	 * @param s
	 * @return int
	 */
	public int majHistorique(int id, Historique s) {

		ContentValues values = new ContentValues();
		values.put(COL_IDHISTORIQUE, s.getIdhistorique());
		values.put(COL_IDJOUEUR, s.getIdjoueur());
		values.put(COL_POINTSADVERSAIRE, s.getPointsAdversaire());
		values.put(COL_POINTSGAGNE, s.getPointsGagne());
		values.put(COL_VICTOIRE_DEFAITE, s.getVictoire_Defaite());
		values.put(COL_POINTSCOURANTS, s.getPointsCourant());

		return bdd.update(TABLE_HISTORIQUE, values, COL_IDHISTORIQUE + " = "
				+ id, null);
	}

	/**
	 * Fonction qui supprime l'historique d'un joueur
	 * 
	 * @param idjoueur
	 */
	public void SupprimerHistorique(int idjoueur) {
		int id_joueur = idjoueur;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery("delete from historique where idjoueur = "
				+ id_joueur, null);
		if (c.moveToFirst()) {
			id_joueur = c.getInt(0);
		}

	}

	/**
	 * Fonction qui selectionne le dernier historique
	 * 
	 * @param idjoueur
	 * @return
	 */
	/**
	 * @return
	 */
	public int SelectDernierHistorique() {
		int id_historique = 0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery("select max(idhistorique) from historique ",
				null);
		if (c.moveToFirst()) {
			id_historique = c.getInt(0);
		}
		return id_historique;

	}

	/**
	 * Fonction qui liste l'historique d'un match en fonction d'un id historique
	 * 
	 * @param idHistorique
	 * @return un objet historique
	 */
	public Historique ListerHistorique(int idHistorique) {

		Cursor c = bdd.query(TABLE_HISTORIQUE, new String[] { COL_IDHISTORIQUE,
				COL_IDJOUEUR, COL_POINTSADVERSAIRE, COL_POINTSGAGNE,
				COL_VICTOIRE_DEFAITE, COL_POINTSCOURANTS

		}, COL_IDHISTORIQUE + " = \"" + idHistorique + "\"", null, null, null,
				null);
		return cursorToHistorique(c);
	}

	/**
	 * Fonction qui liste tout les joueurs de la base et les stockes dans une
	 * arraylist
	 * 
	 * @return ArrayList<Joueur>
	 */
	public ArrayList<Historique> ListerAllHistorique(int idjoueur) {
		Historique s;
		ArrayList<Historique> meshistoriques = new ArrayList<Historique>();
		Cursor c = bdd.rawQuery("select * from historique where idjoueur = "
				+ idjoueur, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			s = new Historique(1, idjoueur, 0.0, 0.0, "Victoire", 0.0);
			s.setIdhistorique(c.getInt(NUM_COL_IDHISTORIQUE));
			s.setIdjoueur(c.getInt(NUM_COL_IDJOUEUR));
			s.setPointsAdversaire(c.getDouble(NUM_COL_POINTSADVERSAIRE));
			s.setPointsGagne(c.getDouble(NUM_COL_POINTSGAGNE));
			s.setVictoire_Defaite(c.getString(NUM_COL_VICTOIRE_DEFAITE));
			s.setPointsCourant(c.getDouble(NUM_COL_POINTSCOURANTS));
			meshistoriques.add(s);
		}
		c.close();

		return meshistoriques;
	}

	/**
	 * Fonction qui donne le dernier point courant d'un joueur Cette fonction
	 * permet d'ajouter le match gagné ou perdu en fonction de ses derniers
	 * points
	 * 
	 * @param idjoueur
	 * @return le nombre de point
	 */
	public double DonnerPointHistoriqueJoueur(int idjoueur) {
		double pointsjoueur = 0.0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select PointsCourant from historique where idjoueur = "
						+ idjoueur, null);
		if (c.moveToLast()) {
			pointsjoueur = c.getDouble(0);
		}
		return pointsjoueur;
	}

	/**
	 * Fonction qui donne le meilleure classement d'un joueur
	 * 
	 * @param idjoueur
	 * @return le meilleure classement en double
	 */
	public double DonnerMeilleureClassementJoueur(int idjoueur) {
		double pointsjoueur = 0.0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select max(PointsCourant) from historique where idjoueur = "
						+ idjoueur, null);
		if (c.moveToLast()) {
			pointsjoueur = c.getDouble(0);
		}
		return pointsjoueur;
	}

	/**
	 * Fonction qui donne le plus bas classement d'un joueur
	 * 
	 * @param idjoueur
	 * @return le plus bas classement en double
	 */
	public double DonnerPlusBasClassementJoueur(int idjoueur) {
		double pointsjoueur = 0.0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select min(PointsCourant) from historique where idjoueur = "
						+ idjoueur, null);
		if (c.moveToLast()) {
			pointsjoueur = c.getDouble(0);
		}
		return pointsjoueur;
	}

	/**
	 * Fonction qui donne les points courant d'un historique
	 * 
	 * @param idjoueur
	 * @return les points courants
	 */
	public double DonnerPointsCourantHistorique(int idhistorique) {
		double idhist = 0.0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select PointsCourant from historique where idhistorique = "
						+ idhistorique, null);
		if (c.moveToLast()) {
			idhist = c.getDouble(0);
		}
		return idhist;
	}
	
	/**
	 * Fonction qui donne les points gagné d'un historique
	 * 
	 * @param idhistorique
	 * @return 
	 */
	public double DonnerPointsGagnéHistorique(int idhistorique) {
		double idhist = 0.0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select PointsGagne from historique where idhistorique = "
						+ idhistorique, null);
		if (c.moveToLast()) {
			idhist = c.getDouble(0);
		}
		return idhist;
	}

	/**
	 * Fonction qui détermine la plus grosse défaite d'un joueur C'est à dire
	 * une défaite sur un joueur ayant le plus grand nombre de point d'écart
	 * 
	 * @param idjoueur
	 * @return le nombre de point du joueur
	 */
	public double DonnerMeilleureContreJoueur(int idjoueur) {
		double pointsjoueur = 0.0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select min(PointsAdversaire) from historique where idjoueur  = \""
						+ idjoueur + "\" " + "and Victoire_Defaite" + " = \""
						+ "Defaite" + "\" ", null);
		if (c.moveToLast()) {
			pointsjoueur = c.getDouble(0);
		}
		return pointsjoueur;
	}

	/**
	 * Fonction qui détermine la moyenne des classements joués
	 * 
	 * @param idjoueur
	 * @return le nombre de point du joueur
	 */
	public double DonnerMoyenneClassementJoueurJouee(int idjoueur) {
		double pointsjoueur = 0.0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select avg(PointsAdversaire) from historique where idjoueur  = \""
						+ idjoueur + "\" ", null);
		if (c.moveToLast()) {
			pointsjoueur = c.getDouble(0);
		}
		return pointsjoueur;
	}

	/**
	 * Fonction qui détermine la plus grosse victoire d'un joueur C'est à dire
	 * une victoire sur un joueur ayant le plus grand nombre de point d'écart
	 * 
	 * @param idjoueur
	 * @return le nombre de point du joueur
	 */
	public double DonnerMeilleurePerformanceJoueur(int idjoueur) {
		double pointsjoueur = 0.0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select max(PointsAdversaire) from historique where idjoueur  = \""
						+ idjoueur + "\" " + "and Victoire_Defaite" + " = \""
						+ "Victoire" + "\" ", null);
		if (c.moveToLast()) {
			pointsjoueur = c.getDouble(0);
		}
		return pointsjoueur;
	}

	public double DonnerIDPrécedent(int idhistorique) {
		double pointsjoueur = 0.0;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select idhistorique from historique where idhistorique  < \""
						+ idhistorique + " limit 0,1" + "\" ", null);
		if (c.moveToLast()) {
			pointsjoueur = c.getDouble(0);
		}
		return pointsjoueur;
	}

	/**
	 * Fonction qui stocke dans une liste l'historique des points courants d'un
	 * joueur
	 * 
	 * @param idjoueur
	 * @return une arraylist d'integer
	 */
	public ArrayList<Integer> HistoriquePoints(int idjoueur) {
		ArrayList<Integer> points = new ArrayList<Integer>();

		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db.rawQuery("select  PointsCourant from historique"
				+ " where idjoueur  = \"" + idjoueur + "\" ", null);
		while (c.moveToNext()) {
			points.add(c.getInt(0));
		}
		return points;
	}
	
	
	
	

	/**
	 * Fonction qui stocke dans une tout l'historique d'un joueur dans une
	 * arraylist d'arraylist
	 * 
	 * @param idjoueur
	 * @returnune arraylist d'arraylist
	 */
	public ArrayList<ArrayList<String>> DonnerHistorique(int idjoueur) {
		ArrayList<ArrayList<String>> informations = new ArrayList<ArrayList<String>>();
		ArrayList<String> info;
		SQLiteDatabase db = MaBaseSQLiteHistorique.getReadableDatabase();
		Cursor c = db
				.rawQuery(
						"select PointsAdversaire, Victoire_Defaite,PointsGagne, PointsCourant from historique"
								+ " where idjoueur  = \""
								+ idjoueur
								+ "\" order by idhistorique DESC", null);
		while (c.moveToNext()) {
			info = new ArrayList<String>();
			info.add(c.getString(0));
			info.add(c.getString(1));
			info.add(c.getString(2));
			info.add(c.getString(3));

			informations.add(info);
		}
		return informations;

	}

	private Historique cursorToHistorique(Cursor c) {

		if (c.getCount() == 0)
			return null;

		// Sinon on se place sur le premier ÈlÈment
		c.moveToFirst();

		Historique s = new Historique();
		// on lui affecte toutes les infos gr‚ce aux infos contenues dans le
		// Cursor
		s.setIdhistorique(c.getInt(NUM_COL_IDHISTORIQUE));
		s.setIdjoueur(c.getInt(NUM_COL_IDJOUEUR));
		s.setPointsAdversaire(c.getDouble(NUM_COL_POINTSADVERSAIRE));
		s.setPointsGagne(c.getDouble(NUM_COL_POINTSGAGNE));
		s.setVictoire_Defaite(c.getString(NUM_COL_VICTOIRE_DEFAITE));
		s.setPointsCourant(c.getDouble(NUM_COL_POINTSCOURANTS));

		// On ferme le cursor
		c.close();

		// On retourne le sondage
		return s;
	}

}