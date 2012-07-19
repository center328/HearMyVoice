package fr.epita.hmv;

import java.util.ArrayList;
import java.util.List;

import fr.epita.hmv.tools.StringOperation;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author Frederic CORREIA et David Fonseca
 * @version 0.1
 * Classe correspondant à l'activity de la page d'accueil avec reconnaissance
 * vocale et création de la liste des mots reconnus.
 */
public class HearMyVoiceAndroidActivity extends Activity
{
	// Liste des phrases reconnus
	private ListView					listMots;
	
	// Resultat de la reconnaissance vocale si c'est OK
	private static final int	REQUEST_CODE	= 1234;

	/**
	 * A la creation de l'instance, démarrage de l'application
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// On indique le layout correspondant a l'activity
		setContentView(R.layout.voice_recog);

		// Mapping du bouton speakButton
		Button speakButton = (Button) findViewById(R.id.speakButton);

		// Mapping de la listView
		listMots = (ListView) findViewById(R.id.list);

		// On regarde si le service de reconnaissance vocale est présent
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0)
		{
			speakButton.setEnabled(false);
			speakButton.setText("Recognizer not present");
		}
	}

	/**
	 * Le listener du bouton pour parler
	 */
	public void speakButtonClicked(View v)
	{
		// Appel de la reconnaissance vocale
		startVoiceRecognitionActivity();
	}

	/**
	 * Démarrage de la reconnaissance vocale
	 */
	private void startVoiceRecognitionActivity()
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition");
		startActivityForResult(intent, REQUEST_CODE);
	}

	/**
	 * Récupération du résultat de la reconnaissance vocale et remplissage de la
	 * listView
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
		{
			// Récupération des phrases reconnus dans une ArrayList
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			
			// Remplissage de la ListView
			listMots.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, matches));
			
			// Listener sur la ListView
			listMots.setOnItemClickListener(new OnItemClickListener()
			{
				// Si l'utilisateur appuie sur une des phrases
				public void onItemClick(AdapterView<?> av, View arg1, int position,
						long arg3)
				{
					// Récupération de la phrase
					String item = (String) av.getItemAtPosition(position);
					
					item = StringOperation.sansAccents(item);
					Intent intent = new Intent(getApplicationContext(), fr.epita.hmv.WebCallActivity.class);
					intent.putExtra("phrase", item);
					
					// Changement de page en envoyant la phrase de l'utilisateur
					startActivity(intent);
				}
			});
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
