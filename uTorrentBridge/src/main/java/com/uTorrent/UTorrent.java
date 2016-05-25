package com.uTorrent;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uTorrent.resource.Torrent;
import com.uTorrent.resource.TorrentFile;
import com.uTorrent.resource.TorrentLabel;
import com.uTorrent.resource.TorrentProperties;
import com.uTorrent.resource.UTorrentSetting;
import com.uTorrent.resource.exception.ServerNotFoundException;
import com.uTorrent.resource.exception.TokenExpiredException;
import com.uTorrent.resource.exception.UnauthorizedException;
import com.uTorrent.resource.json.JSONArray;
import com.uTorrent.resource.json.JSONException;
import com.uTorrent.resource.json.JSONObject;
import com.uTorrent.resource.json.JSONTokener;
import com.uTorrent.resource.json.util.BasicAuth;
import com.uTorrent.resource.json.util.URLEncoder;
import com.uTorrent.resource.json.util.UTF8EncoderDecoder;

/**
 * this class controls all interactions with the utorrent webUI
 * 
 * @author glenn
 * 
 */
public class UTorrent {
	private static UTorrent _instance = new UTorrent();
	private static int BUFF_SIZE = 512;

	private final Logger logger = LoggerFactory.getLogger(UTorrent.class);

	public static UTorrent getInstance() {
		return _instance;
	}

	private String _serverAddress;
	private String _userCredentials;
	private String _cacheId;
	private final Hashtable<String, Torrent> _torrents;
	private final Hashtable<String, TorrentLabel> _labels;
	private final Hashtable<String, UTorrentSetting> _settings;
	private String _token;

	private UTorrent() {
		this._serverAddress = "";
		this._userCredentials = "";
		this._cacheId = "";
		this._torrents = new Hashtable<String, Torrent>();
		this._labels = new Hashtable<String, TorrentLabel>();
		this._settings = new Hashtable<String, UTorrentSetting>();
	}

	/**
	 * sets the location of the webUI server
	 * 
	 * @param useSSL
	 * @param serverAddress
	 * @param serverPort
	 */
	public void setServerSettings(boolean useSSL, String serverAddress, int serverPort) {
		this._serverAddress = (useSSL ? "https" : "http") + "://" + serverAddress + ":" + Integer.toString(serverPort) + "/gui/";
	}

	/**
	 * sets the user credentials for the application to use when connecting to
	 * the utorrent webUI server
	 * 
	 * @param userName
	 * @param password
	 */
	public void setUserCredentials(String userName, String password) {
		this._userCredentials = "Basic " + BasicAuth.encode(userName, password);
	}

	/**
	 * @return get the list of cached torrents on the client
	 */
	public Vector<Torrent> getTorrents() {
		Vector<Torrent> result = new Vector<Torrent>();
		Enumeration<Torrent> torrents = this._torrents.elements();
		while (torrents.hasMoreElements()) {
			result.addElement(torrents.nextElement());
		}
		return result;
	}

	/**
	 * sorts the list of torrents
	 * 
	 * @param list
	 * @return
	 */
	private Vector<Torrent> torrentSort(Vector<Torrent> list)
	{
		Torrent temp = null;
		Torrent[] torrentList = new Torrent[list.size()];
		list.toArray(torrentList);
		for (int i = 0; i < torrentList.length; i++)
		{
			for (int t = i - i; t < torrentList.length; t++)
			{
				if (torrentList[i].compareTo(torrentList[t]) < 1)
				{
					temp = torrentList[i];
					torrentList[i] = torrentList[t];
					torrentList[t] = temp;
				}
			}
		}
		list.clear();
		for (int i = 0; i < torrentList.length; i++)
		{
			list.add(torrentList[i]);
		}

		return list;

	}

	/**
	 * @param torrentHash
	 *            the hash of the torrent to get
	 * @return the torrent with the given hash (or null if not found)
	 */
	public Torrent getTorrent(String torrentHash) {
		if (this._torrents.containsKey(torrentHash)) {
			return this._torrents.get(torrentHash);
		}
		else {
			return null;
		}
	}

	/**
	 * @return the list of cached labels on the client
	 */
	public Vector<TorrentLabel> getLabels() {
		Vector<TorrentLabel> result = new Vector<TorrentLabel>();
		Enumeration<TorrentLabel> labels = this._labels.elements();
		while (labels.hasMoreElements()) {
			result.addElement(labels.nextElement());
		}
		return result;
	}

