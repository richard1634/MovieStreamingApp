import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
 * Servlet implementation class SearchResultsServlet
 */
@WebServlet("/SearchResultsServlet")
public class SearchResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchResultsServlet() {  
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		// Time an event in a program to nanosecond precision
		long startTime = System.nanoTime();
		long startTimeJ = 0,endTimeJ = 0;
		/////////////////////////////////
		
		PrintWriter out = response.getWriter();
		try {
			// TODO Auto-generated method stub
	      
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
	        
	        startTimeJ = System.nanoTime();
	        Connection dbcon = ds.getConnection();
	        if (dbcon == null)
	            System.out.println("dbcon is null.");
	        
	        // -------------------------------------------------------
		    
	      // set response mime type
	      response.setContentType("text/html"); 
	
	      out.println("<html>");
	      out.println("<head><title>Fabflix</title></head>");
      
//      try { // MOVED UP TRY TO INCL CONNECTION POOLING
//  		Class.forName("com.mysql.jdbc.Driver").newInstance();
  		// create database connection
//  		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	      
  		// declare statement
  		String prn_str ="";
		Integer display_int = 0;
		String sort_str = "";
		List<String> catArray = new ArrayList();
  		
  		// -----------------------session---------------------------  		
  		String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String starname = request.getParameter("starname");

  		//----------------------DISPLAY---------------------------------------
  		if (request.getParameter("display") != null) {
  			display_int = Integer.parseInt(request.getParameter("display"));
  		}
      	// ------------------------PREV AND NEXT ------------------------------
      	int search_num = 0; // (Integer)session.getAttribute("search_num");
      	
  		if (request.getParameter("prn") != null) {
  			prn_str = request.getParameter("prn");
  		}
  		
  		// ------------ SEARCH_STR --------------------------------------------
  		String search_str = "";
  		int numArgs = 0;
		
		if (request.getParameter("title")!=null)
		{
			if (numArgs==0)
				search_str += "?";
			else
				search_str += "&";
			numArgs+=1;
			search_str = search_str + "title=" + request.getParameter("title");
		}
		
		if (request.getParameter("year")!=null)
		{
			if (numArgs==0)
				search_str += "?";
			else
				search_str += "&";
			numArgs+=1;
			search_str = search_str + "year=" + request.getParameter("year");
		}
		
		if (request.getParameter("director")!=null)
		{
			if (numArgs==0)
				search_str += "?";
			else
				search_str += "&";
			numArgs+=1;
			search_str = search_str + "director=" + request.getParameter("director");
		}
		
		if (request.getParameter("starname")!=null)
		{
			if (numArgs==0)
				search_str += "?";
			else
				search_str += "&";
			numArgs+=1;
			search_str = search_str + "starname=" + request.getParameter("starname");
		}
		
		if (request.getParameter("title") == null &&
				request.getParameter("year") == null && 
				request.getParameter("director") == null &&
				request.getParameter("starname") == null)
		{
			out.println("<p>No search terms provided</p>");
		}
  		
  		// ---------------------END OF PREV AND NEXT--------------s-------------
		String query = "SELECT m.id,m.title,m.year,m.director, group_concat(DISTINCT genres.name), group_concat(DISTINCT stars.name SEPARATOR ','), r.rating \r\n" + 
				"	FROM movies as m,ratings as r,genres,genres_in_movies,stars,stars_in_movies \r\n" + 
				"	WHERE m.id = r.movieId\r\n" + 
				"	AND r.movieId = genres_in_movies.movieId AND genres_in_movies.movieId = stars_in_movies.movieId \r\n" + 
				"	AND stars_in_movies.starId = stars.id and genres_in_movies.genreId = genres.id \r\n" + 
				"	AND genres.name in (SELECT g.name from genres as g, genres_in_movies as gim, movies WHERE movies.id = gim.movieId AND gim.genreId = g.id) \r\n" + 
				"	AND stars.name in (SELECT s.name from stars as s, stars_in_movies as sim, movies WHERE movies.id = sim.movieId and sim.starId = s.id) ";

		// START OF CHANGES MADE
		if (title!=null && !title.isEmpty())
		{
			List<String> title_splitted2 = new ArrayList<String>(Arrays.asList(title.split(" ")));			
			for (int i=0;i< title_splitted2.size(); i++) {
				for (int j = 0; j<stopwords.length;j++) {
					if (title_splitted2 != null && title_splitted2.size() !=0) {
						if (i>=title_splitted2.size()) {
							break;
						}
						else {
							if (stopwords[j].equals(title_splitted2.get(i).toLowerCase()) ) {
								if (title_splitted2.size()-1 == 0)
								{
									break;
								}
								else {
									title_splitted2.remove(i);
								}
							}
						}
					}
				}
			}
			query += "AND MATCH (m.title) AGAINST (";			
			for (int i=0;i< title_splitted2.size(); i++) {
				if (i == title_splitted2.size()-1) {
					query +=  "?";
					catArray.add(title_splitted2.get(i));
				}
				else {
					query +=  "?" + " ";
					catArray.add(title_splitted2.get(i));
				}
			}
			query += " IN BOOLEAN MODE) ";
		}		    		    	
		
		if (year!=null && !year.isEmpty())
		{
			query += "AND m.year LIKE ? ";
			catArray.add(year); 
		}
		
		if (director!=null && !director.isEmpty())
		{
			query += "AND m.director LIKE ? ";
			catArray.add(director); 
		}
		
		if (starname!=null && !starname.isEmpty())
		{
			query += "AND stars.name LIKE ? ";
			catArray.add(starname); 
		}
		
		// testing

		for (int i = 0; i < catArray.size(); ++i)
		{
			System.out.println(catArray.get(i));
		}
		    		
		query += "GROUP BY m.id ";
		query += "DESC LIMIT ?,?";		
		System.out.println(query);
//		PreparedStatement statement = connection.prepareStatement(query);
		PreparedStatement statement = dbcon.prepareStatement(query);
		
		for (int i=1; i<=catArray.size(); ++i)
		{
			statement.setString(i, "%" + catArray.get(i-1) + "%"); // For LIKE predicate
			System.out.println(i + catArray.get(i-1));
		}
		
		statement.setInt(catArray.size()+1, search_num);    //offset
		statement.setInt(catArray.size()+2, display_int);  //limit
		
		ResultSet resultSet = statement.executeQuery();
		out.println("<body>");
		out.println("<h2>MovieDB Search Results</h2>");
		
		out.println("<form action=\"/your-project-name/ShoppingCartServlet\"> <input type=\"submit\" value=\"Shopping Cart\"></form>"); 
		out.println("<a href = SearchResultsServlet"+search_str+"&display="+display_int +"&sort="+sort_str+ "&prn=Prev>" + "Prev" + "</a>");
		out.println("<a href = SearchResultsServlet"+search_str+"&display="+display_int +"&sort="+sort_str+ "&prn=Next>" + "Next" + "</a>");
		out.println("<br>");
		out.println("<a href = SearchResultsServlet"+search_str+"&display=10&sort="+sort_str+">" + "Display 10" + "</a>");
		out.println("<a href = SearchResultsServlet"+search_str+"&display=30&sort="+sort_str+">" + "Display 30" + "</a>");
		out.println("<a href = SearchResultsServlet"+search_str+"&display=50&sort="+sort_str+">" + "Display 50" + "</a>");

		out.println("<table border>");
		out.println("<tr>");
		out.println("<td><a href = SearchResultsServlet"+search_str+"&display=" +display_int + "&sort=title>"+ "sortByTitle" + "</a></td>");
		out.println("<td>year</td>");
		out.println("<td>director</td>");
		out.println("<td>list of stars</td>");
		out.println("<td>list of genres</td>");
		out.println("<td><a href = SearchResultsServlet"+search_str+"&display=" +display_int + "&sort=rating>"+ "sortByRating"+ "</a></td>");
		out.println("</tr>");
		while (resultSet.next()) 
		{
			String movieId = resultSet.getString("m.id");
			String movieTitle = resultSet.getString("m.title");
			
			String movieYear = resultSet.getString("m.year");
			String movieDirector = resultSet.getString("m.director");
			
			String movieStars = resultSet.getString("group_concat(DISTINCT stars.name SEPARATOR ',')");
			String[] star_list = movieStars.split(",");
			
			String movieGenres = resultSet.getString("group_concat(DISTINCT genres.name)");
			String movieRating = resultSet.getString("r.rating");
			
			out.println("<tr>");
			String cart_url = "SearchResultsServlet?display=" +display_int + "&sort="+sort_str +"&cart="+movieId;
			out.println("<td>" + "<a href = SingleMovie?id=" + movieId + ">"+ movieTitle + "</a>"+ "<br><a href="+cart_url+">Add to Cart</a>"+ "</td>");
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
//		connection.close();
		dbcon.close();
		endTimeJ = System.nanoTime();

  		
	} catch (SQLException ex) {
        ex.printStackTrace();
        while (ex != null) {
            System.out.println("SQL Exception:  " + ex.getMessage());
            ex = ex.getNextException();
        } // end while
    } // end catch SQLException
		
	catch (java.lang.Exception e) {
		e.printStackTrace();
		out.println("<body>");
		out.println("<p>");
		out.println("Exception in doGet: " + e.getMessage());
		out.println("</p>");
		out.print("</body>");
	}
      out.println("</html>");
      out.close();
      
      /////////////////////////////////
      long endTime = System.nanoTime();
      long elapsedTime = endTime - startTime; // elapsed time in nano seconds. Note: print the values in nano seconds
      long elapsedTimeJ = endTimeJ - startTimeJ;
      
      System.out.println("startTime " + startTime);
      System.out.println("endTime " + endTime);
      System.out.println("elapsedTime " + elapsedTime);
      
      String contextPath = getServletContext().getRealPath("/");
      String xmlFilePath=contextPath+"elapsed_time_logs.txt";

      File file = new File(xmlFilePath);
      boolean file_exists = file.exists();
      if (!file_exists) {
    	  FileWriter fw = new FileWriter(xmlFilePath);

    	  double msTime = (double) elapsedTime / 1_000_000.0; 
    	  double msTimeJ = (double) elapsedTimeJ / 1_000_000.0; 

          fw.write(String.valueOf(msTime));
          fw.write(",");
          fw.write(String.valueOf(msTimeJ));
          fw.write("\n");

          fw.close();
      }
      else {
    	  FileWriter fw = new FileWriter(xmlFilePath, true);

    	  BufferedWriter bw = new BufferedWriter(fw);

    	  double msTime = (double) elapsedTime / 1_000_000.0;
    	  double msTimeJ = (double) elapsedTimeJ / 1_000_000.0;

    	  bw.write(String.valueOf(msTime));
          bw.write(",");
    	  bw.write(String.valueOf(msTimeJ));
          bw.write("\n");

          bw.close();
          fw.close();
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