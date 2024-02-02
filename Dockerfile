FROM ubuntu:jammy
COPY target/virtual-threads /virtual-threads
CMD ["/virtual-threads"]