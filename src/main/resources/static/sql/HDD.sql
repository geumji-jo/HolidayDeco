USE hdeco;	

-- 테이블 드랍
DROP TABLE IF EXISTS AMOUNT_T;
DROP TABLE IF EXISTS KAKAO_APPROVE_RESPONSE_T;
DROP TABLE IF EXISTS ITEM_ORDER_LIST_T;
DROP TABLE IF EXISTS CART_DETAIL_T;
DROP TABLE IF EXISTS ITEM_ORDER_T;
DROP TABLE IF EXISTS CART_T;
DROP TABLE IF EXISTS ITEM_T;
DROP TABLE IF EXISTS SLEEP_USER_T;
DROP TABLE IF EXISTS OUT_USER_T;
DROP TABLE IF EXISTS USER_ACCESS_T;
DROP TABLE IF EXISTS USER_T;


-- -- -- -- -- -- -- -- -- --<회원> -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

-- 회원 테이블
CREATE TABLE USER_T (	
	USER_NO        INT         NOT NULL AUTO_INCREMENT,	  -- PK회원 번호
	ID             VARCHAR(40) NOT NULL UNIQUE,	          -- 회원 아이디
	PW             VARCHAR(64),	                          -- 회원 비밀번호
	NAME           VARCHAR(40),	                          -- 회원 이름
	GENDER         VARCHAR(2),	                          -- 회원 성별
	EMAIL          VARCHAR(100) NOT NULL UNIQUE,          -- 회원 이메일
	MOBILE         VARCHAR(15),	                          -- 회원 전화번호
	BIRTHYEAR      VARCHAR(4),	                          -- 회원 태어난 년도
	BIRTHDATE      VARCHAR(4),	                          -- 회원 태어난 월일
	POSTCODE       VARCHAR(5),	                          -- 우편번호
	ROAD_ADDRESS   VARCHAR(100),	                      -- 도로명주소
	JIBUN_ADDRESS  VARCHAR(100),	                      -- 지번주소
	DETAIL_ADDRESS VARCHAR(100),	                      -- 상세주소
	EXTRA_ADDRESS  VARCHAR(100),	                      -- 참고항목
	AGREECODE      INT NOT NULL,	                      -- 약관동의여부
	JOINED_AT      DATETIME,	                          -- 가입일
	PW_MODIFIED_AT DATETIME,	                          -- 비밀번호 수정일
	AUTOLOGIN_ID   VARCHAR(32),	                          -- 자동로그인사용아이디
	AUTOLOGIN_EXPIRED_AT DATETIME,	                          -- 자동로그인만료일
	ADMIN_CHECK   INT DEFAULT 0,	                          -- 사용자 0, 관리자 1로 구분
	CONSTRAINT PK_USER_T PRIMARY KEY(USER_NO)	
);	
	
-- 회원 접속 기록(회원마다 마지막 로그인 날짜 1개만 기록)	
CREATE TABLE USER_ACCESS_T (	
	ID            VARCHAR(40)  NOT NULL UNIQUE,              -- 회원 아이디(회원테이블)
	LAST_LOGIN_AT DATETIME,                                  -- 마지막 로그인 날짜
	CONSTRAINT FK_USER_ACCESS FOREIGN KEY(ID) REFERENCES USER_T(ID) ON DELETE CASCADE
);	
	
-- 탈퇴 (탈퇴한 아이디로 재가입이 불가능)	
CREATE TABLE OUT_USER_T (	
	OUT_USER_NO        INT          NOT NULL AUTO_INCREMENT,   -- PK 탈퇴 회원번호
	USER_NO            INT,	                                   -- FK 회원번호(회원테이블)
	ID                 VARCHAR(40)  NOT NULL UNIQUE,	       -- 회원 아이디(회원테이블)
	EMAIL              VARCHAR(100) NOT NULL UNIQUE,	       -- 회원 이메일(회원테이블)
	JOINED_AT          DATETIME,	                           -- 가입일(회원테이블)
	OUT_AT             DATETIME,                               -- 탈퇴일
	CONSTRAINT PK_OUT_USER_T PRIMARY KEY(OUT_USER_NO),
	CONSTRAINT FK_OUT_USER_T FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE
);	
	
