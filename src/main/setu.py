from pixivpy3 import *
import random

def login(username, password):
    aapi = AppPixivAPI(**_REQUESTS_KWARGS)
    aapi.login(username,password)
    return aapi

def search(aapi,keyword):
    return random.choice(aapi.search_illust(keyword, search_target='partial_match_for_tags').illusts).image_urls['large'].replace("i.pximg.net","i.pixiv.cat")
