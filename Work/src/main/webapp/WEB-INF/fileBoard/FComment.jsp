<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<style>
    body{
        margin: 0;
        box-sizing: border-box;
        overflow: hidden;
    }
    .layout{
        position: absolute;
        width: 100%;
        height: 100%;
        box-sizing: border-box;
       
    }

    .browser{
        overflow-y: scroll;
        display: flex;
        flex-direction: column;
        box-sizing: border-box;
        border: 0px solid black;
        width: 100%;
        margin-top: 3px;
        margin-bottom: 3px;
        height: 79.5%;
        gap: 5px;
        padding-bottom: 5px;
    }

    .browser::-webkit-scrollbar{
        display: none;
    }


    .commentLine{
        width: 100%;
        height: 30px;
        border: 1px solid green;
    }

    .table-style{
        display: flex;
        flex-direction: row;
        gap:1px;
        height: 30px;
        color: black;
        text-align: center;
        margin-left: 1%;
    }

    .table-header-style{
        color: white;
        font-weight: bold;
        font-size: 15px;
        line-height: 25px;
        height: 35px;
    }

    .table-header-style .table-column{
        background-color: #000000de;
        border: 5px double #0a3f07;
        border-style: double none;
        box-shadow: 0px 3px 3px 0.05px rgba(0, 0, 0, 0.233);
    }


    .table-comment-style{
        color: black;
        font-size: 14px;
        line-height: 35px;
        height: 35px;
        
    }

    .table-comment-style .table-column{
        background-color: rgba(0, 0, 0, 0.13);
        border-bottom: 2px solid black;
        box-shadow: 0px 3px 3px 0.05px rgba(0, 0, 0, 0.233);
    }


    /*****************************table Element*****************************/
    .table-header-style .table-column:nth-child(1){
        width: 10%;
        border-radius:10px 0px 0px 10px;
        
    }
    .table-header-style .table-column:nth-child(2){
        width: 76.5%;
    }
    .table-header-style .table-column:nth-child(3){
        width: 12%;
        border-radius:0px 10px 10px 0px;
    }
    .table-column{
        border: 10px;
    }
   
    .table-comment-style .table-column:nth-child(1){
        width: 10%;
        border-radius:10px 0px 0px 10px;
        font-weight: bold;
    }
    .table-comment-style .table-column:nth-child(2){
        width: 76.5%;
    }
    .table-comment-style .table-column:nth-child(3){
        width: 12%;
        border-radius:0px 10px 10px 0px;
        font-weight: bold;
    }

    /*****************************Editor Palette Element*****************************/
    input{
        color:white;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0);
        outline: none;
        border: 0px solid black;
        text-align: center;
    }
    
    
    button{
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0);
        cursor: pointer;
        color:white;
        border: 0px solid black;
    }
    
    a:focus{
        text-decoration: none;
        color:black;
    }
    
    a{
        text-decoration: none;
        color:black;
    }

    .my-comment{
        color:#0b4606;
        cursor: pointer;
    }
    
    .my-comment .table-column:nth-child(2){
        color:red;
    }


    .input-edit{
        color:black;
    }

    .button-Edit, .button-delete{
        color:black;
        font-size: 15px;
        font-weight: bold;
        background-color: #00000071;
    }
	.button-Edit{
		border-radius:0px 10px 10px 0px;
	}
	.button-delete{
		border-radius:10px 0px 0px 10px;
	}
	
	.empty{
	 	width: 100%;
	 	font-size:20px;
	}
	
</style>

