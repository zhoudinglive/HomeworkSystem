package com.carpenter.ssm.assist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

/**   
* @Title: FileMaker.java 
* @Package com.carpenter.ssm.assist 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月18日 下午2:09:56 
* @version V1.0   
*/

public class FileMaker {

	public static boolean createNewFileDir(String path) throws Exception{
		try {
//			Path tmpPath = Paths.get(path);
//			if(Files.exists(tmpPath)){
//				return "01";
//			}
//			Files.createDirectory(tmpPath);
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			return false;
		}		
	}
	
	public static boolean deleteFileDir(String path) throws Exception{
		try {
			File file = new File(path);
			if(file.isDirectory()){
				String[] children = file.list();
				for(int i=0;i<children.length;++i){
					if(!deleteFileDir(path+File.separator+children[i])){
						return false;
					}
				}
			}
			return file.delete();
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean deleteFileDirButItself(String path) throws Exception{
		try {
			File file = new File(path);
			if(file.isDirectory()){
				String[] children = file.list();
				for(int i=0;i<children.length;++i){
					if(!deleteFileDir(path+File.separator+children[i])){
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean ziper(File orgin, File dest) throws Exception{
		if(orgin == null || dest == null){
			return false;
		}/*else if(!orgin.exists() || !dest.exists()){
			return 2;
		}*/
		
		try {
			Project DEFAULT_PROJECT = new Project();
			Zip zip = new Zip();
			zip.setProject(DEFAULT_PROJECT);
			zip.setDestFile(dest);
			
			File copyFile = new File("/home/upload/zip/"+orgin.getName());
			copyFolder(orgin, copyFile);
			
			FileSet fs = new FileSet();
			fs.setProject(DEFAULT_PROJECT);
			fs.setDir(copyFile);
			
			File[] files = copyFile.listFiles();
			for(File file : files){
				if(file.isDirectory()){
					File[] tmp = file.listFiles();
					if(tmp.length==0){
						deleteFileDir(file.getAbsolutePath());
						//fs.setExcludes(file.getAbsolutePath());
					}
				}
			}
			
			zip.addFileset(fs);
			zip.execute();
			deleteFileDir(copyFile.getAbsolutePath());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static void copyFolder(File src, File dest) throws IOException {  
	    if (src.isDirectory()) {  
	        if (!dest.exists()) {  
	            dest.mkdir();  
	        }  
	        String files[] = src.list();  
	        for (String file : files) {  
	            File srcFile = new File(src, file);  
	            File destFile = new File(dest, file);  
	            // 递归复制  
	            copyFolder(srcFile, destFile);  
	        }  
	    } else {  
	        InputStream in = new FileInputStream(src);  
	        OutputStream out = new FileOutputStream(dest);  
	  
	        byte[] buffer = new byte[1024];  
	  
	        int length;  
	          
	        while ((length = in.read(buffer)) > 0) {  
	            out.write(buffer, 0, length);  
	        }  
	        in.close();  
	        out.close();  
	    }  
	} 
	
	public static String ss(String str){
		return str.replace("\"", "\\\"");
	}

}
