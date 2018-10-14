
public class Graph {
	static double avg_spd;
	LinkedList.Node start;
	
	Graph() {
		
	}
	
	LinkedList getPath(Point startP, Point endP) {
		LinkedList openList=new LinkedList();
		LinkedList closedList=new LinkedList();
		LinkedList path=new LinkedList();
		
		LinkedList objList=new LinkedList();
		LinkedList.Node start=objList.new Node(startP);
		LinkedList.Node fastest_node;
		LinkedList.Node dest;
		LinkedList.Node temp;
		
		closedList.head=closedList.new Node();
		closedList.head.next=start;
		
		openList.head=openList.new Node();
		
		while((temp=closedList.search(endP))==null) {
			addOpenList(openList, closedList);
			fastest_node=getFastestNode(openList, endP);
			if(fastest_node==null) {
				break;
			}
			closedList.append(fastest_node);
			openList.delete(fastest_node);
		}
		
		dest=temp;
		
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
		// cur is start node
		
		return path;
	}
	
	static void addOpenList(LinkedList openList, LinkedList closedList) {
		LinkedList newList=new LinkedList();
		newList.head=newList.new Node();
		LinkedList.Node cur;
		
		cur=closedList.head.next;
		
		Point nearbyPoint;
		
		try {
			while(cur!=null) {
				for(int i=0;i<cur.point.out_link.size();i++) {
					nearbyPoint=cur.point.out_link.get(i).ed_node;
					
					if(closedList.search(nearbyPoint)==null && openList.search(nearbyPoint)==null) {
						// if nearby point is not on the closed list
						
						LinkedList.Node openList_newNode=openList.new Node(nearbyPoint);
						openList_newNode.parentNode=cur;
						openList.append(openList_newNode);
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
		
		double fastestTrvTime=fastest.getFScore(endP);
		double temp;
		
		try {
			while(cur!=null) {
				if((temp=cur.getFScore(endP))<fastestTrvTime) {
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












