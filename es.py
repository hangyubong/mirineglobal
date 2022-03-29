from elasticsearch import Elasticsearch
import pandas as pd

csvdata = pd.read_csv("./output/카카오게임즈.csv")
print(csvdata.head())

# default localhost:9200
# es = Elasticsearch("http://localhost:9200")
es = Elasticsearch('http://127.0.0.1:9200')

from datetime import datetime

# stock_data SET
def stock_data(row, stock_nm):
    doc = {
        'date': row['date'],
        'open': row['open'],
        'high': row['high'],
        'low': row['low'],
        'close': row['close'],
        'diff': row['diff'],
        'volume': row['volume'],
        'stock_nm': stock_nm,
        'timestamp': datetime.now(),
    }
    return doc

for index, row in csvdata.iterrows():
    doc = stock_data(row, "카카오게임즈")
    res = es.index(index="stock", body=doc)