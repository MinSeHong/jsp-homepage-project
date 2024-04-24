<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="css/iframe.css">
    <link rel="stylesheet" type="text/css" href="css/boardList.css">
    <title>Title</title>
</head>
<body>
    <div class="header-wrap">
        <div class="header">
            Bulletin Board
        </div>
        
        <div class="table-style table-header-style">
            <div class="table-column">번호</div>
            <div class="table-column">제목</div>
            <div class="table-column">작성자</div>
            <div class="table-column">작성일</div>
            <div class="table-column">조회수</div>
        </div>
    </div>

<div class="browser">
	<c:if test="${empty boardRecords}">
		<a href="<c:url value="/boardList"/>">
		    <div class="table-style table-body-style">
		        <div class="table-column table-column-number">X</div>
		        <div class="table-column">no record</div>
		        <div class="table-column">no record</div>
		        <div class="table-column">no record</div>
		        <div class="table-column">X</div>
	        </div>
	    </a>
	    <a href="<c:url value="/boardList"/>">
		    <div class="table-style table-body-style">
		        <div class="table-column table-column-number">X</div>
		        <div class="table-column">no record</div>
		        <div class="table-column">no record</div>
		        <div class="table-column">no record</div>
		        <div class="table-column">X</div>
	        </div>
	    </a>
	</c:if>
	
	<c:forEach var="record" items="${boardRecords}">
	    <a href="<c:url value="/view?bno=${record.bno}&nowPage=${page.nowPage}"/><c:if test="${not empty searchText}">&searchColumn=${searchColumn}</c:if><c:if test="${not empty searchText}">&searchText=${searchText}</c:if>">
	        <div class="table-style table-body-style">
	                <div class="table-column table-column-number">${record.postrank}</div>
	                <div class="table-column">${record.title} <span class="comment-count">[${record.commentcount}]</span></div>
	                <div class="table-column">${record.name}</div>
	                <div class="table-column">${record.postdate}</div>
	                <div class="table-column">${record.hitcount}</div>
	        </div>
	    </a>
	</c:forEach>
	

	<div class="paging-style">
	    <form method="post" action="<c:url value="/write"/>">
	    	<input type="submit" class="button-box-style" value="Write board">
	    </form>
	    
	    <form method="get" action="/boardList">
		    <select class="select-style" name="searchColumn">
		        <option value="title" selected>TITLE</option>
		        <option value="name">WRITER</option>
		        <option value="content">CONTENT</option>
		    </select>
		    <input class="input-box-style" type="text" placeholder="Search" autocomplete="off" name="searchText" value="${param.searchText}">
	    	<input type="hidden" name="SubmitBoardList" value="search">
	    </form>
	    
	    
		<div class="paging-box">${paging}</div>
	</div>
</div>
</body>
</html>