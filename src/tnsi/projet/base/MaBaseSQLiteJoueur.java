package tnsi.projet.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MaBaseSQLiteJoueur extends SQLiteOpenHelper {

    public static final String TABLE_JOUEUR = "joueur";
    public static final String COL_IDJOUEUR = "idjoueur";
    public static final String COL_NOMJOUEUR = "NomJoueur";
    public static final String COL_PRENOMJOUEUR = "PrenomJoueur";
    public static final String COL_POINTSJOUEUR_MENSUEL = "PointsJoueur_mensuel";
    public static final String COL_POINTSJOUEUR_COURANT = "PointsJoueur_courant";

    public static final String CREATE_BDD = "CREATE TABLE " + TABLE_JOUEUR
            + " (" + COL_IDJOUEUR + " INTEGER,"
            + COL_NOMJOUEUR + " TEXT NOT NULL,"
            + COL_PRENOMJOUEUR + " TEXT NOT NULL,"
            + COL_POINTSJOUEUR_MENSUEL + " DOUBLE,"
            + COL_POINTSJOUEUR_COURANT + " DOUBLE);";

    public MaBaseSQLiteJoueur(Context context, String name,
                              CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // on cree la table a partir de la requete Ècrite dans la variable
        // CREATE_BDD
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // On peut fait ce qu'on veut ici moi j'ai decide de supprimer la table
        // et de la recreer
        // comme ca lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_JOUEUR + ";");
        onCreate(db);
    }

}