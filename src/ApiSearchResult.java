
public class ApiSearchResult {
	
	public Beer[] beers;
	public int numResults;
	public int numPages;
	public int currentPage;
	
	public ApiSearchResult(Beer[] inBeers, int inNumResults, int inNumPages, int inCurrentPage){
		beers = inBeers;
		numResults = inNumResults;
		numPages = inNumPages;
		currentPage = inCurrentPage;
	}

}
