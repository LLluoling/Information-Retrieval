import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;  
import org.htmlparser.Parser;  
import org.htmlparser.beans.StringBean;  
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;  
import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;  
import org.jsoup.select.Elements;  

public class HtmlUtil {  
	//获得锚文本
	static HashMap<String,String>strings=new HashMap<String,String>();
	static String[] urlStrings=new String[500];
    public static void main(String[] args) throws Exception{
    	
    	GetLinks();
    	writetitle();
    	WriteAchor();
    	//WriteContent();
		
    }
    public static void WriteContent() throws Exception{
    	String readhead="F:\\信息检索\\web\\";
    	String writehead="F:\\信息检索\\content\\";
		
		
		//读
		FileInputStream fi=null;
		InputStreamReader inr=null;
		BufferedReader bReader=null;
		//写
		FileWriter fileWriter=null;
		BufferedWriter bufferedWriter=null;
		for(int i=0;i<500;i++){
			String tmpString1="";
			String tmpString2="";
			//先读
			fi=new FileInputStream(readhead+i+".html");
			inr=new InputStreamReader(fi);
			bReader=new BufferedReader(inr);
			String string="";
			while ((string=bReader.readLine())!=null) {
				tmpString1+=string;
			}
			tmpString2=extractText(tmpString1);
			File file=new File(writehead+i+"_.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			fileWriter=new FileWriter(file.getPath(),true);
			bufferedWriter=new BufferedWriter(fileWriter);
			bufferedWriter.write(tmpString2);
		}
		bReader.close();
		bufferedWriter.close();
    }
    public static void WriteAchor() throws IOException{
    	
	
    	String sssString="";
		for(int i=0;i<500;i++){
			if(!strings.get(urlStrings[i]).equals(""))
				sssString=sssString+i+"\r\n"+strings.get(urlStrings[i])+"\r\n";
			
		}
		File file=new File("F:\\信息检索\\achor.txt");
		if(!file.exists()){
			file.createNewFile();
		}
		
		FileWriter fileWriter=new FileWriter(file.getPath(),true);
		BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
		bufferedWriter.write(sssString);
		bufferedWriter.close();
    		
    }
    public static String extractText(String inputHtml) throws Exception {
    	 StringBuffer text = new StringBuffer();
    	    Parser parser = Parser.createParser(new String(inputHtml.getBytes(),"utf-8"), "utf-8");
    	    // 遍历所有的节点  	    
    	    NodeList nodes = parser.extractAllNodesThatMatch(new NodeFilter() {
    	        public boolean accept(Node node) {
    	          return true;
    	        }
    	    });

    	    System.out.println(nodes.size()); //打印节点的数量
    	    for (int i=0;i<nodes.size();i++){
    	         Node nodet = nodes.elementAt(i);
    	         //System.out.println(nodet.getText()); 
    	        text.append(new String(nodet.toPlainTextString().getBytes("utf-8")));          
    	    }
    	    return text.toString();

    }
    public static String extractTitle(String inputHtml) throws Exception {
    	//System.out.print(inputHtml);
    	String htmlString=inputHtml;
    	String regex;
    	String title = "";
    	final ArrayList<String> list = new ArrayList<String>();
    	regex = "<title>.*?</title>";
    	final Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
    	final Matcher ma = pa.matcher(inputHtml);
    	while (ma.find())
    	{
    	  list.add(ma.group());
    	}
    	String string="";
    	int t;
    	for (int i = 0; i < list.size(); i++)
    	{
    		t=list.get(i).indexOf(">");
    		string=list.get(i).substring(t+1);
    		string=string.substring(0,string.indexOf("<"));
    		title = title + string;
    	}
    	
    	
    	//获得锚文本
    	String string2="href=\"";
    	int tmpNum;
    	int start;
    	int end;
    	String urlString="";
    	String tmpString="";
    	
    	while((tmpNum=htmlString.indexOf(string2))>=0){
    		htmlString=htmlString.substring(tmpNum+string2.length());
    		urlString=htmlString.substring(0,htmlString.indexOf("\""));
    		
    		start=htmlString.indexOf(">");
    		end=htmlString.indexOf("<");
    		if(!urlString.substring(0,5).equals("http:")){
    			urlString="http://www.nankai.edu.cn"+urlString;
    		}
    		
    		if(strings.containsKey(urlString)){
        		if(start>=0&&end>=0&&end>start){
        			tmpString=htmlString.substring(start+1,end);
        			//System.out.println(urlString);
        			//System.out.println(tmpString);
        			String tmp=strings.get(urlString);
        			tmp=tmp+tmpString;
        			strings.put(urlString,tmp );
        		}
    		}
    		htmlString=htmlString.substring(end);
    		
    	}
    	return title;
    }
    public static void writetitle() throws Exception{
    	String readhead="F:\\信息检索\\web\\";		
		
		//读
		FileInputStream fi=null;
		InputStreamReader inr=null;
		BufferedReader bReader=null;
		String tmpString2="";
		for(int i=0;i<500;i++){
			String tmpString1="";
			
			//先读
			fi=new FileInputStream(readhead+i+".html");
			inr=new InputStreamReader(fi);
			bReader=new BufferedReader(inr);
			String string="";
			while ((string=bReader.readLine())!=null) {
				tmpString1+=string;
			}
			//tmpString2=extractText(tmpString1);
			tmpString2=tmpString2+extractTitle(tmpString1)+"\r\n";
			
		}
		bReader.close();
		
		File file=new File("F:\\信息检索\\title.txt");
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fileWriter=new FileWriter(file.getPath(),true);
		BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
		bufferedWriter.write(tmpString2);
		bufferedWriter.close();
    }
    public static void GetLinks() throws IOException{
    	String readhead="F:\\信息检索\\keeper\\";
    	FileInputStream fi=null;
		InputStreamReader inr=null;
		BufferedReader bReader=null;
		for(int i=0;i<500;i++){			
			//先读
			fi=new FileInputStream(readhead+i+".txt");
			inr=new InputStreamReader(fi);
			bReader=new BufferedReader(inr);
			String string="";
			String vacant="";
			if((string=bReader.readLine())!=null){
				strings.put(string, vacant);
				urlStrings[i]=string;
			}
		}
		bReader.close();
    }
}


