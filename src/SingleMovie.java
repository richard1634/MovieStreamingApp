

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class SingleMovie
 */

@WebServlet("/SingleMovie")
public class SingleMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleMovie() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// -----------------------------LOGIN-------------------------------
		HttpSession session = request.getSession(true);
		//-------------------------------------------------------------------

		// get the printwriter for writing response
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
    		String id_num = request.getParameter("id");
    		
    	//	String id_num = "tt0395642";
    		
    		// GENERATES LAG ATM WITHOUT THE COMPLETE QUERY
    		String query = "SELECT m.id,m.title,m.year,m.director, group_concat(DISTINCT g.name), group_concat(DISTINCT s.name),r.rating "
    				+ "from movies as m,ratings as r ,genres as g,genres_in_movies as gim,stars as s,stars_in_movies as sim "
    				+ "WHERE m.id = ? AND r.movieId = m.id AND gim.movieId = r.movieId "
    				+ "AND sim.movieId = gim.movieId AND sim.starId = s.id AND gim.genreId = g.id "
    				+ "AND g.name in (SELECT g2.name from genres as g2, genres_in_movies as gim2, movies as m2 WHERE m2.id = gim2.movieId AND gim2.genreId = g2.id) "
    				+ "AND s.name in (SELECT s3.name from stars as s3, stars_in_movies as sim3, movies as m3 WHERE m3.id = sim3.movieId and sim3.starId = s3.id) ";
    		// execute query
//    		PreparedStatement statement = connection.prepareStatement(query);
    		PreparedStatement statement = dbcon.prepareStatement(query);
    		statement.setString(1,id_num);
    		ResultSet resultSet = statement.executeQuery();

    		out.println("<body>");
    		out.println("<form action=\"/your-project-name/ShoppingCartServlet\"> <input type=\"submit\" value=\"Shopping Cart\"></form>"); 

    		out.println("<h1>MovieDB Movie Information</h1>");
    		
    		out.println("<table border>");
    		
    		// add table header row
    		out.println("<tr>");
    		out.println("<td>id</td>");
    		out.println("<td>title</td>");
    		out.println("<td>year</td>");
    		out.println("<td>director</td>");
    		out.println("<td>genres</td>");
    		out.println("<td>stars</td>");
    		out.println("<td>rating</td>");
    		out.println("</tr>");
    		
    		// add a row for every star result
    		while (resultSet.next()) {
    			// get a star from result set
    			String movieID = resultSet.getString("m.id");
    			String movieTitle = resultSet.getString("m.title");
    			String movieYear = resultSet.getString("m.year");
    			String movieDirector = resultSet.getString("m.director"); 
    			String movieRating = resultSet.getString("r.rating"); 
    			String movieGenres = resultSet.getString("GROUP_CONCAT(DISTINCT g.name)");
    			String movieStars = resultSet.getString("GROUP_CONCAT(DISTINCT s.name)");
    			String[] star_list = movieStars.split(",");
    			
    			String movieStars2 = "";
    			for (String star: star_list) {
    				String enc = URLEncoder.encode(star, "UTF-8");
    				movieStars2 += "<a href = SingleStar?name="+ enc + ">" + star +"</a>  ";
    			}
    			
    			out.println("<tr>");
    			out.println("<td>" + movieID + "</td>");
    			out.println("<td>" + movieTitle + "</td>");
    			out.println("<td>" + movieYear + "</td>");
    			out.println("<td>" + movieDirector + "</td>");
    			out.println("<td>" + movieGenres + "</td>");
    			out.println("<td>" + movieStars2 + "</td>");
    			out.println("<td>" + movieRating + "</td>");
    			out.println("</tr>");
    		}
    		
    		out.println("</table>");
    		
    		out.println("</body>");
    		
    		resultSet.close();
    		statement.close();
//    		connection.close();
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
