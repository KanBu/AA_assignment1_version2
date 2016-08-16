import java.io.*;
import java.util.*;
import java.util.Map.Entry;


/**
 * Adjacency list implementation for the FriendshipGraph interface.
 * 
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2016.
 */
public class AdjList <T extends Object> implements FriendshipGraph<T>  
{   
	private HashMap<T, Integer> map = new HashMap<T, Integer>();
	private NKList<T>[] friends = (NKList<T>[]) new Object[4000];
	private int totalPeople; 
	private NKList<Integer> indexAfterDeletion = new NKList<Integer>(); // to record the list of index of people having been deleted
	
	
	private NKList<T> visitedList = new NKList<T>();
	private NKList<T> checkList = null;
	
    /**
	 * Constructs empty graph.
	 */
    public AdjList() {
    	map = null;
    	friends = null;  	
    	totalPeople = 0;
    	indexAfterDeletion = null;
    } // end of AdjList()

    
    
    public void addVertex(T vertLabel) {
    	
    	int index;
    	// check if the array overflow or not
    	if(totalPeople >= 4000){
    		// get the last index value in the NKList "indexAfterDeletion"
    		index = indexAfterDeletion.getVertice(indexAfterDeletion.getLength()-1);
    	}else{
    		// in the condition that no overflow
    		index = totalPeople;
    	}
    		
    	
    	// register new person
    	friends[index] = new NKList<T>();
    	// update the map
    	map.put(vertLabel, index);
    	// update the total people
    	totalPeople ++;
    	
    } // end of addVertex()
	
    
    public void addEdge(T srcLabel, T tarLabel) {
        
    	//??check if these two people exist ?? Try-catch-throw??
    	if(!checkPersonInList(srcLabel)){
    		System.out.println("The first person does not exist! Please add the person first!");
    		return;
    	}
    	
    	if(!checkPersonInList(tarLabel)){
    		System.out.println("The last person does not exist! Please add the person first!");
    		return;
    	}
    	
    	//find and update the srcLabel's friends list
    	int index = map.get(srcLabel);
    	friends[index].addVertice(tarLabel);  
    	
    	//find the update the tarLabel's friends list
    	index = map.get(tarLabel);
    	friends[index].addVertice(srcLabel);
    	
    	
    } // end of addEdge()
	

    public ArrayList<T> neighbours(T vertLabel) {
        ArrayList<T> neighbours = new ArrayList<T>();
        
        // ?? check if the vertLabel exists or not?? try-catch??
        if(!checkPersonInList(vertLabel)){
    		System.out.println("The person does not exist! Please add the person first!");
    		return neighbours;
    	}
        
        // find the neighbours
        int address = map.get(vertLabel);
        
        NKList<T> targetFriends =friends[address]; 
        for(int i = 0; i < targetFriends.getLength(); i++){
        	neighbours.add(targetFriends.getVertice(i));
        }
   
        return neighbours;
    } // end of neighbours()
    
    
    public void removeVertex(T vertLabel) {
    	
    	// ?? check if the vertLabel exists or not?? try-catch??
    	 if(!checkPersonInList(vertLabel)){
     		System.out.println("The person does not exist!");
     		return;
     	}
    	 
    	// get the index and store it it indexAfterDeletion
    	 int index = map.get(vertLabel);
    	 indexAfterDeletion.addVertice(index);
    	 
    	// remove the person from his/her friends list
    	 removeVFromFriendsList(vertLabel);
    	 
    	// clear the friends index of the target person
    	 friends[index] = null;
    	// remove the person in map
    	map.remove(vertLabel);

    	// update total people number
    	totalPeople --;
    	

    } // end of removeVertex()
	
    
    public void removeEdge(T srcLabel, T tarLabel) {
    	//??check if these two people exist and the edge existed? ?? Try-catch-throw??
    	if(!checkPersonInList(srcLabel)){
    		System.out.println("The first person does not exist! Please add the person first!");
    		return;
    	}
    	
    	if(!checkPersonInList(tarLabel)){
    		System.out.println("The last person does not exist! Please add the person first!");
    		return;
    	}
    	 
    	// remove the edge on both sides
    	int index = map.get(srcLabel);
    	friends[index].deleteVertice(tarLabel);  
    	
    	//find and update the tarLabel's friends list
    	index = map.get(tarLabel);
    	friends[index].addVertice(srcLabel);
    	
    } // end of removeEdges()
	
    
    public void printVertices(PrintWriter os) {  // "PrintWriter" print into a file??
    	for(Entry<T, Integer> entry: map.entrySet()){
    		System.out.print((String)entry.getKey() + '\t');  // ?? can I convert T into String, in order to print??
    	}
    	
    } // end of printVertices()
    
    // for testing only
    public void printVertices() {  // "PrintWriter" print into a file??
    	for(Entry<T, Integer> entry: map.entrySet()){
    		System.out.print((String)entry.getKey() + '\t');  // ?? can I convert T into String, in order to print??
    	}
    	
    } // end of printVertices()
    
    
    
    
    
    
    public void printEdges(PrintWriter os) {	//  "PrintWriter"  print into a file??
    	NKList<T> friendList = new NKList<T>();
    	for(Entry<T, Integer> entry: map.entrySet()){
    		friendList = friends[entry.getValue()];
    		for(int i = 1; i <= friendList.getLength(); i++){
    			System.out.println(entry.getKey() + "  " +(String)friendList.getVertice(i));  // if use PrintWriter, then replace "System.out" with "os.", followed by os.close();
    			
    		}
    	}
    } // end of printEdges()
    
