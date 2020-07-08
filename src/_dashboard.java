import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

import com.mysql.jdbc.DatabaseMetaData;

import java.sql.CallableStatement;
import java.sql.Types;

/**
 * Servlet implementation class _dashboard
 */
@WebServlet("/_dashboard")
public class _dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public _dashboard() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	HttpSession session = request.getSession(true);
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
        	
    	// IF THEY AREN'T EMPLOYEES GO BACK TO LOG IN SERVLET------------------


        out.println("<h1>EMPLOYEE DASH BOARD</h1>");
		out.println("<br>");

        out.println("<h2>Insert Star</h2>");
        out.println("<html>");
		out.println("<body>");
		out.println("<form id=\"insert_star_form\" method=\"post\" action=\"#\">");
		out.println("<label><b>Insert Star Name</b></label>");
		out.println("<input type=\"text\" placeholder=\"Johnny Sins\" name=\"star_name\">");
		out.println("<br>");
		out.println("<label><b>Birth Year (optional)</b></label>");
		out.println("<input type=\"text\" placeholder=\"1998\" name=\"birth_year\">");
		out.println("<br>");
		out.println("<input type=\"submit\" value=\"Insert Star\">");
		out.println("<br>");
		out.println("</form>");
		
		out.println("<h2>Insert Movie</h2>");
        out.println("<form id=\"insert_movie_form\" method=\"post\" action=\"#\">");
          out.println("Fill out ALL fields.<br>");
          out.println("Title  *<br>");
          out.println("<input type=\"text\" placeholder=\"Inception\" name=\"title\"><br>");
          out.println("Year *<br>");
          out.println("<input type=\"text\" placeholder=\"2010\" name=\"year\"><br>");
          out.println("Director *<br>");
          out.println("<input type=\"text\" placeholder=\"Christopher Nolan\" name=\"director\"><br>");
          out.println("Star name *<br>");
          out.println("<input type=\"text\" placeholder=\"Leonardo DiCaprio\" name=\"star_name2\"><br>");
          out.println("Genre name *<br>");
          out.println("<input type=\"text\" placeholder=\"Action\" name=\"genre_name\"><br>");
          out.println("<input type=\"submit\" value=\"Submit\">");
	      out.println("</form>");
		
        // Insert into stars
        String star_name = request.getParameter("star_name");
        String birth_year = null;
        if(request.getParameter("birth_year") != null) {
        	birth_year = request.getParameter("birth_year");
        }
        
        // Insert into / Update movies
        String title = request.getParameter("title");
		String year = request.getParameter("year");
		String director = request.getParameter("director");
		String star_name2 = request.getParameter("star_name2");
		String genre_name = request.getParameter("genre_name");
        
        int starID_int=0;
        
			try {


	        
	        
//	     // ------------- CONNECTION POOLING --------------------
//	    	
//	    	// the following few lines are for connection pooling
//	        // Obtain our environment naming context
//	    	
//	    	Context initCtx = new InitialContext();
//	    	
//	    	Context envCtx = (Context) initCtx.lookup("java:comp/env");
//	    	if (envCtx == null)
//	            System.out.println("envCtx is NULL");
//	    	
//	    	// Look up our data source
//	        DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
//	        
//	        if (ds == null)
//	            System.out.println("ds is null.");
//	
//	        Connection dbcon = ds.getConnection();
//	        if (dbcon == null)
//	            System.out.println("dbcon is null.");
//	        
//	        // -------------------------------------------------------
	        
	
	//        Connection connection = null;
	        //Have to get highest ID and increment it to make the next star ID
	        
	//        	Class.forName("com.mysql.jdbc.Driver").newInstance();
	//			connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
		        String query = "SELECT MAX(stars.id) as max from stars";
				PreparedStatement statement = dbcon.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
	    			String starID = resultSet.getString("max");
	    			System.out.println(starID);
	    			starID_int = Integer.parseInt(starID.substring(2));
	    			starID_int += 1;  //increment star id to get new one
				} 
	    		resultSet.close();
	    		statement.close();
	    		// Done getting ID for the Insertion
	    		//Now we actually insert into DB			
	        }
	        catch (Exception e) {
	    		e.printStackTrace();
	    		out.println("<body>");
	    		out.println("<p>");
	    		out.println("Exception in doGet: " + e.getMessage());
	    		out.println("</p>");
	    		out.print("</body>");
	        }
	        
	        //insert new star
	        try {
//	        	// ------------- CONNECTION POOLING --------------------
//	        	
//	        	// the following few lines are for connection pooling
//	            // Obtain our environment naming context
//	        	
//	        	Context initCtx = new InitialContext();
//	        	
//	        	Context envCtx = (Context) initCtx.lookup("java:comp/env");
//	        	if (envCtx == null)
//	                System.out.println("envCtx is NULL");
//	        	
//	        	// Look up our data source
//	            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
//	            
//	            if (ds == null)
//	                System.out.println("ds is null.");
//	
//	            Connection dbcon = ds.getConnection();
//	            if (dbcon == null)
//	                System.out.println("dbcon is null.");
//	            
//	            // -------------------------------------------------------
	        	
	        	//if the user enters a birth year, then star_id, enter name and birth year
		        if (birth_year != "" && star_name!=null) {
				        String update_query = "INSERT INTO stars (id,name,birthYear) VALUES (?,?,?) ";	
						PreparedStatement update_statement = dbcon.prepareStatement(update_query);
						update_statement.setString(1, "nm"+ starID_int);
						update_statement.setString(2, star_name);
						update_statement.setString(3, birth_year);
						update_statement.executeUpdate();
						out.println("Insertion Successful<br>");
						update_statement.close();
			        }
		        //if they only entered name, enter the star_id and name
		        else if (birth_year == "" && star_name !=null){
			        String update_query = "INSERT INTO stars (id,name) VALUES (?,?) ";
					PreparedStatement update_statement = dbcon.prepareStatement(update_query);
					update_statement.setString(1, "nm"+ starID_int);
					update_statement.setString(2,star_name);
					update_statement.executeUpdate();
					out.println("Insertion Successful<br>");
					update_statement.close();
		        }
	
			} //try
	        catch (Exception e) {
	    		e.printStackTrace();
	    		out.println("<body>");
	    		out.println("<p>");
	    		out.println("Exception in doGet: " + e.getMessage());
	    		out.println("</p>");
	    		out.print("</body>");
	        }
	        
	        // INSERT MOVIE 
	        
	        try {
//	        	// ------------- CONNECTION POOLING --------------------
//	        	
//	        	// the following few lines are for connection pooling
//	            // Obtain our environment naming context
//	        	
//	        	Context initCtx = new InitialContext();
//	        	
//	        	Context envCtx = (Context) initCtx.lookup("java:comp/env");
//	        	if (envCtx == null)
//	                System.out.println("envCtx is NULL");
//	        	
//	        	// Look up our data source
//	            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
//	            
//	            if (ds == null)
//	                System.out.println("ds is null.");
//	
//	            Connection dbcon = ds.getConnection();
//	            if (dbcon == null)
//	                System.out.println("dbcon is null.");
//	            
//	            // -------------------------------------------------------
	        	
	        	if (title != null && year != null && director != null && star_name2 != null && genre_name != null &&
	        			!title.isEmpty() && !year.isEmpty() && !director.isEmpty() && !star_name2.isEmpty() && !genre_name.isEmpty())
	        	{
	        		String max_query = "SELECT MAX(id) AS max FROM movies"; 
					PreparedStatement statement = dbcon.prepareStatement(max_query);
					ResultSet resultSet = statement.executeQuery();
					resultSet.next();
					String strNumMovies = resultSet.getString("max");
					strNumMovies = strNumMovies.substring(2,strNumMovies.length());
	        		int numMovies = Integer.parseInt(strNumMovies);
	        		
	        		statement.close();
					
	        		CallableStatement cStmt = dbcon.prepareCall("{call add_movie(?, ?, ?, ?, ?)}");
	        		cStmt.setString(1, title);
	        		cStmt.setString(2, year);
	        		cStmt.setString(3, director);
	        		cStmt.setString(4, star_name2);
	        		cStmt.setString(5, genre_name);
	        		
	        		cStmt.execute();
	        		
	        		String max_query2 = "SELECT MAX(id) AS max FROM movies"; 
					PreparedStatement statement2 = dbcon.prepareStatement(max_query2);
					ResultSet resultSet2 = statement2.executeQuery();
					resultSet2.next();
	        		String strNumMovies2 = resultSet2.getString("max");
	        		strNumMovies2 = strNumMovies2.substring(2,strNumMovies2.length());
	        		int numMovies2 = Integer.parseInt(strNumMovies2);
	        		
					if (numMovies >= numMovies2)
					{
						out.println("<p>Error: Movie already exists</p>");
					}
					else
					{
		        		out.println("<p>Successfully inputted movie</p>");
					}
					statement2.close();
					
					cStmt.close();
	        		dbcon.close();
	        	}
	        	else
	        	{
	        		out.println("<p>One or more fields incomplete for Insert Movie</p>");
	        	}
	        	
	        }
	        catch (Exception e) {
	    		e.printStackTrace();
	    		out.println("<body>");
	    		out.println("<p>");
	    		out.println("Exception in doGet: " + e.getMessage()); // only for testing
	    		out.println("</p>");
	    		out.print("</body>");
	        }
	        
	        //NOW IM GOING TO PRINT THE META DATA
	        try {
				out.println("<a href = _dashboard?metadata=true>Database MetaData</a>  ");
				
	
				if (request.getParameter("metadata").equals("true")) {        
					Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
					DatabaseMetaData metadata = (DatabaseMetaData) connection.getMetaData();
					ResultSet rs = metadata.getTables(null, null, null, new String[]{"TABLE"});
	
					out.println("<br>Printing TABLE <br>");
					out.println("----------------------------------<br>");
	
					//GETTING THE TABLES TO BE PRINTED
					while(rs.next())
					{
					    //Print
						String table = rs.getString("TABLE_NAME");
					    out.println("<br>"+table+"<br>");
					    out.println("----------------------------<br>");
					    ResultSet columns = metadata.getColumns(null,null,table,"%");
					    //GETTING THE ATTRIBUTES TO BE PRINTED
					    while (columns.next()) {
					    	String column = columns.getString("COLUMN_NAME");
					    	String data_type = columns.getString("DATA_TYPE");
					    	if (data_type.equals("0")) data_type = "NULL";
					    	else if (data_type.equals("12")) data_type = "VARCHAR";
					    	else if (data_type.equals("91")) data_type = "DATE";
					    	else if (data_type.equals("4")) data_type = "INTEGER";
					    	else if (data_type.equals("7")) data_type = "REAL";
					    	out.println(column+ " " +data_type+"<br>");
					    	
					    }
	
					}
				}
				
	        }
	        catch(Exception e) {
	        	System.out.println("metadata: " + e.getMessage());
	        }
	        
			out.print("</body>");
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

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}