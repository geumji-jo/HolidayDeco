USE hdeco;	

-- 테이블 드랍				
DROP TABLE IF EXISTS KAKAO_APPROVE_RESPONSE_T;
DROP TABLE IF EXISTS ITEM_ORDER_T;
DROP TABLE IF EXISTS CART_DETAIL_T;	
DROP TABLE IF EXISTS ITEM_T;
DROP TABLE IF EXISTS CART_T;
DROP TABLE IF EXISTS SLEEP_USER_T;	
DROP TABLE IF EXISTS OUT_USER_T;	
DROP TABLE IF EXISTS USER_ACCESS_T;	
DROP TABLE IF EXISTS USER_T;	





-- -- -- -- -- -- -- -- -- --<회원> -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

-- 회원 테이블
CREATE TABLE USER_T (	
USER_NO        INT AUTO_INCREMENT,            -- PK회원 번호
ID             VARCHAR(40) NOT NULL UNIQUE,	  -- 회원 아이디
PW             VARCHAR(64),	                  -- 회원 비밀번호
NAME           VARCHAR(40),	                  -- 회원 이름
GENDER         VARCHAR(2),	                  -- 회원 성별
EMAIL          VARCHAR(100) NOT NULL UNIQUE,  -- 회원 이메일
MOBILE         VARCHAR(15),	                  -- 회원 전화번호
BIRTHYEAR      VARCHAR(4),	                  -- 회원 태어난 년도
BIRTHDATE      VARCHAR(4),	                  -- 회원 태어난 월일
POSTCODE       VARCHAR(5),	                  -- 우편번호
ROAD_ADDRESS   VARCHAR(100),	              -- 도로명주소
JIBUN_ADDRESS  VARCHAR(100),	              -- 지번주소
DETAIL_ADDRESS VARCHAR(100),	              -- 상세주소
EXTRA_ADDRESS  VARCHAR(100),	              -- 참고항목
AGREECODE      INT NOT NULL,	              -- 약관동의여부(0:필수, 1:위치, 2:이벤트, 3:위치+이벤트)
JOINED_AT      DATETIME,	                  -- 가입일
PW_MODIFIED_AT DATETIME,	                  -- 비밀번호 수정일
AUTOLOGIN_ID   VARCHAR(32),	                  -- 자동로그인사용아이디
AUTOLOGIN_EXPIRED_AT DATETIME,	              -- 자동로그인만료일
ADMIN_CHECK   INT DEFAULT 0,	              -- 사용자 0, 관리자 1로 구분
CONSTRAINT PK_USER_T PRIMARY KEY(USER_NO)	
	
);	
	
-- 회원 접속 기록(회원마다 마지막 로그인 날짜 1개만 기록)	
CREATE TABLE USER_ACCESS_T (	
ID VARCHAR(40)  NOT NULL UNIQUE, -- 회원 아이디(회원테이블)
LAST_LOGIN_AT   DATETIME         -- 마지막로그인날짜
);	
	
-- 탈퇴 (탈퇴한 아이디로 재가입이 불가능)	
CREATE TABLE OUT_USER_T (	
OUT_USER_NO INT AUTO_INCREMENT,             -- PK 회원번호
USER_NO     INT,							-- 회원 번호	
ID        	VARCHAR(40)  NOT NULL UNIQUE,	-- 회원 아이디
EMAIL    	VARCHAR(100) NOT NULL UNIQUE,	-- 회원 이메일
JOINED_AT 	DATETIME,	                    -- 가입일
OUT_AT    	DATETIME,                       -- 탈퇴일
CONSTRAINT FK_OUT_USER_T PRIMARY KEY(OUT_USER_NO)
);	
	
