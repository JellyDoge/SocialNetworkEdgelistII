import java.util.*;
import java.io.*;

public class Driver
{
	private HashMap<Integer, TwitterUser> Users = new HashMap<Integer, TwitterUser>();
	private static HashMap<Integer, Integer> fCounter = new HashMap<Integer, Integer>();
	
	public HashMap<Integer, Integer> getFCounterMap()
	{
		return fCounter;
	}
	
	public Map<Integer, TwitterUser> getUsersMap()
	{
		return Users;
	}
	
	public static void main(String[] args)
	{
		Driver driver = new Driver();
		Scanner sc = new Scanner(System.in);
		try
		{
			System.out.println("DEBUG: Calling readFile");
			long startTime = System.currentTimeMillis();
			
			// File to read from
			driver.readFile("social_network.edgelist");
			
			// Variables for recording readFile time
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			long minutes = (totalTime / 1000) / 60;
			long seconds = (totalTime / 1000) % 60;
			
			System.out.println("DEBUG: readFile passed in " + minutes + " minutes and " + seconds + " seconds.");
			
			do
			{
				int menu = 0;
				menu = displayMenu();
				switch(menu)
				{
					case 1:
						// Test getFollower method
						if(driver.Users != null && driver.Users.size() > 0);
						{
							System.out.println("");
							System.out.println("-- getFollowers --");
							System.out.println("Enter a User ID: ");
							int findID = sc.nextInt();
							
							// Calls getNeighborhood for selected userID
							TwitterUser tUser = driver.Users.get(findID);
							//List<Integer> fList = tUser.getNeighborhood(tUser, 2);
							//System.out.println("DEBUG: fList size: " + fList.size());
							
							// Makes a clone of selected userID
							TwitterUser cloned = tUser.clone();
							cloned.setFollowers(new ArrayList<TwitterUser>());
							
							// Print amount of followers for selected userID and cloned userID
							System.out.println("User ID " + findID + " total followers: " + tUser.getFollowers().size());
							//System.out.println("Total followers of cloned user: " + cloned.getFollowers().size());
							sc.nextLine();
						}
						break;
						
					case 2:
						// Test getFollowing method
						if(driver.Users != null && driver.Users.size() > 0);
						{
							System.out.println("");
							System.out.println("-- getFollowing --");
							System.out.println("Enter a User ID: ");
							int findID = sc.nextInt();
							
							// Prints amount of users following selected userID
							TwitterUser tUser = new TwitterUser();
							System.out.println("User ID " + findID  + " total following: " + tUser.getFollowing(findID));
							//fCounter.get(findID));
						}
						break;
						
					case 3:
						// Test getByPopularity method
						if(driver.Users != null && driver.Users.size() > 0);
						{
							System.out.println("");
							System.out.println("-- getByPopularity --");
							System.out.println("Enter sort depth: ");
							int depth = sc.nextInt();
							TwitterUser tUser = new TwitterUser();
							tUser.getByPopularity(depth);
						}
						break;
						
					default:
						System.out.println("Invalid menu choice.");
						break;
				}
			} while(true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// Implemnted displayMenu and do() loop with switch statements to allow repeated use without having to restart program and read entire edgelist again.
	static int displayMenu()
	{
		System.out.println("---- MENU ----");
		System.out.println("1. getFollowers");
		System.out.println("2. getFollowing");
		System.out.println("3. getByPopularity");
		
		Scanner sc = new Scanner(System.in);
		int menu = sc.nextInt();
		return menu;
	}
	
	// readFile method to scan social_network.edgelist to populate Map with UserID and follower information
	// Added functionality since Midterm to add amount of following users to HashMap, explanation in TwitterUser.java
	public void readFile(String filename) throws Exception
	{
		// Progress tracker variables
		int lineCount = 1;
		int progressTracker = 1;
		int progressMult = 1;
		int tenPercent = 1485587;
		
		// Initializes before beginning loop
		System.out.println("DEBUG: Initiating readFile");
		Users = new LinkedHashMap<Integer, TwitterUser>();
		
		Scanner sc = new Scanner(new File(filename));
		String line = "";
		while (sc.hasNext())
		{
			// Pointless progress tracker
			if(lineCount == tenPercent * progressMult)
			{
				System.out.println(progressTracker + "0% complete");
				progressMult++;
				progressTracker++;
			}
			lineCount++;
			
			// Loop to read info from file
			line = sc.nextLine();
			if (line != null && line.trim().length() > 0)
			{
				String lDta[] = line.split(" ");
				if (lDta != null && lDta.length == 2)
				{
					int userID = Integer.parseInt(lDta[0]);
					int fInt = Integer.parseInt(lDta[1]);
					TwitterUser usr = Users.get(userID);
					if (usr == null)
					{
						usr = new TwitterUser();
						usr.setUserID(userID);
					}
					TwitterUser follower = Users.get(fInt);
					if (follower == null)
					{
						follower = new TwitterUser();
						follower.setUserID(fInt);
						Users.put(fInt, follower);
					}
					ArrayList<TwitterUser> lS = usr.getFollowers();
					if (lS == null)
					{
						lS = new ArrayList<TwitterUser>();
					}
					lS.add(follower);
					usr.setFollowers(lS);
					Users.put(userID, usr);
					
					// HashMap fCounter generation and updating - used for getFollowing
					fCounter.putIfAbsent(fInt, 0);
					fCounter.put(fInt, fCounter.get(fInt) + 1);
				}
			}
		}
		System.out.println("DEBUG: readFile complete");
		sc.close();
	}
}