    // for testing only
    public void printEdges() {	//  "PrintWriter"  print into a file??
    	NKList<T> friendList = new NKList<T>();
    	for(Entry<T, Integer> entry: map.entrySet()){
    		friendList = friends[entry.getValue()];
    		for(int i = 1; i <= friendList.getLength(); i++){
    			System.out.println(entry.getKey() + "  " +(String)friendList.getVertice(i));  
    		}
    	}
    } // end of printEdges()
    
    
    
    
    
    
    
    
    
    
    
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
    	
    	int disconnectedDist = 1;
    	
    	// vertLabel1 == vertLabel2
    	if(vertLabel1 == vertLabel2){
    		disconnectedDist = 0;
    		return disconnectedDist;
    	}
    	
    	// initialize the checkList with vertLabel1
    	checkList.addVertice(vertLabel1);
    	if(findInFriends(vertLabel1, vertLabel2)){
    		return disconnectedDist;
    	}else{
    		NKbfs(disconnectedDist, vertLabel2);
    	}
    		
        // if we reach this point, source and target are disconnected
        return disconnectedDist;    	
    } // end of shortestPathDistance()
    
    
    public int NKbfs(int disconnectedDist, T personTarg){
    	boolean found = false;
    	NKList<T> tempList = null;
    	
    	// draft the new checkList on tempList by collecting all the friends of the people in the current checkList
		for(int i =1; i <= checkList.getLength(); i++){
			// for each person in the checklist
			T tempPerson = checkList.getVertice(i);
			// get their friend list as tempFriends
			NKList<T> tempFriends = friends[map.get(tempPerson)];
			// add each unvisited person in tempFriends to the checkList
			for(int j = 1;j < tempFriends.getLength(); j++){
				// check if it is visited
				if(!visitedList.alreadyExist(tempFriends.getVertice(j))){
					// add friends to checklist
					tempList.addVertice(tempFriends.getVertice(j));
				}
			}
		}
		
		// move the people in the old checkList into visitedList
		for(int i = 0; i <= checkList.getLength(); i++){
			visitedList.addVertice(checkList.getVertice(i));
		}
		
		// update the old checkList with tempList 
		checkList = tempList;
		disconnectedDist ++;
		
		// find personTarg among the friends of the people in checkList
		for(int i = 1; i <= checkList.getLength(); i++){
			if(findInFriends(checkList.getVertice(i), personTarg)){
				found = true;
				break;
			}
		}
		
		if(!found){
			if(visitedList.getLength() >= map.size()){
				disconnectedDist = -1;
				return disconnectedDist;
			}else
				NKbfs(disconnectedDist, personTarg);
		}
		return disconnectedDist;
    }
    
    
    
    // to find if personStart is within personTarg's unvisited friend list
    public boolean findInFriends(T personStart, T personTarg){
    	
    	
    	// get personStart's friend list
    	NKList<T> friendsList = friends[map.get(personStart)];
    	
    	// iterate through each friend in the friendList to check if personTarg exists 
    	for(int i = 1; i <= friendsList.getLength(); i++){
    		// checked if it is visited
    		if(visitedList.getVertice(i) == null){
    			// added to the visited 
    			visitedList.addVertice(friendsList.getVertice(i));
        		
        		// check if it is the target
        		if(friendsList.getVertice(i)== personTarg){
        			return true;
        		}
    		}
    	}
    	return false;
    }
    
    
    // to find the specified person's friends list index from the map 
    private T findPersonFromIndex(int index){
    	for(Entry<T, Integer> entry: map.entrySet()){
    		if(index == entry.getValue()){
    			return entry.getKey();
    		}
    	}
    	return null;
    }
    
    // to find out whether it person is already in the map
    private boolean checkPersonInList(T person){
    	for(Entry<T, Integer> entry: map.entrySet()){
    		if(person == entry.getKey()){
    			return true;
    		}
    	}
    	return false;
    }
    
    // to remove a person from his/her friends' list completed
    private void removeVFromFriendsList(T targVertice){
    	// get his/her friends list index
    	int index = map.get(targVertice);
    	// get his/her friend list
    	NKList<T> VFriends = friends[index];
    	
    	// go through each person and remote the target from their friend lists
    	for(int i = 0; i < VFriends.getLength(); i++){
    		removeEdge1Side(VFriends.getVertice(i),targVertice);
    	}
    }
    
    
    public void removeEdge1Side(T srcLabel, T removedLabel) {
    	//??check if these two people exist and the edge existed? ?? Try-catch-throw??
    	if(!checkPersonInList(srcLabel)){
    		System.out.println("The first person does not exist! Please add the person first!");
    		return;
    	}
    	
    	if(!checkPersonInList(removedLabel)){
    		System.out.println("The last person does not exist! Please add the person first!");
    		return;
    	}
    	 
    	// remove the edge in screLabel's friend list, one side only
    	int index = map.get(srcLabel);
    	friends[index].deleteVertice(removedLabel);
    	
    } // end of removeEdges()
    
		
} // end of class AdjList