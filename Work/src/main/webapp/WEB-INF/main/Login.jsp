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
    <link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>
<script>
    history.pushState(null, null, location.href);
    window.onpopstate = function () {
        history.go(1);
    };
</script>
<c:if test="${error=='OK'}">
	<div class="error-screen">
	    <div class="error-container">
	    	${errorMessage}
	    </div>
    </div>
</c:if>
    <div class="container">
        <input class="checkbox" type="checkbox" id="reg-log" name="reg-log" <c:if test="${flip}"> checked </c:if> />
        <label for="reg-log"></label>
        <div class="card-wrap">
            <div class="card-wrapper">

                <div class="card-front">
                    <h4>Log In</h4>
                    <form action='<c:url value="/main"/>' onsubmit="return loginValidate();" method="post">
                        <input type="text" name="loginId" id="loginId" class="input-box-style" placeholder="Your id" autocomplete="off" maxlength="15">
                        <input type="password" name="loginPassword" id="loginPassword" class="input-box-style" placeholder="Your Password" autocomplete="off" maxlength="15">
                        <input type="submit" class="button" value="submit">
                    </form>
                    <p class="text-center"><a href="#" class="link">find your password</a></p>
                </div>

                <div class="card-back">
                    <h4>Sign Up</h4>
                    <form action="<c:url value="/main"/>" onsubmit="return entryValidate();" method="get">
                        <div class="entry-wrap">
                            <div class="entry-text">Id / Password</div>
                            <input type="text" name="entryId" id="entryId" class="input-box-style" placeholder="Your id" autocomplete="off" maxlength="15" value="${entryAccount.entryId[0]}">
                            <input type="password" name="entryPassword" id="entryPassword" class="input-box-style" placeholder="Your Password" autocomplete="off" maxlength="15" value="${entryAccount.entryPassword[0]}">
                            <input type="password" id="entryPasswordIdentify" class="input-box-style" placeholder="Your Password identify" autocomplete="off" maxlength="15" value="${entryAccount.entryPassword[0]}">
                            <div class="entry-text">Email</div>
                            <input type="text" name="entryEmail" id="entryEmail" class="input-box-style" placeholder="Your Email" autocomplete="off" maxlength="20" value="${entryAccount.entryEmail[0]}">
                            <input type="text" id="entryEmailIdentify" class="input-box-style" placeholder="Your Email identify" autocomplete="off" maxlength="20" value="${entryAccount.entryEmail[0]}">
                            <div class="entry-text" style="margin-top: 10px;">Name</div>
                            <input type="text" name="entryName" id="entryName" class="input-box-style input-box-name" placeholder="Name" autocomplete="off" maxlength="10" value="${entryAccount.entryName[0]}">
                            <div class="entry-text">Birthday</div>
                            <!--여기서부터-->
                            <input type="number" name="entryBirthYear" id="entryBirthYear" class="input-box-style input-box-birthday" placeholder="Year" autocomplete="off" min="1900" max="2023"  value="${entryAccount.entryBirthYear[0]}">
                            <input type="number" name="entryMonth" id="entryMonth" class="input-box-style input-box-birthday" placeholder="Month" autocomplete="off" min="1" max="12"  value="${entryAccount.entryMonth[0]}">
                            <input type="number" name="entryDay" id="entryDay" class="input-box-style input-box-birthday" placeholder="Day" autocomplete="off" min="1" max="31"  value="${entryAccount.entryDay[0]}">
                            <div class="entry-text">Gender</div>
                            <span class="entry-text span" style="margin-left: 17px;">male</span><input type="radio" checked class="input-radio-style" name="entryGender" value="Male"/>
                            <input type="radio" class="input-radio-style" name="entryGender" value="female"  <c:if test="${fn:contains(entryAccount.entryGender[0],'female')}"> checked </c:if> /><span class="entry-text span">female</span>                    
                            <div class="entry-text"  style="margin-top: 40px;">Education</div>
                            <select class="select-style" name="entryEducation" id="entryEducation">
                                <option value="E" selected>Elementary</option>
                                <option value="M" <c:if test="${fn:contains(entryAccount.entryEducation[0],'M')}"> selected </c:if> >Middle school</option>
                                <option value="H" <c:if test="${fn:contains(entryAccount.entryEducation[0],'H')}"> selected </c:if> >High school</option>
                                <option value="C" <c:if test="${fn:contains(entryAccount.entryEducation[0],'C')}"> selected </c:if> >Colleague</option>
                            </select>
                            <div class="entry-text"><br/>Hobby<br/>Choose at least 3</div>
                            <span class="entry-text span">정치</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="POLITICS" <c:if test="${fn:contains(hobby,'POLITICS')}"> checked </c:if> />
                            <span class="entry-text span">경제</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="ECONOMY" <c:if test="${fn:contains(hobby,'ECONOMY')}"> checked </c:if> />
                            <span class="entry-text span">음악</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="MUSIC" <c:if test="${fn:contains(hobby,'MUSIC')}"> checked </c:if> />
                            <span class="entry-text span">역사</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="HISTORY" <c:if test="${fn:contains(hobby,'HISTORY')}"> checked </c:if> />
                            <span class="entry-text span">과학</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="SCIENCE" <c:if test="${fn:contains(hobby,'SCIENCE')}"> checked </c:if> />
                            <span class="entry-text span">수학</span><input type="checkbox" class="input-checkbox-style" name="entryHobby" value="MATH" <c:if test="${fn:contains(hobby,'MATH')}"> checked </c:if> />
                            <input type="submit" class="button" value="submit"  style="margin-top: 40px;">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div id="clock"></div>
