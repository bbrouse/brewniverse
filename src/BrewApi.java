
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
	
	public ApiSearchResult searchBeersByName(String query, int pageNumber){
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
				response.append(line + '\n');
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
		
		
		int numPages = pageNumberFromJson(response.toString());
		int numResults = resultNumberFromJson(response.toString());
		int currentPage = currentPageFromJson(response.toString());
		Beer[] beers = beerListFromJson(response.toString(), numResults);
		return new ApiSearchResult(beers, numResults, numPages, currentPage);
	}
	
	private Beer[] beerListFromJson(String jsonString, int numBeers){
		Beer[] beersToReturn = new Beer[numBeers];
		JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject)parser.parse(jsonString);
 
        JsonArray arr = obj.get("data").getAsJsonArray();
        for(JsonElement b: arr){
        	Beer beer = new Beer();
        	beer.setId(b.get("id"));
        }
	}
	
	private int pageNumberFromJson(String jsonString){
		JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject)parser.parse(jsonString);
        JsonElement id = obj.get("numberOfPages");
        return Integer.parseInt(id.toString());
	}
	
	private int currentPageFromJson(String jsonString){
		JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject)parser.parse(jsonString);
        JsonElement id = obj.get("currentPage");
        return Integer.parseInt(id.toString());
	}
	
	private int resultNumberFromJson(String jsonString){
		JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject)parser.parse(jsonString);
        JsonElement id = obj.get("totalResults");
        return Integer.parseInt(id.toString());
	}
}
