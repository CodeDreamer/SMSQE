; HOTKEY find next channel with same driver V2.00    1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_nxchn

        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_sys'
        include 'dev8_keys_chn'
        include 'dev8_keys_err'

;+++
; This routine takes a given channel ID and looks for the next channel with
; the same driver
;
;       a0 cr p channel ID / next channel ID
;       error returns 0 or err.fdnf
;---
hk_nxchn
reglist reg     d1-d3/d7/a1
        movem.l reglist,-(sp)
        move.w  sr,d7
        trap    #0
        move.l  a0,d3
        moveq   #sms.info,d0             ; get sysvar
        trap    #do.sms2

        move.w  d3,d2                    ; starting channel number
        swap    d3                       ; tag
        move.w  sys_chtp(a0),d1          ; highest number
        move.l  sys_chtb(a0),a0          ; channel base

        move.w  d2,d0                    ; initial channel
        lsl.w   #2,d0
        move.l  (a0,d0.w),d0             ; still open?
        blt.s   hnc_fdnf
        move.l  d0,a1
        cmp.w   chn_tag(a1),d3           ; correct tag?
        bne.s   hnc_fdnf
        move.l  chn_drvr(a1),d3          ; keep driver

hnc_next
        addq.w  #1,d2                    ; next channel
        cmp.w   d1,d2                    ; all checked?
        bgt.s   hnc_fdnf                 ; ... yes

        move.w  d2,d0
        lsl.w   #2,d0
        move.l  (a0,d0.w),d0             ; channel open?
        blt.s   hnc_next
        move.l  d0,a1
        cmp.l   chn_drvr(a1),d3          ; the right driver?
        bne.s   hnc_next                 ; ... no
        move.l  chn_tag(a1),d0
        move.w  d2,d0
        move.l  d0,a0                    ; next channel
        moveq   #0,d0
        bra.s   hnc_exit

hnc_fdnf
        moveq   #err.fdnf,d0
hnc_exit
        move.w  d7,sr
        movem.l (sp)+,reglist
        tst.l   d0
        rts
        end
