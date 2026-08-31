#!/bin/bash
echo ">>> [1/4] Docker 2 (Green) Git 최신 코드 동기화 (Git Pull)..."
git pull origin main

echo ">>> [2/4] 도커 멀티 스테이지 빌드 및 컨테이너 기동..."
docker compose up -d --build --remove-orphans

echo ">>> [3/4] 애플리케이션 기동 대기 중 (10초)..."
sleep 10

echo ">>> [4/4] Docker 2 상태 및 부팅 로그 확인:"
docker compose ps
docker compose logs backend | tail -n 15
echo ">>> 🎉 Docker 2 (Green) 배포 완료!"
