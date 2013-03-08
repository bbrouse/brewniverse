
public class Beer {
	
	public int id;
	public String name;
	public String iconLabelUrl, mediumLabelUrl, largeLabelUrl;
	public String description;
	public float abv;
	public int ibu;
	public String availablity;
	public boolean isOrganic;
	public int year;
	public String style;
	
	public String getIBURange(){
		if (ibu >= 0 && ibu <= 33){
			return "Less Bitterness";
		}
		else if(ibu >= 34 && ibu <= 66){
			return "Medium Bitterness";
		}
		else{
			return "High Bitterness";
		}
	}
	
	public String getABVRange(){
		if (ibu >= 0 && ibu <= 6){
			return "Low ABV";
		}
		else if(ibu >= 7 && ibu <= 13){
			return "Medium ABV";
		}
		else{
			return "High ABV";
		}
	}
	
	
	

}
