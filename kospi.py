"""
네이버 국내증시 코스피, 코스닥의 주가와 거래량 크롤링하기.
"""

# 1. 라이브러리 호출
import requests
from bs4 import BeautifulSoup

marketType = {"KOSPI": "0", "KOSDAQ": "1"}  #kospi, kosdaq을 구분하기 위한 딕셔너리 사용.
for market, code in marketType.items():     #위의 딕셔너리 값을 반복하여 구분 적용하기 위한 for문 작성.
    for page in range(1,41):                #페이지 불러올 for문 작성.(현재 kospi기준 35페이지까지 있으므로 여유있게.)
# 2. 데이터 요청
        req = requests.get(f"https://finance.naver.com/sise/sise_market_sum.naver?sosok={code}&page={page}") #요청할 url을 가지고 와서 sosok과 page를 구분하기위한 f-string 포맷팅.
        html = req.text #html정보만 가지고올 변수 생성.
        soup = BeautifulSoup(html, "html.parser") #html 구문 분석을 위해 BeautifulSoup기능 parser라이브러리 사용.
# 3. 데이터 파싱
        contents = soup.select("#contentarea > div.box_type_l > table.type_2 > tbody > tr") #파싱 soup.select기능을 사용하여 우선 공통되는 주소까지를 설정한다.
        for contents in contents: #반복 실행할 객체를 담는 for문 작성.
            try: #.text로 필요한 정보를 불러올시에 문자가 없는 tr태그들로 인한 오류가 발생하므로 예외처리 진행.
                rank = contents.select_one("td.no").text                #순위를 불러올 구문 작성.
                name = contents.select_one("td:nth-child(2) > a").text  #종목을 불러올 구문 작성.
                price = contents.select_one("td:nth-child(3)").text     #현재 주가를 불러올 구문 작성.
                volume = contents.select_one("td:nth-child(7)").text    #거래량을 불러올 구문 작성.
                print(f"{market}-{rank}위 종목: {name} / 현재가: {price}원 / 거래량: {volume}") #f-string으로 시장구분과 반복객체를 포매팅하여 출력

            except AttributeError as e:
                #continue
                print(e) #에러발생시에는 에러명 출력.
print("-Crawling Over.-")