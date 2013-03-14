/*
 * Ben Brouse
 * bjb85@drexel.edu
 * CS338:GUI, Final Project (Brewniverse)
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import com.google.gson.Gson;

/*
 * The method names in this class are pretty self explanatory.  The class makes
 * a REST api call to BreweryDB for a json string of data.  Then the GSON library
 * parses that json into the appropriate object type.
 */
public class BrewApi {
	
	String apiKey = "86fb5080c06b3f35b96ae64c1e66659f";
	String apiBaseURL = "http://api.brewerydb.com/v2/";	
	boolean DEBUG = false; //used while testing to make sure I didn't make superfluous API calls
	
	public SearchResult searchBeersByName(String query, int pageNumber){
		String apiEndPoint = apiBaseURL + "search?type=beer&key=" + apiKey + "&q=" + URLEncoder.encode(query) + "&p=" + pageNumber;
		System.out.println("URL: " + apiEndPoint);
		String jsonString = stringFromUrl(apiEndPoint);
		System.out.println(jsonString);
		Gson gson = new Gson();
		SearchResult result = gson.fromJson(jsonString, SearchResult.class);
		result.query = query;
		result.isPreferenceSearch = false;
		return result;
	}
	
	public SearchResult searchBeersByPreference(BeerPreference preference, int pageNumber){
		String apiEndPoint = apiBaseURL + "beers?key=" + apiKey + "&p=" + pageNumber;
		
		if (preference.hasAbvPreference()){
			apiEndPoint = apiEndPoint.concat("&abv=" + preference.abvMin + "," + preference.abvMax);
		}
		if (preference.hasIbuPreference()){
			apiEndPoint = apiEndPoint.concat("&ibu=" + preference.ibuMin + "," + preference.ibuMax);
		}
		if (preference.hasStylePreference()){
			apiEndPoint = apiEndPoint.concat("&styleId=" + preference.style.id);
		}
		if (preference.hasSrmPreference()){
			apiEndPoint = apiEndPoint.concat("&srmId=" + preference.srm.id);
		}
		System.out.println("URL: " + apiEndPoint);
		
		String jsonString = stringFromUrl(apiEndPoint);
		Gson gson = new Gson();
		SearchResult result = gson.fromJson(jsonString, SearchResult.class);
		result.preference = preference;
		result.isPreferenceSearch = true;
		return result;
	}
	
	public SearchResult getAdditionalResults(SearchResult previousResults){
		if(previousResults.isPreferenceSearch){
			return searchBeersByPreference(previousResults.preference, previousResults.currentPage+1);
		}
		else{
			return searchBeersByName(previousResults.query, previousResults.currentPage+1);
		}
	}
	
	public List<Beer> importSavedBeers(){
		String jsonString;
		try {
			jsonString = fromFile("saved_beers.json");
		} catch (FileNotFoundException e) {
			return new ArrayList<Beer>();
		}
		Gson gson = new Gson();
		SearchResult result = gson.fromJson(jsonString, SearchResult.class);
		return result.data;
	}
	
	public void exportSavedBeers(List<Beer> savedBeers){
		SearchResult tempSearchResult = new SearchResult();
		tempSearchResult.data = savedBeers;
		Gson gson = new Gson();
		String json = gson.toJson(tempSearchResult);
		toFile(json, "saved_beers.json");
	}
	
	public Vector<BeerStyle> getAllStyles(){
		String jsonString = null;
		if(DEBUG){
			try {
				jsonString = fromFile("styles.json");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Gson gson = new Gson();
			StyleResults styles = gson.fromJson(jsonString, StyleResults.class);
			return styles.data;
		}
		String apiEndPoint = apiBaseURL + "styles?key=" + apiKey;
		jsonString = stringFromUrl(apiEndPoint);
		toFile(jsonString, "styles.json");
		Gson gson = new Gson();
		StyleResults styles = gson.fromJson(jsonString, StyleResults.class);
		return styles.data;
	}
	
	public Vector<SrmValue> getAllSrmValues(){
		String jsonString = null;
		if(DEBUG){
			try {
				jsonString = fromFile("srm_values.json");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Gson gson = new Gson();
			SrmResults srms = gson.fromJson(jsonString, SrmResults.class);
			return srms.data;
		}
		String apiEndPoint = apiBaseURL + "menu/srm?key=" + apiKey;
		jsonString = stringFromUrl(apiEndPoint);
		toFile(jsonString, "srm_values.json");
		Gson gson = new Gson();
		SrmResults srms = gson.fromJson(jsonString, SrmResults.class);
		return srms.data;
	}
	
	private void toFile(String text, String fileName){
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write(text);
			writer.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String fromFile(String fileName) throws FileNotFoundException{
		return new Scanner(new File(fileName)).useDelimiter("\\Z").next();
	}
	
	private String stringFromUrl(String urlString){
		BufferedReader rd = null;
		StringBuilder response = null;
		try {
			URL endpointUrl = new URL(urlString);
			HttpURLConnection request = (HttpURLConnection)endpointUrl.openConnection();
			request.setRequestMethod("GET");
			request.connect();

			rd  = new BufferedReader(new InputStreamReader(request.getInputStream()));
			response = new StringBuilder();
			String line = null;
			while ((line = rd.readLine()) != null){
				response.append(line);
			}
			
			request.disconnect();
			rd.close();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return response.toString();
	}
}
