import java.io.File;
 public class Vertex {
	String name;
	int id;
	double x;
	double y;
	int in_degree;
	int out_degree;
	
	static Vertex[] allocVertex(int n) {
		Vertex[] vertex=new Vertex[n];
		for(int i=0;i<n;i++) {
			vertex[i]=new Vertex();
			vertex[i].name="";
		}
		return vertex;
	}
	
	static void appendVertex(String name, Link link, Vertex[] vertex, int n, RefVars refVars, String type) {
		int pos=searchVertex(name, vertex, n, refVars);
		File file=new File("C:\\Users\\caucse\\Desktop");
		
		if(pos<=0) {
			pos=-pos;
			
			if(name.charAt(0)!='(' && name.charAt(0)!='-') {
				vertex[pos].name=name;
				System.out.println(vertex[pos].name);
			}
			
			MainSystem.getLinkVertexInfo(Long.toString(link.id), vertex[pos], type);
			refVars.numOfVertices=pos+1;
		}
		
		if(type.equals("start")) {
			refVars.startVerIndex=pos;
		}
		else {
			refVars.endVerIndex=pos;
		}
	}
	
	static int searchVertex(String name, Vertex[] vertex, int n, RefVars refVars) {
		int i=0;
		
		for(i=0;i<refVars.numOfVertices;i++) {
			if(vertex[i].name.equals(name)) {
				return i;
			}
		}
		
		return -i;
	}
}