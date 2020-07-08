import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
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
		
    	//----THIS IS SHOPPING CART DATA STRUCTURE----------
		HashMap<String,Integer> cart = new HashMap<String,Integer>();
        session.setAttribute("cart", cart);
		
    	session.setAttribute("search_num",0);

		PrintWriter out = response.getWriter();
        // HTML
		out.println("<html>");
			out.println("<h1>Fablix</h1>");
    		out.println("<form action=\"/your-project-name/ShoppingCartServlet\"> <input type=\"submit\" value=\"Shopping Cart\"></form>"); 

			out.println("<h2>Search for movies</h2>");
			out.println("<form id=\"search_form\" method=\"post\" action=\"#\">");
			  out.println("Fill out at least ONE field.<br>");
			  out.println("Title<br>");
			  out.println("<input type=\"text\" placeholder=\"Inception\" name=\"title\"><br>");
			  out.println("Year<br>");
			  out.println("<input type=\"text\" placeholder=\"2010\" name=\"year\"><br>");
			  out.println("Director<br>");
			  out.println("<input type=\"text\" placeholder=\"Chris Nolan\" name=\"director\"><br>");
			  out.println("Star's name<br>");
			  out.println("<input type=\"text\" placeholder=\"Leonardo DiCaprio\" name=\"starname\"><br><br>");
			  out.println("<input type=\"submit\" value=\"Search\">");
			out.println("</form>");
		out.println("</html>");
        
        // Sends search terms over via URL
		String title = request.getParameter("title");
		String year = request.getParameter("year");
		String director = request.getParameter("director");
		String starname = request.getParameter("starname");
		
		int numArgs = 0;
		
		String redirect = "/your-project-name/SearchResultsServlet";

		if (request.getParameter("title")==null || !request.getParameter("title").isEmpty())
		{
			if (numArgs==0)
				redirect += "?";
			else
				redirect += "&";
			numArgs+=1;
			redirect = redirect + "title=" + request.getParameter("title");
		}
		
		System.out.println("title " + request.getParameter("title"));
		System.out.println("Redirect: "+ redirect);

		
		if (request.getParameter("title")==null || !request.getParameter("year").isEmpty())
		{
			if (numArgs==0)
				redirect += "?";
			else
				redirect += "&";
			numArgs+=1;
			redirect = redirect + "year=" + request.getParameter("year");
		}
		
		System.out.println("year " + request.getParameter("year"));
		System.out.println("Redirect: "+ redirect);

		
		if (request.getParameter("director")==null || !request.getParameter("director").isEmpty())
		{
			if (numArgs==0)
				redirect += "?";
			else
				redirect += "&";
			numArgs+=1;
			redirect = redirect + "director=" + request.getParameter("director");
		}
		
		System.out.println("director " + request.getParameter("director"));
		System.out.println("Redirect: "+ redirect);

		
		if (request.getParameter("starname")==null || !request.getParameter("starname").isEmpty())
		{
			if (numArgs==0)
				redirect += "?";
			else
				redirect += "&";
			numArgs+=1;
			redirect = redirect + "starname=" + request.getParameter("starname");
		}

		System.out.println("starname " + request.getParameter("starname"));
		System.out.println("Redirect: "+ redirect);

		
		if (request.getParameter("title") == null &&
				request.getParameter("year") == null && 
				request.getParameter("director") == null &&
				request.getParameter("starname") == null)
		{
			out.println("<p>No search terms provided</p>");
		}
		else
		{
			redirect = redirect + "&display=20&sort=rating";
			response.sendRedirect(redirect);
		}
		
		System.out.println("Redirect: "+ redirect);
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
