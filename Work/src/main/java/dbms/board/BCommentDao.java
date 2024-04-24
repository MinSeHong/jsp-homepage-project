package dbms.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import dbms.account.AccountDto;
import jakarta.servlet.ServletContext;
import model.PagingModel;
import service.DaoService;

public class BCommentDao{
	
	private Connection conn;
	private ResultSet rs;
	private PreparedStatement psmt;
	//생성자
	public BCommentDao(ServletContext context) {
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
	public List<BCommentDto> boardCommentRecords(String bno){
		List<BCommentDto> records = new Vector<>();
		String sql="SELECT * FROM (SELECT CNO,BNO,BCOMMENT,POSTDATE,NAME, RANK() OVER (ORDER BY cno DESC) AS POSTRANK, B.id FROM BOARD_COMMENT B JOIN ACCOUNT A ON B.id = A.id WHERE BNO=?)";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, bno);
			rs=psmt.executeQuery();
			while(rs.next()) {
				BCommentDto dto = new BCommentDto();
				dto.setCno(rs.getString(1));
				dto.setBno(rs.getString(2));
				dto.setBcomment(rs.getString(3));
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
	
	
	
	public int writeOnComment(String bno, String id, String bcomment) {
		int affected=0;
		String sql="INSERT INTO BOARD_COMMENT(CNO,BNO,ID,BCOMMENT) VALUES(SEQ_CNO.NEXTVAL,?,?,?)";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, bno);
			psmt.setString(2, id);
			psmt.setString(3, bcomment);
			affected = psmt.executeUpdate();
		}catch(SQLException e) {e.printStackTrace();}
		return affected;
	}

	public int editOnComment(String cno, String bcomment) {
		int affected=0;
		String sql="UPDATE BOARD_COMMENT SET BCOMMENT = ?  WHERE CNO=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, bcomment);
			psmt.setString(2, cno);
			affected = psmt.executeUpdate();
		}catch(SQLException e) {e.printStackTrace();}
		return affected;
	}

	public int deleteOnComment(String cno) {
		int affected=0;
		String sql="DELETE BOARD_COMMENT WHERE CNO=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, cno);
			affected = psmt.executeUpdate();
		}catch(SQLException e) {e.printStackTrace();}
		return affected;
		
	}
	
}
