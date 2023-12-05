// 전역 변수 (각종 검사 통과 유무를 저장하는 변수)
var verifyId = false;
var verifyPw = false;
var verifyRePw = false;
var verifyName = false;
var verifyMobile = false;
var verifyEmail = false;

  
// 함수 정의
  
// 1. 아이디 검사(정규식 + 중복)
function fnCheckId(){
  
  $('#id').on('keyup', function(){
    
    // 입력한 아이디
    let id = $(this).val();
    
    // 정규식 (5~40자, 소문자+숫자+하이픈(-)+밑줄(_) 사용 가능, 첫 글자는 소문자+숫자 사용 가능)
    let regId = /^[a-z0-9][a-z0-9-_]{5,39}$/;
    
    // input에 입력한 내용이 없어지면 메시지를 비움
    if (id.length === 0) {
      $('#msgId').text('');
      return;
    }
    
    // 정규식 검사
    verifyId = regId.test(id);
    if(verifyId == false){
      $('#msgId').text('6~40자, 소문자+숫자+하이픈(-)+밑줄(_) 사용 가능, 첫 글자는 소문자+숫자 사용 가능').css('color', 'FireBrick');
      return;  // 여기서 함수 실행을 종료한다. (이후에 나오는 ajax(중복 체크) 실행을 막기 위해서)
    }
      
    // 아이디 중복 체크 ajax
    $.ajax({
      type: 'get',
      url: '/user/verifyId.do',
      data: 'id=' + id,
      dataType: 'json',
      success: function(resData){  // resData = {"enableId": true} 또는 {"enableId": false}
        verifyId = resData.enableId;
        if(verifyId){
          $('#msgId').text('사용 가능한 아이디입니다.').css('color', 'CornflowerBlue');
        } else {
          $('#msgId').text('이미 사용 중인 아이디입니다.').css('color', 'MediumSeaGreen');
        }
      }
    })
    
  })
  
}
  
// 2. 비밀번호 검사 (정규식)
function fnCheckPw(){
  
  $('#pw').on('keyup', function(){
  
    // 입력한 비밀번호
    let pw = $(this).val();
    
    // input에 입력한 내용이 없어지면 메시지를 비움
    if (pw.length === 0) {
      $('#msgPw').text('');
      return;
    }
    
    // 길이(4~20자) 및 정규식(소문자+대문자+숫자+특수문자 사용 가능, 3개 이상 조합)
    let pwLength = pw.length;
    let validCount = /[a-z]/.test(pw)         //   소문자를 가지고 있으면 true(1), 없으면 false(0)
                   + /[A-Z]/.test(pw)         //   대문자를 가지고 있으면 true(1), 없으면 false(0)
                   + /[0-9]/.test(pw)         //     숫자를 가지고 있으면 true(1), 없으면 false(0)
                   + /[^a-zA-Z0-9]/.test(pw); // 특수문자를 가지고 있으면 true(1), 없으면 false(0)
    verifyPw = (pwLength >= 4) && (pwLength <= 20) && (validCount >= 3);
    if(verifyPw){
      $('#msgPw').text('사용 가능한 비밀번호입니다.').css('color', 'CornflowerBlue');
    } else {
      $('#msgPw').text('4~20자, 소문자+대문자+숫자+특수문자 사용 가능, 3개 이상 조합').css('color', 'FireBrick');
    }
  
  })
  
}
  
// 3. 비밀번호 확인
function fnCheckPwAgain(){
  
  $('#rePw').on('keyup', function(){
    
    // 입력된 비밀번호
    let pw = $('#pw').val();
    
    // 재입력한 비밀번호
    let rePw = $(this).val();
    
    // input에 입력한 내용이 없어지면 메시지를 비움
    if (rePw.length === 0) {
      $('#msgRePw').text('');
      return;
    }
    
    
    // 비밀번호와 재입력한 비밀번호 검사
    verifyRePw = (rePw != '') && (rePw == pw);
    if(verifyRePw){
      $('#msgRePw').text('비밀번호가 일치합니다.').css('color', 'CornflowerBlue');
    } else {
      $('#msgRePw').text('비밀번호 입력을 확인하세요.').css('color', 'FireBrick');
    }
    
  })
  
}
  
