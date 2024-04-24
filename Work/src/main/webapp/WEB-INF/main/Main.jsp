<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="css/iframe.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <title>Profile</title>
</head>
<body>
    <script>
        history.pushState(null, null, location.href);
        window.onpopstate = function () {
            history.go(1);
        };
    </script>
    <div class="menu-container">
        <div class="menu-item">
            <div class="menu-button" onclick="popUpMenu()">
                Profile
            </div>
            <div class="menu-button" onclick="location.href='<c:url value="/Logout"></c:url>'">
                Logout
            </div>
            <div class="menu-button">
                <div id="clock"></div>
            </div>
        </div>
        <div class="menu-item iframe">
            <iframe src="<c:url value="/profile"/>" width="100%" height="100%"></iframe>
        </div>
    </div>


    <div class="container">
        <div class="container-item c1">
            <div class="profile-wrap">
                <div class="profile-icon"></div>
                <span class="nav-div-style profile-name">${accountProfile.name}</span>
                <span class="nav-div-style profile-id">${accountProfile.id}</span>
                <span class="nav-div-style profile-email">${accountProfile.email}</span>
            </div>
            <div class="title-wrap">
                <div class="nav-div-style title-style" onclick = "browserFrame('<c:url value="/boardList?nowPage=1"/>')">Board</div>
                <div class="nav-div-style title-style" onclick = "browserFrame('<c:url value="/fileBoardList?nowPage=1"/>')">File Board</div>
                <div class="nav-div-style title-style" onclick = "browserFrame('<c:url value="/boardList?nowPage=1"/>')">Member List</div>
            </div>
            <div class="chatting-wrap">
            <iframe src="<c:url value="/chatting"/>" id="browserFrame" width="100%" height="100%">
            </iframe>
            </div>
        </div>
        <div class="container-item c2">
            <iframe class="i" src="<c:url value="/boardList?nowPage=1"/>" id="browserFrame" width="100%" height="100%" style="margin: 10px 0px;">
            </iframe>
        </div>
    </div>

<script>
    browserFrame= (url) => {
        document.querySelector(".i").src = url;
    };
    
    const clock = document.querySelector("#clock");
    setInterval(function(){
        const date = new Date();
        clock.innerHTML=`\${String(date.getHours()).padStart(2, '0')} :  \${String(date.getMinutes()).padStart(2, '0')} : \${String(date.getSeconds()).padStart(2, '0')}`;
    }
    , 500);
    

    var iframe = document.querySelector('.iframe');
    var menuContainer = document.querySelector('.menu-container');
    
    iframe.style.display="none";
    menuContainer.style.height="60px";
    let toggle = true;
    

    popUpMenu = () =>{
        if(toggle){
            iframe.style.display="block";
            menuContainer.style.height="500px";
            toggle=!toggle;
        }
        else if(!toggle){
            iframe.style.display="none";
            menuContainer.style.height="60px";
            toggle=!toggle;
        }
    };
</script>
</html>