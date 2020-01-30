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
		String[] ret[2];
		ArrayList parts = str.split(" ");
		String[] remove = ["of"];

		parts.removeAll(remove);

		for (int i = 0; i < parts.length; i++){
			if (isNumeric(String[i])){
				
				i = i-1;
			}
		}

		return ret;
	}
}
