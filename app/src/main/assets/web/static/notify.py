#!/usr/bin/env python3
import requests
import argparse

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("-P", "--protocol", default="http")
    parser.add_argument("-i", "--ip", default="127.0.0.1")
    parser.add_argument("-p","--port",default="8081")
    parser.add_argument("-d", "--device", default="default device")
    parser.add_argument("-t", "--title", default="default title")
    parser.add_argument("-n", "--notify", default="default notify")

    args = parser.parse_args()
    protocol = args.protocol
    ip = args.ip
    port = args.port
    device = args.device
    title = args.title
    notify = args.notify

    params = {"device": device, "title": title, "notify": notify}
    url = protocol+"://"+ip+":"+port+"/notify"

    try:
        r = requests.get(url, params=params,headers={"access_token":""})
        print(r.text)
    except Exception  as err:
        print(err)
    print("")