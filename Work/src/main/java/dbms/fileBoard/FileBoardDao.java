package dbms.fileBoard;

import java.io.File;
import java.io.FileInputStream;
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

import org.apache.tomcat.util.codec.binary.Base64;

import dbms.board.BoardDto;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import model.PagingModel;
import service.DaoService;

public class FileBoardDao implements DaoService<FileBoardDto>{
	
	private Connection conn;
	private ResultSet rs;
	private PreparedStatement psmt;
	
	//▶▶▶▶▶생성자
	public FileBoardDao(ServletContext context) {
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
	

	//▶▶▶▶▶게시글 작성
	public int writeOnFileBoard(String id, String title, String content, String attachfile) {
		int affected=0;
		String sql="INSERT INTO FBOARD(FNO,ID,TITLE,CONTENT,ATTACHFILE) VALUES(SEQ_FNO.NEXTVAL,?,?,?,?)";
		try {
			psmt = conn.prepareStatement(sql,new String[] {"fno"});
			psmt.setString(1, id);
			psmt.setString(2, title);
			psmt.setString(3, content);
			psmt.setString(4, attachfile);
			psmt.executeUpdate();
			rs= psmt.getGeneratedKeys();
			System.out.println("==========================================");
			while(rs.next()) {
				System.out.println("방금 입력된 행의 fno값:"+rs.getString(1));
				affected=Integer.parseInt(rs.getString(1));
			}
		}catch(SQLException e) {e.printStackTrace();}
		return affected;
	}
	
	
	
	//▶▶▶▶▶파일 게시글 레코드 얻기
	@Override
	public List<FileBoardDto> boardRecords(Map page) {
		List<FileBoardDto> records = new Vector<>();
		System.out.println("현재 레코드 출력에 들어옴");
		System.out.println("search:"+page.get("searchColumn"));
		System.out.println("searchText:"+page.get("searchText"));
		System.out.println("==========================");
		
		String sql="SELECT * FROM (SELECT FNO,TITLE,CONTENT,NAME,POSTDATE,ATTACHFILE, RANK() OVER (ORDER BY fno DESC) AS POSTRANK, HITCOUNT, (SELECT COUNT(*) FROM FBOARD_COMMENT WHERE fno=f.fno) as commentCount FROM ACCOUNT a JOIN FBOARD f ON a.id=f.id";

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
				FileBoardDto dto = new FileBoardDto();
				dto.setFno(rs.getString(1));
				dto.setTitle(rs.getString(2));
				dto.setContent(rs.getString(3));
				dto.setName(rs.getString(4));
				dto.setPostdate(rs.getDate(5));
				dto.setAttachfile(rs.getString(6));
				dto.setPostrank(rs.getString(7));
				dto.setHitcount(rs.getString(8));
				dto.setCommentcount(rs.getString(9));
				records.add(dto);
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		return records;
	}

	
	//▶▶▶▶▶게시글 히트 카운트 로직
	public void boardRecordHitCount(String fno) {
		try {
			String sql="UPDATE FBOARD SET hitcount=hitcount+1 WHERE fno=?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, fno);
			rs=psmt.executeQuery();
		}catch(SQLException e) {e.printStackTrace();}
	}
	
	
	//▶▶▶▶▶파일 게시글 뷰 보기
	@Override
	public FileBoardDto boardRecordOne(String fno) {
		FileBoardDto dto = null;
		try {
			String sql="SELECT FNO,TITLE,CONTENT,NAME,HITCOUNT,ATTACHFILE,a.ID FROM ACCOUNT a JOIN FBOARD b ON a.id=b.id WHERE FNO=?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, fno);
			rs=psmt.executeQuery();
			while(rs.next()) {
				dto = new FileBoardDto();
				dto.setFno(rs.getString(1));
				dto.setTitle(rs.getString(2));
				dto.setContent(rs.getString(3));
				dto.setName(rs.getString(4));
				dto.setHitcount(rs.getString(5));
				dto.setAttachfile(rs.getString(6));
				dto.setId(rs.getString(7));
			}
		}catch(SQLException e) {e.printStackTrace();}
		return dto;
	}

	//▶▶▶▶▶이전글/다음글 조회
	public Map<String,String> prevNext(String currentFno){
		Map<String,String> viewStep = new HashMap<>();
		try {
			//이전글 얻기
			String sql="SELECT FNO,TITLE FROM FBOARD WHERE FNO=(SELECT MIN(FNO) FROM FBOARD WHERE FNO > ?)";
			
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, currentFno);
			rs = psmt.executeQuery();
			if(rs.next()) {
				viewStep.put("PREV",rs.getString(1));
			}
			
			
			//다음글 얻기
			sql="SELECT FNO,TITLE FROM FBOARD WHERE FNO=(SELECT MAX(FNO) FROM FBOARD WHERE FNO < ?)";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, currentFno);
			rs = psmt.executeQuery();
			if(rs.next()) {
				viewStep.put("NEXT",rs.getString(1));
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		return viewStep;
	}///////////

	
	
	
	
	@Override
	public int writeOnBoard(String title, String content, String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateOnBoard(String title, String content, String no) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	//▶▶▶▶▶파일 게시글 삭제 로직
	@Override
	public int deleteOnBoard(String fno) {
		int affected=0;
		String sql="DELETE FBOARD_COMMENT WHERE FNO=?";
		try {
			System.out.println("파일 게시글 댓글 다 삭제중");
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, fno);
			affected = psmt.executeUpdate();
			System.out.println("파일 게시글 댓글 다 삭제 완료");
		}catch(SQLException e) {e.printStackTrace();}
		
		
		sql="DELETE FBOARD WHERE FNO=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, fno);
			affected = psmt.executeUpdate();
		}catch(SQLException e) {e.printStackTrace();}
		return affected;
	}

	@Override
	public int getTotalRecordCount(Map map) {
		int totalCount=0;
		System.out.println("현재 파일 레코드 카운트 출력에 들어옴");
		System.out.println("search:"+map.get("searchColumn"));
		System.out.println("searchText:"+map.get("searchText"));
		System.out.println("==========================");
		String sql="SELECT COUNT(*) FROM FBOARD";
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
	
	
}
