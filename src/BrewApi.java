
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BrewApi {
	
	String apiKey = "XXX";
	String apiBaseURL = "http://api.brewerydb.com/v2/";	
	
	public void searchBeersByName(String query, int pageNumber){
		BufferedReader rd = null;
		StringBuilder response = null;
		String apiEndPoint = apiBaseURL + "search?type=beer&key=" + apiKey + "&q=" + query + "&p=" + pageNumber;
		
		try {
			URL endpointUrl = new URL(apiEndPoint);
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
		
		System.out.println(response.toString().substring(1380, 1420));
		System.out.println(response.toString().substring(1405, 1407));
		Gson gson = new Gson();
		SearchResult searchResult = gson.fromJson(response.toString(), SearchResult.class);
		System.out.println(searchResult.data.get(0).srm);
	}
	
	public void searchBeersByPreference(BeerPreference preference, int pageNumber){
		BufferedReader rd = null;
		StringBuilder response = null;
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
		
		try {
			URL endpointUrl = new URL(apiEndPoint);
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
		
		System.out.println(response.toString().substring(8920, 8960));
		Gson gson = new Gson();
		SearchResult searchResult = gson.fromJson(response.toString(), SearchResult.class);
		System.out.println(searchResult.data.get(0).name);
	}
}
