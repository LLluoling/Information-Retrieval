package ttt;
import java.awt.datatransfer.StringSelection;
import java.awt.peer.SystemTrayPeer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.kerberos.KerberosKey;

public class ttt {
	//保存链接
	static String[] strings=new String[500];
	//保存每个链接的指向的个数
	static HashMap<String,Integer>linksnum=new HashMap<String,Integer>();
	//保存每个链接的指向的所有链接
	static HashMap<String,Set<String>>linksto=new HashMap<String,Set<String>>();
	public static void intiallinks() throws IOException{
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
			int j=0;
			HashSet<String> linksSet=new HashSet<String>();
			while((string=bReader.readLine())!=null){
				if(j==0){
					strings[i]=string;
					j++;
				}
				else if(j==1){
					j++;
				}
				else{
					linksSet.add(string);
				}
			}
			linksnum.put(strings[i], linksSet.size());
			linksto.put(strings[i], linksSet);
		}
		bReader.close();
	}
	public static void main(String[] args) throws IOException {
		//先读文件
		intiallinks();
		
		//存放最后pagerank的值
		float[] pageRank = new float[500];
		// 链入页面的计算总和  
        float[] prTmp = new float[500];  
        // 设置pageRank[]初始值为1.0f  
        for (int i = 0; i < 500; ++i) {  
            pageRank[i] = 1.0f/500;  
            prTmp[i] = 0.0f;  
        }  
        //系数矩阵
        float[][]coefficient=new float[500][500];
        //系数矩阵的初始值全部赋值为0
        for(int i=0;i<500;i++){
        	for(int j=0;j<500;j++){
        		coefficient[i][j]=0.0f;
        	}
        }

        // 阻尼系数d 
        float d = 0.85f; 
        
        
        //先修改一下指向外面的链接。因为可能我爬取的的网页的链接在指向的链接集合里面没有
        for(int i=0;i<500;i++){
        	for(String value : linksto.get(strings[i])){  
        		if(!(linksnum.containsKey(value))){
        			int tmpi=linksnum.get(strings[i]);
        			tmpi--;
        			linksnum.put(strings[i], tmpi);
        		}
        	}  
        }
        
        
        //给一个临时变量存每一个url的编号
        HashMap<String,Integer>NumberTag=new HashMap<String,Integer>();
        for(int i=0;i<500;i++){
        	NumberTag.put(strings[i], i);
        }
        
        
        //给矩阵赋初值,遍历的是500个链接
        for(int i=0;i<500;i++){
        	float tmpfloat=linksnum.get(strings[i]);
        	for(String li : linksto.get(strings[i])){
        		if(linksnum.containsKey(li)){
        			coefficient[NumberTag.get(li)][i]=1/tmpfloat;
        		}
            } 
        }
        float N=500;
        //迭代10次
        for (int iterator = 0; iterator < 15; iterator++) { 
        	for(int k=0;k<500;k++){
        		System.out.print("\t"+pageRank[k]);
        	}
        	System.out.println();
        	for(int k=0;k<500;k++){
        		for(int t=0;t<500;t++){
        			prTmp[k]+=coefficient[k][t]*pageRank[t];
        		}
        		prTmp[k]=(1-d)/N+prTmp[k]*d;
        	}
        	//将prTmp的值赋给pageRank
        	for(int k=0;k<500;k++){
        		pageRank[k]=prTmp[k];
        	}
        	for(int k=0;k<500;k++){
        		prTmp[k]=0.0f;
        	}
        }
        float countss2=0.0f;
        for(int k=0;k<500;k++){
        	countss2+=pageRank[k];
    	}
        System.out.println();
        System.out.println("所有的pagerank的总和：\t"+countss2);
	}
}








