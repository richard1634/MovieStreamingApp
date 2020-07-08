

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
 * Servlet implementation class TitleServlet
 */
@WebServlet("/TitleServlet")
public class TitleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TitleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
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
        
//    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
//    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		// declare statement
    		String query="";
    		String title_str ="";	    
    		String num_str ="";
    		Integer display_int= 0;
    		String sort_str ="";
    		// -----------------------------LOGIN-------------------------------
    		HttpSession session = request.getSession(true);
    		if (session.getAttribute("username") == null) response.sendRedirect("/your-project-name/LoginServlet");
    		//-------------------------------------------------------------------
        	//--------------------Shopping cart-------------------------
    		HashMap<String,Integer> cart = (HashMap<String,Integer>)session.getAttribute("cart"); //hashMap
        	if (request.getParameter("cart") != null) {
        		String cart_id = request.getParameter("cart");
        		cart.put(cart_id, 1);
        		System.out.println(cart);
        	}
        	
        	int title_num = (Integer)session.getAttribute("title_num");
        	
 
    		//----------------------DISPLAY---------------------------------------
    		if (request.getParameter("display") != null) {
    			display_int = Integer.parseInt(request.getParameter("display"));
    		}
        	// ------------------------PREV AND NEXT ------------------------------
    		if (request.getParameter("prn") != null) {
    			num_str = request.getParameter("prn");
    		}
    		
    		if (num_str.equals("Prev")) {
    			title_num -=display_int;
    			//if user tries to go back when they're on the first page
    			if (title_num < 0) {
    	    		out.println("<h4>Can't go back:You're on the first page</h4>");

    				title_num +=display_int;
    			}
    			session.setAttribute("title_num", title_num);
    		}
    		else if (num_str.equals("Next")) {
    			title_num += display_int;
    			session.setAttribute("title_num", title_num);
    		}
    		
    		// ---------------------END OF PREV AND NEXT---------------------------

    		//THIS QUERY NEEDS WORK REGEX
    		if (request.getParameter("title") != null) 
    		{
    			title_str = request.getParameter("title");
        		query = "SELECT m.id,m.title,m.year,m.director, group_concat(DISTINCT genres.name), group_concat(DISTINCT stars.name SEPARATOR ','),r.rating "
        				+ "from movies as m,ratings as r,genres,genres_in_movies,stars,stars_in_movies "
          				+ "WHERE m.id = r.movieId and m.title LIKE ?"
          				+ "AND r.movieId = genres_in_movies.movieId AND genres_in_movies.movieId = stars_in_movies.movieId "
          				+ "AND stars_in_movies.starId = stars.id and genres_in_movies.genreId = genres.id "
          				+ "AND genres.name in (SELECT g.name from genres as g, genres_in_movies as gim, movies WHERE movies.id = gim.movieId AND gim.genreId = g.id) "
        				+ "AND stars.name in (SELECT s.name from stars as s, stars_in_movies as sim, movies WHERE movies.id = sim.movieId and sim.starId = s.id) "
        				+ "GROUP BY m.id ORDER BY r.rating DESC limit ?,?";
    		}
    		
        	if (request.getParameter("sort")!= null){
        		sort_str = request.getParameter("sort");
        		if (sort_str.equals("title")) {
            		query = "SELECT m.id,m.title,m.year,m.director, group_concat(DISTINCT genres.name), group_concat(DISTINCT stars.name SEPARATOR ','),r.rating "
            				+ "from movies as m,ratings as r,genres,genres_in_movies,stars,stars_in_movies "
              				+ "WHERE m.id = r.movieId and m.title LIKE ?"
              				+ "AND r.movieId = genres_in_movies.movieId AND genres_in_movies.movieId = stars_in_movies.movieId "
              				+ "AND stars_in_movies.starId = stars.id and genres_in_movies.genreId = genres.id "
              				+ "AND genres.name in (SELECT g.name from genres as g, genres_in_movies as gim, movies WHERE movies.id = gim.movieId AND gim.genreId = g.id) "
            				+ "AND stars.name in (SELECT s.name from stars as s, stars_in_movies as sim, movies WHERE movies.id = sim.movieId and sim.starId = s.id) "
            				+ "GROUP BY m.id ORDER BY m.title DESC limit ?,?";
        		}
        	}
        	
    		// execute query
    		PreparedStatement statement = dbcon.prepareStatement(query);
    		statement.setString(1,title_str + "%");
    		statement.setInt(2, title_num);    		
    		statement.setInt(3, display_int);

    		ResultSet resultSet = statement.executeQuery();

    		
    		out.println("<body>");
    		//Shopping Cart
    		out.println("<form action=\"/your-project-name/ShoppingCartServlet\"> <input type=\"submit\" value=\"Shopping Cart\"></form>"); 
    		out.println("<h1>Movies with Title Starting With: " + title_str+ "</h1>");
    		out.println("<a href = TitleServlet?title="+title_str+"&display="+display_int+"&sort="+sort_str + "&prn=Prev>" + "Prev" + "</a>"); //Prev and next add display
    		out.println("<a href = TitleServlet?title="+title_str+"&display="+display_int+"&sort="+sort_str + "&prn=Next>" + "Next" + "</a>"); 
    		out.println("<br>");
    		out.println("<a href = TitleServlet?title="+title_str+"&display=10&sort="+sort_str+">" + "Display 10" + "</a>");  //gotta get param + or - set attrivute and setInt
    		out.println("<a href = TitleServlet?title="+title_str+"&display=30&sort="+sort_str+">" + "Display 30" + "</a>");
    		out.println("<a href = TitleServlet?title="+title_str+"&display=50&sort="+sort_str+">" + "Display 50" + "</a>");
    		
    		
    		out.println("<table border>");
    		out.println("<tr>");
    		out.println("<td><a href = TitleServlet?title="+title_str+"&display=" +display_int + "&sort=title>"+ "title" + "</a></td>");
    		out.println("<td>year</td>");
    		out.println("<td>director</td>");
    		out.println("<td>list of stars</td>");
    		out.println("<td>list of genres</td>");
    		out.println("<td><a href = TitleServlet?title="+title_str+"&display=" +display_int + "&sort=rating>"+ "rating" + "</a></td>");
    		out.println("</tr>");
    		while (resultSet.next()) {
    			// get a star from result set
    			String movieId = resultSet.getString("m.id");
    			if (movieId == null) {
    	    		out.println("<h4>No More results fam</h4>");

    			}
    			String movieTitle = resultSet.getString("m.title");
    			String movieYear = resultSet.getString("m.year");
    			String movieDirector = resultSet.getString("m.director");
    			
    			String movieStars = resultSet.getString("group_concat(DISTINCT stars.name SEPARATOR ',')");
    			String[] star_list = movieStars.split(",");
    			
    			String movieGenres = resultSet.getString("group_concat(DISTINCT genres.name)");
    			String movieRating = resultSet.getString("r.rating");
    			
    			out.println("<tr>");
    			String cart_url = "TitleServlet?title="+title_str+"&display=" +display_int + "&sort="+sort_str +"&cart="+movieId;

    			out.println("<td>" + "<a href = SingleMovie?id=" + movieId + ">"+ movieTitle +"</a><br><a href="+cart_url+">Add to Cart</a>"+ "</td>");		
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
