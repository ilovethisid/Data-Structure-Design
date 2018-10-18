# -
-README FILE-

자료구조 설계 github repository - readme

20175532 신동진

주제: Smart Navigation

내용: 실시간 교통 상황을 반영하여 가장 빠른 길을 안내한다

*** 수정 사항 ***
고속도로를 주제 -> 주 간선도로로 변경
이유: 고속도로는 길이 하나밖에 없는 경우가 많아
실시간 소통정보를 반영하더라도 길이 크게 달라지지 않는다

Testing 완료! 몇 가지 버그가 있어 노력을 통해 해결.
Sample을 통해 길을 찾고 도로 소통 정보를 임의로 바꾸는 실험을 한 결과
대체 경로를 찾는게 확인됨.

*** 유의 사항 ***
txt파일은 데이터 파일을 저장하는 중요한 파일이니 testing을 할 시에는
하나의 파일에 저장해 그 파일의 directory를 소스파일에 넣어주어야
프로그램이 작동한다.
->Point.java와 Link.java의 filepath에 넣어주면 됨

testing을 할 때 엑셀파일을 이용해 start node와 end node의 index를 물어볼 때
이름과 index를 찾아 테스트를 할 수 있다

sample결과를 출력하는 것으로 테스트를 할 수도 있음

참고로 roadData나 trafficData를 업데이트 하는 것은 매우 긴 시간이 소요됨
(30분 정도)

** 감사합니다!!! **
