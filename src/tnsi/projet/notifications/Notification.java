package tnsi.projet.notifications;

import java.util.ArrayList;

import tnsi.projet.base.FonctionJoueur;
import tnsi.projet.base.Joueur;
import tnsi.projet.fragments.MAJClassement;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import com.pongiste.calping.R;


/**
 * ACTIVITE ALARME
 * Elle permet de déclancher l'alarme à la date voulu
 * elle envoie une notification à l'utilisateur et on cliquant sur celle-ci appelle l'activité
 * MAJ Classement pour mettre à jour son nombre de point
 *
 * @author guillaumehautcoeur
 *
 */
@SuppressLint("Wakelock") public class Notification extends BroadcastReceiver
{
	 Context context ;

	 Joueur j;

     @Override
     public void onReceive(Context context, Intent intent)
     {
         PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
         wl.acquire();
         notification(context);
         wl.release();

     }


     /**
      * Fonction qui envoie la notification à l'utilisateur
      * @param context
      */
	public void notification(Context context)
     {
    	 FonctionJoueur bd_joueur = new FonctionJoueur(context);
 		 bd_joueur.open();
 		 ArrayList<Integer> listeidjoueur = new ArrayList<Integer>();
 		 listeidjoueur = bd_joueur.ListerIDjoueur();

 		for (Integer s : listeidjoueur)
 		{
 				 bd_joueur.open();
 				 double pointcourant,pointmensuel=0;
				 int idjoueur=s;

				 pointcourant = bd_joueur.DonnerPointCourantJoueur(idjoueur);

				 Joueur j = new Joueur();
				 j = bd_joueur.ListerJoueur(idjoueur);

				  pointmensuel =  j.getPointsjoueur_mensuel();

				 if(pointcourant == pointmensuel)
				 {
					 System.err.println("Pas besoin de mise à jour , puisque les points n'ont pas changé");

				 }
				 else
				 {

				//Quand nous cliquons sur la notification mettre à jour les classements
			    	 Intent maj = new Intent(context,MAJClassement.class);
			    	 maj.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			    	PendingIntent resultIntent =  PendingIntent.getActivity(context, 0, maj, 0);

				     NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				                context).setSmallIcon(R.drawable.moniconeapplication)
				                .setContentTitle("Mise à jour des classements")
				                .setContentText("Lancer l application pour mettre à jour vos points .");

				                	     mBuilder.setContentIntent(resultIntent)
				    	        		.setAutoCancel(true);


				    Uri alarmSound = RingtoneManager
				            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				    mBuilder.setSound(alarmSound);
				    NotificationManager mNotificationManager = (NotificationManager) context.
				            getSystemService(Context.NOTIFICATION_SERVICE);
				    mNotificationManager.notify(1, mBuilder.build());

				 }

				 bd_joueur.close();

 			}
 		bd_joueur.close();


}
}