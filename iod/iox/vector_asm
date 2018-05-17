; Directory Device Dummy Vector Routines V2.01    1988   Tony Tebby QJUMP

        section iox

        xdef    iox_chek
;+++
; Directory Device check slave blocks
;
;       d6 c  p drive / file number
;       a0 c  p channel block
;       a3 c  p linkage block
;       a4 c s  definition block
;
;       status return standard
;---
iox_chek
        jmp     iox_flsh          ; just flush

        xdef    iox_flsh
;+++
; Directory Device flush slave blocks and map
;
;       d3 c  p 0 first entry or close / nz subsequent entries
;       a3 c  p linkage block
;
;       status return standard
;---
iox_flsh


        xdef    iox_occi
;+++
; Directory Device occupancy information.
; Finds empty / usable sectors, sets locations in drive definition block
; and sets d1 to QDOS compatible values.
;
;       d1  r   msw empty sectors / lsw total usable sectors (QDOS compatible)
;       a3 c  p linkage block
;
;       status return standard
;---
iox_occi


        xdef    iox_load
;+++
; Directory Device Scatter Load
;
;       status return standard
;---
iox_load


        xdef    iox_save
;+++
; Directory Device scatter save
;
;       status return standard
;---
iox_save


        xdef    iox_trnc
;+++
; Directory Device truncate file by freeing all sectors beyond new end of file.
;
;       d5 c  p file position (0 remove all but first allocation)
;                             (-1 remove all)
;       d6 c  p drive / file number
;       a0 c  p channel block
;       a3 c  p linkage block
;       a4 c  p definition block
;
;       status return standard
;---
iox_trnc


        xdef    iox_lcbf
;+++
; Directory Device locates a buffer for read or write operations.
; If necessary, it reads the appropriate sector from the device.
;
;       d3  r   (word) bytes left in buffer
;       d4  r   block/byte
;       d5 c  p file position
;       d6 c  p drive / file number
;       a0 c  p channel block
;       a2  r   pointer to buffer
;       a3 c  p linkage block
;       a4 c  p definition block
;
;       status return standard
;---
iox_lcbf


        xdef    iox_albf
;+++
; Directory Device locates or allocates a buffer for write operations.
; If necessary, it reads the appropriate sector from the device or extends the
; file allocation.
;
;       d3  r   (word) bytes left in buffer
;       d4  r   block/byte
;       d5 c  p file position
;       d6 c  p drive / file number
;       a0 c  p channel block
;       a2 c  p pointer to buffer
;       a3 c  p linkage block
;       a4 c  p definition block
;
;       status return standard
;---
iox_albf


        xdef    iox_upbf
;+++
; This marks the buffer (actually a slave block) updated
;
;       a0 c  p channel block
;       a3 c  p pointer to linkage block
;       a4 c  p pointer to physical definition
;
;       status return 0
;---
iox_upbf

        xdef    iox_alfs
;+++
; This allocates or re-allocates the first sector of a file (if necessary) and
; returns the (new) file ID for future identification.
;
;       d1 cr   old file ID (or zero) / new file ID (word)
;       d5 c  p sub-directory pointer
;       d6 c  p drive / sub-directory ID
;       a0 c  p channel block (looks like directory)
;       a2  r   pointer to buffer
;       a3 c  p device driver linkage
;       a4 c  p drive definition
;
;       status return standard
;
iox_alfs


        xdef    iox_ckop
;+++
; This checks the Directory Device before an open operation.
; If the medium is changed, it should set up IOD_MNAM, IOD_WPRT, IOD_FTYP,
; IOD_RDLN, IOD_RDID and IOD_HDRL. If the file name is a specialname for
; direct sector IO. The sector length flag should set in the lsw of d6, and 
; d0 returned positive. The IOU_OPEN routine will check whether there are
; any files open and set the appropriate flags.
;
;       d6 cr p drive number / zero or sector length flag (direct sector open)
;       a0 c  p channel block
;       a3 c  p pointer to linkage block
;       a4 c  p pointer to physical definition
;
;       status return 0, 1 or err.mchk
;---
iox_ckop

        xdef    iox_fdrv
;+++
; This formats the Directory Device
;
;       d1 cr   drive number / good groups
;       d2  r   total groups
;       a1 c s  medium name  
;       a3 c  p pointer to linkage block
;       other registers scratch
;       status return 0, err.fdnf or err.fmtf
;---
iox_fdrv

        xdef    iox_rsec
;+++
; This reads a sector from the medium (direct read, retries if needed)
;
;       d1 c s  drive number (1 to ...)
;       d2 c s  head number (0 to ...)
;       d3 c s  cylinder number (0 to ...)
;       d4 c s  sector number (0 to ...)
;       d5   s
;       d6 c  p word sector length (0:128 1:256 2:512 3:1024 etc.)
;       d7   s
;       a0 c  p pointer to channel block 
;       a1 cr   pointer to buffer
;       a2   s
;       a3 c  p pointer to linkage block   
;       a4 c  p pointer to physical definition
;       a5   s
;---
iox_rsec

        xdef    iox_wsec
;+++
; This writes a sector from the medium (direct write, retries if needed)
;
;       d1 c s  drive number (1 to ...)
;       d2 c s  head number (0 to ...)
;       d3 c s  cylinder number (0 to ...)
;       d4 c s  sector number (0 to ...)
;       d5   s
;       d6 c  p word sector length (0:128 1:256 2:512 3:1024 etc.)
;       d7   s
;       a0 c  p pointer to channel block 
;       a1 cr   pointer to buffer
;       a2   s
;       a3 c  p pointer to linkage block   
;       a4 c  p pointer to physical definition
;       a5   s
;---
iox_wsec

        end
