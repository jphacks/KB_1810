import requests

url = 'http://153.126.157.135/good'
files = {'upload_file': open("test.jpg", "rb")}

res = requests.post(url, files=files)
