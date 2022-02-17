import pandas as pd
from bs4 import BeautifulSoup
import requests
import traceback
import datetime
import csv
import matplotlib.pyplot as plt

# 함수정리1. 한국거래소 상장법인목록에서 전체 데이터를 받아 필요한 정보를 추출.
def get_krx_code():
    try:
        url = 'http://kind.krx.co.kr/corpgeneral/corpList.do?method=download&searchType=13{0}' #한국거래소 상장법인목록 다운로드
        stock_code = pd.read_html(url, header=0)[0] #url html의 table가지고옴 / header=0으로 0번째줄 컬럼설정 / [0]으로 인덱싱하여 데이터프레임 타입으로 만들어준다.
        stock_code['종목코드'] = stock_code['종목코드'].map('{:06d}'.format) # url에 사용하기 위해 '종목코드' 항목을 0을 채워넣은 6자리 숫자 포맷으로 '종목코드'에 다시 저장해준다.
        stock_code = stock_code[['회사명', '종목코드', '상장일']] # 추출할 컬럼만 선정.
        stock_code = stock_code.rename(columns={'회사명': 'company', '종목코드': 'code', '상장일': 'listing_date'}) # 컬럼명 한글->영문으로 변경.
        stock_code['listing_date'] = pd.to_datetime(stock_code['listing_date'])  # 상장일 컬럼 datetime으로 날짜형식 적용.
        # print(stock_code)  # '회사명', '종목코드', '상장일' 출력확인

        # <회사명, 종목코드 리스트 csv파일로 저장>
        stock_code = stock_code.sort_values(by='code')
        krxList = stock_code[['code', 'company']]
        # write CSV
        with open(file='company.csv', mode='w', encoding='utf-8-sig', newline='') as f:
            csv_f = csv.writer(f)
            for i in range(len(krxList)):
                value = [krxList.code.values[i], krxList.company.values[i]]
                print(value)
                csv_f.writerow(value)
            print("-상장법인 코드분류 리스트 csv파일 저장완료-")

        return stock_code

    except Exception as e:
        traceback.print_exc()


# 종목명 입력
item_name = '카카오'
stock = get_krx_code().query("company=='{}'".format(item_name))['code'].to_string(index=False) # item_name 종목 코드를 가지고옴
print('\n' + item_name + " 종목의 코드: "+ stock + '\n') # 종목코드 확인

# 함수정리2. 종목명에 따른 정보(일자별 시세 데이터) 추출.
def get_stock_price(code):
    try:
        df = pd.DataFrame()
        url = f"http://finance.naver.com/item/sise_day.nhn?code={code}"
        print(url) #url 확인
        header = {'User-agent': 'Mozilla/5.0'}
        bs = BeautifulSoup(requests.get(url=url, headers=header).text, 'html.parser')

        #마지막 페이지를 구하고 for문으로 전체페이지 정보 가져올수있도록 한다
        pgrr = bs.find("td", class_="pgRR")
        last_page = int(pgrr.a["href"].split('=')[-1])

        for page in range(1, last_page + 1):
            req = requests.get(f'{url}&page={page}', headers=header)
            df = pd.concat([df, pd.read_html(req.text)[0]])

            # 지정일자로부터 최근날짜까지 가지고 온다
            str_datefrom = datetime.datetime.strftime(datetime.datetime(year=1999, month=11, day=11), '%Y.%m.%d')
            df = df[df['날짜'] > str_datefrom]

        # 영문컬럼명으로 변경 및 필요없는 컬럼명 제거
        df = df.rename(columns={'날짜': 'date', '종가': 'close', '전일비': 'diff'
            , '시가': 'open', '고가': 'high', '저가': 'low', '거래량': 'volume'})  # 영문으로 컬럼명 변경
        df['date'] = pd.to_datetime(df['date'])
        df = df.dropna() # 결측값 제거
        df[['close', 'diff', 'open', 'high', 'low', 'volume']] = \
            df[['close', 'diff', 'open', 'high', 'low', 'volume']].astype(int)  # int형으로 변경. 출력 값 소수점(.0)제거
        df = df[['date', 'open', 'high', 'low', 'close', 'diff', 'volume']]  # 컬럼 순서 정렬
        df.drop(columns=['diff', 'open', 'high', 'low'], inplace=True)  # 필요없는 컬럼 제거
        df = df.sort_values(by='date')  # 날짜순으로 정렬

        df = df.reset_index(drop=True)

        return df

    except Exception as e:
        traceback.print_exc()

df = get_stock_price(stock) #코드명에 따른 데이터출력 함수
# print(df) #item_name의 종가,거래량 데이터확인

"""csv파일 생성 / 읽어오기"""
df.to_csv(f'{item_name}.csv', encoding='utf-8-sig') #(item_name종가,거래량 데이터 csv파일 생성(저장)
data = pd.read_csv(f'{item_name}.csv', encoding='utf-8-sig')  #csv파일 불러오기
print(data) #csv파일 데이터 확인
print('\n' + item_name + '.csv 파일저장 완료' + '\n')

"""plot라이브러리 그래프 나타내기"""
fig = plt.figure(figsize=(11,8)) #출력창 사이즈 설정
plt.rc('font', family='Malgun Gothic') #한글 표시되도록 글꼴설정
top_axes = plt.subplot2grid((4,4), (0,0), rowspan=3, colspan=4) #상단그래프 위치 설정
top_axes.set_title(item_name + "종목의 종가와 거래량 분석", fontsize=22) #제목 설정

bottom_axes = plt.subplot2grid((4,4), (3,0), rowspan=1, colspan=4) #하단그래프 위치 설정
bottom_axes.set_xlabel('Date', fontsize=15)# X축 라벨 지정

top_axes.plot(df['date'], df['close']) #(상단) 종가 출력값의 그래프
bottom_axes.bar(df['date'], df['volume']) #(하단) 거래량 출력값의 그래프

plt.show()
