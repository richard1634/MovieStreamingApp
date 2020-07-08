
import java.io.IOException;
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
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// server endpoint URL
@WebServlet("/hero-suggestion")
public class HeroSuggestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/*
	 * populate the Super hero hash map.
	 * Key is hero ID. Value is hero name.
	 */
	public static HashMap<String, String> superHeroMap = new HashMap<>();
	
	static {


	}
    
    public HeroSuggestion() {
        super();
    }

    /*
     * 
     * Match the query against superheroes and return a JSON response.
     * 
     * For example, if the query is "super":
     * The JSON response look like this:
     * [
     * 	{ "value": "Superman", "data": { "heroID": 101 } },
     * 	{ "value": "Supergirl", "data": { "heroID": 113 } }
     * ]
     * 
     * The format is like this because it can be directly used by the 
     *   JSON auto complete library this example is using. So that you don't have to convert the format.
     *   
     * The response contains a list of suggestions.
     * In each suggestion object, the "value" is the item string shown in the dropdown list,
     *   the "data" object can contain any additional information.
     * 
     * 
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
	        
//    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
//    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

			
			// setup the response json arrray
			JsonArray jsonArray = new JsonArray();
			
			// get the query string from parameter
			String query = request.getParameter("query");
			
			// return the empty json array if query is null or empty
			if (query == null || query.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}	
			else {
				//does this need to be full text? I dont think so
				System.out.println("THIS IS A AJAX QUERY");
				String db_query = "SELECT m.id,m.title FROM movies as m WHERE MATCH (m.title) AGAINST('" + query + "*' IN BOOLEAN MODE) limit 50";
				superHeroMap.put("1", "Ghost Rider");
//				superHeroMap.put("3", "Luke Cage");
//				superHeroMap.put("4", "Silver Surfer");
//				superHeroMap.put("5", "Good Morning Surfer");
	    		PreparedStatement statement = dbcon.prepareStatement(db_query);

	    		ResultSet resultSet = statement.executeQuery();
	    		
	    		while (resultSet.next()) {
	    			String movieId = resultSet.getString("m.id");
	    			String movieTitle = resultSet.getString("m.title");
	    			superHeroMap.put(movieId,movieTitle);
	    		}

			}
			// search on superheroes and add the results to JSON Array
			// this example only does a substring match			
			 
			for (String id : superHeroMap.keySet()) {
				String heroName = superHeroMap.get(id);
				if (heroName.toLowerCase().contains(query.toLowerCase())) {
					jsonArray.add(generateJsonObject(id, heroName));
				}
			}
			
			response.getWriter().write(jsonArray.toString());
			return;
		} catch (SQLException ex) {
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
	
	/*
	 * Generate the JSON Object from hero to be like this format:
	 * {
	 *   "value": "Iron Man",
	 *   "data": { "heroID": 11 }
	 * }
	 * 
	 */
	private static JsonObject generateJsonObject(String heroID, String heroName) {
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", heroName);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("heroID", heroID);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}


}
