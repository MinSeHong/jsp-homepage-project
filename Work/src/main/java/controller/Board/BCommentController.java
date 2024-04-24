package controller.Board;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbms.board.BCommentDao;
import dbms.board.BCommentDto;
import dbms.board.BoardDao;
import dbms.board.BoardDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.JWTOkens;
import model.PagingModel;

@WebServlet("/bcomment")
public class BCommentController extends HttpServlet {
	@Override
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doget은 BComment.jsp의 iframe에 보여주는 View.jsp용■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BCommentDao bCommentDao = new BCommentDao(getServletContext());
		
		//◆◆◆◆ 게시판 댓글 레코드 가져오기 ◆◆◆◆
		List<BCommentDto> cRecords = bCommentDao.boardCommentRecords(req.getParameter("bno"));
		req.setAttribute("cRecords", cRecords);
		bCommentDao.close();
		
		//◆◆◆◆ 토큰에 저장된 회원 정보 여부 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/board/BComment.jsp");
		dispatcher.forward(req, resp);
	}
	
	
	@Override
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doPost는 BComment.jsp의 컨트롤러■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BCommentDao bCommentDao = new BCommentDao(getServletContext());
		
		String submitCommentController = req.getParameter("submitCommentController");
		
		String bno = req.getParameter("bno")==null?"x":req.getParameter("bno");
		String bcomment = req.getParameter("bcomment")==null?"x":req.getParameter("bcomment");
		String cno = req.getParameter("cno")==null?"x":req.getParameter("cno");
		
		
		//◆◆◆◆ 토큰에 저장된 회원 정보 여부 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		String id =(String)claims.get("sub");
		
		
		if(submitCommentController.equals("commentWrite")) {//▶▶ 댓글 작성.
			bCommentDao.writeOnComment(bno, id, bcomment);
		}
		else if(submitCommentController.equals("commentEdit")) {//▶▶ 댓글 수정.
			bCommentDao.editOnComment(cno,bcomment);
		}
		else if(submitCommentController.equals("commentDelete")) {//▶▶ 댓글 삭제.
			bCommentDao.deleteOnComment(cno);
		}
		
		
		List<BCommentDto> cRecords = bCommentDao.boardCommentRecords(req.getParameter("bno"));
		req.setAttribute("cRecords", cRecords);
		bCommentDao.close();
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/board/BComment.jsp");
		dispatcher.forward(req, resp);
	}
	
}
