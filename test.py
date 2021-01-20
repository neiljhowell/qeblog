import requests

url = 'http://wp.qe-blog.10.50.222.172.xip.io/feed/'
data = requests.get(url)
with open('test.xml','w', encoding="utf-8") as out_f:
   out_f.write(data.text)
