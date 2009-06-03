package com.blogspot.gambitgeoff.ggeve;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.blogspot.gambitgeoff.ggeve.eveapi.AccountCharacters;
import com.blogspot.gambitgeoff.ggeve.eveapi.CharacterSheet;
import com.blogspot.gambitgeoff.ggeve.eveapi.SkillInTraining;

import android.app.Service;
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
		refreshAccountCharacters();
		refreshCharacterDetails();
		refreshTrainingInfo();
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
		}
	}
}
