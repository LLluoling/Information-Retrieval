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
	//��������
	static String[] strings=new String[500];
	//����ÿ�����ӵ�ָ��ĸ���
	static HashMap<String,Integer>linksnum=new HashMap<String,Integer>();
	//����ÿ�����ӵ�ָ�����������
	static HashMap<String,Set<String>>linksto=new HashMap<String,Set<String>>();
	public static void intiallinks() throws IOException{
		String readhead="F:\\��Ϣ����\\keeper\\";
    	FileInputStream fi=null;
		InputStreamReader inr=null;
		BufferedReader bReader=null;
		for(int i=0;i<500;i++){			
			//�ȶ�
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
		//�ȶ��ļ�
		intiallinks();
		
		//������pagerank��ֵ
		float[] pageRank = new float[500];
		// ����ҳ��ļ����ܺ�  
        float[] prTmp = new float[500];  
        // ����pageRank[]��ʼֵΪ1.0f  
        for (int i = 0; i < 500; ++i) {  
            pageRank[i] = 1.0f/500;  
            prTmp[i] = 0.0f;  
        }  
        //ϵ������
        float[][]coefficient=new float[500][500];
        //ϵ������ĳ�ʼֵȫ����ֵΪ0
        for(int i=0;i<500;i++){
        	for(int j=0;j<500;j++){
        		coefficient[i][j]=0.0f;
        	}
        }

        // ����ϵ��d 
        float d = 0.85f; 
        
        
        //���޸�һ��ָ����������ӡ���Ϊ��������ȡ�ĵ���ҳ��������ָ������Ӽ�������û��
        for(int i=0;i<500;i++){
        	for(String value : linksto.get(strings[i])){  
        		if(!(linksnum.containsKey(value))){
        			int tmpi=linksnum.get(strings[i]);
        			tmpi--;
        			linksnum.put(strings[i], tmpi);
        		}
        	}  
        }
        
        
        //��һ����ʱ������ÿһ��url�ı��
        HashMap<String,Integer>NumberTag=new HashMap<String,Integer>();
        for(int i=0;i<500;i++){
        	NumberTag.put(strings[i], i);
        }
        
        
        //�����󸳳�ֵ,��������500������
        for(int i=0;i<500;i++){
        	float tmpfloat=linksnum.get(strings[i]);
        	for(String li : linksto.get(strings[i])){
        		if(linksnum.containsKey(li)){
        			coefficient[NumberTag.get(li)][i]=1/tmpfloat;
        		}
            } 
        }
        float N=500;
        //����10��
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
        	//��prTmp��ֵ����pageRank
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
        System.out.println("���е�pagerank���ܺͣ�\t"+countss2);
	}
}








