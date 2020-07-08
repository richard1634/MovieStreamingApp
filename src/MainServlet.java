

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
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
	
		PrintWriter out = response.getWriter();
		out.println("<html>");
        out.println("<h1>Welcome to Fablix</h1>");
        
        out.println("<form method=\"post\" action=\"/your-project-name/BrowseServlet\">");
        out.println("<input type=\"submit\" value=\"Movie Browse\">");
        out.println("</form>");
        out.println("<form method=\"post\" action=\"/your-project-name/index.html\">");
        out.println("<input type=\"submit\" value=\"Movie Search\">");
		out.println("</form>");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
