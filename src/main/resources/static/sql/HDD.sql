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
USER_NO        INT AUTO_INCREMENT,             -- PK회원 번호
ID             VARCHAR(40) NOT NULL UNIQUE,     -- 회원 아이디
PW             VARCHAR(64),                     -- 회원 비밀번호
NAME           VARCHAR(40),                     -- 회원 이름
GENDER         VARCHAR(2),                     -- 회원 성별
EMAIL          VARCHAR(100) NOT NULL UNIQUE,  -- 회원 이메일
MOBILE         VARCHAR(15),                     -- 회원 전화번호
BIRTHYEAR      VARCHAR(4),                     -- 회원 태어난 년도
BIRTHDATE      VARCHAR(4),                     -- 회원 태어난 월일
POSTCODE       VARCHAR(5),                     -- 우편번호
ROAD_ADDRESS   VARCHAR(100),                 -- 도로명주소
JIBUN_ADDRESS  VARCHAR(100),                 -- 지번주소
DETAIL_ADDRESS VARCHAR(100),                 -- 상세주소
EXTRA_ADDRESS  VARCHAR(100),                 -- 참고항목
AGREECODE      INT NOT NULL,                 -- 약관동의여부
JOINED_AT      DATETIME,                     -- 가입일
PW_MODIFIED_AT DATETIME,                     -- 비밀번호 수정일
AUTOLOGIN_ID   VARCHAR(32),                     -- 자동로그인사용아이디
AUTOLOGIN_EXPIRED_AT DATETIME,                 -- 자동로그인만료일
ADMIN_CHECK   INT,                             -- 사용자, 관리자 구분
CONSTRAINT PK_USER_T PRIMARY KEY(USER_NO)   
   
);   
   
-- 회원 접속 기록(회원마다 마지막 로그인 날짜 1개만 기록)   
CREATE TABLE USER_ACCESS_T (   
USER_NO        INT NOT NULL,    -- FK회원 번호(회원테이블)
ID VARCHAR(40)  NOT NULL UNIQUE, -- 회원 아이디(회원테이블)
JOINED_AT DATETIME,              -- 가입일(회원테이블)
LAST_LOGIN_AT DATETIME,          -- 마지막로그인날짜
CONSTRAINT FK_USER_ACCESS_T FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE
);   
   
-- 탈퇴 (탈퇴한 아이디로 재가입이 불가능)   
CREATE TABLE OUT_USER_T (   
OUT_USER_NO  INT NOT NULL AUTO_INCREMENT,   -- PK 탈퇴 회원번호
USER_NO   INT NOT NULL,                       -- FK 회원번호(회원테이블)
ID        VARCHAR(40)  NOT NULL UNIQUE,       -- 회원 아이디(회원테이블)
EMAIL     VARCHAR(100) NOT NULL UNIQUE,       -- 회원 이메일(회원테이블)
JOINED_AT DATETIME,                           -- 가입일(회원테이블)
OUT_AT DATETIME,                            -- 탈퇴일
CONSTRAINT PK_OUT_USER_T PRIMARY KEY(OUT_USER_NO),
CONSTRAINT FK_OUT_USER_T FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE

);   
   
-- 휴면 (1년 이상 로그인을 안하면 휴면 처리)   
CREATE TABLE SLEEP_USER_T (   
SLEEP_USER_NO  INT NOT NULL AUTO_INCREMENT,   -- PK 휴면 회원번호
USER_NO        INT NOT NULL,                 -- FK 회원번호(회원테이블)
ID             VARCHAR(40) NOT NULL UNIQUE,     -- 회원 아이디(회원테이블)
PW             VARCHAR(64),                     -- 회원 비밀번호(회원테이블)
NAME           VARCHAR(40),                     -- 회원 이름(회원테이블)
GENDER         VARCHAR(2),                     -- 회원 성별(회원테이블)
EMAIL          VARCHAR(100) NOT NULL UNIQUE,  -- 회원 이메일(회원테이블)
MOBILE         VARCHAR(15),                     -- 회원 전화번호(회원테이블)
BIRTHYEAR      VARCHAR(4),                     -- 회원 태어난 년도(회원테이블)
BIRTHDATE      VARCHAR(4),                     -- 회원 태어난 월일(회원테이블)
POSTCODE       VARCHAR(5),                     -- 우편번호(회원테이블)
ROAD_ADDRESS   VARCHAR(100),                 -- 도로명주소(회원테이블)
JIBUN_ADDRESS  VARCHAR(100),                 -- 지번주소(회원테이블)
DETAIL_ADDRESS VARCHAR(100),                 -- 상세주소(회원테이블)
EXTRA_ADDRESS  VARCHAR(100),                 -- 참고항목(회원테이블)
AGREECODE      INT NOT NULL,                 -- 약관동의여부(회원테이블)
JOINED_AT      DATETIME,                     -- 가입일(회원테이블)
PW_MODIFIED_AT DATETIME,                     -- 비밀번호 수정일(회원테이블)
SLEPT_AT       DATETIME,                     -- 휴면일
ADMIN_CHECK   INT,                             -- 사용자, 관리자 구분(회원테이블)
CONSTRAINT PK_SLEEP_USER_T PRIMARY KEY(SLEEP_USER_NO),
CONSTRAINT FK_SLEEP_USER_T FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE   
);   



-- -- -- -- -- -- -- -- -- --<장바구니> -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

-- 장바구니 테이블
CREATE TABLE CART_T (
CART_NO INT NOT NULL, -- PK
USER_NO INT NOT NULL, -- FK 유저번호
ID VARCHAR(40) NOT NULL UNIQUE, -- USER_NO통해서 가져온 ID
MADE_AT DATETIME,
CONSTRAINT PK_CART_T PRIMARY KEY(CART_NO),
CONSTRAINT FK_CART_T FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE
);



-- -- -- -- -- -- -- -- -- --<아이템> -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- 아이템 테이블
CREATE TABLE ITEM_T(   
ITEM_NO INT NOT NULL AUTO_INCREMENT,   -- PK 아이템번호
ITEM_TITLE VARCHAR(40) ,               -- 아이템이름
ITEM_PRICE VARCHAR(40) ,                -- 아이템가격
ITEM_MAIN_IMG VARCHAR(100) ,           -- 아이템메인이미지
ITEM_DETAIL_IMG VARCHAR(100),           -- 아이템상세이미지
ITEM_STOCK INT,                         -- 아이템수량
ITEM_WIRTED_AT DATETIME,                -- 아이템등록날짜
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
CART_NO INT NOT NULL, -- FK 카트번호
ITEM_NO INT NOT NULL, -- FK 아이템번호
USER_NO INT NOT NULL, -- FK 유저번호
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
MOBILE         VARCHAR(15),              -- 회원 전화번호
POSTCODE       VARCHAR(5),              -- 우편번호
JIBUN_ADDRESS  VARCHAR(100),          -- 지번주소
DETAIL_ADDRESS VARCHAR(100),          -- 상세주소
NAME           VARCHAR(40),              -- 회원 이름
ITEM_MAIN_IMG VARCHAR(100) ,          -- 아이템메인이미지
CART_DETAIL_COUNT INT , 
CART_NO INT NOT NULL, -- FK 카트번호
ITEM_NO INT NOT NULL, -- FK 아이템번호
USER_NO INT NOT NULL, -- FK 유저번호
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

