LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"

INSANE_SKIP:${PN} += "already-stripped"
DEPENDS = "dbus owasys-libpollux paho-mqtt-c "
RDEPENDS:${PN} = "dbus libev owasys-libpollux hiredis "

inherit systemd

SRC_URI = " file://mqtt.json \
            file://polluxc-mqtt \
            file://pollux.target \
            file://owasysd-polluxc-mqtt.service \
"

do_install() {

    # Install binaries
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/polluxc-mqtt ${D}${bindir}

    # Install configuration files
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}/pollux
    install -d ${D}${sysconfdir}/systemd/system
        
    install ${WORKDIR}/mqtt.json                           ${D}${sysconfdir}/pollux/
    install ${WORKDIR}/pollux.target                       ${D}${sysconfdir}/systemd/system/
    install ${WORKDIR}/owasysd-polluxc-mqtt.service        ${D}${sysconfdir}/systemd/system/

    ln -s -r ${D}/${sysconfdir}/systemd/system/owasysd-polluxc-mqtt.service     ${D}/${sysconfdir}/systemd/system/polluxc-mqtt.service 
}

FILES_${PN} += "${bindir}/polluxc-mqtt"