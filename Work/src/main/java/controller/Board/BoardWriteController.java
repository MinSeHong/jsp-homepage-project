package controller.Board;
import java.io.IOException;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTOkens;

@WebServlet("/write")
public class BoardWriteController extends HttpServlet {
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doPost는 BoardList.jsp에서 Write.jsp로 이동할 때 사용 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//◆◆◆◆ 토큰에 저장된 회원 정보 여부 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/board/BoardWrite.jsp");
		dispatcher.forward(req, resp);
	}
}
