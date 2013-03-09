
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BrewApi {
	
	String apiKey = "XXX";
	String apiBaseURL = "http://api.brewerydb.com/v2/";	
	
	public SearchResult searchBeersByName(String query, int pageNumber){
		String apiEndPoint = apiBaseURL + "search?type=beer&key=" + apiKey + "&q=" + query + "&p=" + pageNumber;
		String jsonString = stringFromUrl(apiEndPoint);
		Gson gson = new Gson();
		return gson.fromJson(jsonString, SearchResult.class);
	}
	
	public SearchResult searchBeersByPreference(BeerPreference preference, int pageNumber){
		String apiEndPoint = apiBaseURL + "beers?key=" + apiKey;
		
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
		
		String jsonString = stringFromUrl(apiEndPoint);
		Gson gson = new Gson();
		return gson.fromJson(jsonString, SearchResult.class);
	}
	
	public List<Style> getAllStyles(){
		String apiEndPoint = apiBaseURL + "styles?key=" + apiKey;
		String jsonString = stringFromUrl(apiEndPoint);
		Gson gson = new Gson();
		StyleResults styles = gson.fromJson(jsonString, StyleResults.class);
		return styles.data;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.toString();
	}
}
