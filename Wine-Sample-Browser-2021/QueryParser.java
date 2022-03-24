package uk.ac.sheffield.assignment2021;

import uk.ac.sheffield.assignment2021.codeprovided.AbstractQueryParser;
import uk.ac.sheffield.assignment2021.codeprovided.Query;
import uk.ac.sheffield.assignment2021.codeprovided.SubQuery;
import uk.ac.sheffield.assignment2021.codeprovided.WineProperty;
import uk.ac.sheffield.assignment2021.codeprovided.WineType;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * QueryParser extends AbstractQueryParser, and parses through a given file, creating query and 
 * sub query objects which are then later used to generate statistics and histogram data.
 * @author benda
 *
 */
public class QueryParser extends AbstractQueryParser {
    
    static final String[] VALID_OPERATORS =  {"<", "<=", "=", ">=", ">", "!="};
    static final String[] VALID_PROPERTIES = {"f_acid", "v_acid", "c_acid", "r_sugar", "chlorid", "f_sulf", "t_sulf", "dens", "pH", "sulph", "alc", "qual"};
    
    /**
     * 1 - receives the List of Strings, each is a single token
     * 2 - assesses their content, creates the relevant Query & SubQuery objects
     * 3 - and then returns a List of the Query objects
     *
     * @param queryTokens The List of tokenized Strings from the readQueryFile method
     * @return List of all Query objects
     * @throws IllegalArgumentException if the provided query tokens are invalid (e.g. non-numbers
     * as boundary values, invalid operators, etc)
     */
    @Override
    public List<Query> readQueries(List<String> queryTokens) throws IllegalArgumentException {
        ArrayList<Query> queryList = new ArrayList<Query>();
        WineType wineType = null;
        WineProperty wineProperty = null;
        String wineOperator = null;
        double wineValue = 0.0;
        
        //This array list of array lists is used to store lists of subqueries. When a new query is created, the next subquery list
        //is used. This avoids complications involving clearing existing lists for re-use while currently being used in other 
        //query objects.
        ArrayList<ArrayList<SubQuery>> subQueryLists = new ArrayList<ArrayList<SubQuery>>();
        for (int i=0; i<queryTokens.size(); i++) {
            subQueryLists.add(new ArrayList<SubQuery>());
        }
        int queryNumber = 0;
        
        for (int i=0; i<queryTokens.size(); i++) { 
            if ((i < queryTokens.size()-2) && (queryTokens.get(i).equals("red")  || queryTokens.get(i).equals("white") || queryTokens.get(i+1).equals("or"))) {
                String type = queryTokens.get(i);
                if (type.equals("white") && !queryTokens.get(i+1).equals("or")) {
                    wineType = WineType.WHITE;
                } else if (type.equals("red") && !queryTokens.get(i+1).equals("or")) {
                    wineType = WineType.RED;
                } else {
                    wineType = WineType.ALL;
                    i += 2;
                }
            }
            else if (queryTokens.get(i).equals("and")) {
                subQueryLists.get(queryNumber).add(new SubQuery(wineProperty, wineOperator, wineValue));
                
            }
            
            else if ((queryTokens.get(i).equals("select") || i+1 == queryTokens.size()) && i>1) {
                subQueryLists.get(queryNumber).add(new SubQuery(wineProperty, wineOperator, wineValue));
                queryList.add(new Query(subQueryLists.get(queryNumber), wineType));
                queryNumber += 1;
            }
            else {
                boolean found = false;
                if (found == false) {
                    for (String property : VALID_PROPERTIES) {
                        
                        if (property.equals(queryTokens.get(i))) {
                            wineProperty = WineProperty.fromFileIdentifier(queryTokens.get(i));
                            found = true;
                        } 
                    }
                }
                
                if (found == false) {
                    for (String operator : VALID_OPERATORS) {
                        if (operator.equals(queryTokens.get(i))) {
                            wineOperator = queryTokens.get(i);
                            found = true;
                        } 
                    }
                }
                
                if ((found == false) && (!queryTokens.get(i).equals("where")) && i>0) {
                    wineValue = Double.parseDouble(queryTokens.get(i)); 
                }
            }
        }
        
        return queryList;
    }
}
