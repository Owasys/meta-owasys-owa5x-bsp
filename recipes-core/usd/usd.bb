LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"

SRC_URI = " file://custom_actions.sh \
            file://flash.sh \
            file://expand_fdisk.sh \
            file://owa5x-boot.script \
"

# The image that we want to flash
do_configure[depends] += "owa5-image-nand:do_image_complete"

RDEPENDS:${PN} += "bash"

do_install() {

  install -d ${D}/home

  install -m 0755 ${WORKDIR}/custom_actions.sh ${D}/home
  install -m 0755 ${WORKDIR}/flash.sh ${D}/home
  install -m 0755 ${WORKDIR}/expand_fdisk.sh ${D}/home

  install -d ${D}/home/VFLASH

  install -m 0644 ${DEPLOY_DIR_IMAGE}/owa5x-flash.bin ${D}/home/VFLASH/flash.bin
  install -m 0644 ${DEPLOY_DIR_IMAGE}/owa5-image-nand-owa5x.ubi ${D}/home/VFLASH/nand_image

  # This is to use IMAGE_BOOT_FILES:append in owa5-booteable-usd.bb (the file must be in ${DEPLOY_DIR_IMAGE} to use it)
  install -m 0644 ${WORKDIR}/owa5x-boot.script ${DEPLOY_DIR_IMAGE}/owa5x-boot.script
}

FILES:${PN} += " \
  /home \
  /home/VFLASH \
"