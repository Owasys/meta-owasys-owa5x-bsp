DESCRIPTION = "mlanutl - configure the additional parameters available for NXP mdriver"
SECTION = "Binaries"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"
FILESEXTRAPATHS:prepend := "${THISDIR}/src:"
SRC_URI = " file://mlanutl \
"

S = "${WORKDIR}"

INSANE_SKIP:${PN} += "already-stripped"

do_install() {
    # create the /usr/bin folder in the rootfs with default permissions
    install -d ${D}${bindir}
    
    # install the application into the /usr/bin folder with default permissions
    install -m 0755 ${WORKDIR}/mlanutl ${D}${bindir}

    # create alias for this tool
    install -d ${D}/${base_bindir}
    ln -s ${bindir}/mlanutl    ${D}${base_bindir}/owasys-mlanutl
}