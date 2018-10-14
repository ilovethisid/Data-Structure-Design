import java.io.*;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

public class Link {
	String name;
	String id;
	Point st_node;
	Point ed_node;
	int length;
	double trv_time;
	static ArrayList<Link> links=new ArrayList<Link>();
	static String filePath="C:\\Users\\sdj\\OneDrive\\내 파일\\진행중\\자구설";
	static File LinkData=new File(filePath+"\\LinkData.txt");
	static File TrafficData=new File(filePath+"\\TrafficData.txt");
	
	Link() {
		
	}
	
	Link(String id) {
		this.id=id;
	}
	
	// get tag value from api
	private static String getTagValue(String tag, Element eElement) {
	    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    if(nValue == null) 
	        return null;
	    return nValue.getNodeValue();
	}
	
	static void getRoad_axis_cd(String key, String roadClassCode) {
		try {
			// parsing할 url 지정(API 키 포함해서)
			String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/RoadInfo/1/50/"+roadClassCode;
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("RoadInfo");
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;
			
			nList = doc.getElementsByTagName("row");
			
			for(int temp = 0; temp < nList.getLength(); temp++) {
				nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					eElement = (Element) nNode;
					getLink_id(key, getTagValue("axis_cd", eElement));
				}
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
	
	static void getLink_id(String key, String axis_cd) {
		try {
			// parsing할 url 지정(API 키 포함해서)
			String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/LinkWithLoad/1/50/"+axis_cd;
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize();
			
			NodeList nList=doc.getElementsByTagName("row");
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;
			
			for(int temp = 0; temp < nList.getLength(); temp++) {
				nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					eElement = (Element) nNode;
					
					links.add(new Link(getTagValue("link_id", eElement)));
					getLink_info(key, links.get(links.size()-1).id);
				}
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
	
	static void getLink_info(String key, String link_id) {
		try {
			// parsing할 url 지정(API 키 포함해서)
			String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/LinkInfo/1/50/"+link_id;
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize();
			
			NodeList nList=doc.getElementsByTagName("row");
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;
			
			for(int temp = 0; temp < nList.getLength(); temp++) {
				nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					eElement = (Element) nNode;
					
					// save link infos
					links.get(links.size()-1).name=getTagValue("road_name", eElement);
					links.get(links.size()-1).length=Integer.parseInt(getTagValue("map_dist", eElement));
					
					if(links.get(links.size()-1).name.equals("-")) {
						links.remove(links.size()-1);
						break;
					}
					
					// save point infos for start and end
					String st_node_nm=getTagValue("st_node_nm", eElement);
					String ed_node_nm=getTagValue("ed_node_nm", eElement);
					
					if(st_node_nm.equals("-")||ed_node_nm.equals("-")) {
						links.remove(links.size()-1);
						break;
					}
					
					int searchSt=Point.search(st_node_nm);
					int searchEd=Point.search(ed_node_nm);
					
					if(searchSt<0) {
						Point.points.add(new Point(st_node_nm));
						Point.points.get(Point.points.size()-1).index=Point.points.size()-1;
						Point.points.get(Point.points.size()-1).out_link.add(links.get(links.size()-1));
						Point.points.get(Point.points.size()-1).getPointInfo(key, link_id);
						links.get(links.size()-1).st_node=Point.points.get(Point.points.size()-1);
					} else {
						Point.points.get(searchSt).out_link.add(links.get(links.size()-1));
						Point.points.get(searchSt).index=searchSt;
						Point.points.get(searchSt).getPointInfo(key, link_id);
						links.get(links.size()-1).st_node=Point.points.get(searchSt);
					}
					
					if(searchEd<0) {
						Point.points.add(new Point(ed_node_nm));
						Point.points.get(Point.points.size()-1).index=Point.points.size()-1;
						Point.points.get(Point.points.size()-1).in_link.add(links.get(links.size()-1));
						Point.points.get(Point.points.size()-1).getPointInfo(key, link_id);
						links.get(links.size()-1).ed_node=Point.points.get(Point.points.size()-1);
					} else {
						Point.points.get(searchEd).in_link.add(links.get(links.size()-1));
						Point.points.get(searchEd).index=searchEd;
						Point.points.get(searchEd).getPointInfo(key, link_id);
						links.get(links.size()-1).ed_node=Point.points.get(searchEd);
					}
				}
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
	
	static void updateTrafficInfo(String key) {
		double sum_spd=0;
		
		for(int i=0;i<links.size();i++) {
			try {
				String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/TrafficInfo/1/50/"+links.get(i).id;
				
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(url);
				doc.getDocumentElement().normalize();
				
				NodeList nList=doc.getElementsByTagName("row");
				Node nNode = nList.item(0);
				Element eElement = (Element) nNode;
				
				
				nNode = nList.item(0);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					eElement = (Element) nNode;
					
					sum_spd+=Double.parseDouble(getTagValue("prcs_spd", eElement));
					links.get(i).trv_time=Double.parseDouble(getTagValue("prcs_trv_time", eElement));
					MainSystem.print(links.get(i).trv_time);
				}	// for end
			} catch (Exception e) {	
//					e.printStackTrace();
				// error
				links.get(i).trv_time=links.get(i).length/Graph.avg_spd;
			} // try~catch end
		}
			
		Graph.avg_spd=sum_spd/links.size();
	}
	
	static Link getLinkBetween(Point st_node, Point ed_node) {
		for(int i=0;i<st_node.out_link.size();i++) {
			if(st_node.out_link.get(i).ed_node==ed_node) {
				return st_node.out_link.get(i);
			}
		}
		
		return null;
	}
	
	static void saveLinkData() {
		try {
			FileWriter fw=new FileWriter(LinkData);
			BufferedWriter bw=new BufferedWriter(fw);
			
			bw.write("id/length/name/st_node.index/ed_node/index");
			bw.newLine();
			
			for(int i=0;i<links.size();i++) {
				bw.write(links.get(i).id);
				bw.newLine();
				bw.write(Integer.toString(links.get(i).length));
				bw.newLine();
				bw.write(links.get(i).name);
				bw.newLine();
				bw.write(Integer.toString(links.get(i).st_node.index));
				bw.newLine();
				bw.write(Integer.toString(links.get(i).ed_node.index));
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void saveTrafficData() {
		try {
			FileWriter fw=new FileWriter(TrafficData);
			BufferedWriter bw=new BufferedWriter(fw);
			
			// get average speed of links and save it in the first line
			bw.write("avg_spd");
			bw.newLine();
			bw.write(Double.toString(Graph.avg_spd));
			bw.newLine();
			
			bw.write("trv_time");
			bw.newLine();
			for(int i=0;i<links.size();i++) {
				bw.write(Double.toString(links.get(i).trv_time));
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}









