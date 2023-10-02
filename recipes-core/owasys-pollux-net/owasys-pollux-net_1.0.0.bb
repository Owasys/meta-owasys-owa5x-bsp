LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"

SRC_URI = " file://apn.json \
            file://net.json \
            file://test.json \
            file://pollux-net \
            file://pollux-net.conf \
            file://pollux-net.logrotate \
            file://owasysd-pollux-net.service \
"

INSANE_SKIP:${PN} += "already-stripped"
DEPENDS = "dbus owasys-libs paho-mqtt-c pkgconfig-native hiredis owasys-libpollux libmnl "
RDEPENDS:${PN} = "dbus libev owasys-libs hiredis libmnl owasys-libpollux "

SYSTEMD_AUTO_ENABLE = "disable"
SYSTEMD_SERVICE:${PN} ="owasysd-pollux-net.service"

do_install() {

    # Install binaries
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/pollux-net ${D}${bindir}/pollux-net

    # Install configuration files
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}/pollux
    install -d ${D}${sysconfdir}/pollux.d
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -d ${D}${sysconfdir}/logrotate.d
    install -d ${D}${sysconfdir}/systemd/system
    
    install ${WORKDIR}/pollux-net.conf              ${D}${sysconfdir}/dbus-1/system.d/
    install ${WORKDIR}/pollux-net.logrotate         ${D}${sysconfdir}/logrotate.d/pollux-net
    install ${WORKDIR}/apn.json                     ${D}${sysconfdir}/pollux/
    install ${WORKDIR}/net.json                     ${D}${sysconfdir}/pollux/
    install ${WORKDIR}/test.json                    ${D}${sysconfdir}/pollux/
    install ${WORKDIR}/owasysd-pollux-net.service   ${D}${sysconfdir}/systemd/system/

}

FILES_${PN} += "${bindir}/pollux-net"