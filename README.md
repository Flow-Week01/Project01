# 채우는 공간 : 채우공

![app1](https://user-images.githubusercontent.com/65072995/148039199-99cc45bc-a88c-4328-8933-1a3152d68cad.png)

> 가구를 공간에 가상으로 배치할 수 있고, 배치한 장면이나 원하는 인테리어 사진을 갤러리에 저장하여 볼 수 있으며, 인테리어 업체들의 연락처를 추가/수정/삭제 할 수 있는 **인테리어 증강 현실 애플리케이션**
> 
- TAB1 : 연락처
- TAB2 : 갤러리
- TAB3 : AR View

### TEAM

김민결 이현정

### NEED

사진 촬영/동영상 녹화 권한 허용 필요

## TAB 1 . 연락처

![app3](https://user-images.githubusercontent.com/65072995/148039203-99653f0e-e688-43fc-9f3a-746c9f59a591.jpg)

- JSON 파일을 사용한 기기 내부 저장소 이용
- ListView 구현 (ScrollView - 스크롤 가능)
1. **상세 정보 페이지**
    
    ![app3(1)](https://user-images.githubusercontent.com/65072995/148032165-dfa4a283-b568-4372-a613-1a7ef59b2e09.png)
    
    리스트의 이름 클릭 시 실행.
    
2. **연락처 추가/수정/삭제**
    
    ![apps](https://user-images.githubusercontent.com/65072995/148032177-57eef1e3-0b57-4546-a686-d78232dddb36.png)
    
    - 추가 (ADD) - ADD 버튼 클릭 시 실행. Input validation check (Name/Number)
    - 수정 (EDIT) - EDIT 버튼 클릭 시 실행. Input validation check (Name/Number)
    - 삭제 (DELETE) - 휴지통 아이콘 클릭 시 실행.
    
    파일 입출력을 활용해 앱 종료 후 재실행 해도 정보 수정이 유지될 수 있도록 함.
    
3. **나의 PICK**
    
    ![change](https://user-images.githubusercontent.com/65072995/148032179-d3562b57-e17d-4761-b009-9ae4d9cd3c88.png)
    
    - 등록 - PICK 해두길 원하는 업체의 프로필 아이콘을 클릭 시 랜덤으로 색상 변경.
    - 취소 - 새로 고침 아이콘 클릭 시 PICK 취소(색상 reset) 가능.

## TAB 2. 갤러리

![12](https://user-images.githubusercontent.com/65072995/148032167-9d42d304-bbe6-4a70-bda2-54d6ab11b198.png)

- 슬라이드 뷰와 그리드 뷰, 두 가지 레이아웃으로 갤러리 디자인

1. 사진 촬영
    
    ![6](https://user-images.githubusercontent.com/65072995/148032130-67cdcf15-6148-4690-9cea-d2be2507fb17.png)
    
    
    - 휴대폰 카메라로 새 이미지 촬영 및 갤러리에 추가. 두 개의 뷰에서 모두 가능.
    
2. 뷰 전환
    
    
    ![78](https://user-images.githubusercontent.com/65072995/148032159-d2ae60c2-b8ef-492b-816e-19ea93b99663.png)
    
    ![3](https://user-images.githubusercontent.com/65072995/148032766-cc4305ce-1b12-458e-85ec-f8e02ab6ad53.gif)
    
    - 전구 이미지 클릭하여 뷰 간 전환 가능.
    
3. 스위치
    
    ![9](https://user-images.githubusercontent.com/65072995/148032145-7310cc27-d969-4836-b904-e252e2ab20d8.png)
    
    ![4](https://user-images.githubusercontent.com/65072995/148032772-ba83ea9e-f9de-4176-ab01-53656d92de0c.gif)
    
    - 슬라이드 뷰에 나타나는 전구 조명을 on/off.
    
4. 이미지 선택 (슬라이드 뷰)
    
    ![34](https://user-images.githubusercontent.com/65072995/148032168-98ee7e95-9f20-48a4-8237-268a08710ab4.png)
    
    - 하단 scroll view에서 원하는 이미지 클릭하여 중앙에 표시.
    
5. 크게 보기 (그리드 뷰)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
    
    ![5](https://user-images.githubusercontent.com/65072995/148032820-c0463304-ba9d-40da-85af-295f52ee025a.gif)
    
    - 각 이미지 클릭하여 전체 화면으로 보기.
    - 좌우 스크롤로 이전, 다음 이미지 확인.
    - 화면을 한 번 클릭하여 몇 번째 이미지인지 확인 가능.

## TAB 3. AR View

![5](https://user-images.githubusercontent.com/65072995/148032121-c8066955-b9b7-45b2-86ec-3a3dafe32661.png)

- AR Core 사용 : Google에서 제공하는 AR 애플리케이션 개발용 SDK

1. 가구 추가/이동/삭제/리셋
    
    ![6](https://user-images.githubusercontent.com/65072995/148036986-18926790-8f06-4449-bde5-05bdeb1df7d5.gif)
    
    - 추가 (ADD) 및 이동 - 하단 scroll view에서 원하는 가구 클릭 시 추가 가능.
    
    ![10](https://user-images.githubusercontent.com/65072995/148038622-faba6d5a-93c3-4924-971e-ccb3ba5a4a25.gif)
    
    - 이동 (MOVE) - 배치된 가구를 잡고 끌어 이동 가능.
    
    ![7](https://user-images.githubusercontent.com/65072995/148036975-fdaf986b-29f4-41b7-83fc-533211ec9d09.gif)
    
    - 삭제 (DELETE) - 삭제하고 싶은 가구를 선택, 하단에 나타나는 X 버튼 클릭 시 삭제 가능.
    
    ![8](https://user-images.githubusercontent.com/65072995/148036980-ab71febf-81e2-4529-85dc-97afd192d3ca.gif)
    
    - 리셋 (RESET) - 전체 가구를 삭제하고 싶을 때 상단 RESET 버튼 클릭.
    
2. CAPTURE
    
    ![9](https://user-images.githubusercontent.com/65072995/148041272-00e45f13-84b7-4057-9a1a-f0d78ba7e678.jpg)
    - 가구 배치 후 상단 CAPTURE 버튼 클릭 시 애플리케이션 내 갤러리에 저장 가능.