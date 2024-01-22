FROM mysql
ENV MYSQL_DATABASE test
ENV MYSQL_ROOT_HOST %
COPY init.sql /docker-entrypoint-initdb.d/
EXPOSE 3306
ENTRYPOINT ["mysqld", "--secure-file-priv="]