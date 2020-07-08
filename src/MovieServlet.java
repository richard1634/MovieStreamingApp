import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MovieServlet
 */
@WebServlet("/MovieServlet")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieServlet() {
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
	//	System.out.println(session.getAttribute("username"));
		if (session.getAttribute("username") == null) response.sendRedirect("/your-project-name/LoginServlet");
		//-------------------------------------------------------------------
        
        response.setContentType("text/html"); 
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Fabflix</title></head>");
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		// declare statement
    		Statement statement = connection.createStatement();	
    		//JUST BY RATING
    		String id_num = request.getParameter("id");
    		
    		String query = "SELECT m.id,m.title,m.year,m.director, group_concat(DISTINCT genres.name), group_concat(DISTINCT stars.name SEPARATOR ','),r.rating "
    				+ "from movies as m,ratings as r,genres,genres_in_movies,stars,stars_in_movies "
      				+ "WHERE m.id = r.movieId "
      				+ "AND r.movieId = genres_in_movies.movieId AND genres_in_movies.movieId = stars_in_movies.movieId "
      				+ "AND stars_in_movies.starId = stars.id and genres_in_movies.genreId = genres.id "
      				+ "AND genres.name in (SELECT g.name from genres as g, genres_in_movies as gim, movies WHERE movies.id = gim.movieId AND gim.genreId = g.id) "
    				+ "AND stars.name in (SELECT s.name from stars as s, stars_in_movies as sim, movies WHERE movies.id = sim.movieId and sim.starId = s.id) "
    				+ "GROUP BY m.id ORDER BY r.rating DESC limit 20";
    		
    		ResultSet resultSet = statement.executeQuery(query);
    		out.println("<body>");
    		out.println("<h1>MovieDB MovieList</h1>");
    		out.println("<table border>");
    		out.println("<tr>");
    		out.println("<td>title</td>");
    		out.println("<td>year</td>");
    		out.println("<td>director</td>");
    		out.println("<td>list of stars</td>");
    		out.println("<td>list of genres</td>");
    		out.println("<td>rating</td>");
    		out.println("</tr>");
    		while (resultSet.next()) {
    			// get a star from result set
    			String movieId = resultSet.getString("m.id");
    			String movieTitle = resultSet.getString("m.title");
    			String movieYear = resultSet.getString("m.year");
    			String movieDirector = resultSet.getString("m.director");
    			
    			String movieStars = resultSet.getString("group_concat(DISTINCT stars.name SEPARATOR ',')");
    			String[] star_list = movieStars.split(",");
    			
    			String movieGenres = resultSet.getString("group_concat(DISTINCT genres.name)");
    			String movieRating = resultSet.getString("r.rating");
    			
    			out.println("<tr>");
    			out.println("<td>" + "<a href = SingleMovie?id=" + movieId + ">"+ movieTitle + "</a>"+ "</td>");		
    			out.println("<td>" + movieYear + "</td>");
    			out.println("<td>" + movieDirector + "</td>");
    			
    			String movieStars2 = "";
    			for (String star: star_list) {
    				String enc = URLEncoder.encode(star, "UTF-8");
    				movieStars2 += "<a href = SingleStar?name="+ enc + ">" + star +"</a>  ";
    			}
    			
    			out.println("<td>" + movieStars2 + "</td>");    			
    			out.println("<td>" + movieGenres + "</td>");
    			out.println("<td>" + movieRating + "</td>");
    			out.println("</tr>");
    		}
    		out.println("</table>");
    		out.println("</body>");
    		resultSet.close();
    		statement.close();
    		connection.close();
        }catch (Exception e) {
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
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}