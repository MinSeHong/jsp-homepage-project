
/* Drop Tables */

DROP TABLE authority CASCADE CONSTRAINTS;
DROP TABLE blacklist CASCADE CONSTRAINTS;
DROP TABLE board_comment_likes CASCADE CONSTRAINTS;
DROP TABLE board_comment_reports CASCADE CONSTRAINTS;
DROP TABLE board_comment CASCADE CONSTRAINTS;
DROP TABLE board_likes CASCADE CONSTRAINTS;
DROP TABLE board_reports CASCADE CONSTRAINTS;
DROP TABLE board_scraps CASCADE CONSTRAINTS;
DROP TABLE board CASCADE CONSTRAINTS;
DROP TABLE calendar CASCADE CONSTRAINTS;
DROP TABLE chatting_chatroom CASCADE CONSTRAINTS;
DROP TABLE chatting_list CASCADE CONSTRAINTS;
DROP TABLE diet CASCADE CONSTRAINTS;
DROP TABLE exercise CASCADE CONSTRAINTS;
DROP TABLE friendship CASCADE CONSTRAINTS;
DROP TABLE gameroom_ranking CASCADE CONSTRAINTS;
DROP TABLE inbody CASCADE CONSTRAINTS;
DROP TABLE notification CASCADE CONSTRAINTS;
DROP TABLE point CASCADE CONSTRAINTS;
DROP TABLE ranking CASCADE CONSTRAINTS;
DROP TABLE social CASCADE CONSTRAINTS;
DROP TABLE account CASCADE CONSTRAINTS;
DROP TABLE chatting_chatroom_notice CASCADE CONSTRAINTS;
DROP TABLE chatting CASCADE CONSTRAINTS;
DROP TABLE gameroom CASCADE CONSTRAINTS;
DROP TABLE visitor CASCADE CONSTRAINTS;
DROP TABLE visitor_count CASCADE CONSTRAINTS;




/* Create Tables */

CREATE TABLE account
(
	account_no number NOT NULL,
	email nvarchar2(100) NOT NULL,
	password nvarchar2(50) NOT NULL,
	name nvarchar2(20) NOT NULL,
	gender nvarchar2(1) NOT NULL,
	age number NOT NULL,
	home_address nvarchar2(100) NOT NULL,
	hobby char(1 char) NOT NULL,
	enroll_date date DEFAULT SYSDATE NOT NULL,
	PRIMARY KEY (account_no)
);


CREATE TABLE authority
(
	account_no number NOT NULL,
	-- 시큐리티 회원 권한 설정
	-- ["ADMIN"]
	-- ["USER"]
	-- ["BLACKLIST"]
	authority nvarchar2(20) NOT NULL
);


CREATE TABLE blacklist
(
	account_no number NOT NULL,
	declaration_date date DEFAULT SYSDATE NOT NULL,
	realease_date date DEFAULT SYSDATE NOT NULL,
	black_reason nvarchar2(500) NOT NULL,
	appeal nvarchar2(500)
);


CREATE TABLE board
(
	bno number NOT NULL,
	account_no number NOT NULL,
	postdate date DEFAULT SYSDATE NOT NULL,
	title nvarchar2(150) NOT NULL,
	hit_count number DEFAULT 0 NOT NULL,
	-- [0:"일반"]
	-- [1:"식단"]
	-- [2:"운동"]
	board_category char(1 char) NOT NULL,
	board_images nvarchar2(500),
	board_comment nvarchar2(2000) NOT NULL,
	address nvarchar2(100),
	edit_date date,
	PRIMARY KEY (bno)
);


CREATE TABLE board_comment
(
	bcno number NOT NULL,
	bno number NOT NULL,
	account_no number NOT NULL,
	postdate date DEFAULT SYSDATE NOT NULL,
	bc_comment nvarchar2(2000) NOT NULL,
	-- [null:"댓글"]
	-- [>=1:"답글"]
	reply_no number,
	edit_date date,
	PRIMARY KEY (bcno)
);


CREATE TABLE board_comment_likes
(
	bcno number NOT NULL,
	account_no number NOT NULL,
	like_date date DEFAULT SYSDATE NOT NULL
);


