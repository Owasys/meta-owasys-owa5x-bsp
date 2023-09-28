#!/bin/bash

BackUpCopy=0
MainCopy=0

if [ "$1" ]
then

   if [ ! -d '/tmp/fs' ]; then
      echo "Create /tmp/fs DIR"
      mkdir -p /tmp/fs
   fi

   MTDNAME=$(cat /proc/mtd | grep file-systemB | cut -d : -f 1 | xargs basename)
   rc=$?
   if [ ${rc} -ne 0 ]
   then
      echo "ERROR ${rc}, partition not found"
   else
      echo "PARTITION file-systemB:$MTDNAME"
      ubiattach /dev/ubi_ctrl -p /dev/$MTDNAME
      rc=$?
      if [ ${rc} -eq 0 ]
      then
         mount -t ubifs ubi0 /tmp/fs
         rc=$?
         if [ ${rc} -eq 0 ]
         then
            if [ -f '/tmp/fs/etc/default/hw.json' ];
            then
               echo "Save file FS B /etc/default/hw.json"
               cp /tmp/fs/etc/default/hw.json ./hw_b.json
               BackUpCopy=1
            else
               echo "File FS B /etc/default/hw.json does not exist"
            fi
            umount /tmp/fs
            ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
         else
            echo "Mount FS B error ${rc}"
            ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
         fi
      else
         echo "Attach FS B error ${rc}"
      fi
   fi

   MTDNAME=$(cat /proc/mtd | grep file-systemA | cut -d : -f 1 | xargs basename)
   rc=$?
   if [ ${rc} -ne 0 ]
   then
      echo "ERROR ${rc}, partition not found"
   else
      echo "PARTITION file-systemA:$MTDNAME"
      ubiattach /dev/ubi_ctrl -p /dev/$MTDNAME
      rc=$?
      if [ ${rc} -eq 0 ]
      then
         mount -t ubifs ubi0 /tmp/fs
         rc=$?
         if [ ${rc} -eq 0 ]
         then
            if [ -f '/tmp/fs/etc/default/hw.json' ];
            then
               echo "Save file FS A /etc/default/hw.json"
               cp /tmp/fs/etc/default/hw.json ./hw_m.json
               MainCopy=1
            else
               echo "File FS A /etc/default/hw.json does not exist"
            fi
            umount /tmp/fs
            ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
         else
            echo "Mount Main error ${rc}"
            ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
         fi
      else
         echo "Attach Main error ${rc}"
      fi
   fi

   MTDNAME=$(cat /proc/mtd | grep u-boot\" | cut -d : -f 1 | xargs basename)
   rc=$?
   if [ ${rc} -ne 0 ]
   then
      echo "ERROR ${rc}, partition u-boot not found"
      exit 1
   else
      echo "PARTITION u-boot:$MTDNAME"       
      kobs-ng init -v --chip_0_device_path=/dev/$MTDNAME --secondary_boot_stream_off_in_MB=4 $1"flash.bin"
      rc=$?
      if [ ${rc} -ne 0 ]
      then
         echo "ERROR ${rc} flashing u-boot"
         exit 1
      else
         echo "Flash u-boot OK"
      fi
   fi

   MTDNAME=$(cat /proc/mtd | grep u-boot-env | cut -d : -f 1 | xargs basename)
   rc=$?
   if [ ${rc} -ne 0 ]
   then
      echo "ERROR ${rc}, partition u-boot-env not found"
      exit 1
   else
      echo "PARTITION u-boot-env:$MTDNAME"
      flash_erase /dev/$MTDNAME 0 0 
      rc=$?
      if [ ${rc} -ne 0 ]
      then
         echo "ERROR ${rc} ERASING u-boot-env"
         exit 1
      else
         echo "ERASE u-boot-env OK"
      fi
   fi

   MTDNAME=$(cat /proc/mtd | grep u-boot-env.backup | cut -d : -f 1 | xargs basename)
   rc=$?
   if [ ${rc} -ne 0 ]
   then
      echo "ERROR ${rc}, partition u-boot-env.backup not found"
      exit 1
   else
      echo "PARTITION u-boot-env.backup:$MTDNAME"
      flash_erase /dev/$MTDNAME 0 0 
      rc=$?
      if [ ${rc} -ne 0 ]
      then
         echo "ERROR ${rc} ERASING u-boot-env.backup"
         exit 1
      else
         echo "ERASE u-boot-env.backup OK"
      fi
   fi

   #Delete environment variables if needed
#   MTDNAME=$(cat /proc/mtd | grep u-boot-env\" | cut -d : -f 1 | xargs basename)
#   rc=$?
#   if [ ${rc} -ne 0 ]
#   then
#      echo "ERROR ${rc}, partition u-boot-env not found"
#      exit
#   else
#      flash_erase /dev/$MTDNAME 0 0 
#   fi
   #Delete backup environment variables if needed
#   MTDNAME=$(cat /proc/mtd | grep u-boot-env.backup\" | cut -d : -f 1 | xargs basename)
#   rc=$?
#   if [ ${rc} -ne 0 ]
#   then
#      echo "ERROR ${rc}, partition u-boot-env.backup not found"
#      exit
#   else
#      flash_erase /dev/$MTDNAME 0 0 
#   fi

   MTDNAME=$(cat /proc/mtd | grep file-systemB | cut -d : -f 1 | xargs basename)
   rc=$?
   if [ ${rc} -ne 0 ]
   then
      echo "ERROR ${rc}, partition FS B not found"
      exit 1
   else
      echo "PARTITION file-systemB:$MTDNAME"
      flash_erase /dev/$MTDNAME 0 0 
      rc=$?
      if [ ${rc} -ne 0 ]
      then
         echo "ERROR ${rc} ERASING FS B"
         exit 1
      else
         echo "ERASE FS B OK"
      fi
      nandwrite -p /dev/$MTDNAME $1"nand_image"
      rc=$?
      if [ ${rc} -ne 0 ]
      then
         echo "ERROR ${rc} FLASING FS B"
         exit 1
      else
         echo "FLASH FS B OK"
      fi
   fi

   if [ $BackUpCopy -eq 1 -o $MainCopy -eq 1 ]
   then
      MTDNAME=$(cat /proc/mtd | grep file-systemB | cut -d : -f 1 | xargs basename)
      rc=$?
      if [ ${rc} -ne 0 ]
      then
         echo "ERROR ${rc}, partition FS B not found"
      else
         echo "PARTITION file-systemB:$MTDNAME"
         ubiattach /dev/ubi_ctrl -p /dev/$MTDNAME
         rc=$?
         if [ ${rc} -eq 0 ]
         then
            mount -t ubifs ubi0 /tmp/fs
            rc=$?
            if [ ${rc} -eq 0 ]
            then
               if [ ! -d '/tmp/fs/etc/default' ];
               then
                  echo "Create /etc/default DIR"
                  mkdir /tmp/fs/etc/default
               fi
               if [ -f './hw_b.json' ];
               then
                  echo "Copy file B /etc/default/hw.json"
                  cp ./hw_b.json /tmp/fs/etc/default/hw.json
               else
                  echo "File B does not exist"
                  if [ -f './hw_m.json' ];
                  then
                     echo "Copy file A to B /etc/default/hw.json"
                     cp ./hw_m.json /tmp/fs/etc/default/hw.json
                  else
                     echo "File A does not exist"
                  fi
               fi
               umount /tmp/fs
               ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
            else
               echo "Mount FS B error ${rc}"
               ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
            fi
         else
            echo "Attach FS B error ${rc}"
         fi
      fi
   fi

   MTDNAME=$(cat /proc/mtd | grep file-systemA\" | cut -d : -f 1 | xargs basename)
   rc=$?
   if [ ${rc} -ne 0 ]
   then
      echo "ERROR ${rc}, partition FS A not found"
      exit 1
   else
      echo "PARTITION file-systemA:$MTDNAME"
      flash_erase /dev/$MTDNAME 0 0 
      rc=$?
      if [ ${rc} -ne 0 ]
      then
         echo "ERROR ${rc} ERASING FS A"
         exit 1
      else
         echo "ERASE FS A OK"
      fi
      nandwrite -p /dev/$MTDNAME $1"nand_image"
      rc=$?
      if [ ${rc} -ne 0 ]
      then
         echo "ERROR ${rc} FLASING FS A"
         exit 1
      else
         echo "FLASH FS A OK"
      fi
   fi

   if [ $BackUpCopy -eq 1 -o $MainCopy -eq 1 ]
   then
      MTDNAME=$(cat /proc/mtd | grep file-systemA\" | cut -d : -f 1 | xargs basename)
      rc=$?
      if [ ${rc} -ne 0 ]
      then
         echo "ERROR ${rc}, partition FS A not found"
      else
         echo "PARTITION file-system:$MTDNAME"
         ubiattach /dev/ubi_ctrl -p /dev/$MTDNAME
         rc=$?
         if [ ${rc} -eq 0 ]
         then
            mount -t ubifs ubi0 /tmp/fs
            rc=$?
            if [ ${rc} -eq 0 ]
            then
               if [ ! -d '/tmp/fs/etc/default' ];
               then
                  echo "Create /etc/default DIR"
                  mkdir /tmp/fs/etc/default
               fi 

               if [ $MainCopy -eq 1 ]
               then
                  echo "Copy FS A /etc/default/hw.json"
                  cp ./hw_m.json /tmp/fs/etc/default/hw.json
               else
                  if [ $BackUpCopy -eq 1 ]
                  then
                     echo "Copy FS B to FS A /etc/default/hw.json"
                     cp ./hw_b.json /tmp/fs/etc/default/hw.json
                  fi
               fi
               umount /tmp/fs
               ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
            else
               echo "Mount FS A error ${rc}"
               ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
            fi
         else
            echo "Attach FS A error ${rc}"
         fi
      fi
   fi

   if [ -f './hw_b.json' ];
   then
      rm ./hw_b.json
   fi

   if [ -f './hw_m.json' ];
   then
      rm ./hw_m.json
   fi

else
   echo "syntax: flash dir"
   exit 1
fi

exit 0
