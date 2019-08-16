package main;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
	//_
	//\

	static File file = new File("/Users/arjunaryansingh/eclipse-workspace/Data/src/main/data.html");	
	
	public static List<String> extractGeneID() throws IOException {
		 Document doc = Jsoup.parse(file, "UTF-8");
       List<String> geneList = new ArrayList<String>();
		String geneID = doc.getElementsByClass("gene_id").text();
		geneID.split(" ");
		Scanner scanner = new Scanner(geneID);
		while (scanner.hasNext()) {
			geneList.add(scanner.next());
		}
		scanner.close();
		return geneList;
	}
	
	
	public static List<String> extractGeneNames() throws IOException {
		 Document doc = Jsoup.parse(file, "UTF-8");
		Elements tr = doc.select("tr[class=active loaded], tr[class=loaded active]");
		List<String> geneNames = new ArrayList<String>();
		
		for (int i = 0; i < tr.size(); i++) {
			Elements current = tr.get(i).select("td");
			String finalID = current.get(7).text();
			geneNames.add(finalID);
		} 
		
		return geneNames;
	}
	
	public static List<String> organizeData() throws IOException {
		
		List<String> geneNames = extractGeneNames();
		List<String> geneID = extractGeneID();
		List<String> idData = new ArrayList<String>(new ArrayList<String>());
		
		for (int i = 0; i < geneNames.size(); i++) {
			List<String> current = new ArrayList<String>();
			current.add(geneNames.get(i) + "," + geneID.get(i));
			idData.add(current.toString());
		}
		return idData;
	}
	
	public static List<String> getGroups() throws IOException {
		 Document doc = Jsoup.parse(file, "UTF-8");
		Elements groupNames = doc.select("div[class=groupText]");
		List<String> groups = new ArrayList<String>(new ArrayList<String>());
		
		for (int i = 0; i < groupNames.size(); i++) {
			String groupHTML = groupNames.get(i).select("input[type=hidden]").attr("value");
			String groupSampleSize = groupNames.get(i).select("span[class=grpSelectedCnt]").text();
			List<String> current = new ArrayList<String>();
			current.add(groupHTML + " " + groupSampleSize);
			groups.add(current.toString());
		}
		return groups;
	}

	public static void getExpressionData() throws IOException {
		Document doc = Jsoup.parse(file, "UTF-8");
		Elements sampleTable = doc.select("table[id=sampleTable]");
		String[][] expressionData = new String[10000][3];		
		

		for (int i = 0; i < sampleTable.size(); i++) {
			Elements current = sampleTable.get(i).select("tr");
			for (int n = 0; n < current.size(); n++) {
				Element currentElement = current.get(n);
				
				String sampleID = currentElement.select("td").toString();
				System.out.print(sampleID);
				
				
			}

			// for (Element tr : current) {
			// 	String sampleID = tr.select("td").first().toString();
			// 	String geneID = tr.select("td").get(2).toString();
			// 	String expressionValue = tr.select("td").get(3).toString();
			// 	System.out.print(sampleID + " " + geneID + " " + expressionValue + " " + "\n");
			// }
		} 

	}
	
	public static void main(String[] args) throws IOException {
		
		getExpressionData();
	}
}	