// 4. 이름
function fnCheckName(){
  
  $('#name').on('keyup', function(){
  // 입력한 이름
  let name = $(this).val();
  
  // 정규식 (5~40자, 소문자+숫자+하이픈(-)+밑줄(_) 사용 가능, 첫 글자는 소문자+숫자 사용 가능)
  let regName = /^[가-힣]{2,20}$/;
  
  // input에 입력한 내용이 없어지면 메시지를 비움
    if (name.length === 0) {
      $('#msgName').text('');
      return;
    }
  
  // 정규식 검사
  verifyName = regName.test(name);
  if(verifyName){
    $('#msgName').text('');
  } else {
    $('#msgName').text('올바른 이름을 작성해주세요').css('color', 'FireBrick');
  }
  })
      
}
  
// 5. 휴대전화
function fnCheckMobile(){
  
  $('#mobile').on('keyup', function(){
    
    // 입력한 휴대전화
    let mobile = $(this).val();
    
    // input에 입력한 내용이 없어지면 메시지를 비움
    if (mobile.length === 0) {
      $('#msgMobile').text('');
      return;
    }
    
    // 정규식
    let regMobile = /^010[0-9]{7,8}$/;
    
    // 정규식 검사
    verifyMobile = regMobile.test(mobile);
    if(verifyMobile){
      $('#msgMobile').text('');
    } else {
      $('#msgMobile').text('휴대전화 입력을 확인하세요.').css('color', 'FireBrick');        
    }
    
  })
  
}
  
// 6. 년/월/일
function fnCreateDate(){
  
  // 년도(100년 전 ~ 1년 후)
  let year = new Date().getFullYear();
  let strYear = '<option value="">년도</option>';
  for(let y = year - 100; y <= year + 1; y++){
    strYear += '<option value="' + y + '">' + y + '년</option>';
  }
  $('#birthyear').append(strYear);
  
  // 월(1 ~ 12월)
  let strMonth = '<option value="">월</option>';
  for(let m = 1; m <= 12; m++){
    if(m < 10){
      strMonth += '<option value="0' + m + '">' + m + '월</option>';
    } else {
      strMonth += '<option value="' + m + '">' + m + '월</option>';
    }
  }
  $('#birthmonth').append(strMonth);
  
  // 일(월에 따른 연동)
  $('#birthdate').append('<option value="">일</option>');
  
  $('#birthmonth').on('change', function(){
    
    $('#birthdate').empty();
    $('#birthdate').append('<option value="">일</option>');
    let endDay = 0;
    let strDay = '';
    switch($(this).val()){
    case '02':
      endDay = 29; break;
    case '04':
    case '06':
    case '09':
    case '11':
      endDay = 30; break;
    default:
      endDay = 31; break;
    }
    for(let d = 1; d <= endDay; d++){
      if(d < 10){
        strDay += '<option value="0' + d + '">' + d + '일</option>';
      } else {
        strDay += '<option value="' + d + '">' + d + '일</option>';
      }
    }
    $('#birthdate').append(strDay);
    
  });
  
}
  