<script>
  	//************************* Clock Next *************************//
    const clock = document.querySelector("#clock");
    setInterval(function(){
        const date = new Date();
        clock.innerHTML=`\${String(date.getHours()).padStart(2, '0')} :  \${String(date.getMinutes()).padStart(2, '0')} : \${String(date.getSeconds()).padStart(2, '0')}`;
    }
    , 500);
    //************************* Input Next *************************//
    document.addEventListener('keydown', function(e) {
        if(e.target.nodeName=='INPUT'){
            if (e.keyCode === 13) {
                if(e.target.nextElementSibling.nodeName=='INPUT'){
                    if(!(e.target.nextElementSibling.value=='submit'))e.target.nextElementSibling.value="";
                    e.target.nextElementSibling.focus();
                    e.preventDefault();
                }
                else if(e.target.nextElementSibling.nextElementSibling.nodeName=='INPUT'){
                    e.target.nextElementSibling.nextElementSibling.innerHTML="";
                    e.target.nextElementSibling.nextElementSibling.focus();
                    e.preventDefault();
                }
                else{
                    e.preventDefault();
                }
            }
        }
    });
    //************************* Validate Check *************************//



    
    console.log(document.querySelector("#entryBirthYear").value.length);

    loginValidate = () =>{
        let loginId = document.querySelector("#loginId");
        let loginPassword = document.querySelector("#loginPassword");
        
        if(loginId.value.length<=6){
            animationValidate(loginId,"Please enter at least 7 characters");
            return false;
        }
        else if(loginPassword.value.length<=8){
            animationValidate(loginPassword,"Please enter at least 9 characters");
            return false;
        }
        return true;
    }

    entryValidate = ()=>{
        let entryId = document.querySelector("#entryId");
        let entryPassword = document.querySelector("#entryPassword");
        let entryPasswordIdentify = document.querySelector("#entryPasswordIdentify");
        

        let entryEmail = document.querySelector("#entryEmail");
        let entryEmailIdentify = document.querySelector("#entryEmailIdentify");

        let entryName = document.querySelector("#entryName");
        let entryBirthYear = document.querySelector("#entryBirthYear");
        let entryMonth = document.querySelector("#entryMonth");
        let entryDay = document.querySelector("#entryDay");
        let entryHobby = document.querySelectorAll("input[name='entryHobby']:checked");
        
        
        if(!((entryId.value).match(/[a-zA-z0-9]{7,}/))){
            animationValidate(entryId,"Please enter at least 7 ENG or Number");
            return false;
        }
        else if(entryPassword.value.length<=8){
            animationValidate(entryPassword,"Please enter at least 9 characters");
            return false;
        }
        else if(!(entryPassword.value==entryPasswordIdentify.value)){
            animationValidate(entryPasswordIdentify,"Password is not equal");
            return false;
        }
        else if(!((entryEmail.value).match(/[a-zA-z0-9]+@[a-z]+\.[a-z]{2,3}/))){
            animationValidate(entryEmail,"EX) Example@gmail.com");
            return false;
        }
        else if(!(entryEmail.value==entryEmailIdentify.value)){
            animationValidate(entryEmailIdentify,"Email is not equal");
            return false;
        }
        else if(!((entryName.value).match(/^[a-zA-z]{3,10}$/) || (entryName.value).match(/^[가-힣]{3,10}$/))){
            animationValidate(entryName,"Only input ENG or KR");
            return false;
        }
        else if(entryBirthYear.value<=0){
            animationValidateOnlyFocus(entryBirthYear);
            return false;
        }
        else if(entryMonth.value.length<=0){
            animationValidateOnlyFocus(entryMonth);
            return false;
        }
        else if(entryDay.value.length<=0){
            animationValidateOnlyFocus(entryDay);
            return false;
        }
        else if(entryHobby.length<=2){
            return false;
        }
        return true;
    }
    //************************* Validate animation *************************//
    animationValidate= (name,message)=>{
        name.focus();
        name.value="";
        name.placeholder=message;
        name.style.border="1px solid red";
        setTimeout(()=>{
            name.style.border="1px solid black";
        },1000);
    }

    animationValidateOnlyFocus= (name)=>{
        name.focus();
        name.style.border="1px solid red";
        setTimeout(()=>{
            name.style.border="1px solid black";
        },1000);
    }
</script>

</body>
</html>