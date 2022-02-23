import pandas as pd
from bs4 import BeautifulSoup
import requests
import traceback
import datetime
import csv
import sys
import matplotlib.pyplot as plt
import logging

# 関数定理1. 韓国取引所上場法人目録から全データを受け、必要な情報を抽出。
def get_krx_code():
    try:
        url = 'http://kind.krx.co.kr/corpgeneral/corpList.do?method=download&searchType=13{0}' # 韓国取引所上場法人目録ダウンロード
        stock_code = pd.read_html(url, header=0)[0] #url htmlのテーブルを持ってくる / header=0で0番目の行カラム設定 / [0]でインデックシングしてデータフレームタイプにしてくれる。
        stock_code['종목코드'] = stock_code['종목코드'].map('{:06d}'.format) # urlに使用するために'種目コード'項目を0を満たした6桁の数字のフォーマットで'種目コード'に再保存する。
        stock_code = stock_code[['회사명', '종목코드', '상장일']] # 抽出するカラムのみ選定。
        stock_code = stock_code.rename(columns={'회사명': 'company', '종목코드': 'code', '상장일': 'listing_date'}) # カラム名をハングル - >英文に変更。
        stock_code['listing_date'] = pd.to_datetime(stock_code['listing_date'])  # 上場日カラムdatetimeに日付形式を適用。
        # print(stock_code)  # 「会社名」、「種目コード」、「上場日」出力確認

        # <会社名、種目コードリストcsvファイルで保存>
        stock_code = stock_code.sort_values(by='code')
        krxList = stock_code[['code', 'company']]
        filename = 'company.csv'
        # write CSV
        with open(file=filename, mode='w', encoding='utf-8-sig', newline='') as f:
            csv_f = csv.writer(f)
            for i in range(len(krxList)):
                value = [krxList.code.values[i], krxList.company.values[i]]
                # print(value)
                csv_f.writerow(value)
            mylogger.info("Success! save codelist file : " + filename)

        return stock_code

    except Exception as e:
        mylogger.info('\033[31m' + "Check the 'error.log' file!!")
        logging.error(traceback.format_exc())

# 関数定理2. 種目名に応じた情報（日付別の相場データ）抽出
def get_stock_price(code, str_datefrom):
    try:
        df = pd.DataFrame()
        url = f"http://finance.naver.com/item/sise_day.nhn?code={code}"
        print("URL : " + url) #url確認
        header = {'User-agent': 'Mozilla/5.0'}
        bs = BeautifulSoup(requests.get(url=url, headers=header).text, 'html.parser')

        # 最後のページを探してfor文で全ページの情報を読み込めるようにする。
        pgrr = bs.find("td", class_="pgRR")
        last_page = int(pgrr.a["href"].split('=')[-1])

        for page in range(1, last_page + 1):
            req = requests.get(f'{url}&page={page}', headers=header)
            df = pd.concat([df, pd.read_html(req.text)[0]])

        # 英文カラム名に変更·不要カラム名を削除
        df = df.rename(columns={'날짜': 'date', '종가': 'close', '전일비': 'diff'
            , '시가': 'open', '고가': 'high', '저가': 'low', '거래량': 'volume'})  # 英文でカラム名を変更
        df['date'] = pd.to_datetime(df['date'])
        df = df.dropna() # 数値のない行除去
        df[['close', 'diff', 'open', 'high', 'low', 'volume']] = \
            df[['close', 'diff', 'open', 'high', 'low', 'volume']].astype(int)  # int形に変更。 出力数値小数点(.0)除去
        df = df[['date', 'open', 'high', 'low', 'close', 'diff', 'volume']]  # カラム順序整列
        df.drop(columns=['diff', 'open', 'high', 'low'], inplace=True)  # 不要なカラムの除去, 変更されたデータフレームを適用
        # print(df.columns)
        df = df.sort_values(by='date')  # 日付順にソート

        # 日付 出力期間 範囲設定 (指定日付から現時点まで読み込むようにする)
        str_datefrom = datetime.datetime.strptime(str_datefrom, '%Y.%m.%d')
        df = df[df['date'] > str_datefrom]
        # print(str_datefrom) # 入力された日付がちゃんと入ってるか確認。
        df = df.reset_index(drop=True)
        mylogger.info("Success! Crawling.")
        return df

    except Exception as e:
        mylogger.info('\033[31m' + "Check the 'error.log' file!!")
        logging.error(traceback.format_exc())

