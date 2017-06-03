package wxrmq;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class ScreenShotServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		String uin = TextUtils.getPathParam(req.getRequestURL().toString());
		File dir = new File("screenshot/" + uin +"/");
		ArrayList<MFile> mFiles = new ArrayList<>();
		for(File file:dir.listFiles()){
			MFile mFile = new MFile();
			mFile.setDeleteType("DELETE");
			mFile.setDeleteUrl("screenshot_img/" + uin +"/" + file.getName());
			mFile.setName(file.getName());
			mFile.setSize(file.length());
			mFile.setUrl("screenshot_img/" + uin +"/" + file.getName());
			mFile.setThumbnailUrl("screenshot_img/" + uin +"/" + file.getName());
			mFiles.add(mFile);
		}
		HashMap<String, Object> list = new HashMap<>();
		list.put("files", mFiles);
		resp.getWriter().write(new Gson().toJson(list));

	}

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uin = TextUtils.getPathParam(req.getRequestURL().toString());
		 
		File file = new File("temp/");
		if(!file.exists()){
			file.mkdirs();
		}
		System.out.println(file.getAbsolutePath());
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
			    	processUploadedFile(item, ++index, uin,mFiles);
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


	private void processUploadedFile(FileItem item,int index ,String uin, ArrayList<MFile> mFiles) throws Exception {
		String fieldName = item.getFieldName();
	    String fileName = item.getName();
	    String contentType = item.getContentType();
	    boolean isInMemory = item.isInMemory();
	    long sizeInBytes = item.getSize();
	    File dir = new File("screenshot/" + uin +"/");
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
	    mFile.setDeleteUrl("screenshot_img/" + uin +"/" + pngFile.getName());
	    mFile.setType("image/jpeg");
	    mFile.setUrl("screenshot_img/" + uin +"/" + pngFile.getName());
	    mFile.setThumbnailUrl("screenshot_img/" + uin +"/" + pngFile.getName());
		mFiles.add(mFile);
	}

	private void processFormField(FileItem item,int index ,String uin) throws Exception {

	    
	   
		
	}

}
