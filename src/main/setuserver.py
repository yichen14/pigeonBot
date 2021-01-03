from pixivpy3 import PixivAPI 
import rpyc
import sys

_REQUESTS_KWARGS = {
     #'proxies': {
     #    'https': 'http://127.0.0.1:1080',
     #},
     #'verify': True,
}

class SetuService(rpyc.Service):
    def __init__(self,username,password):
        super().__init__()
        self._username=username
        self._password=password
        _fglfile= open('src/main/resources/fgl.txt','r')
        self._fgl = []
        for line in _fglfile.readlines():
            self._fgl.append(line.strip())
        _fglfile.close()
    def on_connect(self, conn):
        self._api=PixivAPI(**_REQUESTS_KWARGS)
        self._api.login(self._username,self._password)
    def exposed_search(self,keyword,mode):
        _res=self._api.search_works(keyword,types=["illustration"],per_page=500,mode=mode,sort="popular")
        _illusts=_res.response
        #print(_res)
        for i in _illusts:
            if str(i.id) not in self._fgl:
                f = open('src/main/resources/fgl.txt', 'a')
                self._fgl.append(str(i.id))
                f.write(str(i.id) + '\n')
                f.close()
                return i.image_urls.large.replace("i.pximg.net","i.pixiv.cat")
                break


rpyc.utils.server.ThreadedServer(SetuService(sys.argv[1],sys.argv[2]),port=11451).start()