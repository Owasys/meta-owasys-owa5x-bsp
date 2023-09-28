LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"

INSANE_SKIP:${PN} += "already-stripped"
DEPENDS = "dbus owasys-libpollux paho-mqtt-c "
RDEPENDS:${PN} = "dbus libev owasys-libpollux hiredis "

SRC_URI = " file://mqtt.json \
            file://pollux-dbus-mqtt.json \
            file://polluxc-mqtt \
"

do_install() {

    # Install binaries
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/polluxc-mqtt ${D}${bindir}

    # Install configuration files
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}/pollux
    install -d ${D}${sysconfdir}/pollux.d
    
    install ${WORKDIR}/mqtt.json                    ${D}${sysconfdir}/pollux/
    install ${WORKDIR}/pollux-dbus-mqtt.json        ${D}${sysconfdir}/pollux/pollux.d
}

FILES_${PN} += "${bindir}/polluxc-mqtt"