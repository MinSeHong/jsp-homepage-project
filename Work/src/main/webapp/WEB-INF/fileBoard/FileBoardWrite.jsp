<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="../css/f_iframe.css">
    <link rel="stylesheet" type="text/css" href="../css/f_editor.css">
    <title>Title</title>
    <style>
        .table-body-style{
            margin-bottom: 5px;
            height: 30px;
            line-height: 30px;
            color: black;
        }
		.table-body-style .table-column{
            position: relative;
            background-image: url(./images/paper.jpg);
            background-size: cover;
            box-shadow: 2px 2px 5px 1px rgba(0, 0, 0, 0.683);
        }
        .button-box-style:nth-child(1){
            right:150px;
        }
        .button-box-style:nth-child(2){
            right:80px;
        }
        .button-box-style:nth-child(3){
            right:10px;
        }
    </style>
</head>
<body>
    <div class="header-wrap">
        <div class="header">
            Image Board
        </div>

        <div class="table-style table-header-style">
            <div class="table-column">제목</div>
            <div class="table-column">작성자</div>
        </div>
    </div>

<div class="browser">
    <div class="table-style table-body-style">
        <div class="table-column table-column-number"><input class="input-box-style" id="title" type="text" placeholder="Input Title" maxlength="30" value="${requestScope.title}"></div>
        <div class="table-column">${requestScope.account}</div>
    </div>
    <div class="file-browser">
        <button class="upload-button" onclick="fileUpload()">
            Image Upload
        </button>
        <div class="upload-browser">
        </div>
    </div>
    <div class="editor-style palette-style">
        <button class="palette-button palette-color" value="red"></button>
        <button class="palette-button palette-color" value="yellow"></button>
        <button class="palette-button palette-color" value="green"></button>
        <button class="palette-button palette-color" value="blue"></button>
        <button class="palette-button palette-color" value="white"></button>
        <button class="palette-button palette-color" value="black"></button>
        <button class="palette-button palette-color" value="black"></button>  
        <input type="color" class="palette-button palette-color-select"></button>&nbsp;
        <button class="palette-button palette-font-decoration" value="bold"><b style="pointer-events: none;">B</b></button>
        <button class="palette-button palette-font-decoration" value="italic"><i style="pointer-events: none;">i</i></button>
        <button class="palette-button palette-font-decoration" value="underline"><u style="pointer-events: none;">U</u></button>
        <button class="palette-button palette-font-decoration" value="strikeThrough"><s style="pointer-events: none;">S</s></button>
        <button class="palette-button palette-font-decoration" value="insertOrderedList"><b style="pointer-events: none;">Ol</b></button>
        <button class="palette-button palette-font-decoration" value="insertUnorderedList"><b style="pointer-events: none;">Ul</b></button>
        
    </div>
    <div contenteditable="true" id="contentEditable" class="editor-style textarea-style" spellcheck="false">${requestScope.content}</div>
</div>
<div class="paging-style">
	<form action="/fileBoardList" method="post" onsubmit="return writeValidate();" enctype="multipart/form-data">
    	<button class="button-box-style" name="SubmitBoardList" value="write">작성</button>
    	<input type="hidden" id="titleParameter" name="titleParameter"></input>
		<input type="hidden" id="contentParameter" name="contentParameter"></input>
        <input id="fileName" type="file" name="fileName" accept="image/*" multiple/>
    </form>
    <button class="button-box-style" name="reset" onclick="popUpReset()">초기화</button>
    <button class="button-box-style" name="out"><a href='<c:url value="/fileBoardList"/>'>나가기</a></button>
</div>

<div class="pop-up-layout-reset">
    <div class="reset-3d-card">
        <div class="reset-message">Are you sure you want to delete it?</div>
        <button class="reset-button" onclick="ResetButtonYes()">YES</button>
        <button class="reset-button" onclick="ResetButtonNo()">NO</button>
    </div>
</div>

<script>
    var palette_style = document.querySelector(".editor-style");
	var contentEditable = document.querySelector("#contentEditable");
	var contentParameter = document.querySelector("#contentParameter");
	var title = document.querySelector("#title");
	var titleParameter = document.querySelector("#titleParameter");
	
	
    palette_style.addEventListener("click",(e)=>{
        if(e.target.className =="palette-button palette-color"){
            document.execCommand('foreColor', false, e.target.value);
        }
        if(e.target.className =="palette-button palette-font-decoration"){
            document.execCommand(e.target.value);
        }
        contentParameter.value=contentEditable.innerHTML;
    });

    
    palette_style.addEventListener("change",(e)=>{
    if(e.target.className =="palette-button palette-color-select"){
            e.target.previousElementSibling.style.backgroundColor=e.target.value;
            e.target.previousElementSibling.value=e.target.value;
        };
    });
    
    contentEditable.addEventListener("keyup",()=>{
    	contentParameter.value=contentEditable.innerHTML;
    });
    
    
    title.addEventListener("keyup",()=>{
    	titleParameter.value=title.value;
    });
    
       
    animationValidate= (name,message)=>{
        name.focus();
        name.value="";
        name.placeholder=message;
    }

    
    writeValidate = ()=>{
        if(titleParameter.value.length<=5){
            animationValidate(title,"Please input at least 6 Characters");
            return false;
        }
        
        else if(contentParameter.value.length<=0){
        	contentEditable.focus();
            return false;
        }
        console.log("확인 끝");
        return true;
    }
    

    popUpReset = () => {
        document.querySelector(".pop-up-layout-reset").style="display:block;";
    }
    ResetButtonNo = () =>{
        document.querySelector(".pop-up-layout-reset").style="display:none;";
    }
    ResetButtonYes = () =>{
        document.querySelector(".pop-up-layout-reset").style="display:none;";
        contentParameter.value="";
        contentEditable.innerHTML="";
        titleParameter.value="";
        title.value="";
    }

    var upload_Browser = document.querySelector('.upload-browser');


    document.querySelector('#fileName').onchange=(e)=>{
        upload_Browser.innerHTML="";
        for(var image of event.target.files){
            var reader = new FileReader();
            reader.readAsDataURL(image);
                reader.onload=(event)=>{
                var div = document.createElement('div');
                div.setAttribute('class','upload-item');
                var img = document.createElement('img');
                img.setAttribute('width','100%');
                img.setAttribute('height','100%');
                img.setAttribute('onerror',"this.src='../images/noImage.jpg'");
                img.setAttribute('style','-webkit-user-drag: none; box-shadow: 2px 2px 5px 1px rgba(0, 0, 0, 0.683);');
                img.src=event.target.result;
                div.append(img);
                upload_Browser.append(div);
                console.log(image.size)
            };
        }

	};

    var fileName = document.querySelector('#fileName');

    fileUpload = () =>{
        fileName.click();
    }
</script>
</body>
</html>

