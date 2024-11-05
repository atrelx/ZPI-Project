FROM mysql:latest

ENV MYSQL_ROOT_PASSWORD=rootpassword
ENV MYSQL_DATABASE=mydatabase
ENV MYSQL_USER=myuser
ENV MYSQL_PASSWORD=mypassword

ENV MYSQL_CHARSET=utf8mb4
ENV MYSQL_COLLATION=utf8mb4_unicode_ci

RUN echo "[mysqld]" > /etc/mysql/conf.d/my_custom.cnf \
    && echo "log_bin_trust_function_creators=1" >> /etc/mysql/conf.d/my_custom.cnf