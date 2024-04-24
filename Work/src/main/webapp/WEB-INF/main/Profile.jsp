<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
        overflow: hidden;
    }
    .container{
        position: absolute;
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: row;
        box-sizing: border-box;
        justify-content: space-evenly;
        place-items: center center;
    }

    .item::-webkit-scrollbar{
        display: none;
    }

    .item::-webkit-scrollbar-thumb{
        background-color: rgba(0, 0, 0, 0.322);
        
    }


    .item{
        border: 5px solid #ffffffe3;
        border-radius: 10px;
        width: 49%;
        height: 92%;
        box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.683);
        background-color:#ffffffe3;
        overflow-y: scroll;
    }

/********************************************************* item:nth-child(1) ***************************/
    .item:nth-child(1){
        width: 250px;
        align-items: center;
    }

    .profile-title{
        margin: auto;
        height: 40px;
        line-height: 40px;
        width: 100%;
        text-align: center;
        font-size: 30px;
        margin-bottom: 10px;
        font-weight: bold;
        color:#0c4705;
        text-shadow: 2px 2px 2px rgba(0, 0, 0, 0.397);
    }

    .profile-text{
        margin: auto;
        height: 30px;
        line-height: 28px;
        text-align: center;
        font-size: 18px;
        font-weight: bold;
        color:black;
        width: 80%;
        margin-top: 20px;
        border-radius: 15px;
        background-color: rgba(0, 0, 0, 0.103);
        box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.683);
        border:1px solid rgba(0, 0, 0, 0.3);
    }

    .profile-box{
        margin: auto;
        font-size: 14px;
        height: 25px;
        line-height: 25px;
        text-align: center;
        outline: none;
        width: 70%;
        border-radius: 15px;
        box-shadow: inset 2px 2px 5px rgba(0, 0, 0, 0.683);
    }

    .profile-sub-box-layout{
        margin: auto;
        display: flex;
        width: 70%;
        height: 25px;
        justify-content: space-evenly;
        margin-bottom: 20px;
    }

    .profile-sub-box{
        width: 30%;
        height: 100%;
        box-shadow: inset 2px 2px 5px rgba(0, 0, 0, 0.683);
        border-radius: 15px;
        font-size: 15px;
        line-height: 25px;
        text-align: center;
    }

    .g1{
        width: 40%;
    }


    .profile-input-style{
        font-size: 13px;
        height: 25px;
        line-height: 25px;
        width: 75%;
        border-radius: 15px;
        text-align: center;
        margin-left: 30px;
        outline: none;
        border: 0px solid black;
    }

    .profile-introduce{
        margin: auto;
        padding:10px;
        font-size: 15px;
        height: 130px;
        line-height: 20px;
        width: 73%;
        border-radius: 10px;
        text-align: center;
        margin-bottom: 10px;
        outline: none;
        overflow-y:scroll;
    }


    .profile-introduce::-webkit-scrollbar{
        display: none;
    }

    .editable{
        box-shadow: inset 2px 2px 5px rgba(0, 0, 0, 0.683);
    }

    .profile-edit-button-style{
        display: inline-block;
        margin: auto 0;
        outline: none;
        margin-left: 30%;
        width: 100px;
        height: 30px;
        font-size: 15px;
        background-color: #0c4705;
        border-radius: 10px;
        color:#FFECCC;
        margin-bottom: 10px;
    }

	.profile-sub-box, .profile-box, .profile-introduce, .profile-input-style{
		border:1px solid #492100;
		background-color:white;
		font-size:16px;
	}
