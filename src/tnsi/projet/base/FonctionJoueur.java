package tnsi.projet.base;

import java.util.ArrayList;

import tnsi.projet.base.MaBaseSQLiteJoueur;
import tnsi.projet.base.Joueur;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Fonction qui permette d'échanger des données 
 * avec la base de données JOUEUR
 * @author guillaumehautcoeur
 */
public class FonctionJoueur {

	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "joueur.db";
	private static final String TABLE_JOUEUR = "joueur";
	private static final String COL_IDJOUEUR = "idjoueur";
	private static final int NUM_COL_IDJOUEUR = 0;
	private static final String COL_NOMJOUEUR= "NomJoueur";
	private static final int NUM_COL_NOMJOUEUR = 1;
	private static final String COL_PRENOMJOUEUR = "PrenomJoueur";
	private static final int NUM_COL_PRENOMJOUEUR = 2;
	private static final String COL_POINTSJOUEUR_MENSUEL = "PointsJoueur_mensuel";
	private static final int NUM_COL_POINTSJOUEUR_MENSUEL = 3;
	private static final String COL_POINTSJOUEUR_COURANT = "PointsJoueur_courant";
	private static final int NUM_COL_POINTSJOUEUR_COURANT = 4;

	private SQLiteDatabase bdd;

	private MaBaseSQLiteJoueur MaBaseSQLiteJoueur;

	public FonctionJoueur(Context context) {
		MaBaseSQLiteJoueur = new MaBaseSQLiteJoueur(context, NOM_BDD, null,
				VERSION_BDD);
	}

	/**
	 * Fonction qui ouvre la base de donnée
	 */
	public void open() {
		bdd = MaBaseSQLiteJoueur.getWritableDatabase();

	}

	/**
	 * Fonction qui ferme la base de donnée
	 */
	public void close() {
		bdd.close();
	}

	/**
	 * Fonction qui retourne la base de donnée
	 */
	public SQLiteDatabase getBDD() {
		return bdd;
	}

	/**
	 * Inserer un joueur
	 * 
	 * @param idJoueur
	 * @param NomJoueur
	 * @param PrenomJoueur
	 * @param PointsJoueur
	 * @return
	 */
	public long insererJoueur(int idjoueur, String NomJoueur,String PrenomJoueur,String PointsJoueur_mensuel,String PointsJoueur_courant) {
		
		ContentValues values = new ContentValues();
		// on lui ajoute une valeur associee à une clé (qui est le nom de la
		// colonne dans laquelle on veut mettre la valeur)
		
		/**
		 * Permet de mettre en majuscule la première lettre du nom
		 */
		String nomjoueur = NomJoueur;
		char [] char_table = nomjoueur.toCharArray();
		char_table[0] = Character.toUpperCase(char_table[0]);
		nomjoueur = new String(char_table);
		
		/**
		 * Permet de mettre en majuscule la première lettre du prénom
		 */
		String prenomjoueur = PrenomJoueur;
		char [] char_table2 = prenomjoueur.toCharArray();
		char_table2[0] = Character.toUpperCase(char_table2[0]);
		prenomjoueur = new String(char_table2);
		
		
		values.put(COL_IDJOUEUR, idjoueur);
		values.put(COL_NOMJOUEUR, nomjoueur);
		values.put(COL_PRENOMJOUEUR, prenomjoueur);
		values.put(COL_POINTSJOUEUR_MENSUEL, PointsJoueur_mensuel);
		values.put(COL_POINTSJOUEUR_COURANT, PointsJoueur_courant);
		return bdd.insert(TABLE_JOUEUR, null, values);
	}
	


	/**
	 * Compter le nombre de Joueur
	 * 
	 * @param idjoueur
	 * @return le nombre de joueur dans la base de données
	 */
	public int CompterNbJoueur() {
		SQLiteDatabase db = MaBaseSQLiteJoueur.getReadableDatabase();
		int numbersOfrows = 0;

		Cursor c = db.rawQuery("select count(*) from joueur", null);
		if (c.moveToFirst()) {
			numbersOfrows = c.getInt(0);
		}
		return numbersOfrows;
	}

	/**
	 * Mettre à jour la table Joueur
	 * 
	 * @param id
	 * @param s
	 * @return
	 */
	public int majJoueur(int id, Joueur s) {

		ContentValues values = new ContentValues();
		values.put(COL_IDJOUEUR, s.getIdjoueur());
		values.put(COL_NOMJOUEUR, s.getNomJoueur());
		values.put(COL_PRENOMJOUEUR, s.getPrenomJoueur());
		values.put(COL_POINTSJOUEUR_MENSUEL, s.getPointsjoueur_mensuel());
		values.put(COL_POINTSJOUEUR_COURANT, s.getPointsjoueur_courant());
		return bdd.update(TABLE_JOUEUR, values, COL_IDJOUEUR + " = " + id,
				null);
	}
	
	


