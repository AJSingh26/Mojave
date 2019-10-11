import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;

public class Main {
    
    static File file = new File("/Users/arjunaryansingh/Desktop/DataTest/data.html");
    
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
        List<String> expressionData = getExpressionData();
	List<String> groups = getGroups();

	// for this, the second i block is always going to be an odd number, so have this loop occur a number of times equal to the number of odd places in the groups list.
	for (int i = 0; i < Integer.parseInt(groups.get(1)); i++) {
	    System.out.print("hello");
	}
	
        for (int i = 0; i < geneNames.size(); i++) {
            List<String> current = new ArrayList<String>();
	    current.add(geneNames.get(i) + "," + geneID.get(i));
            for (int n = 0; n < expressionData.size() / geneNames.size(); n++) {
                current.add(expressionData.get(n));
		
            }
            idData.add(current.toString()); // FIX THIS
        }
        return idData;
    }

    public static List<String> getGroups() throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        Elements groupNames = doc.select("div[class=groupText]");
        List<String> groups = new ArrayList<String>(new ArrayList<String>());
	
        for (int i = 0; i < groupNames.size(); i++) {
            String groupHTML = groupNames.get(i).select("input[type=hidden]").attr("value");
            String groupSampleSizeTest = groupNames.get(i).select("span[class=grpSelectedCnt]").text();
	    Scanner scanner = new Scanner(groupSampleSizeTest);
	    String size = scanner.next().substring(1);
	    groups.add(groupHTML);
	    groups.add(size);
	}

	return groups;
	
    }

    public static List<String> getExpressionData() throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        List<String> geneID = extractGeneID();
        Elements sampleTable = doc.select("table[id=sampleTable]");
        List<String> expressionData = new ArrayList<String>(new ArrayList<String>());
        List<String> currentExpressionValue = new ArrayList<String>();
	List<String> groupData = getGroups();

        for (int i = 0; i < geneID.size(); i++) {
            Element current = sampleTable.get(i);
            Elements rows = current.select("tr");
            for (int n = 1; n < rows.size(); n++) {
                Elements sampleValues = rows.get(n).select("td");
                currentExpressionValue.add(sampleValues.get(1).html());
                currentExpressionValue.add(sampleValues.get(2).html());
                expressionData.addAll(currentExpressionValue);
                currentExpressionValue.clear();
            }
        }
        return expressionData;
    }

        public static void main(String[] args) throws IOException {
	    
	}
}
