
public class Graph {
	static double avg_spd;
	
	Graph() {
		
	}
	
	LinkedList getPath(Point startP, Point endP) {
		LinkedList openList=new LinkedList();
		LinkedList closedList=new LinkedList();
		LinkedList path=new LinkedList();
		
		LinkedList objList=new LinkedList();
		
		LinkedList.Node start=objList.new Node(startP);
		LinkedList.Node dest;
		// start and destination
		
		LinkedList.Node fastest_node;
		LinkedList.Node temp;
		
		closedList.head=closedList.new Node();
		closedList.head.next=start;
		// save start in closed list
		
		openList.head=openList.new Node();
		
		while((temp=closedList.search(endP))==null) {
			// loop until path is found
			
			addOpenList(openList, closedList);
			fastest_node=getFastestNode(openList, endP);
			// get fastest_node in open list
			
			if(fastest_node==null) {
				break;
			}
			// if there is no node in openList, terminate

			openList.delete(fastest_node);
			// remove fastest_node from openList
			closedList.append(fastest_node);
			// and append in closedList
		}
		
		dest=temp;
		// save destination node
		
		LinkedList.Node cur=dest;
		
		if(cur==null) {
			return null;
		}
		// cannot find a path
		
		while(cur.parentNode!=null) {
			cur.parentNode.next=cur;
			cur=cur.parentNode;
		}
		// rearranging the linked list
		// (dest -> start to start -> dest)
		
		path.head=cur;
		// save start_node in head of linked list path
		
		return path;
	}
	
	static void addOpenList(LinkedList openList, LinkedList closedList) {
		LinkedList.Node cur;
		
		cur=closedList.head.next;
		
		Point nearbyPoint;
		
		try {
			while(cur!=null) {
				for(int i=0;i<cur.point.out_link.size();i++) {
					// for all the out_links of current point
					
					nearbyPoint=cur.point.out_link.get(i).ed_node;
					// find nearbyPoint
					
					if(closedList.search(nearbyPoint)==null && openList.search(nearbyPoint)==null) {
						// if nearby point is not on the closedList and openList
						
						LinkedList.Node openList_newNode=openList.new Node(nearbyPoint);
						openList_newNode.parentNode=cur;
						// save parentNode
						openList.append(openList_newNode);
						// append new node to openList
					}
				}
				
				cur=cur.next;
			}
		} catch(NullPointerException e) {
			
		}
	}
	
	static LinkedList.Node getFastestNode(LinkedList openList, Point endP) {
		LinkedList.Node cur;
		LinkedList.Node fastest;
		
		cur=openList.head.next;
		fastest=cur;
		
		if(fastest==null) {
			return null;
		}
		// openList is empty
		
		if(fastest.next==null) {
			fastest.getFScore(endP);
			return fastest;
		}
		// openList has one element
		
		double fastestTrvTime=fastest.getFScore(endP);
		double temp;
		
		cur=cur.next;
		
		try {
			while(cur!=null) {
				if((temp=cur.getFScore(endP))<fastestTrvTime) {
					// if current node is faster
					fastest=cur;
					fastestTrvTime=temp;
				}
				
				cur=cur.next;
			}
		} catch(NullPointerException e) {
			
		}
		
		return fastest;
	}
}












