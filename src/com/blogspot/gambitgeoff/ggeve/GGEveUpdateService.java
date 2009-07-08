package com.blogspot.gambitgeoff.ggeve;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.blogspot.gambitgeoff.ggeve.eveapi.AccountCharacters;
import com.blogspot.gambitgeoff.ggeve.eveapi.CharacterSheet;
import com.blogspot.gambitgeoff.ggeve.eveapi.ServerStatus;
import com.blogspot.gambitgeoff.ggeve.eveapi.SkillInTraining;
import com.blogspot.gambitgeoff.ggeve.eveapi.SkillTree;
import com.blogspot.gambitgeoff.ggeve.eveapi.SkillTreeEventHandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class GGEveUpdateService extends Service {
	
	private GGEveDBAdapter myGGEveDBAdapter;
	private Timer myUpdateTimer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate()
	{
		myUpdateTimer = new Timer("GGEveUpdateTimer");
	}
	
	public void onStart(Intent intent, int startID)
	{
		myGGEveDBAdapter = GGEveApplicationRunner.getDatabaseAdapter();
		myUpdateTimer.cancel();
		try {
			  Class<?> serviceClass = Class.forName(this.getClass().getName());
			  stopService(new Intent(this, serviceClass));
			  System.out.println("service stopped");
			} catch (ClassNotFoundException e) {e.printStackTrace();}

		myUpdateTimer = new Timer("GGEveUpdateTimer");
		myUpdateTimer.scheduleAtFixedRate(updateTask, 0, 5*60*1000);
		refreshCharacterInfo();
	}
	
	@SuppressWarnings("unused")
	private void sendNotification(String inCharacterName, long inTrainTime)
	{
		long time = System.currentTimeMillis();
		System.out.println("Going to notify in: " + ((inTrainTime-time)/1000)/60 + " seconds.");
		String serviceName = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager;
		notificationManager = (NotificationManager)getSystemService(serviceName);
		String text = "Skill Training Complete";
		long when = inTrainTime;
		int icon = R.drawable.menu_item_icon;
		Notification n = new Notification(icon, text, when);
		
		Context context = getApplicationContext();
		String expandedText = inCharacterName + " has finished training.";
		String expandedTitle = "Eve Skill Training Complete";
		Intent startActivityIntent = new Intent(this, GGEveOverviewActivity.class);
		PendingIntent launchIntent = PendingIntent.getActivity(context, 0, startActivityIntent, 0);
		int count = 1;
		n.setLatestEventInfo(context, expandedTitle, expandedText, launchIntent);
		
		notificationManager.notify(count++, n);
	}


	
	private TimerTask updateTask = new TimerTask(){
		public void run(){
			refreshCharacterInfo();
		}
	};
	
	private Runnable myUpdateThread = new Runnable(){
		public void run()
		{
			doRefreshCharacterInfo();
		}
	};
	
	private void refreshCharacterInfo()
	{
		Thread updateThread = new Thread(null, myUpdateThread, "Update Eve Info");
		updateThread.start();
	}
	
	private void doRefreshCharacterInfo()
	{
		System.out.println("Refreshing Character Information!");
		updateServerStatus();
		updateSkillTree();
		refreshAccountCharacters();
		refreshCharacterDetails();
		refreshTrainingInfo();
	}
	
	private void updateSkillTree()
	{
		if (!GGEveApplicationRunner.getIsRunningOffline()) {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet("http://api.eve-online.com/eve/SkillTree.xml.aspx");
			HttpResponse response = null;
			try {
				response = client.execute(request);
			} catch (ClientProtocolException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			int status = response.getStatusLine().getStatusCode();
			System.out.println("httpresponse error status: " + status);
			if (status == HttpStatus.SC_OK) {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				try {
					DocumentBuilder db = dbf.newDocumentBuilder();
					db.reset();
					try {
						SAXParserFactory spf = SAXParserFactory.newInstance();
						SAXParser sp = spf.newSAXParser();
						XMLReader reader = sp.getXMLReader();
						SkillTreeEventHandler handler = new SkillTreeEventHandler();
						reader.setContentHandler(handler);
						InputSource is = null;
						try {
							is = new InputSource(response.getEntity().getContent());
						} catch (IllegalStateException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							reader.parse(is);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						SkillTree tree = handler.getSkillTree();
						myGGEveDBAdapter.updateSkillTree(tree);

					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void updateServerStatus()
	{
		ServerStatus ss = new ServerStatus();
		myGGEveDBAdapter.updateServerStatus(ss.getStatusInformation());
	}
	
	private void refreshAccountCharacters()
	{
		Vector<AccountDetails> accounts = myGGEveDBAdapter.getAccounts();
		for (AccountDetails account: accounts)
		{
			try {
				AccountCharacters ac = new AccountCharacters(account.getUserID(), account.getPublicAPIKey());
				Vector <EveCharacter> characters = ac.getCharacters();
				for (EveCharacter ec: characters)
				{
					myGGEveDBAdapter.updateEveCharacter(ec);
				}
			} catch (EveAuthenticationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void refreshCharacterDetails()
	{
		Vector<EveCharacter> ecs = myGGEveDBAdapter.getEveCharacters();
		for (EveCharacter ec: ecs)
		{
			AccountDetails account = myGGEveDBAdapter.getAccount(ec.getUserID());
			CharacterSheet cs = new CharacterSheet(account.getUserID(), account.getPublicAPIKey(), ec.getCharacterID());
			myGGEveDBAdapter.updateEveCharacter(cs.getCharacter());
		}
	}
	
	private void refreshTrainingInfo()
	{
		Vector<EveCharacter> ecs = myGGEveDBAdapter.getEveCharacters();
		for (EveCharacter ec: ecs)
		{
			SkillInTraining sit = new SkillInTraining(ec.getCharacterID());
			TrainingInformation ti = sit.getTrainingInformation();
			ti.setCharacterID(ec.getCharacterID());
			myGGEveDBAdapter.updateTrainingInformation(ti);
			int number = ti.getSkillInTraining();
			if (number>0)
			{
//				long end = ti.getTrainingEndTime().getTime();
//				sendNotification(ec.getCharacterName(), end);
			}
		}
	}
}
