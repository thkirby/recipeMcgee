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
	// "1/2 pound of pork" returns ".5 pound of pork"
	public static String fracToDec(String oldstr){
		String str = "";
		
		boolean checking = false;
		boolean denom = false;
		String numerator = "";
		String denominator = "";
		float addF = 0;
		for (int i = 0; i < oldstr.length(); i++){
			char curr = oldstr.charAt(i);
			if (checking){
				if (Character.isDigit(curr)){
					if (!denom){
						numerator = numerator + curr;
					}
					else{
						denominator = denominator + curr;
					}
				}
				else if (curr == '/' || curr == '\\'){
					denom = true;
				}
				else if ((curr == '-' || curr == ' ') && !"".equals(numerator) && !denom){ //for things like 1-1/2
					addF = Float.parseFloat(numerator);
					numerator = "";
				}
				else{
					if (denom == true && !"".equals(denominator) && !"".equals(numerator)){
						String toAdd = Float.toString(addF + ((Float.parseFloat(numerator))/(Float.parseFloat(denominator))));
						str = str + " " + toAdd + curr;
					}
					else{
						if (addF != 0){
							str = str + addF + " " + numerator + curr;
						}
						else{
							str = str + numerator + curr;
						}
					}

					numerator = "";
					denominator = "";
					addF = 0;
					denom = false;
					checking = false;
				}
			}
			else{
				if(Character.isDigit(curr)){
					numerator = numerator + curr;
					checking = true;
				}
				else{
					str = str + curr;
				}
			}
			
		}

		return str;
	}

	// Returns a string with any content in parenthesis removed
	public static String noParenth(String oldstr){
		String str = "";
		int level = 0;

		for (int i = 0; i < oldstr.length(); i++){
			if (oldstr.charAt(i) == '('){
				level = level + 1;	
			}
			else if (oldstr.charAt(i) == ')'){
				level = level - 1;
			}
			else if (level <= 0){
				str = str + oldstr.charAt(i);
			}
		}

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

		List<String> remove = Arrays.asList("of", "diced", "crushed", "", "for",
						    "serving", "and", "drained", "rinsed",
						    "chopped", "can", "cans", "one", "at",
						    "softened", "room", "temperature", "to");
		List<String> measurements = Arrays.asList("cup", "cups", "g", "mg", 
							  "grams", "gram", "oz",
							  "ounces", "ounce", "pounds",
							  "teaspoon", "teaspoons", "tablespoon",
							  "tablespoons");

		str = str.replaceAll("[,:]", "");
		str = fracToDec(spaceGrams(noParenth(str)));
		str = str.replaceAll("[-]", " ");
		parts = str.split(" ");
		
		boolean enteredNum = false;
		String mVal = "", mType = "";
		for (int i = 0; i < parts.length; i++){
			if (isNumeric(parts[i])){
				mVal = parts[i];
				enteredNum = true;

			}
			else if (measurements.contains(parts[i])){
				mType = parts[i];
			}
			else if (remove.contains(parts[i])){
				; //do nothing
			}
			else {
				ret[0] = ret[0] + parts[i] + " ";
			}

		}
		ret[1] = mVal + " " + mType;

		return ret;
	}
}