-- 휴면 (1년 이상 로그인을 안하면 휴면 처리)	
CREATE TABLE SLEEP_USER_T (	
SLEEP_USER_NO  INT NOT NULL AUTO_INCREMENT,   -- PK 휴면 회원번호
USER_NO        INT,	              -- FK 회원번호(회원테이블)
ID             VARCHAR(40) NOT NULL UNIQUE,	  -- 회원 아이디(회원테이블)
PW             VARCHAR(64),	                  -- 회원 비밀번호(회원테이블)
NAME           VARCHAR(40),	                  -- 회원 이름(회원테이블)
GENDER         VARCHAR(2),	                  -- 회원 성별(회원테이블)
EMAIL          VARCHAR(100) NOT NULL UNIQUE,  -- 회원 이메일(회원테이블)
MOBILE         VARCHAR(15),	                  -- 회원 전화번호(회원테이블)
BIRTHYEAR      VARCHAR(4),	                  -- 회원 태어난 년도(회원테이블)
BIRTHDATE      VARCHAR(4),	                  -- 회원 태어난 월일(회원테이블)
POSTCODE       VARCHAR(5),	                  -- 우편번호(회원테이블)
ROAD_ADDRESS   VARCHAR(100),	              -- 도로명주소(회원테이블)
JIBUN_ADDRESS  VARCHAR(100),	              -- 지번주소(회원테이블)
DETAIL_ADDRESS VARCHAR(100),	              -- 상세주소(회원테이블)
EXTRA_ADDRESS  VARCHAR(100),	              -- 참고항목(회원테이블)
AGREECODE      INT NOT NULL,	              -- 약관동의여부(회원테이블)
JOINED_AT      DATETIME,	                  -- 가입일(회원테이블)
PW_MODIFIED_AT DATETIME,	                  -- 비밀번호 수정일(회원테이블)
SLEPT_AT       DATETIME,	                  -- 휴면일
ADMIN_CHECK    INT,	                          -- 사용자, 관리자 구분(회원테이블)
CONSTRAINT PK_SLEEP_USER_T PRIMARY KEY(SLEEP_USER_NO)

);	


-- -- -- -- -- -- -- -- -- --<상품> -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- 상품 테이블
CREATE TABLE ITEM_T(	
	ITEM_NO INT NOT NULL AUTO_INCREMENT,	-- PK 상품 번호
	ITEM_TITLE VARCHAR(40) ,	        -- 상품명 
	ITEM_PRICE VARCHAR(40) ,                -- 상품 가격
	ITEM_MAIN_IMG VARCHAR(100) ,	        -- 상품 메인이미지
	ITEM_DETAIL_IMG VARCHAR(100),           -- 상품 상세이미지
	ITEM_STOCK INT,                         -- 상품 재고
    ITEM_WRITED_AT DATETIME,
	CONSTRAINT PK_ITEM_T PRIMARY KEY(ITEM_NO)
);	



-- -- -- -- -- -- -- -- -- --<장바구니> -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

-- 장바구니 테이블
CREATE TABLE CART_T (
CART_NO INT NOT NULL, -- PK
USER_NO INT, -- FK 유저번호
ID VARCHAR(40) NOT NULL UNIQUE, -- USER_NO통해서 가져온 ID
MADE_AT DATETIME,
CONSTRAINT PK_CART_T PRIMARY KEY(CART_NO),
CONSTRAINT FK_CART_T FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE
);



-- -- -- -- -- -- -- -- -- --<아이템> -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- 아이템 테이블
CREATE TABLE ITEM_T(	
ITEM_NO INT NOT NULL AUTO_INCREMENT,	-- PK 아이템번호
ITEM_TITLE VARCHAR(40) ,	            -- 아이템이름
ITEM_PRICE VARCHAR(40) ,                -- 아이템가격
ITEM_MAIN_IMG VARCHAR(100) ,	        -- 아이템메인이미지
ITEM_DETAIL_IMG VARCHAR(100),           -- 아이템상세이미지
ITEM_STOCK INT,                         -- 아이템수량
ITEM_WRITED_AT DATETIME,                -- 아이템등록날짜
CONSTRAINT PK_ITEM_T PRIMARY KEY(ITEM_NO)
);	



-- -- -- -- -- -- -- -- -- --<장바구니디테일> -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- 장바구니 디테일 테이블
CREATE TABLE CART_DETAIL_T (
CART_DETAIL_NO INT NOT NULL,
CART_DETAIL_COUNT INT,
CART_DETAIL_CHECK INT,
ITEM_TITLE VARCHAR(40) ,	
ITEM_PRICE VARCHAR(40) ,
CART_NO INT, -- FK 카트번호
ITEM_NO INT, -- FK 아이템번호
USER_NO INT, -- FK 유저번호
CONSTRAINT PK_CART_DETAIL_T PRIMARY KEY(CART_DETAIL_NO),
CONSTRAINT FK_CART_DETAIL_T_USER_T FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE,
CONSTRAINT FK_CART_DETAIL_T_ITEM_T FOREIGN KEY(ITEM_NO) REFERENCES ITEM_T(ITEM_NO) ON DELETE CASCADE,
CONSTRAINT FK_CART_DETAIL_T_CART_T FOREIGN KEY(CART_NO) REFERENCES CART_T(CART_NO) ON DELETE CASCADE

);


-- -- -- -- -- -- -- -- -- --<아이템주문내역> -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 


