package controller.Board;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbms.account.introduce.AccountIntroduceDao;
import dbms.board.BoardDao;
import dbms.board.BoardDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.JWTOkens;
import model.PagingModel;

@WebServlet("/boardList")
public class BoardListController extends HttpServlet {
	@Override
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doget은 Main.jsp의 iframe에 보여주는 boardList용 // Search(검색)■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDao boardDao = new BoardDao(getServletContext());
		Map page = new HashMap();
		
		String Button = req.getParameter("SubmitBoardList")==null?"default": req.getParameter("SubmitBoardList");
		String searchColumn = req.getParameter("searchColumn");
		
		String searchText = req.getParameter("searchText")!=null?req.getParameter("searchText"):null;
		
		if(Button.equals("default")) {
			if(searchText!=null) {
				req.setAttribute("searchColumn", searchColumn);
				req.setAttribute("searchText", searchText);
				
				page.put("searchColumn", searchColumn);
				page.put("searchText", searchText);
			}
		}
		
		if(Button.equals("search")) {//▶▶ 검색으로 BoardList.jsp에 레코드 출력
			searchText = req.getParameter("searchText");
			
			req.setAttribute("searchColumn", searchColumn);
			req.setAttribute("searchText", searchText);
			
			page.put("searchColumn", searchColumn);
			page.put("searchText", searchText);
		}
		
//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆Paging Logic◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆//
		PagingModel.setMapForPaging(page, boardDao, req);
		
		int totalRecordCount = Integer.parseInt(page.get(PagingModel.TOTAL_RECORD_COUNT).toString());
		int pageSize=Integer.parseInt(page.get(PagingModel.PAGE_SIZE).toString());
		int blockPage=Integer.parseInt(page.get(PagingModel.BLOCK_PAGE).toString());
		int nowPageInt=Integer.parseInt(page.get(PagingModel.NOWPAGE).toString());
		
		List<BoardDto> boardRecords = boardDao.boardRecords(page);
		req.setAttribute("boardRecords", boardRecords);
		req.setAttribute("paging", PagingModel.pagingStyle(totalRecordCount, pageSize, blockPage, nowPageInt,"/boardList?", page));
		req.setAttribute("page", page);
		boardDao.close();
//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆Paging Logic◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆//
		
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/board/BoardList.jsp?nowPage="+1);
		dispatcher.forward(req, resp);
	}
	
	
	
	
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doPost은 CRUD로 boardList.jsp로 이동 할 때 사용■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDao boardDao = new BoardDao(getServletContext());
		String Button = req.getParameter("SubmitBoardList")==null?"default": req.getParameter("SubmitBoardList");
		Map page = new HashMap();//페이지 용 Map 생성
		
		
		//■■■■■■ 토큰으로 회원 판단 ■■■■■■
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		req.setAttribute("account", (String)claims.get("sub"));
		String id = (String)claims.get("sub");
		
		if(Button.equals("write")) {//▶▶ Write.jsp에서 글 작성 후 BoardView.jsp로 이동
			Map viewStep = new HashMap();
			String title = req.getParameter("titleParameter");
			String content = req.getParameter("contentParameter");
			
			String searchColumn = req.getParameter("searchColumn");
			String searchText = req.getParameter("searchText")!=null?req.getParameter("searchText"):null;
			
			if(searchText!=null) {
				req.setAttribute("searchColumn", searchColumn);
				req.setAttribute("searchText", searchText);
				
				page.put("searchColumn", searchColumn);
				page.put("searchText", searchText);
			}
			
			int affected = boardDao.writeOnBoard(title, content, id);
			String bno = Integer.toString(affected);
			
			//◆◆◆◆ bno으로 View에 보여줄 게시글 ◆◆◆◆
			BoardDto boardRecord = boardDao.boardRecordOne(bno);
			
			//◆◆◆◆ 현재 view의 회원 자기소개 카드 데이터 가져오기 ◆◆◆◆
			AccountIntroduceDao accountIntroduce = new AccountIntroduceDao(getServletContext());
			req.setAttribute("accountCard", accountIntroduce.accountCard(bno));
			accountIntroduce.close();
			
			//◆◆◆◆ bno으로 View에 다음/이전 버튼 생성 여부 ◆◆◆◆
			viewStep = boardDao.prevNext(bno);
			req.setAttribute("viewStep", viewStep);
			req.setAttribute("boardRecord", boardRecord);
			req.setAttribute("viewStep", viewStep);
			boardDao.close();
			
			RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/board/BoardView.jsp?bno="+Integer.toString(affected)+"&nowPage=1");
			dispatcher.forward(req, resp);
			return;
		}
		else if(Button.equals("delete")) {//▶▶ view.jsp에서 글 삭제 후 BoardList.jsp로 이동
			String searchColumn = req.getParameter("searchColumn");
			String searchText = req.getParameter("searchText")!=null?req.getParameter("searchText"):null;
			
			if((searchText!=null)&&(searchText!="")) {
				req.setAttribute("searchColumn", searchColumn);
				req.setAttribute("searchText", searchText);
				
				page.put("searchColumn", searchColumn);
				page.put("searchText", searchText);
			}
			
			String bno = req.getParameter("bno");
			String nowPage = req.getParameter("nowPage");
			int affected = boardDao.deleteOnBoard(bno);
		}
		
//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆Paging Logic◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆//
		String nowPage = req.getParameter("nowPage")==null?"1":req.getParameter("nowPage");
		PagingModel.setMapForPaging(page, boardDao, req);
		
		int totalRecordCount = Integer.parseInt(page.get(PagingModel.TOTAL_RECORD_COUNT).toString());
		int pageSize=Integer.parseInt(page.get(PagingModel.PAGE_SIZE).toString());
		int blockPage=Integer.parseInt(page.get(PagingModel.BLOCK_PAGE).toString());
		int nowPageInt=Integer.parseInt(page.get(PagingModel.NOWPAGE).toString());
		

		List<BoardDto> boardRecords = boardDao.boardRecords(page);
		req.setAttribute("boardRecords", boardRecords);
		req.setAttribute("paging", PagingModel.pagingStyle(totalRecordCount, pageSize, blockPage, nowPageInt,"/boardList?", page));
		req.setAttribute("page", page);
		boardDao.close();
//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆Paging Logic◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆//
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/board/BoardList.jsp?nowPage="+nowPage);
		dispatcher.forward(req, resp);
	}
}
