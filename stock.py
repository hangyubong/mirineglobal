import pandas as pd
from bs4 import BeautifulSoup
import requests
import traceback
import datetime
import csv
import matplotlib.pyplot as plt
import logging
import sys


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


item_name = ""
code = ""
str_datefrom = ""

# 함수정리2. 종목명에 따른 정보(일자별 시세 데이터) 추출.
def get_stock_price(code, str_datefrom):
    try:
        df = pd.DataFrame()
        url = f"http://finance.naver.com/item/sise_day.nhn?code={code}"
        print(url) #url 확인
        header = {'User-agent': 'Mozilla/5.0'}
        bs = BeautifulSoup(requests.get(url=url, headers=header).text, 'html.parser')

        #마지막 페이지를 구하고 for문으로 전체페이지 정보 가져올수있도록 한다
        pgrr = bs.find("td", class_="pgRR")
        last_page = int(pgrr.a["href"].split('=')[-1])
        print(last_page)
        for page in range(1, last_page + 1):
            req = requests.get(f'{url}&page={page}', headers=header)
            df = pd.concat([df, pd.read_html(req.text)[0]])

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

        # 날짜 출력기간 범위설정 (지정날짜로부터 현재시점까지 불러오도록 함)
        startDate = datetime.datetime.strptime((str_datefrom), '%Y.%m.%d')
        df = df[df['date'] > startDate]

        df = df.reset_index(drop=True)

        return df

    except Exception as e:
        traceback.print_exc()

if __name__ == '__main__': #특정 모듈의 기능만 사용하고싶을때.

    mylogger = logging.getLogger("my") # 特定ロガーで生成
    mylogger.setLevel(logging.INFO) # INFOレベル以上は出力

    # 確認したい情報をフォーマットして出力
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s') # asctime時間, nameロガー名, levelnameロギングレベル, messageメッセージ

    # stream_hander = logging.StreamHandler() # コンソール出力
    # stream_hander.setFormatter(formatter)
    # mylogger.addHandler(stream_hander)

    file_handler = logging.FileHandler('stock.log') # ファイルに出力
    file_handler.setFormatter(formatter) # フォーマット適用
    mylogger.addHandler(file_handler)

    mylogger.info("server start!!!")

    if len(sys.argv) == 1:
        print("정보를 입력받지 못했습니다.")
        # exit(1)

    args = sys.argv
    print(args)
    print(len(sys.argv))

    item_name = args[1]
    str_datefrom = args[2].split(".")
    code = get_krx_code().query("company=='{}'".format(item_name))['code'].to_string(index=False)  # item_name 種目名による種目コードマッチング
    print('\n' + item_name + " 종목의 코드: " + code + '\n')  # 種目コードの確認
    print(args[1])
    print(args[2])
    df = get_stock_price(code, str_datefrom)
    print(df)


"""csvファイル作成/読み込み"""
df.to_csv(f'{item_name}.csv', encoding='utf-8-sig') # (item_name終値、ボリュームデータcsvファイル作成(保存)
data = pd.read_csv(f'{item_name}.csv', encoding='utf-8-sig')  # csvファイルの読み込み
print(data) # csvファイルデータ確認
print('\n' + item_name + '.csvファイル保存完了' + '\n')

"""プロットライブラリグラフ表示"""
fig = plt.figure(figsize=(11,8)) # 出力ウィンドウのサイズ設定

plt.rc('font', family='Malgun Gothic') # ハングル表示されるようにフォント設定
top_axes = plt.subplot2grid((4,4), (0,0), rowspan=3, colspan=4) # 上段グラフ位置設定
top_axes.set_title(item_name + " 種目の終値とVolume分析", fontsize=22) # タイトル設定

bottom_axes = plt.subplot2grid((4,4), (3,0), rowspan=1, colspan=4) # 下段グラフ位置設定
bottom_axes.set_xlabel('Date', fontsize=15)# X軸ラベル指定

top_axes.plot(df['date'], df['close']) # (上段)終値出力値のグラフ
bottom_axes.bar(df['date'], df['volume']) # (下段)ボリューム出力値のグラフ

plt.show()