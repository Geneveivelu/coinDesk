FROM openjdk:17-jdk

LABEL maintainer="yw2lu"

COPY target/lib /var/coindesk/lib
COPY target/conf /var/coindesk/conf
COPY target/demo.jar /var/coindesk/
COPY run.sh /var/coindesk/

WORKDIR /var/coindesk

ENTRYPOINT ["/bin/bash"]

CMD ["./run.sh"]
