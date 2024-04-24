package controller.fileBoard;

import java.io.IOException;
import java.util.Map;

import dbms.fileBoard.FileBoardDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTOkens;

@WebServlet("/fileEdit")
public class FileBoardEditController extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FileBoardDao boardDao = new FileBoardDao(getServletContext());
		
		//◆◆◆◆ 토큰에 저장된 회원 정보 여부 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		
		//◆◆◆◆ bno, newPage 저장 ◆◆◆◆
		req.setAttribute("boardRecord", boardDao.boardRecordOne(req.getParameter("fno")));
		req.setAttribute("nowPage", req.getParameter("nowPage"));
		
		boardDao.close();
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/fileBoard/FileBoardEdit.jsp");
		dispatcher.forward(req, resp);
	}
}
