import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Point {
	String name;
	double x;
	double y;
	ArrayList<Link> in_link=new ArrayList<Link>();
	ArrayList<Link> out_link=new ArrayList<Link>();
	static ArrayList<Point> points=new ArrayList<Point>();
	int index;
	static String filePath="C:\\Users\\sdj\\OneDrive\\내 파일\\진행중\\자구설";
	static File saveFile=new File(filePath+"\\PointData.txt");
	
	Point(String name) {
		this.name=name;
	}
	
	static int search(String name) {
		for(int i=0;i<points.size();i++) {
			if(points.get(i).name.equals(name)) {
				return i;
			}
		}
		
		return -1;
	}
	
	// get tag value from api
	private static String getTagValue(String tag, Element eElement) {
	    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    if(nValue == null) 
	        return null;
	    return nValue.getNodeValue();
	}
	
	void getPointInfo(String key, String link_id) {
		try {
			// parsing할 url 지정(API 키 포함해서)
			String url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/LinkVerInfo/1/50/"+link_id;
			
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
					
					if(temp==0) {
						this.x=Double.parseDouble(getTagValue("grs80tm_x", eElement));
						this.y=Double.parseDouble(getTagValue("grs80tm_y", eElement));
					}
				}
			}
			
			this.index=points.size()-1;
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
	
	static void save() {
		try {
			FileWriter fw=new FileWriter(saveFile);
			BufferedWriter bw=new BufferedWriter(fw);
			
			for(int i=0;i<points.size();i++) {
				bw.write(points.get(i).name);
				bw.newLine();
				bw.write(Double.toString(points.get(i).x));
				bw.newLine();
				bw.write(Double.toString(points.get(i).y));
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}