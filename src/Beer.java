import java.util.ArrayList;
import java.util.List;


public class Beer {
	
	public String id;
	public String name;
	public String description;
	public float abv;
	public float ibu;
	public String availablity;
	public String isOrganic;
	public int year;
	public Style style;
	public Label labels;
	public SrmValue srm;

	public String getIBUDescription(){
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
	
	public String getABVDescription(){
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