// 7. 이메일 검사 및 인증코드 전송
function fnCheckEmail(){
  
  $('#btnGetCode').on('click', function(){
    
     // 입력한 이메일
    let emailInput = $('#email');
    let email = emailInput.val();
    
    emailInput.on('keyup', function() {
      if (emailInput.val().length === 0) {
        $('#msgEmail').text('');
      }
    });
    
    // id 필수 입력 필드 검사
    if ($('#id').val() == '') {
      alert('ID를 입력해주세요.');
      return;
    }
    
    // pw 필수 입력 필드 검사
    if ($('#pw').val() == '') {
      alert('비밀번호를 입력해주세요.');
      return;
    }
    
    // rePw 필수 입력 필드 검사
    if ($('#rePw').val() == '') {
      alert('비밀번호확인을 입력해주세요.');
      return;
    }
    
    // name 필수 입력 필드 검사
    if ($('#name').val() == '') {
      alert('이름을 입력해주세요.');
      return;
    }
    
    // mobile 필수 입력 필드 검사
    if ($('#mobile').val() == '') {
      alert('핸드폰번호를 입력해주세요.');
      return;
    }
    
    // birth 필수 입력 필드 검사
    if ($('#birthyear').val() == '' || $('#birthmonth').val() == '' || $('#birthdate').val() == '') {
      alert('생년월일을 입력해주세요.');
      return;
    }
    
    // 필수 입력 필드 검사
    if (email.trim() === '') {
      $('#msgEmail').text('이메일을 입력해주세요.').css('color', 'FireBrick');
      return;
    }
    
    
    
    new Promise(function(resolve, reject){
      
      // 정규식
      let regEmail = /^[a-zA-Z0-9-_]+@[a-zA-Z0-9]{2,}(\.[a-zA-Z]{2,6}){1,2}$/;
      //
      //                  gt_min     @ naver         (.com)
      //                                             (.co)(.kr)
      
      // 정규식 검사
      verifyEmail = regEmail.test(email);
      if(verifyEmail == false){
        reject(1);  // catch 메소드에 정의된 function을 호출한다. 인수로 1을 전달한다.
        return;
      }
      
      // 이메일 중복 체크
      $.ajax({
        type: 'get',
        url: '/user/verifyEmail.do',
        data: 'email=' + email,
        dataType: 'json',
        success: function(resData){  // resData = {"enableEmail": true} 또는 {"enableEmail": false}
          if(resData.enableEmail){
            resolve();  // then 메소드에 정의된 function을 호출한다.
          } else {
            reject(2);  // catch 메소드에 정의된 function을 호출한다. 인수로 2을 전달한다.
          }
        }
      })
      
    }).then(function(){
      
      // 이메일로 인증번호를 보내는 ajax
      $.ajax({
        type: 'get',
        url: '/user/sendAuthCode.do',
        data: 'email=' + email,
        dataType: 'json',
        success: function(resData){  // resData = {"authCode": "6T43G9"}  사용자에게 전송한 인증코드를 의미
          
          alert(email + "으로 인증코드를 전송했습니다.");
          
          // 메일로 받은 인증코드 입력 후 인증하기 버튼을 클릭한 경우
          $('#btnVerifyCode').on('click', function(){
            
            verifyEmail = (resData.authCode == $('#authCode').val());  // 사용자에게 전송한 인증코드 == 사용자가 입력한 인증코드값
            if(verifyEmail) {
              alert('인증되었습니다.');
            } else {
              alert('인증에 실패했습니다.');
            }
            
          })
          
        },
        error: function(jqXHR){
          alert('인증번호가 발송되지 않았습니다.');
          verifyEmail = false;
        }
      })
      
    }).catch(function(number){
      
      let msg = '';
      let color = ''; // 텍스트 색상을 저장할 변수 추가
      switch(number){
      case 1:
        msg = '<br>이메일 형식이 올바르지 않습니다.<br>';  // 정규식 실패
        color = 'FireBrick';
        break;
      case 2:
        msg = '<br>이미 사용 중인 이메일입니다.<br>';      // 이메일 중복 체크 실패
        color = 'MediumSeaGreen';
        break;
      }
      $('#msgEmail').html(msg).css('color', color);
      verifyEmail = false;
      
    })
    
  })
  
}
  
// 8. submit (회원가입)
function fnJoin(){

  $('#frmJoin').on('submit', function(event){
    
    if(verifyId == false){
      alert('아이디를 확인하세요.');
      event.preventDefault();
      return;
    } else if(verifyPw == false || verifyRePw == false){
      alert('비밀번호를 확인하세요.');
      event.preventDefault();
      return;
    } else if(verifyName == false){
      alert('이름을 확인하세요.');
      event.preventDefault();
      return;
    } else if(verifyMobile == false){
      alert('휴대전화번호를 확인하세요.');
      event.preventDefault();
      return;
    } else if($('#birthyear').val() == '' || $('#birthmonth').val() == '' || $('#birthdate').val() == ''){
      alert('생년월일을 확인하세요.');
      event.preventDefault();
      return;
    } else if(verifyEmail == false){
      alert('가입을 위해서 이메일 인증이 필요합니다.');
      event.preventDefault();
      return;
    }
    
  })
  
}
// 취소버튼 누르면 홈으로 돌아간다.
function fnCancelBtn() {
    location.href = '/';
  }

// 함수 호출
$(function(){
  fnCheckId();
  fnCheckPw();
  fnCheckPwAgain();
  fnCheckName();
  fnCheckMobile();
  fnCreateDate();
  fnCheckEmail();
  fnJoin();
})