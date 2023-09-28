#!/bin/bash

cd /home || exit

# Link the desired directory to VFLASH, for example like this:
# ln -s V2.1.3 VFLASH

#echo 1 > /sys/class/leds/ledsw2\:red/brightness
echo timer > /sys/class/leds/ledsw0\:yellow/trigger
echo timer > /sys/class/leds/ledsw1\:green/trigger
echo timer > /sys/class/leds/ledsw2\:red/trigger
echo timer > /sys/class/leds/ppsgps\:orange/trigger
#sleep 10
./flash.sh VFLASH/
rc=$?
if [ ${rc} -ne 0 ]; then
   #echo heartbeat > /sys/class/leds/ledsw2:red/trigger
   echo 0 > /sys/class/leds/ledsw0\:yellow/brightness
   echo 0 > /sys/class/leds/ledsw1\:green/brightness
   echo 0 > /sys/class/leds/ledsw2\:red/brightness
   echo 0 > /sys/class/leds/ppsgps\:orange/brightness
   echo 1 > /sys/class/leds/ledsw2\:red/brightness
fi

MOUNTED_POINT="/tmp/fs"
OS_RELEASE_DIR="$MOUNTED_POINT/etc"
OS_RELEASE="$OS_RELEASE_DIR/os-release"

MTDNAME=$(cat /proc/mtd | grep file-systemA | cut -d : -f 1 | xargs basename)
rc=$?
if [ ${rc} -ne 0 ]; then
   echo "ERROR ${rc}, partition not found"
   exit 1
fi

echo "PARTITION file-systemA:$MTDNAME"
ubiattach /dev/ubi_ctrl -p /dev/$MTDNAME
rc=$?
if [ ${rc} -ne 0 ]; then
   echo "Attach FS A error ${rc}"
   exit 1
fi

mount -t ubifs ubi0 $MOUNTED_POINT
rc=$?
if [ ${rc} -ne 0 ]; then
   echo "Mount FS A error ${rc}"
   ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
   exit 1
fi

if [ -f "$OS_RELEASE" ]; then

   echo "File $OS_RELEASE exists in FS A"

   # Generate file to be read by odm-agent
   fw_name=$(grep -w META_OWASYS_VARIANT $OS_RELEASE | tr -d '"' )
   fw_name=${fw_name#*=}
   fw_name=${fw_name:-"owa5-yocto-1.0.0"}
   app_name=$(grep -w META_OWASYS_APP $OS_RELEASE | tr -d '"' )
   app_name=${app_name#*=}
   app_name=${app_name:-"odm"}
   package_name="$fw_name-$app_name"
   package_version=$(grep -w META_OWASYS_APP_VERSION_ID $OS_RELEASE | tr -d '"' )
   package_version=${package_version#*=}
   package_version=${package_version:-"1.0.1"}
   odm_custom_message="Flashed by uSD"
   if [ -d "$OS_RELEASE_DIR/odm" ]; then
   
      $MOUNTED_POINT/home/scripts/rauc_uboot_envvar_setup.sh		
   	
      odm_telemetry_file="$OS_RELEASE_DIR/odm/odm_telemetry.json"
      jq -c -n --arg custom "$odm_custom_message"  \
         --arg version "$package_version"  \
         --arg title  "$package_name"  '[{
         "fw_state": $custom,
         "current_fw_title": $title,
         "current_fw_version": $version
      }]' | tr -d '\n' > $odm_telemetry_file
   fi

else
   echo "File FS A $PACKAGE_JSON does not exist"
fi

umount $MOUNTED_POINT
ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME

# flashing finished, switch green led instead
#echo 0 > /sys/class/leds/ledsw2\:red/brightness
#echo 1 > /sys/class/leds/ledsw1\:green/brightness
echo 0 > /sys/class/leds/ledsw0\:yellow/brightness
echo 0 > /sys/class/leds/ledsw1\:green/brightness
echo 0 > /sys/class/leds/ledsw2\:red/brightness
echo 0 > /sys/class/leds/ppsgps\:orange/brightness
echo 1 > /sys/class/leds/ledsw1\:green/brightness

exit 0