	/**
	 * Supprimer un joueur par son id
	 * 
	 * @param id
	 * @return
	 */
	public int SupprimerJoueurByID(int id) {
		return bdd.delete(TABLE_JOUEUR, COL_IDJOUEUR + " = " + id, null);
	}

	/**
	 * Lister les joueurs
	 * 
	 * @param id
	 * @return
	 */
	public Joueur ListerJoueur(int idJoueur) {

		Cursor c = bdd.query(TABLE_JOUEUR, new String[] { COL_IDJOUEUR,
				COL_NOMJOUEUR,COL_PRENOMJOUEUR, COL_POINTSJOUEUR_MENSUEL, COL_POINTSJOUEUR_COURANT }, COL_IDJOUEUR + " = \""
				+ idJoueur + "\"", null, null, null, null);
		return cursorToJoueur(c);
	}
	
	/**
	 * Fonction qui liste tout les joueurs de la base et les stockes dans une arraylist
	 * @return ArrayList<Joueur>
	 */
	public ArrayList<Joueur> ListerAllJoueur() {
		Joueur s;
		ArrayList<Joueur> mesjoueurs = new ArrayList<Joueur>();
		Cursor c = bdd.rawQuery("select * from Joueur ;",null);
		for(c.moveToFirst(); !c.isAfterLast();c.moveToNext())
		{
			s = new Joueur(1,null,null,0,0);
			s.setidjoueur(c.getInt(NUM_COL_IDJOUEUR));
			s.setNomJoueur(c.getString(NUM_COL_NOMJOUEUR));
			s.setPrenomJoueur(c.getString(NUM_COL_PRENOMJOUEUR));
			s.setPointsjoueur_mensuel(c.getDouble(NUM_COL_POINTSJOUEUR_MENSUEL));
			s.setPointsjoueur_courant(c.getDouble(NUM_COL_POINTSJOUEUR_COURANT));
			mesjoueurs.add(s);
		}
		c.close();
		
			
		return mesjoueurs;
	}
	
	
	/**
	 * Fonction qui liste tous les ids des joueurs
	 * @return une arraylist integer contenant les ID joueurs
	 */
	public ArrayList<Integer> ListerIDjoueur()
	{
		ArrayList<Integer> listeIDJoueur = new ArrayList<Integer>();
	
		SQLiteDatabase db = MaBaseSQLiteJoueur.getReadableDatabase();
		Cursor c = db.rawQuery("select idjoueur from joueur", null);
		while (c.moveToNext())
		{
			listeIDJoueur.add(c.getInt(0));	
		}
	return listeIDJoueur;
	}
	
	/**
	 * Fonction qui selectionne le dernier idjoueur 
	 * @return int nb joueur
	 */
	public int SelectDernierIDJoueur() {
		int idjoueur = 0;
		SQLiteDatabase db = MaBaseSQLiteJoueur.getReadableDatabase();
		Cursor c = db.rawQuery("select max(idjoueur) from joueur ", null);
		if (c.moveToFirst()) {
			idjoueur = c.getInt(0);
		}
		return idjoueur;
		
	}
	
	/**
	 * Fonction qui supprime un joueur de la base de données
	 * @param idjoueur
	 */
	public void SupprimerJoueur(int idjoueur) {
		int id_joueur=idjoueur;
		SQLiteDatabase db = MaBaseSQLiteJoueur.getReadableDatabase();
		Cursor c = db.rawQuery("delete from joueur where idjoueur = "+ id_joueur, null);
		if (c.moveToFirst()) {
			id_joueur = c.getInt(0);
		}
		
	}
	
	/**
	 * Fonction qui donne les points mensuel d'un joueur 
	 * @param idjoueur
	 * @return le nombre de point mensuel
	 */
	public double DonnerPointJoueur(int idjoueur) {
		double pointsjoueur = 0.0;
		SQLiteDatabase db = MaBaseSQLiteJoueur.getReadableDatabase();
		Cursor c = db.rawQuery("select Pointsjoueur_mensuel from joueur where idjoueur = "+ idjoueur, null);
		if (c.moveToFirst()) {
			pointsjoueur = c.getDouble(0);
		}
		return pointsjoueur;
	}
	
	/**
	 * Fonction qui donne les points courant d'un joueur 
	 * @param idjoueur
	 * @return le nombre de point courant
	 */
	public double DonnerPointCourantJoueur(int idjoueur) {
		double pointsjoueur = 0.0;
		SQLiteDatabase db = MaBaseSQLiteJoueur.getReadableDatabase();
		Cursor c = db.rawQuery("select Pointsjoueur_courant from joueur where idjoueur = "+ idjoueur, null);
		if (c.moveToFirst()) {
			pointsjoueur = c.getDouble(0);
		}
		return pointsjoueur;
	}
	
	/**
	 * Fonction qui donne le nom et prénom du joueur en fonction de son idjoueur
	 * @param idjoueur
	 * @return le nom et prénom du joueur
	 */
	public String DonnerNomPrenom(int idjoueur)
	{
		String information="";
		SQLiteDatabase db = MaBaseSQLiteJoueur.getReadableDatabase();
		Cursor c = db.rawQuery("select NomJoueur, PrenomJoueur from joueur where idjoueur = "+ idjoueur, null);
		if (c.moveToFirst()) {
			information = c.getString(0)+ " - " +c.getString(1);
		}
		return information;	
	}
	
