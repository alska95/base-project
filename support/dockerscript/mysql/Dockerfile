FROM mysql:latest
ENV MYSQL_DATABASE test
ENV MYSQL_ROOT_HOST %
COPY init.sql /docker-entrypoint-initdb.d/init.sql
COPY init.sh /init.sh
RUN chmod +x /init.sh
EXPOSE 3306
ENTRYPOINT ["/init.sh"]