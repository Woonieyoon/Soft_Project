package project;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Test {

	public static void main(String[] args) throws ClassNotFoundException {
		Connection conn = null;
		Statement stmt 	= null;
		ResultSet rs 	= null;
	
	
		String driver	= "com.mysql.jdbc.Driver";					// jdbc 드라이버
		String url		= "jdbc:mysql://localhost/project?useSSL=false&autocommit=true"; // url 정보 //드라이버위치
		String uid		= "sungwon";									// 사용자 id
		String pwd1		= "1234";		
		
		Class.forName(driver); 
		try {
			conn = (Connection) DriverManager.getConnection(url, uid, pwd1);
			
			String sql="insert into login"
					+ " values(?,?)";
			
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1,"a");
			pstmt.setString(2,"b");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}

}
