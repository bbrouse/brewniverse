
public class Beer {
	
	public String id;
	public String name;
	public String iconLabelUrl, mediumLabelUrl, largeLabelUrl;
	public String description;
	public float abv;
	public int ibu;
	public String availablity;
	public boolean isOrganic;
	public int year;
	public String style;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconLabelUrl() {
		return iconLabelUrl;
	}

	public void setIconLabelUrl(String iconLabelUrl) {
		this.iconLabelUrl = iconLabelUrl;
	}

	public String getMediumLabelUrl() {
		return mediumLabelUrl;
	}

	public void setMediumLabelUrl(String mediumLabelUrl) {
		this.mediumLabelUrl = mediumLabelUrl;
	}

	public String getLargeLabelUrl() {
		return largeLabelUrl;
	}

	public void setLargeLabelUrl(String largeLabelUrl) {
		this.largeLabelUrl = largeLabelUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getAbv() {
		return abv;
	}

	public void setAbv(float abv) {
		this.abv = abv;
	}

	public int getIbu() {
		return ibu;
	}

	public void setIbu(int ibu) {
		this.ibu = ibu;
	}

	public String getAvailablity() {
		return availablity;
	}

	public void setAvailablity(String availablity) {
		this.availablity = availablity;
	}

	public boolean isOrganic() {
		return isOrganic;
	}

	public void setOrganic(boolean isOrganic) {
		this.isOrganic = isOrganic;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

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
