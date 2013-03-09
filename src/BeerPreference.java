
public class BeerPreference {
	public String name;
	public float ibuMin, ibuMax, abvMin, abvMax;
	public SrmValue srm;
	public Style style;
	
	public BeerPreference(String inName, int inIbuMin, int inIbuMax, int inAbvMin, int inAbvMax, SrmValue inSrm, Style inStyle){
		name = inName;
		if(inIbuMin < 0 || inIbuMax < 0){
			ibuMin = -1;
			ibuMax = -1;
		}
		else{
			ibuMin = inIbuMin;
			ibuMax = inIbuMax;
		}
		
		if(inAbvMin < 0 || inAbvMax < 0){
			abvMin = -1;
			abvMax = -1;
		}
		else{
			abvMin = inAbvMin;
			abvMax = inAbvMax;
		}
		
		srm = inSrm;
		style = inStyle;
	}
	
	public boolean hasIbuPreference(){
		if (ibuMin == -1 && ibuMax == -1){
			return false;
		}
		else{
			return true;
		}
	}
	
	public boolean hasAbvPreference(){
		if (abvMin == -1 && abvMax == -1){
			return false;
		}
		else{
			return true;
		}
	}
	
	public boolean hasSrmPreference(){
		if(srm == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	public boolean hasStylePreference(){
		if (style == null){
			return false;
		}
		else{
			return true;
		}
	}
}
