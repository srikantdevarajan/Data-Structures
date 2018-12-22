package friends;

import structures.Queue;
import structures.Stack;


import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Friends {
	
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner sc = new Scanner(new File("names.txt"));
		
		Graph test = new Graph(sc);
		
		System.out.println(shortestChain(test, "kaitlin", "nick"));
	}
	
	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		boolean[] visited = new boolean[g.members.length];
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Edge> editedEdges = new ArrayList<Edge>();
		ArrayList<String> finalNames = new ArrayList<String>();
		//make the queue
		Queue namesQueue = new Queue();
		//find the place where p1 is
		int firstNumber = g.map.get(p1);
		visited[firstNumber] = true;
		namesQueue.enqueue(firstNumber);
		/** ]COMPLETE THIS METHOD **/
		while(namesQueue.isEmpty()==false){
			int dequedNumber = (Integer) namesQueue.dequeue();
			//System.out.println(dequedNumber);
			if(p2.equals(g.members[dequedNumber].name)){
				break;
			}
			//enqueue neighbors
			
			for(Friend x = g.members[dequedNumber].first; x != null; x = x.next){
				if(visited[x.fnum]==false){
					Edge newEdge = new Edge(dequedNumber, x.fnum);
					edges.add(newEdge);
					visited[x.fnum]=true;
					namesQueue.enqueue(x.fnum);
				}
				
				
			}
		}
		for(int i = 0; i < visited.length;i++){
			//System.out.print(i + ""+ visited[i] + ", ");
		}
		
		//System.out.println();
	
		String newNametoGet= null;
		for(Edge e : edges){
			if(g.members[e.v2].name.equalsIgnoreCase(p2)){
				editedEdges.add(e);
				newNametoGet = g.members[e.v1].name;
			}
		}
		for(int i = 0; i < edges.size();i++){
			for(Edge innerLoop : edges){
				if(g.members[innerLoop.v2].name.equalsIgnoreCase(newNametoGet)){
					editedEdges.add(innerLoop);
					newNametoGet = g.members[innerLoop.v1].name;
					}
			}
			
		}
		Collections.reverse(editedEdges);
		for(int i = 0; i < editedEdges.size();i++){
			if(i!= editedEdges.size()-1){
				finalNames.add(g.members[editedEdges.get(i).v1].name);
			}else{
				finalNames.add(g.members[editedEdges.get(i).v1].name);
				finalNames.add(g.members[editedEdges.get(i).v2].name);
			}
		}
		
		
		return finalNames;
	}
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		ArrayList<ArrayList<String>> masterList = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		
		
		for(int v= 0; v < visited.length;v++){
			ArrayList<String> smallList = new ArrayList<String>();
			if(g.members[v].school!=null){
				if(g.members[v].school.equals(school) && visited[v]==false){
					smallList = dfs(visited, g, v, school, smallList);
				}
			}
			if(smallList.size()!=0){
				masterList.add(smallList);
			}
		}
	
		return masterList;
		
		
	}
		
	private static ArrayList<String> dfs(boolean[] visited, Graph g, int indexOfNode, String schoolName, ArrayList<String> smallList){
		visited[indexOfNode]=true;
		smallList.add(g.members[indexOfNode].name);
		for(Friend currentFriend = g.members[indexOfNode].first; currentFriend!=null; currentFriend = currentFriend.next){
			if(g.members[currentFriend.fnum].school!=null){
				if(visited[currentFriend.fnum]==false && g.members[currentFriend.fnum].school.equals(schoolName)){
					smallList = dfs(visited, g, currentFriend.fnum, schoolName, smallList);
				}

			}
		}
		
		return smallList;
	}
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */

	
	public static ArrayList<String> connectors(Graph g) {
		ArrayList<ArrayList<String>> finalList = new ArrayList<ArrayList<String>>();
		ArrayList<String> master = new ArrayList<String>();
		boolean[] visited = new boolean[g.members.length];
		int[] dfsNumbers = new int[g.members.length];
		int[] back = new int[g.members.length];
		boolean[] ifOneIsConnector = new boolean[g.members.length];
		int startingAmount = 1;
		for(int v= 0; v < visited.length;v++){
			ArrayList<String> temp = new ArrayList<String>();
			finalList.add(dfsConnector(g,visited,v,startingAmount,dfsNumbers,back, temp,ifOneIsConnector));
		}
		for(ArrayList<String> list: finalList){
			for(String s : list){
				if(master.contains(s)==false){
					master.add(s);
				}
			}
		}
		return master;
		
	}
	
	
	private static ArrayList<String> dfsConnector(Graph g, boolean[] visited, int indexOfPerson, int startingAmount, int[] dfsNumbers, int[] back, ArrayList<String> list, boolean[] gotfucked){
		if(visited[indexOfPerson]==false){
			//set it to true
			visited[indexOfPerson]=true;
			dfsNumbers[indexOfPerson] = startingAmount;
			back[indexOfPerson]= startingAmount;
		}
		
		for(Friend currentFriend = g.members[indexOfPerson].first; currentFriend!=null; currentFriend = currentFriend.next){
			
		}
		for(Friend friendAt = g.members[indexOfPerson].first; friendAt!=null; friendAt = friendAt.next){
			if(visited[friendAt.fnum]==true){
				back[indexOfPerson] = Math.min(back[indexOfPerson], dfsNumbers[friendAt.fnum]);
				
			}
			else if(visited[friendAt.fnum]==false){
				if(visited[friendAt.fnum]==false){
					
					//call dfs here
					list = dfsConnector(g, visited, friendAt.fnum, startingAmount = startingAmount+1, dfsNumbers,back,list, gotfucked);
				}
				if(dfsNumbers[indexOfPerson] > back[friendAt.fnum]){
					back[indexOfPerson] = Math.min(back[indexOfPerson], back[friendAt.fnum]);
					
				}
				
				if(dfsNumbers[indexOfPerson] <= back[friendAt.fnum] && (dfsNumbers[indexOfPerson]!=1)){
					list.add(g.members[indexOfPerson].name);
				}
				if(dfsNumbers[indexOfPerson]<= back[friendAt.fnum] && (dfsNumbers[indexOfPerson]==1)){
					if(gotfucked[indexOfPerson]==false){
						gotfucked[indexOfPerson]=true;
						
					}else if(gotfucked[indexOfPerson]==true){
						list.add(g.members[indexOfPerson].name);
					} 
				}
			}
			
			
			//if friend is not visited
		}
		return list;
	}
}

