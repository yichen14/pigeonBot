import sys
from pixivpy3 import PixivAPI

_REQUESTS_KWARGS = {
     'proxies': {
         'https': 'http://127.0.0.1:8118',
     },
     'verify': True,
}

class SetuService():
    def __init__(self,username,password):
        self._username=username
        self._password=password
        _fglfile= open('src/main/resources/fgl.txt','r')
        self._fgl = []
        for line in _fglfile.readlines():
            self._fgl.append(line.strip())
        _fglfile.close()
        self._api=PixivAPI(**_REQUESTS_KWARGS)
        self._api.login(self._username,self._password)
    def search(self,keyword,mode):
        _res=self._api.search_works(keyword,types=["illustration"],per_page=1000,mode=mode,sort="popular")
        _illusts=_res.response
        for i in _illusts:
            if str(i.id) not in self._fgl:
                f = open('src/main/resources/fgl.txt', 'a')
                self._fgl.append(str(i.id))
                f.write(str(i.id) + '\n')
                f.close()
                return i.image_urls.large.replace("i.pximg.net","i.pixiv.cat")+" "+str(i.id)

print(SetuService(sys.argv[1],sys.argv[2]).search(sys.argv[3],sys.argv[4]))