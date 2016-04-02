package edu.zju.sa.servlets;

import com.google.gson.JsonObject;
import edu.zju.sa.dao.BOSClient;
import edu.zju.sa.dao.BOSClientFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/FileUploadServlet")
public class FileUploadServlet extends HttpServlet {
	private BOSClient bosClient = BOSClientFactory.getClient();

	public FileUploadServlet() {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//新增
		
		
		response.setContentType("text/plain; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if (!ServletFileUpload.isMultipartContent(request)) {
			out.println(jsonOutput(1, "请选择文件。"));
			return;
		}

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		List items = null;
		try {
			items = upload.parseRequest(request);
		} catch (Exception ex) {
			out.println(jsonOutput(1, ex.getMessage()));
			return;
		}
		Iterator itr = items.iterator();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			String fileName = item.getName();
			if (!item.isFormField()) {
				// 检查文件大
				try {
					bosClient.putFile(fileName, item.get(), true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		//新增
		
		System.out.println("上传成功！");
		out.println(jsonOutput(0, null));
	}

	private String jsonOutput(int errorCode, String message) {
		JsonObject obj = new JsonObject();
		obj.addProperty("ok", errorCode);
		if(message!=null) {
			obj.addProperty("message", message);
		}
		return obj.toString();
	}
}
