DESCRIPTION = "Shared libraries needed to control GPS, GSM, etc"
SECTION = "Binaries"
DEPENDS = " alsa-lib"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"
FILESEXTRAPATHS:prepend := "${THISDIR}/src:"
COMPATIBLE_MACHINE = "(owa5x)"

SRC_URI = " file://libFMS_Module.so.1.1.5 \
            file://libGPS2_Module.so.1.0.18 \
            file://libGSM_Module.so.1.0.29 \
            file://libINET_Module.so.1.0.6 \
            file://libIOs_Module.so.1.0.17 \
            file://libRTU_Module.so.1.0.20 \
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
    # create the /lib/ folder in the rootfs with default permissions
    install -d ${D}${libdir}/owasys

    # install the application into the /lib/ folder with default permissions
    install ${WORKDIR}/libFMS_Module.so.1.1.5 ${D}${libdir}/owasys
    install ${WORKDIR}/libGPS2_Module.so.1.0.18 ${D}${libdir}/owasys
    install ${WORKDIR}/libGSM_Module.so.1.0.29 ${D}${libdir}/owasys
    install ${WORKDIR}/libINET_Module.so.1.0.6 ${D}${libdir}/owasys
    install ${WORKDIR}/libIOs_Module.so.1.0.17 ${D}${libdir}/owasys
    install ${WORKDIR}/libRTU_Module.so.1.0.20 ${D}${libdir}/owasys

    # create symbolic links from /usr/lib/owasys to /lib/

    install -d ${D}/${nonarch_base_libdir}
    ln -s -r ${D}/${libdir}/owasys/libFMS_Module.so.1.1.5     ${D}${nonarch_base_libdir}/libFMS_Module.so
    ln -s -r ${D}/${libdir}/owasys/libGPS2_Module.so.1.0.18   ${D}${nonarch_base_libdir}/libGPS2_Module.so
    ln -s -r ${D}/${libdir}/owasys/libGSM_Module.so.1.0.29    ${D}${nonarch_base_libdir}/libGSM_Module.so
    ln -s -r ${D}/${libdir}/owasys/libINET_Module.so.1.0.6    ${D}${nonarch_base_libdir}/libINET_Module.so
    ln -s -r ${D}/${libdir}/owasys/libIOs_Module.so.1.0.17    ${D}${nonarch_base_libdir}/libIOs_Module.so
    ln -s -r ${D}/${libdir}/owasys/libRTU_Module.so.1.0.20    ${D}${nonarch_base_libdir}/libRTU_Module.so
}

RPROVIDES:${PN} += " \
                    libudev.so.1(LIBUDEV_183)(64bit) \
                    libudev.so.1()(64bit) \
                    libRTU_Module.so()(64bit) \
                    libIOs_Module.so()(64bit) "
