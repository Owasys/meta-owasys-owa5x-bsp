LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"
inherit systemd

RDEPENDS:${PN} += "bash"
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${PN} ="owasysd-first-boot-operations.service"

SRC_URI:append = " \
  file://owasysd-first-boot-operations.service \
  file://first-boot-operations.sh \
"

do_install:append() {
  install -d ${D}${bindir}
  install -d ${D}${sysconfdir}/systemd/system
  
  install -m 0644 ${WORKDIR}/owasysd-first-boot-operations.service ${D}${sysconfdir}/systemd/system
  install -m 0755 ${WORKDIR}/first-boot-operations.sh ${D}${bindir}
}

FILES:${PN} += "${sysconfdir}/systemd/owasysd-first-boot-operations.service"
