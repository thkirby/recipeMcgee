import java.util.*;

class Splitter {
	public static void main(String[] args){
		String ingr = args[0];
		String[] ingrs = splitIngr(ingr);
		for (String var : ingrs){
			System.out.println(var);
		}
	}

	// Checks if a string is numeric, returns a boolean
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

	// Returns a string with the fraction substrings converted into decimals
	public static String fracToDec(String oldstr){
		String str = new String();
		str = "";

		return str;
	}

	// Returns a string with spaces between numerical characters and grams
	public static String spaceGrams(String oldstr){
		String str = new String();
		str = "";

		char prev = ' ';
		char prev2 = ' ';
		for (int i = 0; i < oldstr.length(); i++){
			if (oldstr.charAt(i) == 'g' && Character.isDigit(prev)){
				str = str + " ";
			}
			else if (oldstr.charAt(i) == 'g' && Character.isDigit(prev2)){
				str = str.substring(0, i-1) + " " + str.charAt(i-1);
			}

			prev2 = prev;
			prev = oldstr.charAt(i);
			str = str + prev;
		}
		
		return str;
	}

	// Returns an array of two strings, one for the ingredient and one for the quantity
	public static String[] splitIngr(String str){
		String[] ret = new String[2];
		ret[0] = "";
		ret[1] = "";
		String[] parts;

		List<String> remove = Arrays.asList("of", "diced", "crushed");
		List<String> measurements = Arrays.asList("cup", "cups", "g", "mg", 
							  "grams", "gram", "oz",
							  "ounces", "ounce");
		parts = spaceGrams(str.toLowerCase()).split(" ");

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
