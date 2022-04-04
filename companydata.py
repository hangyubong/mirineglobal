from urllib.request import urlopen
from bs4 import BeautifulSoup


class Company:
    def __init__(self, Name, Code):
        self.Name = Name
        self.Code = Code
        self.LoadStockSummary(Code)

    def LoadStockSummary(self, ticker):
        url = "http://comp.fnguide.com/SVO2/ASP/SVD_Main.asp?pGB=1&gicode=A" + ticker + "&cID=&MenuYn=Y&ReportGB=&NewMenuID=Y&stkGb=701"
        response = urlopen(url)
        soup = BeautifulSoup(response, 'html.parser')

        # get PER PBR ROE
        tmp = soup.find('div', {'class': 'corp_group2'})
        tmp2 = tmp.findAll('dd')
        self.PER = tmp2[1].contents
        self.PER12M = tmp2[3].contents
        self.PERIndustry = tmp2[5].contents
        self.PBR = tmp2[7].contents
        self.DividendRatio = tmp2[9].contents

        # get Summary
        for anchor in soup.select(".um_bssummary"):
            pass
        self.Summary = anchor.get_text()

    def print_info(self):
        print("Name: ", self.Name)
        print("Code: ", self.Code)
        print("Summary: ", self.Summary)
        print("PER: ", self.PER)
        print("PER12M: ", self.PER12M)
        print("PERIndustry: ", self.PERIndustry)
        print("PBR: ", self.PBR)
        print("DividendRatio: ", self.DividendRatio)


def run():

    Name = "삼성전자"
    Code = "005930"
    company = Company(Name, Code)
    company.print_info()


if __name__ == "__main__":
    run()


