/**
 * ���������������
 * 
 * @author heyi
 * @version 2013-6-24
 * 
 */
import java.io.*;
public class BatchReplaceTool {

	/** ������·�� */
	private  String dir;
	/** �ļ���׺�� */
	private  String[] suffixs;
	/** Ҫ��������ַ� */
	private  String oldChar;
	/** ����ַ� */
	private  String newChar;

	public BatchReplaceTool(String dir,String[] suffixs,String oldChar,String newChar){
		this.dir=dir;
		this.suffixs=suffixs;
		this.oldChar=oldChar;
		this.newChar=newChar;
	}
	
	public BatchReplaceTool() {

	}
	private void execute(){
		File afile=new File(this.dir);
		this.batchReplace(afile);
	}

/**�ж��ļ��ı����ʽ*/
	private static  String getFilecharset(File sourceFile) {
		  String charset = "GBK";
		  byte[] first3Bytes = new byte[3];
		  try {
		   boolean checked = false;
		   BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
		   bis.mark(0);
		   int read = bis.read(first3Bytes, 0, 3);
		   if (read == -1) {
		    return charset; //�ļ�����Ϊ ANSI
		   } else if (first3Bytes[0] == (byte) 0xFF
		     && first3Bytes[1] == (byte) 0xFE) {
		    charset = "UTF-16LE"; //�ļ�����Ϊ Unicode
		    checked = true;
		   } else if (first3Bytes[0] == (byte) 0xFE
		     && first3Bytes[1] == (byte) 0xFF) {
		    charset = "UTF-16BE"; //�ļ�����Ϊ Unicode big endian
		    checked = true;
		   } else if (first3Bytes[0] == (byte) 0xEF
		     && first3Bytes[1] == (byte) 0xBB
		     && first3Bytes[2] == (byte) 0xBF) {
		    charset = "UTF-8"; //�ļ�����Ϊ UTF-8
		    checked = true;
		   }
		   bis.reset();
		   if (!checked) {
		    int loc = 0;
		    while ((read = bis.read()) != -1) {
		     loc++;
		     if (read >= 0xF0)
		      break;
		     if (0x80 <= read && read <= 0xBF) // ��������BF���µģ�Ҳ����GBK
		      break;
		     if (0xC0 <= read && read <= 0xDF) {
		      read = bis.read();
		      if (0x80 <= read && read <= 0xBF) // ˫�ֽ� (0xC0 - 0xDF)
		       // (0x80
		       // - 0xBF),Ҳ������GB������
		       continue;
		      else
		       break;
		     } else if (0xE0 <= read && read <= 0xEF) {// Ҳ�п��ܳ������Ǽ��ʽ�С
		      read = bis.read();
		      if (0x80 <= read && read <= 0xBF) {
		       read = bis.read();
		       if (0x80 <= read && read <= 0xBF) {
		        charset = "UTF-8";
		        break;
		       } else
		        break;
		      } else
		       break;
		     }
		    }
		   }
		   bis.close();
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		  return charset;
		 }
	
	private void batchReplace(File afile){

		if(afile!=null){
			if(afile.exists()){
				if(afile.isDirectory()){
					File[] files= afile.listFiles();
					for(File sonfile:files){
						batchReplace(sonfile);
					}
				}else{
					Boolean needReplace=false;
					String filename=afile.getName();
					String[] split=filename.split("\\.");
					String suffix=split[split.length-1];
					if(suffixs!=null){
						for(String s:suffixs){
							if(s.equals(suffix)){
								needReplace=true;
								break;
							}
						}
					}
					
					if(needReplace)
					fileReplace(afile);
				}
			}
			
		}
	}
	
	private void fileReplace(File afile) {

		InputStreamReader inr=null;
		BufferedReader br=null;
		OutputStreamWriter fw=null;
		try{
		String cs=getFilecharset(afile);
		System.out.println(afile.getName()+":"+cs);
		inr=new InputStreamReader(new FileInputStream(afile), cs);
		br=new BufferedReader(inr);
		String tmp=null;
		StringBuilder sb=new StringBuilder();
		while((tmp=br.readLine()) != null){
			sb.append(tmp);
			sb.append("\r\n");
		}
		String newContent=sb.toString().replace(this.oldChar, this.newChar);
		fw=new OutputStreamWriter(new FileOutputStream(afile), cs);
		fw.write(newContent);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
			if(inr!=null)
				inr.close();
			if(br!=null)
				br.close();
			if(fw!=null)
				fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String[] getSuffixs() {
		return suffixs;
	}

	public void setSuffix(String[] suffixs) {
		this.suffixs = suffixs;
	}

	public String getOldChar() {
		return oldChar;
	}

	public void setOldChar(String oldChar) {
		this.oldChar = oldChar;
	}

	public String getNewChar() {
		return newChar;
	}

	public void setNewChar(String newChar) {
		this.newChar = newChar;
	}

	public static void main(String[] args) {
		String[] suffixs={"java"};
		BatchReplaceTool tool=new BatchReplaceTool("F:\\workspace\\weishangbao\\src",suffixs,"����֪�ݹ���Ϣϵͳ���޹�˾","���ݰ�����Ϣ�������޹�˾");

		System.out.println("done!");
	}

}
