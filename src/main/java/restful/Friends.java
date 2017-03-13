package restful;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Jonathan Baker
 */
public class Friends extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String username = request.getParameter("username");
		connect("UPDATE Friends SET status='Confirmed' WHERE username='" + name.trim() + "' AND friend='" + username + "';");
        connect("INSERT INTO Friends VALUES ('" + username + "', '" + name.trim() + "', 'Confirmed');");
        
        response.setStatus(HttpServletResponse.SC_OK);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String username = request.getParameter("username");
		connect("DELETE FROM Friends WHERE username='" + name.trim() + "' AND friend='" + username + "';");
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String username = request.getParameter("username");
		connect("INSERT INTO Friends VALUES ('" + username + "', '" + name.trim() + "', 'Pending');");
		
		response.setStatus(HttpServletResponse.SC_OK);
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
