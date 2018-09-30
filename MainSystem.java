

public class MainSystem {
	
	static String key="474d5a6356696c6f36386a59757667";
	
	public static void main(String[] args) {
		
		long startTime=System.currentTimeMillis();
		
		print("////////////////////////////");
		updateData(key);
		Link.save();
		Point.save();
		
		long endTime=System.currentTimeMillis();
		
		int elapsed_time=(int)(endTime-startTime)/1000;
		
		print(elapsed_time+"s");
		
		test();
	} // main end
	
	private static void test() {
		for(int i=0;i<Link.links.size();i++) {
			print(Link.links.get(i).name);
			print(Link.links.get(i).id);
		}
		
		for(int i=0;i<Point.points.size();i++) {
			print(Point.points.get(i).name);
			print(Point.points.get(i).x);
			print(Point.points.get(i).y);
		}
		
		print(Point.points.get(50).out_link.get(0).name);
		print(Point.points.get(25).in_link.get(0).name);
	}

	
	private static void updateData(String key) {
		Link.getRoad_axis_cd(key, "02");
		// other functions are called continuously
	}
	
	static void print(String str) {
		System.out.println(str);
	}
	
	static void print(int n) {
		System.out.println(n);
	}
	
	static void print(double x) {
		System.out.println(x);
	}
	
	static void print(long x) {
		System.out.println(x);
	}
	
	private static void readData() {
		
	}
}	// class end