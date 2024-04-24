package model;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import service.DaoService;

public class PagingModel {
	
	public static final String START="start";
	public static final String END="end";
	public static final String TOTAL_RECORD_COUNT="totalRecordCount";
	public static final String PAGE_SIZE="pageSize";
	public static final String BLOCK_PAGE="blockPage";
	public static final String TOTAL_PAGE="totalPage";
	public static final String NOWPAGE="nowPage";

	
	public static String pagingStyle(int totalRecordCount,int pageSize,int blockPage,int nowPage,String page, Map search){
		String pagingStr="";
		
		//■■■■■■ 전체페이지 구하는 곳 ■■■■■■
		int totalPage= (int)(Math.ceil(((double)totalRecordCount/pageSize)));
		
		int intTemp = ((nowPage - 1) / blockPage) * blockPage + 1;

		//■■■■■■ 처음 / 이전 페이지 링크 ■■■■■■
		if(intTemp != 1){
			if(search.get("searchText")!=null && search.get("searchText").toString().length()!=0) {
				pagingStr+="<div class=\"page-item\"><a class=\"page-link\" href='"+page+NOWPAGE+"=1&searchColumn="+search.get("searchColumn")+"&searchText="+search.get("searchText")+"&SubmitBoardList=search'><span class=\"page-number\">⤺</span></a></div>\r\n" + 
						"<div class=\"page-item\"><a class=\"page-link\" href='"+page+NOWPAGE+"="+(intTemp -blockPage)+"&searchColumn="+search.get("searchColumn")+"&searchText="+search.get("searchText")+"&SubmitBoardList=search'><span class=\"page-number\">⇦</a></span></div>";   
			}
			else {
				pagingStr+="<div class=\"page-item\"><a class=\"page-link\" href='"+page+NOWPAGE+"=1'><span class=\"page-number\" style='font-size:20px; padding-bottom:1px;'>⤺</span></a></div>\r\n" + 
						"<div class=\"page-item\"><a class=\"page-link\" href='"+page+NOWPAGE+"="+(intTemp -blockPage)+"'><span class=\"page-number\" style='font-size:20px; padding-bottom:3px;'>⇦</a></span></div>";   
			}
		}
		
		
		//■■■■■■ 페이지 표시 제어 ■■■■■■
		int blockCount = 1;
		
		//■■■■■■블락 페이지 수만큼 혹은 마지막 페이지가 될때까지 페이지를 표시 ■■■■■■ 
		while(blockCount <= blockPage && intTemp <= totalPage){  // 페이지 오버 를 체크 // 현재 페이지를 의미함
			if(intTemp == nowPage){  
				if(search.get("searchText")!=null && search.get("searchText").toString().length()!=0) {
					pagingStr+="<div class=\"page-item page-now\"><a class=\"page-link\" href='"+page+NOWPAGE+"="+intTemp+"&searchColumn="+search.get("searchColumn")+"&searchText="+search.get("searchText")+"&SubmitBoardList=search'><span class=\"page-number\">"+intTemp+"</span></a></div>";
				}
				else {
					pagingStr+="<div class=\"page-item page-now\"><a class=\"page-link\" href='"+page+NOWPAGE+"="+intTemp+"'><span class=\"page-number\">"+intTemp+"</span></a></div>";
				}
				
			}
		    else {
				if(search.get("searchText")!=null && search.get("searchText").toString().length()!=0) {
					pagingStr+="<div class=\"page-item\"><a class=\"page-link\" href='"+page+NOWPAGE+"="+intTemp+"&searchColumn="+search.get("searchColumn")+"&searchText="+search.get("searchText")+"&SubmitBoardList=search'><span class=\"page-number\">"+intTemp+"</span></a></div>";
				}
				else {
					pagingStr+="<div class=\"page-item\"><a class=\"page-link\" href='"+page+NOWPAGE+"="+intTemp+"'><span class=\"page-number\">"+intTemp+"</span></a></div>";
				} 
		     } 
			intTemp = intTemp + 1;
			blockCount = blockCount + 1;
		}

		//■■■■■■ 다음 / 마지막 페이지 링크 ■■■■■■
		if(intTemp <= totalPage){
			if(search.get("searchText")!=null && search.get("searchText").toString().length()!=0) {
				pagingStr+="<div class=\"page-item\"><a class=\"page-link\" href='"+page+NOWPAGE+"="+intTemp+"&searchColumn="+search.get("searchColumn")+"&searchText="+search.get("searchText")+"&SubmitBoardList=search'><span class=\"page-number\">⇨</a></span></div>\r\n" + 
						"<div class=\"page-item\"><a class=\"page-link\" href='"+page+NOWPAGE+"="+totalPage+"&searchColumn="+search.get("searchColumn")+"&searchText="+search.get("searchText")+"&SubmitBoardList=search'><span class=\"page-number\">⤻</a></span></div>";
			}
			else {
				pagingStr+="<div class=\"page-item\"><a class=\"page-link\" href='"+page+NOWPAGE+"="+intTemp+"'><span class=\"page-number\" style='font-size:20px; padding-bottom:3px;'>⇨</a></span></div>\r\n" + 
						"<div class=\"page-item\"><a class=\"page-link\" href='"+page+NOWPAGE+"="+totalPage+"'><span class=\"page-number\" style='font-size:20px; padding-bottom:1px;'>⤻</a></span></div>";
			}				   
		}
		return pagingStr;
	}
	
	
	public static <T extends DaoService> void setMapForPaging(Map map,T dao,HttpServletRequest request) {
		//■■■■■■페이징을 위한 로직 시작
		///■■■■■■전체 레코드수
		int totalRecordCount = dao.getTotalRecordCount(map);
		//■■■■■■페이지 사이즈와 블락페이지는 서블릿 초기화 파라미터로 설정해 놓은 값
		
		//■■■■■■페이지사이즈
		int pageSize = 20;
		
		//■■■■■■블락페이지
		int blockPage= 5;
		
		//■■■■■■전체 페이지수
		int totalPage = (int)Math.ceil((double)totalRecordCount/pageSize);
		//■■■■■■현재 페이지 번호
		int nowPage =request.getParameter(NOWPAGE)==null? 1 : Integer.parseInt(request.getParameter(NOWPAGE));
		
		//■■■■■■시작 및 끝 ROWNUM구하기
		int start = (nowPage-1)*pageSize+1;
		int end = nowPage*pageSize;	
		//■■■■■■페이징을 위한 로직 끝	
		map.put(START,start);	
		map.put(END,end);	
		map.put(TOTAL_RECORD_COUNT, totalRecordCount);
		map.put(PAGE_SIZE, pageSize);
		map.put(BLOCK_PAGE, blockPage);
		map.put(TOTAL_PAGE, totalPage);
		map.put(NOWPAGE, nowPage);			
	}////////////////////////////
}
