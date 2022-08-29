import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Map.Entry;  

public class TwitterUser implements Comparable<TwitterUser>, Cloneable
{
	// Define variables
	private ArrayList<TwitterUser> followers;
	private int userID;
	Driver driver = new Driver();

	// Method to return userID
	public int getUserID()
	{
		return userID;
	}
	
	// Method to set User ID
	public void setUserID(int userID)
	{
		this.userID = userID;
	}
	
	// Method to return followers array
	public ArrayList<TwitterUser> getFollowers()
	{
		return followers;
	}
	
	// Method to set followers array
	public void setFollowers(ArrayList<TwitterUser> followers)
	{
		this.followers = followers;
	}
	
	// Method to compare UserIDs
	@Override
	public int compareTo(TwitterUser lO)
	{
		if(lO.userID == this.userID)
			return 0;
		if(lO.userID < this.userID)
			return -1;
		else
			return 1;
	}
	
	// Method to create TwitterUser clone
	@Override
	protected TwitterUser clone() throws CloneNotSupportedException
	{
		// Create clone
		TwitterUser user = new TwitterUser();
		user.userID = this.userID;
		if(this.followers != null)
		{
			ArrayList cloneArray = (ArrayList) this.followers.clone();
			user.setFollowers(cloneArray);
		}
		return user;
	}
	
	// Method for getting user neighborhood of followed users
	public ArrayList<Integer> getNeighborhood(TwitterUser ID, int depth)
	{
		// Create followers array
		ArrayList<Integer> followers = new ArrayList<Integer>();
		
		// Checks if followers array is not empty
		if(depth > 0 && ID.followers != null && ID.followers.size() > 0)
		{
			// Iterates through followers array to add followers to neighborhood list
			for(TwitterUser Follower:ID.followers) {
				followers.add(Follower.userID);
				ArrayList<Integer> follower = getNeighborhood(Follower, depth-1);
				
				for(int folInt:follower)
				{
					if(!followers.contains(folInt))
					{
						followers.add(folInt);
					}
				}
			}
		}
		return followers;
	}
	
	// EXPLANATION for Collection<TwitterUser> getFollowing(TwitterUser user)
	// HashMap<Integer, Integer> fCounter was created in Driver.Java to store the userID and amount of users following.
	// Two new lines were added in the main readFile loop to add values to this hashmap as the edgelist file is read
	// fCounter.putIfAbsent(fInt, 0); 					- Creates a new entry on the hashmap of the second value in the edgelist and initializes value to zero
	// fCounter.put(fInt, fCounter.get(fInt) + 1);		- Updates already existing entries with and incremented following count number when the value is found again.
	// getFollowing is then called with findID in Driver.java to search the hashmap with the findID key for number of following users.
	public ArrayList<Integer> getFollowing(int findID)
	{
		HashMap<Integer, Integer> fCounter = driver.getFCounterMap();
		ArrayList<Integer> fArray = new ArrayList<Integer>();
		fArray.add(fCounter.get(findID));
		
		return fArray;
	}
	
	// EXPLANATION for TwitterUser getByPopularity(int x)
	// Method starts by calling the sortByKey method which calls getFCounterMap to get a HashMap of fCounterMap from Driver.java
	//
	// This is then converted into a TreeMap as to be automatically sorted by key ID. This TreeMap is returned to getByPopularity.
	//
	// getByPopularity then creates a LinkedHashMap with the sorted TreeMap. The TreeMap is iterated through and all entries with 
	// a ID greater then the requested depth is removed.
	//
	// A new Map is then made calling sortByValue with the new sorted and "reized" list. This is where the Map is sorted to display
	// following users from largest to smallest as well as deal with matching following counts.
	//
	// Finally, a for loop is ran with the returned finished Map and printed in order.
	public TwitterUser getByPopularity(int depth)
	{
		// Call sortByKey() to sort fCounter by userID to filter search by depth
		// Returns TreeMap keySortMap
		TreeMap<Integer, Integer> keySortMap = sortByKey(depth);
		
		// Iterates through keySortMap and removes all values greater then search depth
		for(Map.Entry<Integer, Integer> e : new LinkedHashMap<Integer, Integer>(keySortMap).entrySet())
		{
			if(e.getKey() > depth)
			{
				keySortMap.remove(e.getKey());
			}
		}
		
		// Iterate through filter and modified keySortMap to display correct getByPopularity results
		Map<Integer, Integer> hm1 = sortByValue(keySortMap);
		for (Map.Entry<Integer, Integer> en : hm1.entrySet())
		{
			System.out.println("UserID: " + en.getKey() + "		| Following: " + en.getValue());
		}
		return null;
	}
	
	// Method for sorting keySortMap by largest to lowest values
	public HashMap<Integer, Integer> sortByValue(TreeMap<Integer, Integer> keySortMap)
	{
		// Convert TreeMap to List
        List<Map.Entry<Integer, Integer> > list = new LinkedList<Map.Entry<Integer, Integer> >(keySortMap.entrySet());
  
         // Sort list from highest to lowest
         Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >()
         {
             public int compare(Map.Entry<Integer, Integer> o2, Map.Entry<Integer, Integer> o1)
             {
            	 // If statement to sort users with the same following count, sorts user by followers
            	 if(o1.getValue() == o2.getValue());
            	 {
            		 Map<Integer, TwitterUser> Users = driver.getUsersMap();
            		 
            		 TwitterUser u1 = Users.get(o2.getKey());
            		 //u1.getFollowers().size();
            		 
            		 TwitterUser u2 = Users.get(o1.getKey());
            		 //u2.getFollowers().size();
            	 }
                 return (o1.getValue()).compareTo(o2.getValue());
             }
         });
          
         // Put sorted list into HashMap and return
         HashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
         for (Map.Entry<Integer, Integer> aa : list)
         {
        	 map.put(aa.getKey(), aa.getValue());
         }
         return map;
	}
	
	// Method for sorting fCounter by Key value
	public TreeMap<Integer, Integer> sortByKey(int depth)
	{
		// Get HashMap from Driver.java
		HashMap<Integer, Integer> fCounter = driver.getFCounterMap();
		
		// Convert HashMap to TreeMap and copy into TreeMap to be sorted
		TreeMap<Integer, Integer> sorted = new TreeMap<>();
		sorted.putAll(fCounter);
		
		return sorted;
	}
}
