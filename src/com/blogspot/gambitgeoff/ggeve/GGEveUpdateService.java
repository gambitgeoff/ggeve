package com.blogspot.gambitgeoff.ggeve;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.blogspot.gambitgeoff.ggeve.eveapi.AccountCharacters;
import com.blogspot.gambitgeoff.ggeve.eveapi.CharacterSheet;
import com.blogspot.gambitgeoff.ggeve.eveapi.SkillInTraining;

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
			  Class serviceClass = Class.forName(this.getClass().getName());
			  stopService(new Intent(this, serviceClass));
			  System.out.println("service stopped");
			} catch (ClassNotFoundException e) {e.printStackTrace();}

		myUpdateTimer = new Timer("GGEveUpdateTimer");
		myUpdateTimer.scheduleAtFixedRate(updateTask, 0, 5*60*1000);
		refreshCharacterInfo();
	}
	
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
		refreshAccountCharacters();
		refreshCharacterDetails();
		refreshTrainingInfo();
	}
	
	private void updateServerStatus()
	{
		
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
				long end = ti.getTrainingEndTime().getTime();
				sendNotification(ec.getCharacterName(), end);
			}
		}
	}
}
