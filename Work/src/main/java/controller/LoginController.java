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
import jakarta.servlet.http.HttpSession;
import model.JWTOkens;

@WebServlet("/Login")
public class LoginController extends HttpServlet {
	//■■■■■■■■■■■■■■■iframe에 Login화면 출력. iframe으로 바로 연결하기 때문에 doGet()을 사용■■■■■■■■■■■■■■■
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//■■■■■■■■■■■■■■■쿠키 확인■■■■■■■■■■■■■■■
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		boolean isValid=JWTOkens.verifyToken(token);
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		
		//■■■■■■■■■■■■■■■계정 확인■■■■■■■■■■■■■■■
		AccountDao accountDao = new AccountDao(getServletContext());
		boolean isMember = accountDao.isMember((String)claims.get("sub"), (String)claims.get("loginPassword"));
		
		//■■■■■■■■■■■■■■■쿠키&계정 확인한 후 Board로 이동■■■■■■■■■■■■■■■
		if(isValid && isMember) {//▶▶ 로그인 한 기록이 있으면 바로 Main 화면으로 이동
			req.setAttribute("account", (String)claims.get("sub"));
			req.setAttribute("accountProfile",accountDao.accountProfile((String)claims.get("sub")));
			RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/main/Main.jsp");
			accountDao.close();
			dispatcher.forward(req, resp);
		}
		else{//▶▶ 로그인 한 기록이 없으면 로그인 화면으로 이동
			accountDao.close();
			RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/main/Login.jsp");
			dispatcher.forward(req, resp);
		}
	}
	
	
//■■■■■■■■■■■■■■■doPost는 로그인 회원 정보가 아닐 시 전용■■■■■■■■■■■■■■■
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("errorMessage", "Invalid Id or Password");
		req.setAttribute("error", "OK");
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/main/Login.jsp");
		dispatcher.forward(req, resp);
	}
}
