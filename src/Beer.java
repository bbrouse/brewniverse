/*
 * Ben Brouse
 * bjb85@drexel.edu
 * CS338:GUI, Final Project (Brewniverse)
 */

public class Beer {
	
	public String id;
	public String name;
	public String description;
	public float abv;
	public float ibu;
	public BeerAvailability available;
	public String isOrganic;
	public int year;
	public BeerStyle style;
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
		if (abv >= 0 && abv <= 3){
			return "Low ABV";
		}
		else if(abv >= 4 && abv <= 6){
			return "Average ABV";
		}
		else{
			return "High ABV";
		}
	}
}
