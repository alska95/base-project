# Dockerfile
FROM arm64v8/ubuntu

# 패키지 업데이트 및 필요한 패키지 설치
RUN apt-get update && apt-get install -y apt-transport-https ca-certificates curl software-properties-common openssh-server

# Docker의 GPG key 추가
RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -

# Docker repository 추가
RUN add-apt-repository "deb [arch=arm64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"

# Docker 설치
RUN apt-get update && apt-get install -y docker-ce

# SSH 설정
RUN mkdir /var/run/sshd
RUN echo 'root:password' | chpasswd
RUN sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config

# SSH 로그인 시 표준 입력(stdin)을 사용할 수 있게 설정
ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile

# SSH StrictHostKeyChecking 끄기
RUN mkdir -p /root/.ssh
RUN echo "Host *\n\tStrictHostKeyChecking no\n\n" > /root/.ssh/config

# Docker 서비스 시작 및 ssh 스크립트 생성
RUN echo "#!/bin/sh\n/usr/bin/dockerd & /usr/sbin/sshd -D" > /start.sh && chmod +x /start.sh

CMD ["/start.sh"]
