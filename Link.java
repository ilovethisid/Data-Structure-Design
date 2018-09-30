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
	int travelTime;
	int length;
	static ArrayList<Link> links=new ArrayList<Link>();
	static String filePath="C:\\Users\\sdj\\OneDrive\\내 파일\\진행중\\자구설";
	static File saveFile=new File(filePath+"\\LinkData.txt");
	
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
			
			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
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
			
			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
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
					getCurrentTrafficInfo(key, links.get(links.size()-1).id);
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
			
			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
			Document doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize();
			
			NodeList nList=doc.getElementsByTagName("row");
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;
			
			for(int temp = 0; temp < nList.getLength(); temp++) {
				nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					eElement = (Element) nNode;
					
					String st_node_nm=getTagValue("st_node_nm", eElement);
					String ed_node_nm=getTagValue("ed_node_nm", eElement);
					
					int searchSt=Point.search(st_node_nm);
					int searchEd=Point.search(ed_node_nm);
					
					// save point infos for start and end
					if(searchSt<0) {
						Point.points.add(new Point(st_node_nm));
						Point.points.get(Point.points.size()-1).out_link.add(links.get(links.size()-1));
						Point.points.get(Point.points.size()-1).getPointInfo(key, link_id);
						links.get(links.size()-1).st_node=Point.points.get(Point.points.size()-1);
					} else {
						Point.points.get(searchSt).out_link.add(links.get(links.size()-1));
						Point.points.get(searchSt).getPointInfo(key, link_id);
						links.get(links.size()-1).st_node=Point.points.get(searchSt);
					}
					
					if(searchEd<0) {
						Point.points.add(new Point(ed_node_nm));
						Point.points.get(Point.points.size()-1).in_link.add(links.get(links.size()-1));
						Point.points.get(Point.points.size()-1).getPointInfo(key, link_id);
						links.get(links.size()-1).ed_node=Point.points.get(Point.points.size()-1);
					} else {
						Point.points.get(searchEd).in_link.add(links.get(links.size()-1));
						Point.points.get(searchEd).getPointInfo(key, link_id);
						links.get(links.size()-1).ed_node=Point.points.get(searchEd);
					}
					
					// save link infos
					links.get(links.size()-1).name=getTagValue("road_name", eElement);
					links.get(links.size()-1).length=Integer.parseInt(getTagValue("map_dist", eElement));
				}
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
		
	static void getCurrentTrafficInfo(String key, String link_id) {
		try {
			String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/TrafficInfo/1/50/"+link_id;
			
			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
			Document doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize();
			
			NodeList nList=doc.getElementsByTagName("row");
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;
			
			for(int temp = 0; temp < nList.getLength(); temp++) {
				nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					eElement = (Element) nNode;
					
					Graph.avg_spd+=Integer.parseInt(getTagValue("prcs_spd", eElement));
					links.get(links.size()-1).travelTime=Integer.parseInt(getTagValue("prcs_trv_time", eElement));
				}	// for end
			}	// if end
		} catch (Exception e) {	
			e.printStackTrace();
		} // try~catch end
	}
	
	static void save() {
		try {
			FileWriter fw=new FileWriter(saveFile);
			BufferedWriter bw=new BufferedWriter(fw);
			
			for(int i=0;i<links.size();i++) {
				bw.write(links.get(i).id);
				bw.newLine();
				bw.write(Integer.toString(links.get(i).length));
				bw.newLine();
				bw.write(links.get(i).name);
				bw.newLine();
				bw.write(Integer.toString(links.get(i).travelTime));
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
}









