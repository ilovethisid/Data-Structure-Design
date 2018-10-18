public class LinkedList {
	Node head;
	
	LinkedList() {
		
	}
	
	public class Node {
		Point point;
		int FScore, GScore, HScore;
		Node parentNode;
		Node next;
		
		Node() {
			this.point=null;
			this.next=null;
			this.parentNode=null;
		}
		
		Node(Point point) {
			this.point=point;
			this.next=null;
			this.parentNode=null;
		}
		
		int getFScore(Point endP) {
			this.FScore=this.getGScore()+this.getHScore(endP);
			
			return this.FScore;
		}
		
		int getGScore() {
			Node cur;
			
			cur=this;
			
			this.GScore=this.parentNode.GScore+Link.getLinkBetween(cur.parentNode.point, cur.point).trv_time;
			// GScore is sum of all GScores of parent nodes,
			// so this.GScore = parent.GScore + GScore(this, parent)
			
			return this.GScore;
		}
		
		int getHScore(Point endP) {
			// get HScore
			// (estimated score left)
			
			double distance;
			
			distance=Math.sqrt(Math.pow(this.point.x-endP.x, 2) + Math.pow(this.point.y-endP.y, 2));
			// distance calculated by vertexs' coordinates
			
			this.HScore=(int)(distance/Graph.avg_spd);
			// HScore=distance/spd (=time)
			
			return this.HScore;
		}
	}
	
	void append(Node newNode) {
		Node cur;
		
		cur=this.head;
		
		try {
			while(cur.next!=null) {
				cur=cur.next;
			}
		} catch(NullPointerException e) {
			
		}
		
		cur.next=newNode;
		newNode.next=null;
	}
	
	void delete(Node node) {
		Node cur;
		Node temp;
		
		cur=this.head;
		
		try {
			while(cur.next!=node&&cur!=null) {
				cur=cur.next;
			}
		} catch(NullPointerException e) {
			
		}
		
		temp=cur.next.next;
		cur.next=null;
		cur.next=temp;
	}
	
	Node search(Node node) {
		Node cur;
		
		cur=this.head;
		
		try {
			while(cur!=null) {
				if(cur==node) {
					return cur;
				}
				
				cur=cur.next;
			}
		} catch(NullPointerException e) {
			
		}
		
		return null;
	}
	
	Node search(Point point) {
		Node cur;
		
		cur=this.head;
		
		while(cur!=null) {
			if(cur.point==point) {
				return cur;
			}
			
			cur=cur.next;
		}
		
		return null;
	}
	
	void print() {
		Node cur;
		int seconds;
		int minutes;
		
		cur=this.head;
		
		try {
			while(cur.next!=null) {
				if(cur.parentNode!=null) {
					System.out.println("->"+Link.getLinkBetween(cur.parentNode.point, cur.point).name+"->");
					// print links
				}
				System.out.println(cur.point.name);
				// print points
				
				cur=cur.next;
			}
			
			System.out.println("->"+Link.getLinkBetween(cur.parentNode.point, cur.point).name+"->");
			System.out.println(cur.point.name);
			// print destination link and point
			
			seconds=(int)(cur.FScore%60);
			minutes=(int)(cur.FScore/60);
			// calculate estimated trv_time
			
			System.out.println("This route takes "+minutes+" minutes and "+seconds+" seconds");
			// print estimated trv_time
			
			System.out.println("");
		} catch(NullPointerException e) {
			
		}
	}
}