-- 휴면 (1년 이상 로그인을 안하면 휴면 처리)	
CREATE TABLE SLEEP_USER_T (	
	SLEEP_USER_NO  INT         NOT NULL AUTO_INCREMENT,       -- PK 휴면 회원번호
	USER_NO        INT,	                                      -- FK 회원번호(회원테이블)
	ID             VARCHAR(40) NOT NULL UNIQUE,	              -- 회원 아이디(회원테이블)
	PW             VARCHAR(64),	                 			  -- 회원 비밀번호(회원테이블)
	NAME           VARCHAR(40),	                  			  -- 회원 이름(회원테이블)
	GENDER         VARCHAR(2),	                  			  -- 회원 성별(회원테이블)
	EMAIL          VARCHAR(100) NOT NULL UNIQUE,              -- 회원 이메일(회원테이블)
	MOBILE         VARCHAR(15),	                              -- 회원 전화번호(회원테이블)
	BIRTHYEAR      VARCHAR(4),	                              -- 회원 태어난 년도(회원테이블)
	BIRTHDATE      VARCHAR(4),	                              -- 회원 태어난 월일(회원테이블)
	POSTCODE       VARCHAR(5),	                              -- 우편번호(회원테이블)
	ROAD_ADDRESS   VARCHAR(100),	                          -- 도로명주소(회원테이블)
	JIBUN_ADDRESS  VARCHAR(100),	                          -- 지번주소(회원테이블)
	DETAIL_ADDRESS VARCHAR(100),	                          -- 상세주소(회원테이블)
	EXTRA_ADDRESS  VARCHAR(100),	                          -- 참고항목(회원테이블)
	AGREECODE      INT NOT NULL,	                          -- 약관동의여부(회원테이블)
	JOINED_AT      DATETIME,	                              -- 가입일(회원테이블)
	PW_MODIFIED_AT DATETIME,	                              -- 비밀번호 수정일(회원테이블)
	SLEPT_AT       DATETIME,	                              -- 휴면일
	ADMIN_CHECK    INT,	                                      -- 사용자, 관리자 구분(회원테이블)
	CONSTRAINT PK_SLEEP_USER_T PRIMARY KEY(SLEEP_USER_NO),
	CONSTRAINT FK_SLEEP_USER_T FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE	
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
    CART_NO INT NOT NULL AUTO_INCREMENT,           -- PK
    USER_NO INT NOT NULL,                          -- FK 유저번호
    ITEM_NO INT NOT NULL,                          -- FK 아이템번호
    QUANTITY INT NOT NULL,                         -- 주문수량
    ITEM_TITLE VARCHAR(40),                        -- 상품명
    ITEM_PRICE VARCHAR(40),                        -- 상품 가격
    ITEM_MAIN_IMG VARCHAR(100),                    -- 상품 이미지
    CONSTRAINT PK_CART_T PRIMARY KEY(CART_NO),
    CONSTRAINT FK_CART_T_USER FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE,
    CONSTRAINT FK_CART_T_ITEM FOREIGN KEY(ITEM_NO) REFERENCES ITEM_T(ITEM_NO) ON DELETE CASCADE
);

-- 주문 테이블
CREATE TABLE ITEM_ORDER_T (
	ITEM_ORDER_NO               INT    NOT NULL AUTO_INCREMENT,        -- PK 주문번호
	USER_NO                INT,                                   -- FK 유저번호
	NAME                   VARCHAR(40),	                      -- 회원 이름
	MOBILE                  VARCHAR(15),	                      -- 회원 전화번호
	POSTCODE                VARCHAR(5),	                      -- 우편번호
	ROAD_ADDRESS            VARCHAR(100),			      -- 도로명 주소 
	JIBUN_ADDRESS           VARCHAR(100),	                      -- 지번 주소
	DETAIL_ADDRESS          VARCHAR(100),	                      -- 상세 주소
	ORDER_TOTAL             INT,                                  -- 전체 주문 금액
	ITEM_MAIN_IMG           VARCHAR(100),	                      -- 아이템 메인 이미지
	PAY_NO                  VARCHAR(50),                    -- 결제 번호(merchan uid)    
	CART_DETAIL_COUNT INT , 
	CONSTRAINT PK_ITEM_ORDER_T PRIMARY KEY(ITEM_ORDER_NO),
	CONSTRAINT FK_ITEM_ORDER_T_USER_T FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE
);