CREATE TABLE board_comment_reports
(
	bcno number NOT NULL,
	account_no number NOT NULL,
	report_reason nvarchar2(2000) NOT NULL,
	report_date date DEFAULT SYSDATE NOT NULL
);


CREATE TABLE board_likes
(
	bno number NOT NULL,
	account_no number NOT NULL,
	like_date date DEFAULT SYSDATE NOT NULL
);


CREATE TABLE board_reports
(
	bno number NOT NULL,
	account_no number NOT NULL,
	report_comment nvarchar2(2000) NOT NULL,
	report_date date DEFAULT SYSDATE NOT NULL
);


CREATE TABLE board_scraps
(
	bno number NOT NULL,
	account_no number NOT NULL
);


CREATE TABLE calendar
(
	account_no number NOT NULL,
	postdate date DEFAULT SYSDATE NOT NULL,
	-- [f:"식단"]
	-- [e:"운동"]
	-- [m:"메모"]
	calendar_category nvarchar2(1) NOT NULL,
	-- "운동 스쿼트 50분"
	-- "운동 스쿼트 40분"
	-- "식단 총 소비량 539kcal"
	description nvarchar2(50) NOT NULL,
	-- ”스쿼트:50분 칼로리 소비량:523Kcal”
	memo nvarchar2(500) NOT NULL
);


CREATE TABLE chatting
(
	-- <<시퀀스 적용>>
	chatting_no number NOT NULL,
	opening_date date DEFAULT SYSDATE NOT NULL,
	PRIMARY KEY (chatting_no)
);


CREATE TABLE chatting_chatroom
(
	-- <<시퀀스 적용>>
	chatting_no number NOT NULL,
	account_no number NOT NULL,
	postdate date DEFAULT SYSDATE NOT NULL,
	chat_comment nvarchar2(2000) NOT NULL,
	attach_file nvarchar2(100)
);


CREATE TABLE chatting_chatroom_notice
(
	-- 채팅방 공지는 항상 하나만 존재하기 때문에
	-- UNIQUE설정
	chatting_no number NOT NULL UNIQUE,
	postdate date DEFAULT SYSDATE NOT NULL,
	comment_name nvarchar2(20) NOT NULL,
	notice_comment nvarchar2(200) NOT NULL
);


CREATE TABLE chatting_list
(
	-- <<시퀀스 적용>>
	chatting_no number NOT NULL,
	account_no number NOT NULL,
	-- 채팅방 기본 이름 설정은 상대방 이름을 따서
	-- 
	-- "이명진님과의 채팅방"
	-- "조동훈님과의 채팅방"
	-- 
	-- 으로 설정
	chatting_nick nvarchar2(100)
);


CREATE TABLE diet
(
	account_no number NOT NULL,
	postdate date DEFAULT SYSDATE NOT NULL,
	-- "채소"
	-- "과일"
	-- "육류"
	diet_category nvarchar2(50) NOT NULL,
	-- "사과"
	-- "토마토"
	-- "소고기"
	food nvarchar2(100) NOT NULL,
	-- 음식 100g당 칼로리
	calorie number DEFAULT 0 NOT NULL
);


CREATE TABLE exercise
(
	account_no number NOT NULL,
	postdate date DEFAULT SYSDATE NOT NULL,
	-- "스쿼트"
	-- "윗몸"
	-- "팔굽혀펴기"
	category nvarchar2(50) NOT NULL,
	-- 운동 평균 정확도
	accuracy number(3) NOT NULL,
	minutes number DEFAULT 0 NOT NULL,
	counts number DEFAULT 0 NOT NULL
);


CREATE TABLE friendship
(
	account_no number NOT NULL,
	opponent_no number NOT NULL,
	nickname nvarchar2(20),
	-- 상대와의 관계
	-- 
	-- [1:"친구"]
	-- [2:"친한친구"]
	-- [3:"차단"]
	-- [4:"친구요청"]
	realation number(1) NOT NULL
);


CREATE TABLE gameroom
(
	-- <<시퀀스 적용>>
	gameroom_no number NOT NULL,
	-- [R:"(Ranking)랭킹매칭"]
	-- [N:"(Normal)사용자 지정게임"]
	game_mode char(1 char) NOT NULL,
	PRIMARY KEY (gameroom_no)
);


