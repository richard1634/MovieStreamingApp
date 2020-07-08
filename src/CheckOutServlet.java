

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CheckOutServlet
 */
@WebServlet("/CheckOutServlet")
public class CheckOutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckOutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        response.setContentType("text/html");
        
        // -----------------------------LOGIN-------------------------------
        HttpSession session = request.getSession(true);
        if (session.getAttribute("username") == null) response.sendRedirect("/your-project-name/LoginServlet");
        //-------------------------------------------------------------------
       
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Fabflix</title></head>");
        out.println("<h1>Check Out</h1>");
        out.println("<p>Confirm your payment information below.</p>");
        out.println("<form id=\"checkout_form\" method=\"post\" action=\"#\">");
          out.println("Fill out ALL fields.<br>");
          out.println("First name<br>");
          out.println("<input type=\"text\" placeholder=\"Mike\" name=\"firstName\"><br>");
          out.println("Last name<br>");
          out.println("<input type=\"text\" placeholder=\"Tyson\" name=\"year\"><br>");
          out.println("ID<br>");
          out.println("<input type=\"text\" placeholder=\"2342942003240\" name=\"ccID\"><br>");
          out.println("Expiration Date<br>");
          out.println("<input type=\"text\" placeholder=\"2009-09-09\" name=\"expDate\"><br><br>");
          out.println("<input type=\"submit\" value=\"Submit\">");
        out.println("</form>");
        out.println("</html>");
       
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String ccID = request.getParameter("ccID");
        String expDate = request.getParameter("expDate");

        response.setContentType("text/html");
 
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            Statement statement = connection.createStatement();
           
            String query = "SELECT creditcards.id from creditcards";
           
            ResultSet resultSet = statement.executeQuery(query);
            int fail_or_not = 0;
            while (resultSet.next())
            {
                String qID = resultSet.getString("creditcards.id");
               
                if (ccID.equals(qID))
                {
                    System.out.println("SUCCESS");
                    out.println("<p>Order placed.</p>");
                    fail_or_not = 1;
                }

            }
            if (fail_or_not != 1) {
            	out.println("Failed to log in");
            }
                   
        }
        catch (Exception e)
        {
            e.printStackTrace();
            out.println("<body>");
            out.println("<p>");
            out.println("Exception in doGet: " + e.getMessage());
            out.println("</p>");
            out.print("</body>");
        }
       
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
