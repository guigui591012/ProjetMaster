package tnsi.projet.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MaBaseSQLiteHistorique extends SQLiteOpenHelper {

    private static final String TABLE_HISTORIQUE = "historique";
    private static final String COL_IDHISTORIQUE = "idhistorique";
    private static final String COL_IDJOUEUR = "idjoueur";
    private static final String COL_POINTSADVERSAIRE = "PointsAdversaire";
    private static final String COL_POINTSGAGNE = "PointsGagne";
    private static final String COL_VICTOIRE_DEFAITE = "Victoire_Defaite";
    private static final String COL_POINTSCOURANT = "PointsCourant";
    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_HISTORIQUE
            + " (" + COL_IDHISTORIQUE + " INTEGER ,"
            + COL_IDJOUEUR + " INTEGER ,"
            + COL_POINTSADVERSAIRE + " DOUBLE ,"
            + COL_POINTSGAGNE + " DOUBLE ,"
            + COL_VICTOIRE_DEFAITE + " TEXT NOT NULL ,"
            + COL_POINTSCOURANT + " DOUBLE );";


    public MaBaseSQLiteHistorique(Context context, String name,
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
        db.execSQL("DROP TABLE " + TABLE_HISTORIQUE + ";");
        onCreate(db);
    }

}