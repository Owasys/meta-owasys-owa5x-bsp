LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"

SRC_URI = " file://ppppty \
            file://default \
            file://ip-up \
            file://0dns-up \
"
do_install() {
  
  install -m 0755 -d ${D}${sysconfdir}/ppp

  if ![ -d "${D}${sysconfdir}/ppp/peers" ]; then
    bbnote " ppp/peers directory doesn't exist, creating it..."
    mkdir ${D}${sysconfdir}/ppp/peers
  fi

  mkdir ${D}${sysconfdir}/ppp/peers
  mkdir ${D}${sysconfdir}/ppp/ip-up.d


#  install ${WORKDIR}/resolv.conf ${D}${sysconfdir}/
  install ${WORKDIR}/ip-up ${D}${sysconfdir}/ppp/
  install ${WORKDIR}/0dns-up ${D}${sysconfdir}/ppp/ip-up.d/
  install ${WORKDIR}/ppppty ${D}${sysconfdir}/ppp/peers/
  install ${WORKDIR}/default ${D}${sysconfdir}/ppp/peers/
}
