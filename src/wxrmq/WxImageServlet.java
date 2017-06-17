package wxrmq;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class WxImageServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");	
		File file = new File("../wyl" +req.getPathInfo());
		if (!file.exists()){
			file = new File("../wyl" +req.getPathInfo() +"/");
		}
		if(file.isFile()){
			getSingleFile(file, resp);
		}else if (file.isDirectory()){
			getFiles(file, req.getPathInfo(),resp);
		}
		 
	}
	
	private void getFiles(File dir, String path, HttpServletResponse resp) throws IOException {
		ArrayList<MFile> mFiles = new ArrayList<>();
		for(File file:dir.listFiles()){
			if(file.getName().startsWith("head")){
				continue;
			}
			MFile mFile = new MFile();
			mFile.setDeleteType("DELETE");
			String imgUrl = "wxImage" + path  +"/"+ file.getName();
			mFile.setDeleteUrl(imgUrl);
			mFile.setName(file.getName());
			mFile.setSize(file.length());
			mFile.setUrl(imgUrl);
			mFile.setThumbnailUrl(imgUrl);
			mFiles.add(mFile);
		}
		HashMap<String, Object> list = new HashMap<>();
		list.put("files", mFiles);
		resp.getWriter().write(new Gson().toJson(list));
		
	}

	private void getSingleFile(File file, HttpServletResponse resp) throws IOException{
		ServletContext sc = getServletContext();
	       // Get the MIME type of the image
	       String mimeType = sc.getMimeType(file.getName());
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
		File file = new File("../wyl" +req.getPathInfo());
		if(file.isFile() && file.exists()){
			file.delete();
		}
		resp.getWriter().write("{}");	
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");	
		File file = new File("temp/");
		if(!file.exists()){
			file.mkdirs();
		}
		
		DiskFileItemFactory factory = new DiskFileItemFactory(0, file);
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(10000000);
		ArrayList<MFile> mFiles = new ArrayList<>();
		try {
			List<FileItem> items = upload.parseRequest(req);
			Iterator<FileItem> iter = items.iterator();
			int index =0 ;
			while (iter.hasNext()) {
			    FileItem item = iter.next();
			    if (item.isFormField()) {
			       // processFormField(item, ++index, uin);
			    } else {
			    	processUploadedFile(item, ++index, req.getPathInfo(),mFiles);
			    }
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, Object> list = new HashMap<>();
		list.put("files", mFiles);
		resp.getWriter().write(new Gson().toJson(list));
	}
	
	private void processUploadedFile(FileItem item,int index ,String path, ArrayList<MFile> mFiles) throws Exception {
		String fieldName = item.getFieldName();
	    String fileName = item.getName();
	    String contentType = item.getContentType();
	    boolean isInMemory = item.isInMemory();
	    long sizeInBytes = item.getSize();
	    File dir = new File("../wyl/" + path +"/");
	    if(!dir.exists()){
	    	dir.mkdirs();
	    }
	    File pngFile = new File(dir,fileName);
	    System.out.println(pngFile.getAbsolutePath());
	    item.write(pngFile);
	    
	    MFile mFile = new MFile();
	    mFile.setSize(sizeInBytes);
	    mFile.setName(pngFile.getName());
	    mFile.setDeleteType("DELETE");
	    mFile.setDeleteUrl("wxImage/" + path +"/" + pngFile.getName());
	    mFile.setType("image/jpeg");
	    mFile.setUrl("wxImage/" + path +"/" + pngFile.getName());
	    mFile.setThumbnailUrl("wxImage/" + path +"/" + pngFile.getName());
		mFiles.add(mFile);
	}

	


}
