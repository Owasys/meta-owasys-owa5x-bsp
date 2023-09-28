DESCRIPTION = "Shared libraries needed to control GPS, GSM, etc"
SECTION = "Binaries"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"
FILESEXTRAPATHS:prepend := "${THISDIR}/src:"
COMPATIBLE_MACHINE = "(owa5x)"

SRC_URI = " file://FMS_Defs.h \
            file://GPS2_ModuleDefs.h \
            file://GSM_ModuleDefs.h \
            file://INET_ModuleDefs.h \
            file://IOs_ModuleDefs.h \
            file://owa5x_gpio.h \
            file://owcomdefs.h \
            file://owerrors.h \
            file://pm_messages.h \
            file://RTUControlDefs.h \
"

S = "${WORKDIR}"

INSANE_SKIP:${PN} += "already-stripped dev-so"
FILES:${PN} += " \
        ${libdir}/* \
        ${nonarch_base_libdir}/* \
"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""


do_install() {

   install -d ${D}${includedir}/owa5x

    # create the /lib/ folder in the rootfs with default permissions
    mkdir -p ${D}${includedir}/owa5x
    install -m 0755 -d ${D}${includedir}/owa5x

    # install the application into the /lib/ folder with default permissions
    install ${WORKDIR}/*.h ${D}${includedir}/owa5x

}