if __name__ == '__main__':
    try:
        # logging --
        mylogger = logging.getLogger("my")  # 特定ロガーで生成
        mylogger.setLevel(logging.INFO)  # INFOレベル以上は出力

        # 確認したい情報をフォーマットして出力
        consolelog = logging.Formatter('%(levelname)s - %(message)s')  # levelnameロギングレベル, messageメッセージ
        filelog = logging.Formatter('%(asctime)s - %(name)s - %(module)s - %(funcName)s - %(levelname)s - %(message)s')  # asctime時間, nameロガー名, levelnameロギングレベル, messageメッセージ

        # ハンドラsetting
        stream_hander = logging.StreamHandler() # コンソール出力
        stream_hander.setFormatter(consolelog)
        mylogger.addHandler(stream_hander)
        file_handler = logging.FileHandler('stock.log')  # ファイルに出力
        file_handler.setFormatter(filelog)  # フォーマット適用
        mylogger.addHandler(file_handler)

        # ERRORレベル以上のログを`error.log`に出力するハンドラsetting
        logger = logging.getLogger()  # 特定ロガーで生成
        errorlog = logging.Formatter('%(asctime)s - %(name)s - %(module)s - %(funcName)s - %(levelname)s - %(message)s', '%Y-%m-%d %H:%M:%S')
        file_error_handler = logging.FileHandler('error.log')
        file_error_handler.setLevel(logging.ERROR)
        file_error_handler.setFormatter(errorlog)
        logger.addHandler(file_error_handler)

        # sys.argv --
        args = sys.argv
        item_name = args[1] # Input company
        str_datefrom = args[2] # Input date
        code = get_krx_code().query("company=='{}'".format(item_name))['code'].to_string(index=False)  # item_name 種目名による種目コードマッチング

        if len(sys.argv) == 1: # アーギュメントレングス / インデックス[0] = filenameが入ってる。
            mylogger.info("No parameter information was received.") # パラメータを受けていない
            exit(1) # 終了される
        else:
            mylogger.info("Success! start : " + item_name + ", " + str_datefrom + " (Successfully the stock price from the date)")

        # 入力された情報確認（console)
        # mylogger.info("The number of indexes : " + str(len(sys.argv)))
        print("Check the index value : " + str(args))
        print("Company name : " + args[1]) # Check name
        print("Company code: " + code)  # Check code
        print("startdate : " + args[2]) # Check date

        df = get_stock_price(code, str_datefrom)
        # print(df)

        # csvファイル作成/読み込み --
        df.to_csv(f'{item_name}.csv', encoding='utf-8-sig')  # (item_name終値、ボリュームデータcsvファイル作成(保存) /,index=Falseを使うと、ファイル保存時に自動追加される索引(index)なし
        csvDf = pd.read_csv(f'{item_name}.csv', encoding='utf-8-sig', index_col = 0)  # csvファイルの読み込み / index_col = 0 : 第一カラムをindexとして使用するように指定
        # csvDf.drop(['Unnamed: 0'], axis=1, inplace=True) # 上のindex_col = 0の代わりにこのコードも使える / Unnamed: 0カラムをdropして除去
        print(csvDf)  # csvファイルデータ確認
        mylogger.info("Success! save company price file : " + item_name + ".csv")

        # プロットライブラリグラフ表示 --
        fig = plt.figure(figsize=(11, 8))  # 出力ウィンドウのサイズ設定
        plt.rc('font', family='Malgun Gothic')  # ハングル表示されるようにフォント設定
        top_axes = plt.subplot2grid((4, 4), (0, 0), rowspan=3, colspan=4)  # 上段グラフ位置設定
        top_axes.set_title(item_name + " 種目の終値とVolume分析", fontsize=22)  # タイトル設定
        bottom_axes = plt.subplot2grid((4, 4), (3, 0), rowspan=1, colspan=4)  # 下段グラフ位置設定
        bottom_axes.set_xlabel('Date', fontsize=15)  # X軸ラベル指定
        top_axes.plot(df['date'], df['close'])  # (上段)終値出力値のグラフ
        bottom_axes.bar(df['date'], df['volume'])  # (下段)ボリューム出力値のグラフ
        plt.show()
        mylogger.info("Success! Plot graph.")

    except Exception as e:
        mylogger.info('\033[31m' + "Check the 'error.log' file!!")
        logging.error(traceback.format_exc())