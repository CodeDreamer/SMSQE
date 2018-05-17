; IO Utilities Find Def Block   V2.00   1990   Tony Tebby QJUMP

        section iou

        xdef    iou_fdef

        include 'dev8_keys_sys'
        include 'dev8_keys_iod'
        include 'dev8_keys_err'
;+++
; IO Utilities find drive definition block given driver linkage and drive
; number
;
;       d0  r   drive ID or error
;       d6 c  p drive number msw
;       a2  r   pointer to drive table (+4)
;       a3 c  p linkage block
;       a4  r   pointer to definition block
;       all other registers preserved
;       status 0 or ERR.FDNF
;---
iou_fdef
        move.l  a1,-(sp)
        swap    d6
        lea     iod_iolk(a3),a1          ; io linkage
        moveq   #0,d0                    ; drive id
        lea     sys_fsdd(a6),a2
ifd_fdrv
        move.l  (a2)+,a4
        cmp.l   iod_drlk(a4),a1          ; our driver?
        bne.s   ifd_next                 ; ... no
        cmp.b   iod_dnum(a4),d6          ; our drive?
        beq.s   ifd_exit                 ; ... yes
ifd_next
        addq.b  #1,d0
        cmp.b   #16,d0
        blt.s   ifd_fdrv                 ; next

        moveq   #err.fdnf,d0             ; no dd linkage

ifd_exit
        move.l  (sp)+,a1
        swap    d6
        tst.l   d0
        rts
        end
