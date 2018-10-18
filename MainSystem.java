import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;

public class MainSystem {
	
	static String key="474d5a6356696c6f36386a59757667";
	// key for road APIs
	
	public static void main(String[] args) {
		// main
		
		System.out.println("NaviNavi");
		
		// get data from text file
		readPointData();
		readLinkData();
		readTrafficData();
		
		System.out.println("Do you wish to update road data?(It takes a lot of time)");
		System.out.println("Yes: 1, No: 0");
		
		Scanner scanner=new Scanner(System.in);
		
		int answer=scanner.nextInt();
		
		if(answer==1)
			updateRoadData();
		// in case of updating data
		
		System.out.println("Do you want to view sample?");
		System.out.println("Yes: 1, No: 0");
		
		answer=scanner.nextInt();
		
		if(answer==1)
			test();
		// in case of viewing sample
		
		// navigation start
		navigation_start();
		
		scanner.close();
		
	} // main end
	
	
	
	private static void navigation_start() {
		
		Scanner scanner=new Scanner(System.in);
		
		while(true) {
			Graph graph=new Graph();
			int startIndex, endIndex;
			
			System.out.println("Do you wish to update traffic data?(It takes a lot of time)");
			System.out.println("Yes: 1, No: 0");
			
			int answer=scanner.nextInt();
			
			if(answer==1)
				updateTrafficData();
			
			System.out.println("Enter start node's index: ");
			startIndex=scanner.nextInt();
			System.out.println("Enter end node's index: ");
			endIndex=scanner.nextInt();
			
			long startTime=System.currentTimeMillis();
			
			LinkedList temp=graph.getPath(Point.points.get(startIndex), Point.points.get(endIndex));
			
			if(temp!=null) {
				temp.print();
				// display path
			}
			else {
				System.out.println("Cannot find a path");
				// if there is no path
			}
			System.out.println("---------------------------");
			
			long endTime=System.currentTimeMillis();
			
			int elapsed_time=(int)(endTime-startTime)/1000;
			
			System.out.println("Elapsed Time: "+elapsed_time+"s");
			// print out elapsed time
		}
	}
	
	private static void test() {
		long startTime=System.currentTimeMillis();
		
		Graph sample=new Graph();
		int temp;
		
		Point start=Point.points.get(30);
		Point end=Point.points.get(60);
		// point[30] => point[60]
		// sample route(can test other routes as well)
		
		System.out.println("Start at: "+start.name);
		System.out.println("Destination: "+end.name);
		// print start location and destination
		
		
		sample.getPath(start, end).print();
		// get path
		
		
		System.out.println("Now we change trv_time to test if the path changes.");	
		
		Link sampleLink=Link.getLinkBetween(Point.points.get(27), Point.points.get(212));
		temp=sampleLink.trv_time;
		sampleLink.trv_time=3000;
		// increase trv_time of one of the link in the path
		// can choose which link to increase trv_time
		
		System.out.println("Start at: "+start.name);
		System.out.println("Destination: "+end.name);
		
		sample.getPath(Point.points.get(30), Point.points.get(60)).print();
		
		sampleLink.trv_time=temp;
		
		long endTime=System.currentTimeMillis();
		
		int elapsed_time=(int)(endTime-startTime)/1000;
		
		System.out.println("Elapsed Time: "+elapsed_time+"s");
		System.out.println("Test complete!");
		System.out.println("");
	}
	
	
	private static void updateRoadData() {
		updateRoadData(key);
		Link.saveLinkData();
		Point.save();
	}
	
	private static void updateTrafficData() {
		Link.updateTrafficInfo(key);
		Link.saveTrafficData();
	}
	

	private static void updateRoadData(String key) {
		Link.links.clear();
		Link.getRoad_axis_cd(key, "03");
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
			// read point data
			
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
				// save link id, length, name
				
				Link.links.get(i).st_node=Point.points.get(Integer.parseInt(br.readLine()));
				Link.links.get(i).st_node.out_link.add(Link.links.get(i));
				Link.links.get(i).ed_node=Point.points.get(Integer.parseInt(br.readLine()));
				Link.links.get(i).ed_node.in_link.add(Link.links.get(i));
				// save st_node and ed_node
				// and in_links and out_links as well
				
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
			Graph.avg_spd=Double.parseDouble(br.readLine());
			// avg_spd
			br.readLine();
			// description about traffic data
			
			while(true) {
				if((s=br.readLine())==null)
						break;
				Link.links.get(i).trv_time=(int)Double.parseDouble(s);
				
				i++;
			}
			// read link trv_time
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}	// class end