	/**
	 * @param torrentLabel
	 *            the torrent label
	 * @return the torrentLabel requested (or null if not found)
	 */
	public TorrentLabel getLabel(String torrentLabel) {
		if (this._labels.containsKey(torrentLabel)) {
			return this._labels.get(torrentLabel);
		}
		else {
			return null;
		}
	}

	/**
	 * apply a label to a torrent
	 * 
	 * @param label
	 * @param torrent
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public void applyLabelToTorrent(String label, Torrent torrent) throws ServerNotFoundException, UnauthorizedException {
		getServerResponse("action=setprops&hash=" + torrent.getHash() + "&s=label&v=" + label);
	}

	/**
	 * check the server for the latest torrent statuses and update the client
	 * cache
	 * 
	 * @return a list of torrents and related information
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public Vector<Torrent> checkTorrents() throws ServerNotFoundException, UnauthorizedException {
		if (this._cacheId.equals("")) {
			String response = getServerResponse("list=1");
			parseFullResponse(response);
		}
		else {
			String response = getServerResponse("list=1&cid=" + this._cacheId);
			parseDiffResponse(response);
		}
		return torrentSort(getTorrents());
	}

	/**
	 * parse the json from a full torrent list response
	 * 
	 * @param response
	 */
	private void parseFullResponse(String response) {
		try {
			JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);
			parseCommonResponse(jo);

			JSONArray torrents = jo.getJSONArray("torrents");
			this._torrents.clear();
			for (int i = 0; i < torrents.length(); ++i) {
				Torrent newTorrent = new Torrent(torrents.getJSONArray(i));
				this._torrents.put(newTorrent.getHash(), newTorrent);
			}

		} catch (JSONException ex) {
			this.logger.error("Exception wile parsing full response: ",ex);
		}
	}

	/**
	 * update the labels and cacheid collection
	 * 
	 * @param jo
	 * @throws JSONException
	 */
	private void parseCommonResponse(JSONObject jo) throws JSONException {
		this._cacheId = jo.getString("torrentc");

		JSONArray labels = jo.getJSONArray("label");
		this._labels.clear();
		for (int i = 0; i < labels.length(); ++i) {
			TorrentLabel torrentLabel = new TorrentLabel(labels.getJSONArray(i));
			this._labels.put(torrentLabel.getLabel(), torrentLabel);
		}
	}

	/**
	 * parse the json from a diff torrent list response
	 * 
	 * @param response
	 */
	private void parseDiffResponse(String response) {
		try {
			JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);

			JSONArray changedTorrents = jo.getJSONArray("torrentp");
			for (int i = 0; i < changedTorrents.length(); ++i) {
				Torrent existingTorrent = new Torrent(changedTorrents.getJSONArray(i));
				this._torrents.put(existingTorrent.getHash(), existingTorrent);
			}

			JSONArray removedTorrents = jo.getJSONArray("torrentm");
			for (int i = 0; i < removedTorrents.length(); ++i) {
				this._torrents.remove(removedTorrents.getString(i));
			}

			parseCommonResponse(jo);
			// if it won't parse as a diff, then parse the full response
		} catch (JSONException ex) {
			parseFullResponse(response);
		}
	}

	private String getServerResponse(String queryString) throws ServerNotFoundException, UnauthorizedException
	{
		return getServerResponse(queryString, true);
	}

	/**
	 * call the webUI server and return a response string
	 * 
	 * @param queryString
	 *            the url querystring to pass to the server
	 * @return
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	private String getServerResponse(String queryString, boolean retryIfTokenAuthFails) throws ServerNotFoundException, UnauthorizedException {
		HttpURLConnection c;

		try {
			String requestUrl = this._serverAddress;
			// only add token auth if its required, this ensures backwards
			// compatibility
			requestUrl += (this._token != null && this._token != "") ? "?token=" + this._token + "&" : "?";
			requestUrl += queryString;
			URL url = new URL(requestUrl);
			// c = (HttpURLConnection)Connector.open(requestUrl);
			c = (HttpURLConnection) url.openConnection();
			c.setRequestProperty("Authorization", this._userCredentials);
			c.connect();

			// hack as on some cell networks the authorization header gets
			// stripped. This is a backup
			// so you can set up a proxy which alters this header to the
			// Authorization header when it reaches
			// its destination
			// c.setRequestProperty("X-J2me-Auth",_userCredentials);

			this.logger.info("URI: {}",requestUrl);
			this.logger.info("Authorization: {}",this._userCredentials);
		} catch (Exception e) {
			this.logger.error("Exception while getting server response: ",e);
			throw new ServerNotFoundException(e.toString() + " " + e.getMessage());
		}

		try {
			return getServerResponse(c);
		} catch (TokenExpiredException | IOException e) {
			// refresh the token and retry if the first call fails
			if (retryIfTokenAuthFails) {
				refreshToken();
				return getServerResponse(queryString, false);
			}
			// if the token is still not valid after the second call, bail out
			else {
				throw new UnauthorizedException("Invalid Authentication Token: " + e.getMessage());
			}
		}
	}

	private String getServerResponse(HttpURLConnection c) throws ServerNotFoundException, UnauthorizedException, TokenExpiredException, IOException {
		StringBuffer s = new StringBuffer();
		try {
			// got response OK
			if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = c.getInputStream();

				byte[] buff = new byte[BUFF_SIZE];
				int bytesRead = is.read(buff, 0, BUFF_SIZE);
				while (bytesRead > 0) {
					s.append(UTF8EncoderDecoder.UTF8Decode(buff, 0, bytesRead));
					bytesRead = is.read(buff, 0, BUFF_SIZE);
				}
				is.close();
				c.disconnect();
			}
			// incorrect authorization
			else if (c.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
				throw new UnauthorizedException("Response code: " + Integer.toString(c.getResponseCode()));
			}
			// failed token authentication
			else if (c.getResponseCode() == HttpURLConnection.HTTP_MULT_CHOICE || c.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
				throw new TokenExpiredException();
			}
			// other invalid http responses
			else {
				throw new ServerNotFoundException("Response code: " + Integer.toString(c.getResponseCode()));
			}
		} catch (UnauthorizedException |ServerNotFoundException| TokenExpiredException e) {
			if(e instanceof UnauthorizedException){
				this.logger.warn("Autherization exeption: {}",e.getMessage());
			}else if(e instanceof ServerNotFoundException){
				this.logger.warn("Could not find server: {}",e.getMessage());
			}else if(e instanceof TokenExpiredException){
				this.logger.warn("Token has expired: {}",e.getMessage());
			}else{
				this.logger.error("Exception while getting server response",e);
			}
			throw e;
		}

		return s.toString();
	}

	/**
	 * start a torrent
	 * 
	 * @param torrent
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public void start(Torrent torrent) throws ServerNotFoundException, UnauthorizedException
	{
		getServerResponse("action=start&hash=" + torrent.getHash());
	}

	/**
	 * force start a torrent
	 * 
	 * @param torrent
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public void forceStart(Torrent torrent) throws ServerNotFoundException, UnauthorizedException
	{
		getServerResponse("action=forcestart&hash=" + torrent.getHash());
	}

	/**
	 * stop a torrent
	 * 
	 * @param torrent
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public void stop(Torrent torrent) throws ServerNotFoundException, UnauthorizedException
	{
		getServerResponse("action=stop&hash=" + torrent.getHash());
	}

	/**
	 * pause a torrent
	 * 
	 * @param torrent
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public void pause(Torrent torrent) throws ServerNotFoundException, UnauthorizedException
	{
		getServerResponse("action=pause&hash=" + torrent.getHash());
	}

	/**
	 * remove a torrent and optionally remove the data as well
	 * 
	 * @param torrent
	 * @param removeFiles
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public void remove(Torrent torrent, boolean removeFiles) throws ServerNotFoundException, UnauthorizedException
	{
		String action = "action=remove";
		if (removeFiles) {
			action += "data";
		}

		getServerResponse(action + "&hash=" + torrent.getHash());
	}

	/**
	 * get the list of files in a torrent
	 * 
	 * @param torrent
	 * @return a list of TorrentFiles contained within a given torrent
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public Vector<TorrentFile> getFilesInTorrent(Torrent torrent) throws ServerNotFoundException, UnauthorizedException {
		String response = getServerResponse("action=getfiles&hash=" + torrent.getHash());
		Vector<TorrentFile> result = new Vector<TorrentFile>();
		try {
			JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);

			JSONArray files = jo.getJSONArray("files").getJSONArray(1);
			for (int i = 0; i < files.length(); ++i) {
				result.addElement(new TorrentFile(files.getJSONArray(i)));
			}
			Collections.sort(result, new Comparator<TorrentFile>() {

				@Override
				public int compare(TorrentFile file1, TorrentFile file2) {
					return file1.getFileName().compareToIgnoreCase(file2.getFileName());
				}
			});
		} catch (JSONException ex) {
			this.logger.error("Exception while getting files in torrent: ",ex);
		}

		return result;
	}

	/**
	 * get the properties for a torrent
	 * 
	 * @param torrent
	 * @return
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public TorrentProperties getPropertiesForTorrent(Torrent torrent) throws ServerNotFoundException, UnauthorizedException
	{
		String response = getServerResponse("action=getprops&hash=" + torrent.getHash());

		try {
			JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);

			JSONObject properties = jo.getJSONObject("props");
			return new TorrentProperties(properties);
		} catch (JSONException ex) {
			this.logger.error("Exception while getting properties for torrent: ",ex);
		}
		return null;
	}

	private void refreshToken() throws UnauthorizedException, ServerNotFoundException {
		this.logger.info("Refreshing token...");

		HttpURLConnection c;
		try {
			// c =
			// (HttpURLConnection)Connector.open(_serverAddress+"token.html");
			c = (HttpURLConnection) (new URL(this._serverAddress + "token.html")).openConnection();
			c.setRequestProperty("Authorization", this._userCredentials);
			// c.setRequestProperty("X-J2me-Auth",_userCredentials);

			this.logger.info("URI: {}token.html",this._serverAddress);
			this.logger.info("Authorization: {}", this._userCredentials);
		} catch (Exception e) {
			this._token = "";
			this.logger.error("Exception while refressing token: ",e);
			throw new ServerNotFoundException("Unable to refresh Authentication Token: " + e.getMessage());
		}

		try {
			String tokenResponse = getServerResponse(c);

			String preToken = "<html><div id='token' style='display:none;'>";
			String postToken = "</div></html>";

			this._token = tokenResponse.substring(preToken.length(), tokenResponse.length() - postToken.length());
			this.logger.info("Extracted token: {}", this._token);
		} catch (Exception e) {
			this._token = "";
			this.logger.error("Exception while refressing token: ",e);
			throw new ServerNotFoundException("Unable to refresh Authentication Token: " + e.getMessage());
		}
	}

	public void setPriority(Torrent torrent, int index, int priority) throws ServerNotFoundException, UnauthorizedException {
		getServerResponse("action=setprio&hash=" + torrent.getHash() + "&p=" + priority + "&f=" + index);
	}

	/**
	 * check with the server and get a list of all exposed utorrent settings
	 * 
	 * @return a list of UtorrentSetting objects
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public Vector<UTorrentSetting> checkSettings() throws ServerNotFoundException, UnauthorizedException {
		String response = getServerResponse("action=getsettings");

		try {
			JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);

			this._settings.clear();
			JSONArray settings = jo.getJSONArray("settings");
			for (int i = 0; i < settings.length(); ++i) {
				UTorrentSetting setting = new UTorrentSetting(settings.getJSONArray(i));
				this._settings.put(setting.getName(), setting);
			}
		} catch (JSONException ex) {
			this.logger.error("Exeption while checking settings: ",ex);
		}

		return getSettings();
	}

	/**
	 * @return get the list of cached settings on the client
	 */
	public Vector<UTorrentSetting> getSettings() {
		Vector<UTorrentSetting> result = new Vector<UTorrentSetting>();
		Enumeration<UTorrentSetting> settings = this._settings.elements();
		while (settings.hasMoreElements()) {
			result.addElement(settings.nextElement());
		}
		return result;
	}

	/**
	 * get a particular setting from the cached list on the client
	 * 
	 * @param setting
	 * @return
	 */
	public UTorrentSetting getSetting(String setting) {
		if (this._settings.containsKey(setting)) {
			return this._settings.get(setting);
		}
		else {
			return null;
		}
	}

	/**
	 * changes the value of a utorrent setting
	 * 
	 * @param setting
	 *            the setting to update
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public void setSetting(UTorrentSetting setting) throws ServerNotFoundException, UnauthorizedException
	{
		try {
			getServerResponse("action=setsetting&s=" + setting.getName() + "&v=" + URLEncoder.encode(setting.getValue(), "UTF-8"));
		} catch (IOException ex) {
			this.logger.error("Error setting a setting: ",ex);
		}
	}

}