/********************************************************************************************/


    .item:nth-child(2){
        width: 700px;
        align-items: center;
    }

    .account-item{
        margin: auto;
        height: 30px;
        text-align: center;
        font-size: 18px;
        font-weight: bold;
        color:black;
        width: 94%;
        height: 200px;
        margin-top: 20px;
        border-radius: 15px;
        background-color: rgba(0, 0, 0, 0.103);
        box-shadow: inset 2px 2px 5px rgba(0, 0, 0, 0.683);
        
    }




    .item:nth-child(3){
        width: 260px;
        align-items: center;
    }




    .input-checkbox-style {
        border-radius: 0px;
        margin: 10px;
        scale: 1.7;
    }

    .hobby-text{
        margin-left: 10px;
    }


    .pop-up-layout-leave{
        display: none;
        user-select: none;
        z-index: 100;
        box-sizing: border-box;
        position: absolute;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0);
        transition: all 0.4s linear;
        animation: message 1s linear 1;
        animation-fill-mode: both;
    }

    @keyframes message{

        100%{
            background-color: rgba(0, 0, 0, 0.25);
        }
    }



    .leave-3d-card{
        position: relative;
        text-align: center;
        background-color: #ffffffa9;
        width: 300px;
        height: 80px;
        margin: 20% auto 0 auto;
        font-size: 20px;
        animation-fill-mode: both;
        transform-origin: 50% 50%;
        box-shadow: 4px 4px 8px 1px #0000008a;
        transform: translateY(100px);
        animation: messageSlide 1s ease-in-out 1;
        animation-fill-mode: both;
        border-radius: 10px;
    }

    @keyframes messageSlide{

        100%{
            transform: translateY(0px);
        }
    }

    .leave-message{
        color: black;
        line-height: 40px;
        font-size: 16px;
        text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.5);
    }

    .leave-button{
        position: absolute;
        width: 100px;
        height: 30px;
        background-color: #0b4606;
        color:#FFECCC;
        border:0px solid black;
        box-shadow: 2px 2px 2px 1px rgba(0, 0, 0, 0.384);
        border-radius: 10px;
    }

    .leave-button:hover{
        animation: bound 1s ease-in-out 1;
        animation-fill-mode: both;
    }

    @keyframes bound{
        25%{transform: translateY(-3px);}
        50%{transform: translateY(0px);}
        75%{transform: translateY(-1px);}
    }
    .leave-button:nth-child(2){
        left:10px;
    }

    .leave-button:nth-child(3){
        right:10px;
    }