-- 주문 상세 테이블 (장바구니 목록)
CREATE TABLE CART_DETAIL_T (
    CART_DETAIL_NO INT NOT NULL AUTO_INCREMENT,     -- 주문 상세 번호
    ITEM_ORDER_NO INT,                    -- 주문 번호
    QUANTITY INT,                    -- 주문 수량
    ORDER_TOTAL INT,                 -- 주문 금액
    ITEM_NO INT,                     -- FK 아이템번호
    CONSTRAINT PK_CART_DETAIL_T PRIMARY KEY(CART_DETAIL_NO),
    CONSTRAINT FK_CART_DETAIL_T_ITEM_T FOREIGN KEY(ITEM_NO) REFERENCES ITEM_T(ITEM_NO) ON DELETE CASCADE,
    CONSTRAINT FK_CART_DETAIL_T_ORDER_T FOREIGN KEY(ITEM_ORDER_NO) REFERENCES ITEM_ORDER_T(ITEM_ORDER_NO) ON DELETE CASCADE
);

-- 주문 내역 테이블
CREATE TABLE ITEM_ORDER_LIST_T (
	ORDER_LIST_NO          INT    NOT NULL AUTO_INCREMENT,        -- PK 주문번호
	USER_NO                INT,                                   -- FK 유저번호
	NAME                   VARCHAR(40),	                          -- 회원 이름
	MOBILE                  VARCHAR(15),	                      -- 회원 전화번호
	POSTCODE                VARCHAR(5),	                          -- 우편번호
	ROAD_ADDRESS            VARCHAR(100),			              -- 도로명 주소 
	JIBUN_ADDRESS           VARCHAR(100),	                      -- 지번 주소
	DETAIL_ADDRESS          VARCHAR(100),	                      -- 상세 주소
	ORDER_TOTAL             INT,                                  -- 전체 주문 금액   
	CART_DETAIL_COUNT INT , 
	CONSTRAINT PK_ITEM_ORDER_T PRIMARY KEY(ORDER_LIST_NO),
	CONSTRAINT FK_ITEM_ORDER_T_USER FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE
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



-- -- -- -- -- -- -- -- -- --<user Insert> -- -- -- -- -- -- -- -- -- -- -- 
INSERT INTO USER_T (ID, PW, NAME, GENDER, EMAIL, MOBILE, BIRTHYEAR, BIRTHDATE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, EXTRA_ADDRESS, AGREECODE, JOINED_AT, ADMIN_CHECK)
VALUES ('admin', 'admin1!', '관리자', 'F', 'admin@naver.com', '01000000000','1998', '0105', 34659, '대전 동구 광명길 2', '대전 동구 대동 352-1', '1-105', '(대동)', 0, '2023-05-01 13:01:01', 1);


INSERT INTO USER_T (ID, PW, NAME, GENDER, EMAIL, MOBILE, BIRTHYEAR, BIRTHDATE, POSTCODE, ROAD_ADDRESS, JIBUN_ADDRESS, DETAIL_ADDRESS, EXTRA_ADDRESS, AGREECODE, JOINED_AT, ADMIN_CHECK)
VALUES ('user1', '1BB187EEBA48 8E71E90655D20EB58319D3B351F4F29CD3A959D45 EC9193F 4', '사용자', 'F', 'user1@naver.com', '01000000000','1998', '0105', 34659, '대전 동구 광명길 2', '대전 동구 대동 352-1', '1-105', '(대동)', 0, '2023-05-01 13:01:01', 0);


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
INSERT INTO ITEM_T (ITEM_TITLE, ITEM_PRICE, ITEM_MAIN_IMG, ITEM_DETAIL_IMG, ITEM_STOCK, ITEM_WRITED_AT)
VALUES ('wrapper set','40000','/storage/itemImg/상품(21).jpg','/storage/itemImg/상품(21).jpg',100,NOW());

