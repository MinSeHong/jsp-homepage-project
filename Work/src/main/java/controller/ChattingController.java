package controller;
import java.io.IOException;
import java.util.Map;

import dbms.account.AccountDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTOkens;

@WebServlet("/chatting")
public class ChattingController extends HttpServlet {
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doget은 Main.jsp의 iframe에 보여주는 Chatting.jsp용 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AccountDao accountDao = new AccountDao(getServletContext());
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		
		//◆◆◆◆ 토큰에 저장된 회원 정보 ◆◆◆◆
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		req.setAttribute("accountProfile", accountDao.accountProfile((String)claims.get("sub")));
		accountDao.close();
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/main/Chatting.jsp");
		dispatcher.forward(req, resp);
	}
}
