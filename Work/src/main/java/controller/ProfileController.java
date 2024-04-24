package controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

import dbms.account.AccountDao;
import dbms.account.introduce.AccountIntroduceDao;
import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTOkens;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {
	@Override
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doget은 Main.jsp의 iframe에 보여주는 Profile.jsp용■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//◆◆◆◆ 토큰에 저장된 회원 정보 여부 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		
		//◆◆◆◆ 계정 프로필 생성 ◆◆◆◆
		AccountDao accountDao = new AccountDao(getServletContext());
		req.setAttribute("accountProfile", accountDao.accountProfile((String)claims.get("sub")));
		accountDao.close();
		//◆◆◆◆ 회원 소개 생성 ◆◆◆◆
		AccountIntroduceDao accountIntroduce = new AccountIntroduceDao(getServletContext());
		req.setAttribute("accountIntroduce", accountIntroduce.accountIntroduce((String)claims.get("sub")));
		accountIntroduce.close();
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/main/Profile.jsp");
		dispatcher.forward(req, resp);
	}
	
	@Override
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doPost는 Profile.jsp의 회원탈퇴, 프로필 수정 용 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String profileControllerButton = req.getParameter("profileControllerButton");
		
		//◆◆◆◆ 토큰에 저장된 회원 정보 여부 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		String id = (String)claims.get("sub");
		
		
		
		
		//◆◆◆◆ 계정 프로필 생성 ◆◆◆◆
		AccountDao accountDao = new AccountDao(getServletContext());
		req.setAttribute("accountProfile", accountDao.accountProfile((String)claims.get("sub")));
		AccountIntroduceDao accountIntroduce = new AccountIntroduceDao(getServletContext());
		
		
		if(profileControllerButton.equals("edit")) {//▶▶ 프로필 수정.
			String pnumber = req.getParameter("pnumber");
			String introduce = req.getParameter("introduceParameter");
			String[] entryHobbys = req.getParameterValues("entryHobby");
			String entryHobby = Arrays.asList(entryHobbys).stream().collect(Collectors.joining(","));
			
			accountDao.accountHobbyEdit(id, entryHobby);
			accountIntroduce.accountIntroduceEdit(id, pnumber, introduce);
			
			//◆◆◆◆ 계정 프로필 생성 ◆◆◆◆
			req.setAttribute("accountProfile", accountDao.accountProfile((String)claims.get("sub")));
			accountDao.close();
			//◆◆◆◆ 회원 소개 생성 ◆◆◆◆
			req.setAttribute("accountIntroduce", accountIntroduce.accountIntroduce((String)claims.get("sub")));
			accountIntroduce.close();
			
			
			RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/main/Profile.jsp");
			dispatcher.forward(req, resp);
		}
		else if(profileControllerButton.equals("delete")) {//▶▶ 회원 탈퇴

			//◆◆◆◆ 회원 탈퇴 ◆◆◆◆
			accountDao.accountDelete(id);
			JWTOkens.removeToken(req, resp);
			accountDao.close();
			
			//◆◆◆◆ 회원 탈퇴 후 로그인으로 이동 ◆◆◆◆
			PrintWriter out = resp.getWriter();
			out.println("<script>");
			out.println("window.parent.location.href ='/';");
			out.println("</script>");
		}
		
		


	}
	
}
