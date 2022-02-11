"""라이브러리 호출"""
import requests
from bs4 import BeautifulSoup
import pandas as pd
import datetime
import traceback
import matplotlib.pyplot as plt

"""데이터 요청"""
code = '035720' # 카카오 종목코드.
url = f"http://finance.naver.com/item/sise_day.nhn?code={code}" #크롤링할 주소 url변수 생성.
headers = {'User-agent': 'Mozilla/5.0'} # 웹브라우저 접속처럼 인식시키기 위해 유저정보 추가
req = requests.get(url=url, headers = headers) #요청할 url을 리퀘스트 변수에 담음
bs = BeautifulSoup(req.text, 'html.parser') #BeautifulSoup기능으로 html구문 분석을 위한 parser사용 パーザ
print(url)

"""데이터 파싱"""
# 마지막 페이지 구하기
pgrr = bs.find('td', class_='pgRR') #タッグ
lastPage = int(pgrr.a["href"].split('=')[-1]) #スプリット

df = pd.DataFrame() #판다스 DataFrame으로 출력형태를 갖출 변수 생성.

for page in range(1, lastPage): #페이지를 불러올 for문 작성.
    try:
        page_url = '{}&page={}'.format(url, page) #フォーマット
        df = pd.concat([pd.read_html(requests.get(page_url, headers={'User-agent': 'Mozilla/5.0'}).text)[0]]) #コンカット
        df = df.dropna()  # 결측값 제거 ナル

        str_datefrom = datetime.datetime.strftime(datetime.datetime(year=1999, month=11, day=11), '%Y.%m.%d') #날짜지정
        str_dateto = datetime.datetime.strftime(datetime.datetime.today(), '%Y.%m.%d') #현재날짜

        df_filtered = df[df["날짜"] > str_datefrom]
    except Exception as e:
        traceback.print_exc() # 에러에 대한 자세한 로그 출력.

    print(df_filtered)

print("- 조회일자 : " + str_datefrom + " ~ " + str_dateto + " -")

"""csv파일 생성 및 불러오기"""
df.to_csv('카카오.csv', encoding='utf-8-sig')      #csv 파일 생성
data = pd.read_csv("카카오.csv", encoding="utf-8")  #csv파일 불러오기

"""plot 그리기"""
plt.figure                             #figure생성
plt.plot(data['날짜'], data['종가'])    #plot에 표시할 데이터 담기
plt.show()                            #plot 결과보기
