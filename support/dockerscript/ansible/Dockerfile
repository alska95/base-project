# 기본 이미지로 arm64v8/ubuntu를 사용합니다.
FROM arm64v8/ubuntu

# 패키지 업데이트 및 필요한 패키지 설치
RUN apt-get update && apt-get install -y software-properties-common openssh-server

# Ansible PPA 추가
RUN apt-add-repository ppa:ansible/ansible -y

# Ansible 설치
RUN apt-get update && apt-get install -y ansible

# SSH 설정
RUN mkdir /var/run/sshd
RUN echo 'root:password' | chpasswd
RUN sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config

# SSH 로그인 시 표준 입력(stdin)을 사용할 수 있게 설정
ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile

# Ansible playbook 파일 복사
COPY playbook.yml /root/

# SSH StrictHostKeyChecking 끄기
RUN mkdir -p /root/.ssh
RUN echo "Host *\n\tStrictHostKeyChecking no\n\n" > /root/.ssh/config

# SSH 서비스 시작 및 Ansible playbook 실행 스크립트 생성
RUN echo "#!/bin/sh\n/usr/sbin/sshd -D & ansible-playbook /root/playbook.yml" > /start.sh && chmod +x /start.sh

CMD ["/start.sh"]