import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;

public class MainSystem {
	
	static String key="474d5a6356696c6f36386a59757667";
	
	public static void main(String[] args) {
		
		// System setup
		
		print("NaviNavi");
		readPointData();
		readLinkData();
		readTrafficData();
		
//		updateRoadData(key);
//		Link.save();
//		Point.save();
		
//		Link.updateTrafficInfo(key);
//		Link.saveTrafficData();
		
		test();
		
		Point.print();
		
		// navigation start
		navigation_start();
	} // main end
	
	private static void navigation_start() {
		while(true) {
			Graph graph=new Graph();
			
			Scanner scanner=new Scanner(System.in);
			
			print("Enter start node's index: ");
			int start=scanner.nextInt();
			print("Enter end node's index: ");
			int end=scanner.nextInt();
			
			long startTime=System.currentTimeMillis();
			
			LinkedList temp=graph.getPath(Point.points.get(start), Point.points.get(end));
			
			if(temp!=null) {
				temp.print();
				// display path
			}
			else {
				print("Cannot find a path");
			}
			print("---------------------------");
			
			long endTime=System.currentTimeMillis();
			
			int elapsed_time=(int)(endTime-startTime)/1000;
			
			print("Time: "+elapsed_time+"s");
		}
	}
	
	private static void test() {
		long startTime=System.currentTimeMillis();
		
		Graph sample=new Graph();
		
		for(int i=50;i<100;i++) {
			LinkedList temp=sample.getPath(Point.points.get(0), Point.points.get(i));
		
			if(temp!=null) {
				temp.print();
			}
			else {
				print("Cannot find a path");
			}
			print("---------------------------");
		}
		
		long endTime=System.currentTimeMillis();
		
		int elapsed_time=(int)(endTime-startTime)/1000;
		
		print("Time: "+elapsed_time+"s");
		print("test complete!");
		print("///////////////////////////////////////////");
	}

	private static void updateRoadData(String key) {
		Link.links.clear();
		Link.getRoad_axis_cd(key, "02");
		// other functions are called continuously
	}
	
	private static void readPointData() {
		String s;
		
		try {
			FileReader fr=new FileReader(Point.PointData);
			BufferedReader br=new BufferedReader(fr);
			
			int i=0;
			
			br.readLine();
			// description about data
			
			while((s=br.readLine()) != null) {
				Point.points.add(new Point());
				
				Point.points.get(i).name=s;
				Point.points.get(i).x=Double.parseDouble(br.readLine());
				Point.points.get(i).y=Double.parseDouble(br.readLine());
				Point.points.get(i).index=i;
				
				i++;
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void readLinkData() {
		String s;
		
		try {
			FileReader fr=new FileReader(Link.LinkData);
			BufferedReader br=new BufferedReader(fr);
			
			int i=0;
			
			br.readLine();
			// description about data
			
			while((s=br.readLine()) != null) {
				Link.links.add(new Link());
				
				Link.links.get(i).id=s;
				Link.links.get(i).length=Integer.parseInt(br.readLine());
				Link.links.get(i).name=br.readLine();
				Link.links.get(i).st_node=Point.points.get(Integer.parseInt(br.readLine()));
				Link.links.get(i).st_node.out_link.add(Link.links.get(i));
				Link.links.get(i).ed_node=Point.points.get(Integer.parseInt(br.readLine()));
				Link.links.get(i).ed_node.in_link.add(Link.links.get(i));
				
				i++;
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void readTrafficData() {
		String s;
		
		try {
			FileReader fr=new FileReader(Link.TrafficData);
			BufferedReader br=new BufferedReader(fr);
			
			int i=0;
			
			br.readLine();
			// description about avg_spd
			br.readLine();
			// avg_spd
			br.readLine();
			// description about traffic data
			
			while((s=br.readLine()) != null) {
				Link.links.add(new Link());
				
				Link.links.get(i).trv_time=Double.parseDouble(br.readLine());
				
				i++;
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}	// class end