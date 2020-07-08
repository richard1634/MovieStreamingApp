

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ShoppingCartServlet
 */
@WebServlet("/ShoppingCartServlet")
public class ShoppingCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingCartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
        response.setContentType("text/html"); 

        PrintWriter out = response.getWriter();
		// -----------------------------LOGIN-------------------------------
		HttpSession session = request.getSession(true);
		if (session.getAttribute("username") == null) response.sendRedirect("/your-project-name/LoginServlet");
		//-------------------------------------------------------------------
        out.println("<html>");
        out.println("<head><title>Fabflix</title></head>");
		out.println("<form action=\"/your-project-name/CheckOutServlet\"> <input type=\"submit\" value=\"Check Out\"></form>"); 

		out.println("<h1>Shopping Cart</h1>");
		HashMap<String,Integer> cart = (HashMap<String,Integer>)session.getAttribute("cart"); //hashMap
		
		if (request.getParameter("Delete") != null) {
			String delete_id = request.getParameter("Delete");
			Integer delete_int = cart.get(delete_id) -1;
			cart.put(delete_id, delete_int);
			if (delete_int <= 0) { cart.remove(delete_id);}
			System.out.println(cart);
		}
		
		if (request.getParameter("amount")!= null) {
			Integer amount_id = Integer.valueOf(request.getParameter("amount"));
			String movie_id = request.getParameter("id");

			if (amount_id<0) {
				out.println("Invalid Input, Greater than 0 please<br>");
			}
			else if (amount_id == 0) {
				cart.remove(movie_id);

			}
			else {
			cart.put(movie_id, amount_id);}
		}
		Iterator<Map.Entry<String,Integer>> cartIterator = cart.entrySet().iterator();

		while (cartIterator.hasNext()) {
			Map.Entry<String,Integer> next = cartIterator.next();
			Integer value = next.getValue();
			out.println("MovieID = " + next.getKey()+ " | Amount = " + value);

			out.println("<a href=/your-project-name/ShoppingCartServlet?Delete="+next.getKey()+">Delete</a><br>");
		

			out.println("<form action=\"#\">");
			out.println("<input type=\"text\" placeholder=\"Enter Amount\" name=\"amount\">");
			out.println("<input type=\"text\" name=\"id\" value="+next.getKey()+">");
			out.println("<br>");
			out.println("<input type=\"submit\" value=\"Submit\">");
			out.println("</form>");
			String movie_id = request.getParameter("id");
			String amount_id = request.getParameter("amount");
			System.out.println(amount_id);
			System.out.println(cart);
		    System.out.println(movie_id);
		}
		out.println("</table>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
