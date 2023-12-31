DESCRIPTION = "i.MX ARM Trusted Firmware for the Owa5x Platform"
SECTION = "BSP"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

PROVIDES = "imx-atf-owasys"

PV .= "+git${SRCPV}"

ATF_SRC ?= "git://github.com/nxp-imx/imx-atf.git;protocol=https"
SRC_URI = "${ATF_SRC};branch=${SRCBRANCH} \
           file://0001-platform.mk-changed-UART-base-address.patch \
"
SRCBRANCH = "lf_v2.4"
SRCREV = "5782363f92a2fdf926784449270433cf3ddf44bd"

S = "${WORKDIR}/git"

inherit deploy

BOOT_TOOLS = "imx-boot-tools"

ATF_PLATFORM        ?= "INVALID"
ATF_PLATFORM_mx8mp   = "imx8mp"
ATF_PLATFORM_owa5x   = "imx8mp"

EXTRA_OEMAKE += " \
    CROSS_COMPILE="${TARGET_PREFIX}" \
    PLAT=${ATF_PLATFORM} \
"

BUILD_OPTEE = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'true', 'false', d)}"

do_compile() {
    # Clear LDFLAGS to avoid the option -Wl recognize issue
    unset LDFLAGS
    oe_runmake bl31
    if ${BUILD_OPTEE}; then
       oe_runmake clean BUILD_BASE=build-optee
       oe_runmake BUILD_BASE=build-optee SPD=opteed bl31
    fi
}

do_install[noexec] = "1"

do_deploy() {
    install -Dm 0644 ${S}/build/${ATF_PLATFORM}/release/bl31.bin ${DEPLOYDIR}/${BOOT_TOOLS}/bl31-${ATF_PLATFORM}.bin
    if ${BUILD_OPTEE}; then
       install -m 0644 ${S}/build-optee/${ATF_PLATFORM}/release/bl31.bin ${DEPLOYDIR}/${BOOT_TOOLS}/bl31-${ATF_PLATFORM}.bin-optee
    fi
}
addtask deploy after do_compile

PACKAGE_ARCH = "${MACHINE_SOCARCH}"
COMPATIBLE_MACHINE = "(owa5x)"