-- 아이템주문내역
CREATE TABLE ITEM_ORDER_T (
ORDER_NO INT NOT NULL,                 -- PK 주문번호
ORDER_TOTAL INT,                       -- 전체주문금액
ID VARCHAR(40)  NOT NULL UNIQUE,       -- 회원 아이디(회원테이블)
MOBILE         VARCHAR(15),	           -- 회원 전화번호
POSTCODE       VARCHAR(5),	           -- 우편번호
JIBUN_ADDRESS  VARCHAR(100),	       -- 지번주소
DETAIL_ADDRESS VARCHAR(100),	       -- 상세주소
NAME           VARCHAR(40),	           -- 회원 이름
ITEM_MAIN_IMG VARCHAR(100) ,	       -- 아이템메인이미지
CART_DETAIL_COUNT INT , 
CART_NO INT, -- FK 카트번호
ITEM_NO INT, -- FK 아이템번호
USER_NO INT, -- FK 유저번호
CONSTRAINT PK_ITEM_ORDER_T PRIMARY KEY(ORDER_NO),
CONSTRAINT FK_ITEM_ORDER_T_USER_T FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE,
CONSTRAINT FK_ITEM_ORDER_T_ITEM_T FOREIGN KEY(ITEM_NO) REFERENCES ITEM_T(ITEM_NO) ON DELETE CASCADE,
CONSTRAINT FK_ITEM_ORDER_T_CART_T FOREIGN KEY(CART_NO) REFERENCES CART_T(CART_NO) ON DELETE CASCADE

);


-- -- -- -- -- -- -- -- -- --<카카오페이> -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

-- 카카오페이테이블
CREATE TABLE KAKAO_APPROVE_RESPONSE_T (
AID VARCHAR(100)   NOT NULL UNIQUE,
TID LONGTEXT,
CID LONGTEXT,
SID LONGTEXT,
PARTNER_ORDER_ID LONGTEXT,
PARTNER_USER_ID LONGTEXT,
PAYMENT_METHOD_TYPE LONGTEXT,
ITEM_NAME LONGTEXT,
ITEM_CODE LONGTEXT,
QUANTITY INT,
CREATED_AT DATETIME,
APPROVED_AT DATETIME,
CONSTRAINT PK_AID PRIMARY KEY(AID)
);


CREATE TABLE AMOUNT_T (
   TOTAL       INT      NOT NULL UNIQUE,
    TAX_FREE   INT      NULL,
    VAT        INT      NULL,
    POINT      INT      NULL,
    DISCOUNT   INT      NULL,
    CONSTRAINT PK_TOTAL PRIMARY KEY (TOTAL)
);


-- -- -- -- -- -- -- -- -- --<user Insert> -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
INSERT INTO USER_T (ID, PW, NAME, GENDER, EMAIL, MOBILE, BIRTHYEAR, BIRTHDATE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, EXTRA_ADDRESS, AGREECODE, JOINED_AT, ADMIN_CHECK)
VALUES ('admin', 'admin1!', '관리자', 'F', 'admin@naver.com', '01000000000','1998', '0105', 34659, '대전 동구 광명길 2', '대전 동구 대동 352-1', '1-105', '(대동)', 0, '2023-05-01 13:01:01', 1);

INSERT INTO USER_T (ID, PW, NAME, GENDER, EMAIL, MOBILE, BIRTHYEAR, BIRTHDATE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, EXTRA_ADDRESS, AGREECODE, JOINED_AT, ADMIN_CHECK)
VALUES ('user01', 'user01!', '유저원', 'F', 'user01@naver.com', '01000000000','1998', '0105', 34659, '대전 동구 광명길 2', '대전 동구 대동 352-1', '1-105', '(대동)', 0, '2023-11-01 13:01:01', 0);

INSERT INTO USER_T (ID, PW, NAME, GENDER, EMAIL, MOBILE, BIRTHYEAR, BIRTHDATE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, EXTRA_ADDRESS, AGREECODE, JOINED_AT, ADMIN_CHECK)
VALUES ('user02', 'user02@', '유저투', 'M', 'user02@naver.com', '01000000000','1998', '0105', 34659, '대전 동구 광명길 2', '대전 동구 대동 352-1', '1-105', '(대동)', 0, '2023-11-02 13:01:02', 0);

INSERT INTO USER_T (ID, PW, NAME, GENDER, EMAIL, MOBILE, BIRTHYEAR, BIRTHDATE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, EXTRA_ADDRESS, AGREECODE, JOINED_AT, ADMIN_CHECK)
VALUES ('user03', 'user03#', '유저쓰리', 'M', 'user03@naver.com', '01000000000','1998', '0105', 34659, '대전 동구 광명길 2', '대전 동구 대동 352-1', '1-105', '(대동)', 0, '2023-11-03 13:01:03', 0);
<<<<<<< HEAD

