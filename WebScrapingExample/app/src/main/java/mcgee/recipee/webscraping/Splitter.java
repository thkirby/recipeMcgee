package mcgee.recipee.webscraping;
import android.util.Log;

import java.util.*;

class Splitter {


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

    // Converts fraction characters substrings to multiple character substrings.
    // e.g. "½ cup flour" becomes "1/2 cup flour"
    public static String fracChar(String oldstr){
        char curr;
        String str = "";
        for (int i = 0; i < oldstr.length(); i++){
            curr = oldstr.charAt(i);
            if (curr == '½'){
                str = str + " 1/2";
            }
            else if (curr == '⅓'){
                str = str + " 1/3";
            }
            else if(curr == '⅔'){
                str = str + " 2/3";
            }
            else if(curr == '¼'){
                str = str + " 1/4";
            }
            else if(curr == '¾'){
                str = str + " 3/4";
            }
            else if(curr == '⅕'){
                str = str + " 1/5";
            }
            else if(curr == '⅖'){
                str = str + " 2/5";
            }
            else if(curr == '⅗'){
                str = str + " 3/5";
            }
            else if(curr == '⅘'){
                str = str + " 4/5";
            }
            else if(curr == '⅙'){
                str = str + " 1/6";
            }
            else if(curr == '⅚'){
                str = str + " 5/6";
            }
            else if(curr == '⅐'){
                str = str + " 1/7";
            }
            else if(curr == '⅛'){
                str = str + " 1/8";
            }
            else if(curr == '⅜'){
                str = str + " 3/8";
            }
            else if(curr == '⅝'){
                str = str + " 5/8";
            }
            else if(curr == '⅞'){
                str = str + " 7/8";
            }
            else if(curr == '⅑'){
                str = str + " 1/9";
            }
            else if(curr == '⅒'){
                str = str + " 1/10";
            }

            else{
                str = str + curr;
            }
        }

        return str;
    }

    // Multiplies values when 'two' 'three', etc. are in the thing
    // e.g. 'two 14-oz cans of woop-ass' becomes '28-oz cans of woop-ass' the cans gets filtered out later
    // and  'two eggs' becomes '2 eggs'
    public static String writtenNums(String oldstr, List<String> measurements){

        String[] parts;

        oldstr = oldstr.replaceAll("[-]", " ");
        oldstr = oldstr.replaceAll("[()]", "");
        parts = oldstr.split(" ");

        float numFound = -1;

        for (int i=0; i < parts.length; i++){
            if(parts[i].equals("one")){
                parts[i] = "1";
                numFound = 1;
            }
            else if(parts[i].equals("two")){
                parts[i] = "2";
                numFound = 2;
            }
            else if(parts[i].equals("three")){
                parts[i] = "3";
                numFound = 3;
            }
            else if(parts[i].equals("four")){
                parts[i] = "4";
                numFound = 4;
            }
            else if(parts[i].equals("five")){
                parts[i] = "5";
                numFound = 5;
            }
            else if(parts[i].equals("six")){
                parts[i] = "6";
                numFound = 6;
            }
            else if(parts[i].equals("half")){
                parts[i] = ".5";
                numFound = .5f;
            }
            else if(parts[i].equals("quarter")){
                parts[i] = ".25";
                numFound = .25f;
            }
            else if(parts[i].equals("dozen")){
                parts[i] = "12";
                numFound = 12;
            }
            else if(isNumeric(parts[i]) && i != 0 && i != parts.length-1 && numFound != -1 && measurements.contains(parts[i+1])){
                parts[i] = Float.toString(Float.parseFloat(parts[i])*numFound);
            }
        }

        String str = "";

        for (int i = 0; i < parts.length; i++){
            str += parts[i] + " ";
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

    // Returns a string with any content in carrots (<>) removed
    public static String noCarrots(String oldstr){
        String str = "";
        int level = 0;
        for (int i = 0; i < oldstr.length(); i++){
            if (oldstr.charAt(i) == '<'){
                level = level + 1;
            }
            else if (oldstr.charAt(i) == '>'){
                level = level - 1;
            }
            else if (level <= 0){
                str = str + oldstr.charAt(i);
            }
        }

        return str;
    }

    public static String onlyCurlyBracket(String oldstr){
        String str = "";
        int level = 0;
        for (int i = 0; i < oldstr.length(); i++){
            if (oldstr.charAt(i) == '{'){
                level = level + 1;
                str = str+'{';
            }
            else if (oldstr.charAt(i) == '}'){
                level = level - 1;
                str = str+'}';
            }
            else if (level > 0){
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
        String[] ret = new String[3];
        ret[0] = ""; //name of the ingredient
        ret[1] = ""; //how many of the ingredient
        ret[2] = ""; //unit of measurement
        String[] parts;

        List<String> remove = Arrays.asList("of", "diced", "crushed", "", "for",
                "serving", "and", "drained", "rinsed",
                "chopped", "can", "cans", "one", "at",
                "softened", "room", "temperature", "to", "a",
                "an", "cooked", "grated");
        List<String> measurements = Arrays.asList("cup", "cups", "g", "mg",
                "grams", "gram", "oz",
                "ounces", "ounce", "pounds",
                "teaspoon", "teaspoons", "tablespoon",
                "tablespoons", "tsp", "tbsp");

        str = str.replaceAll("[,:]", "").toLowerCase();
        str = noCarrots(str);
        str = fracChar(str);
        str = fracToDec(spaceGrams(str));
        str = writtenNums(str, measurements);
        str = str.replaceAll("[-()]", " ");
        parts = str.split(" ");

        boolean enteredNum = false;
        String mVal = "1", mType = "";
        Log.d("Should be a number: ", parts[0]);
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
        ret[1] = mVal;
        if (mType.equals("")){
            ret[2] = "units";
        }
        else {
            ret[2] = mType;
        }

        return ret;
    }

}
