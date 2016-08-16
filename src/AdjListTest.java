
public class AdjListTest<T extends Object> {
	
	
	public static void main(String Args[]){
		AdjList<String> adjList1 = (AdjList<String>)new AdjList();//?? why it says "Class cast exception" 
		
		String s1 = new String("S");
		String s2 = new String("A");
		String s3 = new String("P");
		String s4 = new String("M");
		String s5 = new String("B");
		String s6 = new String("H");
		
		adjList1.addVertex(s1);
		adjList1.addVertex(s2);
		adjList1.addVertex(s3);
		adjList1.addVertex(s4);
		adjList1.addVertex(s5);
		adjList1.addVertex(s6);

		adjList1.addEdge(s1, s2);
		adjList1.addEdge(s1, s3);
		adjList1.addEdge(s1, s4);
		adjList1.addEdge(s2, s5);
		adjList1.addEdge(s2, s6);
		adjList1.addEdge(s3, s6);
		
		adjList1.printVertices();
		adjList1.printEdges();
	}
}
