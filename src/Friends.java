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

/*
 * @author Jonathan Baker
 */

@WebServlet("/friends")
public class Friends extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String confirm = request.getParameter("confirm");
		String name = request.getParameter("name");
		String decline = request.getParameter("decline");
		String friend = request.getParameter("friend");
		String username = (String) request.getSession().getAttribute("username");
		if (friend != null && friend.equals("true")) {
			connect("INSERT INTO Friends VALUES (NULL, '" + username + "', '" + name.trim() + "', 'Pending');");
		}
		else if (confirm != null && confirm.equals("true")) {
			connect("UPDATE Friends SET status='Confirmed' WHERE username='" + name.trim() + "' AND friend='" + username + "';");
            connect("INSERT INTO Friends VALUES (NULL, '" + username + "', '" + name.trim() + "', 'Confirmed');");
		}
		else if (decline != null && decline.equals("true")) {
			connect("DELETE FROM Friends WHERE username='" + name.trim() + "' AND friend='" + username + "';");
		}
		getServletContext().getRequestDispatcher("/profile").forward(request, response);
	}
	
	private void connect(String sql) {
		Connection con = null;
		Statement state = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password");
			state = con.createStatement();
			state.execute(sql);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (state != null) { state.close(); }
				if (con != null) { con.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

}
