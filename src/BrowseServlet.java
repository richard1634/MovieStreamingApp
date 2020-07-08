

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class BrowseServlet
 */
@WebServlet("/BrowseServlet")
public class BrowseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BrowseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();
		// -----------------------------LOGIN-------------------------------
		HttpSession session = request.getSession(true);
		if (session.getAttribute("username") == null) response.sendRedirect("/your-project-name/LoginServlet");
		//-------------------------------------------------------------------
    	session.setAttribute("title_num",0);
    	session.setAttribute("genre_num",0);
    	//----THIS IS SHOPPING CART DATA STRUCTURE----------
		HashMap<String,Integer> cart = new HashMap<String,Integer>();
        session.setAttribute("cart", cart);
        
        
		out.println("<html>");
        out.println("<h1>Movie Browse</h1>");
        out.println("<h2>Browse By Title</h2>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "A" + "&display=20&sort=rating>"+ "A" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "B" + "&display=20&sort=rating>"+ "B" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "C" + "&display=20&sort=rating>"+ "C" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "D" + "&display=20&sort=rating>"+ "D" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "E" + "&display=20&sort=rating>"+ "E" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "F" + "&display=20&sort=rating>"+ "F" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "G" + "&display=20&sort=rating>"+ "G" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "H" + "&display=20&sort=rating>"+ "H" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "I" + "&display=20&sort=rating>"+ "I" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "J" + "&display=20&sort=rating>"+ "J" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "K" + "&display=20&sort=rating>"+ "K" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "L" + "&display=20&sort=rating>"+ "L" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "M" + "&display=20&sort=rating>"+ "M" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "N" + "&display=20&sort=rating>"+ "N" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "O" + "&display=20&sort=rating>"+ "O" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "P" + "&display=20&sort=rating>"+ "P" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "Q" + "&display=20&sort=rating>"+ "Q" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "R" + "&display=20&sort=rating>"+ "R" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "S" + "&display=20&sort=rating>"+ "S" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "T" + "&display=20&sort=rating>"+ "T" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "U" + "&display=20&sort=rating>"+ "U" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "V" + "&display=20&sort=rating>"+ "V" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "W" + "&display=20&sort=rating>"+ "W" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "X" + "&display=20&sort=rating>"+ "X" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "Y" + "&display=20&sort=rating>"+ "Y" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "Z" + "&display=20&sort=rating>"+ "Z" + "</a>"+ "</td>");
        out.println("<br>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "1" + "&display=20&sort=rating>"+ "1" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "2" + "&display=20&sort=rating>"+ "2" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "3" + "&display=20&sort=rating>"+ "3" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "4" + "&display=20&sort=rating>"+ "4" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "5" + "&display=20&sort=rating>"+ "5" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "6" + "&display=20&sort=rating>"+ "6" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "7" + "&display=20&sort=rating>"+ "7" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "8" + "&display=20&sort=rating>"+ "8" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = TitleServlet?title=" + "9" + "&display=20&sort=rating>"+ "9" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = TitleServlet?title=" + "0" + "&display=20&sort=rating>"+ "0" + "</a>"+ "</td>");	
        out.println("<h2>Browse By Genre</h2>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Action" + "&display=20&sort=rating>"+ "Action" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Adult" + "&display=20&sort=rating>"+ "Adult" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Adventure" + "&display=20&sort=rating>"+ "Adventure" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Animation" + "&display=20&sort=rating>"+ "Animation" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Biography" + "&display=20&sort=rating>"+ "Biography" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Comedy" + "&display=20&sort=rating>"+ "Comedy" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Crime" + "&display=20&sort=rating>"+ "Crime" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Documentary" + "&display=20&sort=rating>"+ "Documentary" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Drama" + "&display=20&sort=rating>"+ "Drama" + "</a>"+ "</td>");
        out.println("<br>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Family" + "&display=20&sort=rating>"+ "Family" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Fantasy" + "&display=20&sort=rating>"+ "Fantasy" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "History" + "&display=20&sort=rating>"+ "History" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Horror" + "&display=20&sort=rating>"+ "Horror" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Music" + "&display=20&sort=rating>"+ "Music" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Musical" + "&display=20&sort=rating>"+ "Musical" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Mystery" + "&display=20&sort=rating>"+ "Mystery" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Reality-TV" + "&display=20&sort=rating>"+ "Reality-TV" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Romance" + "&display=20&sort=rating>"+ "Romance" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Sci-Fi" + "&display=20&sort=rating>"+ "Sci-Fi" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Sport" + "&display=20&sort=rating>"+ "Sport" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Thriller" + "&display=20&sort=rating>"+ "Thriller" + "</a>"+ "</td>");
        out.println("<td>" + "<a href = GenreServlet?genre=" + "War" + "&display=20&sort=rating>"+ "War" + "</a>"+ "</td>");	
        out.println("<td>" + "<a href = GenreServlet?genre=" + "Western" + "&display=20&sort=rating>"+ "Western" + "</a>"+ "</td>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
