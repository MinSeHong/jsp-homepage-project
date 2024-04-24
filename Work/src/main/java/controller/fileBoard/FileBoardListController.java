package controller.fileBoard;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbms.account.introduce.AccountIntroduceDao;
import dbms.board.BoardDto;
import dbms.fileBoard.FileBoardDao;
import dbms.fileBoard.FileBoardDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.FileUtils;
import model.JWTOkens;
import model.PagingModel;

@WebServlet("/fileBoardList")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10,maxRequestSize = 1024 * 1024 * 10 * 5)
public class FileBoardListController extends HttpServlet{
	@Override
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doget은 Main.jsp의 iframe에 보여주는 FileBoardList용 // Search(검색)■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FileBoardDao boardDao = new FileBoardDao(getServletContext());
		Map page = new HashMap();
		
		String Button = req.getParameter("SubmitBoardList")==null?"default": req.getParameter("SubmitBoardList");
		String searchColumn = req.getParameter("searchColumn");
		req.setAttribute("saveDirectory", getServletContext().getRealPath("/upload/fileBoard/"));
		
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
				
				List<FileBoardDto> fileBoardRecords = boardDao.boardRecords(page);
				req.setAttribute("fileBoardRecords", fileBoardRecords);
				req.setAttribute("paging", PagingModel.pagingStyle(totalRecordCount, pageSize, blockPage, nowPageInt,"/fileBoardList?", page));
				req.setAttribute("page", page);
				boardDao.close();
		//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆Paging Logic◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆//
		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/fileBoard/FileBoardList.jsp?nowPage="+1);
		dispatcher.forward(req, resp);
	}
	
	
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ doPost은 CRUD로 FileBoardList.jsp로 이동 할 때 사용■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String Button = req.getParameter("SubmitBoardList")==null?"default": req.getParameter("SubmitBoardList");
		
		//■■■■■■ 토큰으로 회원 판단 ■■■■■■
		String token=JWTOkens.getToken(req,"COOKIE-NAME");
		boolean isValid=JWTOkens.verifyToken(token);
		Map<String,Object> claims= JWTOkens.getTokenPayloads(token);
		String id = (String)claims.get("sub");
		
		Map page = new HashMap();//페이지 용 Map 생성
		
		//▶▶기타 파라미터 받기
		String title = req.getParameter("titleParameter");
		String content = req.getParameter("contentParameter");
		
		if(Button.equals("write")) {//▶▶ FileWrite.jsp에서 글 작성 후 FileBoardView.jsp로 이동
			int fno;
			try {
				//▶▶업로드된 파일 정보를 얻기 위한 Part객체 생성
				Collection<Part> parts= req.getParts();
				
				//▶▶파일을 저장할 서버의 물리적 경로
				String saveDirectory=  getServletContext().getRealPath("/upload/fileBoard");
				//▶▶파일 업로드 로직 호출
				StringBuffer filenames= FileUtils.upload(parts, saveDirectory);
				
				if(filenames== null) {//파일 입 업로드시
					req.setAttribute("account", id);
					req.setAttribute("title", title);
					req.setAttribute("content", content);
					req.getRequestDispatcher("/WEB-INF/fileBoard/FileBoardWrite.jsp").forward(req, resp);
					return;
				}
				
				//▶▶입력 처리로직 호출(데이타베이스 CRUD작업과 관련된 모델 호출)
				FileBoardDao dao = new FileBoardDao(getServletContext());
				fno=dao.writeOnFileBoard(id, title, content, filenames.toString());
				
				if(fno==0) {//입력 실패시 업로드된 파일 삭제
					//▶▶파일 삭제 로직 호출
					FileUtils.deletes(filenames, saveDirectory, ",");
				}
			}
			catch(Exception e) {
				fno=-1;
				req.setAttribute("account", id);
				req.setAttribute("title", title);
				req.setAttribute("content", content);
				req.getRequestDispatcher("/WEB-INF/fileBoard/FileBoardWrite.jsp").forward(req, resp);
				return;
			}
			Map viewStep = new HashMap();
			FileBoardDao boardDao = new FileBoardDao(getServletContext());
			
			//◆◆◆◆ bno으로 View에 보여줄 게시글 ◆◆◆◆
			FileBoardDto boardRecord = boardDao.boardRecordOne(Integer.toString(fno));
			
			//◆◆◆◆ 현재 view의 회원 자기소개 카드 데이터 가져오기 ◆◆◆◆
			AccountIntroduceDao accountIntroduce = new AccountIntroduceDao(getServletContext());
			req.setAttribute("accountCard", accountIntroduce.accountCardFromFile(Integer.toString(fno)));
			accountIntroduce.close();
			
			//◆◆◆◆ bno으로 View에 다음/이전 버튼 생성 여부 ◆◆◆◆
			viewStep = boardDao.prevNext(Integer.toString(fno));
			req.setAttribute("viewStep", viewStep);
			req.setAttribute("boardRecord", boardRecord);
			req.setAttribute("viewStep", viewStep);
			boardDao.close();
			
			
			//◆◆◆◆ 토큰에 저장된 회원 정보로 수정/삭제 버튼 생성 여부 ◆◆◆◆
			req.setAttribute("account", (String)claims.get("sub"));
			
			RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/fileBoard/FileBoardView.jsp?fno="+fno+"&nowPage=1");
			dispatcher.forward(req, resp);
			
		}
		else if(Button.equals("delete")) {//▶▶ FileBoardView.jsp에서 글 삭제 후 FileBoardList.jsp로 이동
			//▶▶입력 처리로직 호출(데이타베이스 CRUD작업과 관련된 모델 호출)
			FileBoardDao dao = new FileBoardDao(getServletContext());
			String orignalFileName = req.getParameter("orignalFileName");
			String fno = req.getParameter("fno");
			String nowPage = req.getParameter("nowPage");
			int affected = dao.deleteOnBoard(fno);
			
			String searchColumn = req.getParameter("searchColumn");
			String searchText = req.getParameter("searchText")!=null?req.getParameter("searchText"):null;
			
			if((searchText!=null)&&(searchText!="")) {
				req.setAttribute("searchColumn", searchColumn);
				req.setAttribute("searchText", searchText);
				
				page.put("searchColumn", searchColumn);
				page.put("searchText", searchText);
			}
			
			if(affected==1) {
				FileUtils.deletes(new StringBuffer(orignalFileName),getServletContext().getRealPath("/upload/fileBoard"), ",");
			}
			else {
			}
			
			
		}
		

		FileBoardDao boardDao = new FileBoardDao(getServletContext());
		
		
		//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆Paging Logic◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆//
		PagingModel.setMapForPaging(page, boardDao, req);
		
		int totalRecordCount = Integer.parseInt(page.get(PagingModel.TOTAL_RECORD_COUNT).toString());
		int pageSize=Integer.parseInt(page.get(PagingModel.PAGE_SIZE).toString());
		int blockPage=Integer.parseInt(page.get(PagingModel.BLOCK_PAGE).toString());
		int nowPageInt=Integer.parseInt(page.get(PagingModel.NOWPAGE).toString());
		
		List<FileBoardDto> fileBoardRecords = boardDao.boardRecords(page);
		req.setAttribute("fileBoardRecords", fileBoardRecords);
		req.setAttribute("paging", PagingModel.pagingStyle(totalRecordCount, pageSize, blockPage, nowPageInt,"/boardList?", page));
		req.setAttribute("page", page);
		boardDao.close();
		//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆Paging Logic◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆//

		
		RequestDispatcher dispatcher= req.getRequestDispatcher("/WEB-INF/fileBoard/FileBoardList.jsp?nowPage="+1);
		dispatcher.forward(req, resp);
	}
}
