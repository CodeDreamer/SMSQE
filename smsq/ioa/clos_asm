; Close device or file     V2.00    1986  Tony Tebby  QJUMP

        section ioa

        xdef    ioa_clos
        xdef    ioa_topc

        xref    io_ckchn
        xref    sms_rte

        include 'dev8_keys_iod'
        include 'dev8_keys_sys'
;+++
;       d0  r   error return 0, or not found
;       a0 c s  channel id
;       a6 c  p base of system variables
;
;       all other registers preserved
;---
ioa_clos
reglist reg     d1-d6/a1-a4
        movem.l reglist,-(sp)
        bsr.l   io_ckchn                 ; check the channel ID (a0,a3)
        bne.s   icl_exit
        move.l  iod_clos(a3),a4
        jsr     (a4)                     ; and close channel

        bsr.s   ioa_topc                 ; reset top channel number

        moveq   #0,d0

icl_exit
        movem.l (sp)+,reglist
        bra.l   sms_rte

;+++
; Reset top channel number
;
;       d0  r   new top channel number
;       a1   s
;       a6      sysvar
;---
ioa_topc
        move.w  sys_chtp(a6),d0          ; current top channel
        ble.s   itc_set                  ; none or only one
        move.w  d0,a1
        add.l   a1,a1
        add.l   a1,a1
        add.l   sys_chtb(a6),a1          ; highest in channel table
        addq.l  #4,a1                    ; highest + 1
itc_loop
        tst.l   -(a1)                    ; vacant now?
        dbpl    d0,itc_loop
itc_set
        move.w  d0,sys_chtp(a6)
        rts
        end
