package edu.zju.sa.servlets;

import edu.zju.sa.dao.BOSClient;
import edu.zju.sa.dao.BOSClientFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Servlet implementation class FileGetServlet
 */
@WebServlet("/FileGetServlet")
public class FileGetServlet extends HttpServlet {
	private BOSClient bosClient = BOSClientFactory.getClient();

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String filePath = request.getParameter("path");
		if (filePath == null) {
			// bad request
			response.sendError(400);
			return;
		}
		System.out.println("Get file: " + filePath);
		byte[] data = null;
		try {
			data = bosClient.getFile(filePath);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (data == null) {
			response.sendError(404);
			return;
		}
		
		response.setContentType("text/plain; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Location", filePath);
		response.addHeader("Content-Disposition", "attachment; filename=" + filePath);
		// cache for one month
	
		response.setContentLength(data.length);
		OutputStream os = response.getOutputStream();
		os.write(data);
		os.flush();
		os.close();
	}
}
