import java.util.ArrayList;
import java.util.List;


public class SearchResult {
	
	public List<Beer> data = new ArrayList<Beer>();
	public int totalResults = 0;
	public int numberOfPages = 0;
	public int currentPage = 0;
	public BeerPreference preference;
	public String query;
	public boolean isPreferenceSearch;
	
	public boolean hasPagesLeft(){
		return (currentPage < numberOfPages);
	}

}
