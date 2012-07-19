package fr.epita.hmv;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebCallActivity extends Activity
{
	// Explorer web
	private WebView	webview;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// On indique le layout correspondant a l'activity
		setContentView(R.layout.web_call);

		Bundle bundle = this.getIntent().getExtras();
		String phrase = "";

		// Récupération de la phrase choisie par l'utilisateur
		if (bundle.containsKey("phrase"))
			phrase = this.getIntent().getStringExtra("phrase");

		// Mapping de l'explorer web
		webview = (WebView) findViewById(R.id.webView1);

	// Appel de notre serveur afin d'afficher le resultat de la traduction
		postData(phrase);	
	}

	/**
	 * Methode envoyant la requete sur notre server avec la methode POST
	 * @param phrase : La phrase que l'utilisateur a choisit de traduire
	 */
	public void postData(String phrase)
	{
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("phrase", phrase));
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://draigon.free.fr/index.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httppost);

			String data = new BasicResponseHandler().handleResponse(response);

			webview.loadDataWithBaseURL(httppost.getURI().toString(), data,
					"text/html", HTTP.UTF_8, null);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}
}
