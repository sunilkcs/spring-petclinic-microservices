FROM mysql:5.6
ENV MYSQL_DATABASE=petclinic
ENV MYSQL_ROOT_PASSWORD=root
COPY ./sql-scripts/ /docker-entrypoint-initdb.d/
EXPOSE 3306
CMD ["mysqld"]
