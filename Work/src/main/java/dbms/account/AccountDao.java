package dbms.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.ServletContext;

public class AccountDao{
	
	private Connection conn;
	private ResultSet rs;
	private PreparedStatement psmt;
	//생성자
	public AccountDao(ServletContext context) {
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
	
	
	
	
	
	////회원 판단
	public boolean isMember(String id ,String password) {
		String sql="SELECT COUNT(*) FROM account WHERE id=? AND password=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, password);
			rs = psmt.executeQuery();
			rs.next();
			if(rs.getInt(1)==0) return false;
		}
		catch(SQLException e) {return false;}
		return true;
	}/////////////
	
	////회원 중복 판단
	public boolean isMemberCheck(String id) {
		String sql="SELECT COUNT(*) FROM account WHERE id=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			rs.next();
			System.out.println(rs.getInt(1));
			if(rs.getInt(1)==0) return false;
		}
		catch(SQLException e) {return false;}
		return true;
	}/////////////
	
	
	public boolean isEmailCheck(String email) {
		String sql="SELECT COUNT(*) FROM account WHERE email=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, email);
			rs = psmt.executeQuery();
			rs.next();
			System.out.println(rs.getInt(1));
			if(rs.getInt(1)==0) return false;
		}
		catch(SQLException e) {return false;}
		return true;
	}/////////////
	
	
	//회원 프로필용 가져오기 
	public AccountDto accountProfile(String loginId) {
		AccountDto dto=null;
		try {
			String sql="SELECT * FROM ACCOUNT WHERE ID = ?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, loginId);
			rs= psmt.executeQuery();
			if(rs.next()) {
				dto=new AccountDto();
				dto.setId(rs.getString(1));
				dto.setPassword(rs.getNString(2));
				dto.setEmail(rs.getString(3));
				dto.setName(rs.getString(4));
				dto.setBirthday(rs.getString(5));
				dto.setGender(rs.getString(6));
				dto.setEducation(rs.getString(7));
				dto.setHobby(rs.getString(8));
				dto.setJoindate(rs.getDate(9));
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		return dto;
	}/////////////////////
	
	
	public void accountEntry(Map<String, String[]> entryAccount) {
		try {
			String sql="INSERT INTO ACCOUNT VALUES(?,?,?,?,?,?,?,?,SYSDATE)";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, entryAccount.get("entryId")[0]);
			psmt.setString(2, entryAccount.get("entryPassword")[0]);
			psmt.setString(3, entryAccount.get("entryEmail")[0]);
			psmt.setString(4, entryAccount.get("entryName")[0]);
			psmt.setString(5, entryAccount.get("entryBirthYear")[0]+","+entryAccount.get("entryMonth")[0]+","+entryAccount.get("entryDay")[0]);
			psmt.setString(6, entryAccount.get("entryGender")[0]);
			psmt.setString(7, entryAccount.get("entryEducation")[0]);
			psmt.setString(8, Arrays.asList(entryAccount.get("entryHobby")).stream().collect(Collectors.joining(",")));
			rs= psmt.executeQuery();
			
			///회원 자기소개 테이블 생성
			sql="INSERT INTO ACCOUNT_INTRODUCE(INO,ID) VALUES(SEQ_INO.NEXTVAL,?)";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, entryAccount.get("entryId")[0]);
			rs= psmt.executeQuery();
		}
		catch(SQLException e) {e.printStackTrace();}
	}//////////////////////
	
	
	//회원 취미 수정
	public void accountHobbyEdit(String id, String hobby) {
		String sql="UPDATE ACCOUNT SET HOBBY=? WHERE ID=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, hobby);
			psmt.setString(2, id);
			psmt.executeUpdate();
		}catch(SQLException e) {e.printStackTrace();}
	}
	
	//회원 탈퇴
	public void accountDelete(String id) {
		try {
			//▶▶▶▶▶▶▶▶▶▶▶▶ 일반 게시글 본인 기록 삭제
			String sql="DELETE (SELECT * FROM BOARD_COMMENT WHERE BNO IN (SELECT BNO FROM BOARD WHERE ID=?))";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.executeUpdate();
			System.out.println("BNO에 있는 본인 댓글 삭제");
			
			sql="DELETE BOARD_COMMENT WHERE ID=?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.executeUpdate();
			System.out.println("다른 BNO에 있는 본인 댓글 삭제");
			
			sql="DELETE BOARD WHERE ID=?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.executeUpdate();
			System.out.println("BNO 글 삭제");
			
			//▶▶▶▶▶▶▶▶▶▶▶▶ 파일 게시글 본인 기록 삭제
			sql="DELETE (SELECT * FROM FBOARD_COMMENT WHERE FNO IN (SELECT FNO FROM FBOARD WHERE ID=?))";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.executeUpdate();
			System.out.println("FNO에 있는 본인 댓글 삭제");
			
			sql="DELETE FBOARD_COMMENT WHERE ID=?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.executeUpdate();
			System.out.println("다른 FNO에 있는 본인 댓글 삭제");
			
			sql="DELETE FBOARD WHERE ID=?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.executeUpdate();
			System.out.println("FNO 글 삭제");
			
			
			
			
			//▶▶▶▶▶▶▶▶▶▶▶▶ 회원 자기소개 삭제
			sql="DELETE ACCOUNT_INTRODUCE WHERE ID=?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.executeUpdate();
			System.out.println("회원 자기소개 삭제");
			
			//▶▶▶▶▶▶▶▶▶▶▶▶ 최종 아이디 삭제
			sql="DELETE ACCOUNT WHERE ID=?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.executeUpdate();
			System.out.println("아이디 삭제");
		}catch(SQLException e) {e.printStackTrace();}
	}
	
}