		/**
		 * FONCTION qui permet de récupérer les points d'un joueur en fonction de son nom et prénom
		 * Fonction qui sert à remplir le nombre de point dans un EdiText
		 * @param nom
		 * @param prenom
		 * @return le nombre de points mensuel du joueur
		 */
	public String RemplirPoints(String nom, String prenom)
	{
		String nom_joueur=nom;
		String prenom_joueur=prenom;
		String points="";
		SQLiteDatabase db = MaBaseSQLiteJoueur.getReadableDatabase();
		Cursor c = db.rawQuery("select PointsJoueur_mensuel from joueur where NomJoueur  = \"" + nom_joueur + "\" " + "and PrenomJoueur" + " = \""
		+ prenom_joueur + "\" ", null);
		
		if (c.moveToFirst()) {
			points = c.getString(0);
		}
		return points;	
	}
	
	/**
	 * Récupérer l'idjoueur en fonction de son nom et prénom
	 * @param nom
	 * @param prenom
	 * @return idjoueur en int
	 */
	public int RecupererIdByJoueur(String nom, String prenom)
	{
		String nom_joueur=nom;
		String prenom_joueur=prenom;
		int id=0;
		SQLiteDatabase db = MaBaseSQLiteJoueur.getReadableDatabase();
		Cursor c = db.rawQuery("select idjoueur from joueur where NomJoueur  = \"" + nom_joueur + "\" " + "and PrenomJoueur" + " = \""
		+ prenom_joueur + "\" ", null);
		
		if (c.moveToFirst()) {
			id = c.getInt(0);
		}
		return id;	
	}
	
	/**
	 * Fonction liste tout les joueurs en cursor
	 * @return
	 */
	public Cursor getAllJoueur()
	{
		SQLiteDatabase db = MaBaseSQLiteJoueur.getReadableDatabase();
		Cursor c = db.query(TABLE_JOUEUR, new String[] { COL_IDJOUEUR,
				COL_NOMJOUEUR,COL_PRENOMJOUEUR, COL_POINTSJOUEUR_MENSUEL, COL_POINTSJOUEUR_COURANT }, null,null,null,null,null);
		return c;
	}
	
	/**
	 * Stocke dans une arrayslit les informations d'un joueur
	 * @param c
	 * @return
	 */
	public ArrayList<Joueur> cursortoJoueur(Cursor c) {
		if (c.getCount() == 0)
			return null;
		ArrayList<Joueur>  retJoueur = new ArrayList<Joueur>(c.getCount());
			
		c.moveToFirst();

		Joueur s = new Joueur();
		// on lui affecte toutes les infos gr‚ce aux infos contenues dans le
		// Cursor
		s.setidjoueur(c.getInt(NUM_COL_IDJOUEUR));
		s.setNomJoueur(c.getString(NUM_COL_NOMJOUEUR));
		s.setPrenomJoueur(c.getString(NUM_COL_PRENOMJOUEUR));
		s.setPointsjoueur_mensuel(c.getDouble(NUM_COL_POINTSJOUEUR_MENSUEL));
		s.setPointsjoueur_courant(c.getDouble(NUM_COL_POINTSJOUEUR_COURANT));

		// On ferme le cursor
		c.close();
		return retJoueur;
	}

	
	public ArrayList<ArrayList<String>> DonnerInformations()
	{
		ArrayList<ArrayList<String>> informations = new ArrayList<ArrayList<String>>();
		ArrayList<String> info;
		SQLiteDatabase db = MaBaseSQLiteJoueur.getReadableDatabase();
		Cursor c = db.rawQuery("select NomJoueur, PrenomJoueur , PointsJoueur_mensuel,  PointsJoueur_courant from joueur" , null);
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

	/**
	 * Cette methode permet de convertir un cursor en un Joueur
	 * 
	 * @param c
	 * @return
	 */
	private Joueur cursorToJoueur(Cursor c) {

		if (c.getCount() == 0)
			return null;

		// Sinon on se place sur le premier ÈlÈment
		c.moveToFirst();

		Joueur s = new Joueur();
		// on lui affecte toutes les infos gr‚ce aux infos contenues dans le
		// Cursor
		s.setidjoueur(c.getInt(NUM_COL_IDJOUEUR));
		s.setNomJoueur(c.getString(NUM_COL_NOMJOUEUR));
		s.setPrenomJoueur(c.getString(NUM_COL_PRENOMJOUEUR));
		s.setPointsjoueur_mensuel(c.getDouble(NUM_COL_POINTSJOUEUR_MENSUEL));
		s.setPointsjoueur_courant(c.getDouble(NUM_COL_POINTSJOUEUR_COURANT));

		// On ferme le cursor
		c.close();

		// On retourne le sondage
		return s;
	}

}