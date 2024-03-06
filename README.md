

![image](https://github.com/bbooom2/HolidayDeco/assets/118744207/de460bf3-74c2-4512-949e-2afe448b35e9)

# 🎄HolidayDeco🎄
- 크리스마스 기념용품을 판매하는 사이트입니다. 


# 📅프로젝트 기간 
- 2023-11-21 ~ 2024-02-08
  

# 📂개발 환경 및 라이브러리 
- 구현언어 : JAVA, Spring Boot, HTML, JavaScript, jQuery, SQL
- WAS : Tomcat 9.0
- DB 서버 : MySQL
- 형상관리 : Github, Cafe24 Hosting
  

# 🎀담당 기능 
- 인덱스
- 관리자 : 페이지네이션, 검색, CRUD, 주문 내역 관리
- 판매 : 페이지네이션, 검색, 상품 상세페이지, 장바구니, 찜하기, 구매
- 회원 : 구매내역
  

# 🗨️프로젝트 담당 기능 설명  
📍 **(관리자/판매) 상품 CRUD, 페이지네이션 및 검색 기능**
- 상품 등록,수정,삭제와 목록을 조회할 수 있습니다.
- 페이지네이션을 통하여 페이지 이동이 가능합니다.
- 상품명으로 검색이 가능합니다.


📍 **(관리자) 주문 내역 관리** 
- 결제가 끝난 주문 내역을 관리할 수 있습니다.
- 아임포트API를 통해 결제된 주문 내역은 관리자가 '배송 준비 중' 일 때에 한하여 아임포트 주문 취소 API를 활용하여 취소 처리를 할 수 있습니다. 
- 배송 준비 중인 주문 내역은 관리자가 배송 중, 배송 완료를 통해 주문 상태를 변경할 수 있습니다.
- 배송 완료가 되면 해당 수량 만큼 재고가 감소합니다.


📍 **(판매)상품 상세페이지**     
- 수량을 조절하여 총 수량 및 합계 금액을 확인할 수 있습니다.
- 회원이 상품 상세페이지에 접속했을 때 재고를 확인할 수 있게 재고 현황을 나타냈습니다.


📍 **(판매)장바구니** 
- 장바구니에 담긴 각 상품의 수량 조절 및 선택삭제, 전체삭제가 가능합니다.
- 무료배송 기준을 지정하여 미달된 금액일 시 배송비를 부과합니다.
- 장바구니의 상품을 선택구매, 전체구매할 수 있습니다.


📍 **(판매)찜하기**
- 회원은 마음에 드는 상품을 찜하기를 통해 목록화 가능하며 이는 myPageHome의 찜한 상품과 헤더에서 확인할 수 있습니다. 
- 찜하기는 상품리스트, 상품 상세페이지에서 가능합니다.
- 찜하기 삭제는 상품리스트, 상품 상세페이지, myPageHome의 찜한 상품과 헤더에 접근하여 확인할 수 있습니다. 


📍 **(판매)구매**
- 다음주소API를 활용하여 배송 주소를 설정 할 수 있습니다.
- 아임포트API를 활용하여 일반 결제를 진행할 수 있습니다.


📍 **(회원)구매내역**
- 회원은 myPageHome의 구매 내역 조회를 통해 구매 내역 목록 - 구매 상세 내역의 순서로 확인할 수 있습니다.

  
# 🔗웹페이지 
http://holidayDeco.cafe24.com (ID/PW 포트폴리오 참조) 
