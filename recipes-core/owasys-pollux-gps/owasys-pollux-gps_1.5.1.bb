DESCRIPTION = " Owasys pollux service "
LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"

SRC_URI = " file://pollux-gps \
            file://gps.json \
            file://pollux.target \
            file://pollux-gps.conf \
            file://owasysd-pollux-gps.service \
"

S = "${WORKDIR}/git"

INSANE_SKIP:${PN} += "already-stripped"

DEPENDS = "dbus owasys-libs paho-mqtt-c pkgconfig-native hiredis owasys-libpollux libmnl "
RDEPENDS:${PN} = "dbus libev owasys-libs hiredis libmnl owasys-libpollux "

SYSTEMD_AUTO_ENABLE = "disable"
SYSTEMD_SERVICE:${PN} ="owasysd-pollux-gps.service"

do_install() {

    # Install binaries
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/pollux-gps           ${D}${bindir}

    # Install configuration files
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}/pollux
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -d ${D}${sysconfdir}/systemd/system
    
    install ${WORKDIR}/pollux-gps.conf             ${D}${sysconfdir}/dbus-1/system.d/
    install ${WORKDIR}/gps.json                    ${D}${sysconfdir}/pollux/
    install ${WORKDIR}/pollux.target               ${D}${sysconfdir}/systemd/system/
    install ${WORKDIR}/owasysd-pollux-gps.service  ${D}${sysconfdir}/systemd/system/

    ln -s -r ${D}/${sysconfdir}/systemd/system/owasysd-pollux-gps.service     ${D}/${sysconfdir}/systemd/system/pollux-gps.service 
    ln -s -r ${D}/${sysconfdir}/systemd/system/owasysd-pollux-gps.service     ${D}/${sysconfdir}/systemd/system/polluxgps.service 


}

FILES_${PN} += "${bindir}/pollux-gps"