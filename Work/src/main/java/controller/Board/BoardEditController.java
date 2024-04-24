package controller.Board;
import java.io.IOException;
import java.util.Map;

import dbms.board.BoardDao;
import dbms.board.BoardDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTOkens;

@WebServlet("/edit")
public class BoardEditController extends HttpServlet {
	
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doPost는  View.jsp에서 Edit.jsp로 이동할 때 사용 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDao boardDao = new BoardDao(getServletContext());
		
		//◆◆◆◆ 토큰에 저장된 회원 정보 얻기 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		
		//◆◆◆◆ bno, newPage 저장 ◆◆◆◆
		req.setAttribute("boardRecord", boardDao.boardRecordOne(req.getParameter("bno")));
		req.setAttribute("nowPage", req.getParameter("nowPage"));
		req.setAttribute("searchColumn", req.getParameter("searchColumn"));
		req.setAttribute("searchText", req.getParameter("searchText"));
		
		boardDao.close();
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/board/BoardEdit.jsp");
		dispatcher.forward(req, resp);
	}
}
