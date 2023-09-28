SUMMARY = "Owasys owa5X YOCTO image flasheable by usd"
IMAGE_LINGUAS = "en-us"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM ="file://${COMPANY_CUSTOM_LICENSES}/OWASYS_Propietary_SW_License_Agreement.md;md5=203a753c44e11367199c31c2168fa959"

inherit core-image image_types 

DEPENDS += " owa5-image-nand "

# Set rootfs to 460MiB by default
IMAGE_OVERHEAD_FACTOR ?= "1.0"
IMAGE_ROOTFS_SIZE ?= "460000"
IMAGE_FSTYPES += " wic "
WKS_FILE = "owa5-bootable-usd.wks"

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL} kernel-devicetree"
IMAGE_INSTALL += "  systemd-analyze iw mtd-utils mtd-utils-ubifs openssh sudo \
                    alsa-utils iproute2 net-tools e2fsprogs iptables \
                    imx-kobs inetutils libev e2fsprogs-resize2fs \
                    packagegroup-core-base-utils owasys-libs jq \
                    opensc u-boot-fw-utils lrzsz vim pmsrv-service \
                    kernel-modules usd usd-service u-boot-owasys \
                    imx-boot-owasys \
"

hostname:pn-base-files = ""
inherit extrausers

# Encrypt the password 'example', using $mkpasswd -m sha-512 example -s "11223344"
EXTRA_USERS_PARAMS = "\
    usermod -p '\$6\$75920571\$AWaW0t3hK.zgl8TVCxyvhfw3xMv0S4SmMvkUgS0lQF5Sq2M8E6qAemab22iD/a.EryeFPz.Eym/Vn2Fzazvef.' root; \
	useradd -p '\$6\$75920571\$KFBliyUh3yluZQbWWpY8jDgASgj1UgB8HsYjLSySKToJPATn0yQGPiyz.zEdDJtAMBxdDIPqhuSUxs.Y1eVor1' owasys; \
	usermod -aG sudo owasys; \
"

# Here we give sudo access to sudo members
update_sudoers(){
    sed -i 's/# %sudo/%sudo/' ${IMAGE_ROOTFS}/etc/sudoers
}

ROOTFS_POSTPROCESS_COMMAND += "update_sudoers;"

IMAGE_BOOT_FILES:append = " owa5x-boot.script"
