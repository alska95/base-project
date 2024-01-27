#!/bin/bash
# MySQL 데이터 디렉토리가 비어 있는지 확인
if [ ! "$(ls -A /var/lib/mysql)" ]; then
   # 데이터 디렉토리가 비어 있으면 초기화
   mysqld --initialize
fi
# MySQL 서버 시작
exec mysqld