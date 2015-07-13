package tnsi.projet.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.pongiste.calping.R;

/**
 * ACTIVITE RECHERCHER
 * Elle permet de rechercher à l'aide du WebView
 * sur le site de la FEDERATION FRANCAISE DE TENNIS DE TABLE
 * le nombre de point mensuel d'un joueur
 *
 * @author guillaumehautcoeur
 */
public class Rechercher_Joueur extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        /**
         * Config de la WEBVIEW
         */
        WebView myWebView = (WebView) findViewById(R.id.rechercher);
        WebSettings web = myWebView.getSettings();
        web.setJavaScriptEnabled(true);
        myWebView.setVerticalScrollBarEnabled(true);
        myWebView.setHorizontalScrollBarEnabled(true);
        // myWebView.loadUrl("http://www.fftt.com/sportif/sportif.htm");
        myWebView.loadUrl("http://www.pingpocket.fr/#accueil");
    }

    public void OnPause() {
        super.onPause();

    }

    public void onResume() {
        super.onResume();

    }
}

	

