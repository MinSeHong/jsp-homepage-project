<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="css/f_iframe.css">
    <link rel="stylesheet" type="text/css" href="css/f_view.css">
    <title>Title</title>
</head>
<body>

    <div class="header-wrap">
        <div class="header">
            Image Board
        </div>

        <div class="table-style table-header-style">
            <div class="table-column">제목</div>
            <div class="table-column" onclick="popUpProfile()">작성자</div>
            <div class="table-column">조회수</div>
        </div>
    </div>

<div class="browser">
    <div class="table-style table-body-style">
        <div class="table-column">${boardRecord.title}</div>
        <div class="table-column" onclick="popUpProfile()">${boardRecord.name}</div>
        <div class="table-column">${boardRecord.hitcount}</div>
    </div>
    <div class="file-browser">
        <div class="upload-browser">

            <c:forEach var="file" items="${fn:split(boardRecord.attachfile,',')}">
		        <div class="upload-item">
		            <a href='<c:url value="/download?filename=${file}"/>'><img src="/thumbnail/${file}" width="100%" height="100%" onerror="this.src='../images/noImage.jpg'"></a>
		        </div>
		        <div class="file-detail" style="margin-top: 2px;">
		            <span>${file}</span></br>
		        </div>
	        </c:forEach>
	        
        </div>
    </div>
    <div class="editor-style textarea-style">${boardRecord.content}</div>
</div>
<div class="paging-style">
	<c:if test="${not empty viewStep.NEXT and viewStep.NEXT!=null}">
		<button class="button-box-style b1" onClick="location.href='/fileBoardView?fno=${viewStep.NEXT}&nowPage=${param.nowPage}<c:if test="${not empty param.searchText}">&searchColumn=${param.searchColumn}</c:if><c:if test="${not empty param.searchText}">&searchText=${param.searchText}</c:if>'">이전 글</button>
	</c:if>
    <button class="button-box-style b2" onclick="popUpComment()">댓글 목록</button>
    <button class="button-box-style b3" ><a href='<c:url value="/fileBoardList?nowPage=${param.nowPage}"/><c:if test="${not empty param.searchText}">&searchColumn=${param.searchColumn}</c:if><c:if test="${not empty param.searchText}">&searchText=${param.searchText}</c:if>'>나가기</a></button>
    <c:if test="${not empty viewStep.PREV and viewStep.PREV!=null}">
    	<button class="button-box-style b4" onClick="location.href='/fileBoardView?fno=${viewStep.PREV}&nowPage=${param.nowPage}<c:if test="${not empty param.searchText}">&searchColumn=${param.searchColumn}</c:if><c:if test="${not empty param.searchText}">&searchText=${param.searchText}</c:if>'">다음 글</button>
    </c:if>
    <c:if test="${requestScope.account eq boardRecord.id}">
		<button class="button-box-style b5" onclick="popUpDelete()">삭제</button>
		<form class="edit-form" action='/fileEdit' method="post">
			<!--
				<button class="button-box-style" style="width:50px; margin-left:-24px;">수정</button>
			-->
			<input type="hidden" value="${param.nowPage}" name="nowPage"></input>
			<input type="hidden" value="${param.fno}" name="fno"></input>
	    </form>
    </c:if>
</div>

<!-- 상대 프로필 보기 -->
<div class="pop-up-layout-profile" onclick="popUpProfileClose()">
    <div class="profile-3d-card">

        <div class="profile-wrap">
            <div class="profile-icon"></div>
            <span class="nav-div-style profile-name">${accountCard.name}</span>
            <span class="nav-div-style phone-number">${accountCard.pnumber}</span>
            <div class="nav-div-style profile-email">${accountCard.email}</div>
        </div>


        <div class="certification-wrap">
            <div class="nav-div-style profile-introduce">${accountCard.introduce}</div>
            <div class="profile-detail-wrap">
                <div class="nav-div-style profile-detail">
                <c:if test="${fn:contains(accountCard.hobby,'POLITICS')}">정치</c:if>
                </div>
                <div class="nav-div-style profile-detail">
                <c:if test="${fn:contains(accountCard.hobby,'ECONOMY')}">경제</c:if>
                </div>
                <div class="nav-div-style profile-detail">
                <c:if test="${fn:contains(accountCard.hobby,'MUSIC')}">음악</c:if>
                </div>
            </div>
            <div class="profile-detail-wrap">
                <div class="nav-div-style profile-detail">
                <c:if test="${fn:contains(accountCard.hobby,'HISTORY')}">역사</c:if>
                </div>
                <div class="nav-div-style profile-detail">
                <c:if test="${fn:contains(accountCard.hobby,'SCIENCE')}">과학</c:if>
                </div>
                <div class="nav-div-style profile-detail">
                <c:if test="${fn:contains(accountCard.hobby,'MATH')}">수학</c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="pop-up-layout-delete">
    <div class="delete-3d-card">
        <div class="delete-message">Are you sure you want to delete it?</div>
        <form class="delete-form" action='/fileBoardList' method="post">
            <button class="delete-button" name="SubmitBoardList" value="delete">YES</button>
            <input type="hidden" class="formParameter" name="fno" value="${boardRecord.fno}">
            <input type="hidden" class="formParameter" name="nowPage" value="${param.nowPage}">
            <input type="hidden" name="orignalFileName" value="${boardRecord.attachfile}"/>
            <input type="hidden" value="${param.searchColumn}" name="searchColumn"></input>
			<input type="hidden" value="${param.searchText}" name="searchText"></input>
        </form>
        <button class="delete-button" onclick="deleteButtonClose()">NO</button>
    </div>
</div>

<div class="pop-up-layout-comment" onclick="commentButtonClose()">
    <div class="comment-3d-card">
        <iframe src="<c:url value="/fcomment?fno=${param.fno}"/>" id="browserFrame" width="100%" height="95%">
        </iframe>
    </div>
</div>
<script>
    popUpDelete = () => {
        document.querySelector(".pop-up-layout-delete").style="display:block;";
    }
    deleteButtonClose = () =>{
        document.querySelector(".pop-up-layout-delete").style="display:none;";
    }
    
    popUpComment = () => {
        document.querySelector(".pop-up-layout-comment").style="display:block;";
    }
    
    commentButtonClose = () =>{
        document.querySelector(".pop-up-layout-comment").style="display:none;";
    }

    popUpProfile = () => {
        document.querySelector(".pop-up-layout-profile").style="display:block;";
    }

    popUpProfileClose = () => {
        document.querySelector(".pop-up-layout-profile").style="display:none;";
    }
</script>
</body>
</html>