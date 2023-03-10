/**
  1. 유저 프로파일 페이지
  (1) 유저 프로파일 페이지 구독하기, 구독취소
  (2) 구독자 정보 모달 보기
  (3) 구독자 정보 모달에서 구독하기, 구독취소
  (4) 유저 프로필 사진 변경
  (5) 사용자 정보 메뉴 열기 닫기
  (6) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
  (7) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달 
  (8) 구독자 정보 모달 닫기
 */

// (1) 유저 프로파일 페이지 구독하기, 구독취소
function toggleSubscribe(toUserId, obj) {
	if ($(obj).text() === "구독취소") {




    $.ajax({
        type: "delete",
        url: "/api/subscribe/"+toUserId,
        dataType: "json"
    }).done(res=>{
        $(obj).text("구독하기");
        $(obj).toggleClass("blue");

    }).fail(error=>{
            console.log("구독취소실패", error);
    });


	} else {

	 $.ajax({
            type: "post",
            url: "/api/subscribe/"+toUserId,
            dataType: "json"
        }).done(res=>{
            $(obj).text("구독취소");
         $(obj).toggleClass("blue");
        }).fail(error=>{
                console.log("구독하기실패", error);
        });



	}
}

// (2) 구독자 정보  모달 보기
function subscribeInfoModalOpen(pageUserId) {

	$(".modal-subscribe").css("display", "flex");

        $.ajax({
           url: `/api/user/${pageUserId}/subscribe`,
           dataType: "json"

        }).done(res=>{
//              console.log(res.data);

              res.data.forEach((u)=>{


                let item = getSubscribeModalItem(u);
                $("#subscribeModalList").append(item);
              });
        }).fail(error=>{
              console.log("구독정보 불러오기 오류" , error);
        });

}

function getSubscribeModalItem(u) {

    let item = `<div class="subscribe__item" id="subscribeModalItem-${u.id}">
                    <div class="subscribe__img">
                        <img src="/upload/${u.profileImageUrl}" onerror="this.src='/images/person.jpeg'"/>
                    </div>
                    <div class="subscribe__text">
                        <h2>${u.username}</h2>
                    </div>
                    <div class="subscribe__btn">`;
                    if(!u.equalUserState){ //동일 유저가 아닐때 버튼이 만들어져야함
                        if(u.subscribeState){
                      item+=`  <button class="cta blue" onclick="toggleSubscribe(${u.id}, this)">구독취소</button>`;
                        }else{ //구독 안한상태
            `<button class="cta" onclick="toggleSubscribe(${u.id}, this)">구독하기</button>`;
                        }

                 }
                        item +=`
                    </div>
                </div>
`


    return item;

}


// (3) 구독자 정보 모달에서 구독하기, 구독취소
//function toggleSubscribeModal(obj) {
//	if ($(obj).text() === "구독취소") {
//		$(obj).text("구독하기");
//		$(obj).toggleClass("blue");
//	} else {
//		$(obj).text("구독취소");
//		$(obj).toggleClass("blue");
//	}
//}

// (3) 유저 프로파일 사진 변경 (완)
function profileImageUpload(pageUserId , principalId) {
        //데이터 잘 와지는지 테스트
        console.log("pageUserId" , pageUserId);
        console.log("principalId" , principalId);

        if(pageUserId != principalId){
            alert("아이디의 주인만 사진을 변경할 수 있습니다.");
            return;
        }

	$("#userProfileImageInput").click();

	$("#userProfileImageInput").on("change", (e) => {
		let f = e.target.files[0];

		if (!f.type.match("image.*")) {
			alert("이미지를 등록해야 합니다.");
			return;
		}


		//서버에 이미지를 전송해줘야함.

		let profileImageForm = $("#userProfileImageForm")[0];
		// 왜 뒤에 [0]을 붙였는지? == 콘솔에 입력해보니 배열로 리턴해주더라
		// 배열 맨첫번째가 form값이고 뒤에값은 필요없기때문

		console.log(profileImageForm)

		//FormData 객체를 이용하면 form 태그의 필드와 그 값을 나타내는 일련의
		//key/value 쌍을 담을 수 있다.
		let formData = new FormData(profileImageForm);
//		console.log("폼 데이타" , formData);

		$.ajax({
		    type: "put",
		    url: `/api/user/${principalId}/profileImageUrl`,
		    data: formData,
		    contentType: false, //필수 이유 : 디폴트값이 www-x-urlencorded로 파싱되는것 방지
            processData:false,   //필수  이유: contentType을 false로 줬을 떄 QueryString 자동설정됨. 해제
            encType:"multipart/form-data",
            dataType:"json"
		}).done(res => {

            // 사진 전송 성공시 이미지 변경
		    let reader = new FileReader();
		reader.onload = (e) => {
			$("#userProfileImage").attr("src", e.target.result);
		    }
		    reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.

		}).fail(error => {
           console.log(error,"오류");
		});




	});
}


// (4) 사용자 정보 메뉴 열기 닫기
function popup(obj) {
	$(obj).css("display", "flex");
}

function closePopup(obj) {
	$(obj).css("display", "none");
}


// (5) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
function modalInfo() {
	$(".modal-info").css("display", "none");
}

// (6) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달
function modalImage() {
	$(".modal-image").css("display", "none");
}

// (7) 구독자 정보 모달 닫기
function modalClose() {
	$(".modal-subscribe").css("display", "none");
	location.reload();
}






