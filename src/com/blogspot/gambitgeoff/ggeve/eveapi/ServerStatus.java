package com.blogspot.gambitgeoff.ggeve.eveapi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

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

import com.blogspot.gambitgeoff.ggeve.GGEveApplicationRunner;
import com.blogspot.gambitgeoff.ggeve.StatusInformation;

/**
 * − <eveapi version="2"> <currentTime>2009-06-08 12:53:28</currentTime> −
 * <result> <serverOpen>True</serverOpen> <onlinePlayers>24072</onlinePlayers>
 * </result> <cachedUntil>2009-06-08 12:56:28</cachedUntil> </eveapi>
 * 
 * @author geoffrey
 * 
 */

public class ServerStatus {

	private StatusInformation myStatusInformation;

	public ServerStatus() {
		updateStatus();
	}

	public StatusInformation getStatusInformation() {
		return myStatusInformation;
	}

	public synchronized void updateStatus() {
//		URL eveOnlineURL = null;
//		HttpURLConnection myConnection = null;
		if (!GGEveApplicationRunner.getIsRunningOffline()) {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet("http://api.eve-online.com/server/ServerStatus.xml.aspx");
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
						ServerStatusEventHandler handler = new ServerStatusEventHandler();
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
						myStatusInformation = handler.getServerStatus();

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
}
