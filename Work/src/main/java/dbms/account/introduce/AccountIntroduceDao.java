package dbms.account.introduce;

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

import dbms.account.AccountDto;
import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.ServletContext;

public class AccountIntroduceDao{
	
	private Connection conn;
	private ResultSet rs;
	private PreparedStatement psmt;
	//생성자
	public AccountIntroduceDao(ServletContext context) {
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
	
	
	//회원 소개 테이블 생성
	public AccountIntroduceDto accountIntroduce(String id) {
		AccountIntroduceDto dto=null;
		try {
			String sql="SELECT * FROM ACCOUNT_INTRODUCE WHERE ID=?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs= psmt.executeQuery();
			if(rs.next()) {
				dto=new AccountIntroduceDto();
				dto.setIno(rs.getString(1));
				dto.setId(rs.getString(2));
				dto.setPnumber(rs.getString(3)==null?"":rs.getString(3));
				dto.setIntroduce(rs.getString(4)==null?"":rs.getString(4));
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		return dto;
	}/////////////////////
	
	
	
	
	//회원 소개 수정
	public void accountIntroduceEdit(String id, String pnumber, String introduce) {
		String sql="UPDATE ACCOUNT_INTRODUCE SET PNUMBER=?, INTRODUCE=? WHERE ID=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, pnumber);
			psmt.setString(2, introduce);
			psmt.setString(3, id);
			psmt.executeUpdate();
		}catch(SQLException e) {e.printStackTrace();}
	}
	
	
	//게시글의 회원의 정보 얻기
	public AccountIntroduceDto accountCard(String bno){
		AccountIntroduceDto dto=null;
		String sql="SELECT NAME, PNUMBER, EMAIL,INTRODUCE, HOBBY FROM ACCOUNT_INTRODUCE i JOIN ACCOUNT a ON i.id = a.id WHERE a.ID=(SELECT ID FROM BOARD WHERE BNO=?)";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, bno);
			rs = psmt.executeQuery();
			if(rs.next()) {
				dto=new AccountIntroduceDto();
				dto.setName(rs.getString(1));
				dto.setPnumber(rs.getString(2));
				dto.setEmail(rs.getString(3));
				dto.setIntroduce(rs.getString(4));
				dto.setHobby(rs.getString(5));
			}
		}catch(SQLException e) {}
		return dto;
	}
	
	
	//파일 게시글의 회원의 정보 얻기
	public AccountIntroduceDto accountCardFromFile(String fno){
		AccountIntroduceDto dto=null;
		String sql="SELECT NAME, PNUMBER, EMAIL,INTRODUCE, HOBBY FROM ACCOUNT_INTRODUCE i JOIN ACCOUNT a ON i.id = a.id WHERE a.ID=(SELECT ID FROM FBOARD WHERE FNO=?)";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, fno);
			rs = psmt.executeQuery();
			if(rs.next()) {
				dto=new AccountIntroduceDto();
				dto.setName(rs.getString(1));
				dto.setPnumber(rs.getString(2));
				dto.setEmail(rs.getString(3));
				dto.setIntroduce(rs.getString(4));
				dto.setHobby(rs.getString(5));
			}
		}catch(SQLException e) {}
		return dto;
	}
	
}
