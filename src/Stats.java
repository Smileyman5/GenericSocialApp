import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Jonathan Baker
 */
@WebServlet("/stats")
public class Stats extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int counter = 0;
	ArrayList<String> users = new ArrayList<String>();
	ArrayList<Long> loginTimes = new ArrayList<Long>();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null;
		Statement state = null;
		String username = (String) request.getSession().getAttribute("username");
		   try {
			   Class.forName("com.mysql.jdbc.Driver");
			   con = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password");
			   state = con.createStatement();
			   state.execute("UPDATE Users SET login=login+1 WHERE username='" + username + "';");
		   } catch (SQLException | ClassNotFoundException e) {
			   e.printStackTrace();
		   } finally {	
			   try {
				if (state != null) { state.close(); }
				if (con != null) { con.close(); }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   
		   }
		 RequestDispatcher dispatcher = request.getRequestDispatcher("/profile");
		 dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