</style>
<body>
    
    <div class="pop-up-layout-leave">
        <div class="leave-3d-card">
            <div class="leave-message">Are you sure you want to leave it?</div>
            <button class="leave-button" name="SubmitBoardList" onclick="leaveOk()" value="delete">YES</button>
            <button class="leave-button" onclick="popUpLayoutLeave()">NO</button>
        </div>
    </div>




    <div class="container">
        
        <div class="item">
        <form action='<c:url value="/profile"/>' onsubmit="return editValidate();" method="post">
            <div class="profile-title">Profile</div>

            <div class="profile-text">Name</div>
            <div class="profile-box">${accountProfile.name }</div>

            <div  class="profile-text">Id</div>
            <div class="profile-box">${accountProfile.id }</div>

            <div class="profile-text">Gender</div>
            <div class="profile-sub-box-layout">
                <div class="profile-sub-box g1">${accountProfile.gender }</div>
            </div>

            <div class="profile-text">Birth</div>
            <div class="profile-sub-box-layout">
                <div class="profile-sub-box">${fn:split(accountProfile.birthday,',')[0]}</div>
                <div class="profile-sub-box">${fn:split(accountProfile.birthday,',')[1]}</div>
                <div class="profile-sub-box">${fn:split(accountProfile.birthday,',')[2]}</div>
            </div>

            <div  class="profile-text">Email</div>
            <div class="profile-box" style="width:80%; font-size:12px;">${accountProfile.email}</div>

            <div  class="profile-text">Phone</div>
            <input type="text" name="pnumber" id="pnumber" class="profile-input-style editable" maxlength="11" autocomplete="off" value="${accountIntroduce.pnumber}">

            <div  class="profile-text">Introduce</div>
            <div class="profile-introduce editable" name="introduce" id="introduce" contenteditable="true" spellcheck="false">${accountIntroduce.introduce}</div>
            
            <span class="hobby-text">정치</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="POLITICS" <c:if test="${fn:contains(accountProfile.hobby,'POLITICS')}"> checked </c:if> />
            <span class="hobby-text">경제</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="ECONOMY" <c:if test="${fn:contains(accountProfile.hobby,'ECONOMY')}"> checked </c:if> />
            <span class="hobby-text">음악</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="MUSIC" <c:if test="${fn:contains(accountProfile.hobby,'MUSIC')}"> checked </c:if> />
            <br/>
            <span class="hobby-text">역사</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="HISTORY" <c:if test="${fn:contains(accountProfile.hobby,'HISTORY')}"> checked </c:if> />
            <span class="hobby-text">과학</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="SCIENCE" <c:if test="${fn:contains(accountProfile.hobby,'SCIENCE')}"> checked </c:if> />
            <span class="hobby-text">수학</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="MATH" <c:if test="${fn:contains(accountProfile.hobby,'MATH')}"> checked </c:if> />
            <button class="profile-edit-button-style" style="margin-top: 20px">Edit Profile</button>
            <input type="hidden" name="profileControllerButton" value="edit">
            <input type="hidden" id="introduceParameter" name="introduceParameter"></input>
        </form>
        </div>
    


        <div class="item">
            <div class="profile-title">Memo</div>
            <div  class="account-item"></div>
            <div  class="account-item" style="margin-bottom: 10px;"></div>
            
        </div>
        <div class="item">
            <form onsubmit="return leaveValidate();" name="leave" action='<c:url value="/profile"/>' method="post">
                <div class="profile-title">Leave a party</div>
                <div class="profile-text" style="margin-top: 40px;">Id</div>
                <input type="text" id="id" name="id" class="profile-input-style editable"  maxlength="15" autocomplete="off" spellcheck="false">
                <div class="profile-text">Password</div>
                <input type="password" id="password" name="password" class="profile-input-style editable"  maxlength="20" autocomplete="off">
                <div class="profile-text">Password Identify</div>
                <input type="password" id="passwordIdentify" class="profile-input-style editable"  maxlength="20" autocomplete="off">
                <div></div>
                <input type="submit" class="profile-edit-button-style" style="margin-top:30px" value="Delete"></input>
            	<input type="hidden" name="profileControllerButton" value="delete">
            </form>
        </div>
    </div>
    <script>
    
    var introduce = document.querySelector("#introduce");
    var introduceParameter = document.querySelector("#introduceParameter");
    introduceParameter.value=introduce.innerHTML;
    
    
    introduce.addEventListener("keyup",()=>{
    	introduceParameter.value=introduce.innerHTML;
    });
    
        document.addEventListener('keydown', function(e) {
            if(e.target.className.includes('editable')){
                if(e.keyCode === 13) {
                    if(e.target.nextElementSibling.nextElementSibling.className.includes('editable')){
                        e.target.nextElementSibling.nextElementSibling.focus();
                        e.preventDefault();
                    }
                    else{
                        e.preventDefault();
                    }
                }
            }
        });

        leaveValidate= () => {
            let id = document.querySelector("#id");
            let password = document.querySelector("#password");
            let passwordIdentify = document.querySelector("#passwordIdentify");
            let popUpLayoutLeave = document.querySelector(".pop-up-layout-leave");
            if(!(id.value=="${accountProfile.id}")){
                id.value="";
                id.placeholder="ID is not correct";
                animationValidateOnlyFocus(id);
                return false;
            }
            else if(!(password.value==passwordIdentify.value)){
                animationValidateOnlyFocus(passwordIdentify);
                return false;
            }
            else if(!(password.value=="${accountProfile.password}")){
                password.value="";
                passwordIdentify.value="";
                password.placeholder="Password is not correct";
                animationValidateOnlyFocus(password);
                return false;
            }
            popUpLayoutLeave.style.display="block";
            return false;
        }

        leaveOk = () => {
        	document.leave.submit();
        }

        popUpLayoutLeave= () =>{
            let popUpLayoutLeave = document.querySelector(".pop-up-layout-leave");
            popUpLayoutLeave.style.display="none";
        }

        animationValidateOnlyFocus= (name)=>{
            name.focus();
            name.style.backgroundColor="rgba(0,0,0,0.05)";
            setTimeout(()=>{
                name.style.backgroundColor="rgba(0,0,0,0)";
            },1000);
        }


        editValidate = () => {
            let pnumber = document.querySelector("#pnumber");
            let entryHobby = document.querySelectorAll("input[name='entryHobby']:checked");

            if(!((pnumber.value).match(/[0-9]{10,}/))){
                animationValidateOnlyFocus(pnumber);
                pnumber.value="";
                pnumber.placeholder="Enter 10~11 Numbers";
                return false;
            }
            else if(entryHobby.length<=2){
                return false;
            }
           return true;
        }
    </script>
</body>
</html>