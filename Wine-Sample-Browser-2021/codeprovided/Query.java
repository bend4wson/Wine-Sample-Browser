package uk.ac.sheffield.assignment2021.codeprovided;

import java.util.*;

/**
 * Class designed to be used to create Query objects from the Query List.
 *
 * @version 1.1  09/02/2021
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ben Clegg
 *
 * Copyright (c) University of Sheffield 2021
 */
public class Query {

    final List<SubQuery> subQueryList;
    final WineType wineType;

    // Constructor
    public Query(List<SubQuery> subQueryList, WineType wineType) {
        this.subQueryList = subQueryList;
        this.wineType = wineType;
    }

    public WineType getWineType() {
        return wineType;
    }

    /**
     * Get SubQueries of this Query
     *
     * @return subQueryList
     */
    public List<SubQuery> getSubQueryList() {
        return subQueryList;
    }


    /**
     * Apply the Query to the wines of a WineSampleCellar, retrieve the wines which match.
     * @param cellar the WineSampleCellar to query
     * @return List of filtered wine samples
     */
    public List<WineSample> executeQuery(AbstractWineSampleCellar cellar) {

        // Start by adding all of the wines with the matching type
        List<WineSample> filteredWineList = new ArrayList<>(cellar.getWineSampleList(wineType));

        // Continuously filter the wines according to each SubQuery
        //System.out.println(subQueryList + " **");
        for (SubQuery subQuery : subQueryList)
        {
            filteredWineList = executeSubQuery(filteredWineList, subQuery);

        }

        // Return the filtered wines
        return filteredWineList;
    }

    /**
     * Filter provided wines according to a SubQuery
     * @param wines the Collection of relevant wines
     * @param subQuery the SubQuery to use to filter wines
     * @return List of all wines which meet criteria
     */
    private List<WineSample> executeSubQuery(Collection<WineSample> wines, SubQuery subQuery) {
        List<WineSample> filteredWineList = new ArrayList<>();
        for (WineSample w : wines) {
            if(subQuery.wineMatchesSubQuery(w))
                filteredWineList.add(w);
        }
        return filteredWineList;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subQueryList == null) ? 0 : subQueryList.hashCode());
		result = prime * result + ((wineType == null) ? 0 : wineType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Query other = (Query) obj;
		if (subQueryList == null) {
			if (other.subQueryList != null)
				return false;
		} else if (!subQueryList.equals(other.subQueryList))
			return false;
        return wineType == other.wineType;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Query: ").append(wineType).append("{");

        Iterator<SubQuery> subQueryIterator = subQueryList.iterator();

        while (subQueryIterator.hasNext()) {
            sb.append(subQueryIterator.next());
            if(subQueryIterator.hasNext())
                sb.append(", ");
        }

        sb.append("}");

        return sb.toString();
    }
    
}