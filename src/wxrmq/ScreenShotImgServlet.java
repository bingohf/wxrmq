package wxrmq;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional.TxType;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.google.gson.Gson;
import com.oracle.webservices.internal.api.databinding.DatabindingFactory;
import com.sun.net.httpserver.Headers;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import wxrmq.data.remote.ContactList;
import wxrmq.data.remote.MFile;
import wxrmq.data.remote.UUIDResponse;
import wxrmq.domain.WxUser;
import wxrmq.utils.NetWork;
import wxrmq.utils.TextUtils;

public class ScreenShotImgServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		String uin = TextUtils.getPathParam(req.getRequestURL().toString(),-1);
		String fileName = TextUtils.getPathParam(req.getRequestURL().toString(),0);
		File dir = new File("screenshot/" + uin +"/");
		File file = new File(dir, fileName);
		
		 ServletContext sc = getServletContext();
	       // Get the MIME type of the image
	       String mimeType = sc.getMimeType(fileName);
	       if (mimeType == null) {
	           resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	           return;
	       }
	    
	       // Set content type
	       resp.setContentType(mimeType);
	       resp.setContentLength((int)file.length());
	    
	       // Open the file and output streams
	       FileInputStream in = new FileInputStream(file);
	       OutputStream out = resp.getOutputStream();
	    
	       // Copy the contents of the file to the output stream
	       byte[] buf = new byte[1024];
	       int count = 0;
	       while ((count = in.read(buf)) >= 0) {
	           out.write(buf, 0, count);
	       }
	       in.close();
	       out.close();

	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uin = TextUtils.getPathParam(req.getRequestURL().toString(),-1);
		String fileName = TextUtils.getPathParam(req.getRequestURL().toString(),0);
		File dir = new File("screenshot/" + uin +"/");
		File file = new File(dir, fileName);
		file.delete();
		resp.getWriter().write("{}");
		
	}

	


}
