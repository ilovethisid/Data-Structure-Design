public class LinkedList {
	Node head;
	
	LinkedList() {
		
	}
	
	public class Node {
		Point point;
		double FScore, GScore, HScore = 0;
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
		
		double getFScore(Point endP) {
			this.FScore=this.getGScore()+this.getHScore(endP);
			
			return this.FScore;
		}
		
		double getGScore() {
			Node cur;
			double GScore=0;
			
			cur=this;
			
			try {
				while(cur.parentNode!=null) {
					Link linkBetween=Link.getLinkBetween(cur.parentNode.point, cur.point);
					GScore+=linkBetween.trv_time;
					
					cur=cur.parentNode;
				}
			} catch(NullPointerException e) {
				
			}
			
			this.GScore=GScore;
			
			return this.GScore;
		}
		
		double getHScore(Point endP) {
			for(int i=0;i<this.point.out_link.size();i++) {
				if(this.point.out_link.get(i).ed_node==endP) {
					Link linkBetween=Link.getLinkBetween(this.parentNode.point, this.point);
					return linkBetween.trv_time;
				}
			}
			
			double distance;
			
			distance=Math.sqrt(Math.pow(this.point.x-endP.x, 2) + Math.pow(this.point.y-endP.y, 2));
			
			this.HScore=distance/Graph.avg_spd;
			
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
			while(cur.next!=node||cur==null) {
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
		
		cur=this.head;
		
		try {
			while(cur!=null) {
				if(cur.parentNode!=null) {
					System.out.println("->"+Link.getLinkBetween(cur.parentNode.point, cur.point).name+"->");
				}
				System.out.println(cur.point.name);
				
				cur=cur.next;
			}
			
			System.out.println("////////////////////");
		} catch(NullPointerException e) {
			
		}
	}
}