<body>
    <div class="layout">
        <div class="table-style table-header-style" style="margin-bottom: 5px;">
            <div class="table-column">작성자</div>
            <div class="table-column">제목</div>
            <div class="table-column">작성일</div>
        </div>
        <div class="browser">
        <c:forEach var="record" items="${cRecords}"> 
            <div class="table-style table-comment-style<c:if test="${requestScope.account eq record.id}"> my-comment </c:if>">
                <div class="table-column">${record.name}</div>
                <div class="table-column<c:if test="${requestScope.account eq record.id}"> edit</c:if>">${record.fcomment}</div>
                <div class="table-column">${record.postdate}</div>
                <input type="hidden" value="${record.fcno}">
            </div>
		</c:forEach>
		
		<c:if test="${empty cRecords}"> 
			<div class="table-style table-comment-style">
		        <div class="empty">no file comment</div>
			</div>
		</c:if>
		
        </div>
        <form method="post" action="<c:url value="/fcomment"/>" onsubmit="return Validate()">
            <div class="table-style table-header-style">
                <div class="table-column comment-explain">댓글작성</div>
                <div class="table-column input-style"><input class="comment-input" type="text" maxlength="39" minlength="5" name="fcomment" autocomplete="off"></div>
                <div class="table-column"><button type="submit" value="commentWrite">작성</button></div>
                <input type="hidden" name="fno" value="${param.fno }">
                <input type="hidden" name="submitCommentController" value="commentWrite">
            </div>
        </form>
    </div>

    <form name="commentController" method="post" action="<c:url value="/fcomment"/>">
    	<input type="hidden" name="fno" value="${param.fno }">
        <input type="hidden" id="fcno" name="fcno">
        <input type="hidden" id="fcomment" name="fcomment" value="sdfsdfdsf">
        <input type="hidden" id="submitCommentController" name="submitCommentController" value="">
    </form>

</body>

<script>
    var browser = document.querySelector('.browser');
    var commentInput = document.querySelector('.comment-input');
    var fcno = document.querySelector('#fcno');
    var fcomment = document.querySelector('#fcomment');
    var submitCommentController = document.querySelector('#submitCommentController');



    browser.addEventListener("click", function(e){
    	console.log(e.target.className);
        if(e.target.className.includes('table-column edit')){
            const inputEdit = document.createElement('input');
            inputEdit.setAttribute('type','text')
            inputEdit.setAttribute('maxlength','39');
            inputEdit.setAttribute('class','input-edit')
            inputEdit.value=e.target.parentElement.children[1].innerText;
            e.target.parentElement.children[1].innerText="";
            e.target.parentElement.children[1].appendChild(inputEdit);

            const buttonEdit = document.createElement('button');
            buttonEdit.setAttribute('class','button-Edit');
            buttonEdit.innerText="Edit";

            const buttonDelete = document.createElement('button');
            buttonDelete.setAttribute('class','button-delete');
            buttonDelete.innerText="Delete";
            
            e.target.parentElement.children[0].innerText="";
            e.target.parentElement.children[0].appendChild(buttonDelete);

            e.target.parentElement.children[2].innerText="";
            e.target.parentElement.children[2].appendChild(buttonEdit);
        }

        else if(e.target.className.includes('button-Edit') && e.target.parentElement.parentElement.children[1].children[0].value.length>=5){
            console.log("수정 부분");
        	fcno.value = e.target.parentElement.parentElement.children[3].value;
            fcomment.value = e.target.parentElement.parentElement.children[1].children[0].value;
            submitCommentController.value="commentEdit";
            e.target.parentElement.parentElement.children[0].innerHTML="";
            e.target.parentElement.parentElement.children[1].innerHTML=e.target.parentElement.parentElement.children[1].children[0].value;
            e.target.parentElement.parentElement.children[2].innerHTML="";
            document.commentController.submit();
        }

        else if(e.target.className.includes('button-delete')){
            fcno.value = e.target.parentElement.parentElement.children[3].value;
            submitCommentController.value="commentDelete";
            e.target.parentElement.parentElement.remove();
            console.log("ㅇㅅㅇ 삭제양");
            document.commentController.submit();
        }
        else{
        	return false;
        }
    })


    Validate = () =>{
        if(commentInput.value.length<=4){
            return false;
        }
        return true;
    }
</script>
</html>