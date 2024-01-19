FROM mysql
ENV MYSQL_DATABASE test
EXPOSE 3306
RUN ["chown", "-R", "mysql:mysql", "/var/lib/mysql"]
ENTRYPOINT ["mysqld", "--secure-file-priv="]