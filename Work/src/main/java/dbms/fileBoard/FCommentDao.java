package dbms.fileBoard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jakarta.servlet.ServletContext;

public class FCommentDao {

	private Connection conn;
	private ResultSet rs;
	private PreparedStatement psmt;
	//생성자
	public FCommentDao(ServletContext context) {
		try {
			Context ctx = new InitialContext();
			DataSource source= (DataSource)ctx.lookup("java:comp/env/dbms");			
			conn = source.getConnection();
		}
		catch(Exception e) {}
	}
	
	public void close() {
		try {
			if(rs !=null) rs.close();
			if(psmt !=null) psmt.close();
			if(conn !=null) conn.close();
		}
		catch(SQLException e) {}
	}
	
	
	//댓글 레코드 얻기
	public List<FCommentDto> boardCommentRecords(String fno){
		List<FCommentDto> records = new Vector<>();
		String sql="SELECT * FROM (SELECT FCNO,FNO,FCOMMENT,POSTDATE,NAME, RANK() OVER (ORDER BY fcno DESC) AS POSTRANK, F.id FROM FBOARD_COMMENT F JOIN ACCOUNT A ON F.id = A.id WHERE FNO=?)";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, fno);
			rs=psmt.executeQuery();
			while(rs.next()) {
				FCommentDto dto = new FCommentDto();
				dto.setFcno(rs.getString(1));
				dto.setFno(rs.getString(2));
				dto.setFcomment(rs.getString(3));
				dto.setPostdate(rs.getDate(4));
				dto.setName(rs.getString(5));
				dto.setPostrank(rs.getString(6));
				dto.setId(rs.getString(7));
				records.add(dto);
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		return records;
	}
	
	
	
	public int writeOnComment(String fno, String id, String fcomment) {
		int affected=0;
		String sql="INSERT INTO FBOARD_COMMENT(FCNO,FNO,ID,FCOMMENT) VALUES(SEQ_FCNO.NEXTVAL,?,?,?)";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, fno);
			psmt.setString(2, id);
			psmt.setString(3, fcomment);
			affected = psmt.executeUpdate();
		}catch(SQLException e) {e.printStackTrace();}
		return affected;
	}
	
	
	public int editOnComment(String fcno, String fcomment) {
		int affected=0;
		String sql="UPDATE FBOARD_COMMENT SET FCOMMENT = ?  WHERE FCNO=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, fcomment);
			psmt.setString(2, fcno);
			affected = psmt.executeUpdate();
		}catch(SQLException e) {e.printStackTrace();}
		return affected;
	}
	
	public int deleteOnComment(String fcno) {
		int affected=0;
		String sql="DELETE FBOARD_COMMENT WHERE FCNO=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, fcno);
			affected = psmt.executeUpdate();
		}catch(SQLException e) {e.printStackTrace();}
		return affected;
	}
	
}
