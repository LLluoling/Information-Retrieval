package pack;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.el.parser.ParseException;
import org.apache.lucene.analysis.Analyzer;    
import org.apache.lucene.analysis.standard.StandardAnalyzer;    
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;    
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Servlet implementation class query
 */
public class query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		request.setCharacterEncoding("UTF-8");
		
		//取的搜索的是哪一个域
		String area= request.getParameter("choice");
		//取的搜索的内容
		String text = request.getParameter("wd");
		
		out.println("<meta charset=\"utf-8\"/>");
		out.println("<html><head> <TITLE>LL搜索引擎 </TITLE></head>");
		out.println("<body><CENTER><BR><BR>");
		out.println("<p><img border=\"0\" src=\"mark.jpg\" width=\"245\" height=\"106\"></p>");
		out.println("<TABLE id=1 cellSpacing=0 cellpadding =0>");
		out.println("<TBODY>\r\n" + 
				"  <TR>\r\n" + 
				"    <TD align=left>\r\n" + 
				"      <DIV id=m>　</DIV></TD></TR></TBODY></TABLE>");
		out.println("<TABLE cellSpacing=0 cellPadding=0>\r\n" + 
				"  <TBODY>\r\n" + 
				"  <TR vAlign=top>\r\n" + 
				"    <TD width=92></TD>\r\n" + 
				"    <TD noWrap height=62>\r\n" + 
				"      <FORM method=\"post\" action=\"query\"><INPUT id=kw maxLength=100 size=36 name=wd style=\"width:280px;height:20px;\">\r\n" + 
				"      <INPUT type=hidden value=3 name=cl> \r\n" + 
				"      <INPUT id=sb type=submit value=LL搜索  style=\"background:#6FB7B7;width:60px;height:30px;\">\r\n" + 
				"      <INPUT type=\"radio\" name=\"choice\" value=\"content\" checked>内容"  +
				"      <INPUT type=\"radio\" name=\"choice\" value=\"achor\" >锚文本"  +
				"      <INPUT type=\"radio\" name=\"choice\" value=\"title\" >标题"  +
				"     <BR><BR></FORM></TD>\r\n"+
				"    <TD width=100>　</TD></TR></TBODY></TABLE>\r\n" + 
			   
				"</CENTER>");
		
		try{
			Directory directory =FSDirectory.open(new File("F:\\信息检索\\index"));
			@SuppressWarnings("deprecation")
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);    
			DirectoryReader reader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);    

			out.println("<table border=0 width=80% id=table1 cellspacing=0 cellpadding=0>");
			
			
			Query query=null;
			ScoreDoc[] hits=null;
			if(text != null)
			{
				
				@SuppressWarnings("deprecation")
				//默认是content搜索
				QueryParser parser=new QueryParser(Version.LUCENE_CURRENT, "content", analyzer);;
				if(area.equals("content")){
					parser = new QueryParser(Version.LUCENE_CURRENT, "content", analyzer);
					query = parser.parse(text); 
				}
				else if(area.equals("title")){
					parser = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer);
					query = parser.parse(text); 
				}
				else if(area.equals("achor")){
					parser = new QueryParser(Version.LUCENE_CURRENT, "achor", analyzer);
					query = parser.parse(text); 
				}
				
				  
				
				if (searcher != null) { 
					//返回最多30篇文本
					hits =searcher.search(query, null,30).scoreDocs; 
					int i = 0;
					if (hits.length > 0)
					{ 
						
						while(i < hits.length)
						{
							Document d = searcher.doc(hits[i].doc);
							
							//打印title
							out.print("<tr>");
							out.print("<td>");
							out.print("<font color= #0072E3 size=\"3.5px\">");
							out.print("<a href=\"" + d.get("url") + "\">");
							out.print(d.get("title"));
							out.print("</a>");
							out.print("</font>");
							out.print("</td>");
							out.print("</tr>");
							//打印一些内容
							out.print("<tr>");
							out.print("<td>");
							out.print("<font color= #616130 size=\"2px\">");
							out.print(d.get("content").substring(0,200));
							out.print("</font>");
							out.print("</td>");
							out.print("</tr>");
							//打印百度快照
							out.print("<tr>");
							out.print("<td>");
							if(d.get("url").length()>=40){
								out.print(d.get("url").substring(0, 25));
							}
							else
								out.print(d.get("url"));
							out.print("<font color=	#00FFFF size=\"2px\">");
							out.print("<a href=\"" + d.get("path") + "\">");
							out.print("    本地快照");
							out.print("</a>");
							out.print("</font>");
							out.print("</td>");
							out.print("</tr>");
							
							out.print("<tr>");
							out.print("</tr>"); 	
							i++;
							} 
					}
					else
					{
						out.print("<tr>");
						out.print("<td>");
						out.print("没有你要找的项");
						out.print("<tr>");
						out.print("<td>");
					}
				} 
			}	
			reader.close();
            directory.close();
			out.println("<table>");
			out.println("</BODY></HTML>");  
		}catch(Exception e){}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