CREATE TABLE gameroom_ranking
(
	-- <<시퀀스 적용>>
	gameroom_no number NOT NULL,
	account_no number NOT NULL,
	game_date date DEFAULT SYSDATE NOT NULL,
	gameroom_rank number NOT NULL,
	gameroom_point number NOT NULL
);


CREATE TABLE inbody
(
	account_no number NOT NULL,
	postdate date DEFAULT SYSDATE NOT NULL,
	height number(5,2) NOT NULL,
	weight number(5,2) NOT NULL,
	body_water number(6,2),
	protein number(6,2),
	minerals number(6,2),
	fat_mass number(6,2)
);


CREATE TABLE notification
(
	account_no number NOT NULL,
	-- 1:"게시글"
	-- 2:"게시글 댓글"
	-- 3:"친구 요청"
	notification_category char(1 char) NOT NULL,
	-- 숫자가 들어가는 경우 => "게시글 번호로 링크"
	-- NULL인 경우 => "친구 요청"
	notification_numbering number,
	opponent_no number NOT NULL,
	-- Y:"확인"
	-- N:"확인 안함"
	confirm char(1 char) NOT NULL,
	notice_date date DEFAULT SYSDATE NOT NULL
);


CREATE TABLE point
(
	account_no number NOT NULL,
	point number DEFAULT 0 NOT NULL
);


CREATE TABLE ranking
(
	account_no number NOT NULL,
	ranking_date date DEFAULT SYSDATE NOT NULL,
	score number NOT NULL
);


CREATE TABLE social
(
	account_no number NOT NULL,
	-- ["Google"]
	-- ["Naver"]
	social_category nvarchar2(50) NOT NULL
);


CREATE TABLE visitor
(
	visitor nvarchar2(50) NOT NULL UNIQUE,
	visit_date date DEFAULT SYSDATE NOT NULL
);


CREATE TABLE visitor_count
(
	-- 하루 기준으로 잡는다.
	-- "2023/01/02"
	-- 
	-- "2023/01/05"
	-- 
	-- "2023/01/07"
	-- 
	visit_date date DEFAULT SYSDATE NOT NULL UNIQUE,
	count number DEFAULT 0 NOT NULL
);



/* Create Foreign Keys */

ALTER TABLE authority
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE blacklist
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE board
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE board_comment
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE board_comment_likes
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE board_comment_reports
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE board_likes
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE board_reports
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE board_scraps
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE calendar
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE chatting_chatroom
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE chatting_list
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE diet
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE exercise
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE friendship
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE gameroom_ranking
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE inbody
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE notification
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE point
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE ranking
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE social
	ADD FOREIGN KEY (account_no)
	REFERENCES account (account_no)
;


ALTER TABLE board_comment
	ADD FOREIGN KEY (bno)
	REFERENCES board (bno)
;


ALTER TABLE board_likes
	ADD FOREIGN KEY (bno)
	REFERENCES board (bno)
;


ALTER TABLE board_reports
	ADD FOREIGN KEY (bno)
	REFERENCES board (bno)
;


ALTER TABLE board_scraps
	ADD FOREIGN KEY (bno)
	REFERENCES board (bno)
;


ALTER TABLE board_comment_likes
	ADD FOREIGN KEY (bcno)
	REFERENCES board_comment (bcno)
;


ALTER TABLE board_comment_reports
	ADD FOREIGN KEY (bcno)
	REFERENCES board_comment (bcno)
;


ALTER TABLE chatting_chatroom
	ADD FOREIGN KEY (chatting_no)
	REFERENCES chatting (chatting_no)
;


ALTER TABLE chatting_chatroom_notice
	ADD FOREIGN KEY (chatting_no)
	REFERENCES chatting (chatting_no)
;


ALTER TABLE chatting_list
	ADD FOREIGN KEY (chatting_no)
	REFERENCES chatting (chatting_no)
;


ALTER TABLE gameroom_ranking
	ADD FOREIGN KEY (gameroom_no)
	REFERENCES gameroom (gameroom_no)
;



