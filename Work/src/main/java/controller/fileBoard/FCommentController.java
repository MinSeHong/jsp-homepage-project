package controller.fileBoard;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import dbms.fileBoard.FCommentDao;
import dbms.fileBoard.FCommentDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTOkens;

@WebServlet("/fcomment")
public class FCommentController extends HttpServlet{
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doget은 FComment.jsp의 iframe에 보여주는 FileBoardView.jsp용■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FCommentDao fCommentDao = new FCommentDao(getServletContext());
		
		//◆◆◆◆ 게시판 댓글 레코드 가져오기 ◆◆◆◆
		List<FCommentDto> cRecords = fCommentDao.boardCommentRecords(req.getParameter("fno"));
		req.setAttribute("cRecords", cRecords);
		fCommentDao.close();
		
		//◆◆◆◆ 토큰에 저장된 회원 정보 여부 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/fileBoard/FComment.jsp");
		dispatcher.forward(req, resp);
	}
	
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doPost는 FComment.jsp의 컨트롤러■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FCommentDao fCommentDao = new FCommentDao(getServletContext());
		
		String submitCommentController = req.getParameter("submitCommentController");
		
		String fno = req.getParameter("fno")==null?"x":req.getParameter("fno");
		String fcomment = req.getParameter("fcomment")==null?"x":req.getParameter("fcomment");
		String fcno = req.getParameter("fcno")==null?"x":req.getParameter("fcno");
		
		
		//◆◆◆◆ 토큰에 저장된 회원 정보 여부 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		String id =(String)claims.get("sub");
		
		if(submitCommentController.equals("commentWrite")) {//▶▶ 댓글 작성.
			fCommentDao.writeOnComment(fno, id, fcomment);
		}
		else if(submitCommentController.equals("commentEdit")) {//▶▶ 댓글 수정.
			fCommentDao.editOnComment(fcno,fcomment);
		}
		else if(submitCommentController.equals("commentDelete")) {//▶▶ 댓글 삭제.
			fCommentDao.deleteOnComment(fcno);
		}
		
		
		List<FCommentDto> cRecords = fCommentDao.boardCommentRecords(req.getParameter("fno"));
		req.setAttribute("cRecords", cRecords);
		fCommentDao.close();
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/fileBoard/FComment.jsp");
		dispatcher.forward(req, resp);
	}
}
