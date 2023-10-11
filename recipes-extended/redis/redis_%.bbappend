# In order to get ready comms between polluxnet and redis, both must be using the same socker /tmp/redis.sock
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-7:"
SRC_URI += " file://redis-server.service "

do_install () {
    
    export PREFIX=${D}/${prefix}
    oe_runmake install
    install -d ${D}/${sysconfdir}/redis
    install -m 0644 ${WORKDIR}/redis.conf ${D}/${sysconfdir}/redis/redis.conf
    install -d ${D}/${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/init-redis-server ${D}/${sysconfdir}/init.d/redis-server
    install -d ${D}/var/lib/redis/
    chown redis.redis ${D}/var/lib/redis/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/redis-server.service ${D}${systemd_system_unitdir}
    sed -i 's!/usr/sbin/!${sbindir}/!g' ${D}${systemd_system_unitdir}/redis-server.service

    if [ "${REDIS_ON_SYSTEMD}" = true ]; then
        sed -i 's!daemonize yes!# daemonize yes!' ${D}/${sysconfdir}/redis/redis.conf
    fi

    sed -i 's!# unixsocket /tmp/redis.sock!unixsocket /tmp/redis.sock!' ${D}/${sysconfdir}/redis/redis.conf
    sed -i 's!# unixsocketperm 700!unixsocketperm 700!' ${D}/${sysconfdir}/redis/redis.conf

}

SYSTEMD_SERVICE:${PN} = "redis-server.service"
