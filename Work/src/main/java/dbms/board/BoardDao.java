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

public class BoardDao implements DaoService<BoardDto>{
	
	private Connection conn;
	private ResultSet rs;
	private PreparedStatement psmt;
	
	//▶▶▶▶▶생성자
	public BoardDao(ServletContext context) {
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
	
	
	//▶▶▶▶▶게시글 레코드 얻기
	public List<BoardDto> boardRecords(Map page){
		List<BoardDto> records = new Vector<>();
		String sql="SELECT * FROM (SELECT b.*, name, RANK() OVER (ORDER BY b.bno DESC) AS POSTRANK, (SELECT COUNT(*) FROM BOARD_COMMENT WHERE bno=b.bno) as commentCount FROM ACCOUNT a JOIN BOARD b ON a.id = b.id";

		if(page.get("searchText") !=null) {
			System.out.println("searchText가 널아님"+page.get("search"));
			sql+=" AND "+page.get("searchColumn") + " LIKE '%"+page.get("searchText")+"%'";
		}
		sql+= ") WHERE POSTRANK BETWEEN ? AND ? ";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, page.get(PagingModel.START).toString());
			psmt.setString(2, page.get(PagingModel.END).toString());
			rs=psmt.executeQuery();
			while(rs.next()) {
				BoardDto dto = new BoardDto();
				dto.setBno(rs.getString(1));
				dto.setTitle(rs.getString(2));
				dto.setPostdate(rs.getDate(3));
				dto.setHitcount(rs.getString(4));
				dto.setLikecount(rs.getString(5));
				dto.setContent(rs.getString(6));
				dto.setId(rs.getString(7));
				dto.setName(rs.getString(8));
				dto.setPostrank(rs.getString(9));
				dto.setCommentcount(rs.getString(10));
				records.add(dto);
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		return records;
	}
	
	
	//▶▶▶▶▶게시글 뷰 보기
	public BoardDto boardRecordOne(String bno) {
		BoardDto dto=null;
		try {
			String sql="SELECT b.*, name FROM BOARD b JOIN ACCOUNT a ON b.id = a.id WHERE bno=?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, bno);
			rs=psmt.executeQuery();
			while(rs.next()) {
				dto = new BoardDto();
				dto.setBno(rs.getString(1));
				dto.setTitle(rs.getString(2));
				dto.setPostdate(rs.getDate(3));
				dto.setHitcount(rs.getString(4));
				dto.setLikecount(rs.getString(5));
				dto.setContent(rs.getString(6));
				dto.setId(rs.getString(7));
				dto.setName(rs.getString(8));
			}
		}catch(SQLException e) {e.printStackTrace();}
		return dto;
	}
	
	
	
	//▶▶▶▶▶게시글 작성
	public int writeOnBoard(String title, String content, String id) {
		int affected=0;
		String sql="INSERT INTO BOARD(BNO,TITLE,CONTENT,ID) VALUES(SEQ_BNO.NEXTVAL,?,?,?)";
		try {
			psmt = conn.prepareStatement(sql,new String[] {"bno"});
			psmt.setString(1, title);
			psmt.setString(2, content);
			psmt.setString(3, id);
			affected = psmt.executeUpdate();
			rs= psmt.getGeneratedKeys();
			while(rs.next()) {
				affected=Integer.parseInt(rs.getString(1));
			}
		}catch(SQLException e) {e.printStackTrace();}
		return affected;
	}
	
	//▶▶▶▶▶게시글 히트 카운트 로직
	public void boardRecordHitCount(String bno) {
		try {
			String sql="UPDATE BOARD SET hitcount=hitcount+1 WHERE bno=?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, bno);
			rs=psmt.executeQuery();
		}catch(SQLException e) {e.printStackTrace();}
	}
	
	//▶▶▶▶▶게시글 수정
	public int updateOnBoard(String title, String content,String bno) {
		int affected=0;
		String sql="UPDATE BOARD SET title=?, content=? WHERE bno=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, title);
			psmt.setString(2, content);
			psmt.setString(3, bno);
			affected = psmt.executeUpdate();
			rs= psmt.getGeneratedKeys();
			while(rs.next()) {
			}
		}catch(SQLException e) {e.printStackTrace();}
		return affected;
	}
	
	
	//▶▶▶▶▶게시글 삭제 로직
	public int deleteOnBoard(String bno) {
		int affected=0;
		
		String sql="DELETE BOARD_COMMENT WHERE BNO=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, bno);
			affected = psmt.executeUpdate();
		}catch(SQLException e) {e.printStackTrace();}
		
		sql="DELETE BOARD WHERE BNO=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, bno);
			affected = psmt.executeUpdate();
		}catch(SQLException e) {e.printStackTrace();}
		return affected;
	}
	
	//▶▶▶▶▶총 레코드 수 얻기용
	@Override
	public int getTotalRecordCount(Map map) {
		int totalCount=0;
		String sql="SELECT COUNT(*) FROM ACCOUNT a JOIN BOARD b ON a.id = b.id";
		if(map.get("searchText") !=null) {
			sql+=" WHERE "+map.get("searchColumn") + " LIKE '%"+map.get("searchText")+"%'";
		}
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			rs.next();
			totalCount= rs.getInt(1);
		}
		catch(SQLException e) {e.printStackTrace();}
		return totalCount;
	}

	
	//▶▶▶▶▶이전글/다음글 조회
	public Map<String,String> prevNext(String currentBno){
		Map<String,String> viewStep = new HashMap<>();
		try {
			//이전글 얻기
			String sql="SELECT BNO,TITLE FROM BOARD WHERE BNO=(SELECT MIN(BNO) FROM BOARD WHERE BNO > ?)";
			
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, currentBno);
			rs = psmt.executeQuery();
			if(rs.next()) {
				viewStep.put("PREV",rs.getString(1));
			}
			
			
			//다음글 얻기
			sql="SELECT BNO,TITLE FROM BOARD WHERE BNO=(SELECT MAX(BNO) FROM BOARD WHERE BNO < ?)";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, currentBno);
			rs = psmt.executeQuery();
			if(rs.next()) {
				viewStep.put("NEXT",rs.getString(1));
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		return viewStep;
	}///////////

}
