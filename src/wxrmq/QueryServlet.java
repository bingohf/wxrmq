package wxrmq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional.TxType;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xmlconfig.NamespaceList.Member2.Item;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.result.Output;

import com.google.gson.Gson;
import com.oracle.webservices.internal.api.databinding.DatabindingFactory;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;
import com.sun.net.httpserver.Headers;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.org.apache.xpath.internal.operations.And;
import com.sun.swing.internal.plaf.basic.resources.basic;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

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
import wxrmq.data.remote.QueryReturn;
import wxrmq.data.remote.RmqValue;
import wxrmq.data.remote.UUIDResponse;
import wxrmq.domain.Account;
import wxrmq.domain.WxUser;
import wxrmq.domain.WxUser_Tag;
import wxrmq.utils.NetWork;
import wxrmq.utils.TextUtils;

public class QueryServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		req.setCharacterEncoding("UTF-8");
		String start = req.getParameter("start");
		String count = req.getParameter("count");
		String format = req.getParameter("format");
		boolean isAdmin = format != null && format.equals("excel");
	
		if(TextUtils.isEmpty(format)){
			format = "json";
		}
		if(TextUtils.isEmpty(start)){
			start ="0";
		}
		if(TextUtils.isEmpty(count)){
			count ="500";
		}
		
		String keyword = req.getParameter("keyword");
		if(!TextUtils.isEmpty(keyword)){
			keyword = keyword.replaceAll("'", "''");
		}
		String quota =  req.getParameter("quota");
		String friendsCount = req.getParameter("friendsCount");
		String city = req.getParameter("city");
		String sex = req.getParameter("sex");
		String industry = req.getParameter("industry");
	    String sql = "select uin,nickName,Sex,FriendsCount,city,age,industry,malePercent,province, wxid ,quota"
	            + " from WxUser a  where 1=1 ";
	    
	    if(!TextUtils.isEmpty(keyword)){
	    	keyword ="'%" + keyword +"%'";
	    	sql += " and CONCAT(ifnull(city,''), ifnull(province,''), nickname,ifnull(memo,''), ifnull(industry,'')) like " + keyword ;
	    }
	    if(!TextUtils.isEmpty(quota)){
	    	sql += " and " + replaceParam(quota, "quota");
	    }
	    if(!TextUtils.isEmpty(city)){
	    	String tagcity = replaceParam(city, "label") + " or " + replaceParam(city, "subLabel");
	    	if (!TextUtils.isEmpty(friendsCount)){
	    		sql += " and  exists (select 1 from wxUser_Tag b where a.uin= b.uin and " +  replaceParam(friendsCount, "count") + " and (" + tagcity +"))";
	    	} else{
	    		sql += " and  (province = " + replaceParam(city, "province") + " or city = " + replaceParam(city, "city") +" or exists(select 1 from wxuser_tag b where a.uin=b.uin and (" + tagcity + ")))";
	    	}
	    }else{
	    	if (!TextUtils.isEmpty(friendsCount)){
	    		sql += " and  " + replaceParam(friendsCount, "friendsCount") ;
	    	}
	    }	
	    if(!TextUtils.isEmpty(sex)){
	    	sql += " and " + replaceParam(sex, "malePercent");
	    }
	    if(!TextUtils.isEmpty(industry)){
	    	sql += " and exists(select 1 from wxUser_tag b where a.uin=b.uin and type= 1 and " + replaceParam(industry, "label") +")";
	    }
		resp.setCharacterEncoding("UTF-8");
		
		QueryReturn queryReturn = new QueryReturn();
		
		String totalCount = "select count(1) totalCount from (" + sql +") a";
		List totalResult = RmqDB.sqlQuery(totalCount);
		Object totalRow = totalResult.get(0);
		
		queryReturn.setTotalCont(((BigInteger)totalRow).intValue());
		
		List<Object[]> result = RmqDB.sqlQuery(sql + "  order by dataFrom,friendsCount desc LIMIT " + start +"," + count);
	
		
		for (Object[] row : result) {
			QueryReturn.Item item = new QueryReturn.Item();
			item.setUin((String) row[0]);
			item.setNickName((String)row[1]);
			item.setSex(((BigInteger)row[2]).intValue());
			item.setFriendsCount(((BigInteger)row[3]).intValue());
			item.setCity((String)row[4]);
			item.setAge((Integer)row[5]);
			item.setIndustry((String)row[6]);

			item.setMalePercent(((BigDecimal )row[7]).floatValue());
			item.setProvince((String)row[8]);
			if(isAdmin){
				item.setWxid((String)row[9]);
			}else{
				item.setWxid(TextUtils.markText((String)row[9]));
			}
			if(row[10]!= null ){
				item.setQuota(((BigDecimal )row[10]).floatValue());
			}
			queryReturn.getItems().add(item);
		}
		output(format, resp, queryReturn);
		

	}
	

	
	private void output(String format, HttpServletResponse resp, QueryReturn queryReturn) throws IOException {
		if(format.equals("json")){
			output_json(resp, queryReturn);
		}else if(format.equals("excel")){
			output_excel(resp, queryReturn);
		}
		
	}



	private void output_excel(HttpServletResponse resp, QueryReturn queryReturn) throws IOException {
		resp.reset();          
		resp.setContentType("application/vnd.ms-excel");        //改成输出excel文件
		resp.setHeader("Content-disposition","attachment; filename=output.xlsx" );
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("report");
		
		
		Iterator<QueryReturn.Item> iterator = queryReturn.getItems().iterator();
		int rowIndex =0;
		int col = 0;
		XSSFRow row = sheet.createRow(rowIndex++);
		row.createCell(col ++).setCellValue("微信号");
		row.createCell(col ++).setCellValue("昵称");
		row.createCell(col ++).setCellValue("性别");
		row.createCell(col ++).setCellValue("年龄");
		row.createCell(col ++).setCellValue("地区");
		row.createCell(col ++).setCellValue("报价");
		row.createCell(col ++).setCellValue("好友数");
		row.createCell(col ++).setCellValue("行业");
		while(iterator.hasNext()){
			QueryReturn.Item item = iterator.next();
			row = sheet.createRow(rowIndex++);
			col = 0;
			row.createCell(col ++).setCellValue(item.getWxid());
			row.createCell(col ++).setCellValue(item.getNickName());
			row.createCell(col ++).setCellValue(TextUtils.sexToString(item.getSex()));
			if(item.getAge() != null){
				row.createCell(col++).setCellValue(item.getAge());
			}else{
				col ++;
			}
			row.createCell(col ++).setCellValue(item.getCity());
			if(item.getQuota() != null){
				row.createCell(col++).setCellValue(item.getQuota());
			}else{
				col ++;
			}
			row.createCell(col++).setCellValue(item.getFriendsCount());
			row.createCell(col++).setCellValue(item.getIndustry());
		}
		
		ServletOutputStream op = resp.getOutputStream();
		wb.write(op);
		op.flush();
		op.close();
		
		
	}

	

	private void output_json(HttpServletResponse resp, QueryReturn queryReturn) throws IOException {
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.getWriter().write(new Gson().toJson(queryReturn));
		
	}



	private String replaceParam(String qString,String paramName){
		return qString.replaceAll("\\(param\\)", paramName);
	}

}