INSERT INTO USER_T (ID, PW, NAME, GENDER, EMAIL, MOBILE, BIRTHYEAR, BIRTHDATE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, EXTRA_ADDRESS, AGREECODE, JOINED_AT, ADMIN_CHECK)
VALUES ('user04', 'user04$', '유저포', 'M', 'user04@naver.com', '01000000000','1998', '0105', 34659, '대전 동구 광명길 2', '대전 동구 대동 352-1', '1-105', '(대동)', 0, '2023-11-04 13:01:04', 0);

INSERT INTO USER_T (ID, PW, NAME, GENDER, EMAIL, MOBILE, BIRTHYEAR, BIRTHDATE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, EXTRA_ADDRESS, AGREECODE, JOINED_AT, ADMIN_CHECK)
VALUES ('user05', 'user05%', '유저파이브', 'M', 'user05@naver.com', '01000000000','1998', '0105', 34659, '대전 동구 광명길 2', '대전 동구 대동 352-1', '1-105', '(대동)', 0, '2023-11-05 13:01:05', 0);
=======
>>>>>>> 441b5bf4c22d129b1d28c498320b78e7a9fd986c

INSERT INTO USER_T (ID, PW, NAME, GENDER, EMAIL, MOBILE, BIRTHYEAR, BIRTHDATE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, EXTRA_ADDRESS, AGREECODE, JOINED_AT, ADMIN_CHECK)
VALUES ('user04', 'user04$', '유저포', 'M', 'user04@naver.com', '01000000000','1998', '0105', 34659, '대전 동구 광명길 2', '대전 동구 대동 352-1', '1-105', '(대동)', 0, '2023-11-04 13:01:04', 0);

INSERT INTO USER_T (ID, PW, NAME, GENDER, EMAIL, MOBILE, BIRTHYEAR, BIRTHDATE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, EXTRA_ADDRESS, AGREECODE, JOINED_AT, ADMIN_CHECK)
VALUES ('user05', 'user05%', '유저파이브', 'M', 'user05@naver.com', '01000000000','1998', '0105', 34659, '대전 동구 광명길 2', '대전 동구 대동 352-1', '1-105', '(대동)', 0, '2023-11-05 13:01:05', 0);
-- -- -- -- -- -- -- -- -- --<item Insert> -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('tree paper bag','5700','/storage/itemImg/상품(1).jpg','/storage/itemImg/상품(1).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('cutie bear doll','12000','/storage/itemImg/상품(2).jpg','/storage/itemImg/상품(2).png',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('ornament_gold','5200','/storage/itemImg/상품(3).jpg','/storage/itemImg/상품(2).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('christmas candle','8900','/storage/itemImg/상품(4).jpg','/storage/itemImg/상품(4).png',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('ornament_snowman','6000','/storage/itemImg/상품(5).jpg','/storage/itemImg/상품(5).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('snowball','9200','/storage/itemImg/상품(6).jpg','/storage/itemImg/상품(6).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('christmas cookie','5000','/storage/itemImg/상품(7).jpg','/storage/itemImg/상품(7).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('ornament_snowman2','5300','/storage/itemImg/상품(8).jpg','/storage/itemImg/상품(8).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('cane candy','1200','/storage/itemImg/상품(9).jpg','/storage/itemImg/상품(9).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('snowball_house','15000','/storage/itemImg/상품(10).jpg','/storage/itemImg/상품(10).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('tree cookie tool','4500','/storage/itemImg/상품(11).jpg','/storage/itemImg/상품(11).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('ornament_twinkle','4600','/storage/itemImg/상품(12).jpg','/storage/itemImg/상품(12).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('ornament_bell','5000','/storage/itemImg/상품(13).jpg','/storage/itemImg/상품(13).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('ornament_horse','15700','/storage/itemImg/상품(14).jpg','/storage/itemImg/상품(14).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('ornament_red','5900','/storage/itemImg/상품(15).jpg','/storage/itemImg/상품(15).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('red candle set','22000','/storage/itemImg/상품(16).jpg','/storage/itemImg/상품(16).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('christmas card','3000','/storage/itemImg/상품(17).jpg','/storage/itemImg/상품(17).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('dishes','18200','/storage/itemImg/상품(18).jpg','/storage/itemImg/상품(18).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('tree wrapper','4000','/storage/itemImg/상품(19).jpg','/storage/itemImg/상품(19).jpg',100,NOW());
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('wrapper set','8000','/storage/itemImg/상품(20).jpg','/storage/itemImg/상품(20).jpg',100,NOW());

