<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Title</title>
<style>

body{
	overflow:hidden;
	margin:0 auto;
}

p {
  color: whitesmoke;
  margin: 6px 6px 20px 6px;
  font-size:14px;
}

span {
  display: inline-block;
  max-width: 75%;
  font-size:12px;
  padding: 5px 10px;
  position: relative;
  word-wrap: break;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.5);
  transform-origin: 50% 50%;
  animation: pop 0.5s ease-in-out 1;
  animation-fill-mode: both;
}

@keyframes pop{
	0%{scale:1 1;}
    50%{scale:1.1 1;}
    100%{scale:1 1;}
}

.ext {
  text-align: left;
}

.ext span {
  background: rgba(0,0,0,0.8);
}

.int {
  text-align: right;
}

.int span {
  background: rgba(0,0,0,0.3);
  right: 0;
}

#chatArea{
	position:absolute;
	display:flex;
	flex-direction:column;
	width:100%;
	height:100%;
	align-items: center;
	margin:auto;
}

#chatMessage{
 	overflow-y: scroll;
 	height:345px;
 	width:100%;
 	margin-bottom:5px;
}

#chatMessage::-webkit-scrollbar {
    display: none;
}

#chatMessage::-webkit-scrollbar-track{
	display: none;
}

#chatMessage::-webkit-scrollbar-thumb {
    display: none;
}

#message::placeholder{
	color:black;
}

#message{
	text-align:center;
	height:30px;
	width:95%;
	border:1px double rgba(0,0,0,0.5);
	background-color: rgba(255, 255, 255, 0.654);
	box-shadow: 2px 2px 7px 1px rgba(0, 0, 0, 0.5);
	box-sizing: border-box;
	margin-bottom:5px;
	outline: none;
}
#message:focus{
  animation: pop 0.5s ease-in-out 1;
  animation-fill-mode: both;
}
.writer{
	margin-bottom:5px;
}

</style>
		<div id="chatArea">
			<div id="chatMessage">
			
			</div>
			<input type="text" id="message" autocomplete="off" placeholder="chatting" maxlength="20">
			<input type="hidden" id="chattingName" value="${accountProfile.name}"/>
		</div>

<script>
	
	var chattingName = document.querySelector('#chattingName');
	let wscoket;

	
	connect = ()=>{
		wsocket = new WebSocket("ws://localhost:8989/<c:url value="/chatting"/>")
		console.log(wsocket);
		//서버와 연결된 웹 소켓에 이벤트 등록
		wsocket.onopen =open;
		wsocket.onclose=()=>{appendMessage("Connection lost...");};
		wsocket.onmessage =receive;
		wsocket.onerror=(e)=>{console.log('에러:',e)};
	};
	
	connect();
	
	
	function open(){
		//서버로 연결한 사람의 정보(닉네임) 전송
		//msg:kim가(이) 입장했어요
		//사용자가 입력한 닉네임 저장	
		wsocket.send(`msg:\${chattingName.value}가(이)<br/> 입장했어요`);
		appendMessage('<p class="ext"><span>Connect OK</span></p>');
	}//////////
	
	//서버에서 메시지를 받을때마다 호출되는 함수 
	function receive(e){//e는 message이벤트 객체
		if(e.data.substring(0,4).toUpperCase()==='MSG:')
			//서버로부터 받은 메시지를 msg:부분을 제외하고 div에 출력
			appendMessage('<p class="ext"><span>'+e.data.substring(4)+'</span></p>')
	}////////////
	
	
	//사용자가 입력한 메시지(확인용) 또는 서버로부터 받은 메시지를 채팅창(div)에 출력하는 함수
	function appendMessage(msg){
		const chatMessage=document.querySelector('#chatMessage');
		chatMessage.innerHTML+=msg;
		chatMessage.scrollTop=chatMessage.scrollHeight;
	}
	
	
	
	const text=document.querySelector('#message');
	text.onkeypress=(e)=>{
		if(e.keyCode===13 && text.value.length>=2){
			wsocket.send(`msg:\${chattingName.value}<br/>\${text.value}`);
			appendMessage('<p class="int"><span>'+text.value+'</span></p>');	
			text.value="";
			text.focus();
		}
		
	};
</script>
</body>
</html>