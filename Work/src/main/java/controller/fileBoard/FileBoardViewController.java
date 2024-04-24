package controller.fileBoard;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dbms.account.introduce.AccountIntroduceDao;
import dbms.board.BoardDao;
import dbms.board.BoardDto;
import dbms.fileBoard.FileBoardDao;
import dbms.fileBoard.FileBoardDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTOkens;

@WebServlet("/fileBoardView")
public class FileBoardViewController extends HttpServlet{
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doGet은 BoardList.jsp에서 FileBoardView.jsp로 이동할 때 사용 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map viewStep = new HashMap();
		FileBoardDao boardDao = new FileBoardDao(getServletContext());

		//◆◆◆◆ 조회수 증가 ◆◆◆◆
		boardDao.boardRecordHitCount(req.getParameter("fno"));
		//◆◆◆◆ bno으로 View에 보여줄 게시글 ◆◆◆◆
		FileBoardDto boardRecord = boardDao.boardRecordOne(req.getParameter("fno"));
		
		//◆◆◆◆ 현재 view의 회원 자기소개 카드 데이터 가져오기 ◆◆◆◆
		AccountIntroduceDao accountIntroduce = new AccountIntroduceDao(getServletContext());
		req.setAttribute("accountCard", accountIntroduce.accountCardFromFile(req.getParameter("fno")));
		accountIntroduce.close();
		
		//◆◆◆◆ bno으로 View에 다음/이전 버튼 생성 여부 ◆◆◆◆
		viewStep = boardDao.prevNext(req.getParameter("fno"));
		req.setAttribute("viewStep", viewStep);
		req.setAttribute("boardRecord", boardRecord);
		req.setAttribute("viewStep", viewStep);
		boardDao.close();
		
		
		//◆◆◆◆ 토큰에 저장된 회원 정보로 수정/삭제 버튼 생성 여부 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/fileBoard/FileBoardView.jsp?fno="+req.getParameter("fno")+"&nowPage="+req.getParameter("nowPage"));
		dispatcher.forward(req, resp);
	}
	
	
	
}
