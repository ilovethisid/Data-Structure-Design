
import java.awt.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RoadAPI {
	
	static String key="474d5a6356696c6f36386a59757667";
	static RefVars refVars=new RefVars();
	
    // get tag value from api
	private static String getTagValue(String tag, Element eElement) {
	    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    if(nValue == null) 
	        return null;
	    return nValue.getNodeValue();
	}
	
	
	public static void main(String[] args) {
		Information info=new Information();
		
		long start=System.currentTimeMillis();
		
		print("Getting basic road infos...");
		getRoadClassInfo(info);
		getRoadInfo(info, "02");
		
		print("Counting number of roads...");
		for(int i=0;i<info.num_road_axis_cd;i++) {
			countLink(info, info.road_axis_code[i]);
		}
		print(info.num_link_id);
		
		Link[] link=Link.allocateLink(info.num_link_id);
		Vertex[] vertex=Vertex.allocVertex(info.num_link_id+1);
		DirectedGraphMatrix matrix=new DirectedGraphMatrix();
		
		refVars.count=0;
		
		print("Getting road ids...");
		for(int i=0;i<info.num_road_axis_cd;i++) {
			getRoadLinkInfo(info, info.road_axis_code[i], link);
		}
		
		
		print("Saving road infos...");
		for(int i=0;i<info.num_link_id;i++) {
			getLinkInfo(Long.toString(link[i].id), link, vertex, i, info);
		}
		
		getCurrentTrafficInfo("1030001300");
		
		print(info.num_road_div_cd);
		print(info.num_road_axis_cd);
		print(link[3].id);
		print(link[3].name);
		print(vertex[0].name);
		print(vertex[3].x);
		
		long end=System.currentTimeMillis();
		
		int elapsed_time=(int)(end-start)/1000;
		
		print(elapsed_time);
	} // main end
	
	
	static void countNodes() {
		Information info=new Information();
		
	}
	
	
	static void getRoadClassInfo(Information info) {
		int page = 1;	// 페이지 초기값 
		try {
			while(true) {
				// parsing할 url 지정(API 키 포함해서)
				String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/RoadDivInfo/1/10/";
				
				DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
				Document doc = dBuilder.parse(url);
				
				// root tag 
				doc.getDocumentElement().normalize();
				
				
				NodeList nList = doc.getElementsByTagName("RoadDivInfo");
				Node nNode = nList.item(0);
				Element eElement = (Element) nNode;
				
				info.num_road_div_cd=Integer.parseInt(getTagValue("list_total_count",eElement));
				info.allocate_road_div_cd();
				
				// 파싱할 tag
				nList = doc.getElementsByTagName("row");
				
				for(int temp = 0; temp < nList.getLength(); temp++) {
					nNode = nList.item(temp);
					if(nNode.getNodeType() == Node.ELEMENT_NODE) {
						
						eElement = (Element) nNode;
						info.road_div_code[temp]=getTagValue("road_div_cd", eElement);
					}	// for end
				}	// if end
				
				page += 1;
				
				if(page > 1){	
					break;
				}
			}	// while end
		} catch (Exception e) {	
			e.printStackTrace();
		} // try~catch end
	}
	
	static void getRoadInfo(Information info,String roadClassCode) {
		int page = 1;	// 페이지 초기값 
		try {
			while(true) {
				// parsing할 url 지정(API 키 포함해서)
				String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/RoadInfo/1/50/"+roadClassCode;
				
				DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
				Document doc = dBuilder.parse(url);
				
				// root tag 
				doc.getDocumentElement().normalize();
				
				
				NodeList nList = doc.getElementsByTagName("RoadInfo");
				Node nNode = nList.item(0);
				Element eElement = (Element) nNode;
				
				info.num_road_axis_cd=Integer.parseInt(getTagValue("list_total_count",eElement));
				info.allocate_road_axis_cd();
				
				// 파싱할 tag
				nList = doc.getElementsByTagName("row");
				
				for(int temp = 0; temp < nList.getLength(); temp++) {
					nNode = nList.item(temp);
					if(nNode.getNodeType() == Node.ELEMENT_NODE) {
						
						eElement = (Element) nNode;
						info.road_axis_code[temp]=getTagValue("axis_cd", eElement);
					}	// for end
				}	// if end
				
				page += 1;
				
				if(page > 1){	
					break;
				}
			}	// while end
		} catch (Exception e) {	
			e.printStackTrace();
		} // try~catch end
	}
	
	static void countLink(Information info, String axis_cd) {
		int page = 1;	// 페이지 초기값 
		try {
				String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/LinkWithLoad/1/50/"+axis_cd;
				
				DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
				Document doc = dBuilder.parse(url);
				
				// root tag 
				doc.getDocumentElement().normalize();
				
				// 파싱할 tag
				NodeList nList = doc.getElementsByTagName("LinkWithLoad");
				Node nNode = nList.item(0);
				Element eElement = (Element) nNode;
				
				info.num_link_id+=Integer.parseInt(getTagValue("list_total_count",eElement));
				
		} catch (Exception e) {	
			e.printStackTrace();
		} // try~catch end
	}
	
	static void getRoadLinkInfo(Information info, String axis_cd, Link[] link) {
		int page = 1;	// 페이지 초기값 
		try {
			while(page<2) {
				// parsing할 url 지정(API 키 포함해서)
				String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/LinkWithLoad/1/50/"+axis_cd;
				
				DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
				Document doc = dBuilder.parse(url);
				
				// root tag 
				doc.getDocumentElement().normalize();
				
				NodeList nList=doc.getElementsByTagName("row");
				Node nNode = nList.item(0);
				Element eElement = (Element) nNode;
				
				for(int temp = 0; temp < nList.getLength(); temp++) {
					nNode = nList.item(temp);
					if(nNode.getNodeType() == Node.ELEMENT_NODE) {
						
						eElement = (Element) nNode;
						link[refVars.count].id=Long.parseLong(getTagValue("link_id", eElement));
						refVars.count++;
					}	// for end
				}	// if end
				
				page += 1;
			}	// while end
		} catch (Exception e) {	
			e.printStackTrace();
		} // try~catch end
	}
	
	static void getLinkInfo(String link_id, Link[] link, Vertex[] vertex, int n, Information info) {
		int page = 1;	// 페이지 초기값 
		try {
			while(true) {
				// parsing할 url 지정(API 키 포함해서)
				String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/LinkInfo/1/50/"+link_id;
				
				DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
				Document doc = dBuilder.parse(url);
				
				// root tag 
				doc.getDocumentElement().normalize();
				
				
				// 파싱할 tag
				NodeList nList = doc.getElementsByTagName("row");
				refVars.count=0;
				
				for(int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if(nNode.getNodeType() == Node.ELEMENT_NODE) {
						
						Element eElement = (Element) nNode;
						link[n].name=getTagValue("road_name", eElement);
						
						link[n].st_node_nm=getTagValue("st_node_nm", eElement);
						//Vertex.appendVertex(link[n].st_node_nm, link[n], vertex, info.num_link_id+1, refVars, "start");
						
						link[n].ed_node_nm=getTagValue("ed_node_nm", eElement);
						//Vertex.appendVertex(link[n].ed_node_nm, link[n], vertex, info.num_link_id+1, refVars, "end");
						
						print(getTagValue("reg_cd",eElement));
						
//						System.out.println("지도 거리  : " + getTagValue("map_dist", eElement));
//						System.out.println("권역코드  : " + getTagValue("reg_cd", eElement));
						
					}	// for end
				}	// if end
				
				page += 1;
				
				if(page > 1){	
					break;
				}
			}	// while end
		} catch (Exception e) {	
			e.printStackTrace();
		} // try~catch end
	}
	
	static void getLinkVertexInfo(String link_id, Vertex vertex, String type) {
		int page = 1;	// 페이지 초기값 
		try {
			// parsing할 url 지정(API 키 포함해서)
			String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/LinkVerInfo/1/50/"+link_id;
			
			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
			Document doc = dBuilder.parse(url);
			
			// root tag 
			doc.getDocumentElement().normalize();
			
			
			// 파싱할 tag
			NodeList nList = doc.getElementsByTagName("row");
			//System.out.println("파싱할 리스트 수 : "+ nList.getLength());
			
			Node nNode;
			
			if(type.equals("start")) {
				nNode = nList.item(0);
			}
			else {
				nNode = nList.item(nList.getLength()-1);
			}
			
			Element eElement = (Element) nNode;
			
			vertex.x=Double.parseDouble(getTagValue("grs80tm_x", eElement));
			vertex.y=Double.parseDouble(getTagValue("grs80tm_y", eElement));
			
		} catch (Exception e) {	
			e.printStackTrace();
		} // try~catch end
	}
	
	static void getCurrentTrafficInfo(String link_id) {
		int page = 1;	// 페이지 초기값 
		try {
			while(true) {
				// parsing할 url 지정(API 키 포함해서)
				String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/TrafficInfo/1/50/"+link_id;
				
				DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
				Document doc = dBuilder.parse(url);
				
				// root tag 
				doc.getDocumentElement().normalize();
				
				
				// 파싱할 tag
				NodeList nList = doc.getElementsByTagName("row");
				//System.out.println("파싱할 리스트 수 : "+ nList.getLength());
				
				for(int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if(nNode.getNodeType() == Node.ELEMENT_NODE) {
						
						Element eElement = (Element) nNode;
						print("######################");
						//print(eElement.getTextContent());
						print("속도  : " + getTagValue("prcs_spd", eElement));
						print("여행 시간  : " + getTagValue("prcs_trv_time", eElement));
					}	// for end
				}	// if end
				
				page += 1;
				
				if(page > 1){	
					break;
				}
			}	// while end
		} catch (Exception e) {	
			e.printStackTrace();
		} // try~catch end
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
}	// class end










