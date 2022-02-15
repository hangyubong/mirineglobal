# 네이버 주가 일별시세 크롤링 및 데이터 시각화
___
###요구사항 정의
>1.기업의 회사코드명과 취득개시일을 입력받아 취득개시일로부터 현시점까지의 일별 종가와 거개량을 csv파일로 저장한다.
<br>
>2.csv파일을 읽어와서 출력한다.<br>
&nbsp;&nbsp;&nbsp;-회사명('코드'), 날짜, 종가, 거래량)<br>
&nbsp;&nbsp;&nbsp;-Plot을 이용한 그래프 출력
<br>

###입력
>종목코드('005930'), 취득개시일('yyyy-mm-dd') 형태로 입력받는다.

<br>

###출력
>csv파일로 데이터를 읽어와서 날짜, 종가, 거래량을 출력하고 plot라이브러리를 사용하여 주가를 그래프로 출력한다.

<br>

##출력예시(이미지)
###1. 출력데이터

---

<img src="../../Desktop/출력.jpg" width="800" height="150"/>

----
###2. Plot차트

---
<img src="../../Desktop/그래프.jpg" width="800" height="250"/>

---