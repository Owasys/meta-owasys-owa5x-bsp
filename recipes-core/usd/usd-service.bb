LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"
inherit systemd

SRC_URI:append= " file://usd.service"

DEPENDS += " \
    usd \
"
RDEPENDS:${PN} += "\
			      usd \
"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${PN} ="usd.service"

do_install:append() {
  install -d ${D}${sysconfdir}/systemd/system
  install -m 0644 ${WORKDIR}/usd.service ${D}/${sysconfdir}/systemd/system
}

FILES:${PN} += "${sysconfdir}/systemd/system/usd.service"