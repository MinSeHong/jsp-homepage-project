package controller.Board;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbms.account.introduce.AccountIntroduceDao;
import dbms.account.introduce.AccountIntroduceDto;
import dbms.board.BoardDao;
import dbms.board.BoardDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import model.JWTOkens;

@WebServlet("/view")
public class BoardViewController extends HttpServlet {
	
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doGet은 BoardList.jsp에서 View.jsp로 이동할 때 사용 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map viewStep = new HashMap();
		BoardDao boardDao = new BoardDao(getServletContext());
		
		//◆◆◆◆ 조회수 증가 ◆◆◆◆
		boardDao.boardRecordHitCount(req.getParameter("bno"));
		//◆◆◆◆ bno으로 View에 보여줄 게시글 ◆◆◆◆
		BoardDto boardRecord = boardDao.boardRecordOne(req.getParameter("bno"));
		
		//◆◆◆◆ 현재 view의 회원 자기소개 카드 데이터 가져오기 ◆◆◆◆
		AccountIntroduceDao accountIntroduce = new AccountIntroduceDao(getServletContext());
		req.setAttribute("accountCard", accountIntroduce.accountCard(req.getParameter("bno")));
		accountIntroduce.close();
		
		//◆◆◆◆ bno으로 View에 다음/이전 버튼 생성 여부 ◆◆◆◆
		viewStep = boardDao.prevNext(req.getParameter("bno"));
		req.setAttribute("viewStep", viewStep);
		req.setAttribute("boardRecord", boardRecord);
		req.setAttribute("viewStep", viewStep);
		boardDao.close();
		
		//◆◆◆◆ 토큰에 저장된 회원 정보로 수정/삭제 버튼 생성 여부 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/board/BoardView.jsp?bno="+req.getParameter("bno")+"&nowPage="+req.getParameter("nowPage"));
		dispatcher.forward(req, resp);
	}
	
	
	
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doPost는 Edit.jsp에서 View.jsp로 이동할 때 사용 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String Button = req.getParameter("SubmitBoardList");
		BoardDao boardDao = new BoardDao(getServletContext());
		Map viewStep = new HashMap();
		
		
		//◆◆◆◆ 토큰에 저장된 회원 정보로 수정/삭제 버튼 생성 여부 ◆◆◆◆
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		
		if(Button.equals("update")) {//▶▶ 게시글 수정.
			String title = req.getParameter("titleParameter");
			String content = req.getParameter("contentParameter");
			String bno = req.getParameter("bno");
			String nowPage = req.getParameter("nowPage");
			String searchColumn = req.getParameter("searchColumn");
			String searchText = req.getParameter("searchText");
			String searchString="";
			
			if((searchText!="")&&(searchText!=null)) {
				searchString="&searchColumn="+searchColumn+"&searchText="+searchText;
			}
			
			int affected = boardDao.updateOnBoard(title, content, bno);

			//◆◆◆◆ bno으로 View에 보여줄 게시글 ◆◆◆◆
			BoardDto boardRecord = boardDao.boardRecordOne(req.getParameter("bno"));
			viewStep = boardDao.prevNext(req.getParameter("bno"));
			
			//◆◆◆◆ bno으로 View에 다음/이전 버튼 생성 여부 ◆◆◆◆
			req.setAttribute("viewStep", viewStep);
			req.setAttribute("boardRecord", boardRecord);
			RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/board/BoardView.jsp?nowPage="+nowPage+searchString);
			dispatcher.forward(req, resp);
		}
		boardDao.close();
	}
}
