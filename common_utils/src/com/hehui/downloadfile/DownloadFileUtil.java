package com.hehui.downloadfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @date	2019年6月1日
 * @file	DownloadFileUtil.java
 * @author	hehui
 *
 */
public class DownloadFileUtil {
	
	/**
	   * 文件下载
	 * @param request
	 * @param response
	 * @param filePath
	 */
	public static void downloadFile(HttpServletRequest request,HttpServletResponse response,String filePath) {
		String remoteAddr = request.getRemoteAddr();
		System.out.println(remoteAddr+"...");
		InputStream fis = null;
		OutputStream out = null;
		String separator = File.separator;
		String sysPath = request.getServletContext().getRealPath(separator);
		
		try {
			String path = java.net.URLDecoder.decode(sysPath + filePath, "UTF-8");
            File file = new File(path);
            String filename = file.getName();
 
            // 以流的形式下载文件。
            fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            out = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream;chartset=utf-8");
            response.setCharacterEncoding("UTF-8");
            
            out.write(buffer);
            out.flush();
            
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
