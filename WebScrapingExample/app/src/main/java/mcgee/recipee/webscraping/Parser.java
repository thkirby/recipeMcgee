package mcgee.recipee.webscraping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    public static List<String> findVar(String doc){
        List<String> queries = Arrays.asList("dsfgsdgfsdf", "recipeIngredient", "ingredients-item-name","ingredients-item",
                "o-Ingredients__a-Ingredient"); //TODO: remove the nonsense word at 0, that's just for testing.
        ArrayList<String> ret = new ArrayList<>();
        int query_loc = -1;
        outer: for(String query : queries){
            int len = query.length();
            for (int i = 0; i < doc.length()-len - 1; i++) {
                if (doc.substring(i, i + len).equals(query)) { // query has been found
                    query_loc = i + len;
                    break outer;
                }
            }
        }

        if (query_loc == -1){
            return null;
        }

        else{
            boolean inArray = false;
            boolean inQuotes = false;
            StringBuilder ingr = new StringBuilder();
            outer: for (int i = query_loc; i < doc.length(); i++){
                switch (doc.charAt(i)){
                    case '[':
                        inArray = true;
                        break;
                    case ']':
                        inArray = false;
                        break outer;
                    case '"':
                        if (inArray && !inQuotes){
                            inQuotes = true;
                        }
                        else if (inArray && inQuotes){
                            inQuotes = false;
                            ret.add(ingr.toString());
                            ingr.setLength(0);
                        }
                        break;
                    case '\\': //if there's a \" or newline in the middle or something, it just skips over it
                        i++;
                        break;
                    default:
                        if (inArray && inQuotes){
                            ingr.append(doc.charAt(i));
                        }
                        break;

                }

            }
        }

        return ret;
    }
}
