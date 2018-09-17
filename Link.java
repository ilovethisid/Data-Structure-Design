
public class Link {
	String name;
	long id;
	String st_node_nm;
	String ed_node_nm;
	int travelTime;
	
	static Link[] allocateLink(int n) {
		Link[] link=new Link[n];
		for(int i=0;i<n;i++) {
			link[i]=new Link();
		}
		return link;
	}
}
