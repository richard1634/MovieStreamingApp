
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String user_type="";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void service(HttpServletRequest request, HttpServletResponse   response) throws ServletException, IOException {
        doPost(request, response);
}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
    	HttpSession session = request.getSession(true);

        out.println("<html>");
		out.println("<head><title>Log-In</title></head>");
		out.println("<h1>Log In to Fablix</h1>");

		out.println("<body>");
		out.println("<form id=\"login_form\" method=\"post\" action=\"#\">");
		out.println("<label><b>Username</b></label>");
		out.println("<input type=\"text\" placeholder=\"Enter Username\" name=\"username\">");
		out.println("<br>");
		out.println("<label><b>Password</b></label>");
		out.println("<input type=\"password\" placeholder=\"Enter Password\" name=\"password\">");
		out.println("<br>");
		out.println("<html>\r\n" + 
				"  <head>\r\n" + 
				"    <title>reCAPTCHA demo: Simple page</title>\r\n" + 
				"     <script src=\"https://www.google.com/recaptcha/api.js\" async defer></script>\r\n" + 
				"  </head>\r\n" + 
				"  <body>\r\n" + 
				"    <form action=\"?\" method=\"POST\">\r\n" + 
				"      <div class=\"g-recaptcha\" data-sitekey=\"6LeVfJAUAAAAAADTNIl8zGj8H-rVeFPy6cUYyFrA\r\n" + 
				"\"></div>\r\n" + 
				"      <br/>\r\n" + 
				"      <input type=\"submit\" value=\"Login\">\r\n" + 
				"    </form>\r\n" + 
				"  </body>\r\n" + 
				"</html>");
		out.println("</form>");
		out.println("</body>");
        out.println("<head><title>Fabflix</title></head>");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        response.setContentType("text/html"); 

        int captcha_bool = 0;
        //so it wont display incorrect pass all the time
        try 
        {        	
        	String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
	        System.out.println(verifyCredentials(username,password));
		        if (verifyCredentials(username,password) == true && user_type == "user")
		        {
		        	session.setAttribute("username", username);
		        	response.sendRedirect("/your-project-name/MainServlet");
		        }
		        
		        else if (verifyCredentials(username,password) == true && user_type == "employee")
		        {
		        	session.setAttribute("employee_username", username);
		        	response.sendRedirect("/your-project-name/_dashboard");
		        }				
        }
        catch (Exception e) {
        	e.printStackTrace();}   	
    	if (captcha_bool == 1) out.println("<h3>Do captcha <h3>");
    }
        

    
private static boolean verifyCredentials(String email, String password) throws Exception {
	boolean success = false;
	try {		
	    	// ------------- CONNECTION POOLING --------------------
	    	
	    	// the following few lines are for connection pooling
	        // Obtain our environment naming context
	    	
	    	Context initCtx = new InitialContext();
	    	
	    	Context envCtx = (Context) initCtx.lookup("java:comp/env");
	    	if (envCtx == null)
	            System.out.println("envCtx is NULL");
	    	
	    	// Look up our data source
	        DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
	        
	        if (ds == null)
	            System.out.println("ds is null.");
	
	        Connection dbcon = ds.getConnection();
	        if (dbcon == null)
	            System.out.println("dbcon is null.");
	        
	        // -------------------------------------------------------
	    	
	    	
			//		Class.forName("com.mysql.jdbc.Driver").newInstance();
			//		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			//		Statement statement = connection.createStatement();
			
			Statement statement = dbcon.createStatement();
	
	
			String query = String.format("SELECT * from employees where email='%s'", email);
			
	
			ResultSet rs = statement.executeQuery(query);
				
			if (rs.next()) { // CHECKS EMPLOYEE TABLE
				// get the encrypted password from the database
				String encryptedPassword = rs.getString("password");
				
				// use the same encryptor to compare the user input password with encrypted password stored in DB
				success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
				user_type = "employee";
			}
			else if (success== false){
				//maybe make a new connection
				rs.close();
				query = String.format("SELECT * from customers where email='%s'", email);
				rs = statement.executeQuery(query);
				if (rs.next()) {
					String encryptedPassword = rs.getString("password");
					success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
					user_type = "user";
	
				}
			}
	
			rs.close();
			statement.close();
			dbcon.close();
	//		connection.close();
	}
	
	catch (SQLException ex) {
        ex.printStackTrace();
        while (ex != null) {
            System.out.println("SQL Exception:  " + ex.getMessage());
            ex = ex.getNextException();
        } // end while
    } // end catch SQLException
	
    catch (java.lang.Exception ex) {
        System.out.println(ex.getMessage());
    }
		System.out.println("verify " + email + " - " + password);
		return success;
	}
    
}
