<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="css/f_boardList.css">
    <title>Title</title>
</head>
<body>
    <div class="header-wrap">
        <div class="header">
            Image Board
        </div>
    </div>

	
<div class="browser">

	<c:forEach var="record" items="${fileBoardRecords}">
	        <div class="grid-item">
	        	<a href="<c:url value="/fileBoardView?fno=${record.fno}&nowPage=${page.nowPage}"/><c:if test="${not empty searchText}">&searchColumn=${searchColumn}</c:if><c:if test="${not empty searchText}">&searchText=${searchText}</c:if>">
					<div class="item-image">
						<img src="/thumbnail/${fn:split(record.attachfile,',')[0]}" width="100%" height="100%" onerror="this.src='../images/noImage.jpg'">
					</div>
					<div class="item-writer">
						<span class="item-sub-title">Writer</span></br>
						-${record.name}-
					</div>
					<div class="item-title">
						${record.title}
					</div>
					<div class="item-detail">
						<div class="item-detail-box">
							Comment</br>
							${record.commentcount}
						</div>
						<div class="item-detail-box">
							Hitcount</br>
							${record.hitcount}
						</div>
					</div>
				</a>
	        </div>
	</c:forEach>





	<div class="paging-style">
	    <form method="post" action="<c:url value="/fileWrite"/>">
	    	<input type="submit" class="button-box-style" value="Write board">
	    </form>
	    
	    <form method="get" action="/fileBoardList">
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