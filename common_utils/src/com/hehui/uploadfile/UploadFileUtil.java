package com.hehui.uploadfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @date 2019年6月7日
 * @file UploadFileUtil.java
 * @author hehui
 *
 */
public class UploadFileUtil {

	/**
	 * 解析文件上传的请求，并保存在用户指定的path下 保存的文件名为：原文件名_随机uuid.原文件扩展名
	 * 
	 * @param path        从项目的webRoot开始写起
	 * @param fileSizeMax 文件大小上限 单位为kb
	 * @param request
	 * @return 有文件则返回已保存的文件路径集合，没文件则返回空集合，有异常则返回null
	 */
	/**
	 * 
	 * @param path
	 * @param fileSizeMax
	 * @param request
	 * @return
	 */
	public static List<String> uploadFile(String path, long fileSizeMax, HttpServletRequest request) {
		List<String> fileList = null;
		// 解析三步
		// 创建工厂
		FileItemFactory factory = new DiskFileItemFactory();
		// 创建解析器对象
		ServletFileUpload sfu = new ServletFileUpload(factory);

		sfu.setFileSizeMax(fileSizeMax * 1024);

		// 解析request对象，得到用户请求对象中的所以数据，返回一个List<FileItem>
		List<FileItem> fileItemList = null;
		try {
			fileItemList = sfu.parseRequest(request);
		} catch (Exception e) {// 文件大小超过用户指定的大小
			e.printStackTrace();
			throw new RuntimeException("用户上传的文件大小超过了" + fileSizeMax + "KB");
		}
		// 创建存在文件路径的集合
		fileList = new ArrayList<String>();
		for (FileItem fileItem : fileItemList) {
			if (!fileItem.isFormField()) {// 只处理表单中的文件
				String filename = fileItem.getName();
				if (filename == null || filename.length() == 0)
					continue;// 该文件input框为空
				File file = null;
				try {
					// 创建目录
					String webRootPath = request.getServletContext().getRealPath("/");
					String dirPath = path;
					String savePath = webRootPath + dirPath;
					String originalFilename = filename.lastIndexOf("\\") == -1 ? filename
							: filename.substring(filename.lastIndexOf("\\") + 1);
					String extensionFilename = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
					String newFilename = originalFilename.substring(0, originalFilename.lastIndexOf(".")) + "_"
							+ UUID.randomUUID() + "." + extensionFilename;
					file = new File(savePath);
					if (!file.exists() && !file.isDirectory()) {
						file.mkdirs();// 不存在该目录则创建该目录
					}
					file = new File(savePath, newFilename);
					fileItem.write(file);
					fileList.add(savePath + File.separator + newFilename);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return fileList;
	}

}
