#!/bin/sh

CURL_OPTIONS="-s -v"
echo ">>> Testing resources/javaee8"
curl 'http://localhost:8081/ko_pure_web/resources/javaee8' ${CURL_OPTIONS}
echo

echo ">>> Testing resources/systeminfo"
curl 'http://localhost:8081/ko_pure_web/resources/systeminfo' ${CURL_OPTIONS}
echo

