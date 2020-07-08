

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

// first off access the url 
// then you can put it into single star


/**
 * Servlet implementation class SingleStar
 */

@WebServlet("/SingleStar")
public class SingleStar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleStar() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// -----------------------------LOGIN-------------------------------
		HttpSession session = request.getSession(true);
//		if (session.getAttribute("username") == null) response.sendRedirect("/your-project-name/LoginServlet");
		//-------------------------------------------------------------------
		
		PrintWriter out = response.getWriter();
		
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
    		
            // set response mime type
            response.setContentType("text/html");

            out.println("<html>");
            out.println("<head><title>Fabflix</title></head>");
//        try {
//    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
//    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		// declare statement
    		
    		String star_name = request.getParameter("name");
    		String query = "SELECT s.id,s.name,s.birthYear,GROUP_CONCAT(m.title),GROUP_CONCAT(m.id) "
    				+ "from stars as s,stars_in_movies as sim,movies as m "
    				+ "WHERE s.name = ? AND s.id = sim.starId AND sim.movieId = m.id "
    				+ "AND m.title in (SELECT movies.title FROM stars,stars_in_movies,movies WHERE stars.id = stars_in_movies.starId AND stars_in_movies.movieId = movies.id ) "
    				+ "AND m.id in (SELECT movies.id FROM stars,stars_in_movies,movies WHERE stars.id = stars_in_movies.starId AND stars_in_movies.movieId = movies.id )";
    		// execute query
    		PreparedStatement statement = dbcon.prepareStatement(query);
    		statement.setString(1,star_name);
    		ResultSet resultSet = statement.executeQuery();


    		out.println("<body>");
    		out.println("<form action=\"/your-project-name/ShoppingCartServlet\"> <input type=\"submit\" value=\"Shopping Cart\"></form>"); 

    		out.println("<h1>MovieDB Stars</h1>");
    		
    		out.println("<table border>");
    		
    		// add table header row
    		out.println("<tr>");
    		out.println("<td>star id</td>");
    		out.println("<td>star name</td>");
    		out.println("<td>star birth year</td>");
    		out.println("<td>movies actor as been in");
    		out.println("</tr>");
    		
    		// add a row for every star result
    		while (resultSet.next()) {
    			// get a star from result set
    			String starID = resultSet.getString("s.id");
    			String starTitle = resultSet.getString("s.name");
    			String birthYear = resultSet.getString("s.birthYear");	
    			String movieList = resultSet.getString("group_concat(m.title)");
    			String[] movie_list = movieList.split(",");
    			String movieidList = resultSet.getString("group_concat(m.id)");
    			String[] movie_id_list = movieidList.split(",");
    			String result = "";
    			for (int i = 0 ; i <movie_list.length;i++) {
    				result += "<a href = SingleMovie?id=" + movie_id_list[i] + ">" + movie_list[i] + "</a> ";
    			}   			
    			out.println("<tr>");
    			out.println("<td>" + starID + "</td>");
    			out.println("<td>" + starTitle + "</td>");
    			out.println("<td>" + birthYear + "</td>");
    			out.println("<td>" + result + "</td>");
    			out.println("</tr>");
    		}
    		
    		out.println("</table>");
    		
    		out.println("</body>");
    		
    		resultSet.close();
    		statement.close();
    		dbcon.close();
    		
    } catch (SQLException ex) {
        ex.printStackTrace();
        while (ex != null) {
            System.out.println("SQL Exception:  " + ex.getMessage());
            ex = ex.getNextException();
        } // end while
    } // end catch SQLException
		
	catch (java.lang.Exception e) {
    		/*
    		 * After you deploy the WAR file through tomcat manager webpage,
    		 *   there's no console to see the print messages.
    		 * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
    		 * 
    		 * To view the last n lines (for example, 100 lines) of messages you can use:
    		 *   tail -100 catalina.out
    		 * This can help you debug your program after deploying it on AWS.
    		 */
    		e.printStackTrace();
    		
    		out.println("<body>");
    		out.println("<p>");
    		out.println("Exception in doGet: " + e.getMessage());
    		out.println("</p>");
    		out.print("</body>");
    }
    
    out.println("</html>");
    out.close();
    
}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
