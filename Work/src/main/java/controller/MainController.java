package controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import dbms.account.AccountDao;
import dbms.account.AccountDto;
import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.JWTOkens;

@WebServlet("/main")
public class MainController extends HttpServlet {
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doPost는 로그인 인증 관련 메소드로 사용 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginId = req.getParameter("loginId");
		String loginPassword = req.getParameter("loginPassword");

		AccountDao accountDao = new AccountDao(getServletContext());
		boolean isMember = accountDao.isMember(loginId, loginPassword);
		
		if(isMember){//▶▶▶▶▶ 로그인 성공 ◀◀◀◀◀
			//◆◆ 토큰 생성 ◆◆
			Map<String,Object> payloads = new HashMap<>();
			payloads.put("loginPassword",loginPassword);
			long expirationTime = 1000 * 60 * 60 * 3;
			String token=JWTOkens.createToken(loginId, payloads, expirationTime);
			Cookie cookie = new Cookie("COOKIE-NAME",token);
			cookie.setPath(req.getContextPath());
			resp.addCookie(cookie);
			
			//◆◆ 토큰을 이용하여 request영역 계정 프로필 생성 ◆◆
			Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
			req.setAttribute("account", (String)claims.get("sub"));
			req.setAttribute("accountProfile", accountDao.accountProfile((String)claims.get("sub")));
			
			RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/main/Main.jsp");
			dispatcher.forward(req, resp);
		}
		else{//▶▶▶▶▶ 로그인 실패 ◀◀◀◀◀
			req.getRequestDispatcher("/Login").forward(req, resp);
		}
		accountDao.close();
	}
	

//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doPost는 회원 가입 관련 메소드로 사용 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String[]> entryAccount = req.getParameterMap();
		AccountDao accountDao = new AccountDao(getServletContext());
		
		//■■■■■■ accountDao.isMemberCheck(entryAccount.get("entryId")[0]) 중복 회원 판단 ■■■■■■
		boolean isMemberCheck = accountDao.isMemberCheck(entryAccount.get("entryId")[0]);
		boolean isEmailCheck = accountDao.isEmailCheck(entryAccount.get("entryEmail")[0]);
		
		String[] hobby = req.getParameterValues("entryHobby");
		
		
		if(!isMemberCheck && !isEmailCheck){//▶▶▶▶▶ 이메일, 계정 중복 없음 ◀◀◀◀◀
			accountDao.accountEntry(entryAccount);
			req.setAttribute("errorMessage", "Entry Complete!");
			req.setAttribute("error", "OK");
			req.getRequestDispatcher("/Login").forward(req, resp);
		}
		else if(isMemberCheck) {//▶▶▶▶▶ 계정 중복 있음 ◀◀◀◀◀
			req.setAttribute("errorMessage", "Existing Id!");
			req.setAttribute("error", "OK");
			req.setAttribute("flip", true);
			req.setAttribute("entryAccount", entryAccount);
			req.setAttribute("hobby", Arrays.asList(hobby).stream().collect(Collectors.joining(",")));
			req.getRequestDispatcher("/Login").forward(req, resp);
		}
		else if(isEmailCheck) {//▶▶▶▶▶ 이메일 중복 있음 ◀◀◀◀◀
			req.setAttribute("errorMessage", "Existing email!");
			req.setAttribute("error", "OK");
			req.setAttribute("flip", true);
			req.setAttribute("entryAccount", entryAccount);
			req.setAttribute("hobby", Arrays.asList(hobby).stream().collect(Collectors.joining(",")));
			req.getRequestDispatcher("/Login").forward(req, resp);
		}
		
		accountDao.close();
		
	}
	
}


