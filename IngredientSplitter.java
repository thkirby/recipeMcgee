import java.util.*;

class Splitter {
	public static void main(String[] args){
		String ingr = "Flour 50 grams";
		String[] ingrs = splitIngr(ingr);
		for (String var : ingrs){
			System.out.println(var);
		}
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
        		return false;
    		}
    		try {
        		double d = Double.parseDouble(strNum);
    		} catch (NumberFormatException nfe) {
        		return false;
    		}
    		return true;
	}

	public static String[] splitIngr(String str){
		String[] ret = new String[2];
		ret[0] = "";
		ret[1] = "";
		String[] parts;
		parts = str.split(" ");
		List<String> remove = Arrays.asList("of", "diced");
		List<String> measurements = Arrays.asList("cup", "cups", "g", "mg", 
							  "grams", "gram", "oz",
							  "ounces");

		for (int i = 0; i < parts.length; i++){
			if (isNumeric(parts[i])){
				ret[1] = parts[i] + " " + ret[1];
			}
			else if (measurements.contains(parts[i])){
				ret[1] = ret[1] + parts[i];
			}
			else if (remove.contains(parts[i])){
				; //do nothing
			}
			else {
				ret[0] = ret[0] + parts[i] + " ";
			}

		}

		return ret;
	}
}
