import sys
from pixivpy3 import PixivAPI

_REQUESTS_KWARGS = {
    'proxies': {
        'https': 'http://127.0.0.1:8118',
    },
    'verify': True,
}
fglfile = open('src/main/resources/fgl.txt', 'r')
fgl = []
for line in fglfile.readlines():
    fgl.append(line.strip())
fglfile.close()
api = PixivAPI(**_REQUESTS_KWARGS)
api.auth(refresh_token=sys.argv[1])
illusts = None
if sys.argv[2] == "ranking":
    illusts = api.ranking_all().response[0].works
    for i in illusts:
        if str(i.work.id) not in fgl:
            f = open('src/main/resources/fgl.txt', 'a')
            fgl.append(str(i.work.id))
            f.write(str(i.work.id) + '\n')
            f.close()
            print(i.work.image_urls.large.replace("i.pximg.net", "i.pixiv.cat") + " " + str(i.work.id))
            break
else:
    illusts = api.search_works(sys.argv[2], types=["illustration"], per_page=500, mode=sys.argv[3], sort="popular").response
    for i in illusts:
        if str(i.id) not in fgl:
            f = open('src/main/resources/fgl.txt', 'a')
            fgl.append(str(i.id))
            f.write(str(i.id) + '\n')
            f.close()
            print(i.image_urls.large.replace("i.pximg.net", "i.pixiv.cat") + " " + str(i.id))
            break
