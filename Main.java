import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import java.lang.Process;

public class Main {
    
    static File file = new File("data.html");
    static int sampleCounter = 0;

    public static List<String> extractGeneID() throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        int counter = 0;
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
            if (finalID.length() == 0) {
                finalID = "ID: " + extractGeneID().get(i);
            }
            geneNames.add(finalID);
        }

        return geneNames;
    }

    public static List<String> getGroups() throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        Elements groupNames = doc.select("div[class=groupText]");
        List<String> groups = new ArrayList<String>(new ArrayList<String>());
	
        for (int i = 0; i < groupNames.size(); i++) {
            String groupHTML = groupNames.get(i).select("input[type=hidden]").attr("value");
            String groupSampleSizegeneNames = groupNames.get(i).select("span[class=grpSelectedCnt]").text();
	        Scanner scanner = new Scanner(groupSampleSizegeneNames);
	        String size = scanner.next().substring(1);
            for (int n = 0; n < Integer.parseInt(size); n++) {
                groups.add(groupHTML);
            }
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
       //int counter = 1;

        for (int i = 0; i < geneID.size(); i++) {
            Element current = sampleTable.get(i);
            Elements rows = current.select("tr");
            for (int n = 1; n < rows.size(); n++) { 
                Elements sampleValues = rows.get(n).select("td");
                currentExpressionValue.add(sampleValues.get(2).html());
                expressionData.addAll(currentExpressionValue);
                currentExpressionValue.clear();
            }
        }
       return expressionData;
    }

    public static void write() {

        try {

            int counter = 0;
            List<String> groups = getGroups();
            int groupCounter = 0;

            // initialize file and file writers
            File file = new File("data/iris.data.txt");
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            // create header
            List<String> geneNames = extractGeneNames();
            List<String> test = new ArrayList<String>();
            for (int i = 0; i < geneNames.size() - 1; i++) {
                test.add(geneNames.get(i));
            }
             for (int i = 0; i < test.size(); i++) {
                bw.write(test.get(i) + ",");
                bw.flush();
                if (i == test.size() - 1) {
                    bw.write("y\n");
                    bw.flush();
                }
            }

            // create each column
            List<String> expressionValues = getExpressionData();

            for (int n = 0; n < expressionValues.size() / geneNames.size(); n++) {
                for (int i = 0; i < test.size() + 1; i++) {
                    if (i == test.size()) {
                        bw.write(expressionValues.get(counter) + ", ");
                        counter += (expressionValues.size() / geneNames.size() + 1);
                    } else {
                        bw.write(expressionValues.get(counter) + ", ");
                        counter += (expressionValues.size() / geneNames.size());
                    }
                }
                bw.write(groups.get(groupCounter) + "\n");   
                groupCounter++;
                bw.flush();  

                counter = n + 1;
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        write();
        
    }
}