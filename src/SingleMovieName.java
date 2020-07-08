

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SingleMovieName
 */
@WebServlet("/SingleMovieName")
public class SingleMovieName extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleMovieName() {
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
//		if (session.getAttribute("username") == null) response.sendRedirect("/your-project-name/LoginServlet");
		//-------------------------------------------------------------------		
        // set response mime type
        response.setContentType("text/html"); 

        // get the printwriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Fabflix</title></head>");
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		// declare statement
    		String title = request.getParameter("title");
    		List<String> catArray = new ArrayList();

    		System.out.println(title);
    		// GENERATES LAG ATM WITHOUT THE COMPLETE QUERY
    		String query = "SELECT m.id,m.title,m.year,m.director, group_concat(DISTINCT genres.name), group_concat(DISTINCT stars.name SEPARATOR ','), r.rating \r\n" + 
    				"	FROM movies as m,ratings as r,genres,genres_in_movies,stars,stars_in_movies \r\n" + 
    				"	WHERE m.id = r.movieId\r\n" + 
    				"	AND r.movieId = genres_in_movies.movieId AND genres_in_movies.movieId = stars_in_movies.movieId \r\n" + 
    				"	AND stars_in_movies.starId = stars.id and genres_in_movies.genreId = genres.id \r\n" + 
    				"	AND genres.name in (SELECT g.name from genres as g, genres_in_movies as gim, movies WHERE movies.id = gim.movieId AND gim.genreId = g.id) \r\n" + 
    				"	AND stars.name in (SELECT s.name from stars as s, stars_in_movies as sim, movies WHERE movies.id = sim.movieId and sim.starId = s.id) ";
    		// execute query
    		
    		
    		if (title!=null)
    		{
    			String[] title_splitted = title.split("\\s+");
    			query += "AND MATCH (m.title) AGAINST ('";			
    			// plus sign after each one? 
    			for (int i=0;i< title_splitted.length; i++) {
    				if (i == title_splitted.length-1) {
    					query += "+"+title_splitted[i] + "*";
    				}
    				else {
    					query += "+"+title_splitted[i] + "*" + " ";
    				}
    			}
    			query += "' IN BOOLEAN MODE) ";		
    			query += "DESC LIMIT ?,?;";		

    		}
    		PreparedStatement statement = connection.prepareStatement(query);
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
    			String movieGenres = resultSet.getString("GROUP_CONCAT(DISTINCT genres.name)");
    			String movieStars = resultSet.getString("group_concat(DISTINCT stars.name SEPARATOR ',')");
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
    		connection.close();
    		
    } catch (Exception e) {
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

