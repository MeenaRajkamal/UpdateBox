package com.project;
import com.project.StorePojo;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;
import com.project.PMF;
import com.project.PojoClass;
@Controller
public class StoreDetails{


	@RequestMapping(value="/login_google")
	public ModelAndView go(){
		return new ModelAndView("redirect:https://accounts.google.com/o/oauth2/auth?redirect_uri=http://localhost:8888/get_authz_code&response_type=code&client_id=501718494250-bqd8e1sl5cm36r3ontnaqdvgbst53lb4.apps.googleusercontent.com&approval_prompt=force&scope=email&access_type=online");
	}
	@SuppressWarnings({ "unused" })
	@RequestMapping(value="/get_authz_code")
	public ModelAndView get_authorization_code(@RequestParam String code, HttpServletResponse resp) throws IOException{
		
		//code for getting authorization_code
		
		System.out.println("successfully came back...............");
		String auth_code=code;
		System.out.println(auth_code);
		
		//Code for getting access token from the authorization_code.....
		
		URL url=new URL("https://www.googleapis.com/oauth2/v3/token?"
				+ "client_id=501718494250-bqd8e1sl5cm36r3ontnaqdvgbst53lb4.apps.googleusercontent.com"
				+ "&client_secret=TO3C7Ade4itcJ4vpZGm231O-&"
				+ "redirect_uri=http://localhost:8888/get_authz_code&"
				+ "grant_type=authorization_code&"
				+ "code="+auth_code);
		HttpURLConnection connect=(HttpURLConnection) url.openConnection();
		connect.setRequestMethod("POST");
		connect.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		connect.setDoOutput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
		String inputLine;
		String response = "";
		while ((inputLine = in.readLine()) != null) {
			response+=inputLine;
		}
		in.close();
		System.out.println(response.toString());
		JSONParser parser = new JSONParser();
		JSONObject json_access_token = null;
		try {
			json_access_token = (JSONObject) parser.parse(response);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String access_token = (String) json_access_token.get("access_token");
		System.out.println("Access token ="+access_token);
		System.out.println("access token caught");
		
		//code for getting user details by sending access token.......
		
		URL obj1 = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token="+access_token);
		HttpURLConnection conn = (HttpURLConnection) obj1.openConnection();
	    BufferedReader in1 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine1;
		String responsee = "";
		while ((inputLine1 = in1.readLine()) != null) {
			responsee+=inputLine1;
		}
		in1.close();
		System.out.println(responsee.toString());
		JSONObject json_user_details = null;
		try {
			json_user_details = (JSONObject) parser.parse(responsee);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		
		//code for sending accessed user details to display.jsp
		
		String emaill= (String) json_user_details.get("email");
	    String name=(String)  json_user_details.get("name");
	    String id=(String)  json_user_details.get("id");
	    PersistenceManager pm= PMF.get().getPersistenceManager();
	    javax.jdo.Query query = pm.newQuery(PojoClass.class); 
	    PojoClass objPojo = new PojoClass();
	    objPojo.setEmail(emaill);
		objPojo.setUname(name);
		objPojo.setId(id);
		pm.makePersistent(objPojo);
		query.setFilter(" email == '" + emaill + "'");	
		List<PojoClass> results = (List<PojoClass>) query.execute(emaill);
		if(!(results.isEmpty()))
		{
		
		}	else{
				objPojo.setEmail(emaill);
			objPojo.setUname(name);
			objPojo.setId(id);

		try {
		pm.makePersistent(objPojo);
		} finally {
		pm.close();
		}

		
		}
		/*Query queryobj= pm.newQuery(PojoClass.class,”email==‘”+emaill+”’”);
		List<PojoClass> list=(List<PojoClass>) queryobj.execute();
		if(list()>0){
		
		}
		else{
			objPojo.setEmail(emaill);
			objPojo.setUname(name);
			objPojo.setId(id);
			pm.makePersistent(objPojo);

		
		}*/
		
		/*javax.jdo.Query queryobj= pm.newQuery(PojoClass.class);
		queryobj.setFilter("email==emaill");
		List<PojoClass> list=(List<PojoClass>) queryobj.execute();
		if(list.size()>0)
		{
			
		}
		else
			objPojo.setEmail(emaill);
			objPojo.setUname(name);
			objPojo.setId(id);
			pm.makePersistent(objPojo);
			System.out.println("different");*/
		
		
		
		
		
/*List<PojoClass> list = (List<PojoClass>)query.execute();
		  	if(!list.isEmpty()){
				for(PojoClass obj : list){
		String email =(String)obj.getEmail();
		System.out.println("Stored mail id are.."+email);
		System.out.println("Current mail id is.."+emaill);
		if(emaill.equals(email))
		{
			
		}
		else 
		{
			objPojo.setEmail(emaill);
			objPojo.setUname(name);
			objPojo.setId(id);
			pm.makePersistent(objPojo);   
		
		}

}
				
				
}*/
		  	return new ModelAndView("display.jsp?nme="+json_user_details.get("name")+"&mail="+json_user_details.get("email")+"&pic="+	json_user_details.get("picture"));	 			
	     
	}
	@RequestMapping(value = "/loadupdate", method = RequestMethod.POST)//To store the user updates with date
	public void loadUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		ObjectMapper mapper = new ObjectMapper();
		//StorePojo sp=createJson();
		PersistenceManager pm1 = PMF.get().getPersistenceManager();
		javax.jdo.Query q= pm1.newQuery(StorePojo.class);
		q.setOrdering("date desc");//To display updates in the descending order of date
		List<StorePojo> results = (List<StorePojo>) q.execute();
		JSONObject obj = new JSONObject();
				if(!results.isEmpty())
		{
			
			for(StorePojo sp : results)
			{
			/*String update =(String)sp.getUpdate();
				String email =(String)sp.getEmail();
				String timeinfo=(String)sp.getDate();
*/			sp.getUpdate();
				System.out.println("updates are  "+sp.getUpdate());
				sp.getDate();
				sp.getEmail();
				//return sp;
				String jsonInString = mapper.writeValueAsString(sp);
				System.out.println(jsonInString);
				out.println(jsonInString);
	
				mapper.writeValue(new File("c:\\file.json"), sp);
				
				/*out.print("<p style=color:blue;text-align:left;font-size:25>"+email+"</p>");
				out.print("<p style=color:red;text-align:right;font-size:25>"+timeinfo+"</p>");
				out.print("<p style=color:black;text-align=left;font-size:20>"+update+"</p>");
				out.print("<hr>");*/
			/*obj.put("update",update);
			obj.put("email",email);
			obj.put("timeinfo", timeinfo);*/
			}
		}
		
	

		
		//return new ModelAndView("display.jsp?"+obj);
		
	}
	
		
	
	@RequestMapping(value = "/store", method = RequestMethod.POST)//To store the user updates with date
	public  void addUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/*DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance(); 
		dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));*/
		String updates = request.getParameter("update");
		String eemail=request.getParameter("mail");
		String date=request.getParameter("dateinfo");
		StorePojo s = new StorePojo();
		s.setUpdate(updates);
		s.setEmail(eemail);
		System.out.println("persisted email"+eemail);
		s.setDate(date);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try 
		{
			pm.makePersistent(s);
		} 
		finally 
		{
		
			pm.close();
		}
		/*
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		PersistenceManager pm1 = PMF.get().getPersistenceManager();
		javax.jdo.Query q= pm1.newQuery(StorePojo.class);
		q.setOrdering("date desc");//To display updates in the descending order of date
		List<StorePojo> results = (List<StorePojo>) q.execute();
		if(!results.isEmpty())
		{
			for(StorePojo sp : results)
			{
				String update =(String)sp.getUpdate();
				String email =(String)sp.getEmail();
				String timeinfo=(String)sp.getDate();
				out.print("<p style=color:blue;text-align:left;font-size:25>"+email+"</p>");
				out.print("<p style=color:red;text-align:right;font-size:25>"+timeinfo+"</p>");
				out.print("<p style=color:black;text-align=left;font-size:20>"+update+"</p>");
				out.print("<hr>");
			}
		}*/
	}
	@RequestMapping(value = "/signinusers", method = RequestMethod.POST)//Display the updates of all the signed in users
	public void displayUsers(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		PersistenceManager pm1 = PMF.get().getPersistenceManager();
		javax.jdo.Query q= pm1.newQuery(PojoClass.class);
		List<PojoClass> results = (List<PojoClass>) q.execute();
		if(!results.isEmpty())
		{
			for(PojoClass obj : results)
			{
				String email =(String)obj.getEmail();
				PrintWriter out=response.getWriter();
				response.setContentType("text/html");
				out.print("<br>"+email+"<br>");

			}
		}
	}
	@RequestMapping(value = "/getupdate", method = RequestMethod.POST)//To fetch the updates in user-wise order
	public void getUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException	
	{
		String emailid=request.getParameter("mail");
		System.out.println("left pane"+emailid);
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		PersistenceManager pm1 = PMF.get().getPersistenceManager();
		javax.jdo.Query q= pm1.newQuery(StorePojo.class);
		q.setOrdering("date desc");
		List<StorePojo> results = (List<StorePojo>)q.execute();
		out.print("Showing updates of "+emailid);
		if(!results.isEmpty())
		{
			for(StorePojo sp : results)
			{
				String email =(String)sp.getEmail();
				String update =(String)sp.getUpdate();
				String timeinfo=(String)sp.getDate();
				if(email.equals(emailid))
				{
					out.print("<p style=color:hotpink;text-align:right;font-size:25>"+timeinfo+"</p>");
					out.print("<p style=color:black;text-align=left;font-size:20>"+update+"</p>");
					out.print("<hr>");
		
				}
		
			}
		}	
	}
}

	
