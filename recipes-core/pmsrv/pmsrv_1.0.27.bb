DESCRIPTION = "Binary for pmsrv.service"
SECTION = "Binaries"
DEPENDS = ""
LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"
FILESEXTRAPATHS:prepend := "${THISDIR}/src:"

SRC_URI = "file://pmsrv \
           file://pmsrv.json \
           file://hosts.backup \
"

S = "${WORKDIR}"

INSANE_SKIP:${PN} += "already-stripped"

DEPENDS += " dbus owasys-libpollux "

do_install() {
    # create the /usr/bin folder in the rootfs with default permissions
    install -d ${D}${bindir}
    install -m 0755 -d ${D}${sysconfdir}
    install -m 0755 -d ${D}${sysconfdir}/pmsrv

    # install the application into the /usr/bin folder with default permissions
    install ${WORKDIR}/pmsrv ${D}${bindir}
    install ${WORKDIR}/pmsrv.json ${D}${sysconfdir}/pmsrv
    install ${WORKDIR}/hosts.backup ${D}${sysconfdir}/pmsrv
}
