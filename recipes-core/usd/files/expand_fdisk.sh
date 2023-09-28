#!/bin/bash

retcode=0

echo "Expand eMMC partitions"
echo "
d
n
p
1
2048
12000000
N
n
p
2


N
w" | fdisk /dev/mmcblk2

   e2fsck -f /dev/mmcblk2p1
   resize2fs /dev/mmcblk2p1
   echo "y" | mkfs.ext4 /dev/mmcblk2p2
   

   MTDNAME=$(cat /proc/mtd | grep file-systemA | cut -d : -f 1 | xargs basename)
   rc=$?
   if [ ${rc} -ne 0 ]
   then
      echo "ERROR ${rc}, partition FS A not found"
      retcode=1
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
            echo "/dev/mmcblk2p2    /data_backup/   ext4          defaults         0      0" >> /tmp/fs/etc/fstab
            echo "FS A /etc/fstab updated"
            mkdir /tmp/fs/data_backup
            
            umount /tmp/fs
            ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
         else
            echo "Mount FS A error ${rc}"
            ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
            retcode=1
         fi
      else
         echo "Attach FS A error ${rc}"
         retcode=1
      fi
   fi

   MTDNAME=$(cat /proc/mtd | grep file-systemB | cut -d : -f 1 | xargs basename)
   rc=$?
   if [ ${rc} -ne 0 ]
   then
      echo "ERROR ${rc}, partition FS B not found"
      retcode=1
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
            echo "/dev/mmcblk2p2    /data_backup/   ext4          defaults         0      0" >> /tmp/fs/etc/fstab
            echo "FS B /etc/fstab updated"
            mkdir /tmp/fs/data_backup
            
            umount /tmp/fs
            ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
         else
            echo "Mount FS B error ${rc}"
            ubidetach /dev/ubi_ctrl -p /dev/$MTDNAME
            retcode=1
         fi
      else
         echo "Attach FS B error ${rc}"
         retcode=1
      fi
   fi
   
   if [ $retcode -eq 1 ]
   then
      exit 1
   fi
   
   exit 0
   
